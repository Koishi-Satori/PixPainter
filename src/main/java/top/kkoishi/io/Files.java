/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.io;

import java.io.*;
import java.util.stream.Stream;

/**
 * @author KKoishi
 * @apiNote fuck world
 * @since java8
 */
public interface Files {
    int BUFFERED_SIZE = 1024;

    class DefaultFiles implements Files {
        public static Files access = new DefaultFiles();

        private DefaultFiles () {
        }

        public static Files build () {
            return new DefaultFiles();
        }

        @Override
        public String read (String path) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder zwcsb = new StringBuilder();
            char[] cs = new char[BUFFERED_SIZE];
            int len;
            while ((len = reader.read(cs)) != -1) {
                zwcsb.append(new String(cs, 0, len));
            }
            reader.close();
            return zwcsb.toString();
        }

        @Override
        public String read (File file) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder zwcsb = new StringBuilder();
            char[] cs = new char[BUFFERED_SIZE];
            int len;
            while ((len = reader.read(cs)) != -1) {
                zwcsb.append(new String(cs, 0, len));
            }
            reader.close();
            return zwcsb.toString();
        }

        @Override
        public Stream<String> readStream (String path) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            final Stream<String> ss = reader.lines();
            reader.close();
            return ss;
        }

        @Override
        public Stream<String> readStream (File file) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            final Stream<String> ss = reader.lines();
            reader.close();
            return ss;
        }

        @Override
        public byte[] readRaw (String path) throws IOException {
            FileInputStream fis = new FileInputStream(path);
            byte[] bs = fis.readAllBytes();
            fis.close();
            return bs;
        }

        @Override
        public byte[] readRaw (File file) throws IOException {
            FileInputStream fis = new FileInputStream(file);
            byte[] bs = fis.readAllBytes();
            fis.close();
            return bs;
        }

        @Override
        public void write (String path, String content) throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.close();
        }

        @Override
        public void write (String path, byte[] bytes) throws IOException {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(bytes);
            fos.close();
        }

        @Override
        public void write (File file, String content) throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.close();
        }

        @Override
        public void write (File file, byte[] bytes) throws IOException {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        }

        @Override
        public void append (String path, String content) throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
            bw.write(content);
            bw.close();
        }

        @Override
        public void append (String path, byte[] bytes) throws IOException {
            FileOutputStream fos = new FileOutputStream(path, true);
            fos.write(bytes);
            fos.close();
        }

        @Override
        public void append (File file, String content) throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(content);
            bw.close();
        }

        @Override
        public void append (File file, byte[] bytes) throws IOException {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(bytes);
            fos.close();
        }

        @Override
        public boolean writeAndGet (String path, String content, boolean ifAppend) {
            try {
                write(path, content);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        public boolean writeAndGet (String path, byte[] bytes, boolean ifAppend) {
            try {
                write(path, bytes);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        public boolean writeAndGet (File file, String content, boolean ifAppend) {
            try {
                write(file, content);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        public boolean writeAndGet (File file, byte[] bytes, boolean ifAppend) {
            try {
                write(file, bytes);
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }

    static Files build () {
        return new DefaultFiles();
    }

    String read (String path) throws IOException;

    String read (File file) throws IOException;

    Stream<String> readStream (String path) throws IOException;

    Stream<String> readStream (File file) throws IOException;

    byte[] readRaw (String path) throws IOException;

    byte[] readRaw (File file) throws IOException;

    void write (String path, String content) throws IOException;

    void write (String path, byte[] bytes) throws IOException;

    void write (File file, String content) throws IOException;

    void write (File file, byte[] bytes) throws IOException;

    void append (String path, String content) throws IOException;

    void append (String path, byte[] bytes) throws IOException;

    void append (File file, String content) throws IOException;

    void append (File file, byte[] bytes) throws IOException;

    boolean writeAndGet (String path, String content, boolean ifAppend);

    boolean writeAndGet (String path, byte[] bytes, boolean ifAppend);

    boolean writeAndGet (File file, String content, boolean ifAppend);

    boolean writeAndGet (File file, byte[] bytes, boolean ifAppend);
}
