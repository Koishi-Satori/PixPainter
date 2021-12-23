package top.kkoishi.g2d;

import java.util.HashMap;

public class Color {
    public static HashMap<String, java.awt.Color> colors = new HashMap<>();
    static {
        colors.put("white", java.awt.Color.WHITE);
        colors.put("black", java.awt.Color.BLACK);
        colors.put("red", java.awt.Color.RED);
        colors.put("cyan", java.awt.Color.CYAN);
        colors.put("blue", java.awt.Color.BLUE);
        colors.put("lightgray", java.awt.Color.LIGHT_GRAY);
        colors.put("gray", java.awt.Color.gray);
        colors.put("darkgray", java.awt.Color.DARK_GRAY);
        colors.put("pink", java.awt.Color.PINK);
        colors.put("orange", java.awt.Color.ORANGE);
        colors.put("yellow", java.awt.Color.YELLOW);
        colors.put("green", java.awt.Color.GREEN);
        colors.put("mageta", java.awt.Color.MAGENTA);
    }
}
