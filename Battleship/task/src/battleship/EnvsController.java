package battleship;

import java.util.Scanner;

public class EnvsController {

    private Scanner s = new Scanner(System.in);

    private PlayerEnv p1;
    private PlayerEnv p2;

    private PlayerEnv currentPlayer;

    public EnvsController() {
        initPlayers();
        runEnv();
    }

    private void initPlayers() {
        this.p1 = new PlayerEnv("Player 1");
        this.p2 = new PlayerEnv("Player 2");
    }

    private void runEnv() {
        System.out.println("");
        System.out.println(p1.getName() + ", place your ships on the game field");
        System.out.println("");
        p1.userInteractiveInit();

        askNextPlayer();

        System.out.println("");
        System.out.println(p2.getName() + ", place your ships to the game field");
        System.out.println("");
        p2.userInteractiveInit();

        askNextPlayer();

        boolean gameEnd = false;
        currentPlayer = p1;
        while(!gameEnd) {
            boolean isShotCorrect = false;
            String theShot = "";
            PlayerEnv opponent = getOpponent();

            opponent.displayFoggedBattleField();
            System.out.println("---------------------");
            currentPlayer.displayUserBattleField();

            System.out.println("");
            System.out.println(currentPlayer.getName() + ", it's your turn:");
            System.out.println("");

            while(!isShotCorrect) {
                theShot = currentPlayer.askCoordsForShot();
                isShotCorrect = opponent.shot(theShot) == true ? true : false;
            }

            int shipLocationKey = -1;
            if ((shipLocationKey = opponent.getArrayKeyShipLocationsByShot(theShot)) != -1) {
                ShipLocation shipLocation = opponent.getShipLocationByKey(shipLocationKey);
                if (!shipLocation.isShipPresent()) {
                    System.out.println("You sank a ship!");
                }
            }

            if (opponent.areShipsSunk()) {
                System.out.println("");
                System.out.println("You sank the last ship. You won. Congratulations!");
                gameEnd = true;
            } else {
                askNextPlayer();
                currentPlayer = getOpponent();
            }
        }

    }

    private PlayerEnv getOpponent() {
        return currentPlayer == p1 ? p2 : p1;
    }

    private void askNextPlayer() {
        System.out.println("");
        System.out.println("Press Enter and pass the move to another player");
        s.nextLine();
        System.out.println("");
    }
}
