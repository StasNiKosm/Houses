package application.fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class FrameTreeFractal extends JFrame {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;

    private double upDownAngle = Math.PI/6;

    private Timer timer = new Timer(100, e -> {
        this.upDownAngle += Math.PI / 36;
        repaint();
    });

    private boolean left = false;
    private boolean right = false;

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gg = (Graphics2D)g;
        gg.transform(AffineTransform.getScaleInstance(2, 2));
        int h = 100;
        int x = 225;
        int y = 350;
        g.drawLine(x, y, x, y - h);
        line(g, x, y - h, h, 0.0);
    }

    private void line(Graphics g, int x, int y, int h, double angle){
        if(h > 1) {
            h /= 1.6;
            if(left) {
                g.drawLine(x, y, x + ((Double) (h * Math.sin(angle - upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle + upDownAngle))).intValue());
                g.drawLine(x, y, x + ((Double) (h * Math.sin(angle + upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle + upDownAngle))).intValue());
                line(g, x + ((Double) (h * Math.sin(angle - upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle + upDownAngle))).intValue(), h, angle - upDownAngle);
                line(g, x + ((Double) (h * Math.sin(angle + upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle + upDownAngle))).intValue(), h, angle + upDownAngle);
            } else if (right) {
                g.drawLine(x, y, x + ((Double) (h * Math.sin(angle - upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle -upDownAngle))).intValue());
                g.drawLine(x, y, x + ((Double) (h * Math.sin(angle + upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle - upDownAngle))).intValue());
                line(g, x + ((Double) (h * Math.sin(angle - upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle - upDownAngle))).intValue(), h, angle - upDownAngle);
                line(g, x + ((Double) (h * Math.sin(angle + upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle - upDownAngle))).intValue(), h, angle + upDownAngle);
            } else {
                g.drawLine(x, y, x + ((Double) (h * Math.sin(angle - upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle -upDownAngle))).intValue());
                g.drawLine(x, y, x + ((Double) (h * Math.sin(angle + upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle + upDownAngle))).intValue());
                line(g, x + ((Double) (h * Math.sin(angle - upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle - upDownAngle))).intValue(), h, angle - upDownAngle);
                line(g, x + ((Double) (h * Math.sin(angle + upDownAngle))).intValue(), y - ((Double) (h * Math.cos(angle + upDownAngle))).intValue(), h, angle + upDownAngle);
            }
        }
    }

    public FrameTreeFractal() throws HeadlessException {
        super(":)");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        Frame frame = this;

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_UP) upDownAngle -= Math.PI/90;
                if(e.getKeyCode() == KeyEvent.VK_DOWN) upDownAngle += Math.PI/90;
                if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    left = true;
                    right = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    right = true;
                    left = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    right = false;
                    left = false;
                }
                frame.repaint();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);


       // timer.start();
    }
}
