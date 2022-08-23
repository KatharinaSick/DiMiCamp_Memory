package net.coderdojo.linz.memory.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.coderdojo.linz.memory.game.dto.GameRequest;
import net.coderdojo.linz.memory.game.dto.GameResponse;
import net.coderdojo.linz.memory.game.dto.MoveRequest;
import net.coderdojo.linz.memory.game.dto.OptionsResponse;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/options")
    public OptionsResponse getOptions() {
        return new OptionsResponse();
    }

    @PostMapping("/game")
    public GameResponse startGame(@RequestBody GameRequest gameRequest) {
        var response = new GameResponse(gameService.startGame(gameRequest));
        return response;
    }

    @PutMapping("/game/{id}")
    public GameResponse makeMove(@PathVariable String id, @RequestBody MoveRequest moveRequest) {
        return new GameResponse(gameService.makeMove(id, moveRequest));
    }
}
