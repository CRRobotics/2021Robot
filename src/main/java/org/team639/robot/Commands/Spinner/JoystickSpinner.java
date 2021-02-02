package org.team639.robot.Commands.Spinner;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Commands.Climbing.JoystickClimb;
import org.team639.robot.OI;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Spinner;
//TODO: Test the joystick spinner irl, get sensor input for 'PanelSpin3Times'
public class JoystickSpinner extends CommandBase
{

    private Spinner spinner;
    
    public JoystickSpinner()
    {
        spinner = Robot.getSpinner();
        addRequirements(spinner);
    }
    
    @Override
    public void initialize()
    {
        if(!Robot.climbingJoysticksEnabled)
        {
                spinner.setMotorSpeed(0.5);
        }
    }
    public void end(boolean interrupted)
    {
        spinner.setMotorSpeed(0);
    }

}
