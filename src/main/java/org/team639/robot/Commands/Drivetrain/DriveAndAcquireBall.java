package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import org.team639.lib.Constants;
import org.team639.lib.math.PID;
import org.team639.robot.Commands.Acquisition.RunAcquisitionForTime;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.DataManager;
import org.team639.robot.Subsystems.DriveTrain;
import org.team639.robot.Subsystems.Index;
import org.team639.robot.Subsystems.PhotoelectricSensor;


public class DriveAndAcquireBall extends CommandGroupBase {
    private boolean done;
    private DriveTrain driveTrain;
    private Index indexer;
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
    //private DataManager photoInfo;


    public DriveAndAcquireBall()
    {

        addCommands(autoRotate);
        addCommands(driveWithAcquisitionAuto);
        addCommands(runAcquisitionForTime);
        driveTrain = Robot.getDriveTrain();
        addRequirements(driveTrain);
        //photoInfo = new DataManager();
        findBall();
        autoRotate = new AutoRotate(targetAngle);
        //Run acquisition for time created that runs until the CommandScheduler refreshes
        runAcquisitionForTime = new RunAcquisitionForTime(20);
        driveWithAcquisitionAuto = new DriveWithAcquisitionAuto(targetMeters);
    }

    public void findBall()
    {
        //targetMeters = photoInfo.getClosestBall()[0];
        targetMeters = Robot.getDataManager().getClosestBall()[0];
        negative = targetMeters < 0;
        targetRotations = targetMeters * Constants.inchesToRotations;
        targetEncoderUnits = targetRotations * Constants.rotationsToEncoderUnits;
        targetEncoderUnits *= Constants.driveTrainGearRatio;
        //targetAngle = photoInfo.outerHorizontalAngle;
        targetAngle = Robot.getDataManager().outerHorizontalAngle;
    }

    public void initialize() {
        findBall();
        System.out.println("Target Angle is " + targetAngle + "degrees away");
        System.out.println("Driving to target: " + targetMeters + " meters");
        //initialEncoderPositions = driveTrain.getPositions();
        targetEncoderPositionLeft = targetEncoderUnits + driveTrain.getPositions()[0];
        targetEncoderPositionRight = targetEncoderUnits + driveTrain.getPositions()[1];
        pid = new PID(Constants.autoDriveForwardP, Constants.autoDriveForwardI, Constants.autoDriveForwardD, 0.01, .5, 0.01, 0.25, 0);
        if (targetMeters == -1 && targetAngle == -1)
        {
            System.out.println("No ball found");
            done = true;
        }
        else done = false;
        autoRotate.execute();
        driveWithAcquisitionAuto.execute();
    }

    @Override
    public void addCommands(Command... commands) {

    }

    public void execute()
    {
        //indexer.turnOn();
        runAcquisitionForTime.initialize();
        //while (photoInfo.getBalls().size() <= 4 && !done)
        //{
            //runAcquisitionForTime.execute();
        //}
        //while (Robot.getDataManager().getBalls().size() < 4 && !done)
        //{
            runAcquisitionForTime.execute();
        //}
       //double[] positions = driveTrain.getPositions();
     //double leftEncoderPosition = positions[0];
     //double rightEncoderPosition = positions[1];
     //leftEncoderDiff =targetEncoderPositionLeft -leftEncoderPosition;
     //rightEncoderDiff =targetEncoderPositionRight -rightEncoderPosition;
     double average = pid.compute((leftEncoderDiff + rightEncoderDiff) / 2.0);
        driveTrain.setSpeeds(average,average);
     done =((!negative &&(targetRotations< 0||average <=0))
            ||(!negative &&(targetRotations< 0||average <=0)));
      indexer.turnOff();
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
        indexer.turnOff();
    }


}
