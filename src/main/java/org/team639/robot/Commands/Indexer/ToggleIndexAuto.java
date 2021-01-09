package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.ShooterPistons;

public class ToggleIndexAuto extends CommandBase
{

    boolean done;

    public ToggleIndexAuto()
    {
        done = false;
        Robot.getIndexer().toggleAuto();
        done = true;
    }

    public boolean isDone() {
        return done;
    }
}
