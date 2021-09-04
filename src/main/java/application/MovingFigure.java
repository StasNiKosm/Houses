package application;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.*;

public class MovingFigure implements Runnable {

    private static final int MAX_FRAMING_SQUARE_HALF_SIZE = 60;
    private static final int MIN_FRAMING_SQUARE_HALF_SIZE = 10;
    private static final int MIN_STEP_SIZE = 1;

    private Field field;

    private int framingSquareHalfSize;

    private Color color;

    private static final Stroke STROKE = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{4.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 4.0f, 2.0f,  2.0f,  2.0f,  4.0f,  2.0f}, 0.0f);
    private static final Stroke STROKE_IN_MAGNIFIER = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{8.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 8.0f, 4.0f,  4.0f,  4.0f,  8.0f,  4.0f}, 0.0f);
    private static final Font FONT = new Font("Arial", Font.BOLD, 30);
    private static final Font FONT_IN_MAGNIFIER = new Font("Arial", Font.BOLD, 60);
    private double x;
    private double y;

    private int sleepTime;
    private boolean isWaiting;

    private double shiftX;
    private double shiftY;

    private char id;

    private AffineTransform scaling = AffineTransform.getScaleInstance(2, 2);

    public MovingFigure(Field field) {
        this.field = field;
        framingSquareHalfSize = MIN_FRAMING_SQUARE_HALF_SIZE + new Double(Math.random() * (MAX_FRAMING_SQUARE_HALF_SIZE - MIN_FRAMING_SQUARE_HALF_SIZE)).intValue();
        sleepTime = 16 - new Double(Math.round(210 / framingSquareHalfSize)).intValue();
        if (sleepTime < MIN_STEP_SIZE) sleepTime = MIN_STEP_SIZE;
        double angle = Math.random() * 2 * Math.PI;
        shiftX = 3 * Math.cos(angle);
        shiftY = 3 * Math.sin(angle);
        color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        x = framingSquareHalfSize + Math.random() * (field.getSize().getWidth() - 2 * framingSquareHalfSize);
        y = framingSquareHalfSize + Math.random() * (field.getSize().getHeight() - 2 * framingSquareHalfSize);
        id = (char) ((int) (Math.random() * ('F' - 'A') + 'A'));
        System.out.println(id);
        Thread thisThread = new Thread(this);
        thisThread.start();
    }


    @Override
    public void run() {
        try {
            while (true) {
                field.canMove(this);
                if (x + shiftX <= framingSquareHalfSize) {
                    // Достигли левой стенки, отскакиваем право
                    shiftX = -shiftX;
                    x = framingSquareHalfSize;
                } else if (x + shiftX >= field.getWidth() -
                        framingSquareHalfSize) {
                    // Достигли правой стенки, отскок влево
                    shiftX = -shiftX;
                    x = new Double(field.getWidth() -
                            framingSquareHalfSize).intValue();
                } else if (y + shiftY <= framingSquareHalfSize) {
                    // Достигли верхней стенки
                    shiftY = -shiftY;
                    y = framingSquareHalfSize;
                } else if (y + shiftY >= field.getHeight() -
                        framingSquareHalfSize) {
                    // Достигли нижней стенки
                    shiftY = -shiftY;
                    y = new Double(field.getHeight() -
                            framingSquareHalfSize).intValue();
                } else {
                    if(!isWaiting) {
                        x += shiftX;
                        y += shiftY;
                    }
                }
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {
        }
    }

    public void paint(Graphics2D canvas) {
        GeneralPath figure = new GeneralPath();
        figure.moveTo(x - framingSquareHalfSize, y + framingSquareHalfSize);
        figure.lineTo(x - framingSquareHalfSize, y);
        figure.lineTo(x, y - framingSquareHalfSize);
        figure.lineTo(x + framingSquareHalfSize, y);
        figure.lineTo(x + framingSquareHalfSize, y);
        figure.lineTo(x + framingSquareHalfSize, y + framingSquareHalfSize);
        figure.closePath();
        if (!field.isMagnifier()) {
            canvas.setColor(color);
            canvas.fill(figure);
            canvas.setStroke(STROKE);
            canvas.setPaint(Color.black);
            canvas.draw(figure);
            canvas.setFont(new Font("Arial", Font.BOLD, 30));
            canvas.drawString("" + id, (float) (x + framingSquareHalfSize + 2), (float) y);
        } else {

            double xCenterMagnifier = field.getMagnifier().getEllipseMagnifier().getCenterX();
            double yCenterMagnifier = field.getMagnifier().getEllipseMagnifier().getCenterY();

            GeneralPath magnifier = new GeneralPath();
            magnifier.moveTo(field.getMagnifier().getEllipseMagnifier().getCenterX() + (Magnifier.MAGNIFIER_DIAMETER >> 1), yCenterMagnifier);
            for (double i = 0.0; i < 2 * Math.PI; i += 0.2)
                magnifier.lineTo(xCenterMagnifier + (Magnifier.MAGNIFIER_DIAMETER >> 1) * Math.cos(i), yCenterMagnifier + (Magnifier.MAGNIFIER_DIAMETER >> 1) * Math.sin(i));
            magnifier.closePath();

            Area area = new Area(figure);
            area.subtract(new Area(magnifier));
            canvas.setColor(color);
            canvas.fill(area);

            Area areaStroke = new Area(STROKE.createStrokedShape(figure));
            areaStroke.subtract(new Area(magnifier));
            canvas.setPaint(Color.black);
            canvas.fill(areaStroke);

            TextLayout textTl = new TextLayout(""+id, FONT, canvas.getFontRenderContext());
            Shape outline = textTl.getOutline(AffineTransform.getTranslateInstance(x + framingSquareHalfSize + 2, y));
            Area textArea = new Area(outline);
            textArea.subtract(new Area(magnifier));
            canvas.fill(textArea);

            canvas.translate(-xCenterMagnifier, -yCenterMagnifier);

            Area area1 = new Area(magnifier);
            area1.transform(AffineTransform.getTranslateInstance(xCenterMagnifier, yCenterMagnifier));
            Area area2 = new Area(figure.createTransformedShape(scaling));
            area1.intersect(area2);
            canvas.setColor(color);
            canvas.fill(area1);

            Area magnifier2 = new Area(magnifier);
            magnifier2.transform(AffineTransform.getTranslateInstance(xCenterMagnifier, yCenterMagnifier));
            Area areaStroke2 = new Area(STROKE.createStrokedShape(figure));
            areaStroke2.transform(scaling);
            magnifier2.intersect(areaStroke2);
            canvas.setColor(Color.black);
            canvas.fill(magnifier2);

            Area magnifier3 = new Area(magnifier);
            magnifier3.transform(AffineTransform.getTranslateInstance(xCenterMagnifier, yCenterMagnifier));
            TextLayout textT2 = new TextLayout(""+id, FONT, canvas.getFontRenderContext());
            Shape outline2 = textTl.getOutline(AffineTransform.getTranslateInstance(x + framingSquareHalfSize + 2, y));
            Area outLineArea = new Area(outline2);
            outLineArea.transform(scaling);
            magnifier3.intersect(outLineArea);
            canvas.setColor(Color.black);
            canvas.fill(magnifier3);

            canvas.translate(xCenterMagnifier, yCenterMagnifier);
        }
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public char getId() {
        return id;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public Color getColor() {
        return color;
    }
}
