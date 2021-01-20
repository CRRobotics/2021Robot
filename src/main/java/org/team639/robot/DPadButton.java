package org.team639.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;

public class DPadButton extends Button
{
    XboxController joystick;
    int direction;

    //UP = 0
    //RIGHT = 90
    //DOWN = 180
    //LEFT = 270

    public DPadButton(XboxController joystick, int direction)
    {
        this.joystick = joystick;
        this.direction = direction;
    }

    @Override
    public boolean get()
    {
        int dPadValue = joystick.getPOV();
        return (dPadValue == direction);
    }

}