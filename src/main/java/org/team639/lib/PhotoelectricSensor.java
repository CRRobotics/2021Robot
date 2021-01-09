package org.team639.lib;

import edu.wpi.first.wpilibj.DigitalInput;

public class PhotoelectricSensor
{

    private DigitalInput input;
    public PhotoelectricSensor(int channel)
    {
        input = new DigitalInput(channel);
    }
    public boolean isDetected()
    {
        return input.get();
    }
}
