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
    
    public TriggerIndexer()
    {
        indexer = Robot.getIndexer();
        addRequirements(indexer);
    }
    
    public void execute()
    {
        if(!indexer.isAuto() && !Robot.climbingJoysticksEnabled) {
            if (OI.ControlController.getTriggerAxis(GenericHID.Hand.kRight) > Constants.triggerInputThreshold) {
                indexer.turnOn();
            } else {
                indexer.setSpeed(OI.ControlController.getY(GenericHID.Hand.kLeft));
            }
        }
        else
        {
            if(indexer.getSensorValue())
            {
                indexer.setSpeed(.5);
            }
            else
            {
                indexer.turnOff();
            }
        }
    }
    
}
