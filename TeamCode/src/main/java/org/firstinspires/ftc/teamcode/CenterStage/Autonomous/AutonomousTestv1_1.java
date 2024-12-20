
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name="AutonomousTestv1_1", group="Autonomos")
public class AutonomousTestv1_1 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDF, MDT, MEF, MET;

    IMU imu;



    double orientation;

    @Override
    public void runOpMode() {

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);


        imu.initialize(new IMU.Parameters(orientationOnRobot));

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MET.setDirection(DcMotor.Direction.REVERSE);
        MEF.setDirection(DcMotor.Direction.REVERSE);

        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MDT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MET.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MEF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu.resetYaw();

        telemetry.addData("Status", "Aguardando Início");
        telemetry.update();

        waitForStart();


        while (opModeIsActive()) {
            //Giro(90, 2);

            Reto(0.5, 0.025, 35);
            SideWalk(0.5, 0.0237, 20, 1);

        }
    }



    public void Reto(double powALL, double ganho, double distanceCM){

        double distanceT =  (72000 / 100) * distanceCM;

        while (MDF.getCurrentPosition() < distanceT) {

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            if (orientation > 0) {

                MDF.setPower(powALL);
                MDT.setPower(powALL);
                MEF.setPower(powALL + ganho);
                MET.setPower(powALL + ganho);

            } else if (orientation < 0) {

                MDF.setPower(powALL + ganho);
                MDT.setPower(powALL + ganho);
                MEF.setPower(powALL);
                MET.setPower(powALL);

            } else {

                MDF.setPower(powALL);
                MDT.setPower(powALL);
                MEF.setPower(powALL);
                MET.setPower(powALL);

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

    public void Giro(double powALL, double degrees, double tolerance){
        while (orientation != degrees){

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            if (orientation > degrees + tolerance){

                MDF.setPower(-powALL);
                MDT.setPower(-powALL);
                MEF.setPower(powALL );
                MET.setPower(powALL);

            }else if(orientation < degrees - tolerance){

                MDF.setPower(powALL );
                MDT.setPower(powALL );
                MEF.setPower(-powALL);
                MET.setPower(-powALL);

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

    public void SideWalk(double powALL, double ganho, double distanceCM, double direction){

        if (direction == 1) {

            double distanceT = (74000 / 100) * distanceCM;

            while (MDT.getCurrentPosition() < distanceT) {

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > 0) {

                    MDF.setPower(-powALL);
                    MDT.setPower(powALL - ganho);
                    MET.setPower(-powALL);
                    MEF.setPower(powALL + ganho);

                } else if (orientation < 0) {

                    MDF.setPower(-powALL + ganho);
                    MDT.setPower(powALL);
                    MET.setPower(-powALL - ganho);
                    MEF.setPower(powALL);

                } else {

                    MDF.setPower(-powALL);
                    MDT.setPower(powALL);
                    MET.setPower(-powALL);
                    MEF.setPower(powALL);

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

        else{

            MDF.setPower(0);
            MDT.setPower(0);
            MEF.setPower(0);
            MET.setPower(0);

            MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        }

        if (direction == 0){

            double distanceT = -(74000 / 100) * distanceCM;

            while (MDT.getCurrentPosition() > distanceT) {

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > 0) {

                    MDF.setPower(powALL);
                    MDT.setPower(-powALL - ganho);
                    MET.setPower(powALL);
                    MEF.setPower(-powALL + ganho);

                } else if (orientation < 0) {

                    MDF.setPower(powALL + ganho);
                    MDT.setPower(-powALL);
                    MET.setPower(powALL - ganho);
                    MEF.setPower(-powALL);

                } else {

                    MDF.setPower(powALL);
                    MDT.setPower(-powALL);
                    MET.setPower(powALL);
                    MEF.setPower(-powALL);

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
        else{


            MDF.setPower(0);
            MDT.setPower(0);
            MEF.setPower(0);
            MET.setPower(0);

            MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        }



        MDF.setPower(0);
        MDT.setPower(0);
        MEF.setPower(0);
        MET.setPower(0);

        MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
