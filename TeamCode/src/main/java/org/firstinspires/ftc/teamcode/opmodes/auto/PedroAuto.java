package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.BoardMotorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.CommandManager;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Autonomous(name = "Practice Auto", group = "Practice")
public class PedroAuto extends NextFTCOpMode {
    public PedroAuto(){
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(BoardMotorSubsystem.INSTANCE, IntakeSubsystem.INSTANCE),
                BulkReadComponent.INSTANCE
        );
    }
    private final Pose startPose = new Pose(0,0,Math.toRadians(0));
    private final Pose bottomRight = new Pose(60,0,Math.toRadians(90));
    private final Pose topRight = new Pose(60,60,Math.toRadians(180));
    private final Pose topLeft = new Pose(0,60, Math.toRadians(270));

    private PathChain startToRight;
    private PathChain rightToUp;
    private PathChain upToLeft;
    public static Pose lastPose;
    @Override
    public void onInit(){
        buildPaths();
        PedroComponent.follower().setStartingPose(startPose);
    }
    public void buildPaths(){
        startToRight  = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose,bottomRight))
                .setLinearHeadingInterpolation(startPose.getHeading(),bottomRight.getHeading())
                .build();
        rightToUp  = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(bottomRight,topRight))
                .setLinearHeadingInterpolation(bottomRight.getHeading(),topRight.getHeading())
                .build();
        upToLeft  = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(topRight,topLeft))
                .setLinearHeadingInterpolation(topRight.getHeading(),topLeft.getHeading())
                .build();
    }
    @Override
    public void onStartButtonPressed(){
        new SequentialGroup(
                driveWithIntake(startToRight),
                spinThenStop(),
                new FollowPath(rightToUp),
                IntakeSubsystem.INSTANCE.intakeOff(),
                spinThenStop(),
                new FollowPath(upToLeft),
                spinThenStop()
        ).schedule();
    }
    @Override
    public void onStop() {
        lastPose = PedroComponent.follower().getPose();
        CommandManager.INSTANCE.cancelAll();
    }
    public Command driveWithIntake(PathChain path){
        return new ParallelGroup(
                new FollowPath(path),
                IntakeSubsystem.INSTANCE.intakeOn()
        );
    }
    public Command spinThenStop(){
        return new SequentialGroup(
                BoardMotorSubsystem.INSTANCE.spinAtRpm(),
                new Delay(0.5),
                BoardMotorSubsystem.INSTANCE.stopMotor()
        );
    }
}
