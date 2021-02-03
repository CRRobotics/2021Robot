package org.team639.robot.Commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Shooter;

/**
 * Command that allows controller to toggle through different shooter powers
 */
public class ShotToggler extends CommandBase
{

    private Shooter shooter;
    boolean up = true;

    public ShotToggler(boolean up)
    {
        this.shooter = Robot.getShooter();
        this.up = up;
    }

    public void initialize()
    {
        shooter.togglePowers(up);
    }

    public void end(boolean interrupted)
    {

    }
}
