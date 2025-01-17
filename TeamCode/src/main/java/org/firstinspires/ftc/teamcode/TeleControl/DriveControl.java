package org.firstinspires.ftc.teamcode.TeleControl;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Robot;
import org.firstinspires.ftc.teamcode.Util.FallingEdge;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;

@Config
public class DriveControl implements Control {
    public static boolean outreachMode = false;
    public static boolean overrideControl = false;
    public static double throttled = 1;

    public Drivebase drivebase;
    public Robot robot;
    public Gamepad gp1, gp2;
    
    public static double SLOW_MOVE = 0.5;


    public DriveControl(Robot robot, Gamepad gp1, Gamepad gp2) {
        this(robot.drive, gp1, gp2);
        this.robot = robot;
    }

    public DriveControl(Drivebase d, Gamepad gp1, Gamepad gp2) {
        this.drivebase = d;
        this.gp1 = gp1;
        this.gp2 = gp2;
        drivebase.drive.startTeleopDrive();
    }

    @Override
    public void update() {
        if(outreachMode){
            if(gp2.left_stick_x != 0 || gp2.right_stick_x != 0 || gp2.left_stick_y != 0){
                overrideControl = true;
            }
            if(gp1.touchpad){
                overrideControl = false;
            }
            Gamepad cc = (overrideControl) ? gp2 : gp1;

            drivebase.drive.setTeleOpMovementVectors(-throttled*cc.left_stick_y, -cc.left_stick_x * throttled, -throttled * cc.right_stick_x);


            return;
        }


        throttled = (gp1.right_bumper | gp1.left_bumper) ? SLOW_MOVE : 1;

        if (!drivebase.driveDisable) {
            drivebase.drive.setTeleOpMovementVectors(throttled * (-gp1.left_stick_y), -gp1.left_stick_x * throttled, -gp1.right_stick_x * throttled);
        }
        drivebase.update();
    }

    public void addTelemetry(Telemetry telemetry) {

    }
}
