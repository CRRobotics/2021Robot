package org.team639.robot.Subsystems;

import edu.wpi.first.hal.sim.mockdata.PCMDataJNI;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.opencv.photo.Photo;
import org.team639.lib.Constants;
import org.team639.robot.Commands.Drivetrain.AutoDriveForward;
import org.team639.robot.Commands.Drivetrain.AutoRotate;
import org.team639.robot.Commands.Drivetrain.MoveRotateChain;
import org.team639.robot.Robot;

import java.util.ArrayList;

public class DataManager implements Subsystem
{
    private static NetworkTable visionTable;
    boolean visionConnected;
    private boolean acquisitionSensorConnected;
    
    private boolean ballDetected;
    private double[] rawBallData;
    private ArrayList<double[]> balls = new ArrayList<>();
    private ArrayList<Command> autonChain = new ArrayList<Command>();
    
    private boolean canShootInner;
    private double[] angleToOuterTarget;
    public double distanceToOuterTarget;
    
    private boolean i2cConnected;
    
    private byte[] i2cReading;
    private I2C colorSensor;
    double[] colorSensorInput = new double[5];
    public double outerHorizontalAngle;
    private double outerElevationAngle;
    
    private PhotoelectricSensor acquisitionSensor;
    //private boolean acquisitionSensorInputExists;
    private boolean acquisitionSensorInput;
    private NetworkTableEntry tableTest;
    
    private Solenoid ringLightUpper;
    private Solenoid ringLightLower;


    public void connectVisions()
    {
        visionConnected = true;
    }
    public DataManager()
    {
        visionConnected = false;
        i2cConnected = false;
        acquisitionSensorConnected = false;
        ringLightUpper = new Solenoid(4);
        ringLightLower = new Solenoid(5);

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        visionTable = inst.getTable("CameraTracker");
        inst.startClientTeam(639);
        inst.startDSClient();

        colorSensor = new I2C(I2C.Port.kOnboard, 0x18);
        i2cReading = new byte[5];
        colorSensor.readOnly(i2cReading, 5);
        
        acquisitionSensor = new PhotoelectricSensor(Constants.acquisitionSensorChannel);

        updateNetworkTables();
        updateI2C();
        
        disableUpperRingLight();
        enableLowerLight();

    }
    
    public void periodic()
    {
        if(visionConnected)
            enableUpperLight();

        updateNetworkTables();
        if(i2cConnected) {
            updateI2C();
        }
    }
    
    public void updateNetworkTables()
    {
        tableTest = visionTable.getEntry("path");
        rawBallData = visionTable.getEntry("balls").getDoubleArray(new double[] {0, 0});
        SmartDashboard.putNumber("BallDistance", rawBallData[0]);
        System.out.println("------NETWORKTEST: " + tableTest);
        //ballDetected = rawBallData.length > 0;
        //canShootInner = visionTable.getEntry("InnerTargetPossible").getDouble(0) == 1;
        //NetworkTableEntry outerAngle = visionTable.getEntry("OuterHorizontalAngle");
        //outerElevationAngle = visionTable.getEntry("OuterElevationAngle").getDouble(0);
        //angleToOuterTarget = new double[] {outerElevationAngle, outerElevationAngle};
        //NetworkTableEntry distanceToOuterTarget = visionTable.getEntry("OuterHorizontalDistance");
        //SmartDashboard.putNumberArray("RawBallData", rawBallData);
        //SmartDashboard.putBoolean("BallDetected", ballDetected);

        //System.out.println("Angle: " + outerAngle.getDouble(1));
        //System.out.println("Distance: " + distanceToOuterTarget.getDouble(1));

        /*String str = "";
        for(double i : rawBallData)
        {
            str += i + ", ";
        } */
      //  System.out.println("The existence of ball data is " + visionTable.getEntry("balls").exists());
      //  System.out.println("ball: " + str);
      //  System.out.println("The existence of target data is " + visionTable.getEntry("OuterHorizontalDistance").exists());
        //System.out.println("Distance: " + distanceToOuterTarget + ", Angle: " + outerHorizontalAngle);

    }
    
    private void updateI2C()
    {
        for(int i = 0; i < i2cReading.length; i++)
        {
            colorSensorInput[i] = i2cReading[i];
        }
        //SmartDashboard.putNumberArray("COLORS", colorSensorInput);
        //SmartDashboard.putString("Color Values: ", colorSensorInput[0] + ", " + colorSensorInput[1] + ", " +
        //        colorSensorInput[2] + ", " + colorSensorInput[3] + ", " + colorSensorInput[4]);
    }
    
    private void updateAcquisitionSensor()
    {
        acquisitionSensorInput = acquisitionSensor.isDetected();
    }
    
    public double[] getBallDistances()
    {
        double[] ballDistances = new double[balls.size()];
        for(int i = 0; i < balls.size(); i++)
        {
            ballDistances[i] = balls.get(i)[0];
        }
        return ballDistances;
    }
    
    public boolean getAcquisitionSensorInput()
    {
        return acquisitionSensorInput;
    }
    
    public ArrayList<double[]> getBalls()
    {
        ArrayList<double[]> arr = new ArrayList<>();
        for(int i = 1; i < balls.size(); i++)
        {
            arr.add(new double[] {balls.get(i)[0], balls.get(i)[1]});
        }
        return arr;
    }
    
    public double[] getClosestBall()
    {
        int closest = 0;
        for(int i = 1; i < balls.size(); i++)
        {
            if(balls.get(i)[0] < balls.get(closest)[0])
            {
                closest = i;
            }
        }
        return new double[] {balls.get(closest)[0], balls.get(closest)[1]};
    }
    
    public boolean isBallDetected()
    {
        return ballDetected;
    }
    
    public double[] getRawBallData()
    {
        return rawBallData;
    }
    
    public double getDistanceToOuterTarget()
    {
        String str = "" + distanceToOuterTarget;
        //System.out.println("Reported Distance = " + str);
        return distanceToOuterTarget;
    }
    
    public double getHorizontalAngleToOuterTarget()
    {
        return outerHorizontalAngle;
    }
    
    public void enableUpperLight()
    {
        ringLightUpper.set(true);
    }
    
    public void toggleUpperRingLight()
    {
        ringLightUpper.set(!ringLightUpper.get());
    }
    
    public void disableUpperRingLight()
    {
        ringLightUpper.set(false);
    }

    public void enableLowerLight()
    {
        ringLightLower.set(true);
    }

    public void toggleLowerRingLight()
    {
        ringLightLower.set(!ringLightLower.get());
    }

    public void disableLowerRingLight()
    {
        ringLightLower.set(false);
    }
    
}
