package top.kkoishi.swing;

import top.kkoishi.concurrent.DefaultThreadFactory;

import javax.swing.*;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author KKoishi_
 */
public class Log extends JFrame {
    static final File LOG_DIR = new File("./Log");
    static File[] logs;
    static JTextArea area = new JTextArea();
    static JScrollPane jsp = new JScrollPane(area);
    static JMenuBar menu = new JMenuBar();
    static JMenuItem delete = new JMenuItem("Delete");
    static JMenuItem open = new JMenuItem("Open");
    static JMenuItem exit = new JMenuItem("Exit");
    static HashMap<Integer, File> fileMap = new HashMap<>();
    static String name = "";

    /**
     * Const pool.
     */
    static final int INITIAL_WIDTH = 500;
    static final int INITIAL_HEIGHT = 400;
    static final double INITIAL_DEF = 0.8;
    static final double INITIAL_DELTA = 0.01;

    static {
        area.setBounds(0, 0, INITIAL_WIDTH, INITIAL_HEIGHT);
        area.setLineWrap(false);
        menu.add(delete);
        menu.add(open);
        menu.add(exit);
        logs = LOG_DIR.listFiles();
        if (logs != null) {
            area.setText("Log File Amount: " + logs.length + "\nlog file name          Index" +
                    "            path\n");
            int i = 0;
            for (File log : logs) {
                fileMap.put(i, log);
                area.append(log.getName() + "    " +
                        i++ + "    " +
                        log.getAbsolutePath() + "\n");
            }
            System.out.println(logs[0].getName());
        } else {
            area.append("No log files.");
        }
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed (MouseEvent e) {
                super.mousePressed(e);
                new Option("Delete");
            }
        });
        open.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                super.mouseClicked(e);
                new Option("Open");
            }
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
    public Log () throws HeadlessException {
        super("Log Seeker");
        setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        setResizable(true);
        setLocationByPlatform(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setJMenuBar(menu);
        add(jsp);
        setVisible(true);
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1,
                new DefaultThreadFactory());
        pool.scheduleAtFixedRate(new Flush(), 0, 10, TimeUnit.MILLISECONDS);
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                super.mouseClicked(e);
                pool.shutdown();
                dispose();
            }
        });
    }

    static class Delete extends Thread {
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
            while (true) {
                if (fileMap.containsKey(name)) {
                    fileMap.remove(name).deleteOnExit();
                    break;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.interrupt();
        }
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
                area.setSize(width, height);
            }
            if (((double) height / (double) width) - INITIAL_DEF >= INITIAL_DELTA) {
                height = width * 4 / 5;
                setSize(width, height);
                area.setSize(width, height);
            }
        }
    }

    static class Open extends Thread {
        /**
         * If this thread was constructed using a separate
         * {@code Runnable} run object, then that
         * {@code Runnable} object's {@code run} method is called;
         * otherwise, this method does nothing and returns.
         * <p>
         * Subclasses of {@code Thread} should override this method.
         *
         * @see #start()
         */
        @Override
        public void run () {
            while (true) {
                if (fileMap.containsKey(name)) {
                    new ViewLog(fileMap.get(name));
                    break;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.interrupt();
        }
    }

    static class Option extends JFrame {
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
        public Option (String title) throws HeadlessException {
            super(title);
            this.setResizable(false);
            this.setSize(400, 200);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            JPanel jp = new JPanel(null);
            JLabel label = new JLabel("FileIndex");
            jp.add(label);
            JTextField field = new JTextField();
            jp.add(field);
            JButton confirm = new JButton("Confirm");
            jp.add(confirm);
            label.setBounds(0, 10, 100, 30);
            field.setBounds(100, 10, 250, 30);
            confirm.setBounds(100, 80, 200, 50);
            this.add(jp);
            this.setVisible(true);
            confirm.addActionListener(e -> {
                name = field.getText();
                try {
                    int index = Integer.parseInt(name);
                    if (title.contains("Open")) {
                        if (fileMap.containsKey(index)) {
                            new ViewLog(fileMap.get(index));
                        } else {
                            System.out.println(false);
                        }
                    } else {
                        if (fileMap.containsKey(index)) {
                            fileMap.remove(index).deleteOnExit();
                            JOptionPane.showMessageDialog(null,
                                    "the log file has been deleted.");
                        } else {
                            System.out.println(false);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}
