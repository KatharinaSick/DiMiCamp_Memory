package net.coderdojo.linz.memory.game.dto;

import java.util.List;

import net.coderdojo.linz.memory.game.Game;

public record GameResponse(
        String id,
        List<String> cards,
        List<PlayerResponse> players) {

    public GameResponse(Game game) {
        this(game.id(), game.cards(), game.players().stream().map(p -> new PlayerResponse(p, p.name().equals(game.currentPlayerName()))).toList());
    }
}
