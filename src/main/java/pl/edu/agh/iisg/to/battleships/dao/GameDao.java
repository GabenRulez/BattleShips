package pl.edu.agh.iisg.to.battleships.dao;

import pl.edu.agh.iisg.to.battleships.model.Game;

import java.util.Optional;

public class GameDao extends GenericDao<Game> {
    public Optional<Game> saveToDb(Game game) {
//        if (this.findById(game.getId()).isEmpty()) {
            this.save(game);
            return Optional.of(game);
//        }
//        return Optional.empty();
    }

    public Optional<Game> findById(Long id) {
//        try {
//            Game game = currentSession()
//                    .createQuery("SELECT g FROM Game g WHERE g.id = :id", Game.class)
//                    .setParameter("id", id)
//                    .getSingleResult();
//            return Optional.of(game);
//        } catch (PersistenceException e) {
//        }
        return Optional.empty();
    }
}

