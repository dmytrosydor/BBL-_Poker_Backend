package com.poker.poker.game.model;

import java.util.ArrayList;
import java.util.List;

public class PlayerInGame {
    private final Player player;
    private int balance; // Баланс гравця
    private List<Card> hand; // Карти, які має гравець

    public PlayerInGame(Player player, int balance) {
        this.player = player;
        this.balance = balance;
        this.hand = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCards(Card card) {
        this.hand.add(card);
    }
}

