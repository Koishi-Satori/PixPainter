package top.kkoishi.swing;

import top.kkoishi.io.Files;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

public class ViewLog extends JFrame {
    static File LOG;
    static JTextArea area = new JTextArea();
    static JScrollPane jsp = new JScrollPane(area);

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
    public ViewLog (File pointer) throws HeadlessException {
        LOG = pointer;
        area.setLineWrap(false);
        try {
            area.setText(Files.build().read(LOG));
        } catch (IOException e) {
            area.setText(e.getLocalizedMessage());
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setSize(500, 500);
        add(jsp);
        setVisible(true);
    }
}
