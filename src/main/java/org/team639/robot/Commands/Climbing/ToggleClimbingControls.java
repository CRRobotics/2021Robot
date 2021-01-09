package org.team639.robot.Commands.Climbing;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Commands.Drivetrain.ToggleDriveTrainGears;
import org.team639.robot.Robot;

public class ToggleClimbingControls extends CommandBase {

    private boolean done;

    public ToggleClimbingControls()
    {
        done = false;
    }

    public void initialize()
    {
        Robot.climbingJoysticksEnabled = !Robot.climbingJoysticksEnabled;
        done = true;
    }

    public boolean isDone() {
        return done;
    }
}
