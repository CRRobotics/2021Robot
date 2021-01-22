package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;

/**
 * Automatically indexes balls based on sensor reading
 */
public class AutoIndexer extends CommandBase
{
    private Index indexer;
    private boolean done;
    
    public AutoIndexer()
    {
        indexer = Robot.getIndexer();
        addRequirements(indexer);
    }

    public void initialize()
    {
        if (!indexer.isAuto() && Index.sensorValue) { indexer.setSpeed(-1); }
    }

    public void end(boolean interrupted)
    {
        indexer.turnOff();
    }
}

