package cloud_comp.soap.model;

import java.util.List;

public class Team {
    private String teamName;
    private List<Player> players;

    public Team() {}
    public Team(String teamName, List<Player> players) {
        this.teamName = teamName;
        this.players = players;
    }

    // Getters
    public String getTeamName() { return teamName; }
    public List<Player> getPlayers() { return players; }

    // Setters
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public void setPlayers(List<Player> players) { this.players = players; }
}
