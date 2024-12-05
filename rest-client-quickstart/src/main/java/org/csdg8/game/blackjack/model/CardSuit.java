package org.csdg8.game.blackjack.model;

import com.fasterxml.jackson.annotation.JsonValue;


public enum CardSuit {
    HEARTS("Hearts"), TILES("Tiles"), PIKES("Pikes"), CLOVERS("Clovers"), BLANK("Blank");

    private final String displayName;

    CardSuit(String displayName) {
        this.displayName = displayName;
    }
    
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}