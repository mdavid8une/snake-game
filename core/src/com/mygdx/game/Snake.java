package com.mygdx.game;


import java.util.ArrayList;
import java.util.List;

public class Snake {

    // Max Length currently 32 but this should be (width * height)
    public static final int MAX_LENGTH = 32;
    // max array length 33 for circular implementation but this should be (w * h) + 1
    public static final int MAX_ARRAY = 33;
    // current snake positions
    private Position []snakePositions = new Position[MAX_ARRAY]; // circular array

    // current snake length
    private int snakeLength;
    // index of the snake's head position in snakePositions
    private int snakeHeadIndex;
    // index of the snake's tail position in snakePositions
    private int snakeTailIndex;

    // the direction snake is heading
    private SnakeDirection currentDirection;
    // the direction snake wants to head next time step
    private SnakeDirection nextDirection;

    /**
     * Construct a snake with length 2 and puts the snake at position (1,1) (2,1)
     * Snake defaults moves right
     */
    public Snake() {
        snakeLength = 2;
        snakeTailIndex = 0;
        snakeHeadIndex = 1;

        snakePositions[0] = new Position(1,1);
        snakePositions[1] = new Position(2, 1);

        currentDirection = nextDirection = SnakeDirection.SNAKE_RIGHT;
    }

    /**
     * Returns tail position of the snake
     * @return
     */
    public Position getTailPosition() {

        return snakePositions[snakeTailIndex];
    }

    /**
     * Returns the body positions of the snake including the tail and head
     * @return
     */
    public List<Position> getSnakeBodyPositions() {
        // start from tail index
        int index = snakeTailIndex;
        List<Position> positions = new ArrayList<>();

        // iterate snake length
        for (int i = 0; i < snakeLength; i++) {
            // add the position at current index to the result
            positions.add(snakePositions[index]);
            // increment index by 1, if reach end of the array circle back to index 0
            index = ((index + 1) < MAX_ARRAY) ? index + 1 : 0;
        }

        return positions;
    }

    /**
     * Returns the head position of the snake
     * @return
     */
    public Position getHeadPosition() {
        return snakePositions[snakeHeadIndex];
    }

    /**
     * Attempts to move the Snake head forward towards currentDirection
     * Returns SnakeMoveResult based on the movement
     * @param width width of the board
     * @param height height of the board
     * @return
     */
    public SnakeMoveResult advanceSnakeHead(int width, int height) {
        // snake len should never exceed max length
        if (snakeLength > MAX_LENGTH) {
            return SnakeMoveResult.SNAKE_ERROR;
        }

        int headX, headY;
        Position newHeadPos;
        // current head position
        headX = getHeadPosition().getX();
        headY = getHeadPosition().getY();

        // calculate next head position
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
        // create new head position
        newHeadPos = new Position(headX, headY);
        // update current direction
        currentDirection = nextDirection;

        // Snake colluded with itself
        if (isSnakeAt(newHeadPos) && !newHeadPos.equals(snakePositions[snakeTailIndex])) {
            return SnakeMoveResult.COLLUSION;
        }

        // update snake headindex since head moved to new position
        snakeHeadIndex++;

        // snake head index invalid so circle back to 0
        if (snakeHeadIndex == MAX_ARRAY) {
            snakeHeadIndex = 0; // because circular array
        }
        // add new snake head position to snakepositions array and increment its size
        snakePositions[snakeHeadIndex] = newHeadPos;
        snakeLength++; // grow snake

        // check if snake ate food
        if (GameManager.getManager().getFoodManager().isFoodAt(newHeadPos) >= 0) {
            if (snakeLength <= MAX_LENGTH) {
                return SnakeMoveResult.SNAKE_ATE_FOOD;
            } else {
                return SnakeMoveResult.ATE_BUT_CANT_GROW;
            }
        }

        // Snake did not ate food but managed to move forward without collusion
        return SnakeMoveResult.SNAKE_MOVE_OK;
    }

    /**
     * To be called after succesful atempt to move snake head and if snake did not ate food
     */
    public void advanceSnakeTail() {

        // move snake tail
        snakeTailIndex++;

        if (snakeTailIndex == MAX_ARRAY) {
            snakeTailIndex = 0; // because of circular array implementation reset to 0
        }
        snakeLength--; // since not ate food shrink the length that is grow in the advance_snake_head metho
    }

    /**
     * // returns true iff given position belongs to snake
     * @param position
     * @return
     */
    public boolean isSnakeAt(Position position) {
        int i = snakeTailIndex;
        // check every position starting from tail until head
        while (i != snakeHeadIndex) {
            // if a snake position matches the given position return true here
            if (position.equals(snakePositions[i])) {
                return true;
            }
            i++;
            if (i > MAX_LENGTH) {
                i = 0;
            }
        }
        // finally check if the head position equals to the given position
        return position.equals(snakePositions[snakeHeadIndex]);
    }

    /**
     * Sets the new snake direction, the head will move to that direction next
     * @param nextDirection
     */
    public void setNextDirection(SnakeDirection nextDirection) {

        if ((currentDirection == SnakeDirection.SNAKE_UP && nextDirection != SnakeDirection.SNAKE_DOWN) ||
                (currentDirection == SnakeDirection.SNAKE_DOWN && nextDirection != SnakeDirection.SNAKE_UP) ||
                (currentDirection == SnakeDirection.SNAKE_LEFT && nextDirection != SnakeDirection.SNAKE_RIGHT) ||
                (currentDirection == SnakeDirection.SNAKE_RIGHT && nextDirection != SnakeDirection.SNAKE_LEFT)) {
            this.nextDirection = nextDirection;
        }
    }
}
