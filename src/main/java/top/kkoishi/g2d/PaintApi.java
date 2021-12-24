package top.kkoishi.g2d;

import top.kkoishi.CompilerSyntaxErrorException;
import top.kkoishi.PixPainter;
import top.kkoishi.io.Files;
import top.kkoishi.log.LogType;
import top.kkoishi.log.Logger;
import top.kkoishi.swing.Info;
import top.kkoishi.swing.Log;
import top.kkoishi.swing.Paint;
import top.kkoishi.swing.Preview;
import top.kkoishi.swing.Settings;
import top.kkoishi.util.LinkedList;
import top.kkoishi.util.Vector;
import top.kkoishi.util.function.Function;
import top.kkoishi.util.function.Range;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.acos;
import static java.lang.StrictMath.asin;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.cbrt;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.exp;
import static java.lang.StrictMath.log;
import static java.lang.StrictMath.log10;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static java.lang.StrictMath.tan;
import static java.lang.System.gc;
import static java.lang.System.out;
import static top.kkoishi.PixPainter.commands;

/**
 * Command explainer application interface.
 * <br>
 * Invoke example(already static import {@code java.lang.System.*}):
 * <blockquote>
 *
 * <p>PaintApi examplePaintApi = new PaintApi("./g2D");</p>
 * <p>examplePaintApi.execute("create   100 100         test");</p>
 * <p>out.println(examplePaintApi.getCall());</p>
 * </blockquote>
 *
 * @author KKoishi_
 * @apiNote fuck world
 * @see ScriptExplainer#execute()
 * @see Paint
 * @since java8
 */
public class PaintApi {
    /*---------------------------------FieldStart-----------------------------------------------------*/
    /**
     * Const pool.
     */
    public static final String DRAW_EASY_FUNCTION = "drawefunc\\s+-[a-z]{2,6}\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+)?";
    public static final String DRAW_PIC_THREE_PARAM = "drawpic\\s+\\d+\\s+\\d+\\s+.+";
    public static final String DRAW_PIC_FOUR_PARAM = "drawpic\\s+\\d+\\s+\\d+(\\s+\\d+\\s+\\d+)?\\s+.+";
    public static final String DRAW_CRUEL_REGEX = "drawcurel\\s+\\d+\\s+\\d+\\s+.+";
    public static final String DRAW_POLYGON_REGEX = "drawrect\\s+\\d+(\\s+\\d+\\s+\\d+)+";
    public static final String DRAW_POLY_LINE_REGEX = "drawpline\\s+\\d+(\\s+\\d+\\s+\\d+)+";
    public static final String FILL_SIMPLE_SHAPE = "fill\\s+-[a-z]+\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+";
    public static final String DRAW_SIMPLE_SHAPE = "draw\\s+-[a-z]+\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+";
    public static final String EXIT_REGEX = "exit\\s*";
    public static final String PREVIEW_REGEX = "^preview\\s*";
    public static final String DRAWSTRING_REGEX = "drawstring\\s+\\d+\\s+\\d+.+";
    public static final String CHANGEDIR_REGEX = "changedir\\s+.+";
    public static final String DRAWPOINT_REGEX = "drawpoint\\s+\\d+\\s+\\d+";
    public static final String EXPORTFORMAT_REGEX = "exportformat\\s+.+";
    public static final String EXPORT_REGEX = "^export\\s*";
    public static final String DRAWCIRCLE_REGEX = "drawcircle\\s+\\d+\\s+\\d+\\s+\\d+";
    public static final String SETCOLOR_REGEX = "setcolor\\s+.+";
    public static final String SETFONT_REGEX = "^setfont\\s+.+\\s+.+\\s+\\d+";
    public static final String CREATE_REGEX = "^create\\s+\\d+\\s+\\d+\\s+.+";
    public static final String INFO_REGEX = "info\\s*";
    public static final String LIST_REGEX = "list\\s*";
    public static final String EXIT_S = "exit\\s*";
    public static final String SETTINGS_REGEX = "settings\\s*";
    public static final String LOGBROWSE_REGEX = "logbrowse\\s*";
    public static final String LOG_REGEX = "log\\s*";
    public static final String KSHELL_REGEX = "kshell\\s+.+";
    public static final String NODE_REGEX = "node\\s+-[a-z]{4,7}\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+)?\\s+.+";
    public static final String DIRECT_REGEX = "direct\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+)?(\\s+\\d+)?";
    public static final LinkedList<String> EASY_FUNCTION_POOL = new LinkedList<>();
    public static final String CIRCLE = "-circle";
    public static final String ELLIPSE = "-ellipse";
    public static final String RECT = "-rect";
    public static final String ALL_LOG_PATH = "./all.log";
    public static final String FILLP_REGEX = "fillp\\s+\\d+(\\s+\\d+\\s+\\d+)+";
    public static final String GET_REGEX = "get\\s*";
    public static final String COMPILE_REGEX = "gcc\\s+[^\\s]+";
    public static final String REPLACE_EXPLAIN = "gcc\\s+";

