package net.coderdojo.linz.memory.game;

import java.util.ArrayList;
import java.util.List;

public class Game {

        private String id;
        private List<String> cards;
        private List<Player> players;
        private String currentPlayerName;
        private List<Player> winners;

        public Game(String id, List<String> cards, List<Player> players, String currentPlayerName) {
                this.id = id;
                this.cards = cards;
                this.players = players;
                this.currentPlayerName = currentPlayerName;
                this.winners = new ArrayList<>();
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public List<String> getCards() {
                return cards;
        }

        public void setCards(List<String> cards) {
                this.cards = cards;
        }

        public List<Player> getPlayers() {
                return players;
        }

        public void setPlayers(List<Player> players) {
                this.players = players;
        }

        public String getCurrentPlayerName() {
                return currentPlayerName;
        }

        public void setCurrentPlayerName(String currentPlayerName) {
                this.currentPlayerName = currentPlayerName;
        }

        public List<Player> getWinners() {
                return winners;
        }

        public void setWinners(List<Player> winners) {
                this.winners = winners;
        }


        public static class Player {
                private String name;
                private List<String> foundCards;

                public Player(String name, List<String> foundCards) {
                        this.name = name;
                        this.foundCards = foundCards;
                }

                public String getName() {
                        return name;
                }

                public void setName(String name) {
                        this.name = name;
                }

                public List<String> getFoundCards() {
                        return foundCards;
                }

                public void setFoundCards(List<String> foundCards) {
                        this.foundCards = foundCards;
                }
        }
}