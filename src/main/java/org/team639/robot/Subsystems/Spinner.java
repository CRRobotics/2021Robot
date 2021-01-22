package org.team639.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorSensorV3.RawColor;

import org.team639.lib.math.PID;

public class Spinner implements Subsystem {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private ColorSensorV3 sensor = new ColorSensorV3(i2cPort);
    private int currentColor;

    private TalonSRX motor;

    public Spinner() {
        motor = new TalonSRX(Constants.spinnerMotorID);
    }

    public void setMotorSpeed(double speed) {
        motor.set(ControlMode.PercentOutput, speed);
    }

    public RawColor getColor()
    {
        return sensor.getRawColor();
    }

}
