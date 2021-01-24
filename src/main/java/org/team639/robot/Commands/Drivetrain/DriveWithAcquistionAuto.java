package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.lib.math.PID;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Acquisition;
import org.team639.robot.Subsystems.DriveTrain;
import org.team639.robot.Subsystems.Index;

public class DriveWithAcquistionAuto extends CommandBase
{

    private boolean done;

    private DriveTrain driveTrain;

    private double[] initialEncoderPositions;

    private double rotations;

    private PID pid;

    private double targetEncoderPositionLeft;
    private double targetEncoderPositionRight;
    private double targetRotations;
    private double targetEncoderUnits;
    private double targetMeters;

    private double leftEncoderDiff;
    private double rightEncoderDiff;

    private Index indexer;
    private Acquisition acquisition;

    private boolean negative;


    /**
     * Initializes the command
     * @param distance The distance to travel in meters
     */
    public DriveWithAcquistionAuto(double distance) {
        negative = distance < 0;
        driveTrain = Robot.getDriveTrain();
        indexer = Robot.getIndexer();
        acquisition = Robot.getAcquisition();

        addRequirements(driveTrain);
        targetMeters = distance;
        targetRotations = targetMeters * Constants.inchesToRotations;
        targetEncoderUnits = targetRotations * Constants.rotationsToEncoderUnits;
        targetEncoderUnits *= Constants.driveTrainGearRatio;
    }

    /**
     * Sets the target encoder positions and the PID values
     */
    public void initialize()
    {
        done = false;
        initialEncoderPositions = driveTrain.getPositions();
        targetEncoderPositionLeft = targetEncoderUnits + driveTrain.getPositions()[0];
        targetEncoderPositionRight = targetEncoderUnits + driveTrain.getPositions()[1];
        pid = new PID(Constants.autoDriveForwardP, Constants.autoDriveForwardI, Constants.autoDriveForwardD,
                0.01, .5, 0.01, 0.25, 0);
        indexer.turnOn();
        indexer.extendPistons();
        Robot.getAcquisitionPistons().moveDown();

        System.out.println("Driving " + targetMeters + " meters");
    }

    /**
     * Sets motor speeds based on using PID math with the distance left to travel. Sets done to true when the
     * difference in target and actual encoder values is <= 0.
     */
    public void execute()
    {
        acquisition.runAcquisition();
        double[] positions = driveTrain.getPositions();
        double leftEncoderPosition = positions[0];
        double rightEncoderPosition = positions[1];
        leftEncoderDiff = targetEncoderPositionLeft - leftEncoderPosition;
        rightEncoderDiff = targetEncoderPositionRight - rightEncoderPosition;
        double average = pid.compute((leftEncoderDiff + rightEncoderDiff) / 2.0);
        driveTrain.setSpeeds(average, average);
        System.out.println("RightEncoderPosition: " + rightEncoderPosition);
        System.out.println("LeftEncoderPosition: " + leftEncoderPosition);
        System.out.println("Average: " + average);
        done = ((!negative && (targetRotations < 0 || average <= 0))
                || (!negative && (targetRotations < 0 || average <= 0)));
        if(done)
        {
            System.out.println("AutoDriveForward Headed out");
            indexer.turnOff();
            acquisition.stopAcquisition();
        }
    }

    /**
     * Returns whether the command is finished or not.
     * @return Whether the command is finished or not.
     */
    public boolean isFinished()
    {
        return done;
    }

    public void interrupted()
    {
        System.out.println("AutoDriveForward Interrupted");
        end();
    }

    public void end()
    {
        indexer.turnOff();
        acquisition.stopAcquisition();
        System.out.println("AutoDriveForward Ended");
        driveTrain.setSpeeds(0.0, 0.0);
        super.end(false);
    }
}
