package top.kkoishi.g2d;

import top.kkoishi.util.LinkedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

/**
 * @author KKoishi_
 */
public class ScriptExplainer {
    PaintApi api;
    File script;
    LinkedList<String> tokens = new LinkedList<>();
    LinkedList<String> calls = new LinkedList<>();

    public ScriptExplainer (File script) {
        api = new PaintApi("./");
        this.script = script;
    }

    public ScriptExplainer (String dir, File script) {
        api = new PaintApi(dir);
        this.script = script;
    }

    public void read () throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(script));
        StringBuilder zwcsb = new StringBuilder();
        int len;
        char[] cs = new char[1024];
        while ((len = br.read(cs)) != -1) {
            zwcsb.append(new String(cs, 0, len));
        }
        String[] strs = zwcsb.toString().split(";");
        tokens.addAll(Arrays.asList(strs));
        //help gc
        zwcsb = null;
        strs = null;
        br.close();
        //tokens.removeLast();
    }

    public void execute () {
        for (String token : tokens) {
            String command = token.replaceAll("\n", "").replaceAll("\r", "");
            out.print(command + "|");
            api.execute(command);
            String call = api.getCall();
            calls.add(call);
        }
    }

    public List<String> getEcho () {
        return calls;
    }

    public void clear () {
        this.api = null;
        this.script = null;
        this.tokens = null;
        this.calls = null;
    }
}
