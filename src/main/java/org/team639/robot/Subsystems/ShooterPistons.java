package org.team639.robot.Subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;

public class ShooterPistons implements Subsystem
{
    
    private Solenoid elevationSolenoid;
    private boolean close;
    
    public ShooterPistons()
    {
        close = true;
        elevationSolenoid = new Solenoid(Constants.shooterElevationSolenoidID);
        setFar();
    }
    
    public void toggle()
    {
        if(close)
        {
            setFar();
        }
        else
        {
            setClose();
        }
    }
    
    /**
     * Sets the shooter to fire at the target from close (will hopefully be a set value)
     */
    public void setClose()
    {
        close = true;
        // Set Piston to closeHeight
        elevationSolenoid.set(false);
    }
    
    /**
     * Sets the shooter to fire at the target from afar (will hopefully be a set value)
     */
    public void setFar()
    {
        close = false;
        // Set Piston to farHeight
        elevationSolenoid.set(true);
    }
    
    public boolean isClose()
    {
        return close;
    }
}
