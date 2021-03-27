package battleship;

public class Ship {

    private final int length;
    private final String name;

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }
}
