package pl.edu.agh.iisg.to.battleships.dao;

import pl.edu.agh.iisg.to.battleships.model.Player;

import java.util.Optional;

public class HumanPlayerDao extends GenericDao<Player> {
    public Optional<Player> create(String name, String mail, String password) {
        if (this.findByMail(mail).isEmpty()) {
            Player newPlayer = new Player(name, mail, password);
            this.save(newPlayer);
            return Optional.of(newPlayer);
        }
        return Optional.empty();
    }

    public Optional<Player> findByMail(String mail) {
        Player player = null;
//        try {
//            var query = currentSession()
//                    .createQuery("SELECT h FROM Player h WHERE h.mail = :mail", Player.class)
//                    .setParameter("mail", mail);
//            player = query.getSingleResult();
//
//        } catch (PersistenceException e) {
//            e.printStackTrace();
//        }
        return Optional.ofNullable(player);
    }
}
