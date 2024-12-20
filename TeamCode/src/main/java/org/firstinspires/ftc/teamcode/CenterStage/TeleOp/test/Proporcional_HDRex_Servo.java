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

@TeleOp(name="Proporcional HDRex - Servo", group="OpMode")
//@Disabled
public class Proporcional_HDRex_Servo extends OpMode {

    //Declaração das variavéis públicas do código

    //Tempo que o código está rodando
    private final ElapsedTime runtime = new ElapsedTime();

    //Criando o objeto do motor
    DcMotor Elbow = null;

    //Criando o objeto do servo
    private Servo srvP1;

    //Criando a variavel do valor do Gamepad
    double G1LY;

    //Criando a variavel da posição do HDRex (Elbow)
    double posicaoHDRex;

    //Criando a variavel que receberá a posição do servo (Já convertida)
    double posicaoServo;

    @Override
    public void init() {

        //Atribuindo aos objetos seus respectivos motores na configuração
        Elbow = hardwareMap.get(DcMotor.class, "Elbow");
        srvP1 = hardwareMap.get(Servo.class, "srvP1");

        //Setando a direção do motor
        Elbow.setDirection(DcMotor.Direction.FORWARD);

        //Resetando encoder do motor
        Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Redimensionando a escala do servo para que ele ande até no máximo 180 graus (Sem isso o servo gobilda anda 270 graus)
        srvP1.scaleRange(0, 0.666);

        runtime.reset();
    }

    //Repete até que o código seja desligado
    public void loop() {

        Mov(0.3, 0.4);
        ServoP();
        telemetry();

    }

    //Função de Movimentação do HDRex
    public void Mov(double deadzone, double reduction){ //Deadzone é o tamanho da zona morta, e reduction a redução de potência a ser aplicada no HDRex

        //Atribuindo as variaveis os inputs dos gamepads
        G1LY = -gamepad1.left_stick_y; // Invertendo o eixo Y (cima positivo)

        //Se o comando do piloto for maior que a zona morta o motor pode ser movido
        if (G1LY > deadzone || G1LY < -deadzone) {

            //Se o motor passar do intervalo de movimentação (-2048 até 2048), o motor para e só aceita comandos que voltem pro intervalo
            if (Elbow.getCurrentPosition() > 2048) {

                Elbow.setPower(0);
                Elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                if (G1LY < 0){
                    
                    Elbow.setPower(G1LY*reduction);
                    
                }

            } else if (Elbow.getCurrentPosition() < -2048) { //Se o motor passar do intervalo de movimentação (-2048 até 2048), o motor para e só aceita comandos que voltem pro intervalo

                Elbow.setPower(0);
                Elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                if (G1LY > 0){
                    
                    Elbow.setPower(G1LY*reduction);
                    
                }

            }else{ //Se o motor estiver no intervalo correto (-2048 até 2048)

                Elbow.setPower(G1LY*reduction);

            }

        }else{ //Se os comandos do piloto não forem maiores que a zona morta o motor não deve ser movido

            Elbow.setPower(0);

        }

    }

    //Função de movimentação do servo (Em relação a posição do HDRex)
    public void ServoP() {
        posicaoHDRex = Elbow.getCurrentPosition();

        //Mapeia a posição do HDRex para a posição do servo (Faz uma compensação de ticks para coodenadas do servo para que ambos andem o mesmo tanto em ângulos)
        posicaoServo = map(posicaoHDRex, -2080, 2080, 0, 1); //Seta os intervalos máximos e mínimos
        srvP1.setPosition(posicaoServo); //Manda o servo para posição já calibrada
    }

    //Função de mapeamento de valores (Transforma os valores do encoder do motor em coordenadas do servo)
    public double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }


    //Função de telemetria
    public void telemetry(){

        //Valores do gamepad
        telemetry.addData("G1LY", G1LY);

        //Posição do Encoder de cada motor
        telemetry.addData("Encoder Elbow", posicaoHDRex);

        //Posição do servo do pulso
        telemetry.addData("srvP1", posicaoServo);

        //Tempo decorrido
        telemetry.addData("Runtime (Seconds)", runtime.seconds());

        //Atualiza a telemetria
        telemetry.update();

    }

}



