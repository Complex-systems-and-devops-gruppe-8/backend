package org.csdg8.game.blackjack.model;


public enum CardSuit {
    HEARTS("Hearts"), TILES("Tiles"), PIKES("Pikes"), CLOVERS("Clovers"), BLANK("Blank");

    private final String displayName;

    CardSuit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}