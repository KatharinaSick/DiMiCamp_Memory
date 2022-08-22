# DiMiCamp Workshop: Memory

## Intro

* Show the game that we will code
* Explain the benefits of having the game logic on a server (multiple players on various clients, save high scores etc)
* Briefly talk about REST & Spring Boot (https://docs.google.com/presentation/d/14zxRD0DiGX21pDXrC-fBxWp4iToVGXaqKinmkbY_bSg/edit?usp=sharing) & why we are using it

## Init & Run

* Open empty folder in VS Code
* Install extension: [Spring Initializr Java Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr)
* Open command "terminal" with [Ctrl + Shift + p] and type "Spring"
* Select "Spring Initializr: Create a Gradle project...", enter the following options and generate the project into the currently opened folder
    - Spring Boot version: 2.7.3
    - Language: Java
    - Group id: any, e.g. "net.coderdojo.linz"
    - Artifact id: "memory"
    - Packaging type: Jar (doesn't matter)
    - Java version: 18
    - Dependencies: Spring Web
* Open `MemoryApplication` and click on the Play button in the top right to run the app

## Server Status

* Open frontend: https://react-ts-nhw22s.stackblitz.io/ - tiny url: https://tiny.one/dimi-memory
* Check Server status on the top - it should still state "Kein Server gefunden" (unfortunately I couldn't differentiate server not running from CORS)
* Quickly explain CORS and why the server is not reachable because of it
* Allow all CORS origins in the `MemoryApplication.java`
```java
@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
        }
    };
}
```
* Now the status bar should state "/status nicht gefunden" -> implement this endpoint in a new controller
```java
// StatusController.java
package net.coderdojo.linz.memory.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    
    @GetMapping("/status")
    public String getStatus() {
        return "OK";
    }

}
```
* Status bar should be green now

## Game Options

* Now the game is asking for some game options
* Create a DTO that will be sent to the user. Currently, it only contains one option but it can/will be extended during the workshop
```java
// OptionsResponse.java
package net.coderdojo.linz.memory.game.dto;

public record OptionsResponse(
    int maxNumberOfPlayers
) {
    public OptionsResponse() {
        this(1);
    }
}

```
* Create a new controller `GameController` with a GET endpoint that simply returns the game options
```java
// GameController.java
package net.coderdojo.linz.memory.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.coderdojo.linz.memory.game.dto.OptionsResponse;

@RestController
public class GameController {

    @GetMapping("/options")
    public OptionsResponse getOptions() {
        return new OptionsResponse();
    }

}
```
* Restart the server & reload the frontend
* Now it is possible to enter a player name, but the endpoint for starting the game is still missing

## Start the game

* Create a new DTO: `GameRequest`. This contains the data, the frontend is sending to the server when starting a game
```java
// GameRequest.java
package net.coderdojo.linz.memory.game.dto;

import java.util.List;

public record GameRequest(
    List<String> players
) {
    
}
```
* Create two new DTOs: `PlayerResponse` & `GameResponse`. They contain the data, the server is sending back to the frontend when starting a game
```java
// PlayerResponse.java
package net.coderdojo.linz.memory.game.dto;

import java.util.List;

public record PlayerResponse(
    String name,
    boolean currentPlayer,
    List<String> foundCards
) {
    
}
```

```java
// GameResponse.java
package net.coderdojo.linz.memory.game.dto;

import java.util.List;

public record GameResponse(
    int id,
    List<String> cards,
    List<PlayerResponse> players
) {
    
}
```
* Extend the `GameController` with the endpoint for starting a game
```java
// GameController.java
@PostMapping("/game")
public GameResponse startGame(@RequestBody GameRequest gameRequest) {
    var response = new GameResponse(
        UUID.randomUUID().toString(),
        TODO - cards,
        TODO - players
    );
}
```
* Create a new service that will generate cards & players. Cards/emojis will be hardcoded for the first iteration but we'll improve that soon. Emojis can be copied from https://tiny.one/emojis - simply select 4 of them and repeat them
```java
// GameService.java
package net.coderdojo.linz.memory.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import net.coderdojo.linz.memory.game.dto.PlayerResponse;

@Service
public class GameService {
    
    public List<String> generateCards() {
        return new ArrayList<>(Arrays.asList("😅", "😀", "😆", "😁", "😆", "😁", "😅", "😀"));
    }

    public List<PlayerResponse> initPlayers(List<String> playerNames) {
        return playerNames.stream().map(player -> new Player(player, new ArrayList<>())).toList();
    }
}
```
* Update the controller to call those methods
```java
// GameController.java
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

    // options..

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
```
* Now it should be possible to start the game.

## Making a move

* For being able to make a move, we need to store the currently running games somewhere. We'll simply keep them in memory for now, but this is of course not ideal.
* Create a new pojo that contains the data of the current game. I changed it a bit when comparing it to the DTO, to show why we are using a DTO
```java
// Game.java
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
```
* Move the start game logic to the service instead of the controller and make `generateCards` & `initPlayers` private and create the corresponding constructors in the DTOs.
```java
// GameService.java
public Game startGame(GameRequest request) {
    var players = initPlayers(request.players());
    var game = new Game(
            UUID.randomUUID().toString(),
            generateCards(),
            players,
            players.get(0).name());
    return game;
}
```
```java
// GameController.java
...
@PostMapping("/game")
public GameResponse startGame(@RequestBody GameRequest gameRequest) {
    var response = new GameResponse(gameService.startGame(gameRequest));
    return response;
}
...
```
* Add a list to the service that keeps a reference to all games
```java
// GameService.java
@Service
public class GameService {

    private final List<Game> games = new ArrayList<>();

    public Game startGame(GameRequest request) {
        var game = ...;
        games.add(game);
        return game;
    }

    ...
}
```
* Now we are ready to make a move. Add a new DTO
```java
// MoveRequest.java
package net.coderdojo.linz.memory.game.dto;

public record MoveRequest(
        String player,
        String card1,
        String card2) {

}
```
* Add a new endpoint to the `GameController`
```java
// GameController.java
@PutMapping("/game/{id}")
public GameResponse makeMove(@PathVariable String id, @RequestBody MoveRequest moveRequest) {
    return new GameResponse(gameService.makeMove(id, moveRequest));
}
```
* Implement the `makeMove` method in the service
```java
//GameService.java
public Game makeMove(String gameId, MoveRequest request) {
    final Game game = games.stream()
            .filter(g -> g.id().equals(gameId))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Game with id %s not found".formatted(gameId)));

    if (!game.currentPlayerName().equals(request.player())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "This is not the turn of player %s".formatted(request.player()));
    }

    if (request.card1().equals(request.card2())) {
        Collections.replaceAll(game.cards(), request.card1(), "");
        var currentPlayer = game.players().stream()
                .filter(p -> p.name().equals(game.currentPlayerName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Current player not found"));

        currentPlayer.foundCards().add(request.card1());
    }

    return game;
}
```


## Bonus

The below things can be implemented if there is still time left

* Define the game options in a properties file
* Validate the request body of the POST /game endpoint with Springs `@Valid`
* Use a database to store games