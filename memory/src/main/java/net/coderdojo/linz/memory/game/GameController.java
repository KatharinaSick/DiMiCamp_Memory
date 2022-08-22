package net.coderdojo.linz.memory.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.coderdojo.linz.memory.game.dto.OptionsResponse;

@RestController
public class GameController {

    @GetMapping("/options")
    public OptionsResponse getOptions() {
        return new OptionsResponse(1);
    }

}
