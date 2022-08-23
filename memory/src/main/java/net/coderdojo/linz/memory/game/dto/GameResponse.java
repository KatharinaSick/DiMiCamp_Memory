package net.coderdojo.linz.memory.game.dto;

import java.util.List;

import net.coderdojo.linz.memory.game.Game;

public record GameResponse(
        String id,
        List<String> cards,
        List<PlayerResponse> players,
        List<PlayerResponse> winners) {

    public GameResponse(Game game) {
        this(
                game.getId(),
                game.getCards(),
                game.getPlayers().stream().map(p -> new PlayerResponse(p, p.getName().equals(game.getCurrentPlayerName()))).toList(),
                game.getWinners().stream().map(p -> new PlayerResponse(p, p.getName().equals(game.getCurrentPlayerName()))).toList());
    }
}
