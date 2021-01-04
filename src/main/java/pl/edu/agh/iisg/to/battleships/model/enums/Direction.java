package pl.edu.agh.iisg.to.battleships.model.enums;

import pl.edu.agh.iisg.to.battleships.model.Coordinates;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    DIFFERENT;

    public Coordinates getVector(){
        return switch (this) {
            case UP -> new Coordinates(0,-1);
            case DOWN -> new Coordinates(0, 1);
            case LEFT -> new Coordinates(-1, 0);
            case RIGHT -> new Coordinates(1,0);
            default -> null;
        };
    }

    public Direction reverse(){
        return switch (this){
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> DIFFERENT;
        };
    }
}
