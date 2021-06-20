package battleship;

import java.util.Scanner;
import battleship.Coordinate.Status;

public class Game {
    private enum Messages {
        PROMPT_NUMBER_PLAYERS("Enter the number of players:"),
        PROMPT_PLAYER_PLACE_SHIPS("Player %s, place your ships on the game field"),
        PROMPT_PLACE_SHIP("Enter the coordinates of the %s (%s cells):"),
        PROMPT_CHANGE_PLAYERS("Press Enter and pass the move to another player"),
        GAME_STARTS("The game starts!"),
        TAKE_SHOT("Player %s, it's your turn."),
        HIT("You hit a ship!"),
        MISS("You missed!"),
        SHIP_SUNK("You sank a ship."),
        WINNER("You sank the last ship. You won. Congratulations!");

        String text;

        Messages(String text) {
            this.text = text;
        }

        void print(String... subStrings) {
            System.out.println(String.format(text, subStrings));
        }
    }

    private Player[] players;
    private int numberOfPlayers;
    private final boolean withFogOfWar = false;
    private final boolean withoutFogOfWar = true;

    void play() {
        getNumberOfPlayers();
        initializePlayers();
        placeShips();
        startGame();
    }

    private void getNumberOfPlayers() {
        Messages.PROMPT_NUMBER_PLAYERS.print();
        numberOfPlayers = 2; // hard code for now, but ready for multiple players
    }

    private void initializePlayers() {
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(i + 1);
        }
    }

    private void placeShips() {
        Scanner scanner = new Scanner(System.in);
        int playerNumber = 1;
        for (Player player : players) {
            Messages.PROMPT_PLAYER_PLACE_SHIPS.print(Integer.toString(playerNumber++));
            player.printField(withoutFogOfWar);
            String coordinates;
            for (Ship ship : player.getField().getShips()) {
                do {
                    Messages.PROMPT_PLACE_SHIP.print(ship.getType().getType(), Integer.toString(ship.getType().getSize()));
                    coordinates = scanner.nextLine();
                } while (!player.getField().isValidShipPlacement(coordinates, ship));
                player.getField().placeShip(ship, coordinates);
                player.printField(withoutFogOfWar);
            }
            Messages.PROMPT_CHANGE_PLAYERS.print();
        }
    }

    private void startGame() {
        Scanner scanner = new Scanner(System.in);
        Messages.GAME_STARTS.print();
        Player opponent = players[1];
        gameLoop:
        do {
            for (Player player : players) {
                String coordinate;
                do {
                    opponent.printField(withFogOfWar);
                    System.out.println("---------------------");
                    player.printField(withoutFogOfWar);
                    Messages.TAKE_SHOT.print(player.getNumber());
                    coordinate = scanner.nextLine();
                } while (!Field.shotCoordinateValid(coordinate));
                player.takeShot(opponent, coordinate);
                if (opponent.getField().getCoordinate(coordinate).getStatus() == Status.X) {
                    if (opponent.getField().getNumberOfShipsSunk() > opponent.getShipsSunk()) {
                        opponent.setShipsSunk(opponent.getShipsSunk() + 1);
                        if (opponent.getField().getNumberOfShipsSunk() == 5) {
                            break gameLoop;
                        } else {
                            Messages.SHIP_SUNK.print();
                        }
                    } else {
                        Messages.HIT.print();
                    }
                } else {
                    Messages.MISS.print();
                }
                Messages.PROMPT_CHANGE_PLAYERS.print();
                opponent = player;
            }
        } while (!isGameOver());
        Messages.WINNER.print(getWinningPlayerNumber());
    }

    private boolean isGameOver() {
        for (Player player : players) {
            if (player.getShipsSunk() >= 5) {
                return true;
            }
        }
        return false;
    }

    private String getWinningPlayerNumber() {
        for (Player player : players) {
            if (player.getShipsSunk() >= 5) {
                return player.getNumber();
            }
        }
        return "No Winner.";
    }
}
