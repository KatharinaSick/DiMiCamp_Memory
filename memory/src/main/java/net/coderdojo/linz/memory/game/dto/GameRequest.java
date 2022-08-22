package net.coderdojo.linz.memory.game.dto;

import java.util.List;

public record GameRequest(
    List<String> players
) {
    
}
