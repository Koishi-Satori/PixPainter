package top.kkoishi.swing;

import top.kkoishi.io.KoishiReader;
import top.kkoishi.util.LinkedList;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Settings extends JFrame {
    static final String SETTING_PATH = "./settings/settings.koishi";
    static KoishiReader koishiReader = new KoishiReader(SETTING_PATH);
    static LinkedList<Map.Entry<String, String>> entries = new LinkedList<>();
    static {
        koishiReader.read();
        entries.addAll(koishiReader.get());
        koishiReader.clear();

    }
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
    public Settings () throws HeadlessException {
        super("Settings");
    }
}
