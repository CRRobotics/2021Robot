package org.team639.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

import static org.team639.lib.Constants.chassisWidth;

public class DriveConstants
{
    public static final double ksVolts = 0.132;
    public static final double kvVoltSecondsSquareMeter = 2;
    public static final double kaVoltSecondsSquaredPerMeter = 0.0209;
    //public static final double ksVolts = 0.132;
    //public static final double kvVoltSecondsSquareMeter = 5;
    //public static final double kaVoltSecondsSquaredPerMeter = 0.0209;

    public static final double kTrackwidthMeters = chassisWidth;
    public static final DifferentialDriveKinematics kDriveKinematics =
            new DifferentialDriveKinematics(kTrackwidthMeters);

    public static final double kMaxSpeedMetersPerSecond = 2;
    public static final double kMaxAccelerationMetersPerSecondSquared = 2;


    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
}
