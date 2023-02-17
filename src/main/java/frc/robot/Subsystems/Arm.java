// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Utility.Units;

public class Arm extends SubsystemBase {

  private static TalonFX m_armMotor;
  private static TalonFX m_armMotorFollower;
  private static TalonFX m_telescopeMotor;

  private static double m_telescopeZeroPosition;
  private static double m_armZeroPosition;

  private static double m_lastArmPosition;

  /** Creates a new Arm. */
  public Arm() {

    m_armMotor = new TalonFX(Constants.Arm.kLeftArmMotorId);
    m_armMotorFollower = new TalonFX(Constants.Arm.kRightArmMotorId);
    m_telescopeMotor = new TalonFX(Constants.Arm.kTelescopeMotorId);

    // Arm Configuration
    m_armMotor.configFactoryDefault();
    m_armMotorFollower.configFactoryDefault();

    m_armMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.TalonFX.kTimeoutMs);

    m_armMotor.configNominalOutputForward(0, Constants.TalonFX.kTimeoutMs);
    m_armMotor.configNominalOutputReverse(0, Constants.TalonFX.kTimeoutMs);
    m_armMotor.configPeakOutputForward(1, Constants.TalonFX.kTimeoutMs);
    m_armMotor.configPeakOutputReverse(-1, Constants.TalonFX.kTimeoutMs);

    m_armMotor.configAllowableClosedloopError(0, 0, Constants.TalonFX.kTimeoutMs);

    m_armMotor.configForwardSoftLimitThreshold(m_armZeroPosition + Units.degreesToTicks(100, Constants.Arm.kMotorToArm, Constants.TalonFX.kEncoderResolution), Constants.TalonFX.kTimeoutMs);
    m_armMotor.configReverseSoftLimitThreshold(m_armZeroPosition, Constants.TalonFX.kTimeoutMs);
    m_armMotor.configForwardSoftLimitEnable(true);
    m_armMotor.configReverseSoftLimitEnable(true);

    m_armMotor.selectProfileSlot(0, 0);
    m_armMotor.config_kF(0, 0.2);
    m_armMotor.config_kP(0, 0.08);
    m_armMotor.config_kI(0, 0.0);
    m_armMotor.config_kD(0, 0.0);

    m_armMotor.configMotionCruiseVelocity(10000, Constants.TalonFX.kTimeoutMs);
    m_armMotor.configMotionAcceleration(5000, Constants.TalonFX.kTimeoutMs);

    m_armMotor.configNeutralDeadband(0.05);
    m_armMotorFollower.configNeutralDeadband(0.05);

    m_armMotorFollower.follow(m_armMotor);
    m_armMotorFollower.setInverted(TalonFXInvertType.OpposeMaster);

    m_armMotor.setNeutralMode(NeutralMode.Brake);
    m_armMotorFollower.setNeutralMode(NeutralMode.Brake);

    // Telescope configuration
    m_telescopeMotor.configFactoryDefault();

    m_telescopeMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.TalonFX.kTimeoutMs);

    m_telescopeMotor.configNominalOutputForward(0, Constants.TalonFX.kTimeoutMs);
    m_telescopeMotor.configNominalOutputReverse(0, Constants.TalonFX.kTimeoutMs);
    m_telescopeMotor.configPeakOutputForward(1, Constants.TalonFX.kTimeoutMs);
    m_telescopeMotor.configPeakOutputReverse(-1, Constants.TalonFX.kTimeoutMs);

    m_telescopeMotor.configReverseSoftLimitThreshold(m_telescopeZeroPosition);
    m_telescopeMotor.configForwardSoftLimitThreshold(m_telescopeZeroPosition + Units.metersToTicks(Units.inchesToMeters(20), Constants.Arm.kMotorToTelescope, Constants.TalonFX.kEncoderResolution, Constants.Arm.kMetersPerRev));
    m_telescopeMotor.configReverseSoftLimitEnable(true);
    m_telescopeMotor.configForwardSoftLimitEnable(true);

    m_telescopeMotor.configNeutralDeadband(0.05);

    m_telescopeMotor.setNeutralMode(NeutralMode.Brake);

    m_telescopeMotor.setInverted(true);

    // Sets motor zero positions (where they are at the beginning of runtime)
    m_armZeroPosition = getArmPosition();
    m_telescopeZeroPosition = getTelescopePosition();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.setSmartDashboardType("Arm");
      builder.addDoubleProperty("Telescope Position Ticks", this::getTelescopePosition, null);
      builder.addDoubleProperty("Telescope Position Meters", this::getTelescopePositionMeters, null);
      builder.addDoubleProperty("Arm Position", this::getArmPosition, null);
  }

  public void setArmPercentOutput(double percentOutput) {
    m_armMotor.set(ControlMode.PercentOutput, percentOutput);
    m_lastArmPosition = getArmPosition();
  }

  public void setArmPosition(double degrees) {
    double ticks = Units.degreesToTicks(degrees, Constants.Arm.kMotorToArm, Constants.TalonFX.kEncoderResolution);
    m_lastArmPosition = ticks;
    m_armMotor.set(ControlMode.MotionMagic, ticks);
  }

  public void setArmLastPosition() {
    m_armMotor.set(ControlMode.MotionMagic, m_lastArmPosition);
  }

  public void setTelescopePercentOutput(double percentOutput) {
    m_telescopeMotor.set(ControlMode.PercentOutput, percentOutput);
  }

  public void stop() {
    m_armMotor.set(ControlMode.PercentOutput, 0);
  }

  public double getArmPosition() {
    return m_armMotor.getSelectedSensorPosition();
  }

  public double getTelescopePosition() {
    return m_telescopeMotor.getSelectedSensorPosition();
  }

  public double getTelescopeOffset() {
    return getTelescopePosition() - m_telescopeZeroPosition;
  }

  public double getTelescopePositionMeters() {
    return Units.ticksToMeters(getTelescopeOffset(), 1, Constants.TalonFX.kEncoderResolution, Constants.Arm.kMetersPerRev);
  }

  public void zero() {
    m_armMotor.setSelectedSensorPosition(0);
    m_telescopeMotor.setSelectedSensorPosition(0);
  }
}
