package org.team639.robot.Subsystems;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Class for the photoelectric sensors
 */
public class PhotoelectricSensor
{
    
    private DigitalInput input;
    
    public PhotoelectricSensor(int channel)
    {
        //input = new DigitalInput(channel);
    }
    public boolean isDetected()
    {
        //return input.get();
        return false;
    }
}