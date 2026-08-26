package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.opmodes.auto.PedroAuto;
import org.firstinspires.ftc.teamcode.subsystems.BoardMotorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
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
    Pose here;
    private PathChain assistPath;
    @Override
    public void onInit(){
        PedroComponent.follower().setStartingPose(
                PedroAuto.lastPose == null ? new Pose(0, 0, 0) : PedroAuto.lastPose
        );
    }
    @Override
    public void onStartButtonPressed(){
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(new InstantCommand(() -> {
                    here = PedroComponent.follower().getPose();
                    buildAssistPaths(here);
                    PedroComponent.follower().followPath(assistPath);
                }))
                .whenBecomesFalse(new InstantCommand(() ->
                        PedroComponent.follower().startTeleopDrive()));
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(
                new InstantCommand(() ->
                        PedroComponent.follower().setPose(new Pose(0, 0, 0)))
        );
        Gamepads.gamepad2().a().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.spinMotor());
        Gamepads.gamepad2().b().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.stopMotor());
        Gamepads.gamepad2().x().whenBecomesTrue(BoardMotorSubsystem.INSTANCE.reverseMotor());
        Gamepads.gamepad2().y().whenBecomesTrue(
                new ParallelGroup(
                        IntakeSubsystem.INSTANCE.intakeOn(),
                        BoardMotorSubsystem.INSTANCE.spinMotor()
                )
        );
        Gamepads.gamepad2().rightBumper().whenBecomesTrue(IntakeSubsystem.INSTANCE.intakeOn());
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(IntakeSubsystem.INSTANCE.intakeOff());
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
                Gamepads.gamepad1().rightStickX().negate(),
                false
        );
        driverControlled.schedule();
    }
    @Override
    public void onUpdate(){
        Pose p = PedroComponent.follower().getPose();
        telemetry.addData("x",p.getX());
        telemetry.addData("y",p.getY());
        telemetry.addData("heading",p.getHeading());
        telemetry.addData("heading (deg)",Math.toDegrees(p.getHeading()));
        telemetry.update();
    }
    public void buildAssistPaths(Pose here){
        assistPath = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(here, new Pose(here.getX()+24, here.getY(), here.getHeading())))
                .build();
    }
    @Override
    public void onStop(){
        CommandManager.INSTANCE.cancelAll();
    }
}
