package org.csdg8.game.blackjack.model;
 
import java.util.ArrayList;
import java.util.List;

public class CardHand {
    private int handValue;
    private List<Card> cards = new ArrayList<>();

    public CardHand() {
        this.handValue = 0;
    }

    public void addCard(Card card) {
        this.cards.add(card);
       //add method to calculate hand value
       calculateHandValue();
    }
    private void calculateHandValue() {
       //add method to calculate hand value
       this.handValue = 0;
    }
}
