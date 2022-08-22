package net.coderdojo.linz.memory.game;

import java.util.List;

public record Game(
        String id,
        List<String> cards,
        List<Player> players,
        String currentPlayerName
        ) {

    public static record Player(
            String name,
            List<String> foundCards) {
    }
}
