package net.coderdojo.linz.memory.game.dto;

import java.util.List;

public record PlayerResponse(
    String name,
    boolean currentPlayer,
    List<String> foundCards
) {
    
}
