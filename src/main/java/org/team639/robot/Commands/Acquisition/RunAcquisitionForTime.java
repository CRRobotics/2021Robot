package org.team639.robot.Commands.Acquisition;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.team639.lib.Constants;
import org.team639.robot.Commands.Indexer.MoveBallsToTop;
import org.team639.robot.Commands.Indexer.MoveBallsToTopShooting;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Acquisition;
import org.team639.robot.Subsystems.Index;
import org.team639.robot.Subsystems.Shooter;

public class RunAcquisitionForTime extends CommandBase
{

    private Index indexer;
    private Acquisition acquisition;
    private long startMillis;
    private long waitTimeMillis;
    private boolean done;


    public RunAcquisitionForTime(double time)
    {
        indexer = Robot.getIndexer();
        acquisition = Robot.getAcquisition();
        done = false;
        addRequirements(indexer);
        addRequirements(acquisition);
        waitTimeMillis = (long)(time / 1000);
    }

    public void initialize()
    {
        done = false;
        //indexer.retractPistons();
        acquisition.runAcquisition();
        indexer.turnOn();
        indexer.extendPistons();
        startMillis = System.currentTimeMillis();
        waitTimeMillis += Constants.shooterSpinnerExtraTime;
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
            acquisition.stopAcquisition();
            indexer.turnOff();
            System.out.println("Shoot DONE ------- DONE ------- DONE");
            end(false);
        }
    }


    public boolean isFinished()
    {
        return done;
    }

    public void interrupted()
    {
        end(true);
    }

    @Override
    public void end(boolean interrupted)
    {
        System.out.println("Shoot ENDED ------- ENDED ------- ENDED");
        acquisition.stopAcquisition();
        indexer.turnOff();
        super.end(false);
        Robot.getDataManager().disableUpperRingLight();
    }

}
