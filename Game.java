package battleship;

import java.util.Scanner;
import battleship.Field.Ships;
import battleship.Coordinate.Status;

public class Game {
    private enum Messages {
        PROMPT_NUMBER_PLAYERS("Enter the number of players:"),
        PROMPT_PLACE_SHIP("Enter the coordinates of the %s (%s cells):"),
        GAME_STARTS("The game starts!"),
        TAKE_SHOT("Take a shot!"),
        HIT("You hit a ship!"),
        MISS("You missed!");

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
    private boolean withFogOfWar = false;
    private boolean withoutFogOfWar = true;

    void play() {
        getNumberOfPlayers();
        initializePlayers();
        placeShips();
        startGame();
    }

    private void getNumberOfPlayers() {
        Messages.PROMPT_NUMBER_PLAYERS.print();
        numberOfPlayers = 1; // hard code for now, but ready for multiple players
    }

    private void initializePlayers() {
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player();
        }
    }

    private void placeShips() {
        Scanner scanner = new Scanner(System.in);
        for (Player player : players) {
            player.printField(withoutFogOfWar);
            String coordinates;
            for (Ships ship : player.getField().getShips()) {
                do {
                    Messages.PROMPT_PLACE_SHIP.print(ship.type, Integer.toString(ship.size));
                    coordinates = scanner.nextLine();
                } while (!player.getField().isValidShipPlacement(coordinates, ship));
                player.getField().placeShip(ship, coordinates);
                player.printField(withoutFogOfWar);
            }
        }
    }

    private void startGame() {
        Scanner scanner = new Scanner(System.in);
        Messages.GAME_STARTS.print();
        for (Player player : players) {
            player.printField(withFogOfWar);
            Messages.TAKE_SHOT.print();
            String coordinate;
            do {
                coordinate = scanner.nextLine();
            } while (!Field.shotCoordinateValid(coordinate));
            player.takeShot(player, coordinate);

            player.printField(withFogOfWar);

            if (player.getField().getCoordinate(coordinate).getStatus() == Status.X) {
                Messages.HIT.print();
            } else {
                Messages.MISS.print();
            }

            player.printField(withoutFogOfWar);
//            for (Ships ship : player.getField().getShips()) {
//                System.out.println(String.format("%s has %d hits", ship.type, ship.numberOfHits()));
//            }
        }
    }
}
