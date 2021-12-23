package top.kkoishi.io;

import top.kkoishi.util.LinkedList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static java.lang.System.gc;

public class KoishiReader {
    protected LinkedList<Map.Entry<String, String>> entries = new LinkedList<>();
    protected String path;

    public KoishiReader (String path) {
        this.path = path;
    }

    public void read () {
        String str;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            int len;
            char[] buffers = new char[1024];
            while ((len = br.read(buffers)) != -1) {
                sb.append(new String(buffers, 0, len));
            }
            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        explain(str);
    }

    final void explain (String str) {
        String[] strs = str.replaceAll("\n\r", "").split(";");
        for (String s : strs) {
            entries.add(new Map.Entry<>() {
                private final String key = s.split(":")[0];
                private String value = s.split(":")[1].replaceAll("\"", "");

                @Override
                public String getKey () {
                    return key;
                }

                @Override
                public String getValue () {
                    return value;
                }

                @Override
                public String setValue (String value) {
                    final String temp = this.value;
                    this.value = value;
                    return temp;
                }
            });
        }
    }

    public LinkedList<Map.Entry<String, String>> get () {
        return entries;
    }

    public void clear () {
        this.entries = null;
        this.path = null;
        gc();
    }

    public String getPath () {
        return path;
    }

    public void setPath (String path) {
        this.path = path;
    }
}
