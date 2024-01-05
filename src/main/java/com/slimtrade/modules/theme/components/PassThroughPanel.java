package com.slimtrade.modules.theme.components;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that will apply certain functions to all child components.
 */

// FIXME : Check if this is actually needed with the UI rework
@Deprecated
public class PassThroughPanel extends JPanel {

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        for (Component c : getComponents()) {
            c.setBackground(bg);
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        for (Component c : getComponents()) {
            c.setForeground(fg);
        }
    }
}
