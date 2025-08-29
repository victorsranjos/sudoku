package br.com.dio.model;

public enum MoveResult {
    SUCCESS("Número inserido com sucesso!"),
    CELL_IS_FIXED("Não é possível alterar uma célula fixa."),
    INVALID_COORDINATES("As coordenadas informadas estão fora do tabuleiro."),
    INVALID_PLACEMENT("Jogada inválida! O número já existe na linha, coluna ou bloco.");

    private final String message;

    MoveResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
