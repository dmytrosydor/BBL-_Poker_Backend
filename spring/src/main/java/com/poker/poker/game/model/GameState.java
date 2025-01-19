package com.poker.poker.game.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameState {
    private UUID gameId;
    private List<Player> players;
    private GamePhase phase;
    private Deck deck;
    private Map<UUID, List<Card>> playerHands;
    private Map<UUID, Integer> playerBets;
    private int pot;

    public GameState(UUID gameId, List<Player> players, GamePhase phase, Deck deck) {
        this.gameId = gameId;
        this.players = players;
        this.phase = phase;
        this.deck = deck;
        this.playerHands = new HashMap<>();
        this.playerBets = new HashMap<>();
        this.pot = 0;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Map<UUID, List<Card>> getPlayerHands() {
        return playerHands;
    }

    public Map<UUID, Integer> getPlayerBets() {
        return playerBets;
    }

    public int getPot() {
        return pot;
    }

    public void addToPot(int amount) {
        this.pot += amount;
    }



    private Map<UUID, PlayerAction> playerActions = new HashMap<>();

    public void updatePlayerAction(UUID playerId, PlayerAction action) {
        this.playerActions.put(playerId, action);
    }

}
