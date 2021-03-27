package battleship;

public class ShipLocation {
    private Ship ship;
    private String[][] battleField;
    private String[] shipCoordsLocation;
    private String[] initialShipCoordsLocation;

    ShipLocation(Ship ship, String[][] battleField) {
        this.ship = ship;
        this.battleField = battleField;
        this.shipCoordsLocation = new String[0];
        this.initialShipCoordsLocation = new String[0];
    }

    public void setCoords(String from, String to) {
        char fromLetter = from.charAt(0);
        char toLetter   = to.charAt(0);
        int fromNumber  = Integer.parseInt(from.substring(1));
        int toNumber    = Integer.parseInt(to.substring(1));
        fromNumber = Math.min(fromNumber, toNumber);
        toNumber = Math.min(fromNumber, toNumber);
        if (fromLetter == toLetter) {
            for(int i = 0; i < getShipLocationLength(); i++) {
                int increment = (i == 0) ? 0 : 1;
                fromNumber += increment;
                addShipCoordsLocation(from.substring(0, 1) + fromNumber);
                addInitialShipCoordsLocation(from.substring(0, 1) + fromNumber);
            }
            return;
        }

        if (fromNumber == toNumber) {
            for(int i = 0; i < getShipLocationLength(); i++) {
                int increment = (i == 0) ? 0 : 1;
                fromLetter = (char)((int)fromLetter + increment);
                addShipCoordsLocation(Character.toString(fromLetter) + toNumber);
                addInitialShipCoordsLocation(Character.toString(fromLetter) + toNumber);
            }
        }
    }

    public boolean isShipPresent() {
        return shipCoordsLocation.length > 0;
    }

    public Ship getShip() {
        return ship;
    }

    public void addShipCoordsLocation(String coord) {
        String[] newShipCoordsLocation = new String[shipCoordsLocation.length + 1];
        int i;
        for(i = 0; i < shipCoordsLocation.length; i++) {
            newShipCoordsLocation[i] = shipCoordsLocation[i];
        }
        newShipCoordsLocation[i] = coord;
        shipCoordsLocation = newShipCoordsLocation;
    }

    public void addInitialShipCoordsLocation(String coord) {
        String[] newShipCoordsLocation = new String[initialShipCoordsLocation.length + 1];
        int i;
        for(i = 0; i < initialShipCoordsLocation.length; i++) {
            newShipCoordsLocation[i] = initialShipCoordsLocation[i];
        }
        newShipCoordsLocation[i] = coord;
        initialShipCoordsLocation = newShipCoordsLocation;
    }

    public String[] addShipCoordsLocation(String coord, String[] shipCoordsLocation) {
        String[] newShipCoordsLocation = new String[shipCoordsLocation.length + 1];
        int i;
        for(i = 0; i < shipCoordsLocation.length; i++) {
            newShipCoordsLocation[i] = shipCoordsLocation[i];
        }
        newShipCoordsLocation[i] = coord;
        return newShipCoordsLocation;
    }

    public boolean isCoordEverExisted(String coord) {
        for(String coordLocation : initialShipCoordsLocation) {
            if (coordLocation.equals(coord)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCoordPresent(String coord) {
        for(String coordLocation : shipCoordsLocation) {
            if (coordLocation.equals(coord)) {
                return true;
            }
        }
        return false;
    }

    private int getShipLocationLength() {
        return ship.getLength();
    }

    public void removeShipPartCoordsLocation(String coord) {
        if (shipCoordsLocation.length == 0 || !isCoordPresent(coord)) {
            return;
        }
        String[] newShipCoordsLocation = new String[0];
        for(String coordLocation : shipCoordsLocation) {
            if (coordLocation.equals(coord)) {
                continue;
            }
            newShipCoordsLocation = addShipCoordsLocation(coordLocation, newShipCoordsLocation);
        }
        shipCoordsLocation = newShipCoordsLocation;
    }
}
