package org.csdg8.game.blackjack.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embeddable;

@Embeddable
public class CardHand {

    
    private final List<Card> cards = new ArrayList<>();

    public CardHand() {
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }
    public void removeBlankCard() {
       for(Card card : cards) {
           if(card.getSuit().equals(CardSuit.BLANK)) {
               cards.remove(card);
               break;
           }
       }
    }

    public List<Card> getCards() {
        return cards;
    }
    public void printHand() {
        for(Card card : cards) {
            System.out.println(card.toString());
        }
    }
    public int calculateHandValue() {
        int score = 0;
        int aces = 0;

        // Iterate over the cards to calculate score
        for (Card card : cards) {
            CardRank rank = card.getRank();
            if (CardRank.ACE.equals(rank)) {
                aces++; // Count Aces separately
            } else if (CardRank.JACK.equals(rank) || CardRank.QUEEN.equals(rank) || CardRank.KING.equals(rank)) {
                score += 10; // Face cards (J, Q, K) are worth 10 points
            } else {
                try {
                    score += Integer.parseInt(rank.getDisplayName()); // Add numeric rank values
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid card rank: " + rank);
                }
            }
        }

        // Handle Aces
        score += aces; // Count all Aces as 1 point first
        for (int i = 0; i < aces; i++) {
            if (score + 10 <= 21) {
                score += 10; // Treat this Ace as 11 if it doesn't bust
            }
        }

        return score;
    }
}