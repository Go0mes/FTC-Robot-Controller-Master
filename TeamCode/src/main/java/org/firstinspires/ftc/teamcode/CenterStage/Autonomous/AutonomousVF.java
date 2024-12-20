
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name="AutonomousVF", group="Autonomos")
public class AutonomousVF extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDF, MDT, MEF, MET, ElbowA, ElbowB, Slider, Wrist;
    private Servo srvPinD, srvPinE;
    int WristPos = 1;
    double josue = 0,gabriel = 0, luan = 0, TempoAlvoPulso, TempoMovPul;

    double srvPinEPosi, srvPinDPosi;

    IMU imu;

    double orientation;

    double distanceTL, distanceTR;

    @Override
    public void runOpMode() {

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.DOWN;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);


        imu.initialize(new IMU.Parameters(orientationOnRobot));

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        ElbowA = hardwareMap.get(DcMotor.class, "ElbowA");
        ElbowB = hardwareMap.get(DcMotor.class, "ElbowB");

        Slider = hardwareMap.get(DcMotor.class, "Slider");
        Wrist = hardwareMap.get(DcMotor.class, "Wrist");

        srvPinD = hardwareMap.get(Servo .class, "srvPinD");
        srvPinE = hardwareMap.get(Servo.class, "srvPinE");

        MET.setDirection(DcMotor.Direction.REVERSE);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        ElbowA.setDirection(DcMotor.Direction.FORWARD);
        ElbowB.setDirection(DcMotor.Direction.REVERSE);
        Slider.setDirection(DcMotor.Direction.FORWARD);
        Wrist.setDirection(DcMotor.Direction.FORWARD);

        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MDT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MET.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MEF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElbowA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ElbowA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ElbowB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ElbowB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slider.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Wrist.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu.resetYaw();

        srvPinD.setPosition(0);
        srvPinDPosi = srvPinD.getPosition();

        srvPinE.setPosition(1);
        srvPinEPosi = srvPinE.getPosition();

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            //Giro(90, 2);

            //Reto(0.025, 50);
            // direita   SideWalk(0.0237, 100, 1);
            //  SideWalk(0.0237, 100, 1);
            Telemetry();

            Reto(0.025, -60, 0.33);
            sleep(350);
            SideWalk(0.0237, -50, 0, 0.29);
            ElbowUp(1, 0, -0.43, 2700);
            Reto(0.025, -14, 0.1);

            Pinça(0, 1, 0, 1, 1, 0);
            sleep(1000);

            Reto(0.025, 5, 0.17);
            SideWalk(0.0237, -24, 0, 0.29);
            Pinça(1, 0, 0, 1, 1, 0);
            sleep(1000);

            Pinça(1, 1, 0, 1, 1, 0);
            ElbowUp(0, 1, -0.35, 450);
            Reto(0.025, 10, 0.2);
            SideWalk(0.0237, -50, 0 , 0.35);
            Reto(0.025, -30, 0.2);




            //  Pinça(1, 1, 0, 1, 1, 0);


            break;
            //ElbowUp(-0.5, 2700);
        }
    }

    public void WalkTimer(double powMDF, double powMDT, double powMEF, double powMET, float duration) {

        double TargetTime = runtime.seconds() + duration;

        while(runtime.seconds() < TargetTime){

            MDF.setPower(powMDF);
            MDT.setPower(powMDT);
            MEF.setPower(powMEF);
            MET.setPower(powMET);

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

    public void Reto(double ganho, double distanceCM, double power) {

        distanceTR = ((72000 / 100) * distanceCM) + MDF.getCurrentPosition();

        if (distanceTR > 0) {
            while (MDF.getCurrentPosition() < distanceTR) {

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > 0) {

                    MDF.setPower(power);
                    MDT.setPower(power);
                    MEF.setPower(power + ganho);
                    MET.setPower(power + ganho);

                } else if (orientation < 0) {

                    MDF.setPower(power + ganho);
                    MDT.setPower(power + ganho);
                    MEF.setPower(power);
                    MET.setPower(power);

                } else {

                    MDF.setPower(power);
                    MDT.setPower(power);
                    MEF.setPower(power);
                    MET.setPower(power);

                }

                Telemetry();

            }
        }else if (distanceTR < 0){

            while (MDF.getCurrentPosition() > distanceTR) {

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > 0) {

                    MDF.setPower(-power - ganho);
                    MDT.setPower(-power - ganho);
                    MEF.setPower(-power);
                    MET.setPower(-power);

                } else if (orientation < 0) {

                    MDF.setPower(-power);
                    MDT.setPower(-power);
                    MEF.setPower(-power - ganho);
                    MET.setPower(-power - ganho);

                } else {

                    MDF.setPower(-power);
                    MDT.setPower(-power);
                    MEF.setPower(-power);
                    MET.setPower(-power);

                }

                Telemetry();

            }

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

    public void Giro(double degrees, double tolerance, double power) {
        while (orientation != degrees) {

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            if (orientation > degrees + tolerance) {

                MDF.setPower(-power);
                MDT.setPower(-power);
                MEF.setPower(power);
                MET.setPower(power);

            } else if (orientation < degrees - tolerance) {

                MDF.setPower(power);
                MDT.setPower(power);
                MEF.setPower(-power);
                MET.setPower(-power);

            } else {

                MDF.setPower(0);
                MDT.setPower(0);
                MEF.setPower(0);
                MET.setPower(0);

                MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                imu.resetYaw();

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

    public void SideWalk(double ganho, double distanceCM, double direction, double power) {

        distanceTL = ((74000 / 100) * distanceCM) + MDT.getCurrentPosition();

        if (direction == 1) {

            while (MDT.getCurrentPosition() < distanceTL) {

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > 0) {

                    MDF.setPower(-power);
                    MDT.setPower(power - ganho);
                    MET.setPower(-power);
                    MEF.setPower(power + ganho);

                } else if (orientation < 0) {

                    MDF.setPower(-power + ganho);
                    MDT.setPower(power);
                    MET.setPower(-power - ganho);
                    MEF.setPower(power);

                } else {

                    MDF.setPower(-power);
                    MDT.setPower(power);
                    MET.setPower(-power);
                    MEF.setPower(power);

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

        } else {

            MDF.setPower(0);
            MDT.setPower(0);
            MEF.setPower(0);
            MET.setPower(0);

            MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        }

        if (direction == 0) {

            while (MDT.getCurrentPosition() > distanceTL) {

                orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                if (orientation > 0) {

                    MDF.setPower(power);
                    MDT.setPower(-power - ganho);
                    MET.setPower(power);
                    MEF.setPower(-power + ganho);

                } else if (orientation < 0) {

                    MDF.setPower(power + ganho);
                    MDT.setPower(-power);
                    MET.setPower(power - ganho);
                    MEF.setPower(-power);

                } else {

                    MDF.setPower(power);
                    MDT.setPower(-power);
                    MET.setPower(power);
                    MEF.setPower(-power);

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


        } else {


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
    } //0 - Esquerda, 1 - Direita

    public void ElbowUp(double Frente, double Tras, double ElbowPow, double target) {
        Telemetry();

        if (Frente == 1 && Tras == 0) {

            josue =1;
            while (-ElbowA.getCurrentPosition() < target) {

                ElbowA.setPower(ElbowPow);
                ElbowB.setPower(ElbowPow);
                telemetry.update();
                Telemetry();

            }
            Frente = 0;


        } else if (Tras == 1 && Frente == 0) {

            gabriel = 1;
            while (-ElbowA.getCurrentPosition() > target) {

                ElbowA.setPower(-ElbowPow);
                ElbowB.setPower(-ElbowPow);
                telemetry.update();
                Telemetry();


            }
            Tras = 0;

        }
        else{

            ElbowA.setPower(0);
            ElbowB.setPower(0);
            ElbowA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            ElbowB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }


        ElbowA.setPower(0);
        ElbowB.setPower(0);
        ElbowA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ElbowB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Telemetry();

         /*   ElbowA.setPower(ElbowPow);
            ElbowB.setPower(ElbowPow);

            sleep(temposeg*1000);

            ElbowB.setPower(ElbowEst);
            ElbowA.setPower(ElbowEst);
           */
    }

    public void Pulso(double Power) {

        double TempoEx = runtime.seconds() + 0.4;

        while(runtime.seconds() < TempoEx){

            if (WristPos == 0) { // Para Cima

                Wrist.setPower(-Power);
                WristPos = 1;

                Telemetry();

            } else if (WristPos == 1) { // Para Baixo

                Wrist.setPower(Power);
                WristPos = 0;

                Telemetry();

            }
        }

        Wrist.setPower(0);
    }

    public void Pinça(double MoveL, double MoveR, double srvPinEPosA, double srvPinEPosB, double srvPinDPosA, double srvPinDPosB){

        if(MoveL == 1){

            //Condições que verificam a posição que o servo está, alternando sua posição entre a primeira e a segunda posição
            if (srvPinEPosi == srvPinEPosA){ //Se o servo estiver na posição A

                srvPinE.setPosition(srvPinEPosB);
                srvPinEPosi = srvPinEPosB;

            } else if (srvPinEPosi == srvPinEPosB){ //Se o servo estiver na posição B

                srvPinE.setPosition(srvPinEPosA);
                srvPinEPosi = srvPinEPosA;

            }else{ //Se o servo não estiver em nenhuma das duas posições
                srvPinE.setPosition(srvPinEPosA);
                srvPinEPosi = srvPinEPosA;
            }

        }

        if (MoveR == 1){

            //Condições que verificam a posição que o servo está, alternando sua posição entre a primeira e a segunda posição
            if (srvPinDPosi == srvPinDPosA){ //Se o servo estiver na posição A

                srvPinD.setPosition(srvPinDPosB);
                srvPinDPosi = srvPinDPosB;

            } else if (srvPinDPosi == srvPinDPosB){ //Se o servo estiver na posição B

                srvPinD.setPosition(srvPinDPosA);
                srvPinDPosi = srvPinDPosA;

            }else{ //Se o servo não estiver em nenhuma das duas posições
                srvPinD.setPosition(srvPinDPosA);
                srvPinDPosi = srvPinDPosA;
            }

        }

    }


    public void Telemetry() {
            /*telemetry.addData("MDF Position", MDF.getCurrentPosition());
            telemetry.addData("MDT Position", MDT.getCurrentPosition());
            telemetry.addData("MEF Position", MEF.getCurrentPosition());
            telemetry.addData("MET Position", MET.getCurrentPosition());*/


        telemetry.addData("Yaw", orientation);

        telemetry.addData("MDF Posi (Reto)", MDF.getCurrentPosition());
        telemetry.addData("distancia reto", distanceTR);

        telemetry.addData("MDT Posi (Lado)", MDT.getCurrentPosition());
        telemetry.addData("distancia lado", distanceTL);

        telemetry.addData("Elbow posi", ElbowA.getCurrentPosition());
        telemetry.addData("Elbow pow", ElbowA.getPower());
        telemetry.addData("Runtime Seconds", runtime.seconds());
        telemetry.addData("MDF Pow", MDF.getPower());
        telemetry.addData("MEF Pow", MEF.getPower());
        telemetry.addData("MDT Pow", MDT.getPower());
        telemetry.addData("MET Pow", MET.getPower());

        telemetry.addData("MEF Posi", MEF.getCurrentPosition());
        telemetry.addData("MET Posi", MET.getCurrentPosition());

        telemetry.addData("Josue", josue);
        telemetry.addData("Gabriel", gabriel);
        telemetry.addData("Luan", luan);

        telemetry.update();

    }
}
