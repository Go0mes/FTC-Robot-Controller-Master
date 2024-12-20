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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

//Define nome e grupo do script
@TeleOp(name="OmniTestv0_6", group="OpMode")
//@Disabled
public class OmniTestv0_6 extends OpMode {

    // Declaração das variavéis públicas do código

    //Tempo que o código está rodando
    private ElapsedTime runtime = new ElapsedTime();

    //Criando objetos dos motores
    DcMotor MDF,MEF,MDT,MET,Elbow = null;

    //Criando objeto do giroscópio
    IMU imu;

    //Criando variaveis dos valores dos gamepad
    double G1LY, G1LX, G1RX, G2LY;
    boolean LB2, RB2;

    //Criando variavel do Yaw da telemetria
    double orientation;

    //Criando variaveis das potências dos Motores
    double PMEF, PMET, PMDF, PMDT;

    //Criando variaveis dos valores alterados dos vetores
    double X, Y;

    //Criando objeto do servo
    private Servo srvPulD, srvPulE, srvPinD, srvPinE;

    //Criando a variavel da posição do HDRex (Elbow) e sua inversão
    double posicaoHDRex, posicaoHDRexInv;

    //Criando as variaveis que receberá a posição dos servos do pulso (Já convertidas)
    double posicaoSrvPD, posicaoSrvPE;

    double srvPinEPosi = 0;
    double srvPinDPosi = 1;

    //Variaveis de tempo (Cooldown da pinça)
    double TempoAtivoL, TempoAtivoR, TempoAlvoL, TempoAlvoR, cooldown;

    @Override
    public void init() {

        //Atribuindo aos objetos seus respectivos motores na configuração
        MEF = hardwareMap.get(DcMotor.class, "LeftDriveUp");
        MET = hardwareMap.get(DcMotor.class, "LeftDriveDown");
        MDF = hardwareMap.get(DcMotor.class, "RightDriveUp");
        MDT = hardwareMap.get(DcMotor.class, "RightDriveDown");
        Elbow = hardwareMap.get(DcMotor.class, "Elbow");

        //Definindo as direções dos motores
        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);
        Elbow.setDirection(DcMotor.Direction.FORWARD);

        //Resetando encoder do motor do "cotovelo" da garra
        Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Atribuindo ao objeto do imu seu dispositivo na configuração
        imu = hardwareMap.get(IMU.class, "imu");

        //Atribuindo aos servos seus respectivos motores na configuração
        //Servos do pulso
        srvPulD = hardwareMap.get(Servo.class, "srvPulD");
        srvPulE = hardwareMap.get(Servo.class, "srvPulE");
        //servos da pinça
        srvPinD = hardwareMap.get(Servo.class, "srvPinD");
        srvPinE = hardwareMap.get(Servo.class, "srvPinE");

        //Redimensionando a escala dos servos do pulso para que eles andem até no máximo 180 graus (Sem isso o smart servo rev anda 270 graus)
        srvPulD.scaleRange(0, 0.666);
        srvPulE.scaleRange(0, 0.666);

        //Alinhando os servos
        srvPulD.setPosition(0);
        srvPulE.setPosition(0);

        srvPinD.setPosition(0);
        srvPinE.setPosition(0);

