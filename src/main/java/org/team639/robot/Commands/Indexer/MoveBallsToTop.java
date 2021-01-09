package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;

import static org.team639.lib.Constants.*;

/**
 * Moves balls to the top of the indexer
 */
public class MoveBallsToTop extends CommandBase
{
    private Index indexer;
    private long startMillis;
    private long waitTimeMillis;
    private boolean done;
    
    public MoveBallsToTop()
    {
        indexer = Robot.getIndexer();
        addRequirements(indexer);
    }
    
    public void initialize()
    {
        indexer.stopManualOverride();
        done = false;
        System.out.println("MoveBallsToTop initialized");
        indexer.turnOn();
        startMillis = System.currentTimeMillis();
        waitTimeMillis = shootingTime;
    }
    
    public void execute()
    {
        if(System.currentTimeMillis() - startMillis >= waitTimeMillis)
        {
            indexer.turnOff();
            done = true;
        }
        if(done)
        {
            indexer.startManualOverride();
            end();
        }
    }
    
    
    /**
     * Returns whether the command is finished or not.
     * @return Whether the command is finished or not.
     */
    public boolean isFinished()
    {
        return done;
    }
    
    public void interrupted()
    {
        System.out.println("AutoDriveForward Interrupted");
        end();
    }
    
    public void end()
    {
        System.out.println("AutoDriveForward Ended");
        indexer.turnOff();
        indexer.startManualOverride();
        super.end(false);
    }
    
}