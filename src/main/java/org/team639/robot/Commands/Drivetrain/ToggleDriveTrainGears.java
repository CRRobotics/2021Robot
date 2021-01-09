package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;

public class ToggleDriveTrainGears extends CommandBase {

    private boolean done;

    public ToggleDriveTrainGears()
    {
        done = false;
    }

    public void initialize()
    {
        System.out.println("------ Gear Changed ------");
        Robot.getDriveTrain().toggleTransmissionSolenoid();
        done = true;
    }

    public boolean isDone() {
        return done;
    }

}
