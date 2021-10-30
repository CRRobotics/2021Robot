package org.team639.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;

public class RightJoystickButton extends Button
{
    XboxController joystick;
    double value;
    boolean isUp;
    public RightJoystickButton(XboxController joystick, boolean isUp)
    {
        this.joystick = joystick;
        this.isUp = isUp;
    }
    public boolean get()
    {
        value = joystick.getY(GenericHID.Hand.kRight);
        if(isUp == true)
            return(value > 0.1);
        else
            return(value < -.1);
    }
}
