package application;

import application.fractals.FrameForFractalSerpinski;
import application.fractals.FrameMandelbrot;
import application.fractals.FrameTreeFractal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MainFrame extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private JMenuItem selectivePauseMenuItem;
    private JCheckBoxMenuItem onlyCommandCheckBoxMenuItem;
    private JCheckBoxMenuItem magnifierCheckBoxMenuItem;
    private JMenuItem askIdMenuItem;
    private Field field;


    public MainFrame(){
        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
// Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
// Установить начальное состояние окна развернутым на весь экран
        setExtendedState(MAXIMIZED_BOTH);

        field = new Field(this);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu figuresMenu = new JMenu("Фигуры");
        Action addFigureAction = new AbstractAction("Добавить фигуру") {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.addFigure();

                pauseMenuItem.setEnabled(true);
                if(!field.isSelectivePaused()) selectivePauseMenuItem.setEnabled(true);
                onlyCommandCheckBoxMenuItem.setEnabled(true);
                askIdMenuItem.setEnabled(true);
            }
        };

        menuBar.add(figuresMenu);
        figuresMenu.add(addFigureAction);
        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Приостановить движение"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                selectivePauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);

        Action selectiveAction = new AbstractAction("Приостановить выборочно"){
            public void actionPerformed(ActionEvent event) {
                field.selectivePause();
                pauseMenuItem.setEnabled(true);
                selectivePauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        selectivePauseMenuItem = controlMenu.add(selectiveAction);
        selectivePauseMenuItem.setEnabled(false);

        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                selectivePauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        onlyCommandCheckBoxMenuItem = new JCheckBoxMenuItem("Только команда A", false);
        controlMenu.addSeparator();
        controlMenu.add(onlyCommandCheckBoxMenuItem);
        onlyCommandCheckBoxMenuItem.setEnabled(false);

        JTextField text = new JTextField("", 10);
        Action askIdAction = new AbstractAction("Задать идентификатор команды") {
            public void actionPerformed(ActionEvent event) {
                JOptionPane pane = new JOptionPane("Задать новый идентификатор команды", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);
                pane.setWantsInput(true);
                pane.setInputValue(field.getId());
                pane.createDialog("Идентификатор").setVisible(true);
                field.resume();
                field.setId(((String) pane.getInputValue()).isEmpty() ? Field.DEFAULT_ID : (String) pane.getInputValue());
                onlyCommandCheckBoxMenuItem.setText("Только команда " + field.getId());
                int countWaiting = 0;
                for (int i = 0; i < field.getFigures().size(); i++) if(field.getFigures().get(i).isWaiting()) countWaiting++;
                if(countWaiting == field.getFigures().size()) {
                    resumeMenuItem.setEnabled(true);
                    selectivePauseMenuItem.setEnabled(false);
                    pauseMenuItem.setEnabled(false);
                }else if(countWaiting == 0){
                    resumeMenuItem.setEnabled(false);
                    selectivePauseMenuItem.setEnabled(true);
                    pauseMenuItem.setEnabled(true);
                } else {
                    resumeMenuItem.setEnabled(false);
                    selectivePauseMenuItem.setEnabled(true);
                    pauseMenuItem.setEnabled(true);
                }
            }
        };

        askIdMenuItem = controlMenu.add(askIdAction);
        askIdMenuItem.setEnabled(false);

        controlMenu.addSeparator();
        magnifierCheckBoxMenuItem = new JCheckBoxMenuItem("Лупа", false);
        controlMenu.add(magnifierCheckBoxMenuItem);

        magnifierCheckBoxMenuItem.addActionListener(e->{
            field.setMagnifier(!field.isMagnifier());
        });


        ComponentListener componentListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(magnifierCheckBoxMenuItem.isSelected()){
                    if(field.getWidth() < Magnifier.MAGNIFIER_DIAMETER || field.getHeight() < Magnifier.MAGNIFIER_DIAMETER || (field.getWidth() < Magnifier.MAGNIFIER_DIAMETER && field.getHeight() < Magnifier.MAGNIFIER_DIAMETER))
                        setMinimumSize(new Dimension(Magnifier.MAGNIFIER_DIAMETER+20, Magnifier.MAGNIFIER_DIAMETER+70));

                    if(field.getMagnifier().getEllipseMagnifier().x + Magnifier.MAGNIFIER_DIAMETER > field.getWidth()) {
                        field.getMagnifier().getEllipseMagnifier().x = field.getWidth() - Magnifier.MAGNIFIER_DIAMETER;
                     }
                    if(field.getMagnifier().getEllipseMagnifier().y + Magnifier.MAGNIFIER_DIAMETER > field.getHeight()) {
                        field.getMagnifier().getEllipseMagnifier().y = field.getHeight() - Magnifier.MAGNIFIER_DIAMETER;
                    }
                } else setMinimumSize(new Dimension(0,0));
            }
        };

        onlyCommandCheckBoxMenuItem.addActionListener(e->{
            field.setId(!field.isId());
            if(onlyCommandCheckBoxMenuItem.isSelected()) {
                field.resume();
                //getContentPane().getComponentListeners()[0].componentResized(new ComponentEvent(this, 1));
                field.setOnlyCommand(true);
                pauseMenuItem.setEnabled(false);
                selectivePauseMenuItem.setEnabled(false);

                int countWaiting = 0;
                for (int i = 0; i < field.getFigures().size(); i++) if(field.getFigures().get(i).isWaiting()) countWaiting++;
                if(countWaiting == field.getFigures().size()) {
                    resumeMenuItem.setEnabled(true);
                    selectivePauseMenuItem.setEnabled(false);
                    pauseMenuItem.setEnabled(false);
                }else if(countWaiting == 0){
                    resumeMenuItem.setEnabled(false);
                    selectivePauseMenuItem.setEnabled(true);
                    pauseMenuItem.setEnabled(true);
                } else {
                    resumeMenuItem.setEnabled(true);
                    selectivePauseMenuItem.setEnabled(true);
                    pauseMenuItem.setEnabled(true);
                }
            } else {
                field.resume();
                field.setOnlyCommand(false);

                pauseMenuItem.setEnabled(true);
                selectivePauseMenuItem.setEnabled(true);
            }

        });

        getContentPane().add(field, BorderLayout.CENTER);


        field.addMouseMotionListener(new MagnifierMouseListener(field));

        getContentPane().addComponentListener(componentListener);

    }


    public static void main(String[] args) {
        //MainFrame frame = new MainFrame();
        FrameForFunctions frame = new FrameForFunctions();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

       /* ArrayList<Double> X = new ArrayList<>();
        ArrayList<Double> Y = new ArrayList<>();
        X.add(100.0);
        X.add(200.0);
        X.add(400.0);
        X.add(400.000000000001);
        Y.add(100.0);
        Y.add(300.0);
        Y.add(400.0);
        Y.add(400.000000000001);

        SplineInterpolator splineInterpolator = new SplineInterpolator(X, Y);
        splineInterpolator.print(1);*/
    }



}