    /*
      Register functions
     */
    static {
        //function list:
        //-sin/-cos/-tan/-csc/-sec/-cot/-asin/-acos/
        //-atan/-sqrt/-cube/-exp/-log/-ln/-square
        EASY_FUNCTION_POOL.addAll(Arrays.asList(
                "-sin",
                "-cos",
                "-tan",
                "-csc",
                "-sec",
                "-cot",
                "-asin",
                "-acos",
                "-atan",
                "-sqrt",
                "-cube",
                "-exp",
                "-log",
                "-ln",
                "-square"
        ));
    }

    File dir;
    File file = null;
    String call;
    BufferedImage image = null;
    Graphics2D g2D = null;
    String exportType;
    String name = "";
    int height;

    /*-----------------------------------------FieldEnd----------------------------------------------*/


    /**
     * Build a Paint Api for execute command or script.
     *
     * @param path the workspace
     */
    public PaintApi (String path) {
        dir = new File(path);
        exportType = "png";
        height = 0;
    }

    private void analysis (String command) {
        // if execute a script
        if (command.matches(KSHELL_REGEX)) {
            long t = System.currentTimeMillis();
            String filePath = command.replaceFirst("kshell\\s+", "");
            if (filePath.matches(".+((\\.ks$)|(\\.k[sS]cript$))")) {
                ScriptExplainer explainer = new ScriptExplainer(new File(filePath));
                try {
                    explainer.read();
                    explainer.execute();
                    StringBuilder sb = new StringBuilder();
                    for (String echo : explainer.getEcho()) {
                        sb.append(echo);
                    }
                    call = sb.toString();
                    sb = null;
                    call += "\nPixPainter>KShell is executed in " +
                            (System.currentTimeMillis() - t) + "ms\nPixPainter>";
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //help gc
                    filePath = null;
                    t = 0L;
                    explainer.clear();
                }
            } else {
                call = "Illegal file format!";
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EXCEPTION, call);
                call += "\nPixPainter>";
            }
            return;
        }
        //if check legal
        else if (command.matches("check\\s+.+")) {
            String path = command.replaceFirst("^check\\s+", "");
            out.println(path);
            if (path.matches("[^.]+((\\.ks$)|(\\.k[sS]cript$))")) {
                try {
                    LinkedList<Integer> cheeky = syntaxCheck(Files.DefaultFiles.access.read(path));
                    if (cheeky.isEmpty()) {
                        call = "Syntax pass.";
                    } else {
                        call = "There are " + cheeky.size() + "errors:\n";
                        StringBuilder sb = new StringBuilder();
                        for (Integer integer : cheeky) {
                            sb.append("Syntax error at the line ").append(integer).append("\n");
                        }
                        call += sb.toString();
                    }
                    call += "\nPixPainter>";
                } catch (IOException e) {
                    Logger.Builder.set("./Log");
                    Logger logger = Logger.Builder.build();
                    logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                    call = e.getLocalizedMessage() + "\nPixPainter>";
                }
            } else {
                call = "Error Type!";
                call += "\nPixPainter>";
            }
            return;
        }
        //if log operate
        else if (command.matches(LOG_REGEX)) {
            try {
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EVENT, "Invoke log func");
                new Log();
                call = "Load Log success";
            } catch (ExceptionInInitializerError e) {
                call = "A error occurs, the log dir is empty:\n" + e.getLocalizedMessage();
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.ERROR, e.getLocalizedMessage());
                call = e.getLocalizedMessage() + "\nPixPainter>";
            }
            call += "\nPixPainter>";
            return;
        }
        //if browse log
        else if (command.matches(LOGBROWSE_REGEX)) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File("./Log"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            call = "";
            return;
        }
        //if invoke settings
        else if (command.matches(SETTINGS_REGEX)) {
            new Settings();
            return;
        }
        //if exit
        else if (command.matches(EXIT_S)) {
            System.exit(514);
        }
        //if list commands
        else if (command.matches(LIST_REGEX)) {
            StringBuilder zsb = new StringBuilder();
            for (String c : commands) {
                zsb.append("\t").append(c).append("\n");
            }
            call = "Showing command list,you can use \"help [command]\" to get enhanced info" + zsb;
            call += "\nPixPainter>";
            //help hc
            zsb = null;
            return;
        }
        //if show info
        else if (command.matches(INFO_REGEX)) {
            new Info();
            call = "Opening info frame...\nPaintApi>";
            return;
        }
        //if explain a graph file
        else if (command.matches(COMPILE_REGEX)) {
            try {
                String path = command.replaceFirst(
                        REPLACE_EXPLAIN, "");
                if (path.matches(".+k[gG]raph$")) {
                    GraphCompiler explainer = new GraphCompiler(new File(path));
                    explainer.read();
                    try {
                        explainer.compile();
                        String explainerPath = explainer.getPath();
                        explainer.clear();
                        call += "Compile the graph file to a script file[" + explainerPath + "]";
                    } catch (CompilerSyntaxErrorException e) {
                        explainer.clear();
                        call = e.getLocalizedMessage();
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.ERROR, call);
                    }
                } else {
                    call = "Illegal File Format";
                    Logger.Builder.set("./Log");
                    Logger logger = Logger.Builder.build();
                    logger.log(LogType.EXCEPTION, call);
                }
                call += "\nPixPainter>";
            } catch (IOException e) {
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                call = e.getLocalizedMessage() + "\nPixPainter>";
            }
            return;
        }
        //if create image
        if (command.matches(CREATE_REGEX)) {
            String[] strings = command.split("\\s+");
            try {
                init(strings[3], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                call = "Create new Image file!Location:" + file.getAbsolutePath();
                Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                        "top.kkoishi.log.Event:" + call + "\n");
                call += "\nPixPainter>";
            } catch (NumberFormatException e) {
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                call = e.getLocalizedMessage() + "\nPixPainter>";
            } catch (IOException e) {
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
            } finally {
                //help gc
                strings = null;
            }
        } else {
            if (g2D == null) {
                Logger.Builder.set("./Log");
                Logger logger = Logger.Builder.build();
                logger.log(LogType.EXCEPTION, "You have not create a image:\n" +
                        new NullPointerException().getStackTrace()[0]);
                call = "You have not create a image:\n" +
                        new NullPointerException().getStackTrace()[0] + "\nPixPainter>";
                out.println(call);
            } else {
                //TODO:finish echo event
                //if set font
                if (command.matches(SETFONT_REGEX)) {
                    String[] strings = command.split("\\s+");
                    try {
                        setFont(new Font(("null".equals(strings[1]) ? "" : strings[1]),
                                ("bold".equals(strings[2]) ?
                                        Font.BOLD : "plain".equals(strings[2]) ?
                                        Font.PLAIN : Font.ITALIC),
                                Integer.parseInt(strings[3])));
                        call = "Success to set font";
                        Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                getTime() + "top.kkoishi.log.Event:" + call + "\n");
                        call += "\nPixPainter>";
                        out.println(call);
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage() + "\nPixPainter>";
                    } catch (IOException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                    } finally {
                        //help gc
                        strings = null;
                    }
                }
                //if set color
                else if (command.matches(SETCOLOR_REGEX)) {
                    String[] strings = command.split("\\s+");
                    if (Color.colors.containsKey(strings[1])) {
                        g2D.setColor(Color.colors.get(strings[1]));
                        call = "Success to set color to" + Color.colors.get(strings[1]);
                        try {
                            Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                    getTime() + "top.kkoishi.log.Event:" + call + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        call += "\nPixPainter>";
                    } else {
                        call = "Error color type.\n";
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.ERROR, call);
                        call += "You can key in \"colors\" to check the supported colors.\nPixPainter>";
                    }
                    //help gc
                    strings = null;
                }
                //if draw circle
                else if (command.matches(DRAWCIRCLE_REGEX)) {
                    String[] strings = command.split("\\s+");
                    try {
                        int x = Integer.parseInt(strings[1]);
                        int y = Integer.parseInt(strings[2]);
                        int r = Integer.parseInt(strings[3]);
                        Shape shape = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
                        draw(shape);
                        call = "Draw circle(radix = " + r + ") at (" + x + "," + y + ")";
                        try {
                            Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                    getTime() + "top.kkoishi.log.Event:" + call + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        call += "\nPixPainter>";
                        //help gc
                        x = y = r = 0;
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage() + "\nPixPainter>";
                    } finally {
                        //help gc,too
                        strings = null;
                    }
                }
                // if export the picture
                else if (command.matches(EXPORT_REGEX)) {
                    out.println("export executed");
                    call = "Export success!The draw board is empty now.";
                    try {
                        Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                getTime() + "top.kkoishi.log.Event:" + call + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    call += "\nPixPainter>";
                    close();
                }
                // if change export type
                else if (command.matches(EXPORTFORMAT_REGEX)) {
                    String param = command.split("\\s+")[1];
                    if ("jpg".equals(param)) {
                        exportType = "jpg";
                        call = "The export file type has been set to \".jpg\"";
                    } else if ("jepg".equals(param)) {
                        exportType = "jepg";
                        call = "The export file type has been set to \".jepg\"";
                    } else if ("png".equals(param)) {
                        exportType = "png";
                        call = "The export file type has been set to \"png\"";
                    } else {
                        exportType = "png";
                        call = "The export file type is not supported,\nand the type has been set to the default one(.png)";
                    }
                    file = new File(dir.getAbsolutePath() + "/"
                            + name + "." + exportType);
                    Logger.Builder.set("./Log");
                    Logger logger = Logger.Builder.build();
                    logger.log(LogType.EVENT, call);
                    call += "\nPixPainter>";
                    //help gc
                    param = null;
                }
                // if draw point
                else if (command.matches(DRAWPOINT_REGEX)) {
                    try {
                        int x = Integer.parseInt(command.split("\\s+")[1]);
                        int y = Integer.parseInt(command.split("\\s+")[2]);
                        drawPoint(x, y);
                        call = "Draw Point at (" + x + "," + y + ")";
                        try {
                            Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                    getTime() + "top.kkoishi.log.Event:" + call + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        call += "\nPixPainter>";
                        //help gc
                        x = y = 0;
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage() + "\nPixPainter>";
                    }
                }
                // if change workspace
                else if (command.matches(CHANGEDIR_REGEX)) {
                    String path = command.split("\\s+")[1];
                    dir = new File(path);
                    file = new File(dir.getAbsolutePath() + "/"
                            + name + "." + exportType);
                    call = "The workspace has changed to " + path;
                    try {
                        Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                getTime() + "top.kkoishi.log.Event:" + call + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    call += "\nPixPainter>";
                }
                // if draw string
                else if (command.matches(DRAWSTRING_REGEX)) {
                    try {
                        int x = Integer.parseInt(command.split("\\s+")[1]);
                        int y = Integer.parseInt(command.split("\\s+")[2]);
                        String str = command.split("\\s+")[3];
                        g2D.drawString(str, x - g2D.getFont().getSize() * str.length() / 4, y);
                        call = "Draw the string \"" + str + "\"";
                        try {
                            Files.DefaultFiles.access.append(ALL_LOG_PATH,
                                    getTime() + "top.kkoishi.log.Event:" + call + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        call += "\nPixPainter>";
                        //help gc
                        str = null;
                        x = y = 0;
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage() + "\nPixPainter>";
                    }
                }
                //if preview
                else if (command.matches(PREVIEW_REGEX)) {
                    preview();
                    call = "Preview the picture";
                    call += "\nPixPainter>";
                }
                //if exit
                else if (command.matches(EXIT_REGEX)) {
                    gc();
                    System.exit(514);
                }
                //if enhanced drawing
                else if (command.matches(DRAW_SIMPLE_SHAPE)) {
                    String[] params = command.split("\\s+");
                    try {
                        int x1 = Integer.parseInt(params[2]);
                        int y1 = Integer.parseInt(params[3]);
                        int x2 = Integer.parseInt(params[4]);
                        int y2 = Integer.parseInt(params[5]);
                        switch (params[1]) {
                            case ELLIPSE: {
                                g2D.draw(new Ellipse2D.Double(x1, y1, abs(x1 - x2), abs(y1 - y2)));
                                call = "Draw a circle with the color " + g2D.getColor();
                                break;
                            }
                            case CIRCLE: {
                                g2D.draw(new Ellipse2D.Double(x1, y1, abs(x1 - x2), abs(y1 - y2)));
                                call = "Draw a ellipse with the color " + g2D.getColor();
                                break;
                            }
                            case "-line": {
                                g2D.drawLine(x1, y1, x2, y2);
                                call = "Draw a line with the color " + g2D.getColor();
                                break;
                            }
                            case RECT: {
                                g2D.drawRect(x1, y1, abs(x1 - x2), abs(y1 - y2));
                                call = "Draw a rectangle with the color " + g2D.getColor();
                                break;
                            }
                            default:
                                call = "Error draw type,please invoke \"help -draw\" to learn more.";
                                break;
                        }
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EVENT, call);
                        call += "\nPixPainter>";
                        x1 = x2 = y1 = y2 = 0;
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage() + "\nPixPainter>";
                    } finally {
                        //help gc
                        params = null;
                    }
                }
                //if enhanced fill an area
                else if (command.matches(FILL_SIMPLE_SHAPE)) {
                    String[] params = command.split("\\s+");
                    try {
                        int x1 = Integer.parseInt(params[2]);
                        int y1 = Integer.parseInt(params[3]);
                        int x2 = Integer.parseInt(params[4]);
                        int y2 = Integer.parseInt(params[5]);
                        switch (params[1]) {
                            case ELLIPSE: {
                                g2D.fill(new Ellipse2D.Double(x1, y1, abs(x1 - x2), abs(y1 - y2)));
                                call = "fill a circle area with the color " + g2D.getColor();
                                break;
                            }
                            case CIRCLE:
                                g2D.fill(new Ellipse2D.Double(x1, y1, abs(x1 - x2), abs(y1 - y2)));
                                call = "fill a ellipse area with the color " + g2D.getColor();
                                break;
                            case RECT:
                                g2D.fillRect(x1, y1, abs(x1 - x2), abs(y1 - y2));
                                call = "fill a rectangle area with the color " + g2D.getColor();
                                break;
                            default:
                                call = "Error draw type,please invoke \"help -fill\" to learn more.";
                                break;
                        }
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EVENT, call);
                        x1 = x2 = y1 = y2 = 0;
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage();
                    } finally {
                        call += "\nPixPainter>";
                        //help gc
                        params = null;
                    }
                }
                //if draw a polyline
                else if (command.matches(DRAW_POLY_LINE_REGEX)) {
                    String[] params = command.split("\\s+");
                    try {
                        int amount = Integer.parseInt(params[1]);
                        int[] xs = new int[amount];
                        int[] ys = new int[amount];
                        if ((params.length) % 2 != 0) {
                            //help gc.
                            amount = 0;
                            xs = null;
                            ys = null;
                            call = "The points param amount can not be a even number!" +
                                    new IllegalArgumentException().getStackTrace()[0];
                        } else {
                            for (int i = 0; i < amount; i++) {
                                xs[i] = Integer.parseInt(params[i * 2 + 2]);
                                ys[i] = Integer.parseInt(params[i * 2 + 3]);
                            }
                            g2D.drawPolyline(xs, ys, amount);
                            call = "Draw a polyline with " + amount + "points";
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EVENT, call);
                            //help gc.
                            amount = 0;
                            xs = null;
                            ys = null;
                        }
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage();
                    } finally {
                        call += "\nPixPainter>";
                        //help gc
                        params = null;
                    }
                }
                //if draw polygon
                else if (command.matches(DRAW_POLYGON_REGEX)) {
                    try {
                        String[] params = command.split("\\s+");
                        int amount = Integer.parseInt(params[1]);
                        int[] xs = new int[amount];
                        int[] ys = new int[amount];
                        if ((params.length) % 2 != 0) {
                            //help gc.
                            amount = 0;
                            xs = null;
                            ys = null;
                            call = "The points param amount can not be a even number!" +
                                    new IllegalArgumentException().getStackTrace()[0];
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EXCEPTION, call);
                        } else {
                            for (int i = 0; i < amount; i++) {
                                xs[i] = Integer.parseInt(params[i * 2 + 2]);
                                ys[i] = Integer.parseInt(params[i * 2 + 3]);
                            }
                            g2D.drawPolygon(xs, ys, amount);
                            call = "Draw a polygon with " + amount + "points";
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EVENT, call);
                            //help gc.
                            amount = 0;
                            xs = null;
                            ys = null;
                        }
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage();
                    } finally {
                        call += "\nPixPainter>";
                    }
                }
                //if draw simple function
                else if (command.matches(DRAW_CRUEL_REGEX)) {
                    String[] params = command.split("\\s+");
                    try {
                        Function function = new Function.Simple(new Range(Double.parseDouble(params[1]),
                                Double.parseDouble(params[2])), params[3]);
                        for (Point2D point : function.getPoints()) {
                            drawPoint((int) point.getX(), (int) point.getY());
                        }
                        call = "draw a cruel!";
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EVENT, call);
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage();
                    } finally {
                        call += "\nPixPainter>";
                    }
                }
                //if fill polygon
                else if (command.matches(FILLP_REGEX)) {
                    try {
                        String[] params = command.split("\\s+");
                        int amount = Integer.parseInt(params[1]);
                        int[] xs = new int[amount];
                        int[] ys = new int[amount];
                        if ((params.length) % 2 != 0) {
                            //help gc.
                            amount = 0;
                            xs = null;
                            ys = null;
                            call = "The points param amount can not be a even number!" +
                                    new IllegalArgumentException().getStackTrace()[0];
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EXCEPTION, call);
                        } else {
                            try {
                                for (int i = 0; i < amount; i++) {
                                    xs[i] = Integer.parseInt(params[i * 2 + 2]);
                                    ys[i] = Integer.parseInt(params[i * 2 + 3]);
                                }
                                g2D.fillPolygon(xs, ys, amount);
                                call = "Fill a polygon area with " + amount + "points";
                            } catch (ArrayIndexOutOfBoundsException e) {
                                call = e.getLocalizedMessage();
                            }
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EVENT, call);
                            //help gc.
                            amount = 0;
                            xs = null;
                            ys = null;
                        }
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage();
                    } finally {
                        call += "\nPixPainter>";
                    }
                }
                //if draw picture
                else if (command.matches(DRAW_PIC_FOUR_PARAM)) {
                    String[] strings = command.split("\\s+");
                    int x = 0, y = 0, w = 0, h = 0;
                    String path;
                    if (command.matches(DRAW_PIC_THREE_PARAM)) {
                        try {
                            x = Integer.parseInt(strings[1]);
                            y = Integer.parseInt(strings[2]);
                            path = strings[3];
                            Image i = ImageIO.read(new File(path));
                            g2D.drawImage(i, x, y, null);
                            call = "Append a image[" + i.toString() + "].";
                        } catch (NumberFormatException | IOException e) {
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                            call = e.getLocalizedMessage();
                        } finally {
                            x = y = 0;
                            path = null;
                            call += "\nPixPainter>";
                        }
                    } else {
                        try {
                            x = Integer.parseInt(strings[1]);
                            y = Integer.parseInt(strings[2]);
                            w = Integer.parseInt(strings[3]);
                            h = Integer.parseInt(strings[4]);
                            path = strings[5];
                            Image i = ImageIO.read(new File(path));
                            g2D.drawImage(i, x, y, w, h, null);
                            call = "Append a image[" + i.toString() + "].";
                        } catch (NumberFormatException | IOException e) {
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                            call = e.getLocalizedMessage();
                        } finally {
                            x = y = w = h = 0;
                            path = null;
                            call += "\nPixPainter>";
                        }
                    }
                }
                /*
                if draw easy function
                drawefunc [-sin/-cos/-tan/-csc/-sec/-cot/-asin/-acos/
                -atan/-sqrt/-cube/-exp/-log/-ln/-square] [x1] [x2]
                 */
                else if (command.matches(DRAW_EASY_FUNCTION)) {
                    String[] params = command.split("\\s+");
                    if (EASY_FUNCTION_POOL.contains(params[1])) {
                        int x1 = Integer.parseInt(params[2]);
                        int x2 = Integer.parseInt(params[3]);
                        int dx = Integer.parseInt(params[4]);
                        if (params.length == 6) {
                            int dy = Integer.parseInt(params[5]);
                            setDrawEasyFunction(params[1], new Range(x1, x2), dx, dy);
                        } else {
                            setDrawEasyFunction(params[1], new Range(x1, x2), dx, height / 2f);
                        }
                    } else {
                        call = "Unexpected type of function!";
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EVENT, call);
                        call += "\nPixPainter";
                    }
                }
                //if draw node
                else if (command.matches(NODE_REGEX)) {
                    String[] params = command.split("\\s+");
                    //draw circle
                    if (params.length == 6 && CIRCLE.equals(params[1])) {
                        try {
                            int x = Integer.parseInt(params[2]);
                            int y = Integer.parseInt(params[3]);
                            int r = Integer.parseInt(params[4]);
                            draw(new Ellipse2D.Double(x - r, y - r, x + r, y + r));
                            g2D.drawString(params[5], x - g2D.getFont().getSize() * params[5].length() / 4, y);
                            call = "Draw a circle shape node with the string \"" + params[5] + "\"";
                            try {
                                Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                                        "top.kkoishi.log.Event:" + call + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (NumberFormatException e) {
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                            call = e.getLocalizedMessage();
                        }
                    }
                    //draw rectangle or ellipse
                    else if (params.length == 7) {
                        //when draw a rectangle
                        if (ELLIPSE.equals(params[1])) {
                            try {
                                int x1 = Integer.parseInt(params[2]);
                                int y1 = Integer.parseInt(params[3]);
                                int x2 = Integer.parseInt(params[4]);
                                int y2 = Integer.parseInt(params[5]);
                                if (x1 > x2) {
                                    int temp = x1;
                                    x1 = x2;
                                    x2 = temp;
                                }
                                if (y1 > y2) {
                                    int temp = y1;
                                    y1 = y2;
                                    y2 = temp;
                                }
                                draw(new Ellipse2D.Double(x1, y1, x2 - x1, y2 - y1));
                                g2D.drawString(params[6],
                                        (x1 + x2) / 2 - g2D.getFont().getSize() * params[6].length() / 4,
                                        (y1 + y2) / 2);
                                call = "Draw a rectangle shape node with the string \"" + params[6] + "\"";
                                try {
                                    Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                                            "top.kkoishi.log.Event:" + call + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (NumberFormatException e) {
                                Logger.Builder.set("./Log");
                                Logger logger = Logger.Builder.build();
                                logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                                call = e.getLocalizedMessage();
                            }
                        }
                        //when draw an ellipse
                        else if (RECT.equals(params[1])) {
                            try {
                                int x1 = Integer.parseInt(params[2]);
                                int y1 = Integer.parseInt(params[3]);
                                int x2 = Integer.parseInt(params[4]);
                                int y2 = Integer.parseInt(params[5]);
                                if (x1 > x2) {
                                    int temp = x1;
                                    x1 = x2;
                                    x2 = temp;
                                }
                                if (y1 > y2) {
                                    int temp = y1;
                                    y1 = y2;
                                    y2 = temp;
                                }
                                g2D.drawRect(x1, y1, x2 - x1, y2 - y1);
                                g2D.drawString(params[6],
                                        (x1 + x2) / 2 - g2D.getFont().getSize() * params[6].length() / 4,
                                        (y1 + y2) / 2);
                                call = "Draw an ellipse shape node with the string \"" + params[6] + "\"";
                                try {
                                    Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                                            "top.kkoishi.log.Event:" + call + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (NumberFormatException e) {
                                Logger.Builder.set("./Log");
                                Logger logger = Logger.Builder.build();
                                logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                                call = e.getLocalizedMessage();
                            }
                        } else {
                            call = "Error shape of node!";
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.ERROR, call);
                        }
                    } else {
                        call = "Error shape of node!";
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.ERROR, call);
                    }
                    call += "\nPixPainter>";
                }
                //if draw direct
                //direct [x1] [y1] [x2] [y2] [branch-len=20] [degree=30(unit:°)]
                else if (command.matches(DIRECT_REGEX)) {
                    try {
                        String[] params = command.split("\\s+");
                        int x1 = Integer.parseInt(params[1]);
                        int y1 = Integer.parseInt(params[2]);
                        int x2 = Integer.parseInt(params[3]);
                        int y2 = Integer.parseInt(params[4]);
                        int len = 20;
                        int degree = 30;
                        g2D.drawLine(x1, y1, x2, y2);
                        //if len&degree is empty
                        if (params.length == 5) {
                            drawDirect(x2, y2, len, degree);
                            drawDirect(x2, y2, len, -1 * degree);
                            call = "Draw the directed line[top.kkoishi.direct@" + Arrays.hashCode(params) + "]";
                            try {
                                Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                                        "top.kkoishi.log.Event:" + call + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //if degree is empty
                        else if (params.length == 6) {
                            len = Integer.parseInt(params[5]);
                            drawDirect(x2, y2, len, degree);
                            drawDirect(x2, y2, len, -1 * degree);
                            call = "Draw the directed line[top.kkoishi.direct@" + Arrays.hashCode(params) + "]";
                            try {
                                Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                                        "top.kkoishi.log.Event:" + call + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //if no empty part
                        else if (params.length == 7) {
                            len = Integer.parseInt(params[5]);
                            degree = Integer.parseInt(params[6]);
                            drawDirect(x2, y2, len, degree);
                            drawDirect(x2, y2, len, -1 * degree);
                            call = "Draw the directed line[top.kkoishi.direct@" + Arrays.hashCode(params) + "]";
                        } else {
                            call = "Error params";
                            Logger.Builder.set("./Log");
                            Logger logger = Logger.Builder.build();
                            logger.log(LogType.ERROR, call);
                        }
                    } catch (NumberFormatException e) {
                        Logger.Builder.set("./Log");
                        Logger logger = Logger.Builder.build();
                        logger.log(LogType.EXCEPTION, e.getLocalizedMessage());
                        call = e.getLocalizedMessage();
                    }
                    call += "\nPixPainter>";
                }
                //if check
                else if (command.matches(GET_REGEX)) {
                    call = "Current State:\n\tColor=" +
                            g2D.getColor() +
                            "\n\tFont=" +
                            g2D.getFont() +
                            "\n\tSize=top.kkoishi.Dimension[" +
                            image.getWidth() +
                            ", " +
                            image.getHeight() +
                            "\n\tLocation=" +
                            file;
                    try {
                        Files.DefaultFiles.access.append(ALL_LOG_PATH, getTime() +
                                "top.kkoishi.log.Event:" + call + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    call += "\nPixPainter>";
                } else {
                    out.println(false);
                }
            }
            //tell JVM to gc.
            gc();
        }
    }

/*
Command-list:
    create [width] [height] [name]√
    setfont [name:null] bold/plain/italic [size]√
    setcolor [color]√
    draw -circle/-line/-ellipse/-rect [x1] [y1] [x2] [y2]
    drawcircle [x] [y] [r]√
    fill circle/-ellipse/-rect [x1] [y2] [x2] [y2]
    drawpline [amount-point] [x] [y]...
    drawrect [amount-point] [x] [y]...
    fillp [amount-point] [x] [y]...
    drawpoint [x] [y]√
    exportformat jpg/png/jepg√
    drawpic [x] [y] (Optional param:[w] [h]) [path]
    drawcurel [x1] [x2] [func] -> hard
    drawefunc [-sin/-cos/-tan/-csc/-sec/-cot/-asin/-acos/-atan/-sqrt/-cube/-exp/-log/-ln/-square] [x1] [x2] [size-y] [dy=size/2]
    drawstring [x] [y] [string]
help [-[command]]
    changedir [dir]
    kshell [path]
    preview
    list
    exit
    settings
    info
    logs
    logbrowse
    export√
    node -circle [x] [y] [r] [string]
    node -rect/-ellipse [x] [y] [w] [h] [string]
    direct [x1] [y1] [x2] [y2] [branch-len=20] [degree=30°]
    get
    compile [graph file path]
 */

    final void help (String command) {

    }

    final void drawDirect (int x2, int y2, int len, int degree) {
        degree %= 360;
        g2D.drawLine(x2, y2, x2 + (int) (len * (cos(degree / 3f) - sin(degree / 3f))),
                y2 + (int) (len * (sin(degree / 3f) + cos(degree / 3f))));
    }

    final void init (String name, int width, int height) {
        file = new File(dir.getAbsolutePath() + "/"
                + name + "." + exportType);
        this.name = name;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2D = image.createGraphics();
    }

    final void setFont (Font font) {
        g2D.setFont(font);
    }

    final void draw (Shape shape) {
        g2D.draw(shape);
    }

    final void drawPoint (int x, int y) {
        g2D.drawLine(x, y, x, y);
    }

    final void close () {
        g2D.dispose();
        g2D = null;
        try {
            ImageIO.write(image, exportType, file);
            image = null;
        } catch (IOException e) {
            Logger.Builder.set("./Log");
            Logger logger = Logger.Builder.build();
            logger.log(LogType.EXCEPTION, "top.kkoishi.log.Exception:Export failed.\n"
                    + e.getMessage());
        }
    }

    final void preview () {
        File temp = new File("./~" + file.getName() + "." + exportType);
        try {
            ImageIO.write(image, exportType, temp);
            Image img = image;
            new Preview(img);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            temp.deleteOnExit();
        }
    }

    final void setDrawEasyFunction (String function, Range range, double dx, double dy) {
        switch (function) {
            case "-sin": {
                GeneralPath gp = new GeneralPath();
                gp.moveTo(0, dy);
                for (int i : range) {
                    i *= 1;
                    double y = (float) (dx * sin(i) * PI / 180) + dy;
                    gp.lineTo(i, y);
                }
                g2D.draw(gp);
                break;
            }
            case "-cos": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * cos(i)) + (int) dy);
                }
                break;
            }
            case "-tan": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * tan(i)) + (int) dy);
                }
                break;
            }
            case "-csc": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * (1 / sin(i)) + (int) dy));
                }
                break;
            }
            case "-sec": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * (1 / cos(i)) + (int) dy));
                }
                break;
            }
            case "-cot": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * (1 / tan(i)) + (int) dy));
                }
                break;
            }
            case "-asin": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * asin(i)) + (int) dy);
                }
                break;
            }
            case "-acos": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * acos(i)) + (int) dy);
                }
                break;
            }
            case "-atan": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * atan(i)) + (int) dy);
                }
                break;
            }
            case "-sqrt": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * sqrt(i)) + (int) dy);
                }
                break;
            }
            case "-cube": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * cbrt(i)) + (int) dy);
                }
                break;
            }
            case "-exp": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * exp(i)) + (int) dy);
                }
                break;
            }
            case "-log": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * log10(i)) + (int) dy);
                }
                break;
            }
            case "-ln": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * log(i)) + (int) dy);
                }
                break;
            }
            case "-square": {
                for (int i : range) {
                    drawPoint(i, (int) (dx * (i * i)) + (int) dy);
                }
                break;
            }
            default:
                break;
            //function list:
            //-sin/-cos/-tan/-csc/-sec/-cot/-asin/-acos/
            //-atan/-sqrt/-cube/-exp/-log/-ln/-square
        }
        call = "Execute and draw the function:" + function + " x";
        Logger.Builder.set("./Log");
        Logger logger = Logger.Builder.build();
        logger.log(LogType.EVENT, call);
        call += "\nPixPainter>";
    }

    /**
     * Execute a command.
     *
     * @param command command or script line.
     */
    public void execute (String command) {
        out.println(pre(command));
        analysis(pre(command));
    }

    final String pre (String command) {
        return command.replaceFirst("^[a-z-A-Z]+\\s*", command.split("\\s+")[0].toLowerCase() + " ");
    }

    final LinkedList<Integer> syntaxCheck (String commands) {
        String[] strs = commands.split(";");
        Vector<String> tokens = new Vector<>();
        tokens.addAll(Arrays.asList(strs));
        LinkedList<Integer> ans = new LinkedList<>();
        int i = 1;
        for (String token : tokens) {
            token = token.replaceAll("\n", "").replaceAll("\r", "");
            if (token.matches("\\s*")) {
                continue;
            }
            if (!PixPainter.SYNTAX_CHECK_MAP.containsKey(token.split("\\s+")[0].toLowerCase())) {
                ans.add(i);
            }
            i++;
        }
        gc();
        return ans;
    }

    /**
     * Get the feedback of execution.
     *
     * @return the execute result.
     */
    public String getCall () {
        final String temp = call;
        call = "";
        return "\nPixPainter>" + temp;
    }

    /**
     * Get time for log.
     *
     * @return time, like [2021/12/23 8:24:59]
     */
    protected String getTime () {
        Calendar calendar = Calendar.getInstance();
        return "[" + calendar.get(Calendar.YEAR) + "/"
                + calendar.get(Calendar.MONTH) + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + "]";
    }
}
