package org.team639.robot.Subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.team639.lib.Constants;

public class AcquisitionPistons implements Subsystem
{
    
    private Solenoid solenoid;
    
    public AcquisitionPistons()
    {
        solenoid = new Solenoid(Constants.acquisitionSolenoidID);
    }
    
    public boolean getSolenoidValue()
    {
        return solenoid.get();
    }
    
    public void setSolenoid(boolean status)
    {
        solenoid.set(status);
    }
    
    public void moveDown()
    {
        solenoid.set(true);
    }
    
    public void moveUp()
    {
        solenoid.set(false);
    }
    
    public void toggleSolenoid()
    {
        System.out.println("---------- ToggleAcquisitionPistons ----------");
        System.out.println("Solenoid Position: " + solenoid.get() + "  |  Changing To: " + !solenoid.get());
        solenoid.set(!solenoid.get());
    }
    
}
