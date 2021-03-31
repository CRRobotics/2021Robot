package org.team639.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.team639.robot.Commands.Acquisition.ContinuallyRun;
import org.team639.robot.Commands.Acquisition.ManualAcquisition;
import org.team639.robot.Commands.Acquisition.RunAcquisitionForTime;
import org.team639.robot.Commands.Acquisition.ToggleAcquisitionPistons;
import org.team639.robot.Commands.Climbing.JoystickClimb;
import org.team639.robot.Commands.Climbing.ToggleClimbingControls;
import org.team639.robot.Commands.Drivetrain.*;
import org.team639.robot.Commands.Indexer.AutoIndexer;
import org.team639.robot.Commands.Indexer.ToggleIndexAuto;
import org.team639.robot.Commands.Indexer.TriggerIndexer;
import org.team639.robot.Commands.Shooter.*;
import org.team639.robot.Commands.Spinner.JoystickSpinner;
import org.team639.robot.Subsystems.*;
import org.team639.lib.Constants;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
    
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";
    private String m_autoSelected;
    private final SendableChooser<AutoTypes> m_chooser = new SendableChooser<AutoTypes>();
    private double test;
    private static DriveTrain driveTrain = new DriveTrain();;
    private static Acquisition acquisition = new Acquisition();
    private static AcquisitionPistons acquisitionPistons = new AcquisitionPistons();
    private static Index indexer = new Index();
    private static Shooter shooter = new Shooter();

    private static ShooterPistons shooterPistons = new ShooterPistons();
    private static Climbing climbing = new Climbing();

    public static boolean climbingJoysticksEnabled = false;
    private static Spinner spinner = new Spinner();
    private static DataManager dataManager;
    private static double defaultAngle; // In degrees
    //The path you want to use
    private String trajectoryJSON = "paths/galacticSearchA.wpilib.json";

    NetworkTableEntry testEntry;
    NetworkTable visionTable;


    //private static AcquisitionToIndexer acquisitionToIndexer = new AcquisitionToIndexer();
    //private static SenseIndex sensor = new SenseIndex();
    

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit()
    {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        visionTable = inst.getTable("CameraTracker");
        testEntry = visionTable.getEntry("path");
        inst.startClientTeam(639);
        inst.startDSClient();

        dataManager = new DataManager();
        defaultAngle = driveTrain.getHeading().getDegrees();
        setUpXboxController();
        setUpCommands();
        driveTrain.resetOdometry(driveTrain.startPosition);
        dataManager.disableUpperRingLight();
    }
    
    /**
     * Registers subsystems with the CommandScheduler and sets the default command.
     */
    private void setUpCommands()
    {
        CommandScheduler.getInstance().registerSubsystem(driveTrain);
        CommandScheduler.getInstance().registerSubsystem(acquisition);
        CommandScheduler.getInstance().registerSubsystem(indexer);
        CommandScheduler.getInstance().registerSubsystem(shooter);
        CommandScheduler.getInstance().registerSubsystem(climbing);
        CommandScheduler.getInstance().registerSubsystem(dataManager);
        CommandScheduler.getInstance().registerSubsystem(spinner);
        CommandScheduler.getInstance().setDefaultCommand(driveTrain, new JoystickDrive());
        CommandScheduler.getInstance().setDefaultCommand(acquisition, new ManualAcquisition());
        CommandScheduler.getInstance().setDefaultCommand(climbing, new JoystickClimb());
    }
    
    /**
     * Maps all of the buttons used on xBoxController.
     */
    private void setUpXboxController()
    {
        //Driver Settings
        OI.DriverRightBumper.whenPressed(new ToggleDriveTrainGears());
        OI.DriverButtonY.whenPressed(new AutoRotateToTarget());
        OI.DriverButtonA.whenPressed(new ToggleIndexAuto());
        OI.ControlButtonX.whenPressed(new AutoRotate(90));
        //3.048 meters = 10 feet = 4 cones on the field
        OI.DriverButtonB.whenPressed(new DriveWithAcquisitionAuto(3.048));
        OI.DriverDPadDown.whenPressed(new AutoDriveForward(3.048));
        OI.DriverDPadUp.whenPressed(new DriveAndAcquireBall());

        //Controller Settings
        OI.ControlRightBumper.whenPressed(new ToggleClimbingControls());
        OI.ControlLeftBumper.whenHeld(new JoystickSpinner());

        OI.ControlButtonY.whenPressed(new ShootMaxSpeed());
        OI.ControlButtonX.whenPressed(new ToggleAcquisitionPistons());
        OI.ControlButtonA.whenPressed(new Shoot());
        OI.ControlButtonB.whenPressed(new ToggleShooterPistons());
        OI.ControlLeftStickUp.whenHeld(new TriggerIndexer(1));
        OI.ControlLeftStickDown.whenHeld(new TriggerIndexer(-1));


    }

    /**
     * Loads a path from pathweaver into a Trajectory object
     * @return the trajectory loaded
     */
    public Trajectory loadConfig(String path)
    {
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(path);
            Trajectory pathweaverTest = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
            return pathweaverTest;
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + path, ex.getStackTrace());
        }
        System.out.println("Warning: Path not Loaded");
        return null;
    }

    public enum AutoTypes
    {
        METER,
        SNAKE,
        ACQAUTO
    }

    public String getGalacticSearch()
    {
        if(visionTable.getEntry("path").getDouble(100) == 1)
            return "A1";
        else if(visionTable.getEntry("path").getDouble(100) == 100)
        {
            System.out.println("Warning: No Entry Found");
            return "Warning: No entry found";
        }
        return "B1";

    }
    /**
     * Returns autonomous command to use
     * @return command to be used for autonomous
     */
    public Command getAutonomousCommand()
    {

        /*
        return new MoveRotateChain(new Command[] {
                new Shoot(),
                new AutoRotate(180),
                new AutoDriveForwardWhileAcquisitionRunning(2),
                new AutoRotate(180),
                new AutoDriveForward(2),
                new Shoot(),
                new AutoRotate(180)});
         */

        var autoVoltageConstraint =
                new DifferentialDriveVoltageConstraint(
                        new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                DriveConstants.kvVoltSecondsSquareMeter,
                                DriveConstants.kaVoltSecondsSquaredPerMeter),
                        DriveConstants.kDriveKinematics,
                        10);

        //Set a trajectory config, setting constraints and stuff
        TrajectoryConfig config =
                new TrajectoryConfig(DriveConstants.kMaxSpeedMetersPerSecond,
                        DriveConstants.kMaxAccelerationMetersPerSecondSquared)
                        .setKinematics(DriveConstants.kDriveKinematics)
                        .addConstraint(autoVoltageConstraint);

        //Generates a trajectory. Starts at 0,0 -> 1,1 -> 2,-1. This should draw an S pattern. Ends 3 Meters ahead
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                new Pose2d(0, 0, new Rotation2d(0)),
                List.of(
                        new Translation2d(1, 1),
                        new Translation2d(2, -1)
                ),
                new Pose2d(3, 0, new Rotation2d(0)),
                config
        );

        Trajectory trajectory2 = TrajectoryGenerator.generateTrajectory(
                new Pose2d(0, 0, new Rotation2d(0)),
                List.of(
                        new Translation2d(1, 0),
                        new Translation2d(1,1)
                ),
                new Pose2d(1, 1, new Rotation2d(3.14/2)),
                config
        );

        Trajectory meter = TrajectoryGenerator.generateTrajectory(
                new Pose2d(0, 0, new Rotation2d(0)),
                List.of(
                        new Translation2d(3, 0)
                ),
                new Pose2d(5, 0, new Rotation2d(0)),
                config
        );

        SequentialCommandGroup GalacticSearchB = new SequentialCommandGroup(
                new AutoDriveForward(7.62),
                new AutoRotate(90),
                new AutoDriveForward(1.524),
                new AutoRotate(90),
                new AutoDriveForward(15),
                new AutoRotate(180),
                new AutoDriveForward(20)
        );

        SequentialCommandGroup GalacticSearchA = new SequentialCommandGroup(
                new AutoDriveForward(2.286),
                new AutoRotate(33.69),
                new AutoDriveForward(2.7432),
                new AutoRotate(-123.69),
                new AutoDriveForward(3.6576),
                new AutoRotate(123.69),
                new AutoDriveForward(4.572)
        );

        SequentialCommandGroup VisionGalacticSearch = new SequentialCommandGroup(
                new DriveAndAcquireBall(),
                new DriveAndAcquireBall(),
                new DriveAndAcquireBall(),
                new AutoRotate(0);
                new AutoDriveForward(2);
        );


