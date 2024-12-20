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

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

//Define nome e grupo do script
@TeleOp(name="OmniTestv0_9", group="OpMode")
//@Disabled
public class OmniTestv0_9 extends OpMode {

    //Tempo que o código está rodando
    private ElapsedTime runtime = new ElapsedTime();

    //Criando objetos dos motores
    DcMotor MDF, MEF, MDT, MET, ElbowA, ElbowB, Slider, Wrist = null;

    //Criando objeto do giroscópio
    IMU imu;

    //Criando objeto do servo
    private Servo srvPinD, srvPinE, srvPlane;
    public int josue = 0;

    // Declaração das variavéis públicas do código

    //Criando variaveis dos valores dos gamepad
    double G1LY, G1LX, G1RX, G2RY, G2LY;
    boolean LB2, RB2, Dpad2Up, Dpad2Down, G2B;

    //Criando variavel do Yaw da telemetria
    double orientation;

    //Criando variaveis das potências dos Motores
    double PMEF, PMET, PMDF, PMDT;

    //Criando variaveis dos valores alterados dos vetores
    double X, Y;

    //Criando a variavel da posição do HDRex (Elbow) e sua inversão
    double posicaoHDRex, posicaoHDRexInv;

    //Criando as variaveis que receberão a posição dos servos do pulso (Já convertidas)
    double posicaoSrvPD, posicaoSrvPE;

    double posicaoWrist = 0;

    //Variaveis que guardam a posição dos servos da pinça
    double srvPinEPosi = 0;
    double srvPinDPosi = 1;

    //Variaveis de tempo (Cooldown da pinça)
    double TempoAtivoL, TempoAtivoR, TempoAlvoL, TempoAlvoR, TempoPulso, TempoAlvoPulso;

    double cooldown;

    //Variável do cálculo de estabilização do elbow
    double errorElbow = 0;

    //Variável do cálculo de estabilização do slider
    double errorSlider= 0;

    //Controle de Potência da função Elbow
    double ElbowPow;

    //Setpoint desejado
    int Elbowsetpoint = 0;  //Guarda a posição para estabilizar
    int Slidersetpoint = 0; //Guarda a posição para estabilizar

    @Override
    public void init() {

        //Atribuindo ao objeto do imu seu dispositivo na configuração
        imu = hardwareMap.get(IMU.class, "imu");

        //Parâmetros do imu

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);


        imu.initialize(new IMU.Parameters(orientationOnRobot));

        //Atribuindo aos objetos seus respectivos motores na configuração
        MEF = hardwareMap.get(DcMotor.class, "LeftDriveUp");
        MET = hardwareMap.get(DcMotor.class, "LeftDriveDown");
        MDF = hardwareMap.get(DcMotor.class, "RightDriveUp");
        MDT = hardwareMap.get(DcMotor.class, "RightDriveDown");
        ElbowA = hardwareMap.get(DcMotor.class, "ElbowA");
        ElbowB = hardwareMap.get(DcMotor.class, "ElbowB");
        Slider = hardwareMap.get(DcMotor.class, "Slider");
        Wrist = hardwareMap.get(DcMotor.class, "Wrist");

        //Definindo as direções dos motores
        MDF.setDirection(DcMotor.Direction.FORWARD);
        MDT.setDirection(DcMotor.Direction.FORWARD);
        MEF.setDirection(DcMotor.Direction.REVERSE);
        MET.setDirection(DcMotor.Direction.REVERSE);
        ElbowA.setDirection(DcMotor.Direction.FORWARD);
        ElbowB.setDirection(DcMotor.Direction.REVERSE);
        Slider.setDirection(DcMotor.Direction.FORWARD);
        Wrist.setDirection(DcMotor.Direction.FORWARD);

        //Resetando encoder do motor do "cotovelo" da garra

        ElbowA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ElbowA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ElbowB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ElbowB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slider.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        //Atribuindo aos servos seus respectivos motores na configuração
        //Servos do pulso
        //Servos da pinça
        srvPinD = hardwareMap.get(Servo.class, "srvPinD");
        srvPinE = hardwareMap.get(Servo.class, "srvPinE");
        srvPlane = hardwareMap.get(Servo.class, "srvPlane");

        //Redimensionando a escala dos servos do pulso para que eles andem até no máximo 180 graus (Sem isso o smart servo rev anda 270 graus)

        srvPinD.setPosition(0);
        srvPinE.setPosition(1);

