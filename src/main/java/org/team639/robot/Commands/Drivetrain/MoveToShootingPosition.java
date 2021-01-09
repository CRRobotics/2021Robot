package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.team639.lib.Constants;
import org.team639.robot.Robot;

public class MoveToShootingPosition extends SequentialCommandGroup
{

    private double angle; private double distance;

    /**
     * Calls MoveToPoint to move to the point to shoot from.
     * @param angle The horizontal angle to the shooting target.
     * @param distance The distance to the shooting target.
     * @param longDistance If the shot is from the long distance position
     */
    public MoveToShootingPosition(double angle, double distance, boolean longDistance)
    {
        /*
        double x = Math.sin(angle) * distance;
        double y = Math.cos(angle * distance);
        if(!longDistance) {
            addCommands(new MoveToPoint(x - Constants.shortShootingPositionDistance, y));
        }
        else
        {
            addCommands(new MoveToPoint(x - Constants.longShootingPositionDistance, y));
        }
        */
        addCommands(new AutoRotate(angle), new AutoDriveForward(distance - Constants.shortShootingPositionDistance));

    }

    /**
     * Calls MoveToPoint to move to the point to shoot from.
     * @param longDistance If the shot is from the long distance position
     */
    public MoveToShootingPosition(boolean longDistance)
    {
        /*
        double angle = Robot.getDataManager().getHorizontalAngleToOuterTarget();
        double distance = Robot.getDataManager().getDistanceToOuterTarget();
        double x = Math.sin(angle) * distance;
        double y = Math.cos(angle * distance);
        if(!longDistance) {
            addCommands(new MoveToPoint(x - Constants.shortShootingPositionDistance, y));
        }
        else
        {
            addCommands(new MoveToPoint(x - Constants.longShootingPositionDistance, y));
        }
        */
        double angle = Robot.getDataManager().getHorizontalAngleToOuterTarget();
        double distance = Robot.getDataManager().getDistanceToOuterTarget();
        System.out.println("Rotating " + angle + ", Moving " + distance);
        addCommands(new MoveRotateChain(
                new Command[] {new AutoRotate(angle),
                    new AutoDriveForward(distance)}));

    }

}