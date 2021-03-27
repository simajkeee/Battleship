package battleship;

import java.util.Scanner;

public class PlayerEnv {
    public final String SHIP_CELL = "O";
    public final String SHIP_HIT_CELL = "X";
    public final String MISS_CELL = "M";

    private Scanner s;
    private Ship[] ships;
    private String[][] battleField;
    private String[][] foggedBattleField;
    private ShipLocation[] shipLocations;
    private String name;

    PlayerEnv(String name) {
        initState(name);
    }

    private void initState(String name) {
        this.s = new Scanner(System.in);
        ships = new Ship[5];
        this.battleField = new String[10][10];
        this.foggedBattleField = new String[10][10];
        this.shipLocations = new ShipLocation[0];
        this.name = name;
    }

    public void userInteractiveInit() {
        initBattleField(battleField);
        initBattleField(foggedBattleField);
        initShips();
        displayBattleField(battleField);
        setShipsPositions();
//        startGame();
    }

    public String askCoordsForShot() {
        return s.next();
    }

    public boolean shot(String theShot) {
        if (!areCoordsInBattleFieldRange(theShot)) {
            System.out.println("");
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            System.out.println("");
            return false;
        }
        setShotOnBattleField(theShot);
        setShotOnFoggedBattleField(theShot);
        displayBattleField(foggedBattleField);
        if (isShipCell(getCell(theShot))) {
            removeShipPartCoordsLocation(theShot);
            System.out.println("");
            System.out.println("You hit a ship!");
        } else {
            System.out.println("");
            System.out.println("You missed!");
        }
        displayBattleField(battleField);
        return true;
    }

    public int getArrayKeyShipLocationsByShot(String theShot) {
        for(int i = 0; i < shipLocations.length; i++) {
            if (shipLocations[i].isCoordEverExisted(theShot)) {
                return i;
            }
        }
        return -1;
    }

    public ShipLocation getShipLocationByKey(int key) {
        if (key < 0 || key > shipLocations.length - 1) {
            throw new IndexOutOfBoundsException();
        }
        return shipLocations[key];
    }

    public void removeShipPartCoordsLocation(String theShot) {
        for(ShipLocation shipLocation : shipLocations) {
            shipLocation.removeShipPartCoordsLocation(theShot);
        }
    }

    public boolean areShipsSunk() {
        for(ShipLocation shipLocation : shipLocations) {
            if (shipLocation.isShipPresent()) {
                return false;
            }
        }
        return true;
    }

    private void setShotOnFoggedBattleField(String coords) {
        foggedBattleField[getRowByLetter(coords.charAt(0))][Integer.parseInt(coords.substring(1))-1] = battleField[getRowByLetter(coords.charAt(0))][Integer.parseInt(coords.substring(1))-1];
    }

    private void setShotOnBattleField(String coords) {
        if (isShipCell(getCell(coords))) {
            battleField[getRowByLetter(coords.charAt(0))][Integer.parseInt(coords.substring(1))-1] = SHIP_HIT_CELL;
        } else {
            battleField[getRowByLetter(coords.charAt(0))][Integer.parseInt(coords.substring(1))-1] = MISS_CELL;
        }
    }


    private void setShipsPositions() {
        for(Ship ship : ships) {
            System.out.println("");
            System.out.println("Enter the coordinates of the " + ship.getName() + " (" + ship.getLength() + " cells):");
            System.out.println("");
            String[] coords = s.nextLine().split(" ");
            boolean areCoordsLegitimate = false;
            while(!areCoordsLegitimate) {
                if (coords.length < 2) {
                    coords = s.nextLine().split(" ");
                    continue;
                }

                if (!isShipLongEnough(coords[0], coords[1], ship)) {
                    System.out.println("");
                    System.out.println("Error! Wrong length of the " + ship.getName() + "! Try again:");
                    System.out.println("");
                    coords = s.nextLine().split(" ");
                    continue;
                }

                if (!isUserInputLegit(coords[0], coords[1])) {
                    System.out.println("");
                    System.out.println("Error! Wrong ship location! Try again:");
                    System.out.println("");
                    coords = s.nextLine().split(" ");
                    continue;
                }

                if (areThereShipsAround(coords[0], coords[1])) {
                    System.out.println("");
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    System.out.println("");
                    coords = s.nextLine().split(" ");
                    continue;
                }

                ShipLocation shipLocation = new ShipLocation(ship, battleField);
                shipLocation.setCoords(coords[0], coords[1]);
                addShipLocation(shipLocation);
                areCoordsLegitimate = true;
            }
            setShipOnTheBattleField(coords[0], coords[1]);
            displayBattleField(battleField);
        }
    }

