package com.poker.poker.game.model;

import java.util.List;

public class PlayerBestHand {
    private Combination combination;
    private List<Card> cards;

    public PlayerBestHand(Combination combination, List<Card> cards) {
        this.combination = combination;
        this.cards = cards;
    }

    public Combination getCombination() {
        return combination;
    }

    public List<Card> getCards() {
        return cards;
    }
}
