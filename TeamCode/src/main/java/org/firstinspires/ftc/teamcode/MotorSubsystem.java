package org.firstinspires.ftc.teamcode;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class MotorSubsystem implements Subsystem {
    public static MotorSubsystem INSTANCE = new MotorSubsystem();
    ControlSystem controlSystem = ControlSystem.builder()
            .build();
    private final MotorEx motor = new MotorEx("motor");

    public Command stopMotor = instant(() -> {
        motor.setPower(0.0);
    }); // automatically requires this
    public Command setMotor = instant(() -> {
        motor.setPower(20.0);
    });
    public static Command runToPosition(ControlSystem system, double position, float velocity, KineticState tolerance) {
        return new LambdaCommand("RunToState(" + position + "," + velocity+ ")")
                .setStart(() -> system.setGoal(new KineticState(position,velocity)))
                .setIsDone(() -> system.isWithinTolerance(tolerance))
                .requires(system);
    }
//    public Command goToLocation = goToLocation(1000, position);
//    public void goToLocation (float velocity,float position){
//        new RunToState(
//                controlSystem,
//                new KineticState(
//                        position,
//                        velocity,
//                        0
//                ),
//                new KineticState(
//                        position,
//                        1000,
//                        Double.POSITIVE_INFINITY
//                )
//        );
//    }
}