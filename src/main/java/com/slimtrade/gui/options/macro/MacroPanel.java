package com.slimtrade.gui.options.macro;

import com.slimtrade.App;
import com.slimtrade.core.managers.ColorManager;
import com.slimtrade.core.saving.MacroButton;
import com.slimtrade.core.utility.TradeOffer;
import com.slimtrade.enums.MessageType;
import com.slimtrade.gui.basic.SectionHeader;
import com.slimtrade.gui.buttons.BasicButton;
import com.slimtrade.gui.components.AddRemovePanel;
import com.slimtrade.gui.custom.CustomLabel;
import com.slimtrade.gui.enums.DefaultIcons;
import com.slimtrade.gui.messaging.MessageDialogManager;
import com.slimtrade.gui.messaging.MessagePanel;
import com.slimtrade.gui.options.ISaveable;
import com.slimtrade.gui.panels.ContainerPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MacroPanel extends ContainerPanel implements ISaveable {

    // Info
    private JLabel info1 = new CustomLabel("Send whispers or commands using {player}, {self}, {item}, and {price} as variables.");
    private JLabel info2 = new CustomLabel("Hotkeys use the left click of the oldest trade. Use escape to clear a hotkey.");
    private JLabel info3 = new CustomLabel("Plain messages have @{player} added automatically. Check the box to close trade.");
//    private JLabel info4 = new CustomLabel("");

    private SectionHeader tradeHeader = new SectionHeader("Example");

    private JPanel messageWrapper = new JPanel(new GridBagLayout());
    private MessagePanel messagePanel;

    public AddRemovePanel addRemovePanel = new AddRemovePanel();
    public JButton addButton = new BasicButton("Add New Macro");

    // Internal
    private MessageType messageType;
    PresetMacroRow closeMacro = null;

    public MacroPanel(MessageType messageType) {
        this.messageType = messageType;
        if (messageType == MessageType.UNKNOWN) {
            return;
        }
        this.setVisible(false);
        int spacer = 5;

        // Username
        PresetMacroRow usernameMacro = new PresetMacroRow("Username");
        usernameMacro.setLMB("/whois {player}");
        usernameMacro.setRMB("Open empty whisper with {player}");
        usernameMacro.buildPanel();
        PresetMacroRow itemMacro = null;
        if (messageType == MessageType.INCOMING_TRADE) {
            itemMacro = new PresetMacroRow("Item Name");
            itemMacro.setLMB("Open Stash Highlighter");
            itemMacro.setRMB("Ignore Item");
            itemMacro.buildPanel();
            tradeHeader.setText("Incoming Trade");
        } else if (messageType == MessageType.OUTGOING_TRADE) {
            tradeHeader.setText("Outgoing Trade");
        } else if (messageType == MessageType.CHAT_SCANNER) {
            tradeHeader.setText("Scanner Message");
        }

        // Close Button
        if (messageType == MessageType.INCOMING_TRADE || messageType == MessageType.OUTGOING_TRADE) {
            closeMacro = new PresetMacroRow(DefaultIcons.CLOSE);
            closeMacro.setLMB("Close Trade");
            if (messageType == MessageType.INCOMING_TRADE) {
                closeMacro.setRMB("Close Trade + All similar incoming trades");
            } else {
                closeMacro.setRMB("Keep Trade + Close all other outgoing trades");
            }
            closeMacro.setAddHotkey(true);
            closeMacro.buildPanel();
        }


        gc.gridy++;
        container.add(tradeHeader, gc);
        gc.insets.top = spacer;
        gc.gridy++;

        container.add(messageWrapper, gc);
        gc.gridy++;

        container.add(new SectionHeader("Preset Macros"), gc);
        gc.gridy++;
        container.add(usernameMacro, gc);
        gc.gridy++;
        if (messageType == MessageType.INCOMING_TRADE) {
            container.add(itemMacro, gc);
            gc.gridy++;
        }
        if (messageType == MessageType.INCOMING_TRADE || messageType == MessageType.OUTGOING_TRADE) {
            container.add(closeMacro, gc);
            gc.gridy++;
        }

        container.add(new SectionHeader("Custom Macros"), gc);
        gc.gridy++;
        container.add(info1, gc);
        gc.insets.top = 0;
        gc.gridy++;
        container.add(info2, gc);
        gc.gridy++;
        container.add(info3, gc);
        gc.gridy++;
//        container.add(info4, gc);
//        gc.gridy++;
        gc.insets.top = spacer;
        container.add(addButton, gc);
        gc.gridy++;
        container.add(addRemovePanel, gc);

        TradeOffer t = new TradeOffer("", "", messageType, "<GLD>", "PlayerName123", "Item Name", 1, "chaos", 60, "sale", 1, 1, "", "");
        MessagePanel msg = new MessagePanel(t, MessageDialogManager.DEFAULT_SIZE, false);
        msg.stopTimer();
        setExampleMessage(msg);

        addButton.addActionListener(e -> {
            MacroCustomizerRow row = new MacroCustomizerRow();
            addRemovePanel.addRemoveablePanel(row);
            ColorManager.setTheme(ColorManager.getCurrentColorTheme());
            App.eventManager.recursiveColor(row);
            row.upArrowButton.addActionListener(e1 -> addRemovePanel.shiftUp(row));
            row.downArrowButton.addActionListener(e1 -> addRemovePanel.shiftDown(row));
        });

    }

    public void setExampleMessage(MessagePanel panel) {
        messageWrapper.removeAll();
        messagePanel = panel;
        messageWrapper.add(panel, new GridBagConstraints());
        resizeMessage();
//        panel.repaint();
    }

    public void resizeMessage() {
        messagePanel.resizeFrames(new Dimension(MessageDialogManager.DEFAULT_SIZE.width + App.saveManager.overlaySaveFile.messageSizeIncrease, MessageDialogManager.DEFAULT_SIZE.height + App.saveManager.overlaySaveFile.messageSizeIncrease), false);
    }

    @Override
    public void save() {
        addRemovePanel.clearHiddenPanels();
        ArrayList<MacroButton> buttons = new ArrayList<>();
        for (Component c : addRemovePanel.getComponents()) {
            if (c instanceof MacroCustomizerRow) {
                buttons.add(((MacroCustomizerRow) c).getMacroData());
            }
        }
        MacroButton[] macros = buttons.toArray(new MacroButton[0]);
        switch (messageType) {
            case INCOMING_TRADE:
                App.saveManager.saveFile.incomingMacros = macros;
                App.saveManager.saveFile.closeIncomingHotkey = closeMacro.hotkeyInputPane.getHotkeyData();
                break;
            case OUTGOING_TRADE:
                App.saveManager.saveFile.outgoingMacros = macros;
                App.saveManager.saveFile.closeOutoingHotkey = closeMacro.hotkeyInputPane.getHotkeyData();
                break;
            case CHAT_SCANNER:
                break;
            case UNKNOWN:
                break;
        }
        messagePanel.resizeFrames(MessageDialogManager.getMessageSize());
        this.revalidate();
        this.repaint();
    }

    @Override
    public void load() {
        MacroButton[] macros = null;
        switch (messageType) {
            case INCOMING_TRADE:
                macros = App.saveManager.saveFile.incomingMacros;
                closeMacro.hotkeyInputPane.updateHotkey(App.saveManager.saveFile.closeIncomingHotkey);
                break;
            case OUTGOING_TRADE:
                macros = App.saveManager.saveFile.outgoingMacros;
                closeMacro.hotkeyInputPane.updateHotkey(App.saveManager.saveFile.closeOutoingHotkey);
                break;
            case CHAT_SCANNER:
                break;
            case UNKNOWN:
                break;
        }
        if (macros == null) return;
        addRemovePanel.removeAll();
        for (MacroButton b : macros) {
            MacroCustomizerRow row = new MacroCustomizerRow(b);
            row.upArrowButton.addActionListener(e1 -> addRemovePanel.shiftUp(row));
            row.downArrowButton.addActionListener(e1 -> addRemovePanel.shiftDown(row));
            addRemovePanel.addRemoveablePanel(row);
        }
        this.revalidate();
        this.repaint();
    }

}
