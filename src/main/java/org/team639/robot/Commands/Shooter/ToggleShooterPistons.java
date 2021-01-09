package org.team639.robot.Commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.ShooterPistons;

public class ToggleShooterPistons extends CommandBase
{
    
    private ShooterPistons shooterPistons;
    private boolean done;
    
    public ToggleShooterPistons()
    {
        done = false;
        shooterPistons = Robot.getShooterPistons();
    }
    
    public void initialize()
    {
        shooterPistons.toggle();
        done = true;
    }
    
    @Override
    public boolean isFinished()
    {
        return done;
    }

}
