package org.team639.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;

public class JoystickButton extends Button
{
    XboxController joystick = new XboxController(1);
    int value = 0;
    public JoystickButton(XboxController joystick, int value)
    {
        this.joystick = joystick;
        this.value = value;
    }
    public boolean get()
    {
        return(value > 0);
    }
}
