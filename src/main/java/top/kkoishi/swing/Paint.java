package top.kkoishi.swing;

import top.kkoishi.concurrent.DefaultThreadFactory;
import top.kkoishi.g2d.PaintApi;
import top.kkoishi.io.FileChooser;

import javax.swing.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Paint extends JFrame {
    static JTextArea terminal = new JTextArea("PixPainter>");
    static JScrollPane jsp = new JScrollPane(terminal);
    static JMenuBar menu = new JMenuBar();
    static JMenu file = new JMenu("File");
    static JMenu about = new JMenu("About");
    static JMenu events = new JMenu("Event Logs");
    static JMenu build = new JMenu("Build");
    static JMenuItem open = new JMenuItem("Open");
    static JMenuItem newPix = new JMenuItem("New");
    static JMenuItem settings = new JMenuItem("Settings");
    static JMenuItem exit = new JMenuItem("Exit");
    static JMenuItem preview = new JMenuItem("Preview");
    static JMenuItem export = new JMenuItem("Export");
    static JMenuItem showOperations = new JMenuItem("Show Operations");
    static JMenuItem info = new JMenuItem("Info");
    static JMenuItem links = new JMenuItem("Links");
    static JMenuItem browseLog = new JMenuItem("Browse Event Log");
    static JMenuItem openIn = new JMenuItem("Open in File System");
    static JMenuItem execute = new JMenuItem("Execute");
    static boolean ifExecute = false;
    static String path = null;
    static PaintApi api = new PaintApi("../g2d");
    static volatile KeyEvent event;
    static String command = "";

    static final int INITIAL_WIDTH = 500;
    static final int INITIAL_HEIGHT = 400;
    static final double INITIAL_DEF = 0.8;
    static final double INITIAL_DELTA = 0.01;

    static {
        menu.add(file);
        menu.add(about);
        menu.add(events);
        menu.add(build);
        file.add(newPix);
        file.addSeparator();
        file.add(settings);
        file.addSeparator();
        file.add(exit);
        build.add(preview);
        build.addSeparator();
        build.add(export);
        build.addSeparator();
        build.add(showOperations);
        about.add(info);
        build.addSeparator();
        build.add(execute);
        about.addSeparator();
        about.add(links);
        events.add(browseLog);
        events.addSeparator();
        events.add(openIn);
        terminal.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        terminal.setLineWrap(false);
        terminal.setFont(new Font("", Font.BOLD, 12));
        execute.setAccelerator(KeyStroke.getKeyStroke("F5"));
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_DOWN_MASK));
        preview.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10 ,InputEvent.SHIFT_DOWN_MASK));
        execute.addActionListener(e -> {
            String pre = terminal.getText();
            String[] strs = pre.split("PixPainter>");
            out.println(Arrays.toString(strs));
            command = strs[strs.length - 1].replaceAll("\n", "");
            api.execute(command);
            String call = api.getCall();
            terminal.append(call);
            out.println(command);
            command = "";
        });
        preview.addActionListener(e -> {
            api.execute("preview");
            terminal.append(api.getCall());
        });
        export.addActionListener(e -> {
            api.execute("export");
            terminal.append(api.getCall());
        });
        exit.addActionListener(e -> System.exit(514));
        open.addActionListener(e -> {
            try {
                path = FileChooser.getInstance().singleDir("Choose a png file",
                        "WorkSpace");
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                    InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            if (path == null || "".equals(path)) {
                out.println("az...");
            } else {
                api = new PaintApi(path);
            }
        });
        newPix.addActionListener(e -> {
            String echo = JOptionPane.showInputDialog(null,
                    "Input width height and file name", "200 200 newPix");
            api.execute("create " + echo.split("\\s+")[0] +
                    " " + echo.split("\\s+")[1] + " " + echo.split("\\s+")[2]);
            out.println(echo);
            terminal.append(api.getCall());
        });
        browseLog.addActionListener(e -> {
            api.execute("log");
            terminal.append(api.getCall());
        });
        openIn.addActionListener(e -> {
            api.execute("logbrowse");
            terminal.append(api.getCall());
        });
        showOperations.addActionListener(e -> {
            api.execute("list");
            terminal.append(api.getCall());
        });
        info.addActionListener(e -> {
            api.execute("info");
            terminal.append(api.getCall());
        });
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
    public Paint () throws HeadlessException {
        super("Painter");
        setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setJMenuBar(menu);
        setFocusable(true);
        add(jsp);
        setVisible(true);
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(3,
                new DefaultThreadFactory());
        pool.scheduleAtFixedRate(new Flush(), 0, 10, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(new Execute(), 0, 10, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(new Seprate(), 0, 10, TimeUnit.MILLISECONDS);
        addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             *
             * @param e e
             */
            @Override
            public void keyTyped (KeyEvent e) {
                event = e;
                super.keyTyped(e);
            }
        });
    }

    final class Flush implements Runnable {
        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run () {
            int width = getWidth();
            int height = getHeight();
            if (width < INITIAL_WIDTH || height < INITIAL_HEIGHT) {
                width = INITIAL_WIDTH;
                height = INITIAL_HEIGHT;
                setSize(width, height);
                terminal.setSize(width, height);
            }
            if (((double) height / (double) width) - INITIAL_DEF >= INITIAL_DELTA) {
                height = width * 4 / 5;
                setSize(width, height);
                terminal.setSize(width, height);
            }
        }
    }

    static final class Seprate implements Runnable {
        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run () {
            String pre = terminal.getText();
            String[] strs = pre.split("PixPainter>");
            out.println(Arrays.toString(strs));
            command = strs[strs.length - 1].replaceAll("\n", "");
        }
    }

    static final class Execute implements Runnable {

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run () {
            if (ifExecute) {
                ifExecute = false;
            }
            if (event != null && !"".equals(command)) {
                if (event.getKeyCode() == KeyEvent.VK_F5) {
                    api.execute(command);
                    String call = api.getCall();
                    terminal.append(call);
                    out.println(true);
                }
            }
            if (event.getKeyCode() == KeyEvent.VK_F5) {
                out.println(event);
            }
        }
    }
}
