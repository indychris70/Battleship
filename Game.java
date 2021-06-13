package battleship;

import java.util.Scanner;
import battleship.Field.Ships;

public class Game {
    private enum Messages {
        PROMPT_NUMBER_PLAYERS("Enter the number of players:"),
        PROMPT_PLACE_SHIP("Enter the coordinates of the %s (%s cells):");

        String text;

        Messages(String text) {
            this.text = text;
        }

        void print(String... strings) {
            System.out.println(String.format(text, strings));
        }
    }
    Player[] players;

    void play() {
        Scanner scanner = new Scanner(System.in);
        // get number of players
        Messages.PROMPT_NUMBER_PLAYERS.print();
        int numberOfPlayers = 1;

        // create players
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player();
        }

        // Place Ships in Field
        for (Player player : players) {
            player.printField();
            for (Ships ship : player.getField().getShips()) {
                Messages.PROMPT_PLACE_SHIP.print(ship.type, Integer.toString(ship.size));
                String coordinates = scanner.nextLine();
//                String coordinates = "F7 F3";
                while (!player.getField().placeShip(ship, coordinates)) {
                    Messages.PROMPT_PLACE_SHIP.print(ship.type, Integer.toString(ship.size));
                    coordinates = scanner.nextLine();
                }
                player.printField();
            }
        }
    }
}
