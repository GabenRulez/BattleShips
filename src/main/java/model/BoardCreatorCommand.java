package model;

public interface BoardCreatorCommand {
    void execute();

    void undo();
}
