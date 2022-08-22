package net.coderdojo.linz.memory.game.dto;

public record OptionsResponse(
    int maxNumberOfPlayers
) {
    public OptionsResponse() {
        this(1);
    }
}
