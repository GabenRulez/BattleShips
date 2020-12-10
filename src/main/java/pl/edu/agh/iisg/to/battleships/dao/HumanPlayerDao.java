package pl.edu.agh.iisg.to.battleships.dao;

import pl.edu.agh.iisg.to.battleships.model.HumanPlayer;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class HumanPlayerDao extends GenericDao<HumanPlayer> {
    public Optional<HumanPlayer> create(String name, String mail, String password) {
        if (this.findByMail(mail).isEmpty()) {
            HumanPlayer newPlayer = new HumanPlayer(name, mail, password);
            this.save(newPlayer);
            return Optional.of(newPlayer);
        }
        return Optional.empty();
    }

    public Optional<HumanPlayer> findByMail(String mail) {
        HumanPlayer player = null;
        try {
            var query = currentSession()
                    .createQuery("SELECT h FROM HumanPlayer h WHERE h.mail = :mail", HumanPlayer.class)
                    .setParameter("mail", mail);
            player = query.getSingleResult();

        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(player);
    }
}
