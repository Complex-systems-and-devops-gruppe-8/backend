package org.csdg8.game.blackjack.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        for (CardRank rank : CardRank.values()) {
            for (CardSuit suit : CardSuit.values()) {
                if (!suit.equals(CardSuit.BLANK)) { // Exclude "Blank" for standard cards
                    cards.add(new Card(rank, suit));
                }
            }
        }
         
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    public List<Card> getCards() {
        return cards;
    }
}
