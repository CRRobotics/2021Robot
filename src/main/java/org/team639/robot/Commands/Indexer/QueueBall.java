package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;

public class QueueBall extends CommandBase
{
    private Index indexer;
    private long ballDelay;
    private int ballCount = 0;
    boolean done;
    private long logStart;
    
    public QueueBall()
    {
        indexer = Robot.getIndexer();
        done = false;
        addRequirements(indexer);
    }
    
    public void initialize()
    {
        if(indexer.getSensorValue()) {
            indexer.turnOn();
        }
        logStart = System.currentTimeMillis();
    }
    
    public void execute()
    {
        updateMotor();
        end(done);
    }
    
    
    public void updateMotor()
    {
        if(!indexer.getSensorValue())
        {
            done = true;
            indexer.turnOff();
            ballCount ++;
        }
        done = false;
    }
    
    public void end(boolean done)
    {
        long logEnd = System.currentTimeMillis();
        ballDelay = logEnd - logStart;
        if(done || ballCount == 5)
        {
            System.out.println("IndexToAcquisition Ended");
        }
    }
}