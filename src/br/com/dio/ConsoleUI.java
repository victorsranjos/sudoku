package br.com.dio;

// package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Coordinates;
import br.com.dio.model.Space;

import java.util.InputMismatchException;
import java.util.Scanner;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\n--- JOGO DE SUDOKU ---");
        System.out.println("1 - Iniciar um novo Jogo (Médio)");
        System.out.println("2 - Colocar um número");
        System.out.println("3 - Remover um número");
        System.out.println("4 - Visualizar jogo atual");
        System.out.println("5 - Verificar status do jogo");
        System.out.println("6 - Limpar jogo");
        System.out.println("7 - Finalizar e validar jogo");
        System.out.println("8 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    public int getUserOption() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next(); // Limpa o buffer do scanner
            return -1; // Retorna um valor inválido
        }
    }

    public Coordinates getCoordinates() {
        int row = getNumberInRange("Informe a linha (0-8): ", 0, 8);
        int col = getNumberInRange("Informe a coluna (0-8): ", 0, 8);
        return new Coordinates(row, col);
    }

    public int getValueInput() {
        return getNumberInRange("Informe o número (1-9): ", 1, 9);
    }

    public boolean getConfirmation(String prompt) {
        System.out.println(prompt + " (sim/não)");
        String confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")) {
            System.out.println("Resposta inválida. Informe 'sim' ou 'não'");
            confirm = scanner.next();
        }
        return confirm.equalsIgnoreCase("sim");
    }

    public void displayBoard(Board board) {
        if (isNull(board)) {
            displayMessage("O jogo ainda não foi iniciado.");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        // Lógica de iteração mais clara (linha por linha)
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Space space = board.getSpaceAt(row, col);
                String cellDisplay = " ";
                if (nonNull(space.getActual())) {
                    cellDisplay = space.getActual().toString();
                }
                // Adiciona uma diferenciação visual para números fixos
                if (space.isFixed()) {
                    args[argPos++] = "[" + cellDisplay + "]";
                } else {
                    args[argPos++] = " " + cellDisplay + " ";
                }
            }
        }
        System.out.println("\nSeu jogo se encontra da seguinte forma:");
        // Ajuste o BOARD_TEMPLATE para ter 3 caracteres por célula, ex: |  %s  |
        System.out.printf((BOARD_TEMPLATE) + "%n", args);
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    private int getNumberInRange(String prompt, int min, int max) {
        System.out.print(prompt);
        int number = -1;
        while (true) {
            try {
                number = scanner.nextInt();
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.printf("Número inválido. Informe um valor entre %d e %d: ", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Entrada inválida. Informe um número inteiro entre %d e %d: ", min, max);
                scanner.next(); // Limpa o buffer
            }
        }
    }
}