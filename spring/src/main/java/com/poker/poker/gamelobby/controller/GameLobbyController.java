package com.poker.poker.gamelobby.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.poker.poker.gamelobby.service.GameLobbyService; // For the service layer
import com.poker.poker.gamelobby.dto.CreateLobbyRequest;   // For the DTO
import com.poker.poker.gamelobby.entity.GameLobby;
import com.poker.poker.gamelobby.dto.GameLobbyResponse;

@RestController
@RequestMapping("/api/games")
public class GameLobbyController {
    private final GameLobbyService service;

    public GameLobbyController(GameLobbyService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<GameLobby> createGameLobby(@RequestBody CreateLobbyRequest request) {
        GameLobby createdLobby = service.createLobby(request.getLobbyName());
        return ResponseEntity.ok(createdLobby);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GameLobbyResponse>> getAllGameLobbies() {
        List<GameLobbyResponse> lobbies = service.getAllLobbies();
        return ResponseEntity.ok(lobbies);
    }

    @PostMapping("/join/{lobbyId}")
    public ResponseEntity<GameLobby> joinLobby(@PathVariable Long lobbyId, @RequestParam String playerName) {
        GameLobby updatedLobby = service.joinLobby(lobbyId, playerName);
        return ResponseEntity.ok(updatedLobby);
    }

    @PostMapping("/leave/{lobbyId}")
    public ResponseEntity<GameLobby> leaveLobby(@PathVariable Long lobbyId, @RequestParam String playerName) {
        GameLobby updatedLobby = service.leaveLobby(lobbyId, playerName);
        return ResponseEntity.ok(updatedLobby);
    }
}
