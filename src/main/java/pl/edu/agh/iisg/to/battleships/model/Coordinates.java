package pl.edu.agh.iisg.to.battleships.model;

import java.util.Objects;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y){
        if(x >= 0 && y >= 0){
            this.x = x;
            this.y = y;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates add(Coordinates second){
        return new Coordinates(this.getX()+second.getX(), this.getY() + second.getY());
    }

    public Boolean lessOrEqual(Coordinates second){
        return this.getX() <= second.getX() && this.getY() <= second.getY();
    }

    public Boolean less(Coordinates second){
        return this.getX() < second.getX() && this.getY() < second.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
