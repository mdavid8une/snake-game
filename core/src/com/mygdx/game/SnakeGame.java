package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SnakeGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	int windowW = 500;
	int windowH = 500;

	int boxSize = 50;
	int width = 10;
	int height = 10;

	OrthographicCamera camera;
	FitViewport viewport;
	ShapeRenderer shape;

	// snake will default move every half seconds
	long lastMoveTime = 0;


	/**
	 * When Game starts this method is called first
	 */
	@Override
	public void create () {
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		// start the game manager
		GameManager.startUp();

		// setup camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(10, 10);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		// setup shape renderer instead of shape renderer spritebatch can also be used to render images instead of shapes
		shape = new ShapeRenderer();

		// viewport for the camera
		viewport = new FitViewport(10, 10, camera);

	}

	/**
	 * When window is resized this method is called
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}


	/**
	 * Not used, supposed to be used when libgdx reverses y coordinate
	 * @param y
	 * @return
	 */
	private float toScreenY(float y) {
		float height = Gdx.graphics.getHeight();

//		return height - y;
		return y;
	}

	/**
	 * This method is called by libgdx repeatedly
	 * amount of fps (frames per second) is directly related to how many times per second this method can run
	 */
	@Override
	public void render () {
		long currentTime = System.currentTimeMillis();

		// if 500ms passed since last movetime, move the snake
		if (currentTime > lastMoveTime + 500) {
			lastMoveTime = currentTime;
			GameManager.getManager().attemptToMoveSnake();
		}

		// check for player inputs
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			GameManager.getManager().getSnake().setNextDirection(SnakeDirection.SNAKE_LEFT);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			GameManager.getManager().getSnake().setNextDirection(SnakeDirection.SNAKE_RIGHT);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			GameManager.getManager().getSnake().setNextDirection(SnakeDirection.SNAKE_UP);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			GameManager.getManager().getSnake().setNextDirection(SnakeDirection.SNAKE_DOWN);
		}

		// rendering view to screen
		ScreenUtils.clear(0, 0, 0, 1); // clear the screen to black
		shape.setProjectionMatrix(camera.combined); //shapeRendered projection matrix set to camera


		shape.begin(ShapeRenderer.ShapeType.Filled); // begin rendering
		shape.setColor(Color.RED); // color set red for snake body
		// iterate snake positions and fill those positions red rectangle
		for (Position pos : GameManager.getManager().getSnake().getSnakeBodyPositions()) {
			float x = pos.getX();
			float y = pos.getY();
			y = toScreenY(y);
			shape.rect(x, y, 1, 1);
		}
		shape.setColor(Color.ORANGE); // set color orange for head
		Position head = GameManager.getManager().getSnake().getHeadPosition();
		shape.rect(head.getX(), head.getY(), 1, 1); // draw the head of snake

		// set color blue for food
		shape.setColor(Color.BLUE);
		for (Position pos : GameManager.getManager().getFoodManager().getFoodPositions()) {
			float x = pos.getX();
			float y = pos.getY();
			y = toScreenY(y);
			shape.rect(x, y, 1, 1); // draw the food
		}
		shape.end(); // rendering finished
		//batch.begin();



		//batch.end();
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//img.dispose();
		shape.dispose();

	}
}
