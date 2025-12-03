package cloud_comp.soap.service;

import jakarta.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import cloud_comp.soap.model.Team;
import cloud_comp.soap.model.Player;

@WebService(endpointInterface = "cloud_comp.soap.service.TeamService")
public class TeamServiceImpl implements TeamService {
    private List<Team> teams;

    public TeamServiceImpl() {
        initializeTeams();
    }

    private void initializeTeams() {
        teams = new ArrayList<>();

        List<Player> genk = new ArrayList<>();
        genk.add(new Player("Bryan Heynen", "Middenvelder", 8));
        genk.add(new Player("Konstantinos Karetsas", "Aanvaller", 20));
        genk.add(new Player("Matte Smets", "Verdediger", 6));
        teams.add(new Team("Genk", genk));

        List<Player> brugge = new ArrayList<>();
        brugge.add(new Player("Raphael Onyedika", "Middenvelder", 15));
        brugge.add(new Player("Carlos Forbes", "Aanvaller", 9));
        brugge.add(new Player("Brandon Mechele", "Verdediger", 44));
        teams.add(new Team("Brugge", brugge));

        List<Player> anderlecht = new ArrayList<>();
        anderlecht.add(new Player("Nathan De Cat", "Middenvelder", 74));
        anderlecht.add(new Player("Thorgan Hazard", "Aanvaller", 11));
        anderlecht.add(new Player("Lucas Hey", "Verdediger", 3));
        teams.add(new Team("Anderlecht", anderlecht));
    }

    @Override
    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public Player getPlayerByTeamAndName(String teamName, String playerName) {
        for (Team team : teams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                for (Player player : team.getPlayers()) {
                    if (player.getName().equalsIgnoreCase(playerName)) {
                        return player;
                    }
                }
            }
        }
        return null;
    }
}

