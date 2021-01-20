package org.team639.robot.Commands.Acquisition;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.robot.OI;
import org.team639.robot.Robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import org.team639.robot.Subsystems.Acquisition;

import java.util.Set;
import java.util.HashSet;

public class ManualAcquisition extends CommandBase
{
    
    private Acquisition acquisition;
    private boolean manualOverride = false;
    double leftTriggerInput;
    double rightTriggerInput;
    
    public ManualAcquisition()
    {
        //requirements = new HashSet<Subsystem>();
        addRequirements(Robot.getAcquisition());
        acquisition = Robot.getAcquisition();
    }
    
    @Override
    public void initialize()
    {
    
    }
    
    @Override
    public void execute()
    {
        leftTriggerInput = OI.ControlController.getTriggerAxis(GenericHID.Hand.kLeft);
        rightTriggerInput = OI.ControlController.getTriggerAxis(GenericHID.Hand.kRight);
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
}
