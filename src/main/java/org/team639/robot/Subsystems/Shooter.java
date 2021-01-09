package org.team639.robot.Subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class Shooter {

    private CANSparkMax mainMotor;
    private static final int mainMotorID = 0;
    private static final double closePower = 1;
    private static final double farPower = 1;
    private static final double fireTime = 1; //How long the motor runs when firing


    private static final double closeHeight = 1;
    private static final double farHeight = 0;
    private boolean close;

    public Shooter()
    {
        mainMotor = new CANSparkMax(mainMotorID, CANSparkMaxLowLevel.MotorType.kBrushless);
        close = true;
    }

    /**
     * Sets the shooter to fire at the target from close (will hopefully be a set value)
     */
    public void setClose()
    {
        close = true;
        // Set Piston to closeHeight
    }

    /**
     * Sets the shooter to fire at the target from afar (will hopefully be a set value)
     */
    public void setFar()
    {
        close = false;
        // Set Piston to farHeight
    }

    /**
     * Calls either the shootClose or shootFar methods based on what mode the shooter is in
     */
    public void shoot()
    {
        if(close)
        {
            shootClose();
        }
        else
        {
            shootFar();
        }
    }

    public void shootClose()
    {
        mainMotor.set(closePower);
    }

    public void shootFar()
    {
        mainMotor.set(farPower);
    }

}
