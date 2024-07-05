package com.slimtrade.core.saving.legacy.savefiles;

import com.google.gson.annotations.SerializedName;
import com.slimtrade.core.enums.*;
import com.slimtrade.core.hotkeys.HotkeyData;

import java.util.ArrayList;

public class LegacySettingsSaveAdvanced {

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


    // Enable Features
    public boolean enableIncomingTrades = true;
    public boolean enableOutgoingTrades = true;
    public boolean enableItemHighlighter = true;
    @SerializedName("enableMenubar")
    public boolean enableMenuBar = true;


    // Stash Search
    public HotkeyData stashSearchHotkey = null;
    public ArrayList<LegacySettingsSave0.LegacyStashSearchData> stashSearchData = new ArrayList<>();

    // Cheat Sheets
    public ArrayList<LegacySettingsSave0.LegacyCheatSheetData> cheatSheetData = new ArrayList<>();

    // Stash Tabs
    public ArrayList<LegacySettingsSave0.LegacyStashTabData> stashTabs = new ArrayList<>();

    // Ignore Item
    public ArrayList<LegacySettingsSave0.LegacyIgnoreData> ignoreData = new ArrayList<>();
}

