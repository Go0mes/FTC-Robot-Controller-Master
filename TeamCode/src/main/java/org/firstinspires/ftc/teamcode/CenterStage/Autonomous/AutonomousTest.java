package org.firstinspires.ftc.teamcode.CenterStage.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//Seta o nome do autonomo, e o Opmode pré-selecionado para quando ele acabar: "OmniTestv0_4"
@Autonomous(name="Autonomous Test", group="LinearOpmode", preselectTeleOp = "OmniTestv0_4")
//@Disabled


public class AutonomousTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    //Criando os objetos dos motores
    private DcMotor MDF, MDT, MEF, MET;

    //Criando a variável da distância em ticks a ser percorrida
    double distanceT;

    @Override
    public void runOpMode() {

        //Atribuindo aos objetos dos motores seu respectivo motor na configuração do robô
        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        //Definindo as direções dos motores (Os da esquerda tem de ser invertidos para girar pra frente)
        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);

        //Resetando o motor de referência (Motor na direta e frente)
        //Motor de referência é o que o código utiliza o encoder para contar seus passos
        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        runtime.reset();

        //Aqui ficarão os comandos (chamando os voids)
        Straight(0.3, 30);

    }

    //Função de andar em linha reta (Frente/Trás)
    public void Straight(double power, double distanceCM){

        //Convertendo a distância em cm para ticks do motor
        //A posição atual do motor deve ser somada para que ele adicione mais passos a sua posição atual, não considerando apenas a distância setada, mas sim toda a percorrida
        distanceT = (distanceCM * 17.7387267905) + MDF.getCurrentPosition();

        //Se a movimentação for para frente
        if (distanceCM > 0){

            //Da potência aos motores enquanto a distância setada não for atingida
            while (MDF.getCurrentPosition() < distanceT) {

                MDF.setPower(power);
                MDT.setPower(power);
                MEF.setPower(power);
                MET.setPower(power);

                telemetry();

            }

        }else if (distanceCM < 0){ //Se a movimentação for para trás

            //Da potência aos motores enquanto a distância setada não for atingida
            //Como a distância nesse caso é negativa, a potência deve ser dada enquanto a posição do robô for MAIOR que a distância a ser atingida
            while (MDF.getCurrentPosition() > distanceT) {

                //Potência negativa pois o movimento é para trás
                MDF.setPower(-power);
                MDT.setPower(-power);
                MEF.setPower(-power);
                MET.setPower(-power);

                telemetry();

            }

        }

        //Para os motores quando a posição é atingida
        MDF.setPower(0);
        MDT.setPower(0);
        MEF.setPower(0);
        MET.setPower(0);

        //Freia os motores, isso dificulta a saída dos motores da posição em que se encontram (Reduz pequenos erros devido a inércia)
        MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry();

    }
    public void telemetry(){

        //Posição do Encoder de cada motor
        telemetry.addData("Encoder MEF", MEF.getCurrentPosition());
        telemetry.addData("Encoder MET", MET.getCurrentPosition());
        telemetry.addData("Encoder MDF", MDF.getCurrentPosition());
        telemetry.addData("Encoder MDT", MDT.getCurrentPosition());

        //Distância a ser percorrida em ticks
        telemetry.addData("distanceT", distanceT);

        //Tempo decorrido
        telemetry.addData("Runtime (Seconds)", runtime.seconds());

        //Atualiza a telemetria
        telemetry.update();

    }
}
