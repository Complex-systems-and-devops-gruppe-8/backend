package org.csdg8.game.blackjack.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Card {

    private CardRank rank; // Removed final
    private CardSuit suit; // Removed final

    // No-argument constructor required by Hibernate
    protected Card() {
    }

    // Constructor for application use
    public Card(CardRank rank, CardSuit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public CardRank getRank() {
        return rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank.getDisplayName() + " of " + suit.getDisplayName();
    }
}
