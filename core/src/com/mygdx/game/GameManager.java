package com.mygdx.game;

public class GameManager {

    // There can be only 1 GameManager
    private static GameManager manager = null;

    // Snake of the game
    private Snake snake;
    // FoodManager
    private FoodManager foodManager;

    // game dimensions and speed
    private int width = 10;
    private int height = 10;
    private float gameSpeed = 1.0f;

    /**
     * Private constructor, use GameManager.getManager() to access the single manager
     */
    private GameManager() {
        snake = new Snake();
        foodManager = new FoodManager();
    }

    /**
     * To be called once if manager is null
     */
    public static void startUp() {
        System.out.println("start up");
        manager = new GameManager();
        manager.reset();
    }


    /**
     * Returns the manager if manager null starts the manager
     * @return
     */
    public static GameManager getManager() {
        if (manager == null)
            startUp();
        return manager;
    }

    /**
     * Resets the game
     * Resets the snake
     * Adds 3 new food to the game
     */
    public void reset() {
        this.snake = new Snake();
        this.foodManager = new FoodManager();
        this.gameSpeed = 1.0f;
        for (int i = 0; i < 3; i++) {
            foodManager.addNewFood(width, height);
        }
    }

    /**
     * Returns the snake
     * @return
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Returns the food manager
     * @return
     */
    public FoodManager getFoodManager() {
        return foodManager;
    }

    /**
     * Returns the game speed
     * @return
     */
    public float getGameSpeed() {
        return gameSpeed;
    }

    /**
     * Resets game speed
     */
    public void resetGameSpeed() {
        gameSpeed = 1.0f;
    }

    /**
     * To be caleld by SnakeGame in order to attempt to move the snake forward
     * @return
     */
    public boolean attemptToMoveSnake() {

        // Move the snake head forward which would automatically grow the snake by 1
        SnakeMoveResult result = snake.advanceSnakeHead(width, height);

        // if result OOB then snake had a collusion don't continue
        if (result == SnakeMoveResult.OUT_OF_BOUNDS) {
            return false;
        }
        // get new head position of the snake
        Position newHeadPosition = snake.getHeadPosition();

        // check if this new position of the snake head has food
        if (result == SnakeMoveResult.SNAKE_ATE_FOOD ||
                result == SnakeMoveResult.ATE_BUT_CANT_GROW) {
            // snake ate food so increment game speed, maybe also add points here to increase points (score)
            gameSpeed += 0.06f;
            if (gameSpeed > 3.0f) {
                gameSpeed = 3.0f;
            }
            // food has been ate, find that food
            int food = foodManager.isFoodAt(newHeadPosition);
            if (food != -1) {
                // remove that food
                foodManager.removeFood(food);
                // try to add new food
                foodManager.addNewFood(width, height);
            }
        }
        // snake did not ate FOOD or it can not grow anymore, then move the snake tail which would shrink the snake by 1
        if (result == SnakeMoveResult.SNAKE_MOVE_OK ||
                result == SnakeMoveResult.ATE_BUT_CANT_GROW) {
            snake.advanceSnakeTail();
        }
        return true;
    }

}
