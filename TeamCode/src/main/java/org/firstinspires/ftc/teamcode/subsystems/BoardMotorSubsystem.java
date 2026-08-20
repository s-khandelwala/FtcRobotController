package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class BoardMotorSubsystem implements Subsystem {
    public static final BoardMotorSubsystem INSTANCE= new BoardMotorSubsystem();
    private final ControlSystem velocityController = ControlSystem.builder()
            .velPid(0.003,0.002,0.001)
            .basicFF(0.003,0.08,0.0)
            .build();
    private BoardMotorSubsystem(){}
    double tpr;
    double ticksPerSec;
    int rpm;
    private MotorEx motor;
    @Override
    public void initialize(){
        motor = new MotorEx("motor");
        tpr = motor.getMotor().getMotorType().getTicksPerRev();
        rpm = 100;
        ticksPerSec = rpm*tpr/60;
    }
    @Override
    public void periodic(){
        motor.setPower(velocityController.calculate(motor.getState()));
    }
    public Command stopMotor(){
        return new LambdaCommand()
                .setStart(() -> velocityController.setGoal(new KineticState(0,0)))
                .setIsDone(()->true)
                .requires(this);
    }
    public Command spinMotor(){
        return new LambdaCommand()
                .setStart(() -> velocityController.setGoal(new KineticState(0,0.5*ticksPerSec)))
                .setIsDone(()->true)
                .requires(this);
    }
    public Command reverseMotor(){
        return new LambdaCommand()
                .setStart(() -> velocityController.setGoal(new KineticState(0,-0.5*ticksPerSec)))
                .setIsDone(()->true)
                .requires(this);
    }
    public Command spinAtRpm() {
        return new RunToVelocity(velocityController, ticksPerSec)
                .requires(this);
    }
}
