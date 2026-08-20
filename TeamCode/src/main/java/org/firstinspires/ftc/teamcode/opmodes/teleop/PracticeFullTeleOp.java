package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.BoardMotorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp(name = "PracticeFullTeleOp", group = "Practice")
public class PracticeFullTeleOp extends NextFTCOpMode {
    public PracticeFullTeleOp() {
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(BoardMotorSubsystem.INSTANCE, IntakeSubsystem.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    public void onStartButtonPressed(){
        Gamepads.gamepad1().a().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.spinMotor());
        Gamepads.gamepad1().b().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.stopMotor());
        Gamepads.gamepad1().x().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.reverseMotor());
        Gamepads.gamepad1().y().whenBecomesTrue(
                new ParallelGroup(
                        IntakeSubsystem.INSTANCE.intakeOn(),
                        BoardMotorSubsystem.INSTANCE.spinMotor()
                )
        );
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(IntakeSubsystem.INSTANCE.intakeOn());
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(IntakeSubsystem.INSTANCE.intakeOff());
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
                Gamepads.gamepad1().rightStickX().negate(),
                true
        );
        driverControlled.schedule();
    }
}
