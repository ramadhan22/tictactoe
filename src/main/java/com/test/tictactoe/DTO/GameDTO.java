package com.test.tictactoe.DTO;

import com.test.tictactoe.enums.GameType;
import com.test.tictactoe.enums.Piece;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

    private int id;
    private GameType gameType;
    // private Piece piece;
    private int totalRowColumn;
}


