package top.kkoishi.swing;

import javax.swing.*;
import java.awt.*;

public class Preview extends JFrame {
    Image prev;

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public Preview (Image prev) throws HeadlessException {
        super("Preview");
        setSize(prev.getWidth(null), prev.getHeight(null));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        this.prev = prev;
        super.setVisible(true);
    }

    /**
     * {@inheritDoc}
     *
     * @param g
     * @since 1.7
     */
    @Override
    public void paint (Graphics g) {
        super.paint(g);
        g.drawImage(prev, prev.getWidth(null), prev.getHeight(null), this);
    }
}
