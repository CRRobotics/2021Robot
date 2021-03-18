package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import org.team639.lib.Constants;
import org.team639.lib.math.PID;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.DataManager;
import org.team639.robot.Subsystems.DriveTrain;
import org.team639.robot.Subsystems.PhotoelectricSensor;


public class DriveAndAcquireBall extends CommandGroupBase {
    private boolean done;
    private DriveTrain driveTrain;

    private double rotations;

    private PID pid;

    private double targetEncoderPositionLeft;
    private double targetEncoderPositionRight;
    private double targetRotations;
    private double targetEncoderUnits;
    private double targetMeters;
    private double initialEncoderPositions;
    private double targetAngle;
    private Command autoRotate;
    private Command driveWithAcquisitionAuto;
    private Command runAcquisitionForTime;

    private double leftEncoderDiff;
    private double rightEncoderDiff;

    private boolean negative;
    private DataManager photoInfo;

    public DriveAndAcquireBall() {

        addCommands(autoRotate);
        addCommands(driveWithAcquisitionAuto);
        addCommands(runAcquisitionForTime);
        driveTrain = Robot.getDriveTrain();
        addRequirements(driveTrain);
        photoInfo = new DataManager();
        findBall();
        autoRotate = new AutoRotate(targetAngle);
    }

    public void findBall() {
        targetMeters = photoInfo.getClosestBall()[0];
        negative = targetMeters < 0;
        targetRotations = targetMeters * Constants.inchesToRotations;
        targetEncoderUnits = targetRotations * Constants.rotationsToEncoderUnits;
        targetEncoderUnits *= Constants.driveTrainGearRatio;
        targetAngle = photoInfo.outerHorizontalAngle;
    }

    public void initialize() {
        findBall();
        System.out.println("Target Angle is " + targetAngle + "degrees away");
        System.out.println("Driving to target: " + targetMeters + " meters");
        done = false;
        //initialEncoderPositions = driveTrain.getPositions();
        targetEncoderPositionLeft = targetEncoderUnits + driveTrain.getPositions()[0];
        targetEncoderPositionRight = targetEncoderUnits + driveTrain.getPositions()[1];
        pid = new PID(Constants.autoDriveForwardP, Constants.autoDriveForwardI, Constants.autoDriveForwardD, 0.01, .5, 0.01, 0.25, 0);
    }

    @Override
    public void addCommands(Command... commands) {

    }

    public void execute()
    {
    autoRotate.execute();
runAcquisitionForTime.initialize();
        while (photoInfo.getBalls().size() <= 5 && !done)
        {
            runAcquisitionForTime.execute();
        }
    double[] positions = driveTrain.getPositions();
    double leftEncoderPosition = positions[0];
    double rightEncoderPosition = positions[1];
    leftEncoderDiff =targetEncoderPositionLeft -leftEncoderPosition;
    rightEncoderDiff =targetEncoderPositionRight -rightEncoderPosition;
    double average = pid.compute((leftEncoderDiff + rightEncoderDiff) / 2.0);
        driveTrain.setSpeeds(average,average);
    done =((!negative &&(targetRotations< 0||average <=0))
            ||(!negative &&(targetRotations< 0||average <=0)));
    }

    public boolean isFinished()
    {
        return done;
    }

    public void interrupted()
    {
        System.out.println("Drive And Acquire Ball Interrupted");
        end();
    }

    public void end()
    {
        System.out.println("Drive and Acquire Ball Ended");
        driveTrain.setSpeeds(0.0, 0.0);
        super.end(false);
    }


}
