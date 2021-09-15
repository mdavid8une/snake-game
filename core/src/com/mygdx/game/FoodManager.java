package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodManager {

    // currently allow max 6 food items
    public static final int MAX_FOOD = 6;

    // current food positions
    private Position [] foodPositions = new Position[MAX_FOOD];

    // current amount of food, starts from 0, increases each time new food is added
    private int amount = 0;

    /**
     * If there is a food at teh given position returns the index of that position in the foodPositions array
     * or returns -1 if that position is not a food position
     * @param position
     * @return
     */
    public int isFoodAt(Position position) {
        for (int i = 0; i < amount; i++) {
            if (foodPositions[i].equals(position)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Iterates current amount of food, puts the food positions in a List and returns that list
     * @return
     */
    public List<Position> getFoodPositions() {
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            positions.add(foodPositions[i]);
        }
        return positions;
    }

    /**
     * Attempts to add a new food to foodPositions array
     * if successful increments food amount
     * @param width - width of the board
     * @param height - height of the board
     * @return newly added food's position or null
     */
    public Position addNewFood(int width, int height) {
        // already max food
        if (amount == MAX_FOOD) {
            return null;
        }

        int x, y, attempts;
        Position test;

        x = 0;
        y = 0;
        attempts = 0;

        // try positions until find an empty position or 100 attempts
        do {
            x = (x+3+attempts) % width; // get new x value
            y = (y+5)%height; // get new y value
            test = new Position(x, y); // new position
            attempts++; // new attempt

        } while (attempts < 100 &&
                (GameManager.getManager().getSnake().isSnakeAt(test) ||
                        isFoodAt(test) >= 0)); // stop iteration if food position valid or reached max attempts

        if (attempts == 100) {
            // attempted 100 times and couldn't find any good empty position
            return null;
        }
        // add the empty position as food position
        foodPositions[amount++] = test;
        return test; // return the new food position
    }

    /**
     * if foodIndex is valid it returns the foodPosition
     * it may throw indexOutofBounds error
     * @param foodIndex
     * @return
     */
    public Position getFoodPosition(int foodIndex) {
        return foodPositions[foodIndex];
    }

    /**
     * if food index is valid, it removes the foodPositions[foodIndex]
     * @param foodIndex
     */
    public void removeFood(int foodIndex) {
        if (foodIndex < 0 || foodIndex >= amount) {
            return;
        }

        for (int i = foodIndex+1; i < amount; i++) {
            foodPositions[i - 1] = foodPositions[i];
        }
        // decrement amount since food is removed
        amount--;
    }

}
