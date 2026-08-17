package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.BoardMotorSubsystem;

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
                new SubsystemComponent(BoardMotorSubsystem.INSTANCE),
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
    @Override
    public void onInit(){
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
        PedroComponent.follower().setStartingPose(startPose);
    }
    @Override
    public void onStartButtonPressed(){
        new SequentialGroup(
                new FollowPath(startToRight),
                BoardMotorSubsystem.INSTANCE.spinAtRpm,
                BoardMotorSubsystem.INSTANCE.stopMotor,
                new FollowPath(rightToUp),
                BoardMotorSubsystem.INSTANCE.spinAtRpm,
                BoardMotorSubsystem.INSTANCE.stopMotor,
                new FollowPath(upToLeft),
                BoardMotorSubsystem.INSTANCE.spinAtRpm,
                BoardMotorSubsystem.INSTANCE.stopMotor
        ).schedule();
    }
}
