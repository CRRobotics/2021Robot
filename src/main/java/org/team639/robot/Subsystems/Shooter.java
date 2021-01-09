package org.team639.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;
import edu.wpi.first.wpilibj.Solenoid;
import org.team639.robot.Robot;

import javax.xml.crypto.Data;

public class Shooter implements Subsystem
{
    
    private CANSparkMax mainMotor;
    private CANSparkMax secondMotor;
    private static final double overallPower = 0.8;

    private boolean maxSpeed;

    private static final double closePower = overallPower;
    private static final double farPower = overallPower;
    
    
    private static final double closeHeight = 1;
    private static final double farHeight = 0;
    
    
    public Shooter() {
        mainMotor = new CANSparkMax(Constants.shooterSparkMasterID, CANSparkMaxLowLevel.MotorType.kBrushless);
        secondMotor = new CANSparkMax(Constants.shooterSparkServantID, CANSparkMaxLowLevel.MotorType.kBrushless);
        
        mainMotor.restoreFactoryDefaults(); secondMotor.restoreFactoryDefaults();
        
        //secondMotor.follow(mainMotor);
        //secondMotor.setInverted(true);
        secondMotor.setInverted(true);

        mainMotor.setSmartCurrentLimit(80);
        secondMotor.setSmartCurrentLimit(80);

        maxSpeed = false;

    }

    public void toggleMaxSpeed()
    {
        maxSpeed = !maxSpeed;
    }

    /**
     * Calls either the shootClose or shootFar methods based on what mode the shooter is in
     */
    public void shoot()
    {
        //if(!maxSpeed) {
            if (Robot.getShooterPistons().isClose()) {
                setMotorSpeedClose();
            } else {
                setMotorSpeedFar();
            }
        /*}
        else
        {
            setSpeed(1.0);
        }*/
    }
    
    public void setSpeed(double speed)
    {
        mainMotor.set(speed);
        secondMotor.set(speed);
    }
    
    public void stop()
    {
        mainMotor.set(0);
        secondMotor.set(0);
    }
    
    public void setMotorSpeedClose()
    {
        mainMotor.set(closePower);
        secondMotor.set(closePower);
    }
    
    public void setMotorSpeedFar()
    {
        mainMotor.set(farPower);
        secondMotor.set(farPower);
    }
    
    /**
     * Uses PIDF constants to keep the speed constant while shooting balls.
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     */
    public void setPIDF(double kP,double kI, double kD, double kF){
        setPIDF(0.001,0,10,0.00017);
    }
    
}