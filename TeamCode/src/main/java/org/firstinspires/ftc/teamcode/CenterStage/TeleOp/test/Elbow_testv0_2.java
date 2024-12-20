//Esse script serve para o teste do elbow sem quaisquer limitações de coordenada, a movimentação é feita simplesmente a partir do joystick esquerdo do gamepad 1

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

package org.firstinspires.ftc.teamcode.CenterStage.TeleOp.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Elbow_testv0_2", group="OpMode")
//@Disabled
public class Elbow_testv0_2 extends OpMode {

    //Tempo que o código está rodando
    private ElapsedTime runtime = new ElapsedTime();

    //Criando os objetos dos motores
    DcMotor Elbow = null;

    //Criando a variavel do valor do Gamepad
    double G1LY;

    @Override
    public void init() {

        //Atribuindo ao objeto seu respectivo motor na configuração
        Elbow = hardwareMap.get(DcMotor.class, "Slider");

        //Setando a direção do motor

        Elbow.setDirection(DcMotor.Direction.FORWARD);

        //Resetando o encoder do motor
        Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        runtime.reset();
    }

    //Repete até que o código seja desligado
    public void loop() {

        Slider();
        telemetry();

    }

    //Função de movimentação do motor
    public void Slider(){
        G1LY = -gamepad1.left_stick_y;

        Elbow.setPower(G1LY*0.8); //Reduz a potência a 80%

    }




    //Função de telemetria
    public void telemetry(){

        //Posição do Encoder de cada motor
        telemetry.addData("Encoder Elbow", Elbow.getCurrentPosition());
        telemetry.addData("G1LY", G1LY);

        //Tempo decorrido
        telemetry.addData("Runtime (Seconds)", runtime.seconds());

        //Atualiza a telemetria
        telemetry.update();

    }

}