        //Resetando o imu e a contagem de tempo desde o início do script
        imu.resetYaw();
        runtime.reset();
    }

    //Repete até que o código seja desligado
    public void loop() {

        //Movimentação do robô
        Mov(0.1, 0.65, 0.3); //(deadzone é a zona morta dos joysticks), (bindmin é a redução do primeiro nivel da bind), (bindmax é a redução do segundo nivel da bind)

        //Movimentação do motor do cotovelo
        Elbow(0.3, 1); //(deadzone é a zona morta dos joysticks), (reduction é a redução da potência do motor Elbow)

        //Movimentação dos servos do pulso
        ServoPD();
        ServoPE();

        //Movimentação da Pinça
        Pinça(0.27, 0, 1, 0, 1); //(Cooldown é o tempo de espera necessário entre um input da pinça e outro) (As demais variáveis dizem respeito as coodernadas que os servos ficarão alternando)

        //Telemetria
        telemetry();

    }

    //Função de Movimentação
    public void Mov(double deadzone, double bindmin, double bindmax){

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

                MEF.setPower(PMEF*bindmin);
                MET.setPower(PMET*bindmin);
                MDF.setPower(PMDF*bindmin);
                MDT.setPower(PMDT*bindmin);

            } else if (gamepad1.right_trigger > 0.8) { //Segundo nível da bind
                MEF.setPower(PMEF*bindmax);
                MET.setPower(PMET*bindmax);
                MDF.setPower(PMDF*bindmax);
                MDT.setPower(PMDT*bindmax);

            } else { //Sem bind
                MEF.setPower(PMEF);
                MET.setPower(PMET);
                MDF.setPower(PMDF);
                MDT.setPower(PMDT);
            }
        }else{ //Sem comandos

            MEF.setPower(0);
            MET.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);

        }
    }

    //Função de Movimentação do HDRex do "cotovelo" do robô
    public void Elbow(double deadzone, double reduction){ //Deadzone é o tamanho da zona morta, e reduction a redução de potência a ser aplicada no HDRex

        //Atribuindo a variavel o input dos gamepad
        G2LY = -gamepad2.left_stick_y; // Invertendo o eixo Y (cima positivo)

        //Se o comando do piloto for maior que a zona morta o motor pode ser movido
        if (G2LY > deadzone || G2LY < -deadzone) {

            //Se o motor passar do intervalo de movimentação (-2048 até 2048), o motor para e só aceita comandos que voltem pro intervalo
            if (Elbow.getCurrentPosition() > 2048) {

                Elbow.setPower(0);
                Elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                if (G2LY < 0){

                    Elbow.setPower(G2LY*reduction);

                }

            } else if (Elbow.getCurrentPosition() < -2048) { //Se o motor passar do intervalo de movimentação (-2048 até 2048), o motor para e só aceita comandos que voltem pro intervalo

                Elbow.setPower(0);
                Elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                if (G2LY > 0){

                    Elbow.setPower(G2LY*reduction);

                }

            }else{ //Se o motor estiver no intervalo correto (-2048 até 2048)

                Elbow.setPower(G2LY*reduction);

            }

        }else{ //Se os comandos do piloto não forem maiores que a zona morta o motor não deve ser movido

            Elbow.setPower(0);

        }
    }

    //Função de movimentação do servo do pulso da direita (Em relação a posição do HDRex)
    public void ServoPD() {
        //Atribuindo a variavel o valor do motor Elbow
        posicaoHDRex = Elbow.getCurrentPosition();

        //Mapeia a posição do HDRex para a posição do servo (Faz uma compensação de ticks para coodenadas do servo para que ambos andem o mesmo tanto em ângulos)
        posicaoSrvPD = map(posicaoHDRex, -2048, 2048, 0, 1); //Seta os intervalos máximos e mínimos
        srvPulD.setPosition(posicaoSrvPD); //Manda o servo para posição já calibrada
    }
    //Função de movimentação do servo do pulso da esquerda (Em relação a posição do HDRex)
    public void ServoPE() {
        //Atribuindo a variavel o valor do motor Elbow invertido (Um dos servos está de costas na montagem)
        posicaoHDRexInv = -Elbow.getCurrentPosition();

        //Mapeia a posição do HDRex para a posição do servo (Faz uma compensação de ticks para coodenadas do servo para que ambos andem o mesmo tanto em ângulos)
        posicaoSrvPE = map(posicaoHDRexInv, -2048, 2048, 0, 1); //Seta os intervalos máximos e mínimos
        srvPulE.setPosition(posicaoSrvPE); //Manda o servo para posição já calibrada
    }

    //Função de mapeamento de valores (Transforma os valores do encoder do motor (Elbow) em coordenadas do servo (srvPD e srvPE))
    public double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public void Pinça(double Cooldown, double srvPinEPosA, double srvPinEPosB, double srvPinDPosA, double srvPinDPosB){
        //Atribuindo as variaveis seus respectivos valores
        LB2 = gamepad2.left_bumper;
        RB2 = gamepad2.right_bumper;

        cooldown = Cooldown; //cooldown entre um input e outro
        TempoAtivoR = TempoAtivoL = runtime.seconds(); //Tempo decorrido desde o início do código em segundos

        if (LB2 == true && TempoAtivoL > TempoAlvoL){ //Condição que verifica se o botão ta pressionado e se o cooldown já ocorreu completamente

            //Condições que verificam a posição que o servo está, alternando sua posição entre a primeira e a segunda posição
            if (srvPinEPosi == srvPinEPosA){ //Se o servo estiver na posição A

                srvPinE.setPosition(srvPinEPosB);
                srvPinEPosi = srvPinEPosB;

            } else if (srvPinEPosi == srvPinEPosB){ //Se o servo estiver na posição B

                srvPinE.setPosition(srvPinEPosA);
                srvPinEPosi = srvPinEPosA;

            }else{ //Se o servo não estiver em nenhuma das duas posições
                srvPinE.setPosition(srvPinEPosA);
                srvPinEPosi = srvPinEPosA;
            }

            //Soma ao tempo decorrido o valor do cooldown, deixando está parte do código em espera novamente
            TempoAlvoL = TempoAtivoL + cooldown;

        }

        if (RB2 == true && TempoAtivoR > TempoAlvoR){ //Condição que verifica se o botão ta pressionado e se o cooldown já ocorreu completamente

            //Condições que verificam a posição que o servo está, alternando sua posição entre a primeira e a segunda posição
            if (srvPinDPosi == srvPinDPosA){ //Se o servo estiver na posição A

                srvPinD.setPosition(srvPinDPosB);
                srvPinDPosi = srvPinDPosB;

            } else if (srvPinDPosi == srvPinDPosB){ //Se o servo estiver na posição B

                srvPinD.setPosition(srvPinDPosA);
                srvPinDPosi = srvPinDPosA;

            }else{ //Se o servo não estiver em nenhuma das duas posições
                srvPinD.setPosition(srvPinDPosA);
                srvPinDPosi = srvPinDPosA;
            }

            //Soma ao tempo decorrido o valor do cooldown, deixando está parte do código em espera novamente
            TempoAlvoR = TempoAtivoR + cooldown;

        }
    }

    public void telemetry(){

        orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        //Yaw
        //telemetry.addData("Yaw", String.format("%.2f", orientation));
        //Valores do gamepad
        //telemetry.addData("G1LY", String.format("%.2f", G1LY));
        //telemetry.addData("G1LX", String.format("%.2f", G1LX));
        //telemetry.addData("G1RX", String.format("%.2f", G1RX));
        telemetry.addData("RB2", RB2);
        telemetry.addData("LB2", LB2);
        //Potência dos Motores
        //telemetry.addData("PMEF", String.format("%.2f", PMEF));
        //telemetry.addData("PMET", String.format("%.2f", PMET));
        //telemetry.addData("PMDF", String.format("%.2f", PMDF));
        //telemetry.addData("PMDT", String.format("%.2f", PMDT));
        //Vetores Rotacionados
        //telemetry.addData("rotY", String.format("%.2f", Y));
        //telemetry.addData("rotX", String.format("%.2f", X));
        //Valor do gatilho/bind
        //telemetry.addData("RTG1", String.format("%.2f", gamepad1.right_trigger));
        //Valor do encoder do motor do "cotovelo" da garra
        telemetry.addData("Encoder Elbow", posicaoHDRex);
        //Posição do servo do "pulso" da garra
        telemetry.addData("srvPulD", srvPulD.getPosition());
        telemetry.addData("srvPulE", srvPulE.getPosition());
        //Posição dos servos da pinça
        telemetry.addData("srvPinD", srvPinD.getPosition());
        telemetry.addData("srvPinDPosi", srvPinDPosi);

        telemetry.addData("srvPinE", srvPinE.getPosition());
        telemetry.addData("srvPinEPosi", srvPinEPosi);

        //Variaveis de tempo do cooldown da pinça
        telemetry.addData("TempoAtivoR", TempoAtivoR);
        telemetry.addData("TempoAlvoR", TempoAlvoR);

        telemetry.addData("TempoAtivoL", TempoAtivoL);
        telemetry.addData("TempoAlvoL", TempoAlvoL);

        telemetry.addData("Runtime (seconds)", runtime.seconds());

        telemetry.update();

    }

}