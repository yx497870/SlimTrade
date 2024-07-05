package com.slimtrade.core.utility;

import com.slimtrade.modules.updater.ZLogger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Objects;

public class FileUtil {

    public static boolean fileExists(String path) {
        path = StringUtil.cleanPath(path);
        if (isPathRelative(path)) {
            URL url = FileUtil.class.getResource(path);
            return url != null;
        } else {
            File file = new File(path);
            return file.exists();
        }
    }

    public static boolean isPathRelative(String path) {
        path = StringUtil.cleanPath(path);
        return path.startsWith("/");
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

    public static BufferedReader getBufferedReader(String path) {
        path = StringUtil.cleanPath(path);
        InputStreamReader streamReader;
        if (path.startsWith("/")) {
            streamReader = new InputStreamReader(Objects.requireNonNull(FileUtil.class.getResourceAsStream(path)), StandardCharsets.UTF_8);
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

