package net.coderdojo.linz.memory.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
                players.get(0).getName());
        games.add(game);
        return game;
    }

    public Game makeMove(String gameId, MoveRequest request) {
        final Game game = games.stream()
                .filter(g -> g.getId().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Game with id %s not found".formatted(gameId)));

        if (!game.getCurrentPlayerName().equals(request.player())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This is not the turn of player %s".formatted(request.player()));
        }

        if (request.card1().equals(request.card2())) {
            Collections.replaceAll(game.getCards(), request.card1(), "");
            var currentPlayer = game.getPlayers().stream()
                    .filter(p -> p.getName().equals(game.getCurrentPlayerName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Current player not found"));

            currentPlayer.getFoundCards().add(request.card1());
        }

        game.setWinners(determineWinners(game));

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

    private List<Player> determineWinners(Game game) {
        if (game.getCards().stream().anyMatch(card -> StringUtils.hasText(card))) {
            // The game is still going on
            return new ArrayList<>();
        }

        // Could also be solved with a stream but it would be quite hard to understand
        // when never having heard of streams
        List<Player> playersWithMostCards = new ArrayList<>();
        int mostCards = 0;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            var player = game.getPlayers().get(i);
            if (playersWithMostCards.isEmpty() || mostCards < player.getFoundCards().size()) {
                mostCards = player.getFoundCards().size();
                playersWithMostCards.clear();
                playersWithMostCards.add(player);
            } else if (mostCards == player.getFoundCards().size()) {
                playersWithMostCards.add(player);
            }
        }

        return playersWithMostCards;
    }
}
