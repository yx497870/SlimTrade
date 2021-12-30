package com.slimtrade.gui.options;

import com.slimtrade.core.enums.ButtonRow;
import com.slimtrade.core.enums.CustomIcon;
import com.slimtrade.core.utility.MacroButton;
import com.slimtrade.gui.buttons.IconButton;
import com.slimtrade.gui.options.general.GridBagPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MacroCustomizerPanel extends GridBagPanel {

    private int textFieldWidth = 20;

    // Macro Button
    JComboBox<ImageIcon> iconCombo = new JComboBox<>();
    JTextField buttonText = new JTextField();

    JTextField lmbInput = new JTextField(textFieldWidth);
    JTextField rmbInput = new JTextField(textFieldWidth);
    JCheckBox closeCheckbox = new JCheckBox();
    JComboBox<ButtonRow> rowCombo = new JComboBox<>();
    JButton hotkeyButton = new JButton("[Hotkey]");
    JComboBox<MacroButton.MacroButtonType> buttonType = new JComboBox<>();

    // Internal
    JPanel parent;
    JButton shiftUpButton = new IconButton();
    JButton shiftDownButton = new IconButton();
    JButton deleteButton = new IconButton();

    public MacroCustomizerPanel(JPanel parent) {
        this.parent = parent;
        // Labels
        JLabel lmbLabel = new JLabel("LMB");
        JLabel rmbLabel = new JLabel("RMB");
        JLabel closeLabel = new JLabel("Close");
        buttonText.setText("~");

        // Card Type Panel
        CardLayout displayLayout = new CardLayout();
        JPanel displayPanel = new JPanel(displayLayout);
        displayPanel.add(iconCombo, MacroButton.MacroButtonType.ICON.toString());
        displayPanel.add(buttonText, MacroButton.MacroButtonType.TEXT.toString());

        gc.insets = new Insets(0, 5, 0, 5);

        // Add items to combos
        for (ButtonRow row : ButtonRow.values())
            rowCombo.addItem(row);
        for (CustomIcon icon : CustomIcon.values())
            iconCombo.addItem(icon.getColorIcon(UIManager.getColor("Label.foreground"), 18));
        for (MacroButton.MacroButtonType type : MacroButton.MacroButtonType.values())
            buttonType.addItem(type);

        // Shift Buttons
        add(shiftUpButton, gc);
        gc.gridy++;
        add(shiftDownButton, gc);
        gc.gridx++;
        gc.gridy = 0;

        //Labels
        add(lmbLabel, gc);
        gc.gridy++;
        add(rmbLabel, gc);
        gc.gridx++;
        gc.gridy = 0;

        // Response Input
        add(lmbInput, gc);
        gc.gridy++;
        add(rmbInput, gc);
        gc.gridx++;
        gc.gridy = 0;

        // Icon  + Row
//        gc.fill = GridBagConstraints.HORIZONTAL;
//        gc.weightx = 1;
//        add(iconCombo, gc);
//        gc.gridy++;
//        add(rowCombo, gc);
//        gc.gridx++;
//        gc.gridy = 0;

        // Icon  + Row

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        add(buttonType, gc);
        gc.gridy++;
        add(displayPanel, gc);
        gc.gridx++;
        gc.gridy = 0;

        add(rowCombo);
        gc.gridx++;
        gc.gridy = 0;
//        gc.gridx++;
//        gc.gridy = 0;

        // Close
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        add(closeLabel, gc);
        gc.gridy++;
        add(closeCheckbox, gc);
        gc.gridx++;
        gc.gridy = 0;

        // Delete Button
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(deleteButton);
        buttonType.addActionListener(e -> {
            if (buttonType.getSelectedItem() == null) return;
            displayLayout.show(displayPanel, Objects.requireNonNull(buttonType.getSelectedItem()).toString());
        });

        JPanel self = this;
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.remove(self);
                parent.revalidate();
            }
        });

    }

    public MacroButton getMacroButton() {
        MacroButton button = new MacroButton();
        button.lmbResponse = lmbInput.getText();
        button.rmbResponse = rmbInput.getText();
        button.row = (ButtonRow) rowCombo.getSelectedItem();
        button.buttonType = (MacroButton.MacroButtonType) buttonType.getSelectedItem();
        button.text = buttonText.getText();
        button.close = closeCheckbox.isSelected();
        int index = iconCombo.getSelectedIndex();
        if (index == -1) index = 0;
        button.icon = CustomIcon.values()[index];
        return button;
    }

    public void setMacro(MacroButton macro) {
        lmbInput.setText(macro.lmbResponse);
        rmbInput.setText(macro.rmbResponse);
        rowCombo.setSelectedItem(macro.row);
        buttonType.setSelectedItem(macro.buttonType);
        buttonText.setText(macro.text);
        iconCombo.setSelectedIndex(macro.icon.ordinal());
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (iconCombo != null) {
            iconCombo.removeAllItems();
            for (CustomIcon icon : CustomIcon.values())
//                iconCombo.addItem(new ImageIcon(icon.getColorImage(UIManager.getColor("Label.foreground")).getScaledInstance(18,18, Image.SCALE_SMOOTH)));
                iconCombo.addItem(icon.getColorIcon(UIManager.getColor("Label.foreground"), 20));
        }
        setBorder(BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground")));
    }
}