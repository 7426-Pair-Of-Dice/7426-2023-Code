// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Wrist;

public class WristPreset extends CommandBase {

  private static Wrist m_wrist;

  private double m_wristAngle;

  private double m_tolerance;

  /** Creates a new ShoulderPreset. */
  public WristPreset(Wrist wrist, double wristAngle, double tolerance) {

    m_wrist = wrist;

    m_wristAngle = wristAngle;

    m_tolerance = tolerance;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(wrist);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_wrist.setSetpoint(m_wristAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_wrist.setPosition(m_wristAngle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_wrist.atSetpoint(m_tolerance);
  }
}
