package org.team639.robot.Commands.Spinner;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.revrobotics.ColorSensorV3.RawColor;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Spinner;

public class SpinToColor extends CommandBase {

    private boolean done;
    private Spinner controlPanel;
    private RawColor targetColor;

    public SpinToColor(RawColor targetColor)
    {
        this.targetColor = targetColor;
        controlPanel = Robot.getSpinner();
    }

    public void updateColor()
    {
        RawColor currentColor = controlPanel.getColor();
        if (currentColor == targetColor) { done = true; }
    }

    @Override
    public void initialize()
    {
        done = false;
        controlPanel.setMotorSpeed(0.5);
    }

    @Override
    public void execute()
    {
        updateColor();
        if(isDone())
            end(true);
    }

    public boolean isDone()
    {
        return done;
    }

    @Override
    public void end(boolean interrupted)
    {
        controlPanel.setMotorSpeed(0);
        System.out.println("Spin 3 Times Finished");
    }
}