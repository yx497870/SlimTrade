package com.slimtrade.gui.options.ignore;

import com.slimtrade.core.enums.MatchType;
import com.slimtrade.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public class IgnoreInputPanel extends JPanel {

    private JButton ignoreButton = new JButton("Ignore Item");
    private JTextField itemNameInput = new JTextField(20);
    private JComboBox<MatchType> matchTypeCombo = new JComboBox<>();
    private JSpinner timeSpinner = new JSpinner();

    public IgnoreInputPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();

        for (MatchType matchType : MatchType.values()) matchTypeCombo.addItem(matchType);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
        spinnerModel.setMinimum(0);
        spinnerModel.setMaximum(300);
        spinnerModel.setStepSize(10);
        spinnerModel.setValue(60);
        timeSpinner.setModel(spinnerModel);
        ((JSpinner.DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);

        gc.gridx = 1;
        add(new JLabel("Item Name"), gc);
        gc.gridx++;
        add(new JLabel("Match"), gc);
        gc.gridx++;
        add(new JLabel("Minutes"), gc);
        gc.gridx = 0;
        gc.gridy++;

        add(ignoreButton, gc);
        gc.gridx++;
        add(itemNameInput, gc);
        gc.gridx++;
        add(matchTypeCombo, gc);
        gc.gridx++;
        add(timeSpinner, gc);
        gc.gridx++;
    }

}