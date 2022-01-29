package com.slimtrade.gui.messaging;

import com.slimtrade.App;
import com.slimtrade.core.enums.ButtonRow;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.utility.ColorManager;
import com.slimtrade.core.utility.MacroButton;
import com.slimtrade.gui.basic.ColorLabel;
import com.slimtrade.gui.basic.ColorPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TradeMessagePanel extends NotificationPanel {

    JPanel playerPanel = new ColorPanel("ComboBox.background");
    JPanel pricePanel = new JPanel(new GridBagLayout());

    public TradeMessagePanel(TradeOffer tradeOffer) {
        super();
        this.tradeOffer = tradeOffer;

        // Player Panel
        Random rng = new Random();
        playerPanel.setLayout(new GridBagLayout());
        playerPanel.add(new JLabel(tradeOffer.playerName + ThreadLocalRandom.current().nextInt(0, 100)));

        // Price Panel
        JLabel priceLabel = new ColorLabel("(" + tradeOffer.priceQuantity + ")" + tradeOffer.priceTypeString, "TextArea.background");
        pricePanel.add(priceLabel);

        // Item Panel
        JPanel itemPanel = new JPanel(new GridBagLayout());
        JLabel itemLabel = new JLabel(tradeOffer.itemName);
        itemPanel.add(itemLabel);

        // TODO : This loop is inefficient to do everytime. Should either do some caching or change save format
        ArrayList<MacroButton> topButtons = new ArrayList<>();
        ArrayList<MacroButton> bottomButtons = new ArrayList<>();
        if (tradeOffer.offerType == TradeOffer.TradeOfferType.INCOMING) {
            borderColor = ColorManager.GREEN_SALE;
            pricePanel.setBackground(ColorManager.GREEN_SALE);
            if (App.saveManager.settingsSaveFile.incomingMacroButtons != null) {
                for (MacroButton button : App.saveManager.settingsSaveFile.incomingMacroButtons) {
                    if (button.row == ButtonRow.TOP_ROW)
                        topButtons.add(button);
                    else
                        bottomButtons.add(button);
                }
            }
        } else if (tradeOffer.offerType == TradeOffer.TradeOfferType.OUTGOING) {
            borderColor = ColorManager.RED_SALE;
            pricePanel.setBackground(ColorManager.RED_SALE);
            if (App.saveManager.settingsSaveFile.outgoingMacroButtons != null) {
                for (MacroButton button : App.saveManager.settingsSaveFile.outgoingMacroButtons) {
                    if (button.row == ButtonRow.TOP_ROW)
                        topButtons.add(button);
                    else
                        bottomButtons.add(button);
                }
            }

        }
        topMacros = topButtons.toArray(topMacros);
        bottomMacros = bottomButtons.toArray(bottomMacros);
        addTopPanel(playerPanel, 0.8f);
        addTopPanel(pricePanel, 0.2f);
        addBottomPanel(getTimerPanel(), 0.05f);
        addBottomPanel(itemPanel, 0.95f);
        buildPanel();
    }

    public TradeOffer getTradeOffer() {
        return tradeOffer;
    }

}
