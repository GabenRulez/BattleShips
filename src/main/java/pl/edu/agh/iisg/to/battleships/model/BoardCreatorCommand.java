package pl.edu.agh.iisg.to.battleships.model;

public interface BoardCreatorCommand {
    void execute();

    void undo();
}
