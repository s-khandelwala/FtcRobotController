package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class IntakeSubsystem implements Subsystem {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();
    private IntakeSubsystem(){}
    public ServoEx servo = new ServoEx("servo");
    public Command intakeOn(){
        return new SetPosition(servo,0.31);
    }
    public Command intakeOff(){
        return new SetPosition(servo,0.1);
    }
}
