import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FlowRender extends JFrame {
    private BufferedImage img;

    public void paint(Graphics2D g) {
        g.drawImage(img, 0, 0, this);
    }

    public FlowRender(String title) throws HeadlessException {
        this(title, 600, 600);
    }

    public FlowRender(String title, int width, int height) {
        super(title);

    }
}
