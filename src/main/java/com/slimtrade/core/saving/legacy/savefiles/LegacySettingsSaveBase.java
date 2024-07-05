package com.slimtrade.core.saving.legacy.savefiles;


import com.google.gson.annotations.SerializedName;
import com.slimtrade.core.hotkeys.HotkeyData;

public class LegacySettingsSaveBase {

    // Version
    @SerializedName("versionNumber")
    public String appVersionString;

    // Basics
    public String characterName = "";
    public boolean showGuildName = false;
    public boolean folderOffset = false;
    public boolean colorBlindMode = false;
    @SerializedName("colorTheme")
    public HotkeyData quickPasteHotkey = null;

    // Client
    public String clientPath = null;

    // Custom Macros
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
}

