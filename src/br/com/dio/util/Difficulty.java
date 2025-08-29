package br.com.dio.util;

public enum Difficulty {
    EASY(45),
    MEDIUM(50),
    HARD(55);

    private final int cellsToRemove;

    Difficulty(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }

    public int getCellsToRemove() {
        return cellsToRemove;
    }
}
