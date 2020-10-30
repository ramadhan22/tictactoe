package com.test.tictactoe.repository;

import java.util.List;

import com.test.tictactoe.domain.Game;
import com.test.tictactoe.enums.GameStatus;
import com.test.tictactoe.enums.GameType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{
    List<Game> findByGameTypeAndGameStatus(GameType GameType, GameStatus GameStatus);
    List<Game> findByGameStatus(GameStatus gameStatus);
}
