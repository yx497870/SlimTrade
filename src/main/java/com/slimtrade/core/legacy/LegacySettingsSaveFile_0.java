package com.slimtrade.core.legacy;

import com.google.gson.annotations.SerializedName;
import com.slimtrade.core.audio.Sound;
import com.slimtrade.core.audio.SoundComponent;
import com.slimtrade.core.data.CheatSheetData;
import com.slimtrade.core.data.StashTabData;
import com.slimtrade.core.enums.*;
import com.slimtrade.core.hotkeys.HotkeyData;
import com.slimtrade.core.utility.MacroButton;
import com.slimtrade.gui.options.searching.StashSearchTermData;
import com.slimtrade.gui.options.stash.StashTabType;
import com.slimtrade.modules.theme.Theme;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Save file used for v0.3.5 and below.
 */
@SuppressWarnings("unused")
public class LegacySettingsSaveFile_0 {

    //Version
    @SerializedName("versionNumber")
    public String appVersionString;

    // Basics
    public String characterName = "";
    public boolean showGuildName = false;
    public boolean folderOffset = true;
    public boolean colorBlindMode = false;
    @SerializedName("colorTheme")
    public LegacyColorTheme theme = LegacyColorTheme.SOLARIZED_LIGHT;
    //    public QuickPasteSetting quickPasteSetting = QuickPasteSetting.DISABLED;
    public HotkeyData quickPasteHotkey = null;
//    public boolean autoUpdate = true;

    // Messaging
    @SerializedName("collapseExcessiveMessages")
    public boolean collapseMessages = false;
    public int messageCountBeforeCollapse = 3;
    @SerializedName("fadeAfterDuration")
    public boolean fadeMessages = false;
    public float secondsBeforeFading = 2;
    @SerializedName("fadeOpacityPercent")
    public int fadedOpacity = 50;

    // History
    @SerializedName("timeStyle")
    public TimeFormat historyTimeFormat = TimeFormat.H12;
    @SerializedName("dateStyle")
    public LegacyDateFormat historyDateFormat = LegacyDateFormat.DDMMYY;
    @SerializedName("orderType")
    public LegacyHistoryOrder historyOrder = LegacyHistoryOrder.NEW_FIRST;
//    public int historyLimit = 25;

    // Enable Features
    public boolean enableIncomingTrades = true;
    public boolean enableOutgoingTrades = true;
    public boolean enableItemHighlighter = true;
    @SerializedName("enableMenubar")
    public boolean enableMenuBar = true;

    // Audio
    public LegacySoundElement incomingMessageSound = new LegacySoundElement(LegacySound.PING1, 50);
    public LegacySoundElement outgoingMessageSound = new LegacySoundElement(LegacySound.PING1, 50);
    public LegacySoundElement scannerMessageSound = new LegacySoundElement(LegacySound.PING2, 50);
    public LegacySoundElement playerJoinedSound = new LegacySoundElement(LegacySound.BLIP1, 50);
    public LegacySoundElement updateSound = new LegacySoundElement(LegacySound.BLIP3, 50);

    // Stash Search
    public HotkeyData stashSearchHotkey = null;
    public ArrayList<LegacyStashSearchData> stashSearchData = new ArrayList<>();

    // Cheat Sheets
    public ArrayList<LegacyCheatSheetData> cheatSheetData = new ArrayList<>();

    // Client
    public String clientPath = null;

    public ArrayList<LegacyStashTabData> stashTabs = new ArrayList<>();
    //    public ArrayList<IgnoreData> ignoreData = new ArrayList<>();
    // Custom Macros
    public ArrayList<LegacyMacroButton> incomingMacros = new ArrayList<>();
    public ArrayList<LegacyMacroButton> outgoingMacros = new ArrayList<>();

    // SlimTrade Hotkeys
//    public HotkeyData betrayalHotkey = null;
    public HotkeyData optionsHotkey = null;
    public HotkeyData historyHotkey = null;
    public HotkeyData chatScannerHotkey = null;
    public HotkeyData closeTradeHotkey = null;

