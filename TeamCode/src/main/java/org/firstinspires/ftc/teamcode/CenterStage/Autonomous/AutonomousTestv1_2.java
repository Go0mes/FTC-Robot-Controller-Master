
        package org.firstinspires.ftc.teamcode;

        import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.IMU;
        import com.qualcomm.robotcore.util.ElapsedTime;

        import org.firstinspires.ftc.robotcore.external.Telemetry;
        import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

        @Autonomous(name="AutonomousTestv1_2", group="Autonomos")
        public class AutonomousTestv1_2 extends LinearOpMode {

            private ElapsedTime runtime = new ElapsedTime();
            private DcMotor MDF, MDT, MEF, MET, ElbowA, ElbowB, Slider, Wrist;

            int WristPos = 1;
            double Porta = 1, TempoAlvoPulso, TempoMovPul;

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
                ElbowA = hardwareMap.get(DcMotor.class, "ElbowA");
                ElbowB = hardwareMap.get(DcMotor.class, "ElbowB");
                Slider = hardwareMap.get(DcMotor.class, "Slider");
                Wrist = hardwareMap.get(DcMotor.class, "Wrist");

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

                telemetry.update();

                waitForStart();

                while (opModeIsActive()) {
                    //Giro(90, 2);

                    //Reto(0.025, 50);
                    // direita   SideWalk(0.0237, 100, 1);
                    //  SideWalk(0.0237, 100, 1);
                    Telemetry();
                    Pulso(1);

                    //ElbowUp(-0.5, 2700);
                }
            }

            public void Reto(double ganho, double distanceCM) {

                double distanceT = (72000 / 100) * distanceCM;

                while (MDF.getCurrentPosition() < distanceT) {

                    orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                    if (orientation > 0) {

                        MDF.setPower(0.2);
                        MDT.setPower(0.2);
                        MEF.setPower(0.2 + ganho);
                        MET.setPower(0.2 + ganho);

                    } else if (orientation < 0) {

                        MDF.setPower(0.2 + ganho);
                        MDT.setPower(0.2 + ganho);
                        MEF.setPower(0.2);
                        MET.setPower(0.2);

                    } else {

                        MDF.setPower(0.2);
                        MDT.setPower(0.2);
                        MEF.setPower(0.2);
                        MET.setPower(0.2);

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

            public void Giro(double degrees, double tolerance) {
                while (orientation != degrees) {

                    orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                    if (orientation > degrees + tolerance) {

                        MDF.setPower(-0.2);
                        MDT.setPower(-0.2);
                        MEF.setPower(0.2);
                        MET.setPower(0.2);

                    } else if (orientation < degrees - tolerance) {

                        MDF.setPower(0.2);
                        MDT.setPower(0.2);
                        MEF.setPower(-0.2);
                        MET.setPower(-0.2);

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

            public void SideWalk(double ganho, double distanceCM, double direction) {

                if (direction == 1) {

                    double distanceT = (74000 / 100) * distanceCM;

                    while (MDT.getCurrentPosition() < distanceT) {

                        orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                        if (orientation > 0) {

                            MDF.setPower(-0.2);
                            MDT.setPower(0.2 - ganho);
                            MET.setPower(-0.2);
                            MEF.setPower(0.2 + ganho);

                        } else if (orientation < 0) {

                            MDF.setPower(-0.2 + ganho);
                            MDT.setPower(0.2);
                            MET.setPower(-0.2 - ganho);
                            MEF.setPower(0.2);

                        } else {

                            MDF.setPower(-0.2);
                            MDT.setPower(0.2);
                            MET.setPower(-0.2);
                            MEF.setPower(0.2);

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

                    double distanceT = (74000 / 100) * distanceCM;

                    while (MDT.getCurrentPosition() < distanceT) {

                        orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                        if (orientation > 0) {

                            MDF.setPower(0.2);
                            MDT.setPower(-0.2 - ganho);
                            MET.setPower(0.2);
                            MEF.setPower(-0.2 + ganho);

                        } else if (orientation < 0) {

                            MDF.setPower(0.2 + ganho);
                            MDT.setPower(-0.2);
                            MET.setPower(0.2 - ganho);
                            MEF.setPower(-0.2);

                        } else {

                            MDF.setPower(0.2);
                            MDT.setPower(-0.2);
                            MET.setPower(0.2);
                            MEF.setPower(-0.2);

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
            }

            public void ElbowUp(double ElbowPow, double target) {
                Telemetry();

                while (-ElbowA.getCurrentPosition() < target) {

                    ElbowA.setPower(ElbowPow);
                    ElbowB.setPower(ElbowPow);
                    telemetry.update();
                    Telemetry();

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


            public void Telemetry() {
            /*telemetry.addData("MDF Position", MDF.getCurrentPosition());
            telemetry.addData("MDT Position", MDT.getCurrentPosition());
            telemetry.addData("MEF Position", MEF.getCurrentPosition());
            telemetry.addData("MET Position", MET.getCurrentPosition());*/


                telemetry.addData("Yaw", orientation);

                telemetry.addData("Porta", Porta);
                telemetry.addData("Elbow posi", ElbowA.getCurrentPosition());
                telemetry.addData("Elbow pow", ElbowA.getPower());
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
