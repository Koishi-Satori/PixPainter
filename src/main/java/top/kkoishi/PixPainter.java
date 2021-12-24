package top.kkoishi;

import top.kkoishi.swing.Paint;
import top.kkoishi.util.Vector;

import java.util.HashMap;

/**
 * Main class.
 * @author KKoishi_
 */
public class PixPainter {
    public static Vector<String> commands;
    public static HashMap<String, String> commandInfo = new HashMap<>();
    public static final HashMap<String, String> SYNTAX_CHECK_MAP = new HashMap<>();

    //Register
    static {
        SYNTAX_CHECK_MAP.put("create", "^create\\s+\\d+\\s+\\d+\\s+.+");
        SYNTAX_CHECK_MAP.put("setfont", "^setfont\\s+.+\\s+.+\\s+\\d+");
        SYNTAX_CHECK_MAP.put("setcolor", "setcolor\\s+.+");
        SYNTAX_CHECK_MAP.put("draw", "draw\\s+-[a-z]+\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+");
        SYNTAX_CHECK_MAP.put("drawcircle", "drawcircle\\s+\\d+\\s+\\d+\\s+\\d+");
        SYNTAX_CHECK_MAP.put("fill", "drawpline\\s+\\d+(\\s+\\d+\\s+\\d+)+");
        SYNTAX_CHECK_MAP.put("drawpline", "fill\\s+-[a-z]+\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+");
        SYNTAX_CHECK_MAP.put("drawrect", "drawrect\\s+\\d+(\\s+\\d+\\s+\\d+)+");
        SYNTAX_CHECK_MAP.put("fillp", "fillp\\s+\\d+(\\s+\\d+\\s+\\d+)+");
        SYNTAX_CHECK_MAP.put("drawpoint", "drawpoint\\s+\\d+\\s+\\d+");
        SYNTAX_CHECK_MAP.put("exportformat", "exportformat\\s+.+");
        SYNTAX_CHECK_MAP.put("drawpic", "drawpic\\s+\\d+\\s+\\d+(\\s+\\d+\\s+\\d+)?\\s+.+");
        SYNTAX_CHECK_MAP.put("drawefunc", "drawefunc\\s+-[a-z]{2,6}\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+)?");
        SYNTAX_CHECK_MAP.put("drawstring", "drawstring\\s+\\d+\\s+\\d+.+");
        SYNTAX_CHECK_MAP.put("changedir", "changedir\\s+.+");
        SYNTAX_CHECK_MAP.put("kshell", "kshell\\s+.+");
        SYNTAX_CHECK_MAP.put("preview", "^preview\\s*");
        SYNTAX_CHECK_MAP.put("export", "^export\\s*");
        SYNTAX_CHECK_MAP.put("node", "node\\s+-[a-z]{4,7}\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+)?\\s+.+");
        SYNTAX_CHECK_MAP.put("direct", "direct\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+(\\s+\\d+)?(\\s+\\d+)?");
        SYNTAX_CHECK_MAP.put("get", "get\\s*");
        SYNTAX_CHECK_MAP.put("gcc", "gcc\\s+[^\\s]+");

        commands = new Vector<>();
        commands.add("create [width] [height] [name]");
        commandInfo.put("create", "Command:create\nUse:create [width] [height] [name]" +
                "\nParam:\n\t[width]The width of the image." +
                "\n\t[height]The height of the image\n\t[name]The name of the image file." +
                "\nInfo:Create a new picture with the given size and the given name.");
        commands.add("setfont [name:null] bold/plain/italic [size]");
        commandInfo.put("setfont", "Command:setfont\nUse:setfont [name:null] bold/plain/italic [size]" +
                "\nParam:\n\t[name] the font name\n\t[size]The size of the font." +
                "\nInfo:Set the font of the graph painter.When invoke it multiple times,\n" +
                "the font will be different");
        commands.add("setcolor [color]");
        commandInfo.put("setcolor", "Command:setcolor\nUse:setcolor [color]" +
                "\nParam:\n\t[color]The color.Support type:\n\t\t" +
                "white black red cyan blue lightgray gray\n\t\t" +
                "darkgray pink orange yellow green mageta\n" +
                "Info:Set the color of the graph painter.When invoke it multiple times,\\n" +
                "the font will be different");
        commands.add("draw -circle/-line/-ellipse/-rect [x1] [y1] [x2] [y2]");
        commands.add("draw -circle/-line/-ellipse/-rect [x1] [y1] [x2] [y2]");
        commands.add("drawcircle [x] [y] [r]");
        commands.add("fill circle/-ellipse/-rect [x1] [y2] [x2] [y2]");
        commands.add("drawpline [amount-point] [x] [y]...");
        commands.add("drawrect [amount-point] [x] [y]...");
        commands.add("fillp [amount-point] [x] [y]...");
        commands.add("drawpoint [x] [y]");
        commands.add("drawpic [x] [y] [path]");
        commands.add("exportformat png/jepg");
        commands.add("drawcurel [x1] [x2] [func] (not finished)");
        commands.add("drawstring [x] [y] [string]");
        commands.add("help [command]");
        commands.add("changedir [dir]");
        commands.add("kshell [path]");
        commands.add("preview");
        commands.add("list");
        commands.add("exit");
        commands.add("settings");
        commands.add("info");
        commands.add("log");
        commands.add("logbrowse");
        commands.add("export");
        commands.add("drawefunc [-sin/-cos/-tan/-csc/-sec/-cot/-asin/-acos/" +
                "-atan/-sqrt/-cube/-exp/-log/-ln/-square] \n" +
                "[x1] [x2] [size-dx] [size-dy = 2 / size]");
        commands.add("node -circle [x] [y] [r] [string]");
        commands.add("node -rect/-ellipse [x] [y] [w] [h] [string]");
        commands.add("direct [x1] [y1] [x2] [y2] [branch-len=20] [degree=30°]");
        commands.add("get");
        commands.add("gcc [graph file path]");
        commands.add("check [script path]");
    }

    public static void main (String[] args) {
        new Paint();
    }
}
