package org.team639.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMax;
import edu.wpi.first.wpilibj.PWMSparkMax;

public class DriveTrain {

    private CANSparkMax leftMaster;
    private CANSparkMax rightMaster;
    private CANSparkMax leftFollower;
    private CANSparkMax rightFollower;
    private final int ID1Default = 5; // Left Master
    private final int ID2Default = 6; // Left Follower
    private final int ID3Default = 4; // Right Master
    private final int ID4Default = 7; // Right Follower

    public final double speedModifier = 0.5;

    public DriveTrain(CANSparkMax leftMaster, CANSparkMax rightMaster)
    {
        this.leftMaster = leftMaster; this.rightMaster = rightMaster;
    }

    public DriveTrain()
    {
        leftMaster = new CANSparkMax(ID1Default, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftFollower = new CANSparkMax(ID2Default, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMaster = new CANSparkMax(ID3Default, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFollower = new CANSparkMax(ID4Default, CANSparkMaxLowLevel.MotorType.kBrushless);

        leftMaster.restoreFactoryDefaults();
        leftFollower.restoreFactoryDefaults();
        rightMaster.restoreFactoryDefaults();
        rightMaster.restoreFactoryDefaults();

        rightFollower.follow(rightMaster);
        leftFollower.follow(leftMaster);

        leftMaster.setInverted(true);

        leftMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
        leftFollower.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightFollower.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    /**
     * Sets the speeds of the left and right master motors. Between -1.0 and 1.0
     */
    public void setSpeeds(double leftSpeed, double rightSpeed)
    {
        leftMaster.set(leftSpeed * speedModifier); rightMaster.set(rightSpeed * speedModifier);
    }

    /**
     * Sets the voltages of the left and right motors
     */
    public void setVoltages(double leftVoltage, double rightVoltage)
    {
        leftMaster.setVoltage(leftVoltage); rightMaster.setVoltage(rightVoltage);
    }

    /**
     * Returns the positions of the motors {left, right} in volts
     * @return The positions of the motors {left, right} in volts
     */
    public double[] getPositions()
    {
        return null;
    }

}
