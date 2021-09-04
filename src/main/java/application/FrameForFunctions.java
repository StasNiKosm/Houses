package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class FrameForFunctions extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Map<Double, Double> mapXY = new TreeMap<>();

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.LIGHT_GRAY);

        int vertical = 55;
        while (vertical < this.getContentPane().getWidth() ) {
          //  g.setColor(Color.LIGHT_GRAY);
            g.drawLine(vertical, 0, vertical, this.getContentPane().getHeight() );
          //  g.setColor(Color.black);
          //  g.drawString(Integer.toString((vertical - 50) / 50 ), vertical - 10, this.getContentPane().getHeight() +5);
            vertical += 50;
        }
        int horizontal = 36;
        while (horizontal < this.getContentPane().getHeight()) {
          //  g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, horizontal, this.getContentPane().getWidth(), horizontal);
          //  g.setColor(Color.black);
          //  int n = Math.abs((horizontal - 50) / 50 - this.getContentPane().getHeight() / 50 + 2);
          //  g.drawString(n == 0 ? "" : Integer.toString(n), 35, horizontal + 15);
            horizontal += 50;
        }

        List<Double> listX = new ArrayList<>(mapXY.keySet());
        List<Double> listY = new ArrayList<>(mapXY.values());

        g.setColor(Color.black);
        for (int i = 0; i < listX.size(); i++) {
            g.fillOval(listX.get(i).intValue() - 5, listY.get(i).intValue()-5, 10, 10);
        }

        if(this.mapXY.size() > 3){
            int step = 2;
            int x = listX.get(0).intValue();
            int y = listY.get(0).intValue();
            int xNext;
            int yNext;

            SplineInterpolator splineInterpolator = new SplineInterpolator(mapXY);
            for (int i = 0; i < listX.size() - 1; i++) {
                while (x >= listX.get(i) && x < listX.get(i + 1)) {
                    xNext = x+step;
                    yNext = (int) (splineInterpolator.getListCoefficients_A().get(i)
                                                  + splineInterpolator.getListCoefficients_B().get(i) * (xNext - listX.get(i))
                                                + splineInterpolator.getListCoefficients_C().get(i) * (xNext - listX.get(i)) * (xNext - listX.get(i))
                                              + splineInterpolator.getListCoefficients_D().get(i) * (xNext - listX.get(i)) * (xNext - listX.get(i)) * (xNext - listX.get(i)));
                  g.drawLine(x, y, xNext, yNext);
                  x += step;
                  y = yNext;
                }
            }
        }
    }

    public FrameForFunctions() throws HeadlessException {
        super(":)");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        JFrame frame = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mapXY.put((double) e.getX(), (double) e.getY());
                frame.repaint();
            }
        });

    }



}
