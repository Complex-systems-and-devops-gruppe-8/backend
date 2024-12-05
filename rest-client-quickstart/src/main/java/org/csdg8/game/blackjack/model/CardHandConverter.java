package org.csdg8.game.blackjack.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CardHandConverter implements AttributeConverter<CardHand, String> {

    @Override
    public String convertToDatabaseColumn(CardHand hand) {
        // Convert each card to a compact string representation (rank and suit)
        return hand.getCards().stream()
            .map(card -> card.getRank().name() + " " + card.getSuit().name()) // Example: "2 HEARTS"
            .collect(Collectors.joining(","));
    }

    @Override
    public CardHand convertToEntityAttribute(String dbData) {
        // Split the compact string and reconstruct the CardHand
        List<Card> cards = Arrays.stream(dbData.split(","))
            .map(this::convertToCard)
            .collect(Collectors.toList());

        CardHand hand = new CardHand();
        cards.forEach(hand::addCard);
        return hand;
    }

    // Helper method to convert a compact string to a Card object
    private Card convertToCard(String cardData) {
        String[] parts = cardData.split(" ");
        CardRank rank = CardRank.valueOf(parts[0]); // Parse rank
        CardSuit suit = CardSuit.valueOf(parts[1]); // Parse suit
        return new Card(rank, suit);
    }
}
