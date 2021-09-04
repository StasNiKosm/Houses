package application;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
public class MagnifierMouseListener implements MouseMotionListener, MouseListener {

    private Field field;

    public MagnifierMouseListener(Field field) {
        this.field = field;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
       // if (field.getMagnifier().getEllipseMagnifier().getFrame().contains(e.getX(), e.getY())) {
            if (e.getX() > Magnifier.MAGNIFIER_DIAMETER / 2 && e.getX() < field.getWidth() - Magnifier.MAGNIFIER_DIAMETER / 2)
                field.getMagnifier().getEllipseMagnifier().x = e.getX() - (Magnifier.MAGNIFIER_DIAMETER >> 1);
            if (e.getY() > Magnifier.MAGNIFIER_DIAMETER / 2 && e.getY() < field.getHeight() - Magnifier.MAGNIFIER_DIAMETER / 2)
                field.getMagnifier().getEllipseMagnifier().y = e.getY() - (Magnifier.MAGNIFIER_DIAMETER >> 1);
       // }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
