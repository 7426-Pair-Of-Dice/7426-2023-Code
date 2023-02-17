// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import frc.robot.Utility.Units;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase {

  private static TalonFX m_turretMotor;

  /** Creates a new Turret. */
  public Turret() {
    m_turretMotor = new TalonFX(Constants.Turret.kTurretMotorId);

    m_turretMotor.configFactoryDefault();

    m_turretMotor.setInverted(true);

    m_turretMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.TalonFX.kTimeoutMs);

    m_turretMotor.setSelectedSensorPosition(0);

    m_turretMotor.configNominalOutputForward(0, Constants.TalonFX.kTimeoutMs);
    m_turretMotor.configNominalOutputReverse(0, Constants.TalonFX.kTimeoutMs);
    m_turretMotor.configPeakOutputForward(1, Constants.TalonFX.kTimeoutMs);
    m_turretMotor.configPeakOutputReverse(-1, Constants.TalonFX.kTimeoutMs);

    m_turretMotor.configAllowableClosedloopError(0, 0, Constants.TalonFX.kTimeoutMs);

    m_turretMotor.selectProfileSlot(0, 0);
    m_turretMotor.config_kF(0, Constants.Turret.kF);
    m_turretMotor.config_kP(0, Constants.Turret.kP);
    m_turretMotor.config_kI(0, Constants.Turret.kI);
    m_turretMotor.config_kD(0, Constants.Turret.kD);

    m_turretMotor.configMotionCruiseVelocity(10000, Constants.TalonFX.kTimeoutMs);
    m_turretMotor.configMotionAcceleration(5000, Constants.TalonFX.kTimeoutMs);
    m_turretMotor.configMotionSCurveStrength(2);

    m_turretMotor.configForwardSoftLimitThreshold(Units.degreesToTicks(360, Constants.Turret.kMotorToTurret, Constants.TalonFX.kEncoderResolution), Constants.TalonFX.kTimeoutMs);
    m_turretMotor.configReverseSoftLimitThreshold(Units.degreesToTicks(-360, Constants.Turret.kMotorToTurret, Constants.TalonFX.kEncoderResolution), Constants.TalonFX.kTimeoutMs);

    m_turretMotor.configForwardSoftLimitEnable(true);
    m_turretMotor.configReverseSoftLimitEnable(true);

    m_turretMotor.configNeutralDeadband(0.05);
  }

  @Override
  public void periodic() {}

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Turret");
    builder.addDoubleProperty("Turret Position", this::getPosition, null);
    builder.addDoubleProperty("Turret Angle", this::getAngle, null);
  }

  public void setPercentOutput(double percentOutput) {
    m_turretMotor.set(ControlMode.PercentOutput, percentOutput);
  }

  public void setPosition(double degrees) {
    double ticks = Units.degreesToTicks(degrees, Constants.Turret.kMotorToTurret, Constants.TalonFX.kEncoderResolution);
    m_turretMotor.set(ControlMode.MotionMagic, ticks);
  }

  public void stop() {
    m_turretMotor.set(ControlMode.PercentOutput, 0);
  }

  public double getPosition() {
    return m_turretMotor.getSelectedSensorPosition();
  }

  public double getAngle() {
    return Units.ticksToDegrees(getPosition(), Constants.Turret.kMotorToTurret, Constants.TalonFX.kEncoderResolution);
  }

  public void zero() {
    m_turretMotor.setSelectedSensorPosition(0);
  }
}
