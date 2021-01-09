package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.math.PID;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Acquisition;
import org.team639.robot.Subsystems.DriveTrain;
import org.team639.lib.Constants;
import org.team639.robot.Subsystems.Index;

public class AutoDriveForwardWhileAcquisitionRunning extends CommandBase
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
    public AutoDriveForwardWhileAcquisitionRunning(double distance) {
        negative = distance < 0;
        driveTrain = Robot.getDriveTrain();
        indexer = Robot.getIndexer();
        acquisition = Robot.getAcquisition();
        addRequirements(driveTrain);
        addRequirements(indexer);
        addRequirements(acquisition);
        targetMeters = distance;
        targetRotations = targetMeters * Constants.inchesToRotations;
        targetEncoderUnits = targetRotations * Constants.rotationsToEncoderUnits;
        targetEncoderUnits *= Constants.driveTrainGearRatio;
        System.out.println("AutoDriveForward Constructed: " + distance);

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
        pid = new PID(Constants.autoDriveForwardP, Constants.autoDriveForwardI, Constants.autoDriveForwardD, 0.01, 1, 0.01, 0.25, 0);
        indexer.turnOn();
        indexer.extendPistons();
        Robot.getAcquisitionPistons().moveDown();
        acquisition.runAcquisition();
        System.out.println("AutoDriveForwardWhileAcquisitionRunning Initialized: " + targetMeters);
    }

    /**
     * Sets motor speeds based on using PID math with the distance left to travel. Sets done to true when the
     * difference in target and actual encoder values is <= 0.
     */
    public void execute()
    {
        double[] positions = driveTrain.getPositions();
        double leftEncoderPosition = positions[0];
        double rightEncoderPosition = positions[1];
        leftEncoderDiff = targetEncoderPositionLeft - leftEncoderPosition;
        rightEncoderDiff = targetEncoderPositionRight - rightEncoderPosition;
        double average = pid.compute((leftEncoderDiff + rightEncoderDiff) / 2.0);
        driveTrain.setSpeeds(average, average);
        done = targetRotations < 0 || (!negative && (average <= 0 || leftEncoderDiff >= 0 || rightEncoderDiff >= 0)
                || (negative && (average >= 0|| leftEncoderDiff <= 0 || rightEncoderDiff <= 0)));

        if(done)
        {
            indexer.turnOff();
            acquisition.stopAcquisition();
        }

        SmartDashboard.putNumber("leftEncoderDiff", leftEncoderDiff);
        SmartDashboard.putNumber("rightEncoderDiff", rightEncoderDiff);
        SmartDashboard.putNumber("targetEncoderPositionLeft", targetEncoderPositionLeft);
        SmartDashboard.putNumber("targetEncoderPositionRight", targetEncoderPositionRight);

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
