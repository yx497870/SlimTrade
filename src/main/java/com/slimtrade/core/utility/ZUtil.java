package com.slimtrade.core.utility;

import com.slimtrade.App;
import com.slimtrade.core.data.PasteReplacement;
import com.slimtrade.modules.updater.ZLogger;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ZUtil {

    private static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("##.##");

    /**
     * Returns a printable version of an enum name.
     *
     * @param input
     * @return
     */
    public static String enumToString(String input) {
        input = input.replaceAll("_", " ");
        input = input.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i == 0 || input.charAt(i - 1) == ' ') {
                builder.append(Character.toUpperCase(input.charAt(i)));
            } else {
                builder.append(input.charAt(i));
            }
        }
        return builder.toString();
    }

    public static ArrayList<String> getCommandList(String input, PasteReplacement pasteReplacement) {
        ArrayList<String> commands = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '/' || c == '@') {
                if (builder.length() > 0)
                    commands.add(builder.toString().trim());
                builder.setLength(0);
            }
            builder.append(c);
        }
        if (builder.length() > 0)
            commands.add(builder.toString().trim());
        for (int i = 0; i < commands.size(); i++) {
            String clean = commands.get(i);
            clean = clean.replaceAll("\\{zone}", App.chatParser.getCurrentZone());
            clean = clean.replaceAll("\\{message}", pasteReplacement.message);
            if (!clean.startsWith("@") && !clean.startsWith("/"))
                clean = "@" + pasteReplacement.playerName + " " + clean;
            if (pasteReplacement.playerName != null)
                clean = clean.replaceAll("\\{player}", pasteReplacement.playerName);
            if (pasteReplacement.priceName != null) {
                String itemPrefix = pasteReplacement.itemQuantity > 0 ? pasteReplacement.itemQuantity + " " : "1 ";
                clean = clean.replaceAll("\\{item}", itemPrefix + pasteReplacement.itemName);
            }
            if (pasteReplacement.priceName != null) {
                clean = clean.replaceAll("\\{price}", NUMBER_FORMATTER.format(pasteReplacement.priceQuantity) + " " + pasteReplacement.priceName);
            }
            commands.set(i, clean);
        }
        return commands;
    }

    /**
     * Trims a string and normalizes all spaces to 1.
     *
     * @param input String to clean
     * @return Cleaned string
     */
    public static String cleanString(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }


    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static String formatNumber(double d) {
        return NUMBER_FORMATTER.format(d);
    }

    /**
     * Returns a new GridBagConstraint with gridX and gridY initialized to 0.
     * This is needed to allow incrementing either variable to work correctly.
     *
     * @return GridBagConstraints
     */
    public static GridBagConstraints getGC() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        return gc;
    }

    public static JPanel addStrutsToBorderPanel(JPanel panel, int inset) {
        return addStrutsToBorderPanel(panel, new Insets(inset, inset, inset, inset));
    }

    public static JPanel addStrutsToBorderPanel(JPanel panel, Insets insets) {
        if (insets.left > 0) panel.add(Box.createHorizontalStrut(insets.left), BorderLayout.WEST);
        if (insets.right > 0) panel.add(Box.createHorizontalStrut(insets.right), BorderLayout.EAST);
        if (insets.top > 0) panel.add(Box.createVerticalStrut(insets.top), BorderLayout.NORTH);
        if (insets.bottom > 0) panel.add(Box.createVerticalStrut(insets.bottom), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Given a point on the screen, returns the bounds of the monitor containing that point.
     *
     * @param point A point on the screen
     * @return Screen bounding rectangle
     */
    public static Rectangle getScreenBoundsFromPoint(Point point) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            if (bounds.contains(point)) {
                return bounds;
            }
        }
        return null;
    }

    /**
     * Ensure a cheat sheet has a valid extension, then returns that extension (including period)
     *
     * @return
     */
    public static String getCheatSheetExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        String ext = fileName.substring(index);
        return ext;
    }

    public static boolean isEmptyString(String input) {
        return input.matches("\\s*");
    }

    public static boolean openLink(String link) {
        if (link.startsWith("http:")) {
            link = link.replaceFirst("http:", "https:");
        }
        if (!link.startsWith("https://")) {
            link = "https://" + link;
        }
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(link));
                return true;
            } catch (IOException | URISyntaxException e) {
//                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void openExplorer(String path) {
        File targetDir = new File(path);
        if (!targetDir.exists()) {
            boolean success = targetDir.mkdirs();
            if (!success) return;
        }
        if (!targetDir.exists() || !targetDir.isDirectory()) return;
        try {
            Desktop.getDesktop().open(targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFile(String path) {
        if (path == null) return;
        File file = new File(path);
        if (!file.exists() || !file.isFile()) return;
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getVolumeText(int volume) {
        if (volume == 0) {
            return "Muted";
        } else {
            return volume + "%";
        }
    }


    public static void printStackTrace() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement e : elements) {
            System.err.println(e);
        }
    }

    public static <T> void printCallingFunction(Class<T> originClass) {
        String className = originClass.getSimpleName();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        boolean found = false;
        for (StackTraceElement e : elements) {
            String line = e.toString();
            if (found && !line.contains(className)) {
                System.err.println(e);
                break;
            }
            if (line.contains(className)) found = true;
        }
    }

    /**
     * A wrapper for SwingUtilities.invokeAndWait(). Handles try/catch and can be safely called from EDT.
     *
     * @param runnable Target
     */
    public static void invokeAndWait(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) runnable.run();
        else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean fileExists(String path) {
        path = cleanPath(path);
        if (isPathRelative(path)) {
            URL url = ZUtil.class.getResource(path);
            return url != null;
        } else {
            File file = new File(path);
            return file.exists();
        }
    }

    public static boolean isPathRelative(String path) {
        path = cleanPath(path);
        return path.startsWith("/");
    }

    public static String cleanPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * Returns a BufferedReader for a given file path set to UTF-8 encoding.
     * If given a relative path (starts with "/"), will load the file from the resources folder.
     */
    public static BufferedReader getBufferedReader(String path) {
        path = cleanPath(path);
        InputStreamReader streamReader;
        if (path.startsWith("/")) {
            streamReader = new InputStreamReader(Objects.requireNonNull(ZUtil.class.getResourceAsStream(path)), StandardCharsets.UTF_8);
        } else {
            try {
                streamReader = new InputStreamReader(Files.newInputStream(Paths.get(path)), StandardCharsets.UTF_8);
            } catch (IOException e) {
                ZLogger.err("File not found: " + path);
                return null;
            }
        }
        return new BufferedReader(streamReader);
    }

}
