package application.fractals;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovingFractal implements Runnable {

    private List<Integer> listX ;
    private List<Integer> listY ;

    private int lastX = 0;
    private int lastY = 0;

    private List<Integer> listXs = new ArrayList<>();
    private List<Integer> listYs  = new ArrayList<>();

    private int randomPoint;

    public MovingFractal(List<Integer> listX, List<Integer> listY, int randomPoint) {
        this.listX = listX;
        this.listY = listY;
        this.randomPoint = randomPoint;
        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    @Override
    public void run() {
        try {
            lastX = listX.get(randomPoint);
            lastY = listY.get(randomPoint);
            int count = 0;
            while (listX.size() >= 3 && count < 100000) {
                int randomPointNext = ((Double) (Math.random() * 3)).intValue();
                lastX = listX.get(randomPointNext) - (listX.get(randomPointNext) - lastX) / 2;
                listXs.add(lastX);
                lastY = listY.get(randomPointNext) - (listY.get(randomPointNext) - lastY) / 2;
                listYs.add(lastY);
                count++;
                //System.out.println(lastX + " " + lastY);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void paint(Graphics g){
        g.setColor(Color.black);
        for (int i = 0; i < listXs.size(); i++) {
            g.fillOval(listXs.get(i) - 1, listYs.get(i) - 1, 2, 2);
        }
    }

}
