package com.slimtrade.core.data;

public class PasteReplacement {

    public final String message;
    public final String playerName;
    public final String itemName;
    public final int itemQuantity;
    public final String priceName;
    public final double priceQuantity;

    public PasteReplacement(String player) {
        this.message = "";
        this.playerName = player;
        this.itemName = "";
        this.itemQuantity = 0;
        this.priceName = "";
        this.priceQuantity = 0;
    }

    public PasteReplacement(ReplacementParams params) {
        this.message = params.message;
        this.playerName = params.playerName;
        this.itemName = params.itemName;
        this.itemQuantity = params.itemQuantity;
        this.priceName = params.priceName;
        this.priceQuantity = params.priceQuantity;
    }

    public static class ReplacementParams {
        private String message;
        private String playerName;
        private String itemName;
        private int itemQuantity;
        private String priceName;
        private double priceQuantity;

        public ReplacementParams setMessage(String message) {
            this.message = message;
            return this;
        }

        public ReplacementParams setPlayerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public ReplacementParams setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public ReplacementParams setItemQuantity(int itemQuantity) {
            this.itemQuantity = itemQuantity;
            return this;
        }

        public ReplacementParams setPriceName(String priceName) {
            this.priceName = priceName;
            return this;
        }

        public ReplacementParams setPriceQuantity(double priceQuantity) {
            this.priceQuantity = priceQuantity;
            return this;
        }
    }
}
