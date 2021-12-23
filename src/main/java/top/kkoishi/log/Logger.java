package top.kkoishi.log;

import top.kkoishi.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public abstract class Logger {
    protected File logDir;
    protected static Logger logger = new DefaultLogger();

    private Logger (String path) throws IOException {
        logDir = new File(path);
        if (!logDir.exists() && logDir.isDirectory()) {
            logDir.createNewFile();
        }
    }

    private Logger () {
    }

    private static class DefaultLogger extends Logger {
        protected DefaultLogger (String path) throws IOException {
            super(path);
        }

        private DefaultLogger () {
        }

        @Override
        public void log (LogType type, String content) {
            switch (type) {
                case EXCEPTION: {
                    log(getTime() + "top.kkoishi.log.Exception: " + content);
                    break;
                }
                case EVENT: {
                    log(getTime() + "top.kkoishi.log.Event: " + content);
                    break;
                }
                case ERROR: {
                    log(getTime() + "top.kkoishi.log.Error: " + content);
                    break;
                }
                default:
                    log(getTime() + "top.kkoishi.log.UnmatchedError: " + content);
            }
        }

        final void log (String content) {
            String str = getTime().replaceAll("[\\[\\]:/]", "")
                    .replaceAll(" ", "_")+ ".log";
            File f = new File(logDir.getAbsolutePath() + "/" + str);
            System.out.println(f.getAbsolutePath());
            try {
                f.createNewFile();
                Files.DefaultFiles.access.write(f, content + "\n");
                Files.DefaultFiles.access.append(new File("./all.log"), content + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void setPath (String path) {
            logDir = new File(path);
        }
    }

    public static class Builder {
        private static String path;

        private Builder () {
        }

        public static void set (String dirPath) {
            path = dirPath;
        }

        public static Logger build () {
            logger.setPath(path);
            return logger;
        }
    }

    /**
     * Log to a file.
     *
     * @param type    type of log.
     * @param content content of log.
     */
    public abstract void log (LogType type, String content);

    protected String getTime () {
        Calendar calendar = Calendar.getInstance();
        return "[" + calendar.get(Calendar.YEAR) + "/"
                + calendar.get(Calendar.MONTH) + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + "]";
    }

    protected abstract void setPath (String path);
}
