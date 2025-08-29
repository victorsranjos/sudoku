package br.com.dio;

// package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Coordinates;
import br.com.dio.util.BoardFactory;
import br.com.dio.model.MoveResult;
import br.com.dio.util.Difficulty;
import br.com.dio.util.SudokuGenerator;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class GameController {

    private Board board;
    private final ConsoleUI ui;
    private final String[] startupArgs;

    public GameController(ConsoleUI ui, String[] startupArgs) {
        this.ui = ui;
        this.startupArgs = startupArgs;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ui.displayMenu();
            int option = ui.getUserOption();

            switch (option) {
                case 1 -> startGame();
                case 2 -> placeNumber();
                case 3 -> removeNumber();
                case 4 -> ui.displayBoard(board);
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> running = false;
                default -> ui.displayMessage("Opção inválida, selecione uma das opções do menu.");
            }
        }
        ui.displayMessage("Obrigado por jogar!");
    }

    private void startGame() {
        if (nonNull(board)) {
            ui.displayMessage("O jogo já foi iniciado. Limpe o jogo atual para começar um novo.");
            return;
        }
        try {
            SudokuGenerator generator = new SudokuGenerator();
            this.board = generator.generateNewGame(Difficulty.MEDIUM);
            ui.displayMessage("O jogo (médio) está pronto para começar!");
            ui.displayBoard(board);
        } catch (IllegalArgumentException e) {
            ui.displayMessage("Falha ao iniciar o jogo: " + e.getMessage());
        }
    }

    private void placeNumber() {
        if (isNull(board)) {
            ui.displayMessage("O jogo precisa ser iniciado primeiro.");
            return;
        }
        Coordinates coords = ui.getCoordinates();
        int value = ui.getValueInput();

        // Usando o enum MoveResult para um feedback claro
        var result = board.changeValue(coords.row(), coords.col(), value);
        ui.displayMessage(result.getMessage());

        if (result == MoveResult.SUCCESS) {
            ui.displayBoard(board);
        }
    }

    private void removeNumber() {
        if (isNull(board)) {
            ui.displayMessage("O jogo precisa ser iniciado primeiro.");
            return;
        }
        Coordinates coords = ui.getCoordinates();
        if (board.clearValue(coords.row(), coords.col())) {
            ui.displayMessage("Número removido com sucesso.");
            ui.displayBoard(board);
        } else {
            ui.displayMessage("Não é possível remover um número de uma célula fixa.");
        }
    }

    private void showGameStatus() {
        if (isNull(board)) {
            ui.displayMessage("O jogo precisa ser iniciado primeiro.");
            return;
        }
        ui.displayMessage("O jogo atualmente se encontra no status: " + board.getStatus().getLabel());
        if (board.hasErrors()) {
            ui.displayMessage("Atenção: O tabuleiro contém erros (números repetidos em linhas, colunas ou blocos).");
        } else {
            ui.displayMessage("O tabuleiro não contém erros de repetição.");
        }
    }

    private void clearGame() {
        if (isNull(board)) {
            ui.displayMessage("O jogo precisa ser iniciado primeiro.");
            return;
        }
        boolean confirmed = ui.getConfirmation("Tem certeza que deseja limpar seu progresso?");
        if (confirmed) {
            board.reset();
            ui.displayMessage("Jogo limpo. O tabuleiro foi restaurado ao seu estado inicial.");
            ui.displayBoard(board);
        }
    }

    private void finishGame() {
        if (isNull(board)) {
            ui.displayMessage("O jogo precisa ser iniciado primeiro.");
            return;
        }

        if (board.gameIsFinished()) {
            ui.displayMessage("Parabéns, você concluiu o jogo com sucesso!");
            ui.displayBoard(board);
            board = null; // Finaliza o jogo para poder iniciar um novo
        } else if (board.hasErrors()) {
            ui.displayMessage("Não foi possível finalizar. Seu jogo contém erros, verifique o tabuleiro.");
        } else {
            ui.displayMessage("Não foi possível finalizar. O tabuleiro ainda possui espaços em branco.");
        }
    }
}
