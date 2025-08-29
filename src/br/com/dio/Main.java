package br.com.dio;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();

        GameController gameController = new GameController(ui, args);

        gameController.run();
    }
}