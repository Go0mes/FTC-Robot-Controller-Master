//giro
package org.firstinspires.ftc.teamcode.CenterStage.Autonomous;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name="AutonomousTestv1_0", group="Autonomous")
public class AutonomousTestv1_0 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor MDF, MDT, MEF, MET;
    public int josue = 0;

    IMU imu;


    double orientation = 0;
    // em caso de angulo alvo aer 180, coloque um valor proximo.

    @Override
    public void runOpMode() {

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);


        imu.initialize(new IMU.Parameters(orientationOnRobot));

        MDF = hardwareMap.dcMotor.get("RightDriveUp");
        MDT = hardwareMap.dcMotor.get("RightDriveDown");
        MEF = hardwareMap.dcMotor.get("LeftDriveUp");
        MET = hardwareMap.dcMotor.get("LeftDriveDown");

        MET.setDirection(DcMotor.Direction.REVERSE);
        MEF.setDirection(DcMotor.Direction.REVERSE);

        MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MET.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MEF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MDF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu.resetYaw();

        telemetry.addData("Status", "Aguardando Início");
        telemetry.update();



        waitForStart();

        while (opModeIsActive() ) {

            Straight(0.09, 0.2, 0.2, 0.2, 0.2, 100 );

            Telemetry();
        }
    }



    public void WalkTimer(double powMDF, double powMDT, double powMEF, double powMET, float duration) { //

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

    public void Giro(double targetdegrees, double constpower){

        imu.resetYaw();

        while (orientation != targetdegrees){

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            //orientation = angulo atual do robo

            double error = orientation - targetdegrees;
            //error é a diferença entre o' angulo atual e o angulo alvo

            double power = constpower * error;
            // power recebe a multiplicaçao do constpower (0.01) com o error

            /* exemplo:
                    constpower = 0.01 e error = -90 (curva do 0 até o -90)
                    power= -0.9
            */


            if (orientation > targetdegrees ){

                MDF.setPower(-power);
                MDT.setPower(-power);
                MEF.setPower(power);
                MET.setPower(power);



            }else if(orientation < targetdegrees){

                MDF.setPower(power);
                MDT.setPower(power);
                MEF.setPower(-power);
                MET.setPower(-power);

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

            imu.resetYaw();

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

    public void Straight(double ganho, double powMDF, double powMDT, double powMEF, double powMET, double distancecm){

        int distancet = (int) ((72000/100)*distancecm);

        while (MDF.getCurrentPosition() < distancet + MDF.getCurrentPosition()){

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

        MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Telemetry();

    }

    public void SideWalkL(double ganho, double powMDF, double powMDT, double powMEF, double powMET, double distancecm){

        int distancet = (int) ((71500/100)*distancecm + MDT.getCurrentPosition());

        while (MDT.getCurrentPosition() < distancet){

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            if(orientation > 0){

                MDF.setPower(-powMDF);
                MDT.setPower(powMDT-ganho);
                MET.setPower(-powMET);
                MEF.setPower(powMEF+ganho);

            } else if (orientation < 0) {

                MDF.setPower(-powMDF+ganho);
                MDT.setPower(powMDT);
                MET.setPower(-powMET-ganho);
                MEF.setPower(powMEF);

            }else{

                MDF.setPower(-powMDF);
                MDT.setPower(powMDT);
                MET.setPower(-powMET);
                MEF.setPower(powMEF);

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

    public void SideWalkR(double ganho, double powMDF, double powMDT, double powMEF, double powMET, double distancecm){

        int distancet = (int) ((71500/100)*distancecm + MDT.getCurrentPosition());

        while (MDT.getCurrentPosition() < distancet){

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            if(orientation > 0){

                MDF.setPower(powMDF);
                MDT.setPower(-powMDT-ganho);
                MET.setPower(powMET);
                MEF.setPower(-powMEF+ganho);

            } else if (orientation < 0) {

                MDF.setPower(powMDF+ganho);
                MDT.setPower(-powMDT);
                MET.setPower(powMET-ganho);
                MEF.setPower(-powMEF);

            }else{

                MDF.setPower(powMDF);
                MDT.setPower(-powMDT);
                MET.setPower(powMET);
                MEF.setPower(-powMEF);

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

    public void Telemetry() {
        /*telemetry.addData("MDF Position", MDF.getCurrentPosition());
        telemetry.addData("MDT Position", MDT.getCurrentPosition());
        telemetry.addData("MEF Position", MEF.getCurrentPosition());
        telemetry.addData("MET Position", MET.getCurrentPosition());*/


        telemetry.addData("Yaw", orientation);
        telemetry.addData("Runtime Seconds", runtime.seconds());
        telemetry.addData("", "");
        telemetry.addData("angulo atual", orientation);
        telemetry.addData("josueeeeeeeeeeeeee", josue);



        telemetry.addData("Pow MDF", MDF.getPower());
        telemetry.addData("Pow MEF", MEF.getPower());
        telemetry.addData("Pow MDT", MDT.getPower());
        telemetry.addData("Pow MET", MET.getPower());
        telemetry.addData("", "");

        telemetry.addData("MDF Posi", MDF.getCurrentPosition());
        telemetry.addData("MEF Posi", MEF.getCurrentPosition());
        telemetry.addData("MDT Posi", MDT.getCurrentPosition());
        telemetry.addData("MET Posi", MET.getCurrentPosition());


        telemetry.update();

    }
}