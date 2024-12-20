package org.firstinspires.ftc.teamcode.PowerPlay;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name="Autonomo V5", group="LinearOpmode", preselectTeleOp = "OPMODE")
@Disabled


public class Autonomo_V5 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor Tras_d;
    private DcMotor Tras_e;
    private DcMotor Slider;
    private Servo ServoG;

    private DistanceSensor Sensor;

    @Override
    public void runOpMode() {

        Tras_d = hardwareMap.dcMotor.get("Rev Hex Motor 1");
        Tras_e = hardwareMap.dcMotor.get("Rev Hex Motor 0");
        Slider = hardwareMap.dcMotor.get("GoBilda Motor 2");
        ServoG = hardwareMap.get(Servo.class, "Servo 0");

        Sensor = hardwareMap.get(DistanceSensor.class, "Distance Sensor 1");

        Tras_d.setDirection(DcMotor.Direction.FORWARD);
        Tras_e.setDirection(DcMotor.Direction.REVERSE);
        Slider.setDirection(DcMotor.Direction.FORWARD);

        Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        runtime.reset();
        
        double PowerD;
        double PowerE;
        double DISTANCIA;
        
        //ANDAR
        andarENCODER(0.8, 0.8, 1000);
        
        sleep(500);

        //RODAR NA DIREÇÃO DA HASTE
        andarENCODER(-1, 1, 300);
        
        //ANDAR UM POUCO PARA FRENTE
        andarENCODER(0.4, 0.4, 600);
        
        sleep(3000);

        //SUBIR GARRA NA HASTE MAIOR
        Slider.setTargetPosition(3930);
        Slider.setPower(0.8);
        Slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sleep(3000); 
        


        int LIMITE = 35; //limite de distancia
        int STATUS_ENTREGA = 0;
        int STATUS_PROCDIREITA = 1;
        int STATUS_PROCESQUERDA = 0;
        int LIMITADOR = 0;
        int contador_passos=100;
        
        //ajustes de velocidade
        //velocidade para procurar DIREITA
        double vel_md_procd = -0.2;
        double vel_me_procd=0.2;
        
        //velocidade para procurar ESQUERDA
        double vel_md_proce=0.2;
        double vel_me_proce=-0.2;
           
        while (1==1){

            double DIST = Sensor.getDistance(DistanceUnit.CM);

            if (DIST < LIMITE){
                
                Tras_d.setPower(0);
                Tras_e.setPower(0);
                STATUS_ENTREGA = 1;
                break;
            }
            else {
                    
                if (STATUS_PROCDIREITA == 1){
                    Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
                    while (Tras_d.getCurrentPosition() > -1){
        
                        Tras_d.setPower(vel_md_procd);
                        Tras_e.setPower(vel_me_procd);
                    }
                }
                  
                  
                if (STATUS_PROCESQUERDA == 1){
                    Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
                    while (Tras_d.getCurrentPosition() < 1){
        
                        Tras_d.setPower(vel_md_proce);
                        Tras_e.setPower(vel_me_proce);
                    }
                }              
            }




            LIMITADOR = LIMITADOR + 1;  
            
            if (LIMITADOR == contador_passos){
                
                if(STATUS_PROCDIREITA==1){
                STATUS_PROCESQUERDA=1; 
                STATUS_PROCDIREITA=0;
                }
                else{
                STATUS_PROCESQUERDA=0; 
                STATUS_PROCDIREITA=1;
                }
                
            
                LIMITADOR = 0;
                
                
                    
            }
            
        }

        if (STATUS_ENTREGA == 1){
            
            
            //andar(0.4, 0.4, 600);
            
            Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                while (Tras_d.getCurrentPosition() < 200){

                    Tras_d.setPower(0.3);
                    Tras_e.setPower(0.3);

                }

                Tras_d.setPower(0);
                Tras_e.setPower(0);

            ServoG.setPosition(0.2);
            sleep(1500);
            ServoG.setPosition(-0.1);
            sleep(1500);
            
            andarENCODER2(-0.4, -0.4, -300);
            
            Slider.setTargetPosition(0);
            Slider.setPower(0.8);
            Slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            sleep(3000);
            
            
            

        }
        
        STATUS_ENTREGA = 0;

    }
    
    
    public void andarENCODER(double PowerD, double PowerE, int DISTANCIA) {

        
        Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (Tras_d.getCurrentPosition() < DISTANCIA){

            Tras_d.setPower(PowerD);
            Tras_e.setPower(PowerE);

        }

        Tras_d.setPower(0);
        Tras_e.setPower(0);
        

    }
    
    public void andarENCODER2(double PowerD, double PowerE, int DISTANCIA) {

        
        Tras_d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Tras_d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (Tras_d.getCurrentPosition() > DISTANCIA){

            Tras_d.setPower(PowerD);
            Tras_e.setPower(PowerE);

        }

        Tras_d.setPower(0);
        Tras_e.setPower(0);
        

    }
}
