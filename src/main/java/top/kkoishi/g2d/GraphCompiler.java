package top.kkoishi.g2d;

import top.kkoishi.CompilerSyntaxErrorException;
import top.kkoishi.io.Files;
import top.kkoishi.util.LinkedList;

import java.io.File;
import java.io.IOException;

/**
 * Compile the graph file to a script file.
 *
 * @author KKoishi_
 */
public class GraphCompiler {
    PaintApi api;
    String workspace;
    File graph;
    LinkedList<String> tokens = new LinkedList<>();
    LinkedList<String> calls = new LinkedList<>();
    File script = null;

    public GraphCompiler (File script) {
        this.api = new PaintApi("./");
        this.graph = script;
        workspace = script.getParent() + "/";
    }

    public GraphCompiler (String dir, File script) {
        this.api = new PaintApi(dir);
        this.graph = script;
        workspace = dir;
    }

    public void read () throws IOException {
        String[] pre = Files.DefaultFiles.access.read(graph).split("\\s*;");
        for (String token : pre) {
            if (!token.matches("\\s*")) {
                tokens.add(token.
                        replaceAll("\n", "").
                        replaceAll("\r", ""));
            }
        }
    }

    public void compile () throws CompilerSyntaxErrorException {
        script = new File(workspace +
                graph.getName().replaceFirst("\\.k([gG])raph$", "") +
                ".kScript");
        try {
            System.out.println(script.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.DefaultFiles.access.write(script, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String token : tokens) {
            singleCompile(token);
        }
        calls.add("export;");
        for (String call : calls) {
            try {
                Files.DefaultFiles.access.append(script, call);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public LinkedList<String> getCalls () {
        return calls;
    }

    public String getPath () {
        return script.getAbsolutePath();
    }

    final void singleCompile (String command) throws CompilerSyntaxErrorException {
        if (command.matches("info:\".+\"")) {
            calls.add("create " + command
                    .replaceAll("\"", "")
                    .split(":")[1]
                    .replaceAll("\\s*,", " ") + ";\n");
        }
        //if set dir
        else if (command.matches("dir:\".+\"")) {
            calls.add("changedir " + command
                    .replaceAll("\"", "")
                    .replaceFirst(":", "#/#")
                    .split("#/#")[1] + ";\n");
        }
        //if setfont
        else if (command.matches("font:\".+\"")) {
            calls.add("setfont " + command
                    .replaceAll("\"", "")
                    .split(":")[1]
                    .replaceAll("\\s*,", " ") + ";\n");
        }
        //if setcolor
        else if (command.matches("color:\".+\"")) {
            calls.add("setcolor " + command
                    .replaceAll("\"", "")
                    .split(":")[1] + ";\n");
        }
        //if draw circle
        else if (command.matches("circle:\".+\"")) {
            calls.add("drawcircle " + command
                    .replaceAll("\"", "")
                    .split(":")[1]
                    .replaceAll("\\s*,", " ") + ";\n");
        }
        //if draw line
        else if (command.matches("(line|rect|ellipse):\".+\"")) {
            calls.add("draw -" + command
                    .replaceAll("\"", "")
                    .split(":")[0] + " " + command
                    .replaceAll("\"", "")
                    .split(":")[1]
                    .replaceAll("\\s*,", " ") + ";\n");
        }
        //if draw pline
        else if (command.matches("pline:\".+\"")) {
            calls.add("drawpline " + command
                    .replaceAll("\"", "")
                    .split(":")[1]
                    .replaceAll("\\s*,", " ") + ";\n");
        }
        //illegal input
        else {
            throw new CompilerSyntaxErrorException("The syntax is error");
        }
    }

    public void clear () {
        this.api = null;
        this.graph = null;
        this.tokens = null;
    }
}
