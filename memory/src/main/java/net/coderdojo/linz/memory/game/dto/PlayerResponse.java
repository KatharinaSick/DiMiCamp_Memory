package net.coderdojo.linz.memory.game.dto;

import java.util.List;

import net.coderdojo.linz.memory.game.Game.Player;

public record PlayerResponse(
        String name,
        boolean currentPlayer,
        List<String> foundCards) {

    public PlayerResponse(Player player, boolean currentPlayer) {
        this(player.name(), currentPlayer, player.foundCards());
    }

}
