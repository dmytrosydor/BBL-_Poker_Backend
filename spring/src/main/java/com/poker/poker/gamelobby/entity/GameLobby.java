package com.poker.poker.gamelobby.entity;

import com.poker.poker.game.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {
    private Long id;
    private String lobbyName;
    private Integer playerCount;
    private List<Player> players;

    public GameLobby() {
        this.playerCount = 0;
    }

    public GameLobby(String lobbyName) {
        this.lobbyName = lobbyName;
        this.playerCount = 0;
        this.players = new ArrayList<Player>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }
}

