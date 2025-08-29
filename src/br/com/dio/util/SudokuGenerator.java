package br.com.dio.util;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuGenerator {

    private static final int BOARD_SIZE = 9;
    private int[][] grid;

    public SudokuGenerator() {
        this.grid = new int[BOARD_SIZE][BOARD_SIZE];
    }

    public Board generateNewGame(Difficulty difficulty) {

        fillGrid();

        pokeHoles(difficulty);

        return convertGridToBoard();
    }

    // Backtracking
    private boolean fillGrid() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (grid[row][col] == 0) {
                    List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
                    Collections.shuffle(numbers); // Aleatoriedade aqui!

                    for (int num : numbers) {
                        if (isPlacementValid(row, col, num)) {
                            grid[row][col] = num;
                            if (fillGrid()) {
                                return true; // Sucesso!
                            } else {
                                grid[row][col] = 0; // Backtrack
                            }
                        }
                    }
                    return false; // Nenhum número funcionou, backtrack
                }
            }
        }
        return true; // Grade preenchida
    }

    private void pokeHoles(Difficulty difficulty) {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells.add(new int[]{i, j});
            }
        }
        Collections.shuffle(cells);

        for (int i = 0; i < difficulty.getCellsToRemove(); i++) {
            if (i >= cells.size()) break;
            int[] cell = cells.get(i);
            grid[cell[0]][cell[1]] = 0; // 0 representa uma célula vazia
        }
    }

    private Board convertGridToBoard() {
        List<List<Space>> boardRows = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            List<Space> currentRow = new ArrayList<>();
            for (int col = 0; col < BOARD_SIZE; col++) {
                int value = grid[row][col];
                if (value == 0) {
                    currentRow.add(new Space(null, false));
                } else {
                    currentRow.add(new Space(value, true)); // Números restantes são fixos
                }
            }
            boardRows.add(currentRow);
        }
        return new Board(boardRows);
    }

    // Metodo auxiliar para verificar se uma jogada é válida
    private boolean isPlacementValid(int row, int col, int number) {
        // Verifica linha e coluna
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (grid[row][i] == number || grid[i][col] == number) {
                return false;
            }
        }
        // Verifica bloco 3x3
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if (grid[r][c] == number) {
                    return false;
                }
            }
        }
        return true;
    }
}