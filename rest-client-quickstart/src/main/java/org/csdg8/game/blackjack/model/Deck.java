package org.csdg8.game.blackjack.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (CardRank rank : CardRank.values()) {
            for (CardSuit suit : CardSuit.values()) {
                if (!suit.equals(CardSuit.BLANK) && !rank.equals(CardRank.ZERO) ) { // Exclude "Blank" for standard cards
                    cards.add(new Card(rank, suit));
                }
            }
        }
         
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    public List<Card> getCards() {
        return cards;
    }
    public Card getHiddenCard() {
        return new Card(CardRank.ZERO, CardSuit.BLANK);
    }
}
