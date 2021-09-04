package application;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class Field extends JPanel {

    private JFrame frame;

    private boolean isMagnifier;

    private Magnifier magnifier;

    private boolean paused;
    private boolean selectivePaused;

    private ArrayList<MovingFigure> figures = new ArrayList<>(10);

    private Timer repaintTimer = new Timer(10, e -> repaint());

    private boolean isId = false;
    private boolean isOnlyCommand = false;
    private String id;


    public static final String DEFAULT_ID = "A";

    public Field(JFrame frame){
        this.id = DEFAULT_ID;
        this.frame = frame;
        this.magnifier = new Magnifier();
        setBackground(Color.WHITE);
        repaintTimer.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;

        for (MovingFigure figure : figures)
            figure.paint(canvas);
        if (isMagnifier) magnifier.paint(canvas);
    }

    public void addFigure() {
        figures.add(new MovingFigure(this));
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void selectivePause() {
        selectivePaused = true;
    }

    public synchronized void resume() {
        paused = false;
        selectivePaused = false;
        /*if(isOnlyCommand){
            figures.forEach(figure->{
                if(String.valueOf(figure.getId()).equals(id)) notify();
            });
        } else*/
        notifyAll();
    }

    public synchronized void canMove(MovingFigure figure) throws InterruptedException {
        if (paused || (selectivePaused && figure.getSleepTime() < 8) || (isOnlyCommand && !String.valueOf(figure.getId()).equals(id)) ){
            figure.setWaiting(true);
            wait();
        } else figure.setWaiting(false);
    }

    public void setId(boolean id) {
        isId = id;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<MovingFigure> getFigures() {
        return figures;
    }

    public boolean isOnlyCommand() {
        return isOnlyCommand;
    }

    public void setOnlyCommand(boolean onlyCommand) {
        isOnlyCommand = onlyCommand;
    }

    public boolean isMagnifier() {
        return isMagnifier;
    }

    public void setMagnifier(boolean magnifier) {
        isMagnifier = magnifier;
    }

    public Magnifier getMagnifier() {
        return magnifier;
    }

    public JFrame getFrame() {
        return frame;
    }

    public boolean isSelectivePaused() {
        return selectivePaused;
    }
}
