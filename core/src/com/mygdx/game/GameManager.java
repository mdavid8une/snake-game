package com.mygdx.game;

public class GameManager {

    private static GameManager manager = null;

    private Snake snake;
    private FoodManager foodManager;
    private int width = 10;
    private int height = 10;
    private float gameSpeed = 1.0f;

    private GameManager() {
        snake = new Snake();
        foodManager = new FoodManager();
    }

    public static void startUp() {
        System.out.println("start up");
        manager = new GameManager();
        manager.reset();
    }



    public static GameManager getManager() {
        if (manager == null)
            startUp();
        return manager;
    }

    public void reset() {
        this.snake = new Snake();
        this.foodManager = new FoodManager();
        this.gameSpeed = 1.0f;
        for (int i = 0; i < 3; i++) {
            foodManager.addNewFood(width, height);
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public FoodManager getFoodManager() {
        return foodManager;
    }

    public float getGameSpeed() {
        return gameSpeed;
    }

    public void resetGameSpeed() {
        gameSpeed = 1.0f;
    }

    public boolean attemptToMoveSnake() {
        Position headPosition = snake.getHeadPosition();

        SnakeMoveResult result = snake.advanceSnakeHead(width, height);

        if (result == SnakeMoveResult.OUT_OF_BOUNDS) {
            return false;
        }

        Position newHeadPosition = snake.getHeadPosition();

        if (result == SnakeMoveResult.SNAKE_ATE_FOOD ||
                result == SnakeMoveResult.ATE_BUT_CANT_GROW) {
            gameSpeed += 0.06f;
            if (gameSpeed > 3.0f) {
                gameSpeed = 3.0f;
            }
            int food = foodManager.isFoodAt(newHeadPosition);
            if (food != -1) {
                foodManager.removeFood(food);
            }
            foodManager.addNewFood(width, height);
        }
        if (result == SnakeMoveResult.SNAKE_MOVE_OK ||
                result == SnakeMoveResult.ATE_BUT_CANT_GROW) {
            snake.advanceSnakeTail();
        }
        return true;
    }

}
