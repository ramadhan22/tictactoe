package com.test.tictactoe.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Position {
    int boardRow;
    int boardColumn;
}
