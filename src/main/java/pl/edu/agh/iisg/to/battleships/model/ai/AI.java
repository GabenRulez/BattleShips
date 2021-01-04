package pl.edu.agh.iisg.to.battleships.model.ai;

import pl.edu.agh.iisg.to.battleships.model.Board;
import pl.edu.agh.iisg.to.battleships.model.Coordinates;

public interface AI {
    Coordinates getNextAttackPosition(Board enemyBoard);
}