    private void addShipLocation(ShipLocation shipLocation) {
        ShipLocation[] newShipLocations = new ShipLocation[shipLocations.length + 1];
        int i;
        for(i = 0; i < shipLocations.length; i++) {
            newShipLocations[i] = shipLocations[i];
        }
        newShipLocations[i] = shipLocation;
        shipLocations = newShipLocations;
    }

    private void setShipOnTheBattleField(String coords1, String coords2) {
        String coords1Substring = coords1.substring(1);
        String coords2Substring = coords2.substring(1);

        int coords1Number = Integer.parseInt(coords1Substring);
        int coords2Number = Integer.parseInt(coords2Substring);
        if (getRowByLetter(coords1.charAt(0)) == getRowByLetter(coords2.charAt(0))) {

            int index1 = Math.min(coords1Number, coords2Number);
            int index2 = Math.max(coords1Number, coords2Number);
            for (int i = index1; i <= index2; i++) {
                battleField[getRowByLetter(coords1.charAt(0))][i-1] = SHIP_CELL;
            }
        } else {
            int index1 = Math.min(getRowByLetter(coords1.charAt(0)), getRowByLetter(coords2.charAt(0)));
            int index2 = Math.max(getRowByLetter(coords1.charAt(0)), getRowByLetter(coords2.charAt(0)));
            for (int i = index1; i <= index2; i++) {
                battleField[i][coords1Number - 1] = SHIP_CELL;
            }
        }
    }

