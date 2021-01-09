package org.team639.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.team639.lib.Constants;
import org.team639.lib.math.PID;
import org.team639.robot.Robot;
import org.team639.robot.Subsystems.DriveTrain;

public class AutoRotate extends CommandBase
{
    
    private boolean done;
    
    private DriveTrain driveTrain = Robot.getDriveTrain();
    
    private PID pid;
    
    private double leftEncoderDiff;
    private double rightEncoderDiff;
    
    private double gyroAngleDiff;
    private double initialAngle;
    private double targetAngle;
    private double addedAngle;
    private double currentAngle;
    private boolean clockwise; // + = !clockwise, - = clockwise, just like in math
    
    private double p = .015;
    private double i = 0;
    private double d = 0.100;
    
    
    /**
     * Initializes the command
     * @param angle The degrees to rotate
     */
    public AutoRotate(double angle) {
        addRequirements(driveTrain);
        currentAngle = driveTrain.getHeading().getDegrees();
        addedAngle = angle;
        clockwise = angle < 0;
        //System.out.println("AutoRotate Constructed: " + angle);
    }
    
    /**
     * Sets the target gyro angle and the PID values
     */
    public void initialize()
    {
        clockwise = addedAngle < 0;
        done = false;
        pid = new PID(Constants.autoRotateP, Constants.autoRotateI, Constants.autoRotateD, 0.1, .16, 0.01, 0.25, 0);
        currentAngle = driveTrain.getHeading().getDegrees();
        initialAngle = driveTrain.getHeading().getDegrees();
        targetAngle = currentAngle + addedAngle;
        gyroAngleDiff = targetAngle - currentAngle;
        System.out.println("AutoRotate Initialized: " + addedAngle);
    }
    
    /**
     * Sets motor speeds based on using PID math with the angles left to rotate. Sets done to true when the
     * desired rotations have been reached or exceeded.
     */
    public void execute()
    {
        currentAngle = driveTrain.getHeading().getDegrees();
        gyroAngleDiff = targetAngle - currentAngle;
        double average = pid.compute(gyroAngleDiff / 2.0);
        if(clockwise) {
            driveTrain.setSpeeds(-average, average);
            done = gyroAngleDiff >= -1 || average >= 0;
        }
        else
        {
            driveTrain.setSpeeds(-average, average);
            done = gyroAngleDiff <= 1 || average <= 0;
        }
        /*
        SmartDashboard.putNumber("Gyro Angle Diff", gyroAngleDiff);
        SmartDashboard.putNumber("Current Angle", currentAngle);
        SmartDashboard.putNumber("Target Angle", targetAngle);
        SmartDashboard.putNumber("Initial Angle", initialAngle);
        */
    }
    
    /**
     * Returns whether the command is finished or not.
     * @return Whether the command is finished or not.
     */
    public boolean isFinished()
    {
        return done;
    }
    
    public void interrupted()
    {
        System.out.println("AutoRotate Interrupted");
        end();
    }
    
    public void end()
    {
        System.out.println("AutoRotate Ended");
        driveTrain.setSpeeds(0.0, 0.0);
        super.end(false);
    }
}