/*
        //BOX BUG
        Trajectory boxBug = TrajectoryGenerator.generateTrajectory(
                new Pose2d(0,0, new Rotation2d(0)),
                List.of(
                        new Translation2d(2,0),
                        new Translation2d(2,2),
                        new Translation2d(0,2),
                        new Translation2d(0,0),
                        new Translation2d(2,0),
                        new Translation2d(2,2),
                        new Translation2d(0,2),
                        new Translation2d(0,0),
                        new Translation2d(2,0),
                        new Translation2d(2,2),
                        new Translation2d(0,2),
                        new Translation2d(0,0)
                ),
                new Pose2d(0,0, new Rotation2d(0)),
                config
        );
        */
        Trajectory pathweaverRunner = loadConfig(trajectoryJSON);
        Trajectory gal;
        if(this.getGalacticSearch().equals("A1"))
            gal = loadConfig("paths/galacticSearchB.wpilib.json");
        else
            gal = loadConfig("paths/galacticSearchA.wpilib.json");


        RamseteCommand ramseteCommand = new RamseteCommand(
                gal,
                driveTrain::getPose,
                new RamseteController(2.0, 0.7),
                driveTrain.getFeedForward(),
                driveTrain.getKinematics(),
                driveTrain::getWheelSpeeds,
                driveTrain.getLeftPIDController(),
                driveTrain.getRightPIDController(),
                driveTrain::setVoltages,
                driveTrain
                );

        Translation2d nonPathTrans  = new Translation2d(0,0);
        Rotation2d nonPathRot = new Rotation2d(0,0);
        Pose2d nonPathPose = new Pose2d(nonPathTrans,nonPathRot);

        //If non-pathweaver paths are selected, set the parameter to "nonPathPose"
        // else set parameter to "pathweaverRunner.getInitialPose();"
        // or [name of path].getInitialPose
        driveTrain.resetOdometry(gal.getInitialPose());


        ParallelRaceGroup acqRunner = new ParallelRaceGroup(
                new ContinuallyRun(),
                ramseteCommand
        );
        SequentialCommandGroup galA = new SequentialCommandGroup(
                new ToggleAcquisitionPistons(),
                acqRunner
        );

        /*
        switch(m_chooser.getSelected())
        {
            case METER:
                return OneMeter;
            case SNAKE:
                return snake;
            case ACQAUTO:
                return acquisitionAuto;
            default:
                return OneMeter;
        }
        */
        return galA;

    }
    


    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic()
    {
        SmartDashboard.putString("PoseX", driveTrain.getPose().toString());
        SmartDashboard.putNumberArray("EncoderVal", driveTrain.getPositions());
        testEntry = visionTable.getEntry("path");
        test = testEntry.getDouble(9999);
        SmartDashboard.putNumber("networkTable test", test);
    }
    
    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit()
    {
        dataManager.connectVisions();
        //System.out.println("Auto selected: " + m_chooser.getSelected());
        CommandScheduler.getInstance().cancelAll();
        getAutonomousCommand().schedule();
    }

    public void teleopInit()
    {
        CommandScheduler.getInstance().cancelAll();
        dataManager.connectVisions();
    }
    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic()
    {
        /*
        switch (m_autoSelected) {
            case kCustomAuto:
                // Put custom auto code here
                break;
            case kDefaultAuto:
            default:
                // Put default auto code here
                break;
        }

         */
        CommandScheduler.getInstance().run();
    }


    
    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic()
    {
        CommandScheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic()
    {
    }
    
    /**
     * Returns the drive train
     * @return The drive train
     */
    public static DriveTrain getDriveTrain()
    {
        return driveTrain;
    }
    
    public static Acquisition getAcquisition() { return acquisition; }
    
    public static AcquisitionPistons getAcquisitionPistons() { return acquisitionPistons; }
    
    public static Index getIndexer() { return indexer; }
    
    public static Shooter getShooter()
    {
        return shooter;
    }
    
    public static ShooterPistons getShooterPistons() { return shooterPistons; }
    
    public static Climbing getClimbing() { return climbing; }
    
    public static Spinner getSpinner() { return spinner; }
    
    public static DataManager getDataManager() { return dataManager; }
    
    public static double getDefaultAngle()
    {
        return defaultAngle;
    }
    
    @Override
    public void disabledInit()
    {
        CommandScheduler.getInstance().cancelAll();
        super.disabledInit();
    }

    public int getClosestBall()
    {
        return (int)dataManager.getClosestBall()[0];
    }

    public double outerHorizontalAngle()
    {
        return dataManager.outerHorizontalAngle;
    }


}
