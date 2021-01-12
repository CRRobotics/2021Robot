package org.team639.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.team639.robot.Commands.Acquisition.ManualAcquisition;
import org.team639.robot.Commands.Acquisition.ToggleAcquisitionPistons;
import org.team639.robot.Commands.Climbing.JoystickClimb;
import org.team639.robot.Commands.Climbing.ToggleClimbingControls;
import org.team639.robot.Commands.Drivetrain.*;
import org.team639.robot.Commands.Indexer.ToggleIndexAuto;
import org.team639.robot.Commands.Indexer.TriggerIndexer;
import org.team639.robot.Commands.Shooter.Shoot;
import org.team639.robot.Commands.Shooter.ShootMaxSpeed;
import org.team639.robot.Commands.Shooter.ToggleShooterPistons;
import org.team639.robot.Commands.Spinner.JoystickSpinner;
import org.team639.robot.Subsystems.*;
import org.team639.lib.Constants;


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
    private final SendableChooser<String> m_chooser = new SendableChooser<>();
    
    private static DriveTrain driveTrain;
    private static Acquisition acquisition;
    private static AcquisitionPistons acquisitionPistons;
    private static Index indexer;
    private static Shooter shooter;
    private static ShooterPistons shooterPistons;
    private static Climbing climbing;
    public static boolean climbingJoysticksEnabled;
    private static Spinner spinner;
    private static DataManager dataManager;

    private static double defaultAngle; // In degrees
    
    private static XboxController drivingXboxController;
    private static XboxController controlXboxController;

    //private static AcquisitionToIndexer acquisitionToIndexer = new AcquisitionToIndexer();
    //private static SenseIndex sensor = new SenseIndex();
    

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit()
    {
        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        m_chooser.addOption("My Auto", kCustomAuto);
        
        driveTrain = new DriveTrain();
        acquisition = new Acquisition();
        acquisitionPistons = new AcquisitionPistons();
        indexer = new Index();
        shooter = new Shooter();
        shooterPistons = new ShooterPistons();
        climbing = new Climbing();
        climbingJoysticksEnabled = false;
        spinner = new Spinner();
        dataManager = new DataManager();
        
        defaultAngle = driveTrain.getHeading().getDegrees();
        
        setUpXboxController();
        setUpCommands();
        
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
        
        CommandScheduler.getInstance().setDefaultCommand(driveTrain, new JoystickDrive());
        CommandScheduler.getInstance().setDefaultCommand(acquisition, new ManualAcquisition());
        CommandScheduler.getInstance().setDefaultCommand(indexer, new TriggerIndexer());
        
        //CommandScheduler.getInstance().setDefaultCommand(shooter, new JoystickShoot());
        CommandScheduler.getInstance().setDefaultCommand(climbing, new JoystickClimb());
        CommandScheduler.getInstance().setDefaultCommand(spinner, new JoystickSpinner());
    }
    
    /**
     * Maps all of the buttons used on xBoxController.
     */
    private void setUpXboxController()
    {
        drivingXboxController = new XboxController(Constants.drivingXboxControllerPort);
        controlXboxController = new XboxController(Constants.controlXboxControllerPort);
        JoystickButton aButton = new JoystickButton(controlXboxController, XboxController.Button.kA.value);
        JoystickButton bButton = new JoystickButton(controlXboxController, XboxController.Button.kB.value);
        JoystickButton xButton = new JoystickButton(controlXboxController, XboxController.Button.kX.value);
        JoystickButton yButton = new JoystickButton(controlXboxController, XboxController.Button.kY.value);
        JoystickButton leftControlBumper = new JoystickButton(controlXboxController, XboxController.Button.kBumperRight.value);

        JoystickButton xDriveButton = new JoystickButton(drivingXboxController, XboxController.Button.kX.value);
        JoystickButton aDriveButton = new JoystickButton(drivingXboxController, XboxController.Button.kA.value);
        JoystickButton yDriveButton = new JoystickButton(drivingXboxController, XboxController.Button.kY.value);
        JoystickButton rightDriveBumper = new JoystickButton(drivingXboxController, XboxController.Button.kBumperRight.value);

        rightDriveBumper.whenReleased(new ToggleDriveTrainGears());
        //yDriveButton.whenPressed(new MoveRotateChain(new Command[] {yDriveButton.whenPressed(yDriveButton.whenPressed(}));
        yDriveButton.whenPressed(new AutoRotateToTarget());
        aDriveButton.whenPressed(new ToggleIndexAuto());

        leftControlBumper.whenPressed(new ToggleClimbingControls());
        yButton.whenPressed(new ShootMaxSpeed());
        xButton.whenPressed(new ToggleAcquisitionPistons());
        aButton.whenPressed(new Shoot());
        bButton.whenPressed(new ToggleShooterPistons());
    }


    
    public Command getAutonomousCommand()
    {
    /*
        TrajectoryConfig config = new TrajectoryConfig(1, 1);
        config.setKinematics(driveTrain.getKinematics());
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                Arrays.asList(new Pose2d(), new Pose2d(new Translation2d(1.0, 0), new Rotation2d())), config
        );
    
        RamseteCommand ramseteCommand = new RamseteCommand(
                trajectory,
                driveTrain::getPose,
                new RamseteController(2.0, 0.7),
                driveTrain.getFeedForward(),
                driveTrain.getKinematics(),
                driveTrain::getSpeeds,
                driveTrain.getLeftPIDController(),
                driveTrain.getRightPIDController(),
                driveTrain::setVoltages,
                driveTrain
        );
        
        return ramseteCommand;
        
     */
        return new MoveRotateChain(new Command[] {
                new Shoot(),
                new AutoRotate(180),
                new AutoDriveForwardWhileAcquisitionRunning(2),
                new AutoRotate(180),
                new AutoDriveForward(2),
                new Shoot(),
                new AutoRotate(180)});
    }
    
    
    /**
     * Returns the XBoxController used for driving.
     * @return The XBoxController used for driving.
     */
    public static XboxController getDrivingXboxController()
    {
        return drivingXboxController;
    }
    /**
     * Returns the XBoxController used for extra controlling.
     * @return The XBoxController used for extra controlling.
     */
    public static XboxController getControlXboxController() { return controlXboxController; }

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
        m_autoSelected = m_chooser.getSelected();
        m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
        System.out.println("Auto selected: " + m_autoSelected);
        CommandScheduler.getInstance().cancelAll();
        getAutonomousCommand().schedule();
    }
    
    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic()
    {
        switch (m_autoSelected) {
            case kCustomAuto:
                // Put custom auto code here
                break;
            case kDefaultAuto:
            default:
                // Put default auto code here
                break;
        }
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
        super.disabledInit();
        //CommandScheduler.getInstance().cancelAll();
    }
}
