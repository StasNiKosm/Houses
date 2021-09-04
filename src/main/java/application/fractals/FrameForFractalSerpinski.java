package application.fractals;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class FrameForFractalSerpinski extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Map<Integer, Integer> mapThreePoints = new HashMap<>();
    List<Integer> listX;
    List<Integer> listY;

   /* private List<Integer> listX = new ArrayList<>(Arrays.asList(new Integer[]{350, 50, 650}));
    private List<Integer> listY = new ArrayList<>(Arrays.asList(new Integer[]{50, 450, 450}));
*/
    private boolean start = false;

    private Timer repaintTimer = new Timer(10, e -> repaint());

    private MovingFractal movingFractal;

    private int lastX = 0;
    private int lastY = 0;

    private double d = 2.0;

    private List<Integer> listXs = new ArrayList<>();
    private List<Integer> listYs  = new ArrayList<>();

    private int randomPoint;

    public void paint(Graphics g) {
        super.paint(g);

        listX = new ArrayList<>(mapThreePoints.keySet());
        listY = new ArrayList<>(mapThreePoints.values());

        g.setColor(Color.black);
        for (int i = 0; i < listX.size(); i++) g.fillOval(listX.get(i) - 2, listY.get(i) - 2, 4, 4);

       // if (mapThreePoints.size() == 3) {
         //   if(start) {

                //System.out.println("hu");
               // movingFractal.run();
               // start = false;
           // }
           // this.movingFractal.paint(g);
      //  }

      //  try {
        if(mapThreePoints.size() == 3) {
            lastX = listX.get(randomPoint);
            lastY = listY.get(randomPoint);
            int count = 0;
            for (int i = 0; i < listXs.size(); i++) {
                //g.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
                g.fillOval(listXs.get(i)-1, listYs.get(i)-1, 2, 2);
            }
            while (listX.size() >= 3 && count < 10000) {
                int randomPointNext = ((Double) (Math.random() * 3)).intValue();
                lastX = (int)(listX.get(randomPointNext) - (listX.get(randomPointNext) - lastX) / d);
                listXs.add(lastX);
                lastY = (int)(listY.get(randomPointNext) - (listY.get(randomPointNext) - lastY) / d);
                listYs.add(lastY);
                count++;
                //System.out.println(lastX + " " + lastY);
                //Thread.sleep(10);
                //this.wait(10);
                // }

                //} catch (InterruptedException e) {
                //    e.printStackTrace();
            }
        }
    }

    public FrameForFractalSerpinski(){
        super(":)");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        JFrame frame = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(mapThreePoints.size() < 3) {
                    mapThreePoints.put(e.getX(), e.getY());
                    frame.repaint();
                }
                if(mapThreePoints.size() == 3) frame.repaint();
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    d -= 0.1;
                    frame.repaint();
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    d += 0.1;
                    frame.repaint();
                }
            }
        });
       // this.movingFractal = new MovingFractal(this.listX, this.listY, ((Double)(Math.random()*3)).intValue());
        this.randomPoint = ((Double)(Math.random()*3)).intValue();
       // repaintTimer.start();
    }
}
