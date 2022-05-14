

    import java.awt.Color;
    import java.awt.Graphics;
    import java.awt.Image;
    import javax.swing.*;

    public class Simulation extends JFrame {

        Bereshit_Spaceship bs;

        final Image BackroundPath = new ImageIcon(
                "C:\\Users\\uriya\\Computer Science\\gitFolder\\Bereshit\\images\\Moon.png")
                .getImage();
        final Image SpacecraftPath = new ImageIcon(
                "C:\\Users\\uriya\\Computer Science\\gitFolder\\Bereshit\\images\\shuttle2.png")
                .getImage();

        int NormalizePixel_Y = 40;
        int NormalizePixel_X = 85;

        static double lastALT;
        static double lastHS;

        static double lastVS;
        static int x;
        static int y;

        public Simulation() {
            bs = new Bereshit_Spaceship();
            x = (int) bs.getLocation().x;
            y = (int) bs.getLocation().y;

            setSize(1200, 750);
            setLayout(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);

            loop();
        }

        public void paint(Graphics g) {
            g.drawImage(BackroundPath, 0, 0, 1200, 750, null);
            g.setColor(Color.CYAN);
            g.fillOval(Moon.LANDING_AREA_X, Moon.LANDING_AREA_Y, 40, 15);// Landing area
            g.drawImage(SpacecraftPath, x, y, 25, 25, null);
            String hsg = String.valueOf(bs.getHS());
            String nng = String.valueOf(bs.getNN());
            String timeg = String.valueOf(bs.getTime());
            String vsg = String.valueOf(bs.getVS());
            String disg = String.valueOf(bs.getDist());
            String latg = String.valueOf(bs.getLat());
            String altg = String.valueOf(bs.getAlt());
            String angg = String.valueOf(bs.getAng());
            String weig = String.valueOf(bs.getWeight());
            String accg = String.valueOf(bs.getAcc());
            String fug = String.valueOf(bs.getFuel());
            g.drawString("HS :"+hsg,1000,50);
            g.drawString("VS :"+vsg,1000,65);
            g.drawString("NN :"+nng,1000,80);
            g.drawString("Time :"+timeg,1000,95);
            g.drawString("Dist :"+disg,1000,110);
            g.drawString("Lat :"+latg,1000,125);
            g.drawString("Alt :"+altg,1000,140);
            g.drawString("Ang :"+angg,1000,155);
            g.drawString("Weight :"+weig,1000,170);
            g.drawString("Acc :"+accg,1000,185);
            g.drawString("Fuel :"+fug,1000,200);
        }

        public void loop() {

            lastALT = bs.getAlt();
            lastHS = bs.getHS();

            while (bs.getAlt() > Moon.realDestinationPoint.y && bs.getLat() < Moon.realDestinationPoint.x) {

                repaint();

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

                if ((lastALT - bs.getAlt() > 1000) && (lastHS - bs.getHS() > 60)) {

                    y = (int) (bs.getLocation().y + NormalizePixel_Y);
                    x = (int) (bs.getLocation().x + NormalizePixel_X);

                    bs.setLocation(x, y);

                    lastHS = bs.getHS();
                    lastALT = bs.getAlt();
                }

                else if (bs.getAlt() < 1000) {
                    y = (int) (bs.getLocation().y + 1);
                    bs.setLocation(bs.getLocation().x, y);
                }

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

