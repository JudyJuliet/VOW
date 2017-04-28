package com.vow.sketchInterfaces;

public interface UndoRedoCommandInterface {
	public void undo();
    public void redo();
    public boolean canUndo();
    public boolean canRedo();
    public void onDeleteFromUndoStack();
    public void onDeleteFromRedoStack();
}