package cloud_comp.soap.model;

public class Player {
    private String name;
    private String position;
    private int number;

    public Player() {}
    public Player(String name, String position, int number) {
        this.name = name;
        this.position = position;
        this.number = number;
    }

    // Getters
    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getNumber() { return number; }

    // Setters
    public void setName(String name) { this.name = name;}
    public void setPosition(String position) { this.position = position; }
    public void setNumber(int number) { this.number = number; }
}
