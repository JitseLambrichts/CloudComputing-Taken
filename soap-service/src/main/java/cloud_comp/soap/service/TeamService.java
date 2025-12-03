package cloud_comp.soap.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.util.List;
import cloud_comp.soap.model.Team;
import cloud_comp.soap.model.Player;

@WebService
public interface TeamService {
    @WebMethod
    List<Team> getTeams();

    @WebMethod
    Player getPlayerByTeamAndName(String teamName, String playerName);
}
