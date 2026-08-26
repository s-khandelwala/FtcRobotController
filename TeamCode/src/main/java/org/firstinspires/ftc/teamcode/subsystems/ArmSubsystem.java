package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class ArmSubsystem implements Subsystem {
    public static final ArmSubsystem INSTANCE= new ArmSubsystem();
    private MotorEx arm;
    private final ControlSystem armController = ControlSystem.builder()
            .posPid(0.005, 0.0001, 0.01)
            .build();
    private ArmSubsystem(){}
    public Command armUp(){
        return new LambdaCommand()
                .setStart(()-> armController.setGoal(new KineticState(400,0)))
                .setIsDone(()-> true)
                .requires(this);
    }
    public Command armDown(){
        return new LambdaCommand()
                .setStart(()-> armController.setGoal(new KineticState(0,0)))
                .setIsDone(()-> true)
                .requires(this);
    }
    @Override
    public void initialize(){
        arm = new MotorEx("arm");
    }
    @Override
    public void periodic(){
        arm.setPower(armController.calculate(arm.getState()));
    }
}
