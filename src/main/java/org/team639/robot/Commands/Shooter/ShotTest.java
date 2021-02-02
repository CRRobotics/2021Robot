package org.team639.robot.Commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;
import org.team639.robot.Subsystems.Shooter;

public class ShotTest extends CommandBase
{
    private Index indexer;
    private Shooter shooter;

    public ShotTest()
    {
        shooter = Robot.getShooter();
        addRequirements(shooter);
    }

    public void initialize()
    {
        shooter.shoot();
    }
    public void end(boolean interrupted)
    {
        System.out.println("Shoot ENDED ------- ENDED ------- ENDED");
        shooter.stop();
        super.end(false);
    }
}
