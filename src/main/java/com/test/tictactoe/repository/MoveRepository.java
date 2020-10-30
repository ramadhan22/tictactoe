package com.test.tictactoe.repository;

import com.test.tictactoe.domain.Game;
import com.test.tictactoe.domain.Move;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    List<Move> findByGame(Game game);
    List<Move> findByGameAndPlayer(Game game, String player);
    int countByGameAndPlayer(Game game, String player);
}