    private boolean areThereShipsAround(String coords1, String coords2) {
        String coords1Substring = coords1.substring(1);
        String coords2Substring = coords2.substring(1);

        int coords1Number = Integer.parseInt(coords1Substring);
        int coords2Number = Integer.parseInt(coords2Substring);

        if (getRowByLetter(coords1.charAt(0)) == getRowByLetter(coords2.charAt(0))) {
            int index1 = Math.min(coords1Number, coords2Number) - 1;
            int index2 = Math.max(coords1Number, coords2Number) - 1;

            for(int i = index1; i <= index2; i++) {
                if (getRowByLetter(coords1.charAt(0)) > 0 && isShipCell(battleField[getRowByLetter(coords1.charAt(0)) - 1][i])) {
                    return true;
                }

                if (i > 0 && isShipCell(battleField[getRowByLetter(coords1.charAt(0))][i - 1])) {
                    return true;
                }

                if (getRowByLetter(coords1.charAt(0)) < battleField.length - 1 && isShipCell(battleField[getRowByLetter(coords1.charAt(0)) + 1][i])) {
                    return true;
                }

                if (i < battleField[0].length - 1 && isShipCell(battleField[getRowByLetter(coords1.charAt(0))][i + 1])) {
                    return true;
                }
            }
        } else {
            int index1 = Math.min(getRowByLetter(coords1.charAt(0)), getRowByLetter(coords2.charAt(0)));
            int index2 = Math.max(getRowByLetter(coords1.charAt(0)), getRowByLetter(coords2.charAt(0)));
            for(int i = index1; i <= index2; i++) {
                if (i > 0 && isShipCell(battleField[i-1][coords1Number - 1])) {
                    return true;
                }

                if (coords1Number - 1 > 0 && isShipCell(battleField[i][coords1Number - 2])) {
                    return true;
                }

                if (i < battleField.length - 1 && isShipCell(battleField[i+1][coords1Number - 1])) {
                    return true;
                }

                if (coords1Number - 1 < battleField[0].length - 1 && isShipCell(battleField[i][coords1Number])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isShipCell(String cell) {
        return cell.equals(SHIP_CELL) || cell.equals(SHIP_HIT_CELL);
    }

    private boolean isShipLongEnough(String coords1, String coords2, Ship ship) {
        String coords1Substring = coords1.substring(1);
        String coords2Substring = coords2.substring(1);

        int coords1Number = Integer.parseInt(coords1Substring);
        int coords2Number = Integer.parseInt(coords2Substring);

        if (coords1.charAt(0) == coords2.charAt(0)) {
            return Math.abs(coords1Number - coords2Number) + 1 == ship.getLength();
        }

        if (coords1Substring.equals(coords2Substring)) {
            return Math.abs(getRowByLetter(coords1.charAt(0)) - getRowByLetter(coords2.charAt(0))) + 1 == ship.getLength();
        }

        return false;
    }

    private boolean isUserInputLegit(String coords1, String coords2) {
        if (!areCoordsInBattleFieldRange(coords1) || !areCoordsInBattleFieldRange(coords2)) {
            return false;
        }

        if (coords1.charAt(0) != coords2.charAt(0) && coords1.charAt(1) != coords2.charAt(1)) {
            return false;
        }

        if (coords1.charAt(0) == coords2.charAt(0) && coords1.charAt(1) == coords2.charAt(1)) {
            return false;
        }

        return true;
    }

    private boolean areCoordsInBattleFieldRange(String coords) {
        String upperCaseCoords = coords.toUpperCase();
        int row = -1;
        int cellInRow = Integer.parseInt(coords.substring(1));
        if ((row = getRowByLetter(upperCaseCoords.charAt(0))) == -1 || row >= battleField.length) {
            return false;
        }

        if(!isNumeric(upperCaseCoords.charAt(1)) || cellInRow - 1 >= battleField[0].length) {
            return false;
        }

        return true;
    }

    private boolean isNumeric(char num) {
        return num >= 48 && num <= 57;
    }

    private void initShips() {
        ships[0] = new Ship("Aircraft Carrier", 5);
        ships[1] = new Ship("Battleship", 4);
        ships[2] = new Ship("Submarine", 3);
        ships[3] = new Ship("Cruiser", 3);
        ships[4] = new Ship("Destroyer", 2);
    }

    private String getCell(String coords) {
        return battleField[getRowByLetter(coords.charAt(0))][Integer.parseInt(coords.substring(1)) - 1];
    }

    private int getRowByLetter(char letter) {
        int index = -1;
        int firstCharLetter = 65;
        int lastCharLetter = 90;

        if (letter < firstCharLetter || letter > lastCharLetter) {
            return index;
        }

        index = 0;
        while(firstCharLetter+index != letter && firstCharLetter+index <= lastCharLetter) {
            index++;
        }

        return index;
    }

    public String getName() {
        return this.name;
    }

    private void displayBattleField(String[][] battleField) {
        int rows = battleField.length;
        int elInRows = battleField[0].length;

        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= elInRows; j++) {
                if (i == 0 && j == 0) {
                    System.out.print(" ");
                    continue;
                }
                if (i == 0 && j > 0){
                    System.out.print(" " + Integer.toString(j));
                    continue;
                }
                if (i > 0 && j == 0) {
                    System.out.print(Character.toString((char)64+i));
                    continue;
                }
                System.out.print(" " + battleField[i-1][j-1]);
            }
            System.out.println("");
        }
    }

    public void displayUserBattleField() {
        displayBattleField(battleField);
    }

    public void displayFoggedBattleField() {
        displayBattleField(foggedBattleField);
    }

    private void initBattleField(String[][] battleField) {
        for (int i = 0; i < battleField.length; i++) {
            for (int j = 0; j < battleField[i].length; j++) {
                battleField[i][j] = "~";
            }
        }
    }
}
