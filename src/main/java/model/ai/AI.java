package model.ai;

import model.Board;
import model.Coordinates;

public interface AI {
    Coordinates getNextAttackPosition(Board enemyBoard);
}
