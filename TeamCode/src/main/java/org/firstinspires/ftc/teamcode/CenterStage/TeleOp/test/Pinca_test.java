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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Pinça_test", group="OpMode")
//@Disabled
public class Pinca_test extends OpMode {

    //Tempo que o código está rodando
    private ElapsedTime runtime = new ElapsedTime();

    //Criando os objetos dos motores

    private Servo srvPinD, srvPinE;

    //Declaração das variavéis públicas do código
    //Variaveis de controle de coordenada dos servos
    double cordSrvPinD, cordSrvPinE;

    //Variaveis de controle do cooldown de acionamento
    double timerD, timerE;
    double timerPE = 0;
    double timerPD = 0;

    //Criando as variaveis dos valores do Gamepad

    //Criando a variavel de controle (Diz qual dos motores do robô está ativo);


    @Override
    public void init() {

        //Atribuindo aos objetos seus respectivos servos na configuração
        srvPinD = hardwareMap.get(Servo.class, "srvPinD");
        srvPinE = hardwareMap.get(Servo.class, "srvPinE");

        //Mandando servos para posição inicial
        srvPinD.setPosition(1);
        srvPinE.setPosition(0);

        //Posições iniciais
        cordSrvPinD = 1;
        cordSrvPinE = 0;

        runtime.reset();
    }

    //Repete até que o código seja desligado
    public void loop() {

        Servos();
        telemetry();

    }

    //Função de Movimentação dos motores

    public void Servos(){

        timerE = runtime.seconds();
        timerD = runtime.seconds();

        //Se o tempo atual for maior do que o tempo que o botão foi pressionado pela última vez somado ao cooldown (0.3), execute o movimento
        if (timerD > timerPD + 0.3) {

            if (gamepad1.right_bumper) {

                //compara os lados e envia ao oposto
                if (cordSrvPinD == 0) {

                    srvPinD.setPosition(1);
                    cordSrvPinD = 1;


                } else if (cordSrvPinD == 1) {

                    srvPinD.setPosition(0);
                    cordSrvPinD = 0;


                }
                timerPD = runtime.seconds();
            }
        }

        //Se o tempo atual for maior do que o tempo que o botão foi pressionado pela última vez somado ao cooldown (0.3), execute o movimento
        if (timerE > timerPE + 0.3) {

            if (gamepad1.left_bumper) {

                //compara os lados e envia ao oposto
                if (cordSrvPinE == 1) {

                    srvPinE.setPosition(0);
                    cordSrvPinE = 0;

                } else if (cordSrvPinE == 0) {

                    srvPinE.setPosition(1);
                    cordSrvPinE = 1;

                }

            }
            timerPE = runtime.seconds();
        }
    }
    //Função de telemetria
    public void telemetry(){

        //Posição do Encoder de cada motor
        telemetry.addData("SrvPinD", srvPinD.getPosition());
        telemetry.addData("SrvPinE", srvPinE.getPosition());

        telemetry.addData("cordSrvPinD", cordSrvPinD);
        telemetry.addData("cordSrvPinE", cordSrvPinE);

        telemetry.addData("RB1", gamepad1.right_bumper);
        telemetry.addData("LB1", gamepad1.left_bumper);

        //Tempo decorrido
        telemetry.addData("Runtime (Seconds)", runtime.seconds());

        //Atualiza a telemetria
        telemetry.update();

    }

}



