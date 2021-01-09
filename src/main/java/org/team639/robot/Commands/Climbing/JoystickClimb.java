package org.team639.robot.Commands.Climbing;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Climbing;

public class JoystickClimb extends CommandBase
{
    
    private Climbing climbing;
    private boolean done;
    
    public JoystickClimb()
    {
        done = false;
        climbing = Robot.getClimbing();
        addRequirements(climbing);
    }
    
    @Override
    public void execute()
    {
        if(Robot.climbingJoysticksEnabled) {
            climbing.setLift(-Robot.getControlXboxController().getY(GenericHID.Hand.kLeft) * Constants.liftSpeedModifier);
            climbing.setWinch(Robot.getControlXboxController().getY(GenericHID.Hand.kRight) * Constants.winchSpeedModifier);
        }

    }
}
