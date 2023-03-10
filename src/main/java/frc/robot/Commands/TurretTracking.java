// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Turret;
import frc.robot.Constants;
import frc.robot.Subsystems.Limelight;

public class TurretTracking extends CommandBase {

  private static Turret m_turret;

  private static Limelight m_limelight;

  private PIDController m_pidController;

  private int m_pipeline;

  /** Creates a new TurretTracking. */
  public TurretTracking(Turret turret, Limelight limelight, int pipeline) {

    m_turret = turret;

    m_limelight = limelight;

    m_pidController = new PIDController(Constants.TurretTrackingCommand.kP, Constants.TurretTrackingCommand.kI, Constants.TurretTrackingCommand.kD);

    m_pipeline = pipeline;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turret, limelight);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_limelight.setPipeline(m_pipeline);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double error = -m_limelight.getXOffset();

    if (Math.abs(error) > 0.5) {
      m_turret.setPercentOutput(m_pidController.calculate(m_limelight.getXOffset(), 0.0) + Math.signum(error) * Constants.TurretTrackingCommand.kMinCommand);
    } else {
      m_turret.setPercentOutput(0.0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
