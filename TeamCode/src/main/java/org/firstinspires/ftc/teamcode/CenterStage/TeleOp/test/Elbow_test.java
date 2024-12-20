//Este script serve para o teste do elbow com PID e controle nos dpads
package org.firstinspires.ftc.teamcode.CenterStage.TeleOp.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Elbow_test", group="OpMode")
//@Disabled
public class Elbow_test extends OpMode {



    // Tempo que o código está rodando
    private ElapsedTime runtime = new ElapsedTime();

    // Criando os objetos dos motores
    DcMotor Elbow = null;

    // Declaração das variáveis públicas do código
    // Valores do Gamepad
    boolean Dpad1Up;
    boolean Dpad1Down;
    double ElbowPow;

    // Parâmetros do PID
    double kp = 0.1;   //Ganho proporcional
    double ki = 0.01;  //Ganho integral
    double kd = 0.01;  //Ganho derivativo

    // Variáveis do PID
    double error = 0;
    double integral = 0;
    double derivative = 0;
    double lastError = 0;

    // Setpoint desejado
    int setpoint = 0;

    @Override
    public void init() {

        // Atribuindo ao objeto seu respectivo motor na configuração
        Elbow = hardwareMap.get(DcMotor.class, "Slider");

        // Setando a direção do motor
        Elbow.setDirection(DcMotor.Direction.FORWARD);

        // Resetando o encoder do motor
        Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        runtime.reset();
    }


    // Repete até que o código seja desligado
    public void loop() {

        Elbow();
        telemetry();

    }

    public void Elbow(){

        // Atribuindo as variaveis os valores do gamepad
        Dpad1Up = gamepad1.dpad_up;
        Dpad1Down = gamepad1.dpad_down;

        // Se o dpad esquerdo for pressionado, o encoder reseta
        if (gamepad1.dpad_left) {
            Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // Se dpad para cima ou para baixo for pressionado, ele realiza o movimento
        if (Dpad1Up || Dpad1Down) {

            if (Dpad1Up) {
                ElbowPow = 0.1;  // Se o movimento for para baixo
            } else if (Dpad1Down) {
                ElbowPow = -0.6;  // Se o movimento for para cima
            }

            if (Elbow.getCurrentPosition() > 2000){ //Se a posição atual for maior do que o intervalo

                Elbow.setPower(0);
                Elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                if (Dpad1Down){

                    Elbow.setPower(ElbowPow);

                }

            }else if(Elbow.getCurrentPosition() < -2000){ //Se a posição atual for menor do que o intervalo

                Elbow.setPower(0);
                Elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                if (Dpad1Up){

                    Elbow.setPower(ElbowPow);

                }

            }else{ //Se a posição atual estiver dentro do intervalo

                Elbow.setPower(ElbowPow);

            }

            setpoint = Elbow.getCurrentPosition(); //Setpoint recebe a última posição enviada pelo piloto

        } else { //PID que mantém na posição correta

            // Cálculo do erro
            error = setpoint - Elbow.getCurrentPosition();

            // Cálculo da parte proporcional
            double proportional = kp * error;

            // Cálculo da parte integral
            integral += ki * error;

            // Cálculo da parte derivativa
            derivative = kd * (error - lastError);
            lastError = error;

            // Cálculo do valor de controle
            double controlOutput = proportional + integral + derivative;

            // Aplica o valor de controle ao slider
            Elbow.setPower(controlOutput*0.45);
        }

    }

    // Função de telemetria
    public void telemetry() {

        telemetry.addData("Encoder Elbow", Elbow.getCurrentPosition());
        telemetry.addData("DPadUp", gamepad1.dpad_up);
        telemetry.addData("DPadDown", gamepad1.dpad_down);
        telemetry.addData("ElbowCurrentPower", Elbow.getPower());
        telemetry.addData("setpoint", setpoint);

        telemetry.addData("Runtime (Seconds)", runtime.seconds());



        telemetry.update();
    }
}
