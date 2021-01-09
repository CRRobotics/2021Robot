package org.team639.robot.Commands.Spinner;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Spinner;

public class PanelSpin3Times extends CommandBase {

    boolean done;
    private int currentColor;
    private Spinner controlPanel;
    private int ColorCount;
    private int targetColor;



    public PanelSpin3Times(int rotations)
    {
        ColorCount = 0;
        targetColor = controlPanel.getColor();
        controlPanel = Robot.getSpinner();
    }

    public void updateColor()
    {
        if (currentColor == targetColor)
            ColorCount ++;
        if(ColorCount < 6)
        {
            done = true;
        }
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