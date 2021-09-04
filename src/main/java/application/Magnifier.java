package application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Magnifier {

    public static final int MAGNIFIER_DIAMETER = 300;
    private static final String PATH_TO_TEXTURE = "C:\\Users\\User\\IdeaProjects\\lab5\\src\\main\\resources\\magnifierTexture.gif";

    private static final double MAGNIFIER_TRANSPARENCY = 0.4;

    private Ellipse2D.Double ellipseMagnifier;

    public Magnifier() {
        this.ellipseMagnifier = new Ellipse2D.Double();
        ellipseMagnifier.setFrame(0, 0, MAGNIFIER_DIAMETER, MAGNIFIER_DIAMETER);

    }

    public void paint(Graphics2D canvas){
        try {
            BufferedImage texture = ImageIO.read(new File(PATH_TO_TEXTURE));
            BufferedImage textureWithAlpha = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
            modAlpha(texture, textureWithAlpha, MAGNIFIER_TRANSPARENCY);
            canvas.setPaint(new TexturePaint(textureWithAlpha, new Rectangle2D.Float(0, 0, texture.getWidth(), texture.getHeight())));
            canvas.fill(ellipseMagnifier);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //canvas.setPaint(new Color(0.85f, 0.95f, 1.0f, 0.1f));
        canvas.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f));
        canvas.setPaint(new Color(255, 215, 0));
        canvas.draw(ellipseMagnifier);

    }

    private static void modAlpha(BufferedImage modMe, BufferedImage copyModMe, double modAmount) {
        for (int x = 0; x < modMe.getWidth(); x++)
            for (int y = 0; y < modMe.getHeight(); y++) {
                int argb = modMe.getRGB(x, y);// int copyArgb = copyModMe.getRGB(x, y);//always returns TYPE_INT_ARGB
                int alpha = (argb >> 24) & 0xff;  //isolate alpha
                alpha *= modAmount;//similar distortion to tape saturation (has scrunching effect, eliminates clipping)
                alpha &= 0xff;      //keeps alpha in 0-255 range
                argb &= 0x00ffffff; //remove old alpha info
                argb |= (alpha << 24);  //add new alpha info
                copyModMe.setRGB(x, y, argb);
            }
    }

    public Ellipse2D.Double getEllipseMagnifier() {
        return ellipseMagnifier;
    }
}
