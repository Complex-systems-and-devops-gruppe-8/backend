package org.csdg8.game.blackjack.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
 

@Converter
public class DeckConverter implements AttributeConverter<Deck, String> {

    @Override
    public String convertToDatabaseColumn(Deck deck) {
        // Convert each card to a compact string representation (rank and suit)
        return deck.getCards().stream()
            .map(card -> card.getRank().name() + " " + card.getSuit().name()) // Example: "2 HEARTS"
            .collect(Collectors.joining(","));
    }

    @Override
    public Deck convertToEntityAttribute(String dbData) {
        // Split the compact string and reconstruct the Deck
        List<Card> cards = Arrays.stream(dbData.split(","))
            .map(this::convertToCard)
            .collect(Collectors.toList());

        Deck deck = new Deck();
        deck.getCards().clear();
        deck.getCards().addAll(cards);
        return deck;
    }

    // Helper method to convert a compact string to a Card object
    private Card convertToCard(String cardData) {
        String[] parts = cardData.split(" ");
        CardRank rank = CardRank.valueOf(parts[0]); // Parse rank
        CardSuit suit = CardSuit.valueOf(parts[1]); // Parse suit
        return new Card(rank, suit);
    }
}