        //Resetando o imu e a contagem de tempo desde o início do script
        imu.resetYaw();
        runtime.reset();
    }

    //Repete até que o código seja desligado
    public void loop() {

        //Movimentação do robô
        Mov(0.3, 0.7, 0.5, 0.75); //(deadzone é a zona morta dos joysticks), (bindmin é a redução do primeiro nivel da bind), (bindmax é a redução do segundo nivel da bind)

        //Movimentação do motor do cotovelo
        Elbow(2550, -100, 0.3, 0.5, -0.1, 0.4);

        //Movimentação do Slider
        Slider(0.3,  0, 2000, 0.3);

        //Movimentação dos servos do pulso
        //ServoPulD();
        //ServoPulE();
        Pulso();

        //Movimentação da Pinça
        Pinça(0.2, 0, 1, 1, 0); //(Cooldown é o tempo de espera necessário entre um input da pinça e outro) (As demais variáveis dizem respeito as coodernadas que os servos ficarão alternando)

        //Função do Avião
        Plane(1);
        //Telemetria
        telemetry();

    }

    //Função de Movimentação
    public void Mov(double deadzone, double bindmin, double bindmax, double frontsidereduction){

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

                MEF.setPower((PMEF*bindmin)*frontsidereduction); //Redução na frente por causa do peso extra atrás do robô
                MET.setPower(PMET*bindmin);
                MDF.setPower((PMDF*bindmin)*frontsidereduction); //Redução na frente por causa do peso extra atrás do robô
                MDT.setPower(PMDT*bindmin);

            } else if (gamepad1.right_trigger > 0.8) { //Segundo nível da bind
                MEF.setPower((PMEF*bindmax)*frontsidereduction); //Redução na frente por causa do peso extra atrás do robô
                MET.setPower(PMET*bindmax);
                MDF.setPower((PMDF*bindmax)*frontsidereduction); //Redução na frente por causa do peso extra atrás do robô
                MDT.setPower(PMDT*bindmax);

            } else { //Sem bind
                MEF.setPower(PMEF*frontsidereduction); //Redução na frente por causa do peso extra atrás do robô
                MET.setPower(PMET);
                MDF.setPower(PMDF*frontsidereduction); //Redução na frente por causa do peso extra atrás do robô
                MDT.setPower(PMDT);
            }
        }else{ //Sem comandos

            MEF.setPower(0);
            MET.setPower(0);
            MDF.setPower(0);
            MDT.setPower(0);
            MEF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MET.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MDT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
    }

    //Função de Movimentação do HDRex do "cotovelo" do robô
    public void Elbow(double posmax, double posmin, double deadzone, double powEstCima, double powEstBaixo, double powEst){

        G2LY = -gamepad2.left_stick_y;
        double intervaloEst = 60; //Intervalo em que o elbow tentara se manter em volta da posição setpoint

        if (G2LY > deadzone || G2LY < -deadzone){ //Se estiver recebendo inputs do piloto

            if (ElbowA.getCurrentPosition() > posmax){ //Se passar da posição maxima

                if(G2LY < -deadzone){
                    ElbowA.setPower(G2LY);
                    ElbowB.setPower(G2LY);
                }else{

                    ElbowA.setPower(0);
                    ElbowA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    ElbowB.setPower(0);
                    ElbowB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                }

            }else if(ElbowA.getCurrentPosition() < posmin){ //Se passar da posição minima

                if(G2LY > deadzone){
                    ElbowA.setPower(G2LY);
                    ElbowB.setPower(G2LY);
                }else{

                    ElbowA.setPower(0);
                    ElbowA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    ElbowB.setPower(0);
                    ElbowB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                }

            }else if (ElbowA.getCurrentPosition() < posmax  && ElbowA.getCurrentPosition() > posmin){ //Se estiver dentro do intervalo

                ElbowA.setPower(G2LY);
                ElbowB.setPower(G2LY);

            }

            Elbowsetpoint = ElbowA.getCurrentPosition(); //Após qualquer comando do piloto o setpoint é setado

        }else if(G2LY > -deadzone && G2LY < deadzone){ //Se estiver dentro da deadzone (Sem comandos do piloto)

            errorElbow = Elbowsetpoint - ElbowA.getCurrentPosition(); //Calcula o deslocamento do Elbow em relação ao setpoint

            //OBS intervaloEst é a quantidade que o motor pode estar deslocado do setpoint, dando a ele um intervalo onde pode ficar, não apenas uma coordenada
            if(errorElbow > intervaloEst){ //Setpoint está muito maior que a posição atual (Move pra frente/cima)

                //Velocidade pra ir pra cima
                ElbowA.setPower(powEstCima);
                ElbowB.setPower(powEstCima);

            }else if(errorElbow < -intervaloEst){ //Setpoint está muito menor que a posição atual (Move pra trás/baixo)

                //Velocidade pra ir pra baixo
                ElbowA.setPower(powEstBaixo);
                ElbowB.setPower(powEstBaixo);

            }else if(errorElbow > -intervaloEst && errorElbow < intervaloEst && ElbowA.getCurrentPosition() > 140){ //Está dentro do intervalo, mas é maior que 140

                //Potência de estabilização
                ElbowA.setPower(powEst);
                ElbowB.setPower(powEst);

            }

        }

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

    public void Slider(double deadzone, double posmin, double posmax, double PowerEst){

        G2RY = -gamepad2.right_stick_y;

        //Reseta o encoder se o dpad right for pressionado
        if (gamepad2.dpad_right) {

            Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Slider.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }

        //Se o piloto estiver enviando inputs
        if (G2RY < -deadzone || G2RY > deadzone) {

            //Diferencia se o movimento é para cima ou para baixo

            //Se o slider passou da posição maxima
            if (Slider.getCurrentPosition() > posmax){

                if (G2RY < deadzone){ //Só aceita inputs que voltem pro intervalo

                    Slider.setPower(G2RY);

                }else{

                    Slider.setPower(0);
                    Slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                }

            }else if(Slider.getCurrentPosition() < posmin){ //Se o slider passou da posição mínima

                if (G2RY > deadzone){ //Só aceita inputs que voltem pro intervalo

                    Slider.setPower(G2RY);

                }else{

                    Slider.setPower(0);
                    Slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                }

            }else if (Slider.getCurrentPosition() >= posmin && Slider.getCurrentPosition() <= posmax){ //Se estiver no intervalo mova livremente

                Slider.setPower(G2RY);

            }

            Slidersetpoint = Slider.getCurrentPosition(); //Define a última posição enviada pelo piloto como setpoint para estabilização

        } else {

            // Cálculo do erro
            errorSlider = Slidersetpoint - Slider.getCurrentPosition();

            //Se nenhum input está sendo enviado pelo piloto
            if (G2RY > -deadzone && G2RY < deadzone){

                if (errorSlider > 30){ //Ta abaixo do que deveria

                    Slider.setPower(0.3);

                }else if(errorSlider < -30){ //Ta acima do que deveria

                    Slider.setPower(-0.3);

                }else if (errorSlider > -30 && errorSlider < 30 && Slider.getCurrentPosition() > 100){ //Se estiver no intervalo, apenas estabilize

                    Slider.setPower(0);
                    Slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                }
            }
        }
    }

    public void Pulso(){

        //Atribuindo as variaveis seus respectivos valores
        G2B = gamepad2.b;
        TempoPulso = runtime.seconds();

        Wrist.setPower(0.5);

        if (G2B == true && TempoPulso > TempoAlvoPulso){ //Condição que verifica se o botão ta pressionado e se o cooldown já ocorreu completamente

            if (Wrist.getCurrentPosition() > 130){

                Wrist.setTargetPosition(0);
                Wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }else if(Wrist.getCurrentPosition() < 10){

                Wrist.setTargetPosition(140);
                Wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }

            Wrist.setPower(0);
            Wrist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //Soma ao tempo decorrido o valor do cooldown, deixando está parte do código em espera novamente
            TempoAlvoPulso = TempoPulso + cooldown;

        }
    }

    public void Plane(double poslanc){

        //Se o botão A do controle 2 for pressionado, mova o servo e lance o avião
        if (gamepad2.a){

            srvPlane.setPosition(poslanc);



        }

    }

    public void telemetry(){

        telemetry.addData("josueeeeeeeeeeeeeeeeeeeeeeeeeeeee", josue);
        telemetry.addData("G2LY", G2LY);
        telemetry.addData("Power ElbowA", ElbowA.getPower());
        telemetry.addData("Power ElbowB", ElbowB.getPower());
        telemetry.addData("Encoder Elbow", ElbowA.getCurrentPosition());
        telemetry.addData("setpointElbow", Elbowsetpoint);
        telemetry.addData("Dpad2Up", Dpad2Up);
        telemetry.addData("DpaD2Down", Dpad2Down);
        telemetry.addData("Elbow Error", errorElbow);

        telemetry.addData("WristPow", Wrist.getPower());
        telemetry.addData("WristPos", Wrist.getCurrentPosition());

        telemetry.addData("ServoPlan", srvPlane.getPosition());

        //Posição do servo do "pulso" da garra
        telemetry.addData("Error Slider", errorSlider);
        telemetry.addData("Slider Setpoint", Slidersetpoint);
        telemetry.addData("SliderPos", Slider.getCurrentPosition());
        telemetry.addData("Slider Pow", Slider.getPower());
        telemetry.addData("G2RY", G2RY);

        orientation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        //Valor do encoder do motor do "cotovelo" da garra

        //Yaw
        //telemetry.addData("Yaw", String.format("%.2f", orientation));
        //Valores do gamepad
        //telemetry.addData("G1LY", String.format("%.2f", G1LY));
        //telemetry.addData("G1LX", String.format("%.2f", G1LX));
        //telemetry.addData("G1RX", String.format("%.2f", G1RX));
        //telemetry.addData("RB2", RB2);
        //telemetry.addData("LB2", LB2);
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

        //Posição dos servos da pinça
        telemetry.addData("srvPinD", srvPinD.getPosition());
        //telemetry.addData("srvPinDPosi", srvPinDPosi);

        telemetry.addData("RB2", RB2);
        telemetry.addData("LB2", LB2);

        telemetry.addData("srvPinE", srvPinE.getPosition());
        //telemetry.addData("srvPinEPosi", srvPinEPosi);

        //Variaveis de tempo do cooldown da pinça
        //telemetry.addData("TempoAtivoR", TempoAtivoR);
        //telemetry.addData("TempoAlvoR", TempoAlvoR);

        //telemetry.addData("TempoAtivoL", TempoAtivoL);
        //telemetry.addData("TempoAlvoL", TempoAlvoL);

        telemetry.addData("Runtime (seconds)", runtime.seconds());

        telemetry.update();

    }

}