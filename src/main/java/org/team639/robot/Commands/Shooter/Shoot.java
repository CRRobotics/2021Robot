package org.team639.robot.Commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.team639.lib.Constants;
import org.team639.robot.Commands.Indexer.MoveBallsToTop;
import org.team639.robot.Commands.Indexer.MoveBallsToTopShooting;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.Acquisition;
import org.team639.robot.Subsystems.Index;
import org.team639.robot.Subsystems.Shooter;

import static org.team639.lib.Constants.shootingTime;
import static org.team639.robot.Subsystems.Shooter.rpm;

public class Shoot extends CommandBase
{
    
    private Index indexer;
    private Shooter shooter;
    private long startMillis;
    private long waitTimeMillis;
    private boolean done;
    
    
    public Shoot()
    {
        indexer = Robot.getIndexer();
        shooter = Robot.getShooter();
        done = false;
        addRequirements(shooter);
    }
    
    public void initialize()
    {
        Robot.getDataManager().enableUpperLight();
        done = false;
        //indexer.retractPistons();
        CommandScheduler.getInstance().schedule(new MoveBallsToTopShooting());
        startMillis = System.currentTimeMillis();
        waitTimeMillis = shootingTime + Constants.shooterSpinnerExtraTime;
        shooter.shoot();
    }
    
    public void execute()
    {
        shooter.BangBangControl();
        if(System.currentTimeMillis() - startMillis >= waitTimeMillis)
        {
            indexer.turnOff();
            done = true;
        }
        if(done)
        {
            shooter.stop();
            System.out.println("Shoot DONE ------- DONE ------- DONE");
            end(false);
        }
    }
    
    
    public boolean isFinished()
    {
        return done;
    }
    
    public void interrupted()
    {
        end(true);
    }
    
    @Override
    public void end(boolean interrupted)
    {
        System.out.println("Shoot ENDED ------- ENDED ------- ENDED");
        shooter.stop();
        super.end(false);
        Robot.getDataManager().disableUpperRingLight();
    }
    
}
