package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
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
    public Command spinAtRpm;
    private MotorEx motor;
    public Command stopMotor;
    public Command spinMotor;
    public Command reverseMotor;
    @Override
    public void initialize(){
        motor = new MotorEx("motor");
        tpr = motor.getMotor().getMotorType().getTicksPerRev();
        rpm = 100;
        ticksPerSec = rpm*tpr/60;
        stopMotor = instant(()->{
            velocityController.setGoal(new KineticState(0,0));
        }).requires(this);
        spinMotor = instant(() ->{
            velocityController.setGoal(new KineticState(0,0.5*ticksPerSec));
        }).requires(this);
       reverseMotor = instant(() ->{
           velocityController.setGoal(new KineticState(0,-0.5*ticksPerSec));
        }).requires(this);
        spinAtRpm = new RunToVelocity(velocityController,ticksPerSec)
                .requires(this);
    }
    @Override
    public void periodic(){
        motor.setPower(velocityController.calculate(motor.getState()));
    }
}
