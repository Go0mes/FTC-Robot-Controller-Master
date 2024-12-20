package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@Autonomous(name="Autonomous Testv0_2", group="LinearOpmode")
//@Disabled


public class AutonomousTestv0_2 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor MDF;
    private DcMotor MDT;
    private DcMotor MEF;
    private DcMotor MET;

    private DcMotor ElbowA, ElbowB, Slider = null;

    private Servo srvPulD, srvPulE, srvPinD, srvPinE, srvPlane;

    IMU imu;



    double totaltime;

    double distanceT;

    @Override
    public void runOpMode() {

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);

        srvPinD = hardwareMap.get(Servo.class, "srvPinD");
        srvPinE = hardwareMap.get(Servo.class, "srvPinE");



        //Encoder de referência
        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");


        waitForStart();
        runtime.reset();

        //sleep(15000);

        //Aqui ficarão os comandos (chamando os voids)
        //ladodireito(0.7, 1, 1); //Segundos, Potência, Reduction, BoostBack
        //Straight(0.2, -170); //Potência, Centímetros

while (opModeIsActive()) {
    Telemetry();
    Straight(0.1, -50, 0);
}


        /*srvPinD.setPosition(1);
        srvPinE.setPosition(0);

        ElbowA.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ElbowB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ElbowA.setTargetPosition(800);
        ElbowB.setTargetPosition(800);

        ElbowA.setPower(0.4);
        ElbowB.setPower(0.4);

        ElbowA.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ElbowB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ElbowA.setPower(0);
        ElbowA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ElbowB.setPower(0);
        ElbowB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/



       // Girar(0.3, -90);

       // Straight(0.3, 30, 0);

       // sleep(15000);

    }

    public void Girar(double Power, int Angulo) {

        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
        double targetYaw = Angulo;
        double erro_ang = targetYaw - Yaw_atual;
        Power = Power/100;

        imu.resetYaw();
        MDT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MDT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //CALIBRAÇÃO GIRO ANTIHORARIO
        double K1_esquerda = 1.5;
        double K1_direita = 1;

        //CALIBRAÇÃO GIRO HORARIO
        double K2_esquerda = 1.5;
        double K2_direita = 1;

        while (Math.abs(erro_ang) > 3) {   //HORARIO
            Telemetry();
            orientation = imu.getRobotYawPitchRollAngles();
            Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
            erro_ang = targetYaw - Yaw_atual;
            telemetry.addData("Buscando Heading .... erro_ang", "%.2f Deg.", erro_ang);
            telemetry.update();


            //se erro_angulo positivo, logo giro antihorario
            //CURVA ANTI HORARIO
            while (erro_ang>2) {
                MDT.setPower(Power*K1_direita);
                MET.setPower(-Power*K1_esquerda);
                MDT.setPower(-Power*K1_direita);
                MET.setPower(Power*K1_esquerda);

                //*1.5 para compensar motor fraco esquerda ajustar depois
                orientation = imu.getRobotYawPitchRollAngles();
                Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
                erro_ang = targetYaw - Yaw_atual;
                telemetry.addData("GIRANDO ANTIHORARIO > erro_ang", "%.2f Deg.", erro_ang);
                Telemetry();
            }

            while (erro_ang<2) {

                MDT.setPower(-Power*K2_direita);
                MET.setPower(Power*K2_esquerda);
                MDF.setPower(Power*K2_direita);
                MEF.setPower(-Power*K2_esquerda);

                orientation = imu.getRobotYawPitchRollAngles();
                Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
                erro_ang = targetYaw - Yaw_atual;
                telemetry.addData("GIRANDO HORARIO > erro_ang", "%.2f Deg.", erro_ang);
                Telemetry();
            }
            break;
        }

        Telemetry();
        MDT.setPower(0);
        MET.setPower(0);

        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void Straight(double Power, double distanceCM, float Head) {

        double currentPosition = MDT.getCurrentPosition();
        double distanceT = (distanceCM * 17.7387267905) + currentPosition;

        double gain = 0.04;

        MDT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MDT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MET.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (Power <= 0) {
            while (MDT.getCurrentPosition() > distanceT) {
               MDT.setPower(-Power);
               MET.setPower(-Power);
                Telemetry();
            }
        } else {
            while (MDT.getCurrentPosition() <= distanceT) {
                MDT.setPower(Power);
                MET.setPower(Power);
                Telemetry();
            }
        }

        // Parar os motores após alcançar a distância desejada
        MDT.setPower(0);
        MET.setPower(0);

        // Configurar o comportamento de parada
        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void ladodireito (double executime, double reductfront, double boostback){

        totaltime = runtime.seconds() + executime;

        Telemetry();

        while (runtime.seconds() < totaltime){

            MDF.setPower(0.05 * reductfront);
            MDT.setPower(-0.6 * boostback);
            MEF.setPower(-0.05 * reductfront);
            MET.setPower(0.6 * boostback);

            Telemetry();

        }

        MDF.setPower(0);
        MDF.setPower(0);
        MDF.setPower(0);
        MDF.setPower(0);

        MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Telemetry();

    }

    public void giro(double AngAlvo){
        while(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) < AngAlvo){

            MDF.setPower(0);
            MDT.setPower(0.3);
            MEF.setPower(-0.3);
            MET.setPower(0);

        }

    }

    public void Telemetry(){

        telemetry.addData("Distancia", distanceT)   ;

        /*telemetry.addData("MDF Position", MDF.getCurrentPosition());
        telemetry.addData("MDT Position", MDT.getCurrentPosition());
        telemetry.addData("MEF Position", MEF.getCurrentPosition());
        telemetry.addData("MET Position", MET.getCurrentPosition());*/

        telemetry.addData("MDF Pow", MDF.getPower());
        telemetry.addData("MEF Pow", MEF.getPower());
        telemetry.addData("MDT Pow", MDT.getPower());
        telemetry.addData("MET Pow", MET.getPower());


        telemetry.addData("Totaltime", totaltime);
        telemetry.addData("Runtime", runtime.seconds());

        telemetry.update();

    }

}

//Deus seja louvado
