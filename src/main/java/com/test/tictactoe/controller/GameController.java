package com.test.tictactoe.controller;

import com.test.tictactoe.DTO.GameDTO;
import com.test.tictactoe.domain.Game;
import com.test.tictactoe.enums.GameStatus;
import com.test.tictactoe.exceptions.ResourceNotFoundException;
import com.test.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    HttpSession httpSession;

    Logger logger = LoggerFactory.getLogger(GameController.class);

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Game createNewGame(@RequestBody GameDTO gameDTO) {

        Game game = gameService.createNewGame(gameDTO);
        httpSession.setAttribute("gameId", game.getId());

        logger.info("new game id: " + httpSession.getAttribute("gameId") + " stored in session");

        return game;
    }
    
    @RequestMapping(value = "/player/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Game> getPlayerGames() {
        return gameService.getPlayerGames();
    }

    @RequestMapping(value = "/{id}")
    public Game getGameProperties(@PathVariable Long id) throws ResourceNotFoundException {

        httpSession.setAttribute("gameId", id);

        return gameService.getGame(id);
    }



}
