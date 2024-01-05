package com.slimtrade.gui.history;

import com.slimtrade.core.data.SaleItemWrapper;
import com.slimtrade.gui.components.CurrencyLabelFactory;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ItemCellRenderer extends JLabel implements TableCellRenderer {

    public ItemCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
        CurrencyLabelFactory.applyItemToComponent(this, ((SaleItemWrapper) value).items);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }

}
