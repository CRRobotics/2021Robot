package org.team639.robot.Commands.Acquisition;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Acquisition;
import org.team639.robot.Subsystems.Index;

public class ContinuallyRun extends CommandBase
{
    private Index indexer;
    private Acquisition acquisition;
    private long startMillis;
    private long waitTimeMillis;
    private boolean done;


    public ContinuallyRun()
    {
        indexer = Robot.getIndexer();
        acquisition = Robot.getAcquisition();
        done = false;
        addRequirements(indexer);
        addRequirements(acquisition);
    }

    public void initialize()
    {
        acquisition.runAcquisition();
        indexer.turnOn();
        indexer.extendPistons();
    }

    public void end()
    {
        acquisition.stopAcquisition();
        indexer.turnOff();
    }
}
