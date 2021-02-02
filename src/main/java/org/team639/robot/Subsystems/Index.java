package org.team639.robot.Subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team639.lib.Constants;
import org.team639.lib.PhotoelectricSensor;

public class Index extends SubsystemBase
{
    private TalonSRX master;
    private TalonSRX follower;
    private static final double MOTOR_SPEED = -1;
    private final PhotoelectricSensor photo;
    private static Solenoid piston;
    private boolean manualOverride;
    public static boolean sensorValue;

    private boolean auto;
    
    public Index()
    {
        master = new TalonSRX(Constants.indexerMotorID1);
        follower = new TalonSRX(Constants.indexerMotorID2);
        master.configFactoryDefault();
        follower.configFactoryDefault();
        follower.follow(master);

        auto = false;

        photo = new PhotoelectricSensor(Constants.indexerSensorChannel);

        //master.configPeakCurrentDuration();
        piston = new Solenoid(Constants.indexerPistonsSolenoidID);
        extendPistons();
        startManualOverride();

        master.configPeakCurrentLimit(35, 10);
        master.configPeakCurrentDuration(200, 10);
        master.configContinuousCurrentLimit(30, 10);
        master.enableCurrentLimit(true);

        follower.configPeakCurrentLimit(35, 10);
        follower.configPeakCurrentDuration(200, 10);
        follower.configContinuousCurrentLimit(30, 10);
        follower.enableCurrentLimit(true);
    }

    public void periodic()
    {
        SmartDashboard.putBoolean("Index sensor value: ",getSensorValue());
        sensorValue = getSensorValue();
    }

    public void toggleAuto()
    {
        auto = !auto;
    }

    public void startAuto()
    {
        auto = true;
    }

    public void stopAuto()
    {
        auto = false;
    }

    public boolean isAuto()
    {
        return auto;
    }
    
    /**
     * Turns on the index motor with speed of MOTOR_SPEED
     */
    public void turnOn()
    {
        master.set(ControlMode.PercentOutput, MOTOR_SPEED);
    }
    
    /**
     * Turns on the index motor with speed of MOTOR_SPEED
     */
    public void turnOnReverse()
    {
        master.set(ControlMode.PercentOutput, -MOTOR_SPEED);
    }
    
    /**
     * Turns off the index motor
     */
    public void turnOff()
    {
        master.set(ControlMode.PercentOutput, 0);
    }
    
    public void setSpeed(double speed)
    {
        master.set(ControlMode.PercentOutput, speed);
    }
    
    public void startManualOverride()
    {
        manualOverride = true;
    }
    
    public void stopManualOverride()
    {
        manualOverride = false;
    }
    
    public boolean isManualOverride()
    {
        return manualOverride;
    }
    
    public boolean getSensorValue()
    {
        return photo.isDetected();
    }
    
    public void extendPistons()
    {
        piston.set(true);
    }
    
    public void retractPistons()
    {
        piston.set(false);
    }
    
}