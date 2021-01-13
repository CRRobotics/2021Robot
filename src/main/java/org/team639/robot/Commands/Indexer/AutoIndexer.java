package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;

public class AutoIndexer extends CommandBase
{
    private Index indexer;
    private long startMillis;
    private long waitTimeMillis;
    private boolean done;
    
    public AutoIndexer()
    {
        indexer = Robot.getIndexer();
        addRequirements(indexer);
    }

    public void execute()
    {
        if (indexer.getSensorValue())
        {
            indexer.turnOn();
        }
        else if (!indexer.getSensorValue())
        {
            indexer.turnOff();
        }
    }
}

//TODO might not work
