package com.mygdx.game;

public enum SnakeMoveResult {
    OUT_OF_BOUNDS,
    COLLUSION,
    SNAKE_ERROR,
    SNAKE_MOVE_OK,
    SNAKE_ATE_FOOD,
    ATE_BUT_CANT_GROW
}
