package org.team639.lib;

import edu.wpi.first.wpilibj.util.Units;

public class Constants
{
    // DriveTrain
    public static final double inchesToRotations = 1.0 / (Units.inchesToMeters(6.0) * 3.14159265); // Inches to rotations conversion, diameter = 6 inches
    public static final double rotationsToEncoderUnits = 32.0;
    //public static final double driveTrainGearRatio = (10.0/33.0) * (1/2.13) * 1 / .75; //this one is wrong i believe
    public static final double driveTrainGearRatio = 0.13; //wrong!!!
    public static final double inchesToEncoderUnitsScaled = inchesToRotations * rotationsToEncoderUnits * driveTrainGearRatio;
    public static final double metersToInches = 39.37;
    public static final double metersToEncoderUnitsScaled = metersToInches * inchesToEncoderUnitsScaled;
    public static final double metersToRotations = .4747;

    public static final double manualSpeedModifier = .6;
    
    public static final double movingXModifier = .35;
    public static final double stillXModifier = .50;

    //THESE ARE WRONG
    public static final int ID1Default = 5; // Left Master
    public static final int ID3Default = 4; // Right Master
    public static final int ID2Default = 6; // Left Follower
    public static final int ID4Default = 7; // Right Follower
    
    public static final double autoDriveForwardP = 0.07;
    public static final double autoDriveForwardI = 0;
    public static final double autoDriveForwardD = 1.008;
    
    public static final double autoRotateP = 0.025;
    public static final double autoRotateI = 0;
    public static final double autoRotateD = 0.08;
    
    public static final double kS = 0.132;
    public static final double kV = 0.119;
    public static final double kA = 0.0209;
    
    public static final double shortShootingPositionDistance = 1.5;
    public static final double longShootingPositionDistance = 7;
    
    public static final double chassisWidth = .54;
    
    public static final double angleToTurning = 0.4;
    
    public static int leftSparkMasterID = 7;
    public static int leftSparkServantID = 8;
    public static int rightSparkMasterID = 9;
    public static int rightSparkServantID = 10;
    
    public static int driveTrainsmissionSolenoidID = 1;
    
    // Robot
    public static final int drivingXboxControllerPort = 1;
    public static final int controlXboxControllerPort = 0;
    
    // Shooter
    public static final double shooterP = 0.001;
    public static final double shooterI = 0.0;
    public static final double shooterD = 10.0;
    
    public static int shooterSparkMasterID = 5;
    public static int shooterSparkServantID = 6;
    public static int shooterElevationSolenoidID = 2;
    
    public static long shootingTime = 2500l; // In Milliseconds
    public static long shootingReverseTime = 1000l;
    public static long shooterSpinnerExtraTime = 500l;

    // Indexer
    public static final double GEAR_RATIO = 9;
    public static final int CHANNEL_1 = 1;
    public static final int indexerPistonsSolenoidID = 3;
    public static final int indexerMotorID1 = 6;
    public static final int indexerMotorID2 = 5;
    public static final int indexerSensorChannel = 1;
    
    // Climber
    public static final double liftSpeedModifier = 0.5;
    public static final double winchSpeedModifier = 0.5;
    public static final int controllerAxis = 1;
    public static final int climbingMotorID1 = 2;
    public static final int climbingMotorID2 = 4;
    public static final int liftMotor = 0;
    
    
    // Acquisition
    public static final double triggerInputThreshold = 0.05;
    public static final int acquisitionDoubleSolenoidForwardChannel = 4;
    public static final int acquisitionDoubleSolenoidReverseChannel = 0;
    public static final int defaultAcquisitionDoubleSolenoidValue = 0;
    public static final int acquisitionSolenoidID = 0;
    
    // Spinner
    public static final int spinnerMotorID = 7;
    
    // Sensors
    public static final int acquisitionSensorChannel = 1;
    
    // 0 = acquisition
    // 1 = transmission drive
    // 2 = shooter
    // 3 = indexer
}
