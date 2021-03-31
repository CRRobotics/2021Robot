package org.team639.robot.Commands.Drivetrain;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.robot.OI;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.DriveTrain;
import static org.team639.lib.Constants.*;
import org.team639.lib.math.PID;


public class JoystickDrive extends CommandBase
{
    
    private DriveTrain driveTrain;

    private boolean aimBotEnabled;
    private double angleToBall;
    private double distanceToBall;
    private PID aimBotPID;

    private boolean gottaGoFast;

    private boolean officialControls = true;
    
    /*
    private CANSparkMax fifth = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax sixth = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax seventh = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax eighth = new CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax ninth = new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax tenth = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
    */
    
    
    public JoystickDrive ()
    {
        driveTrain = Robot.getDriveTrain();
        addRequirements(driveTrain);
        aimBotEnabled = false;
        aimBotPID = new PID(Constants.autoDriveForwardP, Constants.autoDriveForwardI, Constants.autoDriveForwardD, 0.01, 1, 0.01, 0.25, 0);
        /*
        fifth.restoreFactoryDefaults();
        sixth.restoreFactoryDefaults();
        seventh.restoreFactoryDefaults();
        eighth.restoreFactoryDefaults();
        ninth.restoreFactoryDefaults();
        tenth.restoreFactoryDefaults();
        */
        gottaGoFast = false;
    }
    
    public void initialize()
    {
        System.out.println("JoystickDrive Initialized");
    }
    
    public void execute()
    {
        aimBotEnabled = false;
        updateDriving();
    }
    
    /**
     * Gets input for manual driving and translates it into motor movement
     */
    private void updateDriving()
    {
        double y = 0;
        double x = 0;
        if(officialControls) {
            if(OI.DriverController.getTriggerAxis(GenericHID.Hand.kRight) != 0) {
                y = -OI.DriverController.getY(GenericHID.Hand.kLeft) * manualSpeedModifier
                        * clamp(.20, 1, 1 - OI.DriverController.getTriggerAxis(GenericHID.Hand.kRight));
                x = -OI.DriverController.getX(GenericHID.Hand.kRight) * manualSpeedModifier
                        * clamp(.20, 1, 1 - OI.DriverController.getTriggerAxis(GenericHID.Hand.kRight));
            }
            else if(OI.DriverController.getTriggerAxis(GenericHID.Hand.kLeft) != 0)
            {
                y = -OI.DriverController.getY(GenericHID.Hand.kLeft)
                        * (clamp(.6, 1, OI.DriverController.getTriggerAxis(GenericHID.Hand.kLeft) + .6));
                x = OI.DriverController.getX(GenericHID.Hand.kRight)
                        * clamp(.6, 1, OI.DriverController.getTriggerAxis(GenericHID.Hand.kLeft));
            }
            else
            {
                y = -OI.DriverController.getY(GenericHID.Hand.kLeft) * manualSpeedModifier;
                x = -OI.DriverController.getX(GenericHID.Hand.kRight) * manualSpeedModifier;
            }

            if(OI.DriverController.getY(GenericHID.Hand.kLeft) == 0) {
                driveTrain.setSpeeds(y * (1 - stillXModifier) - x * stillXModifier, y * (1 - stillXModifier) + x * stillXModifier);
            }
            else
            {
                driveTrain.setSpeeds(y * (1 - movingXModifier) - x * movingXModifier, y * (1 - movingXModifier) + x * movingXModifier);
            }
        }
        else
        {
            y = -OI.DriverController.getY(GenericHID.Hand.kRight) * manualSpeedModifier
                    * clamp(.20, 1, 1 - OI.DriverController.getTriggerAxis(GenericHID.Hand.kRight));
            x = -OI.DriverController.getX(GenericHID.Hand.kLeft) * manualSpeedModifier
                    * clamp(.20, 1, 1 - OI.DriverController.getTriggerAxis(GenericHID.Hand.kRight));
            if(y == 0) {
                driveTrain.setSpeeds(y * (1 - stillXModifier) - x * stillXModifier, y * (1 - stillXModifier) + x * stillXModifier);
            }
            else
            {
                driveTrain.setSpeeds(y * (1 - movingXModifier) - x * movingXModifier, y * (1 - movingXModifier) + x * movingXModifier);
            }
        }

    }

    /**
     * Returns maximum if input exceeds maximum input
     * @param min Minimum possible input
     * @param max Maximum possible input
     * @param input Input required
     * @return
     */
    private double clamp(double min, double max, double input)
    {
        if(input < min)
        {
            return min;
        }
        if(input > max)
        {
            return max;
        }
        return input;
    }
    
    public void end()
    {
    
    }
    
}
