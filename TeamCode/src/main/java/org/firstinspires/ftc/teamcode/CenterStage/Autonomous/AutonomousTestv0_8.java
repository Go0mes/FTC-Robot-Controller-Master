//giro

package org.firstinspires.ftc.teamcode.CenterStage.Autonomous;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name="AutonomousTestv0_8", group="Autonomos")
public class AutonomousTestv0_8 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDF, MDT, MEF, MET;

    IMU imu;

    double ganho = 0.03;

    double orientation;
    double tolerance = 2;

    double degrees = 178;
   // em caso de angulo alvo aer 180, coloque um valor proximo.

    @Override
    public void runOpMode() {

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);


        imu.initialize(new IMU.Parameters(orientationOnRobot));

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MET.setDirection(DcMotor.Direction.REVERSE);
        MEF.setDirection(DcMotor.Direction.REVERSE);

        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MET.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MEF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu.resetYaw();

        telemetry.addData("Status", "Aguardando Início");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() ) {

            while (orientation != degrees){

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > degrees + tolerance){

                    MDF.setPower(-0.2);
                    MDT.setPower(-0.2);
                    MEF.setPower(0.2 );
                    MET.setPower(0.2);

                }else if(orientation < degrees - tolerance){

                    MDF.setPower(0.2 );
                    MDT.setPower(0.2 );
                    MEF.setPower(-0.2);
                    MET.setPower(-0.2);

                }else {

                    MDF.setPower(0);
                    MDT.setPower(0);
                    MEF.setPower(0);
                    MET.setPower(0);

                    MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


                }

                Telemetry();

            }

            MDF.setPower(0);
            MDT.setPower(0);
            MEF.setPower(0);
            MET.setPower(0);

            MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Telemetry();

        }
    }

    public void Telemetry() {
        /*telemetry.addData("MDF Position", MDF.getCurrentPosition());
        telemetry.addData("MDT Position", MDT.getCurrentPosition());
        telemetry.addData("MEF Position", MEF.getCurrentPosition());
        telemetry.addData("MET Position", MET.getCurrentPosition());*/


        telemetry.addData("Yaw", orientation);

        telemetry.addData("Runtime Seconds", runtime.seconds());
        telemetry.addData("MDF Pow", MDF.getPower());
        telemetry.addData("MEF Pow", MEF.getPower());
        telemetry.addData("MDT Pow", MDT.getPower());
        telemetry.addData("MET Pow", MET.getPower());

        telemetry.addData("MDF Posi", MDF.getCurrentPosition());
        telemetry.addData("MEF Posi", MEF.getCurrentPosition());
        telemetry.addData("MDT Posi", MDT.getCurrentPosition());
        telemetry.addData("MET Posi", MET.getCurrentPosition());

        telemetry.update();

    }
}