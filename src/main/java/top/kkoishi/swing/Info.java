package top.kkoishi.swing;

import javax.swing.*;
import java.awt.*;

public class Info extends JFrame {
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
    public Info () throws HeadlessException {
        super("Info");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(500, 500);
        setLocationByPlatform(true);
        JPanel jp = new JPanel();

        add(jp);
        setVisible(true);
    }
}
