package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodManager {

    public static final int MAX_FOOD = 8;


    private Position [] foodPositions = new Position[MAX_FOOD];
    private int amount = 0;


    public int isFoodAt(Position position) {
        for (int i = 0; i < amount; i++) {
            if (foodPositions[i].equals(position)) {
                return i;
            }
        }

        return -1;
    }

    public List<Position> getFoodPositions() {
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            positions.add(foodPositions[i]);
        }
        return positions;
    }

    public Position addNewFood(int width, int height) {
        if (amount == MAX_FOOD) {
            return null;
        }

        int x, y, attempts;
        Position test;

        x = 0;
        y = 0;
        attempts = 0;

        do {
            x = (x+3+attempts) % width;
            y = (y+5)%height;
            test = new Position(x, y);
            attempts++;

        } while (attempts < 100 &&
                (GameManager.getManager().getSnake().isSnakeAt(test) ||
                        isFoodAt(test) >= 0));

        if (attempts == 100) {
            return null;
        }

        foodPositions[amount++] = test;
        return test;
    }

    public Position getFoodPosition(int foodIndex) {
        return foodPositions[foodIndex];
    }

    public void removeFood(int foodIndex) {
        if (foodIndex < 0 || foodIndex >= amount) {
            return;
        }

        for (int i = foodIndex+1; i < amount; i++) {
            foodPositions[i - 1] = foodPositions[i];
        }
        amount--;
    }

}
