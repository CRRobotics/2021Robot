package org.team639.robot.Commands.Acquisition;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.AcquisitionPistons;

public class ToggleAcquisitionPistons extends CommandBase
{
    
    private AcquisitionPistons acquisitionPistons;
    private boolean done;
    
    public ToggleAcquisitionPistons()
    {
        acquisitionPistons = Robot.getAcquisitionPistons();
        done = false;
    }
    
    public void initialize()
    {
        System.out.println("---------- ToggleAcquisitionPistons ----------");
        acquisitionPistons.toggleSolenoid();
        done = true;
    }
    
    public boolean isFinished()
    {
        return done;
    }
    
    public void end(boolean interrupted)
    {
        super.end(false);
    }
    
}
