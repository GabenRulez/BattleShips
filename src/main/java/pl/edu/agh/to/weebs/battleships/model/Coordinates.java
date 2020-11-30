package pl.edu.agh.to.weebs.battleships.model;

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
}
