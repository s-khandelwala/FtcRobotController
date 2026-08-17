package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.BoardMotorSubsystem;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
@TeleOp(name = "Practice TeleOp", group = "Practice")
public class BoardMotorNextFTCTeleOp extends NextFTCOpMode {
    public BoardMotorNextFTCTeleOp(){
        addComponents(
                new SubsystemComponent(BoardMotorSubsystem.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onStartButtonPressed(){
        Gamepads.gamepad1().a().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.spinMotor);
        Gamepads.gamepad1().b().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.stopMotor);
        Gamepads.gamepad1().x().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.reverseMotor);
    }
}
