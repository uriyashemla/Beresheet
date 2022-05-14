 import javax.swing.*;

    public class Simulation {

        Bereshit_Spaceship bs;

        static double lastALT;
        static double lastHS;

        static double lastVS;
        static int x;
        static int y;

        public Simulation() {
            bs = new Bereshit_Spaceship();
            x = (int) bs.getLocation().x;
            y = (int) bs.getLocation().y;
            loop();
        }


        public void loop() {

            lastALT = bs.getAlt();
            lastHS = bs.getHS();

            while (bs.getAlt() > Moon.realDestinationPoint.y && bs.getLat() < Moon.realDestinationPoint.x) {

                if (bs.getTime() % 10 == 0 || bs.getAlt() < 100) {
                    bs.print();
                }

                bs.NNControl();
                bs.updateEngines();
                double ang_rad = Math.toRadians(bs.getAng());
                double h_acc = Math.sin(ang_rad) * bs.getAcc();
                double v_acc = Math.cos(ang_rad) * bs.getAcc();
                double vacc = Moon.getAcc(bs.getHS());

                bs.timer();
                bs.fuelControl();
                v_acc -= vacc;
                bs.speedControl(h_acc, v_acc);
                bs.loactionUpdate();



                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public static void main(String[] args) {
            new Simulation();
        }

    }

