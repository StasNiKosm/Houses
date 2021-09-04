package application.fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameMandelbrot extends JFrame {


    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private Timer timer = new Timer(100, e -> {
        repaint();
    });
    private int d = 255;
    private double f = 1.0;
    private int lr = 0;
    private int ud = 0;

    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double a = (x - (WIDTH / 2.0) - lr) / ((WIDTH / 4.0 ) / f);
                double aa = a;
                double b = (y - (HEIGHT  / 2.0) - ud) / ((HEIGHT / 4.0 ) / f);
                double bb = b;
                int countIter = 0;
                double k;
                while (countIter < d) {
                    k = a;
                    a = a * a - b * b;
                    a += aa;
                    b = 2 * k * b;
                    b += bb ;
                    countIter++;
                    if (a * a + b * b >= 4.0) {
                       // if (countIter % 2 == 1) g.setColor(Color.black);
                       // else g.setColor(Color.white);
                        //g.fillRect(x - WIDTH / 2 + lr, y - HEIGHT / 2 + ud, 1, 1);
                        g.setColor(new Color(countIter < 55 ? 0 :countIter,0, countIter));
                        g.fillRect(x , y, 1, 1);
                        break;
                    }
                }
            }
        }
    }

    private boolean[][] getMandelbrotSet(){
        boolean[][] matrix = new boolean[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double a = (x - (WIDTH + lr) / 2.0) / (WIDTH / f) ;
                double b = (y - (HEIGHT + ud) / 2.0) / (HEIGHT / f) ;
                int countIter = 0;
                do {
                    double k = a;
                    a = a * a - b * b;
                    a += (x - (WIDTH + lr) / 2.0) / (WIDTH / f);
                    b = 2 * k * b;
                    b += (y - (HEIGHT + ud) / 2.0) / (HEIGHT / f);
                    countIter++;
                    if (a * a + b * b >= 4.0) {
                        matrix[x][y] = true;
                    }
                } while (countIter < d);
            }
        }
        return matrix;
    }




    public FrameMandelbrot() throws HeadlessException {
        super(":)");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        Frame frame = this;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                f /= 2;
                lr += (WIDTH/2 - e.getX());
                ud += (HEIGHT/2 - e.getY());
                lr *= 2;
                ud *= 2;
                frame.repaint();
            }
        });

        /*this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_UP) {
                    ud += 50*f/1.5;
                    frame.repaint();
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    ud -= 50*f/1.3;
                    frame.repaint();
                }
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    f *= 2.0;
                    frame.repaint();
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    lr += 50;
                    frame.repaint();
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    lr -= 50;
                    frame.repaint();
                }
            }
        });*/


        // timer.start();
    }

}
