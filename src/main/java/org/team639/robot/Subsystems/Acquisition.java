package org.team639.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.robot.Robot;

import javax.xml.crypto.Data;

public class Acquisition implements Subsystem {
    
    //values from ball camera
    private double xAngle;
    private double ballDistance;
    
    private TalonSRX acquisitionMotor1; // Port
    private TalonSRX acquisitionMotor2; // Starboard
    
    //ID numbers for the acquisition motors
    private final int motorID1 = 3;
    private final int motorID2 = 1;
    
    //Speed at which to run the motor, between -1 and 1
    private final double motorSpeed = 0.7;
    
    private boolean manualOverride = false;
    
    
    // For Aiden Bedore's input: 1 = use right motor, 0 = reverse right motor
    
    public Acquisition()
    {
        acquisitionMotor1 = new TalonSRX(motorID1);
        acquisitionMotor2 = new TalonSRX(motorID2);

        acquisitionMotor1.configPeakCurrentLimit(35, 10);
        acquisitionMotor1.configPeakCurrentDuration(200, 10);
        acquisitionMotor1.configContinuousCurrentLimit(30, 10);
        acquisitionMotor1.enableCurrentLimit(true);

        acquisitionMotor2.configPeakCurrentLimit(35, 10);
        acquisitionMotor2.configPeakCurrentDuration(200, 10);
        acquisitionMotor2.configContinuousCurrentLimit(30, 10);
        acquisitionMotor2.enableCurrentLimit(true);
    }
    
    public void periodic()
    {
        updateInput();
    }
    
    private void updateInput()
    {
    
    }
    
    public boolean isManualOverride()
    {
        return manualOverride;
    }
    
    public void runAcquisition()
    {
        runPortMotor(); runStarboardMotor();
    }
    
    public void stopAcquisition()
    {
        stopPortMotor(); stopStarboardMotor();
    }
    
    public void reverseAcquisition()
    {
        reversePortMotor(); reverseStarboardMotor();
    }
    
    public void runPortMotor()
    {
        acquisitionMotor2.set(ControlMode.PercentOutput, motorSpeed);
    }
    
    public void reversePortMotor()
    {
        acquisitionMotor2.set(ControlMode.PercentOutput, -motorSpeed);
    }
    
    public void stopPortMotor()
    {
        acquisitionMotor2.set(ControlMode.PercentOutput, 0);
    }
    
    public void setPortMotor(double percentOutput)
    {
        acquisitionMotor2.set(ControlMode.PercentOutput, percentOutput);
    }
    
    public void stopStarboardMotor()
    {
        acquisitionMotor1.set(ControlMode.PercentOutput, 0);
    }
    
    public void runStarboardMotor()
    {
        acquisitionMotor1.set(ControlMode.PercentOutput, -motorSpeed);
    }
    
    public void reverseStarboardMotor()
    {
        acquisitionMotor1.set(ControlMode.PercentOutput, motorSpeed);
    }
    
    public void setStarboardMotor(double percentOutput)
    {
        acquisitionMotor1.set(ControlMode.PercentOutput, percentOutput);
    }
    
    public double getxAngle()
    {
        return xAngle;
    }
    
    public double getBallDistance()
    {
        return ballDistance;
    }
    
}
