package pl.edu.agh.iisg.to.battleships.dao;

import pl.edu.agh.iisg.to.battleships.model.HumanPlayer;
import pl.edu.agh.iisg.to.battleships.session.SessionService;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class HumanPlayerDao extends GenericDao<HumanPlayer> {
    public Optional<HumanPlayer> create(String name, String mail, String password) {
        if (this.findByMail(mail).isEmpty()) {
            SessionService.openSession();
            HumanPlayer newPlayer = new HumanPlayer(name, mail, password);
            this.save(newPlayer);
            SessionService.closeSession();
            return Optional.of(newPlayer);
        }
        return Optional.empty();
    }

    public Optional<HumanPlayer> findByMail(String mail) {
        SessionService.openSession();
        HumanPlayer player = null;
        try {
            var query = currentSession()
                    .createQuery("SELECT h FROM HumanPlayer h WHERE h.mail = :mail", HumanPlayer.class)
                    .setParameter("mail", mail);
            player = query.getSingleResult();

        } catch (PersistenceException e) {
        }
        SessionService.closeSession();
        return Optional.ofNullable(player);
    }


}
