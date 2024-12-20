package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutonomousTestv0_3", group="Autonomos")
public class AutonomousTestv0_3 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDF, MDT, MEF, MET, ElbowA, ElbowB;

    private Servo srvPinD, srvPinE;


    @Override
    public void runOpMode() {

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MET.setDirection(DcMotor.Direction.REVERSE);
        MDF.setDirection(DcMotor.Direction.FORWARD);


        telemetry.addData("Status", "Aguardando Início");
        telemetry.update();

        waitForStart();


        while (opModeIsActive() ) {
            if (runtime.seconds() > 1) {

                MDT.setPower(0);
                MET.setPower(0);
                MDF.setPower(0);
                MEF.setPower(0);

                MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            }
            else{

                Straight(0.3, 50);
                telemetry.update();
            }
        }
    }


    public void Straight(double Power, double distanceCM) {

        double currentPosition = MDT.getCurrentPosition();
        int distanceT = (int) ((distanceCM * 17.7387267905) + currentPosition);

        MDT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MDT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MET.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (Power <= 0) {
            while (MDT.getCurrentPosition() > distanceT) {
                MDT.setPower(-Power);
                MET.setPower(-Power);
                MEF.setPower(-Power);
                MDF.setPower(-Power);

                MDT.setTargetPosition(distanceT);
                MET.setTargetPosition(distanceT);
                MDF.setTargetPosition(distanceT);
                MEF.setTargetPosition(distanceT);

                MDT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                MET.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                MDF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                MEF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Telemetry();
            }
        } else {
            while (MDT.getCurrentPosition() <= distanceT) {
                MDT.setPower(Power);
                MET.setPower(Power);
                MDF.setPower(Power);
                MEF.setPower(Power);

                MDT.setTargetPosition(distanceT);
                MET.setTargetPosition(distanceT);
                MDF.setTargetPosition(distanceT);
                MEF.setTargetPosition(distanceT);

                MDT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                MET.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                MDF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                MEF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Telemetry();
            }
        }

        // Parar os motores após alcançar a distância desejada
        MDT.setPower(0);
        MET.setPower(0);
        MDF.setPower(0);
        MEF.setPower(0);

        // Configurar o comportamento de parada
        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void Telemetry() {
        /*telemetry.addData("MDF Position", MDF.getCurrentPosition());
        telemetry.addData("MDT Position", MDT.getCurrentPosition());
        telemetry.addData("MEF Position", MEF.getCurrentPosition());
        telemetry.addData("MET Position", MET.getCurrentPosition());*/

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

//Deus seja louvado