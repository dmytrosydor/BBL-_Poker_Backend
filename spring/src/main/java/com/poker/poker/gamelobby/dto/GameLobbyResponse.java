package com.poker.poker.gamelobby.dto;

import java.util.Set;

public class GameLobbyResponse {
    private Long id;
    private String lobbyName;
    private Integer playerCount;
    private Set<String> players;

    public GameLobbyResponse(Long id, String lobbyName, Integer playerCount, Set<String> players) {
        this.id = id;
        this.lobbyName = lobbyName;
        this.playerCount = playerCount;
        this.players = players;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }
}
