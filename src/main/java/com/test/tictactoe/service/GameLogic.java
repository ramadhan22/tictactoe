package com.test.tictactoe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.test.tictactoe.domain.Game;
import com.test.tictactoe.domain.Position;

public class GameLogic {
    private Game game;


    public GameLogic(Game game) {
        this.game = game;
    }

    /**
     * Checks if the player wins
     * @param totalRowColumn Total Row and Column for each game
     * @param positions Board positions from player moves retrieved from database
     * @return true or false if the player is a winner
     */
    static boolean isWinner(int totalRowColumn, List<Position> positions) {
        return getWinningPositions(totalRowColumn).stream().anyMatch(positions::containsAll);
    }

    /**
     * Stores list of winning positions.
     * @param totalRowColumn Total Row and Column for each game
     * @return the list of the winning position's list
     */
    static List<List<Position>> getWinningPositions(int totalRowColumn) {
        List<List<Position>> winingPositions = new ArrayList<>();
        //4
        
        List<Position> diagonal = new ArrayList<>();
        for (int i = 1; i <= totalRowColumn; i++) {
            List<Position> vertical = new ArrayList<>();
            List<Position> horizontal = new ArrayList<>();

            for (int j = 1; j <= totalRowColumn; j++) {
                horizontal.add(new Position(i, j));
                vertical.add(new Position(j, i));
                if (i == j) {
                    diagonal.add(new Position(i, j));
                }
            }
            winingPositions.add(vertical);
            winingPositions.add(horizontal);
        }
        winingPositions.add(diagonal);

        int lastColumn = 1;
        List<Position> diagonalReverse = new ArrayList<>();
        for (int i = totalRowColumn; i >= 1; i--) {
            for (int j = 1; j <= totalRowColumn; j++) {
                if (j == lastColumn) {
                    diagonalReverse.add(new Position(i, j));
                    lastColumn++;
                    break;
                }
            }
        }
        winingPositions.add(diagonalReverse);

        return winingPositions;
    }

    /**
     * Stores all pairs of available rows and columns
     * @return list of all board's positions
     */
    static List<Position> getAllPositions(int totalRowColumn) {
        List<Position> positions = new ArrayList<>();
        for (int row = 1; row <= totalRowColumn; row++) {
            for (int col = 1; col <= totalRowColumn; col++) {
                positions.add(new Position(row, col));
            }
        }
        return positions;
    }

    /**
     *
     * @param numberOfFirstPlayerMovesInGame
     * @param numberOfSecondPlayerMovesInGame
     * @return true or false depending on the count of the player's moves
     */
    static boolean playerTurn(int numberOfFirstPlayerMovesInGame, int numberOfSecondPlayerMovesInGame) {
        return numberOfFirstPlayerMovesInGame == numberOfSecondPlayerMovesInGame || numberOfFirstPlayerMovesInGame == 0;
    }

    /**
     *
     * @param totalRowColumn Total Row and Column for each game
     */
     static boolean isBoardIsFull(int totalRowColumn, List<Position> takenPositions) {
        return takenPositions.size() == totalRowColumn*totalRowColumn;
    }

    // GENERATE COMPUTER'S MOVES
    static List<Position> getOpenPositions(int totalRowColumn, List<Position> takenPositions) {
        return getAllPositions(totalRowColumn).stream().filter(p -> !takenPositions.contains(p)).collect(Collectors.toList());
   }

    static Position nextAutoMove(int totalRowColumn, List<Position> takenPositions) {
        return getOpenPositions(totalRowColumn, takenPositions).get(0);
   }







}
