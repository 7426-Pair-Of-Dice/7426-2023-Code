// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Shoulder;

public class ShoulderPreset extends CommandBase {

  private static Shoulder m_shoulder;

  private double m_shoulderAngle;

  private double m_tolerance;

  /** Creates a new ShoulderPreset. */
  public ShoulderPreset(Shoulder shoulder, double shoulderAngle, double tolerance) {

    m_shoulder = shoulder;

    m_shoulderAngle = shoulderAngle;

    m_tolerance = tolerance;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoulder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_shoulder.setSetpoint(m_shoulderAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shoulder.setPosition(m_shoulderAngle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_shoulder.atSetpoint(m_tolerance);
  }
}
