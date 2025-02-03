package com.poker.poker.game.service;

import com.poker.poker.game.model.*;
import com.poker.poker.notifications.entities.GameEvent;
import com.poker.poker.notifications.entities.LobbyUpdate;
import org.springframework.stereotype.Service;
import com.poker.poker.game.dto.PlayerActionRequest;
import com.poker.poker.game.dto.PlayerActionResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<UUID, GameState> gameStore = new ConcurrentHashMap<>();

    public GameState createGame(List<PlayerInGame> players) {
        UUID gameId = UUID.randomUUID();
        Deck deck = new Deck();
        deck.shuffle();
        List<Card> cards = new ArrayList<>();

        GameState gameState = new GameState(gameId, players, GamePhase.WAITING, deck, cards);

        gameStore.put(gameId, gameState);

        return gameState;
    }

    public Optional<GamePhase> getGamePhaseById(UUID gameId) {
        return Optional.ofNullable(gameStore.get(gameId))
                .map(GameState::getPhase);
    }

    public boolean updateGameState(UUID gameId, GameState updatedState) {
        if (gameStore.containsKey(gameId)) {
            GameState existingState = gameStore.get(gameId);

            existingState.setPhase(updatedState.getPhase());

            gameStore.put(gameId, existingState);
            return true;
        }
        return false;
    }

    public GameEvent getLastEvent(Long lobbyId) {
        return new GameEvent();
    }

    public PlayerActionResponse processPlayerAction(PlayerActionRequest request) {
        UUID gameId = request.getGameId();
        UUID playerId = request.getPlayerId();
        PlayerAction action = request.getAction();

        GameState gameState = gameStore.get(gameId);
        if (gameState == null) {
            return new PlayerActionResponse(false, "Game not found", null);
        }

        Optional<PlayerInGame> playerOptional = gameState.getPlayers().stream()
                .filter(player -> player.getPlayer().getId().equals(playerId))
                .findFirst();

        if (playerOptional.isEmpty()) {
            return new PlayerActionResponse(false, "Player not found", gameState);
        }

        PlayerInGame player = playerOptional.get();

        switch (action) {
            case PLACE_BET:
                return handlePlaceBet(gameState, player, request.getAmount());
            case FOLD:
                return handleFold(gameState, player);
            case DRAW_CARD:
                return handleDrawCard(gameState, player);
            default:
                return new PlayerActionResponse(false, "Invalid action", gameState);
        }
    }
    // Обробка ставки
    private PlayerActionResponse handlePlaceBet(GameState gameState, PlayerInGame player, int amount) {
        if (amount > player.getBalance()) {
            return new PlayerActionResponse(false, "Insufficient balance", gameState);
        }

        player.setBalance(player.getBalance() - amount);
        gameState.addToPot(amount);
        gameState.updatePlayerAction(player.getPlayer().getId(), PlayerAction.PLACE_BET);

        return new PlayerActionResponse(true, "Bet placed successfully", gameState);
    }

    // Обробка фолду
    private PlayerActionResponse handleFold(GameState gameState, PlayerInGame player) {
        gameState.updatePlayerAction(player.getPlayer().getId(), PlayerAction.FOLD);
        return new PlayerActionResponse(true, "Player folded", gameState);
    }

    // Обробка витягування карт
    private PlayerActionResponse handleDrawCard(GameState gameState, PlayerInGame player) {
        if (!gameState.getPhase().equals(GamePhase.DRAW)) {
            return new PlayerActionResponse(false, "Cannot draw cards in this phase", gameState);
        }

        List<Card> drawnCards = gameState.getDeck().drawCards(1);

        for (Card card : drawnCards) {
            player.addCards(card);
        }

        return new PlayerActionResponse(true, "Card drawn successfully", gameState);
    }
}


