package org.csdg8.game.blackjack.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardRank {
    ZERO("0"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"),
    EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

    private final String displayName;

    CardRank(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}

