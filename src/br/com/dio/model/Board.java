package br.com.dio.model;

import java.util.*;


import static br.com.dio.model.GameStatusEnum.COMPLETE;
import static br.com.dio.model.GameStatusEnum.INCOMPLETE;
import static br.com.dio.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public GameStatusEnum getStatus(){
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors(){
        for (int i = 0; i < 9; i++) {
            if (hasDuplicatesInRow(i) || hasDuplicatesInCol(i)) {
                return true;
            }
        }

        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                if (hasDuplicatesInBox(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasDuplicates(List<Integer> numbers) {
        Set<Integer> seen = new HashSet<>();
        for (Integer num : numbers) {
            if (num != null) {
                if (!seen.add(num))
                    return true;
            }
            seen.add(num);
        }
        return false;
    }

    private boolean hasDuplicatesInRow(int rowIndex) {
        List<Integer> rowValues = new ArrayList<>();
        for (int colIndex = 0; colIndex < 9; colIndex++) {
            rowValues.add(spaces.get(colIndex).get(rowIndex).getActual());
        }
        return hasDuplicates(rowValues);
    }

    private boolean hasDuplicatesInCol(int colIndex) {
        List<Integer> colValues = spaces.get(colIndex).stream()
                .map(Space::getActual)
                .toList();
        return hasDuplicates(colValues);
    }

    private boolean hasDuplicatesInBox (int startRow, int startCol) {
        List<Integer> boxValues = new ArrayList<>();
        int endRow = startRow + 3;
        int endCol = startCol + 3;
        for (int r = startRow; r < endRow; r++) {
            for (int c = startCol; c < endCol; c++) {
                boxValues.add(spaces.get(c).get(r).getActual());
            }
        }
        return hasDuplicates(boxValues);
    }

    private boolean isValidPlacement(int row, int col, int number) {
        for (int c = 0; c < 9; c++) {
            if (spaces.get(c).get(row).getActual() != null && spaces.get(c).get(row).getActual() == number) {
                return false;
            }
        }

        for (int r = 0; r < 9; r++) {
            if (spaces.get(col).get(r).getActual() != null && spaces.get(col).get(r).getActual() == number) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (spaces.get(c).get(r).getActual() != null & spaces.get(c).get(r).getActual() == number) {
                    return false;
                }
            }
        }

        return true;
    }

    public MoveResult changeValue(final int col, final int row, final Integer value){
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            return MoveResult.INVALID_COORDINATES;
        }

        var space = spaces.get(col).get(row);

        if (space.isFixed()) {
            return MoveResult.CELL_IS_FIXED;
        }

        if (!isValidPlacement(row, col, value)) {
            return MoveResult.INVALID_PLACEMENT;
        }

        space.setActual(value);
        return MoveResult.SUCCESS;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }

    private static final int BOARD_LIMIT = 9;

    public Space getSpaceAt(int row, int col) {
        if (row < 0 || row >= BOARD_LIMIT || col < 0 || col >= BOARD_LIMIT) {
            throw new IllegalArgumentException("Coordenadas inválidas.");
        }
        return spaces.get(col).get(row);
    }

}
