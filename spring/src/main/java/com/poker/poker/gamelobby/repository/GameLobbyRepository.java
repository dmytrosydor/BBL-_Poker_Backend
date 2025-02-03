package com.poker.poker.gamelobby.repository;

import com.poker.poker.gamelobby.entity.GameLobby;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class GameLobbyRepository {
    private final List<GameLobby> lobbies = new ArrayList<GameLobby>();
    private long nextId = 1;

    public GameLobby save(GameLobby lobby) {
        if (lobby.getId() == null) {
            lobby.setId(nextId++);
            lobbies.add(lobby);
        } else {
            for (int i = 0; i < lobbies.size(); i++) {
                if (lobbies.get(i).getId().equals(lobby.getId())) {
                    lobbies.set(i, lobby);
                    break;
                }
            }
        }

        return lobby;
    }

    public List<GameLobby> findAll() {
        return new ArrayList<>(lobbies);
    }

    public Optional<GameLobby> findById(Long id) {
        return lobbies.stream().filter(lobby -> lobby.getId().equals(id)).findFirst();
    }
}

