package org.team639.robot.Commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Index;

import static org.team639.lib.Constants.*;

/**
 * Moves balls to the top of the indexer
 */
public class MoveBallsToTopShooting extends CommandBase
{
    private Index indexer;
    private long startMillis;
    private long waitTimeMillisDown;
    private long waitTimeMillisUp;
    private boolean done;
    private boolean secondStage;
    
    public MoveBallsToTopShooting()
    {
        waitTimeMillisDown = shootingReverseTime;
        waitTimeMillisUp = shootingTime + shootingReverseTime;
        indexer = Robot.getIndexer();
        addRequirements(indexer);
    }
    
    public void initialize()
    {
        done = false;
        secondStage = false;
        System.out.println("MoveBallsToTop initialized");
        indexer.setSpeed(0.2);
        indexer.retractPistons();
        startMillis = System.currentTimeMillis();
    }
    
    public void execute()
    {
        if(!secondStage) {
            if (System.currentTimeMillis() - startMillis >= waitTimeMillisDown) {
                indexer.turnOn();
                secondStage = true;
                indexer.retractPistons();
            }
        }
        else
        {
            if(System.currentTimeMillis() - startMillis >= waitTimeMillisUp + waitTimeMillisDown)
            {
                indexer.turnOff();
                done = true;
            }
        }
        
        if(done)
        {
            indexer.extendPistons();
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
        indexer.extendPistons();
        super.end(false);
    }
    
}