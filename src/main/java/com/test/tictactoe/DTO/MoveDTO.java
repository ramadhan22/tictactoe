package com.test.tictactoe.DTO;

import com.test.tictactoe.enums.GameStatus;
import com.test.tictactoe.enums.Piece;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveDTO {

    private int boardColumn;
    private int boardRow;
    private Date created;
    private String userName;
    private GameStatus gameStatus;
    private Piece playerPieceCode;
}
