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

    private boolean isUp;
    public ContinuallyRun(boolean isUp)
    {
        this.isUp = isUp;
        acquisition = Robot.getAcquisition();
        done = false;
        addRequirements(acquisition);
    }

    public void initialize()
    {
        if(isUp) {
            acquisition.runAcquisition();
        }
        else{
            acquisition.reversePortMotor();
        }
    }

    public void end()
    {
        acquisition.stopAcquisition();
    }
}
