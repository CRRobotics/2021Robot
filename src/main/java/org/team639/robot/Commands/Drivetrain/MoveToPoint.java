package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.team639.robot.Robot;

public class MoveToPoint extends SequentialCommandGroup
{

    private double angleFromDefault;
    private double totalAngle;
    private double distance;

    public MoveToPoint(double x, double y)
    {
        angleFromDefault = Math.atan(x / y);
        distance = x / Math.sin(angleFromDefault);
        totalAngle = angleFromDefault + (Robot.getDefaultAngle() - Robot.getDriveTrain().getHeading().getDegrees());
        addCommands(new MoveRotateChain(new Command[]{new AutoRotate(totalAngle), new AutoDriveForward(distance),
                new AutoRotate(-angleFromDefault)}));

    }

}