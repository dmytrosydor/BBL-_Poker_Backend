package com.poker.poker.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    private UUID id;
    private String nickname;

    public Player(UUID id, String nickname) {
        this.id = id;
        this.nickname = nickname;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
