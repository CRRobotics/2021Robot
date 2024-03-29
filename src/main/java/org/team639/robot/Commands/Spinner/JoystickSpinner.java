package org.team639.robot.Commands.Spinner;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Commands.Climbing.JoystickClimb;
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
    public void execute()
    {
        if(!Robot.climbingJoysticksEnabled)
        {
            if (Robot.getControlXboxController().getBumper(GenericHID.Hand.kRight))
            {
                spinner.setMotorSpeed(0.5);
            }
            if (Robot.getControlXboxController().getBumperReleased(GenericHID.Hand.kRight))
            {
                end();
            }
        }
    }
    public void end()
    {
        spinner.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
