package org.team639.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;
import org.team639.lib.math.PID;

public class Spinner implements Subsystem
{
    private int currentColor;
    private int targetColor;

    private TalonSRX motor;
    
    public Spinner()
    {
        motor = new TalonSRX(Constants.spinnerMotorID);
    }
    
    public void setMotorSpeed(double speed)
    {
        motor.set(ControlMode.PercentOutput, speed);
    }

    public int getColor()
    {
        return currentColor;
    }
}
