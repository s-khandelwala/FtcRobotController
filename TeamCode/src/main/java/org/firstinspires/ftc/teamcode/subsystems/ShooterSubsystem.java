package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class ShooterSubsystem implements Subsystem {
    public static final ShooterSubsystem INSTANCE= new ShooterSubsystem();
    double tpr;
    double ticksPerSec;
    int rpm;
    private MotorEx shooter;
    private final ControlSystem shooterController = ControlSystem.builder()
            .velPid(0.005, 0.0001, 0.01)
            .build();
    private ShooterSubsystem(){}
    @Override
    public void initialize(){
        shooter = new MotorEx("shooter");
        tpr = shooter.getMotor().getMotorType().getTicksPerRev();
        rpm = 100;
        ticksPerSec = rpm*tpr/60;
    }
    @Override
    public void periodic(){
        shooter.setPower(shooterController.calculate(shooter.getState()));
    }
    public Command shooterOn(){
        return new LambdaCommand()
                .setStart(() -> shooterController.setGoal(new KineticState(0,ticksPerSec)))
                .setIsDone(()->true)
                .requires(this);
    }
    public Command shooterOff(){
        return new LambdaCommand()
                .setStart(() -> shooterController.setGoal(new KineticState(0,0)))
                .setIsDone(()->true)
                .requires(this);
    }
}


