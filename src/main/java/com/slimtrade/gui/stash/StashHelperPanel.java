package com.slimtrade.gui.stash;

import com.slimtrade.core.enums.StashTabColor;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.utility.AdvancedMouseListener;
import com.slimtrade.core.utility.PoeInterface;
import com.slimtrade.core.utility.TradeUtil;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.modules.colortheme.AdvancedButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StashHelperPanel extends AdvancedButton {

    //    private StashHelperContainer parent;
    private JFrame highlighter = new JFrame();
    private TradeOffer tradeOffer;

    public StashHelperPanel(StashHelperContainer parent, TradeOffer offer, StashTabColor color) {

        tradeOffer = offer;
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(new GridBagLayout());
        JLabel tabLabel = new JLabel(offer.stashtabName);
        JLabel itemLabel = new JLabel(offer.itemName);

        GridBagConstraints gc = ZUtil.getGC();
        add(tabLabel, gc);
        gc.gridy++;
        setBackground(color.getBackground());
        tabLabel.setForeground(color.getForeground());
        itemLabel.setForeground(color.getForeground());

        int insetHorizontal = 6;
        int insetVertical = 4;
        Border innerBorder = BorderFactory.createEmptyBorder(insetVertical, insetHorizontal, insetVertical, insetHorizontal);
        Border outerBorder = BorderFactory.createLineBorder(color.getForeground(), 2);
        Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        setBorder(compoundBorder);

        JButton self = this;
        addMouseListener(new AdvancedMouseListener() {
            @Override
            public void click(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    PoeInterface.findInStash(TradeUtil.cleanItemName(tradeOffer.itemName));
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    parent.remove(self);
                    parent.refresh();
                }
            }
        });
    }

}