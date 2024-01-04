package com.slimtrade.gui.options.searching;

import com.slimtrade.core.hotkeys.HotkeyData;

import java.util.ArrayList;

public class StashSearchGroupData {

    public static final String PIN_PREFIX = "SearchWindow:";

    public final int id;
    public final String title;
    public final HotkeyData hotkeyData;
    public final ArrayList<StashSearchTermData> terms;

    public StashSearchGroupData(int id, String title, HotkeyData hotkeyData, ArrayList<StashSearchTermData> terms) {
        this.id = id;
        this.title = title;
        this.hotkeyData = hotkeyData;
        this.terms = terms;
    }

    public String getPinTitle() {
        return PIN_PREFIX + id;
    }

    @Override
    public String toString() {
        return title;
    }

}
