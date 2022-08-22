package net.coderdojo.linz.memory.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import net.coderdojo.linz.memory.game.Game.Player;
import net.coderdojo.linz.memory.game.dto.GameRequest;
import net.coderdojo.linz.memory.game.dto.MoveRequest;

@Service
public class GameService {

    private List<Game> games = new ArrayList<>();

    public Game startGame(GameRequest request) {
        var players = initPlayers(request.players());
        var game = new Game(
                UUID.randomUUID().toString(),
                generateCards(),
                players,
                players.get(0).name());
        games.add(game);
        return game;
    }

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

    private List<String> generateCards() {
        var cards = new ArrayList<String>();
        cards.addAll(Arrays.asList("😅", "😀", "😆", "😁", "😆", "😁", "😅", "😀"));
        return cards;
    }

    private List<Player> initPlayers(List<String> playerNames) {
        return playerNames.stream().map(player -> new Player(player, new ArrayList<>())).toList();
    }
}
