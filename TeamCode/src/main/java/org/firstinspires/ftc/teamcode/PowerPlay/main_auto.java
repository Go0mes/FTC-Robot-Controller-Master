package org.firstinspires.ftc.teamcode.PowerPlay;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name="main_auto", group="LinearOpmode")
//@Disabled
public class main_auto extends LinearOpMode {
    IMU imu;
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor Tras_d;
    private DcMotor Tras_e;
    private DcMotor Slider;
    private Servo ServoG;
    private DistanceSensor Sensor;
    private ColorSensor colorSensor;
    private int RED = 0, GREEN = 0, BLUE = 0;

    @Override
    public void runOpMode() {

        Tras_d = hardwareMap.dcMotor.get("Rev Hex Motor 1");
        Tras_e = hardwareMap.dcMotor.get("Rev Hex Motor 0");
        Slider = hardwareMap.dcMotor.get("GoBilda Motor 2");
        ServoG = hardwareMap.get(Servo.class, "Servo 0");

        Sensor = hardwareMap.get(DistanceSensor.class, "Distance Sensor 1");
        colorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");

        Tras_d.setDirection(DcMotor.Direction.FORWARD);
        Tras_e.setDirection(DcMotor.Direction.REVERSE);
        Slider.setDirection(DcMotor.Direction.FORWARD);

        Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();

        imu = hardwareMap.get(IMU.class, "IMU0");

        waitForStart();
        runtime.reset();

        Slider(0.8, 1000);
        Straight(20, 75, 0);


        if((blue > 2200)){

           BLUE = 1;

        }if(green > 3400){

           GREEN = 1;

        }else if(red > 3000){

           RED = 1;

        }

        Straight(20, 75, 0);

        Straight(-20, 30, 0);

        Girar(20,20); //Power de 0 a 100% / Angulo alvo em graus

        Slider(0.8, 3850); //subindo slider

        Straight(20, 27, 0);

        Garra(1); //abrindo a garra (soltando cone)
        sleep(3000);
        Garra(0); //fechando a garra

        Straight(-20, 35, 0);

        Slider(0.8, 0);

        if(RED == 1){ //Localização 2

            Girar(20, -15);
            //Já está na localização

        }else if (GREEN == 1){ //Localização 1

            Straight(20, 15, 0);
            Girar(20,70); //Power de 0 a 100% / Angulo alvo em graus
            Straight(20, 25, 0);

        }else if (BLUE == 1){ //Localização 3

            Andar(-28, -34, 20);
            Girar(20,-98);
            Andar(18,24,78);

        }

    }

    public void Telemetry() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);
        telemetry.addData("Yaw (Z)", "%.2f Deg.", orientation.getYaw(AngleUnit.DEGREES));
        telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
        telemetry.addData("RED", RED);
        telemetry.addData("GREEN", GREEN);
        telemetry.addData("BLUE", BLUE);
        telemetry.update();
    }
    public void Andar(double PowerD,double PowerE, double DISTANCIA) {

        PowerD = PowerD/100; //divide por 100 para potencia ser posta em porcentagem
        PowerE = PowerE/100;

        DISTANCIA = 1779.797213*DISTANCIA/100; // 1m em pulsos * a distancia em m convertida pra centimetros

        Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (PowerD < 0 && PowerE < 0){

            while (Tras_d.getCurrentPosition() * -1 < DISTANCIA) {
                Tras_d.setPower(PowerD);
                Tras_e.setPower(PowerE);
                Telemetry();
            }

        }else{

            while (Tras_d.getCurrentPosition() < DISTANCIA) {

                Tras_d.setPower(PowerD);
                Tras_e.setPower(PowerE);
                Telemetry();
            }
        }

        Telemetry();
        Tras_d.setPower(0);
        Tras_e.setPower(0);
    }
    public void Girar(double Power, int Angulo) {

        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
        double targetYaw = Angulo;
        double erro_ang = targetYaw - Yaw_atual;
        Power = Power/100;

        imu.resetYaw();
        Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
                Tras_d.setPower(Power*K1_direita);
                Tras_e.setPower(-Power*K1_esquerda); //*1.5 para compensar motor fraco esquerda ajustar depois
                orientation = imu.getRobotYawPitchRollAngles();
                Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
                erro_ang = targetYaw - Yaw_atual;
                telemetry.addData("GIRANDO ANTIHORARIO > erro_ang", "%.2f Deg.", erro_ang);
                Telemetry();
            }

            while (erro_ang<-2) {

                Tras_d.setPower(-Power*K2_direita);
                Tras_e.setPower(Power*K2_esquerda);
                orientation = imu.getRobotYawPitchRollAngles();
                Yaw_atual = orientation.getYaw(AngleUnit.DEGREES);
                erro_ang = targetYaw - Yaw_atual;
                telemetry.addData("GIRANDO HORARIO > erro_ang", "%.2f Deg.", erro_ang);
                Telemetry();
            }
            break;
        }
        Telemetry();
        Tras_d.setPower(0);
        Tras_e.setPower(0);
    }
    public void Slider(double Power, int Posi){

        Slider.setPower(Power);
        Slider.setTargetPosition(Posi);
        Slider.getCurrentPosition();

        while (Slider.getCurrentPosition() < (Posi - 10) || Slider.getCurrentPosition() > (Posi + 10)){ //Indo pra posição do slider com margem de erro de 10 voltas do encoder

            Slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Slider.getCurrentPosition();
        }
    }
    public void Garra(int PosiG){

        if (PosiG == 1){

            ServoG.setPosition(0.2); //abrir garra

            sleep(500);

        } else if (PosiG == 0) {

            ServoG.setPosition(-0.1);

        }
    }
    public void Straight(double Power, double Distancia, float Head){

        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();

        imu.resetYaw();

        Power = Power/100;

        Distancia = 1779.797213*Distancia/100; // 1m em pulsos * a distancia em m convertida pra centimetros

        double gain = 0.04;

        Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (Power < 0){

            while (Tras_d.getCurrentPosition() * -1 < Distancia) {

                orientation = imu.getRobotYawPitchRollAngles();

                double Error = orientation.getYaw(AngleUnit.DEGREES) - Head;

                Tras_d.setPower(Power + (Error * gain));
                Tras_e.setPower(Power - (Error * gain));
                Telemetry();
            }

        }else{

            while (Tras_d.getCurrentPosition() < Distancia) {

                orientation = imu.getRobotYawPitchRollAngles();

                double Error = orientation.getYaw(AngleUnit.DEGREES) - Head;

                Tras_d.setPower(Power - (Error * gain));
                Tras_e.setPower(Power + (Error * gain));
                Telemetry();
            }
        }
        Telemetry();
        Tras_d.setPower(0);
        Tras_e.setPower(0);
    }
}