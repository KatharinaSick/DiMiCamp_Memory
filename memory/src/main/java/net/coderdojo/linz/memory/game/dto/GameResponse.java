package net.coderdojo.linz.memory.game.dto;

import java.util.List;

public record GameResponse(
    String id,
    List<String> cards,
    List<PlayerResponse> players
) {
    
}
