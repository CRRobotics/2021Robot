package org.team639.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;
import edu.wpi.first.wpilibj.Solenoid;
import org.team639.robot.OI;
import org.team639.robot.Robot;

import javax.xml.crypto.Data;

public class Shooter implements Subsystem
{
    
    private CANSparkMax mainMotor;
    private CANSparkMax secondMotor;

    //1.0: 5500 RPM
    //0.8: 4500 RPM
    //0.6: 3400 RPM
    //0.4: 2200 RPM
    //0.2: 1000 RPM
    private static double overallPower = 1;
    public static int rpm = 5000;

    private boolean isMaxSpeed;

    private static final double closePower = overallPower;
    private static final double farPower = overallPower;

    //private CANPIDController mainShooterPIDs;
    //private CANPIDController secondShooterPIDs;
    private static double shooterSetting = 1;
    private static final double closeHeight = 1;
    private static final double farHeight = 0;
    long lastToggle = 0;
    
    
    public Shooter() {
        mainMotor = new CANSparkMax(Constants.shooterSparkMasterID, CANSparkMaxLowLevel.MotorType.kBrushless);
        secondMotor = new CANSparkMax(Constants.shooterSparkServantID, CANSparkMaxLowLevel.MotorType.kBrushless);
        //mainMotor = new CANSparkMax(Constants.shooterSparkMasterID, CANSparkMaxLowLevel.MotorType.kBrushless);
        //secondMotor = new CANSparkMax(Constants.shooterSparkServantID, CANSparkMaxLowLevel.MotorType.kBrushless);
        mainMotor.restoreFactoryDefaults(); secondMotor.restoreFactoryDefaults();
        //mainShooterPIDs = new CANPIDController(mainMotor);
        //secondShooterPIDs = new CANPIDController(secondMotor);

        //secondMotor.follow(mainMotor);
        //secondMotor.setInverted(true);
        secondMotor.setInverted(true);
        //secondMotor.follow(mainMotor);

        mainMotor.setSmartCurrentLimit(80);
        secondMotor.setSmartCurrentLimit(80);

        mainMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        secondMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        isMaxSpeed = false;

    }

    public void periodic()
    {
        toggleRPM(true);
        togglePowers(true);
        SmartDashboard.putNumber("MainMotorRPM", mainMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("SecondaryMotorRPM", secondMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("ShooterSpeed Percentage", overallPower);
        SmartDashboard.putNumber("Target RPM", rpm);
    }

    public void toggleMaxSpeed()
    {
        isMaxSpeed = !isMaxSpeed;
    }

    public void BangBangControl()
    {
        if(mainMotor.getEncoder().getVelocity() > rpm)
            setSpeed(0);
        else
            setSpeed(1);
    }
    /**
     * Calls either the shootClose or shootFar methods based on what mode the shooter is in
     */
    public void shoot()
    {
        //mainShooterPIDs.setP(Constants.shooterP);
        //mainShooterPIDs.setI(Constants.shooterI);
        //mainShooterPIDs.setD(Constants.shooterD);
        //mainShooterPIDs.setFF(Constants.shooterF);
        //secondShooterPIDs.setP(Constants.shooterP);
        //secondShooterPIDs.setI(Constants.shooterI);
        //secondShooterPIDs.setD(Constants.shooterD);
        //secondShooterPIDs.setFF(Constants.shooterF);
        //secondMotor.follow(mainMotor);
        //secondMotor.setInverted(true);
        secondMotor.setInverted(true);
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

    /**
     * Toggles through the various RPM for shooter
     * @param toggleUp Whether the shooter power increases or decreases
     */
    public void toggleRPM(boolean toggleUp)
    {
        if(OI.ControlDPadRight.get() == true && System.currentTimeMillis() > (lastToggle + 300))
        {
            if(rpm != 5500) {
                rpm += 500;
                lastToggle = System.currentTimeMillis();

            }
            else {
                rpm = 0;
                lastToggle = System.currentTimeMillis();
            }
        }
        if(OI.ControlDPadLeft.get() == true && System.currentTimeMillis() > (lastToggle + 300))
        {
            if(rpm != 0) {
                rpm -= 500;
                lastToggle = System.currentTimeMillis();

            }
            else {
                overallPower = 5000;
                lastToggle = System.currentTimeMillis();

            }
        }

    }
    /**
     * Toggles through the various powers for shooter
     * @param toggleUp Whether the shooter power increases or decreases
     */
    public void togglePowers(boolean toggleUp)
    {
        if(OI.ControlDPadUp.get() == true && System.currentTimeMillis() > (lastToggle + 300))
        {
            if(overallPower != 1) {
                overallPower += 0.1;
                lastToggle = System.currentTimeMillis();

            }
            else {
                overallPower = 0;
                lastToggle = System.currentTimeMillis();

            }
        }
        if(OI.ControlDPadDown.get() == true && System.currentTimeMillis() > (lastToggle + 300))
        {
            if(overallPower != 0) {
                overallPower -= 0.1;
                lastToggle = System.currentTimeMillis();

            }
            else {
                overallPower = 1;
                lastToggle = System.currentTimeMillis();

            }
        }

    }

    public void stop()
    {
        mainMotor.set(0);
        secondMotor.set(0);
    }
    
    public void setMotorSpeedClose()
    {
        mainMotor.set(overallPower);
        secondMotor.set(overallPower);
    }
    
    public void setMotorSpeedFar()
    {
        mainMotor.set(overallPower);
        secondMotor.set(overallPower);
    }
    

}