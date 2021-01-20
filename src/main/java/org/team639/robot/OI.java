package org.team639.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import java.awt.*;

public class OI
{

    public static XboxController DriverController = new XboxController(0);
    public static XboxController ControlController = new XboxController(1);

    //Driver XBOX Controller
    public static DPadButton DriverDPadUp = new DPadButton(DriverController, 0);
    public static DPadButton DriverDPadRight = new DPadButton(DriverController, 90);
    public static DPadButton DriverDPadDown = new DPadButton(DriverController, 180);
    public static DPadButton DriverDPadLeft = new DPadButton(DriverController, 270);

    public static JoystickButton DriverButtonY = new JoystickButton(DriverController, XboxController.Button.kY.value);
    public static JoystickButton DriverButtonX = new JoystickButton(DriverController, XboxController.Button.kX.value);
    public static JoystickButton DriverButtonA = new JoystickButton(DriverController, XboxController.Button.kA.value);
    public static JoystickButton DriverButtonB = new JoystickButton(DriverController, XboxController.Button.kB.value);

    public static JoystickButton DriverRightBumper = new JoystickButton(DriverController, XboxController.Button.kBumperRight.value);
    public static JoystickButton DriverLeftBumper = new JoystickButton(DriverController, XboxController.Button.kBumperLeft.value);

    //Controller XBOX Controller
    public static DPadButton ControlDPadUp = new DPadButton(DriverController, 0);
    public static DPadButton ControlDPadRight = new DPadButton(DriverController, 90);
    public static DPadButton ControlDPadDown = new DPadButton(DriverController, 180);
    public static DPadButton ControlDPadLeft = new DPadButton(DriverController, 270);

    public static JoystickButton ControlButtonY = new JoystickButton(DriverController, XboxController.Button.kY.value);
    public static JoystickButton ControlButtonX = new JoystickButton(DriverController, XboxController.Button.kX.value);
    public static JoystickButton ControlButtonA = new JoystickButton(DriverController, XboxController.Button.kA.value);
    public static JoystickButton ControlButtonB = new JoystickButton(DriverController, XboxController.Button.kB.value);

    public static JoystickButton ControlRightBumper = new JoystickButton(DriverController, XboxController.Button.kBumperRight.value);
    public static JoystickButton ControlLeftBumper = new JoystickButton(DriverController, XboxController.Button.kBumperLeft.value);

}
