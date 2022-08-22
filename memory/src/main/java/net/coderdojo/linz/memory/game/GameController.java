package net.coderdojo.linz.memory.game;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.coderdojo.linz.memory.game.dto.GameRequest;
import net.coderdojo.linz.memory.game.dto.GameResponse;
import net.coderdojo.linz.memory.game.dto.OptionsResponse;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/options")
    public OptionsResponse getOptions() {
        return new OptionsResponse(1);
    }

    @PostMapping("/game")
    public GameResponse startGame(@RequestBody GameRequest gameRequest) {
        var response = new GameResponse(
            UUID.randomUUID().toString(),
            gameService.generateCards(),
            gameService.initPlayers(gameRequest.players())
        );
        return response;
    }
}