    // POE Hotkeys
    public HotkeyData delveHotkey = null;
    @SerializedName("dndHotkey")
    public HotkeyData doNotDisturbHotkey = null;
    @SerializedName("exitHotkey")
    public HotkeyData exitToMenuHotkey = null;
    @SerializedName("guildHotkey")
    public HotkeyData guildHideoutHotkey = null;
    public HotkeyData leavePartyHotkey = null;
    public HotkeyData menagerieHotkey = null;
    public HotkeyData metamorphHotkey = null;
    @SerializedName("remainingHotkey")
    public HotkeyData remainingMonstersHotkey = null;
    public HotkeyData hideoutHotkey = null;

    // Cheat Sheet Data
    protected static class LegacyCheatSheetData {
        public String fileName;
        public String cleanName;
        public HotkeyData hotkeyData;

        public CheatSheetData toCheatSheetData() {
            Path path = Paths.get(fileName);
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) return null;
            return new CheatSheetData(file.getName(), hotkeyData);
        }
    }

    // Color Theme
    protected enum LegacyColorTheme {
        SOLARIZED_LIGHT(Theme.SOLARIZED_LIGHT),
        SOLARIZED_DARK(Theme.SOLARIZED_DARK),
        STORMY(Theme.NORD),
        MONOKAI(Theme.MONOCAI);

        public final Theme theme;

        LegacyColorTheme(Theme theme) {
            this.theme = theme;
        }
    }

    // History
    @SuppressWarnings("SpellCheckingInspection")
    protected enum LegacyDateFormat {
        DDMM(DateFormat.DD_MM),
        DDMMYY(DateFormat.DD_MM_YY),
        MMDD(DateFormat.MM_DD),
        MMDDYY(DateFormat.MM_DD_YY),
        YYMMDD(DateFormat.YY_MM_DD);

        public final DateFormat format;

        LegacyDateFormat(DateFormat format) {
            this.format = format;
        }
    }

    protected enum LegacyHistoryOrder {
        NEW_FIRST(HistoryOrder.NEWEST_FIRST),
        NEW_LAST(HistoryOrder.NEWEST_LAST);

        public final HistoryOrder order;

        LegacyHistoryOrder(HistoryOrder order) {
            this.order = order;
        }
    }

    // Macro Buttons
    protected static class LegacyMacroButton {
        public LegacyButtonRow row;
        public String leftMouseResponse;
        public String rightMouseResponse;
        public CustomIcon image;
        public HotkeyData hotkeyData;
        public boolean closeOnClick;

        public MacroButton toMacroButton() {
            MacroButton macroButton = new MacroButton(image, leftMouseResponse, rightMouseResponse, row.row, hotkeyData, closeOnClick);
            macroButton.hotkeyData = hotkeyData;
            return macroButton;
        }
    }

    protected enum LegacyButtonRow {
        TOP(ButtonRow.TOP_ROW),
        BOTTOM(ButtonRow.BOTTOM_ROW);

        public final ButtonRow row;

        LegacyButtonRow(ButtonRow row) {
            this.row = row;
        }
    }

    // Sounds
    protected static class LegacySoundElement {
        public LegacySound sound;
        public int volume;

        public LegacySoundElement(LegacySound sound, int volume) {
            this.sound = sound;
            this.volume = volume;
        }

        public SoundComponent toSoundComponent() {
            return new SoundComponent(new Sound(sound.toString(), Sound.SoundType.INBUILT), volume);
        }
    }

    private enum LegacySound {
        PING1("Ping 1"),
        PING2("Ping 2"),
        BLIP1("Blip 1"),
        BLIP2("Blip 2"),
        BLIP3("Blip 3");

        private final String name;

        LegacySound(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Stash Search
    protected static class LegacyStashSearchData {
        public String searchName;
        public String searchTerms;
        public LegacyStashTabColor color;

        public StashSearchTermData toTermData() {
            return new StashSearchTermData(searchName, searchName, color.ordinal());
        }
    }

    // Stash Tabs
    protected static class LegacyStashTabData {
        public String name;
        public StashTabType type;
        public LegacyStashTabColor color;

        public StashTabData toStashTabData() {
            return new StashTabData(name, MatchType.EXACT_MATCH, type, color.ordinal());
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private enum LegacyStashTabColor {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE,
        THIRTEEN, FOURTEEN, FIFTEEN, SIXTEEN, SEVENTEEN, EIGHTEEN, NINETEEN, TWENTY,
        TWENTYONE, TWENTYTWO, TWENTYTRHEE, TWENTYFOUR, TWENTYFIVE, TWENTYSIX, TWENTYSEVEN,
    }

}