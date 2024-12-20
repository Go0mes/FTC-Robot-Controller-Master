package org.firstinspires.ftc.teamcode.CenterStage.TeleOp;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.Telemetry;



public class Movimentacao {
    
    private HardwareMap hardwareMap;
    private Gamepad gamepad1;
    private Telemetry telemetry;
    private DcMotor MDF, MEF, MDT, MET;
    private IMU imu;

    double G1LY, G1LX, G1RX;
    double orientation;
    double PMEF, PMET, PMDF, PMDT;
    double X, Y;

    public Movimentacao(HardwareMap hardwareMap, Gamepad gamepad1, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;
        this.telemetry = telemetry;
        initHardware();
    }

    private void initHardware() {
        // Inicialização dos motores
        MEF = hardwareMap.get(DcMotor.class, "LeftDriveUp");
        MET = hardwareMap.get(DcMotor.class, "LeftDriveDown");
        MDF = hardwareMap.get(DcMotor.class, "RightDriveUp");
        MDT = hardwareMap.get(DcMotor.class, "RightDriveDown");

        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);

        // Inicialização do IMU
        imu = hardwareMap.get(IMU.class, "imu");
        imu.resetYaw();
    }

    public void Mov(double deadzone, double bindmin, double bindmax){  //Toda a lógica desse código de movimentação foi retirada do gm0 e pode ser encontrada no link: https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html

        orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        //Atribuindo as variaveis os inputs dos gamepads
        G1LY = -gamepad1.left_stick_y; // Invertendo o eixo Y (cima positivo)
        G1LX = gamepad1.left_stick_x;
        G1RX = gamepad1.right_stick_x;

        //Reseta o Yaw
        if (gamepad1.dpad_left){
            imu.resetYaw();
        }

        //Rotacionando vetores
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        X = G1LX * Math.cos(-orientation.getYaw(AngleUnit.RADIANS)) - G1LY * Math.sin(-orientation.getYaw(AngleUnit.RADIANS));
        Y = G1LX * Math.sin(-orientation.getYaw(AngleUnit.RADIANS)) + G1LY * Math.cos(-orientation.getYaw(AngleUnit.RADIANS));

        //Serve para garantir que a potência dada aos motores não passe do intervalo de -1, 1
        //O comando math.max garante que o denominador seja pelomenos 1, para não ocorrer divisão por 0
        double denominador = Math.max(Math.abs(Y) + Math.abs(X) + Math.abs(G1RX), 1); // da uma noção da potência aplicada no robô, serve para

        //Calcula as potências que devem ser adicionadas nos motores
        PMEF = (Y + X + G1RX) / denominador;
        PMET = (Y - X + G1RX) / denominador;
        PMDF = (Y - X - G1RX) / denominador;
        PMDT = (Y + X - G1RX) / denominador;

        //Adiciona as potências aos motores
        //A bind é divida em 2 niveis, (bindmin: até 0.8 de pressão no trigger) (bindmax: mais que 0.8 de pressão no trigger)
        //deadzone é uma zona morta nos joysticks para evitar acionamento involuntario por drift
        if ((Math.abs(G1LY) > deadzone) || ( Math.abs(G1LX) > deadzone) || (Math.abs(G1RX) > deadzone)) {
            if (gamepad1.right_trigger > 0 && gamepad1.right_trigger < 0.8){ //Primeiro nível da bind

                MEF.setPower(PMEF*bindmin); //Redução na frente por causa do peso extra atrás do robô
                MET.setPower(PMET*bindmin);
                MDF.setPower(PMDF*bindmin); //Redução na frente por causa do peso extra atrás do robô
                MDT.setPower(PMDT*bindmin);

            } else if (gamepad1.right_trigger > 0.8) { //Segundo nível da bind
                MEF.setPower(PMEF*bindmax); //Redução na frente por causa do peso extra atrás do robô
                MET.setPower(PMET*bindmax);
                MDF.setPower(PMDF*bindmax); //Redução na frente por causa do peso extra atrás do robô
                MDT.setPower(PMDT*bindmax);

            } else { //Sem bind
                MEF.setPower(PMEF); //Redução na frente por causa do peso extra atrás do robô
                MET.setPower(PMET);
                MDF.setPower(PMDF); //Redução na frente por causa do peso extra atrás do robô
                MDT.setPower(PMDT);
            }
        }else{ //Sem comandos

            MEF.setPower(0);
            MET.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);
            MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
    }

    public void movTelemetry (){

        telemetry.addData("PMEF", String.format("%.2f", PMEF));
        telemetry.addData("PMET", String.format("%.2f", PMET));
        telemetry.addData("PMDF", String.format("%.2f", PMDF));
        telemetry.addData("PMDT", String.format("%.2f", PMDT));

        telemetry.addData("rotY", String.format("%.2f", Y));
        telemetry.addData("rotX", String.format("%.2f", X));

        telemetry.addData("G1LY", String.format("%.2f", G1LY));
        telemetry.addData("G1LX", String.format("%.2f", G1LX));

        telemetry.addData("RTG1", String.format("%.2f", gamepad1.right_trigger));

        telemetry.addData("orientation (yaw)", String.format("%.2f", orientation));

        telemetry.update();
    }

}
