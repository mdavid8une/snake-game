package com.mygdx.game;


import java.util.ArrayList;
import java.util.List;

public class Snake {

    public static final int MAX_LENGTH = 32;
    public static final int MAX_ARRAY = 33;
    private Position []snakePositions = new Position[MAX_ARRAY];
    private int snakeLength;
    private int snakeHeadIndex;
    private int snakeTailIndex;
    private SnakeDirection currentDirection;
    private SnakeDirection nextDirection;

    public Snake() {
        snakeLength = 2;
        snakeTailIndex = 0;
        snakeHeadIndex = 1;

        snakePositions[0] = new Position(1,1);
        snakePositions[1] = new Position(2, 1);
        currentDirection = nextDirection = SnakeDirection.SNAKE_RIGHT;
    }

    public Position getTailPosition() {

        return snakePositions[snakeTailIndex];
    }

    public List<Position> getSnakeBodyPositions() {
        int index = snakeTailIndex;
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < snakeLength; i++) {
            positions.add(snakePositions[index]);
            index = ((index + 1) < MAX_ARRAY) ? index + 1 : 0;
        }

        return positions;
    }

    public Position getHeadPosition() {
        return snakePositions[snakeHeadIndex];
    }

    public SnakeMoveResult advanceSnakeHead(int width, int height) {
        if (snakeLength > MAX_LENGTH) {
            return SnakeMoveResult.SNAKE_ERROR;
        }

        int headX, headY;
        Position newHeadPos;

        headX = getHeadPosition().getX();
        headY = getHeadPosition().getY();

        switch (nextDirection) {
            case SNAKE_UP:
                headY = (headY == height - 1) ? 0 : headY + 1;
                break;
            case SNAKE_DOWN:
                headY = (headY == 0) ? height - 1 : headY - 1;
                break;
            case SNAKE_LEFT:
                headX = (headX == 0) ? width - 1 : headX - 1;
                break;
            case SNAKE_RIGHT:
                headX = (headX == width - 1) ? 0 : headX + 1;
        }

        newHeadPos = new Position(headX, headY);

        currentDirection = nextDirection;

        if (isSnakeAt(newHeadPos) && !newHeadPos.equals(snakePositions[snakeTailIndex])) {
            return SnakeMoveResult.COLLUSION;
        }

        snakeHeadIndex++;
        if (snakeHeadIndex == MAX_ARRAY) {
            snakeHeadIndex = 0;
        }
        snakePositions[snakeHeadIndex] = newHeadPos;
        snakeLength++;

        if (GameManager.getManager().getFoodManager().isFoodAt(newHeadPos) >= 0) {
            if (snakeLength <= MAX_LENGTH) {
                return SnakeMoveResult.SNAKE_ATE_FOOD;
            } else {
                return SnakeMoveResult.ATE_BUT_CANT_GROW;
            }
        }


        return SnakeMoveResult.SNAKE_MOVE_OK;
    }

    public void advanceSnakeTail() {
        //Position previous = snakePositions[snakeTailIndex];

        snakeTailIndex++;
        if (snakeTailIndex == MAX_ARRAY) {
            snakeTailIndex = 0;
        }
        snakeLength--;
    }

    public boolean isSnakeAt(Position position) {
        int i = snakeTailIndex;
        while (i != snakeHeadIndex) {
            if (position.equals(snakePositions[i])) {
                return true;
            }
            i++;
            if (i > MAX_LENGTH) {
                i = 0;
            }
        }
        return position.equals(snakePositions[snakeHeadIndex]);
    }

    public void setNextDirection(SnakeDirection nextDirection) {

        if ((currentDirection == SnakeDirection.SNAKE_UP && nextDirection != SnakeDirection.SNAKE_DOWN) ||
                (currentDirection == SnakeDirection.SNAKE_DOWN && nextDirection != SnakeDirection.SNAKE_UP) ||
                (currentDirection == SnakeDirection.SNAKE_LEFT && nextDirection != SnakeDirection.SNAKE_RIGHT) ||
                (currentDirection == SnakeDirection.SNAKE_RIGHT && nextDirection != SnakeDirection.SNAKE_LEFT)) {
            this.nextDirection = nextDirection;
        }
    }
}
