package com.poker.poker.gamelobby.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GameLobby {
    @Id
    private Long id;
    private String lobbyName;
    private Integer playerCount;

    @ElementCollection
    private Set<String> players;

    public GameLobby() {
        this.playerCount = 0;
        this.players = new HashSet<>();
    }

    public GameLobby(String lobbyName) {
        this.lobbyName = lobbyName;
        this.playerCount = 0;
        this.players = new HashSet<>();
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

    public Set<String> getPlayers() {
        return players;
    }

    public void addPlayer(String playerName) {
        players.add(playerName);
        playerCount = players.size();
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
        playerCount = players.size();
    }


}

