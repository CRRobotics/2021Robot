package org.team639.robot.Commands.Acquisition;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import org.team639.lib.Constants;
import org.team639.robot.Robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import org.team639.robot.Subsystems.Acquisition;

import java.util.Set;
import java.util.HashSet;

public class ManualAcquisition implements Command
{
    
    private Acquisition acquisition;
    private Set requirements;
    private boolean manualOverride = false;
    private XboxController controlXBoxController;
    double leftTriggerInput;
    double rightTriggerInput;
    
    public ManualAcquisition()
    {
        //requirements = new HashSet<Subsystem>();
        requirements.add(Robot.getAcquisition());
        acquisition = Robot.getAcquisition();
        controlXBoxController = Robot.getControlXboxController();
    }
    
    @Override
    public void initialize()
    {
    
    }
    
    @Override
    public void execute()
    {
        leftTriggerInput = controlXBoxController.getTriggerAxis(GenericHID.Hand.kLeft);
        rightTriggerInput = controlXBoxController.getTriggerAxis(GenericHID.Hand.kRight);
        if(rightTriggerInput > Constants.triggerInputThreshold)
        {
            acquisition.runAcquisition();
        }
        else if(leftTriggerInput > Constants.triggerInputThreshold)
        {
            acquisition.reversePortMotor();
        }
        else
        {
            acquisition.stopAcquisition();
        }
        
    }
    
    @Override
    public void end(boolean interrupted)
    {
        acquisition.stopAcquisition();
    }
    
    @Override
    public boolean isFinished()
    {
        //Todo: add code that recognizes when a ball has been placed in the indexer
        return false;
    }
    
    @Override
    public Set<Subsystem> getRequirements()
    {
        return requirements;
    }
}
