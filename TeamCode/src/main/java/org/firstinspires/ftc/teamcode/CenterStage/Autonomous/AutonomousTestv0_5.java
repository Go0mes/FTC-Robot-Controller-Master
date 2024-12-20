package org.firstinspires.ftc.teamcode.CenterStage.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutonomousTestv0_5", group="Autonomos")
public class AutonomousTestv0_5 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDF, MDT, MEF, MET;

    @Override
    public void runOpMode() {

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MET.setDirection(DcMotor.Direction.REVERSE);
        MEF.setDirection(DcMotor.Direction.REVERSE);

        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status", "Aguardando Início");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() ) {

            MDF.setPower(0.2);
            MDT.setPower(0.2);
            MET.setPower(0.2);
            MEF.setPower(0.2);

            Telemetry();

        }
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