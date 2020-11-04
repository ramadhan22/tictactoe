package com.test.tictactoe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.test.tictactoe.DTO.CreateMoveDTO;
import com.test.tictactoe.DTO.MoveDTO;
import com.test.tictactoe.domain.Game;
import com.test.tictactoe.domain.Move;
import com.test.tictactoe.domain.Position;
import com.test.tictactoe.enums.GameStatus;
import com.test.tictactoe.enums.GameType;
import com.test.tictactoe.enums.Piece;
import com.test.tictactoe.repository.MoveRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MoveService {

    private final MoveRepository moveRepository;


    @Autowired
    public MoveService(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    public Move createMove(Game game, CreateMoveDTO createMoveDTO) {
        Move move = new Move();
        move.setBoardColumn(createMoveDTO.getBoardColumn());
        move.setBoardRow(createMoveDTO.getBoardRow());
        move.setCreated(new Date());
        move.setPlayer(createMoveDTO.getPlayer());
        move.setGame(game);

        moveRepository.save(move);

        return move;
    }

    public Move autoCreateMove(Game game) {
        Move move = new Move();
        move.setBoardColumn(GameLogic.nextAutoMove(game.getTotalRowColumn(), getTakenMovePositionsInGame(game)).getBoardColumn());
        move.setBoardRow(GameLogic.nextAutoMove(game.getTotalRowColumn(), getTakenMovePositionsInGame(game)).getBoardRow());
        move.setCreated(new Date());
        move.setPlayer(null);
        move.setGame(game);

        moveRepository.save(move);

        return move;
    }

    public GameStatus checkCurrentGameStatus(Game game) {
        if (GameLogic.isWinner(game.getTotalRowColumn(), getPlayerMovePositionsInGame(game, game.getFirstPlayer()))) {
            return GameStatus.FIRST_PLAYER_WON;
        } else if (GameLogic.isWinner(game.getTotalRowColumn(), getPlayerMovePositionsInGame(game, game.getSecondPlayer()))) {
            return GameStatus.SECOND_PLAYER_WON;
        } else if (GameLogic.isBoardIsFull(game.getTotalRowColumn(), getTakenMovePositionsInGame(game))) {
            return GameStatus.TIE;
        } else {
            return GameStatus.IN_PROGRESS;
        }

    }


    public List<MoveDTO> getMovesInGame(Game game) {

        List<Move> movesInGame = moveRepository.findByGame(game);
        List<MoveDTO> moves = new ArrayList<>();
        Piece currentPiece = game.getFirstPlayerPieceCode();

        for(Move move :  movesInGame) {
            MoveDTO moveDTO = new MoveDTO();
            moveDTO.setBoardColumn(move.getBoardColumn());
            moveDTO.setBoardRow(move.getBoardRow());
            moveDTO.setCreated(move.getCreated());
            moveDTO.setGameStatus(move.getGame().getGameStatus());
            moveDTO.setUserName(move.getPlayer() == null ? GameType.COMPUTER.toString() : move.getPlayer());
            moveDTO.setPlayerPieceCode(currentPiece);
            moves.add(moveDTO);

            currentPiece = currentPiece == Piece.X ? Piece.O : Piece.X;
        }

        return moves;
    }

    public List<Position> getTakenMovePositionsInGame(Game game) {
        return moveRepository.findByGame(game).stream()
                .map(move -> new Position(move.getBoardRow(), move.getBoardColumn()))
                .collect(Collectors.toList());
    }

    public List<Position> getPlayerMovePositionsInGame(Game game, String player) {

        return moveRepository.findByGameAndPlayer(game, player).stream()
                .map(move -> new Position(move.getBoardRow(), move.getBoardColumn()))
                .collect(Collectors.toList());
    }

    public int getTheNumberOfPlayerMovesInGame(Game game, String player) {
        return moveRepository.countByGameAndPlayer(game, player);
    }
}
