package br.com.dio.util;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class BoardFactory {

    private static final int BOARD_LIMIT = 9;

    public static Board createFromArgs(String[] args) {
        try {
            // MUDANÇA 1: Adicionado filtro para mais robustez no parsing inicial.
            // Isso previne erros se um argumento não tiver o formato "chave;valor".
            final Map<String, String> positions = Stream.of(args)
                    .filter(arg -> arg.contains(";")) // Garante que o argumento tem o separador
                    .collect(toMap(
                            k -> k.split(";")[0],
                            v -> v.split(";")[1]
                    ));

            // MUDANÇA 2: A estrutura agora é "lista de linhas" (row-major), que é mais convencional.
            List<List<Space>> boardRows = new ArrayList<>();
            for (int row = 0; row < BOARD_LIMIT; row++) {
                List<Space> currentRow = new ArrayList<>();
                for (int col = 0; col < BOARD_LIMIT; col++) {
                    String positionKey = String.format("%d,%d", row, col);
                    String positionConfig = positions.get(positionKey);

                    Space space = parseSpaceFromConfig(positionConfig);
                    currentRow.add(space);
                }
                boardRows.add(currentRow);
            }
            // A classe Board precisará ser ajustada para receber a lista de linhas,
            // ou podemos transpor a matriz aqui antes de passar para o construtor.
            // Para simplicidade, vamos assumir que o construtor de Board pode ser adaptado.
            return new Board(boardRows);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Erro ao processar os argumentos de inicialização do jogo.", e);
        }
    }

    private static Space parseSpaceFromConfig(String configString) {
        if (configString == null || configString.isBlank()) {
            return new Space(null, false); // Célula vazia e não-fixa
        }

        String[] parts = configString.split(",");
        if (parts.length != 2) {
            // Se a configuração for inválida (ex: "5," ou "true"), trata como vazia.
            return new Space(null, false);
        }

        Integer value = Integer.parseInt(parts[0]);
        boolean isFixed = Boolean.parseBoolean(parts[1]);
        return new Space(value, isFixed);
    }
}
