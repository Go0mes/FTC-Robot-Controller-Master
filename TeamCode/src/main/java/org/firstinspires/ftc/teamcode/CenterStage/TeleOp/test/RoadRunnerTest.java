package org.firstinspires.ftc.teamcode.CenterStage.TeleOp.test;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name = "RoadRunnerTest", group = "Autonomos")
public class RoadRunnerTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDT, MDF, MET, MEF;

    IMU imu;

    double orientation = 0;

    @Override
    public void runOpMode () {

        imu = hardwareMap.get(IMU.class, "imu");

        // Direção que o Drive Hub está em relação ao robô
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MET.setDirection(DcMotorSimple.Direction.REVERSE);
        MEF.setDirection(DcMotorSimple.Direction.REVERSE);

        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MET.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MEF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu.resetYaw();

        telemetry.addData("Status", "Aguardando Inicio" );
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){



        }
    }

    public void Reto ( double ganho, double powMDF, double powMDT, double powMEF, double powMET, double distanceCM){

        int distancet = (int) ((72000/50)*distanceCM);

        while (MDF.getCurrentPosition() < distancet + MDF.getCurrentPosition()) {
            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            if (orientation > 0){

                MDF.setPower(powMDF);
                MDT.setPower(powMDT);
                MEF.setPower(powMEF + ganho);
                MET.setPower(powMET + ganho);

            }else if(orientation < 0){

                MDF.setPower(powMDF + ganho);
                MDT.setPower(powMDT + ganho);
                MEF.setPower(powMEF);
                MET.setPower(powMET);

            }else {

                MDF.setPower(powMDF);
                MDT.setPower(powMDT);
                MEF.setPower(powMEF);
                MET.setPower(powMET);

            }

            Telemetry();
        }

        MDF.setPower(0);
        MDT.setPower(0);
        MEF.setPower(0);
        MET.setPower(0);


        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }


        public void Telemetry() {

            telemetry.addData("Pow MDF", MDF.getPower());
            telemetry.addData("Pow MEF", MEF.getPower());
            telemetry.addData("Pow MDT", MDT.getPower());
            telemetry.addData("Pow MET", MET.getPower());
            telemetry.addData("", "");

            telemetry.update();
         
        }
}
