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

@TeleOp(name="Wheel Test", group="OpMode")
//@Disabled
public class Motors_test extends OpMode {

    // Declaração das variavéis públicas do código

    //Tempo que o código está rodando
    private ElapsedTime runtime = new ElapsedTime();

    //Criando os objetos dos motores
    DcMotor MDF,MEF,MDT,MET, Elbow = null; //Elbow é o motor de movimento 180 graus da garra


    //Criando as variaveis dos valores do Gamepad
    double G1LY;
    double Trigger;

    //Criando a variavel de controle (Diz qual dos motores do robô está ativo)
    double MotorAtivo = 1;

    @Override
    public void init() {

        //Atribuindo aos objetos seus respectivos motores na configuração
        MEF = hardwareMap.get(DcMotor.class, "LeftDriveUp");
        MET = hardwareMap.get(DcMotor.class, "LeftDriveDown");
        MDF = hardwareMap.get(DcMotor.class, "RightDriveUp");
        MDT = hardwareMap.get(DcMotor.class, "RightDriveDown");
        Elbow = hardwareMap.get(DcMotor.class, "Elbow");

        //Setando a direção dos motores (Os da esquerda de movimento devem ser invertidos para girarem para frente)
        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);

        Elbow.setDirection(DcMotor.Direction.FORWARD);

        runtime.reset();
    }

    //Repete até que o código seja desligado
    public void loop() {

        Mov(0.3, 0.65, 0.3, 0.6);
        telemetry();

    }

    //Função de Movimentação dos motores
    public void Mov(double deadzone, double bindmin, double bindmax, double reduction){

        //Atribuindo as variaveis os inputs dos gamepads
        G1LY = -gamepad1.left_stick_y; // Invertendo o eixo Y (cima positivo)
        Trigger = gamepad1.right_trigger;

        //O botão esquerdo do dpad reseta todos os encoders
        if (gamepad1.dpad_left){

            MDF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            MDT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            MEF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            MET.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            MDF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MDT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MEF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MET.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }

        //Se o comando do piloto for maior que a zona morta
        if (G1LY > deadzone || G1LY < -deadzone) {
            //Checa qual motor está ativo
            //Para o caso do motor ativo ser o 1
            if (MotorAtivo == 1) {

                //Primeiro nivel da bind entre as seguintes posições do trigger (Maior que 0 e menor que 0.8)
                if(Trigger > 0 && Trigger < 0.8){

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmin
                    MEF.setPower(G1LY*bindmin);

                }else if(Trigger > 0.8) { //Segundo nivel da bind, basta que a posição do trigger seja maior que 0.8

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmax
                    MEF.setPower(G1LY*bindmax);

                }else{

                    //Seta potência sem bind (Não reduz a potência)
                    MEF.setPower(G1LY);

                }

            } else if (MotorAtivo == 2) { //Para o caso do motor ativo ser o 2

                //Primeiro nivel da bind entre as seguintes posições do trigger (Maior que 0 e menor que 0.8)
                if(Trigger > 0 && Trigger < 0.8){

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmin
                    MDF.setPower(G1LY*bindmin);

                }else if(Trigger > 0.8) { //Segundo nivel da bind, basta que a posição do trigger seja maior que 0.8

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmax
                    MDF.setPower(G1LY*bindmax);

                }else{

                    //Seta potência sem bind (Não reduz a potência)
                    MDF.setPower(G1LY);

                }

            } else if (MotorAtivo == 3) { //Para o caso do motor ativo ser o 3

                //Primeiro nivel da bind entre as seguintes posições do trigger (Maior que 0 e menor que 0.8)
                if(Trigger > 0 && Trigger < 0.8){

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmin
                    MET.setPower(G1LY*bindmin);

                }else if(Trigger > 0.8) { //Segundo nivel da bind, basta que a posição do trigger seja maior que 0.8

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmax
                    MET.setPower(G1LY*bindmax);

                }else{

                    //Seta potência sem bind (Não reduz a potência)
                    MET.setPower(G1LY);

                }

            } else if (MotorAtivo == 4) { //Para o caso do motor ativo ser o 4

                //Primeiro nivel da bind entre as seguintes posições do trigger (Maior que 0 e menor que 0.8)
                if(Trigger > 0 && Trigger < 0.8){

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmin
                    MDT.setPower(G1LY*bindmin);

                }else if(Trigger > 0.8) { //Segundo nivel da bind, basta que a posição do trigger seja maior que 0.8

                    //Seta a potência reduzida do motor de acordo com o parâmetro do void bindmax
                    MDT.setPower(G1LY*bindmax);

                }else {

                    //Seta potência sem bind (Não reduz a potência)
                    MDT.setPower(G1LY);

                }

            } else if (MotorAtivo == 5) {

                //Este motor não é afetado pela bind
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

        }else{

            //Seta a potência dos motores para 0 enquanto nenhum comando está sendo enviado pelo piloto
            MEF.setPower(0);
            MET.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);
            Elbow.setPower(0);

        }

        //Botão X muda o motor ativo para o motor da esquerda frente
        if (gamepad1.x){
            //MEF
            MotorAtivo = 1;

            //Zera a potência dos demais motores durante a transição para evitar bugs
            MET.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);
            Elbow.setPower(0);


        }else if(gamepad1.y){ //Botão Y muda o motor ativo para o motor da direita frente
            //MDF
            MotorAtivo = 2;

            //Zera a potência dos demais motores durante a transição para evitar bugs
            MEF.setPower(0);
            MET.setPower(0);
            MDT.setPower(0);
            Elbow.setPower(0);

        }else if(gamepad1.a){ //Botão A muda o motor ativo para o motor da esquerda trás
            //MET
            MotorAtivo = 3;

            //Zera a potência dos demais motores durante a transição para evitar bugs
            MEF.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);
            Elbow.setPower(0);

        }else if(gamepad1.b){ //Botão B muda o motor ativo para o motor da direita trás
            //MDT
            MotorAtivo = 4;

            //Zera a potência dos demais motores durante a transição para evitar bugs
            MEF.setPower(0);
            MET.setPower(0);
            MDF.setPower(0);
            Elbow.setPower(0);

        }else if(gamepad1.right_bumper){ //Botão Right Bumper muda o motor ativo para o motor de movimento 180 graus da garra
            //Elbow
            MotorAtivo = 5;

            MEF.setPower(0);
            MET.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);

        }

    }

    //Função de telemetria
    public void telemetry(){

        //Valores do gamepad
        telemetry.addData("G1LY", G1LY);

        //Motor ativo no momento
        telemetry.addData("Motor Ativo", MotorAtivo);

        //Posição do Encoder de cada motor
        telemetry.addData("Encoder MEF", MEF.getCurrentPosition());
        telemetry.addData("Encoder MET", MET.getCurrentPosition());
        telemetry.addData("Encoder MDF", MDF.getCurrentPosition());
        telemetry.addData("Encoder MDT", MDT.getCurrentPosition());

        telemetry.addData("Encoder Elbow", Elbow.getCurrentPosition());

        //Posição do Trigger
        telemetry.addData("Trigger", Trigger);

        //Tempo decorrido
        telemetry.addData("Runtime (Seconds)", runtime.seconds());

        //Atualiza a telemetria
        telemetry.update();

    }

}



