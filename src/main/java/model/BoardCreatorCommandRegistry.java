package model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BoardCreatorCommandRegistry {
    private final ObservableList<BoardCreatorCommand> commandStack = FXCollections.observableArrayList();

    private final ObservableList<BoardCreatorCommand> undoStack = FXCollections.observableArrayList();

    private final BooleanProperty isUndoEnabled;
    private final BooleanProperty isRedoEnabled;

    public BoardCreatorCommandRegistry() {
        isUndoEnabled = new SimpleBooleanProperty();
        isUndoEnabled.bind(Bindings.isNotEmpty(commandStack));

        isRedoEnabled = new SimpleBooleanProperty();
        isRedoEnabled.bind(Bindings.isNotEmpty(undoStack));
    }

    public void executeCommand(BoardCreatorCommand command) {
        command.execute();
        commandStack.add(command);
        undoStack.clear();
    }

    public void redo() {
        if(!undoStack.isEmpty()) {
            var command = undoStack.remove(undoStack.size() - 1);
            command.execute();
            commandStack.add(command);
        }
    }

    public void undo() {
        if(!commandStack.isEmpty()) {
            var command = commandStack.remove(commandStack.size() - 1);
            command.undo();
            undoStack.add(command);
        }
    }

    public BooleanProperty isIsRedoEnabled() {
        return isRedoEnabled;
    }

    public BooleanProperty isIsUndoEnabled() {
        return isUndoEnabled;
    }
}
