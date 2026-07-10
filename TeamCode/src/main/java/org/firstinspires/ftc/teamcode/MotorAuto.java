package org.firstinspires.ftc.teamcode; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import dev.nextftc.ftc.NextFTCOpMode;


@Autonomous(name = "MotorAuto", group = "Motor")
public abstract class MotorAuto extends NextFTCOpMode {

    private Follower follower;
    private final Pose startPose = new Pose(10, 10, Math.toRadians(0)); // Start Pose of our robot. This is against the goal facing AWAY
    private final Pose bottomRightPose = new Pose(60, 10, Math.toRadians(90)); // Scoring Pose of our robot.
    private final Pose topRightPose = new Pose(60, 60, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose topLeftPose = new Pose (10, 60); // Final Pose of our robot, off the starting line
    private PathChain startToRight, rightToTop, topToLeft, leftToStart;
    public void buildPaths (){
        startToRight = follower.pathBuilder()
                .addPath(new BezierLine(startPose, bottomRightPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), bottomRightPose.getHeading())
                .build();
        rightToTop = follower.pathBuilder()
                .addPath(new BezierLine(bottomRightPose, topRightPose))
                .setLinearHeadingInterpolation(bottomRightPose.getHeading(), topRightPose.getHeading())
                .build();
        topToLeft = follower.pathBuilder()
                .addPath(new BezierLine(topRightPose, topLeftPose))
                .setLinearHeadingInterpolation(topRightPose.getHeading(), topLeftPose.getHeading())
                .build();
        leftToStart = follower.pathBuilder()
                .addPath(new BezierLine(topLeftPose, startPose))
                .setLinearHeadingInterpolation(topLeftPose.getHeading(), startPose.getHeading())
                .build();
    }
    // create routine, get follower/command, follow path = new FollowPath, cmd groups
    private Timer pathTimer, opmodeTimer;
    public static MotorSubsystem INSTANCE = new MotorSubsystem();
    private int pathState;

    boolean opModeIsActive = true;
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(startToRight);

                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Preload */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(rightToTop,true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(topToLeft,true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(leftToStart,true);
                    opModeIsActive = false;
                    setPathState(-1);
                }
                break;
        }
    }
    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    public void runOpMode() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        waitForStart();
        //on start
        opmodeTimer.resetTimer();
        setPathState(0);

        while (opModeIsActive) {
            follower.update();
            autonomousPathUpdate();

            // Feedback to Driver Hub for debugging
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();
        }
    }
}