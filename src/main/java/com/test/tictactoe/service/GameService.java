package com.test.tictactoe.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.test.tictactoe.DTO.GameDTO;
import com.test.tictactoe.domain.Game;
import com.test.tictactoe.enums.GameStatus;
import com.test.tictactoe.enums.GameType;
import com.test.tictactoe.exceptions.ResourceNotFoundException;
import com.test.tictactoe.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;


    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createNewGame(GameDTO gameDTO) {
        Game game = new Game();
        game.setFirstPlayer("player");
        game.setGameType(GameType.COMPUTER);
        game.setFirstPlayerPieceCode(gameDTO.getPiece());
        game.setGameStatus(GameStatus.IN_PROGRESS);
        game.setTotalRowColumn(gameDTO.getTotalRowColumn());

        game.setCreated(new Date());
        gameRepository.save(game);

        return game;
    }


    public Game updateGameStatus(Game game, GameStatus gameStatus) throws ResourceNotFoundException {
        Game g = getGame(game.getId());
        g.setGameStatus(gameStatus);

        return g;
    }

    public List<Game> getPlayerGames() {
        return gameRepository.findByGameStatus(
                GameStatus.IN_PROGRESS).stream().collect(Collectors.toList());
    }


    public Game getGame(Long id) 
        throws ResourceNotFoundException {
        return gameRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for this id :: " + id));
    }
}
