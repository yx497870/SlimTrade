package com.slimtrade;

import com.slimtrade.core.References;
import com.slimtrade.core.chatparser.ChatParser;
import com.slimtrade.core.enums.AppState;
import com.slimtrade.core.enums.CurrencyType;
import com.slimtrade.core.jna.GlobalKeyboardListener;
import com.slimtrade.core.jna.GlobalMouseListener;
import com.slimtrade.core.managers.AudioManager;
import com.slimtrade.core.managers.FontManager;
import com.slimtrade.core.managers.LockManager;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.trading.LangRegex;
import com.slimtrade.core.utility.POEInterface;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.managers.FrameManager;
import com.slimtrade.gui.managers.HotkeyManager;
import com.slimtrade.gui.managers.SetupManager;
import com.slimtrade.gui.managers.SystemTrayManager;
import com.slimtrade.gui.pinning.PinManager;
import com.slimtrade.gui.windows.LoadingWindow;
import com.slimtrade.gui.windows.TutorialWindow;
import com.slimtrade.modules.stopwatch.Stopwatch;
import com.slimtrade.modules.theme.ThemeManager;
import com.slimtrade.modules.updater.UpdateAction;
import com.slimtrade.modules.updater.UpdateManager;
import com.slimtrade.modules.updater.ZLogger;
import com.slimtrade.modules.updater.data.AppInfo;
import com.slimtrade.modules.updater.data.AppVersion;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static GlobalKeyboardListener globalKeyboardListener;
    public static GlobalMouseListener globalMouseListener;
    private static LoadingWindow loadingWindow;
    private static LockManager lockManager;
    public static UpdateManager updateManager;

    public static ChatParser chatParser;

    public static AppInfo appInfo;
    private static AppState state = AppState.LOADING;
    private static AppState previousState = AppState.LOADING;
    private static boolean themesHaveBeenInitialized = false;
    private static boolean updateIsAvailable = false;
    private static boolean isRunningSetup = false;

    // Debug Flags
    public static boolean noUpdate = false;
    public static boolean noLock = false;
    public static boolean debug = false;
    public static boolean debugUIAlwaysOnTop = false;
    public static boolean chatInConsole = false; // TODO: This is broken, should fix or remove
    public static int debugUIBorders = 0; // Adds borders to certain UI elements. 0 for off, 1 or 2 for debugging
    public static final boolean debugProfileLaunch = false;
    public static boolean showOptionsOnLaunch = false;
    public static boolean forceSetup = false;

    public static void main(String[] args) {
        parseLaunchArgs(args);

        // Lock file to prevent duplicate instances
        lockManager = new LockManager(SaveManager.getSaveDirectory(), "app.lock");
        if (!noLock) {
            boolean lockSuccess = lockManager.tryAndLock();
            if (!lockSuccess) {
                System.err.println("SlimTrade is already running. Terminating new instance.");
                System.exit(0);
            }
        }

        // Logger
        ZLogger.open(SaveManager.getSaveDirectory(), args);
        ZLogger.log("SlimTrade launching... " + Arrays.toString(args));
        ZLogger.cleanOldLogFiles();

        // Launch profiling
        if (debugProfileLaunch) ZLogger.log("Profiling launch actions....");
        Stopwatch.start();

        // This setting gets rid of some rendering issues with transparent frames
        System.setProperty("sun.java2d.noddraw", "true");

        // Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(App::closeProgram));

        // Reduce logging level for JNativeHook
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        // Load save files & app info
        appInfo = readAppInfo();
        SaveManager.init();
        profileLaunch("Time to start update");

        // Update
        updateManager = new UpdateManager(References.AUTHOR, References.GITHUB_REPO, SaveManager.getSaveDirectory(), appInfo, appInfo.appVersion.isPreRelease);
        updateManager.continueUpdateProcess(args);
        if (!noUpdate) {
            if (updateManager.getCurrentUpdateAction() != UpdateAction.CLEAN && updateManager.isUpdateAvailable()) {
                if (SaveManager.settingsSaveFile.data.enableAutomaticUpdate) {
                    updateManager.runUpdateProcess();
                } else {
                    updateIsAvailable = true;
                }
            } else {
                updateManager.runPeriodicUpdateCheck();
            }
        }

        // Loading Window
        try {
            Stopwatch.start();
            SwingUtilities.invokeAndWait(() -> {
                initializeThemes();
                loadingWindow = new LoadingWindow(appInfo);
                loadingWindow.setVisible(true);
            });
            profileLaunch("ThemeManager");
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Init Managers
        Stopwatch.start();
        CurrencyType.initIconList();
        LangRegex.compileAll();
        POEInterface.init();
        AudioManager.init();
        profileLaunch("Managers Launched");

        // UI
        try {
            Stopwatch.start();
            SwingUtilities.invokeAndWait(() -> {
                // Initialize GUI
                SystemTrayManager.init();
                FrameManager.init();
            });
            profileLaunch("UI Creation");
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // JNativeHook Setup
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        globalKeyboardListener = new GlobalKeyboardListener();
        globalMouseListener = new GlobalMouseListener();
        GlobalScreen.addNativeKeyListener(globalKeyboardListener);
        GlobalScreen.addNativeMouseListener(globalMouseListener);
        GlobalScreen.addNativeMouseMotionListener(globalMouseListener);

        // Final Setup
        if (SetupManager.getSetupPhases().size() > 0) runSetupWizard();
        else ZUtil.invokeAndWait(App::launchApp);

        SwingUtilities.invokeLater(() -> loadingWindow.dispose());

        if (debugProfileLaunch) ZLogger.log("Profiling launch complete!\n");
        ZLogger.log("Slimtrade Launched");
    }

    private static void profileLaunch(String context) {
        if (!debugProfileLaunch) return;
        ZLogger.log("\t" + context + ": " + Stopwatch.getElapsedSeconds());
    }

    private static void runSetupWizard() {
        isRunningSetup = true;
        SwingUtilities.invokeLater(() -> {
            FrameManager.setupWindow.setup();
            FrameManager.setWindowVisibility(AppState.SETUP);
        });
    }

    public static void initializeThemes() {
        assert (SwingUtilities.isEventDispatchThread());
        if (themesHaveBeenInitialized) return;
        FontManager.loadFonts();
        ThemeManager.setTheme(SaveManager.settingsSaveFile.data.theme);
        ThemeManager.setFont(SaveManager.settingsSaveFile.data.preferredFontName);
        ThemeManager.setIconSize(SaveManager.settingsSaveFile.data.iconSize);
        ThemeManager.setFontSize(SaveManager.settingsSaveFile.data.fontSize);
        ThemeManager.checkFontChange();
        themesHaveBeenInitialized = true;
    }

    public static void launchApp() {
        assert SwingUtilities.isEventDispatchThread();

        cleanupSetupWindow();
        revertSaveFiles();
        reloadExampleTrades();
        applyPinsAndShowFrames();
        initializeChatParser();
        loadHotkeys();
        setAppStateRunning();
        showTutorialIfNeeded();
        showUpdateIfAvailable();
        showPatchNotesIfCleanUpdate();
    }

    private static void cleanupSetupWindow() {
        if (FrameManager.setupWindow != null) {
            FrameManager.setupWindow.dispose();
            FrameManager.setupWindow = null;
        }
        isRunningSetup = false;
    }

    private static void revertSaveFiles() {
        SaveManager.settingsSaveFile.revertChanges();
        SaveManager.stashSaveFile.revertChanges();
        SaveManager.chatScannerSaveFile.revertChanges();
        SaveManager.overlaySaveFile.revertChanges();
    }

    private static void reloadExampleTrades() {
        FrameManager.optionsWindow.reloadExampleTrades();
    }

    private static void applyPinsAndShowFrames() {
        PinManager.applyAllPins();
        FrameManager.showAppFrames();
        SystemTrayManager.showDefault();
    }

    private static void initializeChatParser() {
        if (chatParser != null) {
            chatParser.close();
            chatParser.removeAllListeners();
        }
        chatParser = new ChatParser();
        chatParser.addOnInitCallback(FrameManager.historyWindow);
        chatParser.addOnLoadedCallback(FrameManager.historyWindow);
        chatParser.addPreloadTradeListener(FrameManager.historyWindow);
        chatParser.addTradeListener(FrameManager.historyWindow);
        chatParser.addTradeListener(FrameManager.messageManager);
        chatParser.addChatScannerListener(FrameManager.messageManager);
        chatParser.addJoinedAreaListener(FrameManager.messageManager);
        chatParser.addOnLoadedCallback(FrameManager.menubarIcon);
        chatParser.addOnLoadedCallback(FrameManager.menubarDialog);
        chatParser.addDndListener(FrameManager.menubarIcon);
        chatParser.addDndListener(FrameManager.menubarDialog);
        chatParser.open(SaveManager.settingsSaveFile.data.clientPath);
    }

    private static void loadHotkeys() {
        HotkeyManager.loadHotkeys();
    }

    private static void setAppStateRunning() {
        App.setState(AppState.RUNNING);
    }

    private static void showTutorialIfNeeded() {
        if (SaveManager.appStateSaveFile.data.tutorialVersion < TutorialWindow.TUTORIAL_VERSION) {
            SwingUtilities.invokeLater(() -> FrameManager.tutorialWindow.setVisible(true));
            SaveManager.appStateSaveFile.data.tutorialVersion = TutorialWindow.TUTORIAL_VERSION;
            SaveManager.appStateSaveFile.saveToDisk(false);
        }
    }

    private static void showUpdateIfAvailable() {
        if (updateIsAvailable) FrameManager.displayUpdateAvailable();
    }

    private static void showPatchNotesIfCleanUpdate() {
        if (updateManager.getCurrentUpdateAction() == UpdateAction.CLEAN)
            SwingUtilities.invokeLater(() -> FrameManager.patchNotesWindow.setVisible(true));
    }


    public static void initParser() {
        if (chatParser != null) {
            chatParser.close();
            chatParser.removeAllListeners();
        }
        chatParser = new ChatParser();
        // History
        chatParser.addOnInitCallback(FrameManager.historyWindow);
        chatParser.addOnLoadedCallback(FrameManager.historyWindow);
        chatParser.addPreloadTradeListener(FrameManager.historyWindow);
        chatParser.addTradeListener(FrameManager.historyWindow);
        // Message Manager
        chatParser.addTradeListener(FrameManager.messageManager);
        chatParser.addChatScannerListener(FrameManager.messageManager);
        chatParser.addJoinedAreaListener(FrameManager.messageManager);
        // Menubar
        chatParser.addOnLoadedCallback(FrameManager.menubarIcon);
        chatParser.addOnLoadedCallback(FrameManager.menubarDialog);
        chatParser.addDndListener(FrameManager.menubarIcon);
        chatParser.addDndListener(FrameManager.menubarDialog);
        // Open
        chatParser.open(SaveManager.settingsSaveFile.data.clientPath);
    }

    public static AppInfo readAppInfo() {
        Properties properties = new Properties();
        try {
            InputStream stream = new BufferedInputStream(Objects.requireNonNull(UpdateManager.class.getClassLoader().getResourceAsStream("project.properties")));
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            ZLogger.err("Properties not found! Create a 'project.properties' file in the resources folder, then add the lines 'version=${project.version}' and 'artifactId=${project.artifactId}'.");
            return null;
        }
        String name = properties.getProperty("name");
        String version = properties.getProperty("version");
        String url = properties.getProperty("url");
        return new AppInfo(name, new AppVersion(version), url);
    }

    private static void parseLaunchArgs(String[] args) {
        for (String arg : args) {
            arg = arg.toLowerCase();
            processArgument(arg);
        }
    }

    private static void processArgument(String arg) {
        switch (arg) {
            case "-nu":
            case "-noupdate":
                noUpdate = true;
                break;
            case "-nl":
            case "-nolock":
                noLock = true;
                break;
            case "-d":
            case "-debug":
                debug = true;
                break;
            case "-o":
            case "-options":
                showOptionsOnLaunch = true;
                break;
            case "-ui":
                debugUIAlwaysOnTop = true;
                break;
            case "-setup":
                forceSetup = true;
                break;
        }
    }


    // FIXME: This
    public static void setState(AppState state) {
        previousState = App.state;
        App.state = state;
    }

    public static AppState getState() {
        return App.state;
    }

    public static AppState getPreviousState() {
        return App.previousState;
    }

    public static boolean isRunningSetup() {
        return isRunningSetup;
    }

    private static void closeProgram() {
        try {
            GlobalScreen.unregisterNativeHook();
            lockManager.closeLock();
            ZLogger.log("SlimTrade Terminated");
            ZLogger.close();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

}