package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.DataManager;
import org.team639.robot.Subsystems.DriveTrain;

import javax.sql.XAConnectionBuilder;

import static org.team639.lib.Constants.*;

public class AimBotDrive extends CommandBase
{

    private DriveTrain driveTrain;
    private XboxController controlXboxController;
    private XboxController driveXboxController;

    public AimBotDrive ()
    {
        driveTrain = Robot.getDriveTrain();
        this.controlXboxController = Robot.getControlXboxController();
        this.driveXboxController = Robot.getDrivingXboxController();
        addRequirements(driveTrain);
    }

    public void initialize()
    {
        System.out.println("JoystickDrive Initialized");
    }

    public void execute()
    {
        updateDriving();
    }

    /**
     * Gets input for manual driving and translates it into motor movement
     */
    private void updateDriving()
    {
        double y = -controlXboxController.getY(GenericHID.Hand.kRight) * manualSpeedModifier;
        double x = -controlXboxController.getX(GenericHID.Hand.kLeft) * manualSpeedModifier;
        driveTrain.setSpeeds(y * .6 - x * .3, y * .6 + x * .3);

        SmartDashboard.putNumber("Drive Position Left", driveTrain.getPositions()[0]);
        SmartDashboard.putNumber("Drive Position Right", driveTrain.getPositions()[1]);

    }

    public void end()
    {

    }

}