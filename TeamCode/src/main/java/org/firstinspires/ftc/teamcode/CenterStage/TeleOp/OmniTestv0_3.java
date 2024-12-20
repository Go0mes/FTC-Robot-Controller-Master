/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.CenterStage.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="OmniTestv0_3", group="OpMode")
//@Disabled
public class OmniTestv0_3 extends OpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor MDF,MEF,MDT,MET = null;
    IMU imu;
    //Valores do Gamepad
    double G1LY;
    double G1LX;
    double G1RX;
    //Yaw da telemetria
    double orientation;
    //Potências dos Motores
    double PMEF;
    double PMET;
    double PMDF;
    double PMDT;
    //Valores alterados dos vetores
    double X;
    double Y;



    @Override
    public void init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        MEF = hardwareMap.get(DcMotor.class, "LeftDriveUp");
        MET = hardwareMap.get(DcMotor.class, "LeftDriveDown");
        MDF = hardwareMap.get(DcMotor.class, "RightDriveUp");
        MDT = hardwareMap.get(DcMotor.class, "RightDriveDown");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot



        imu.resetYaw();
        runtime.reset();
        }

        // run until the end of the match (driver presses STOP)

        public void loop() {

            mov(0.2, 0.65, 0.3);
            telemetry();

        }

        //Função de Movimentação
        public void mov (double deadzone, double bindmin, double bindmax){

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
                if (gamepad1.right_trigger > 0 && gamepad1.right_trigger < 0.8){

                    MEF.setPower(PMEF*bindmin);
                    MET.setPower(PMET*bindmin);
                    MDF.setPower(PMDF*bindmin);
                    MDT.setPower(PMDT*bindmin);

                } else if (gamepad1.right_trigger > 0.8) {
                    MEF.setPower(PMEF*bindmax);
                    MET.setPower(PMET*bindmax);
                    MDF.setPower(PMDF*bindmax);
                    MDT.setPower(PMDT*bindmax);

                } else {
                    MEF.setPower(PMEF);
                    MET.setPower(PMET);
                    MDF.setPower(PMDF);
                    MDT.setPower(PMDT);
                }
            }else{
                
                MEF.setPower(0);
                MET.setPower(0);
                MDF.setPower(0);
                MDT.setPower(0);
                
            }
        }
        public void telemetry(){

            orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            //Yaw
            telemetry.addData("Yaw", orientation);
            //Valores do gamepad
            telemetry.addData("G1LY", G1LY);
            telemetry.addData("G1LX", G1LX);
            telemetry.addData("G1RX", G1RX);
            //Potência dos Motores
            telemetry.addData("PMEF", PMEF);
            telemetry.addData("PMET", PMET);
            telemetry.addData("PMDF", PMDF);
            telemetry.addData("PMDT", PMDT);
            //Vetores Rotacionados
            telemetry.addData("rotY", Y);
            telemetry.addData("rotX", X);
            telemetry.addData("Trigger", gamepad1.right_trigger);
            
            telemetry.update();

        }


}



