package org.csdg8.game.blackjack.model;

public class Card {
    private final CardRank rank;
    private final CardSuit suit;

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