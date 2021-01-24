package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.robot.OI;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;

public class TriggerIndexer extends CommandBase
{

    private Index indexer;
    double speed;
    
    public TriggerIndexer(double speed)
    {
        indexer = Robot.getIndexer();
        addRequirements(indexer);
        this.speed = speed;
    }

    public void initialize()
    {
        if(!Robot.climbingJoysticksEnabled)
            indexer.setSpeed(speed);
    }
    public void end(boolean interrupted)
    {
        indexer.setSpeed(0);
    }

    
}
