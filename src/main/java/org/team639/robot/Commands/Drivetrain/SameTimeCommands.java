package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SameTimeCommands extends ParallelCommandGroup
{
    public SameTimeCommands (Command[] commands)
    {
        for(Command command : commands)
        {
            addCommands(command);
        }
    }
}