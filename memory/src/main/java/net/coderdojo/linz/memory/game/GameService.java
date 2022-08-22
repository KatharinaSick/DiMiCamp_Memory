package net.coderdojo.linz.memory.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import net.coderdojo.linz.memory.game.dto.PlayerResponse;

@Service
public class GameService {
    
    public List<String> generateCards() {
        return Arrays.asList("😅", "😀", "😆", "😁", "😆", "😁", "😅", "😀");
    }

    public List<PlayerResponse> initPlayers(List<String> playerNames) {
        return playerNames.stream().map(player -> new PlayerResponse(player, true, Collections.emptyList())).toList();
    }
}
