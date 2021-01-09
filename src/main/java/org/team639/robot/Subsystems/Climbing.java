package org.team639.robot.Subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Climbing extends SubsystemBase {
    private TalonSRX masterWinch;
    private TalonSRX followerWinch;
    private TalonSRX lift;
    private static final double MOTOR_SPEED = 0.5;
    private final int winchID1 = 2;
    private final int winchID2 = 4;
    private final int liftID = 0;
    
    public Climbing() {
        masterWinch = new TalonSRX(winchID1);
        followerWinch = new TalonSRX(winchID2);
        lift = new TalonSRX(liftID);
        
        masterWinch.configFactoryDefault();
        followerWinch.configFactoryDefault();
        lift.configFactoryDefault();
        followerWinch.follow(masterWinch);

        masterWinch.configPeakCurrentLimit(35, 10);
        masterWinch.configPeakCurrentDuration(200, 10);
        masterWinch.configContinuousCurrentLimit(30, 10);
        masterWinch.enableCurrentLimit(true);

        followerWinch.configPeakCurrentLimit(35, 10);
        followerWinch.configPeakCurrentDuration(200, 10);
        followerWinch.configContinuousCurrentLimit(30, 10);
        followerWinch.enableCurrentLimit(true);

    }
    
    public void spinLift()
    {
        lift.set(ControlMode.PercentOutput, MOTOR_SPEED);
    }
    
    public void reverseLift()
    {
        lift.set(ControlMode.PercentOutput, -MOTOR_SPEED);
    }
    
    public void setLift(double speed)
    {
        lift.set(ControlMode.PercentOutput, speed);
    }
    
    public void setWinch(double speed)
    {
        masterWinch.set(ControlMode.PercentOutput, -Math.abs(speed));
        //System.out.println("Winch ID: " + masterWinch.getDeviceID());
        // negative = good
    }
    
    public void spinWinch()
    {
    
    }
}