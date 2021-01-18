package org.team639.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public class DriveConstants
{
    public static final double ksVolts = 0.132;
    public static final double kvVoltSecondsSquareMeter = 0.119;
    public static final double kaVoltSecondsSquaredPerMeter = 0.0209;

    public static final double kTrackwidthMeters = 0.69;
    public static final DifferentialDriveKinematics kDriveKinematics =
            new DifferentialDriveKinematics(kTrackwidthMeters);

    public static final double kMaxSpeedMetersPerSecond = 2;
    public static final double kMaxAccelerationMetersPerSecondSquared = 2;

    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
}
