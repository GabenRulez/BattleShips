package pl.edu.agh.iisg.to.battleships.dao;

import pl.edu.agh.iisg.to.battleships.session.SessionService;
import pl.edu.agh.iisg.to.battleships.model.Player;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HumanPlayerDao extends GenericDao<Player> {
    public Optional<Player> create(String name, String mail, String password) {
        if (this.findByMail(mail).isEmpty()) {
            SessionService.openSession();
            Player newPlayer = new Player(name, mail, password);
            this.save(newPlayer);
            SessionService.closeSession();
            return Optional.of(newPlayer);
        }
        return Optional.empty();
    }

    public Optional<Player> findByMail(String mail) {
        SessionService.openSession();
        Player player = null;
        try {
            var query = currentSession()
                    .createQuery("SELECT h FROM Player h WHERE h.mail = :mail", Player.class)
                    .setParameter("mail", mail);
            player = query.getSingleResult();

        } catch (PersistenceException e) {
        }
        SessionService.closeSession();
        return Optional.ofNullable(player);
    }

    public void updatePlayer(Player player) {
        SessionService.openSession();
        try {
            this.update(player);

        } catch (PersistenceException e) {
        }
        SessionService.closeSession();
    }

    public List<Player> getPlayersWithRating(){
        SessionService.openSession();
        List<Player> players = new ArrayList<>();
        try {
            var query = currentSession()
                    .createQuery("SELECT p FROM Player p", Player.class);
            players = query.getResultList();

        } catch (PersistenceException e) {
        }
        SessionService.closeSession();

        return players;
    }




}
