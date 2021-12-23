/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.io;

import top.kkoishi.util.LinkedList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * A simple file(directory) chooser interface, and it provides a default instance
 * You can get it by invoking {@code getInstance()}.
 * @author KKoishi
 * @since java8
 * @see FileChooser#getInstance()
 * @apiNote fuck world
 */
public interface FileChooser {
    class DefaultFileChooser implements FileChooser {
        private DefaultFileChooser () {
        }

        public static FileChooser build () {
            return new DefaultFileChooser();
        }

        /**
         * Pop a file choose frame and return a file path which is select by user.
         * If user select none, then will feed back {@code null}, it needs to be handled.
         * The outlook style of the frame is chosen by JVM.
         * <blockquote>
         * <b>[important]The extension name should be without a dot at the begin of the String!</b>
         * <p>Like this:</p>
         * <p>xxx.singleFile("FileChooser","music(.mp3,.flac)","mp3","flac")</p>
         * </blockquote>
         *
         * @param dialogTitle the title of the select frame.
         * @param description the description of the file type.
         * @param extensions  an array, or multiple Strings, which is the extension of the file.
         * @return the absolute path of the file
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public String singleFile (String dialogTitle, String description, String... extensions)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            String path = null;
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                path = jFileChooser.getSelectedFile().getAbsolutePath();
            }
            return path;
        }

        /**
         * Pop a directory choose frame and return a file path which is select by user.
         * If user select none, then will feed back {@code null}, it needs to be handled.
         * The outlook style of the frame is chosen by JVM.
         * <blockquote>
         * <p>A simple invoke method example:</p>
         * <p>xxx.singleDir("DirectoryChooser","Directory")</p>
         * </blockquote>
         *
         * @param dialogTitle the title of the select frame.
         * @param description the description of the directory.
         * @return the absolute path of the directory.
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public String singleDir (String dialogTitle, String description)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            String path = null;
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, "directories");
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle("请选择一个文件夹");
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                path = jFileChooser.getSelectedFile().getAbsolutePath();
            }
            return path;
        }

        /**
         * Pop a directory choose frame and return a file path which is select by user.
         * If user select none, then will feed back {@code null}, it needs to be handled.
         * The outlook style of the frame is chosen by JVM.
         * The description will be "Directory".
         * The default one invoke the method {@code singleDir (String dialogTitle, String description)}.
         * <blockquote>
         * <p>A simple invoke method example:</p>
         * <p>xxx.singleDir("DirectoryChooser")</p>
         * </blockquote>
         *
         * @param dialogTitle the title of the select frame.
         * @return the absolute path of the directory.
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public String singleDir (String dialogTitle)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            return singleDir(dialogTitle, "Directory");
        }

        /**
         * Pop a file choose frame and return a list which contains the files' path which are/is select by user.
         * If user select none, then will feed back {@code empty List}, it needs to be handled.
         * The outlook style of the frame is chosen by JVM.
         * <blockquote>
         * <b>[important]The extension name should be without a dot at the begin of the String!</b>
         * <p>Like this:</p>
         * <p>xxx.files("FileChooser","music(.mp3,.flac)","mp3","flac")</p>
         * </blockquote>
         *
         * @param dialogTitle the title of the select frame.
         * @param description the description of the files' type.
         * @param extensions  an array, or multiple Strings, which is the extension of the files.
         * @return the absolute path of the files, a {@code List<String>}
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public List<String> files (String dialogTitle, String description, String... extensions)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            List<String> paths = new LinkedList<>();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.setMultiSelectionEnabled(true);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                File[] fs = jFileChooser.getSelectedFiles();
                for (File f : fs) {
                    paths.add(f.getAbsolutePath());
                }
            }
            return paths;
        }

        /**
         * Pop a directory choose frame and return a list which contains the directories' path
         * which are/is select by user.
         * If user select none, then will feed back {@code empty List}, it needs to be handled.
         * The outlook style of the frame is chosen by JVM.
         * <blockquote>
         * <p>A simple invoke method:</p>
         * <p>xxx.dirs("DirectoryChooser","Directories")</p>
         * </blockquote>
         *
         * @param dialogTitle the title of the select frame.
         * @param description the description of the directories.
         * @return the absolute path of the directories, a {@code List<String>}
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public List<String> dirs (String dialogTitle, String description)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            List<String> paths = new LinkedList<>();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setMultiSelectionEnabled(true);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, "directories");
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                File[] fs = jFileChooser.getSelectedFiles();
                for (File f : fs) {
                    paths.add(f.getAbsolutePath());
                }
            }
            return paths;
        }

        /**
         * Pop a directory choose frame and return a list which contains the directories' path
         * which are/is select by user.
         * If user select none, then will feed back {@code empty List}, it needs to be handled.
         * The outlook style of the frame is chosen by JVM.
         * <blockquote>
         * <p>A simple invoke method:</p>
         * <p>xxx.dirs("DirectoryChooser")</p>
         * </blockquote>
         *
         * @param dialogTitle the title of the select frame.
         * @return the absolute path of the directories, a {@code List<String>}
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public List<String> dirs (String dialogTitle)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            return dirs(dialogTitle, "Directories");
        }

        /**
         * Pop a file choose frame and return a file path which is select by user.
         * If user select none, then will feed back {@code null}, it needs to be handled.
         * The outlook style of the frame can be chosen by invoker.
         * <blockquote>
         * <b>[important]The extension name should be without a dot at the begin of the String!</b>
         * <p>Like this:</p>
         * <p>xxx.singleFile("FileChooser", UIManager.getSystemLookAndFeelClassName(),"music(.mp3,.flac)","mp3","flac")</p>
         * </blockquote>
         *
         * @param dialogTitle      the title of the select frame.
         * @param lookAndFeelStyle the outlook style of the UI.
         * @param description      the description of the file type.
         * @param extensions       an array, or multiple Strings, which is the extension of the file.
         * @return the absolute path of the file
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public String singleFileCustom (String dialogTitle, String lookAndFeelStyle, String description
                , String... extensions) throws UnsupportedLookAndFeelException
                , ClassNotFoundException, InstantiationException, IllegalAccessException {
            String path = null;
            UIManager.setLookAndFeel(lookAndFeelStyle);
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                path = jFileChooser.getSelectedFile().getAbsolutePath();
            }
            return path;
        }

        /**
         * Pop a directory choose frame and return a file path which is select by user.
         * If user select none, then will feed back {@code null}, it needs to be handled.
         * The outlook style of the frame can be chosen by invoker.
         * <blockquote>
         * <p>A simple invoke method example</p>
         * <p>xxx.singleDir("DirectoryChooser", UIManager.getSystemLookAndFeelClassName(),"directory")</p>
         * </blockquote>
         *
         * @param dialogTitle      the title of the select frame.
         * @param lookAndFeelStyle the outlook style of the UI.
         * @param description      the description of the directory type.
         * @return the absolute path of the directory
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public String singleDirCustom (String dialogTitle, String lookAndFeelStyle, String description)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            String path = null;
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, "directories");
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                path = jFileChooser.getSelectedFile().getAbsolutePath();
            }
            return path;
        }

        /**
         * Pop a directory choose frame and return a file path which is select by user.
         * If user select none, then will feed back {@code null}, it needs to be handled.
         * The outlook style of the frame can be chosen by invoker.
         * <blockquote>
         * <p>A simple invoke method example</p>
         * <p>xxx.singleDir("DirectoryChooser", UIManager.getSystemLookAndFeelClassName())</p>
         * </blockquote>
         *
         * @param dialogTitle      the title of the select frame.
         * @param lookAndFeelStyle the outlook style of the UI.
         * @return the absolute path of the directory
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public String singleDirCustom (String dialogTitle, String lookAndFeelStyle)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            return singleDirCustom(dialogTitle, lookAndFeelStyle, "Directories");
        }

        /**
         * Pop a file choose frame and return a list which contains the files' path which are/is select by user.
         * If user select none, then will feed back {@code empty List}, it needs to be handled.
         * The outlook style of the frame can be chosen by invoker.
         * <blockquote>
         * <b>[important]The extension name should be without a dot at the begin of the String!</b>
         * <p>Like this:</p>
         * <p>xxx.files("FileChooser",UIManager.getSystemLookAndFeelClassName(),"music(.mp3,.flac)","mp3","flac")</p>
         * </blockquote>
         *
         * @param dialogTitle      the title of the select frame.
         * @param lookAndFeelStyle the outlook style of the UI.
         * @param description      the description of the files' type.
         * @param extensions       an array, or multiple Strings, which is the extension of the files.
         * @return the absolute path of the files, a {@code List<String>}
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public List<String> filesCustom (String dialogTitle, String lookAndFeelStyle
                , String description, String... extensions)
                throws UnsupportedLookAndFeelException, ClassNotFoundException
                , InstantiationException, IllegalAccessException {
            List<String> paths = new LinkedList<>();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.setMultiSelectionEnabled(true);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                File[] fs = jFileChooser.getSelectedFiles();
                for (File f : fs) {
                    paths.add(f.getAbsolutePath());
                }
            }
            return paths;
        }

        /**
         * Pop a directory choose frame and return a list which contains the directories' path
         * which are/is select by user.
         * If user select none, then will feed back {@code empty List}, it needs to be handled.
         * The outlook style of the frame can be chosen by invoker.
         * <blockquote>
         * <p>A simple invoke method:</p>
         * <p>xxx.dirs("DirectoryChooser", UIManager.getSystemLookAndFeelClassName(),"Directories")</p>
         * </blockquote>
         *
         * @param dialogTitle      the title of the select frame.
         * @param lookAndFeelStyle the outlook style of the UI.
         * @param description      the description of the directories.
         * @return the absolute path of the directories, a {@code List<String>}
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public List<String> dirsCustom (String dialogTitle, String lookAndFeelStyle, String description)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            List<String> paths = new LinkedList<>();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setMultiSelectionEnabled(true);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, "directories");
            jFileChooser.setFileFilter(filter);
            jFileChooser.setDialogTitle(dialogTitle);
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
                File[] fs = jFileChooser.getSelectedFiles();
                for (File f : fs) {
                    paths.add(f.getAbsolutePath());
                }
            }
            return paths;
        }

        /**
         * Pop a directory choose frame and return a list which contains the directories' path
         * which are/is select by user.
         * If user select none, then will feed back {@code empty List}, it needs to be handled.
         * The outlook style of the frame can be chosen by invoker.
         * <blockquote>
         * <p>A simple invoke method:</p>
         * <p>xxx.dirs("DirectoryChooser", UIManager.getSystemLookAndFeelClassName())</p>
         * </blockquote>
         *
         * @param dialogTitle      the title of the select frame.
         * @param lookAndFeelStyle the outlook style of the UI.
         * @return the absolute path of the directories, a {@code List<String>}
         * @throws ClassNotFoundException          when the UI style is not loaded correctly.
         * @throws InstantiationException          when the UI instance can not be created.
         * @throws IllegalAccessException          thrown by JFileChooser
         * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
         */
        @Override
        public List<String> dirsCustom (String dialogTitle, String lookAndFeelStyle)
                throws UnsupportedLookAndFeelException, ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            return dirsCustom(dialogTitle, lookAndFeelStyle, "Directories");
        }
    }

    static FileChooser getInstance () {
        return new DefaultFileChooser();
    }

    /**
     * Pop a file choose frame and return a file path which is select by user.
     * If user select none, then will feed back {@code null}, it needs to be handled.
     * The outlook style of the frame is chosen by JVM.
     * <blockquote>
     * <b>[important]The extension name should be without a dot at the begin of the String!</b>
     * <p>Like this:</p>
     * <p>xxx.singleFile("FileChooser","music(.mp3,.flac)","mp3","flac")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param description the description of the file type.
     * @param extensions  an array, or multiple Strings, which is the extension of the file.
     * @return the absolute path of the file
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    String singleFile (String dialogTitle, String description, String... extensions)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a file path which is select by user.
     * If user select none, then will feed back {@code null}, it needs to be handled.
     * The outlook style of the frame is chosen by JVM.
     * <blockquote>
     * <p>A simple invoke method example:</p>
     * <p>xxx.singleDir("DirectoryChooser","Directory")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param description the description of the directory.
     * @return the absolute path of the directory.
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    String singleDir (String dialogTitle, String description)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a file path which is select by user.
     * If user select none, then will feed back {@code null}, it needs to be handled.
     * The outlook style of the frame is chosen by JVM.
     * The description will be "Directory".
     * The default one invoke the method {@code singleDir (String dialogTitle, String description)}.
     * <blockquote>
     * <p>A simple invoke method example:</p>
     * <p>xxx.singleDir("DirectoryChooser")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @return the absolute path of the directory.
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    String singleDir (String dialogTitle) throws UnsupportedLookAndFeelException,
            ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * Pop a file choose frame and return a list which contains the files' path which are/is select by user.
     * If user select none, then will feed back {@code empty List}, it needs to be handled.
     * The outlook style of the frame is chosen by JVM.
     * <blockquote>
     * <b>[important]The extension name should be without a dot at the begin of the String!</b>
     * <p>Like this:</p>
     * <p>xxx.files("FileChooser","music(.mp3,.flac)","mp3","flac")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param description the description of the files' type.
     * @param extensions  an array, or multiple Strings, which is the extension of the files.
     * @return the absolute path of the files, a {@code List<String>}
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    List<String> files (String dialogTitle, String description, String... extensions)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a list which contains the directories' path
     * which are/is select by user.
     * If user select none, then will feed back {@code empty List}, it needs to be handled.
     * The outlook style of the frame is chosen by JVM.
     * <blockquote>
     * <p>A simple invoke method:</p>
     * <p>xxx.dirs("DirectoryChooser","Directories")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param description the description of the directories.
     * @return the absolute path of the directories, a {@code List<String>}
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    List<String> dirs (String dialogTitle, String description)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a list which contains the directories' path
     * which are/is select by user.
     * If user select none, then will feed back {@code empty List}, it needs to be handled.
     * The outlook style of the frame is chosen by JVM.
     * <blockquote>
     * <p>A simple invoke method:</p>
     * <p>xxx.dirs("DirectoryChooser")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @return the absolute path of the directories, a {@code List<String>}
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    List<String> dirs (String dialogTitle) throws UnsupportedLookAndFeelException,
            ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * Pop a file choose frame and return a file path which is select by user.
     * If user select none, then will feed back {@code null}, it needs to be handled.
     * The outlook style of the frame can be chosen by invoker.
     * <blockquote>
     * <b>[important]The extension name should be without a dot at the begin of the String!</b>
     * <p>Like this:</p>
     * <p>xxx.singleFile("FileChooser", UIManager.getSystemLookAndFeelClassName(),"music(.mp3,.flac)","mp3","flac")</p>
     * </blockquote>
     *
     * @param dialogTitle      the title of the select frame.
     * @param description      the description of the file type.
     * @param extensions       an array, or multiple Strings, which is the extension of the file.
     * @param lookAndFeelStyle the outlook style of the UI.
     * @return the absolute path of the file
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    String singleFileCustom (String dialogTitle, String lookAndFeelStyle, String description
            , String... extensions) throws UnsupportedLookAndFeelException,
            ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a file path which is select by user.
     * If user select none, then will feed back {@code null}, it needs to be handled.
     * The outlook style of the frame can be chosen by invoker.
     * <blockquote>
     * <p>A simple invoke method example</p>
     * <p>xxx.singleDir("DirectoryChooser", UIManager.getSystemLookAndFeelClassName(),"directory")</p>
     * </blockquote>
     *
     * @param dialogTitle      the title of the select frame.
     * @param description      the description of the directory type.
     * @param lookAndFeelStyle the outlook style of the UI.
     * @return the absolute path of the directory
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    String singleDirCustom (String dialogTitle, String lookAndFeelStyle, String description)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a file path which is select by user.
     * If user select none, then will feed back {@code null}, it needs to be handled.
     * The outlook style of the frame can be chosen by invoker.
     * <blockquote>
     * <p>A simple invoke method example</p>
     * <p>xxx.singleDir("DirectoryChooser", UIManager.getSystemLookAndFeelClassName())</p>
     * </blockquote>
     *
     * @param dialogTitle      the title of the select frame.
     * @param lookAndFeelStyle the outlook style of the UI.
     * @return the absolute path of the directory
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    String singleDirCustom (String dialogTitle, String lookAndFeelStyle)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a file choose frame and return a list which contains the files' path which are/is select by user.
     * If user select none, then will feed back {@code empty List}, it needs to be handled.
     * The outlook style of the frame can be chosen by invoker.
     * <blockquote>
     * <b>[important]The extension name should be without a dot at the begin of the String!</b>
     * <p>Like this:</p>
     * <p>xxx.files("FileChooser",UIManager.getSystemLookAndFeelClassName(),"music(.mp3,.flac)","mp3","flac")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param lookAndFeelStyle the outlook style of the UI.
     * @param description the description of the files' type.
     * @param extensions  an array, or multiple Strings, which is the extension of the files.
     * @return the absolute path of the files, a {@code List<String>}
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    List<String> filesCustom (String dialogTitle, String lookAndFeelStyle, String description
            , String... extensions) throws UnsupportedLookAndFeelException,
            ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a list which contains the directories' path
     * which are/is select by user.
     * If user select none, then will feed back {@code empty List}, it needs to be handled.
     * The outlook style of the frame can be chosen by invoker.
     * <blockquote>
     * <p>A simple invoke method:</p>
     * <p>xxx.dirs("DirectoryChooser", UIManager.getSystemLookAndFeelClassName(),"Directories")</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param lookAndFeelStyle the outlook style of the UI.
     * @param description the description of the directories.
     * @return the absolute path of the directories, a {@code List<String>}
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    List<String> dirsCustom (String dialogTitle, String lookAndFeelStyle, String description)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;

    /**
     * Pop a directory choose frame and return a list which contains the directories' path
     * which are/is select by user.
     * If user select none, then will feed back {@code empty List}, it needs to be handled.
     * The outlook style of the frame can be chosen by invoker.
     * <blockquote>
     * <p>A simple invoke method:</p>
     * <p>xxx.dirs("DirectoryChooser", UIManager.getSystemLookAndFeelClassName())</p>
     * </blockquote>
     *
     * @param dialogTitle the title of the select frame.
     * @param lookAndFeelStyle the outlook style of the UI.
     * @return the absolute path of the directories, a {@code List<String>}
     * @throws ClassNotFoundException          when the UI style is not loaded correctly.
     * @throws InstantiationException          when the UI instance can not be created.
     * @throws IllegalAccessException          thrown by JFileChooser
     * @throws UnsupportedLookAndFeelException when the look and feel style is not supported.
     */
    List<String> dirsCustom (String dialogTitle, String lookAndFeelStyle)
            throws UnsupportedLookAndFeelException, ClassNotFoundException,
            InstantiationException, IllegalAccessException;
}
