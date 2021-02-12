package org.team639.robot.Subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;
import org.team639.lib.math.PID;
import org.team639.lib.math.PIDF;
import org.team639.robot.DriveConstants;

public class DriveTrain implements Subsystem
{
    
    private CANSparkMax leftMaster;
    private CANSparkMax leftServant;
    private CANSparkMax rightMaster;
    private CANSparkMax rightServant;
    private Solenoid transmissionSolenoid;
    
    AHRS gyro = new AHRS(SPI.Port.kMXP);
    
    private Pose2d startPosition = new Pose2d(new Translation2d(0, 0), getHeading());
    private Pose2d pose = startPosition;
    DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Constants.chassisWidth);
    DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(getHeading());
    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(DriveConstants.ksVolts, DriveConstants.kvVoltSecondsSquareMeter, DriveConstants.kaVoltSecondsSquaredPerMeter);
    
    // p ~ 1.36
    //PIDController leftPIDController = new PIDController(.986, 0, 0);
    //PIDController rightPIDController = new PIDController(.986, 0, 0);

    //TODO: Try configuring different PID values for more consistent results
    //PIDController leftPIDController = new PIDController(.0005, 0, 0.0);
    //PIDController rightPIDController = new PIDController(0.0005, 0, 0.0);
    PIDController leftPIDController = new PIDController(.0002, 0, 0.0);
    PIDController rightPIDController = new PIDController(0.0002, 0, 0.0);

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds()
    {
        return new DifferentialDriveWheelSpeeds(leftMaster.getEncoder().getVelocity(), leftMaster.getEncoder().getVelocity());
    }

    public enum DriveMode
    {
        TankDrive,
        ArcadeDrive
    }
    
    public DriveMode mode = DriveMode.ArcadeDrive;
    
    /**
     * Constructor for manually inputting motors, if need be.
     * @param leftMaster The left master motor.
     * @param rightMaster The right master motor.
     */
    public DriveTrain(CANSparkMax leftMaster, CANSparkMax rightMaster)
    {
        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
    }
    
    /**
     * Default constructor, uses motor ID values from Constants class.
     */
    public DriveTrain()
    {
        initializeMotorControllers();
        transmissionSolenoid = new Solenoid(Constants.driveTrainsmissionSolenoidID);
        leftMaster.getEncoder().setPosition(0.0);
        rightMaster.getEncoder().setPosition(0.0);
    }
    
    private void initializeMotorControllers()
    {
        //leftMaster = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
        //rightMaster = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
        
        leftMaster = new CANSparkMax(Constants.leftSparkMasterID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMaster = new CANSparkMax(Constants.rightSparkMasterID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftServant = new CANSparkMax(Constants.leftSparkServantID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightServant = new CANSparkMax(Constants.rightSparkServantID, CANSparkMaxLowLevel.MotorType.kBrushless);

        //leftMaster.getEncoder().setPositionConversionFactor(Constants.driveTrainGearRatio * (6*Math.PI));
        //rightMaster.getEncoder().setPositionConversionFactor(Constants.driveTrainGearRatio * (6*Math.PI));


        leftMaster.restoreFactoryDefaults();
        rightMaster.restoreFactoryDefaults();
        leftServant.restoreFactoryDefaults();
        rightServant.restoreFactoryDefaults();

        leftMaster.setSmartCurrentLimit(40);
        rightMaster.setSmartCurrentLimit(40);
        leftServant.setSmartCurrentLimit(40);
        rightServant.setSmartCurrentLimit(40);
    
        //leftMaster.getEncoder().setPosition(0);
        //rightMaster.getEncoder().setPosition(0);
    
        //rightMaster.setInverted(true);
        //rightServant.setInverted(true);
        
        leftMaster.setInverted(true);
        leftServant.setInverted(true);

        rightServant.follow(rightMaster);
        leftServant.follow(leftMaster);
    
        leftMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
        leftServant.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightServant.setIdleMode(CANSparkMax.IdleMode.kBrake);
        
    }

    public void resetOdometry()
    {
        rightMaster.getEncoder().setPosition(0);
        leftMaster.getEncoder().setPosition(0);
        gyro.reset();
        odometry.resetPosition(startPosition, Rotation2d.fromDegrees(-gyro.getAngle()));
    }

    public void resetEncoders()
    {
        leftMaster.getEncoder();

    }

    public void periodic()
    {
        odometry.update(getHeading(), getLeftPostion(), getRightPostion());
        SmartDashboard.putNumber("Gyro Angle", getHeading().getDegrees());
        SmartDashboard.putNumber("Left Encoder: ", getLeftPostion());
        SmartDashboard.putNumber("Right Encoder: ", getRightPostion());

    }

    public double getLeftPostion()
    {
        return leftMaster.getEncoder().getPosition() * Constants.driveTrainGearRatio * (Units.inchesToMeters(6)*Math.PI);
    }

    public double getRightPostion()
    {
        return rightMaster.getEncoder().getPosition() * Constants.driveTrainGearRatio * (Units.inchesToMeters(6)*Math.PI);
    }
    /**
     * Returns the speeds of the motors stored in a DifferentialDriveWheelSpeeds (in meters/s).
     * @return The speeds of the motors stored in a DifferentialDriveWheelSpeeds (in meters/s).
     */

    public DifferentialDriveWheelSpeeds getSpeeds()
    {
        return new DifferentialDriveWheelSpeeds(
                leftMaster.getEncoder().getVelocity() * Constants.metersToEncoderUnitsScaled / 60,
                rightMaster.getEncoder().getVelocity() * Constants.metersToEncoderUnitsScaled / 60
        );
    }

    
    public DifferentialDriveKinematics getKinematics()
    {
        return kinematics;
    }
    
    public Pose2d getPose()
    {
        //return pose;
        return odometry.getPoseMeters();
    }
    
    public PIDController getLeftPIDController()
    {
        return leftPIDController;
    }
    
    public PIDController getRightPIDController()
    {
        return rightPIDController;
    }
    
    public SimpleMotorFeedforward getFeedForward()
    {
        return feedforward;
    }
    
    /**
     * Returns the heading of the navx from the gyro in degrees.
     * @return The heading of the navx from the gyro in degrees.
     */
    public Rotation2d getHeading()
    {
        return Rotation2d.fromDegrees(-gyro.getAngle());
    }
    
    /**
     * Sets the speeds of the left and right master motors in percentage. Between -1.0 and 1.0.
     * @param leftSpeed The speed to set the left motor.
     * @param rightSpeed The speed to set the right motor.
     */
    public void setSpeeds(double leftSpeed, double rightSpeed)
    {
        leftMaster.set(leftSpeed); rightMaster.set(rightSpeed);
    }
    
    /**
     * Sets the speeds of the left and right master motors with a DifferentialDriveWheelSpeeds.
     * @param speeds The DifferentialDriveWheelSpeeds containing the speeds to set the motors.
     */
    public void setSpeeds(DifferentialDriveWheelSpeeds speeds)
    {
        //leftMaster.set(speeds.leftMetersPerSecond * Constants.metersToEncoderUnitsScaled);
        //rightMaster.set(speeds.rightMetersPerSecond * Constants.metersToEncoderUnitsScaled);
    }
    
    /**
     * Sets the voltages of the left and right motors.
     * @param leftVoltage The voltage to set the left motor.
     * @param rightVoltage The voltage to set the right motor.
     */
    public void setVoltages(double leftVoltage, double rightVoltage)
    {
        leftMaster.setVoltage(leftVoltage); rightMaster.setVoltage(rightVoltage);
        SmartDashboard.putNumber("Left Voltage", leftVoltage);
        SmartDashboard.putNumber("Right Voltage", rightVoltage);
    }
    
    
    /**
     * Returns the positions of the motors {left, right} in rotations.
     * @return The positions of the motors {left, right} in rotations.
     */
    public double[] getPositions()
    {
        double[] positions = new double[] {leftMaster.getEncoder().getPosition(),
                rightMaster.getEncoder().getPosition()};
        return positions;
        //return null;
    }
    
    /**
     * Returns the encoders for the motors {left, right}.
     * @return The encoders for the motors {left, right}.
     */
    public CANEncoder[] getEncoders()
    {
        return new CANEncoder[] {leftMaster.getEncoder(), rightMaster.getEncoder()};
    }
    
    /**
     * Returns the master motors in {left, right} order.
     * @return The left and right master motors.
     */
    public CANSparkMax[] getMotors()
    {
        //return new CANSparkMax[] {leftMaster, rightMaster};
        return null;
    }
    
    /**
     * Disables both motors.
     */
    public void disableMotors()
    {
        /*
        leftMaster.disable();
        leftServant.disable();
        rightMaster.disable();
        rightServant.disable();
        */
    }
    
    public void toggleTransmissionSolenoid()
    {
        transmissionSolenoid.set(!transmissionSolenoid.get());
        System.out.println("New Transmission Solenoid Status: " + transmissionSolenoid.get());
    }
    
    public void setTransmissionSolenoidStatus(boolean status) { transmissionSolenoid.set(status); }
    
    public boolean getTransmissionSolenoidStatus()
    {
        return transmissionSolenoid.get();
    }
    
}
