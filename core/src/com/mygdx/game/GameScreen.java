package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_HEIGHT = 640;
    private static final float WORLD_WIDTH = 480;

    private static final float GAP_BETWEEN_FLOWERS = 200f;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Bird flappyBird = new Bird();
    private Array<Obstacle> obstacles = new Array<Obstacle>();

    private int score = 0;

    // Graphical assets
    private Texture background = new Texture(Gdx.files.internal("assets/bg.jpg"));



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();

        flappyBird.setPosition(WORLD_WIDTH / 4 ,WORLD_HEIGHT / 2);
    }

    public void render(float delta){
        clearScreen();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        // For rendering sprites.
        batch.draw(background,0 ,0);

        drawScore();

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Drawing with code.
        flappyBird.drawDebug(shapeRenderer);

        drawDebugObstacle(shapeRenderer);

        shapeRenderer.end();

        update(delta);
    }

    private void update(float delta){
        flappyBird.update();
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))flappyBird.flyUp();
        worldLimits();
        updateObstacles(delta);
        updateScore();
        if(checkForCollison())restart();
    }

    private void worldLimits(){// Avoids the bird from leaving the screen.
        flappyBird.setPosition(flappyBird.getX(),
                MathUtils.clamp(flappyBird.gety(), 0, WORLD_HEIGHT));
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void updateObstacles(float delta){

        for (Obstacle obstacle: obstacles){
            obstacle.update(delta);
        }
        checkIfObstacleNeeded();
        removeObstacleIfPassed();
    }

    private void createNewObstacle(){
        Obstacle obstacle = new Obstacle();
        obstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH);
        obstacles.add(obstacle);
    }

    private void removeObstacleIfPassed(){
        if(obstacles.size > 0){
            Obstacle firstObstacle = obstacles.first();
            if(firstObstacle.getX() < -Obstacle.WIDTH){
                obstacles.removeValue(firstObstacle, true);
            }
        }
    }

    private void checkIfObstacleNeeded(){
        if(obstacles.size == 0){

            createNewObstacle();

        }else{
            Obstacle obstacle = obstacles.peek();
            if(obstacle.getX() < WORLD_WIDTH - GAP_BETWEEN_FLOWERS){
                createNewObstacle();
            }
        }
    }

    private boolean checkForCollison(){
        for(Obstacle obstacle: obstacles){
            if(obstacle.isColliding(flappyBird))return true;
        }
        return false;
    }

    private void updateScore(){
        Obstacle obstacle = obstacles.first();

        if(flappyBird.getX() > obstacle.getX() && !obstacle.isPointClaimed()){
            score++;
            obstacle.markPointClaimed();
        }
    }

    private void drawScore(){

        glyphLayout.setText(bitmapFont, Integer.toString(score));
        bitmapFont.draw(batch, Integer.toString(score),
                (viewport.getWorldWidth() - glyphLayout.width) / 2,
                viewport.getWorldHeight() * 4 / 5 - glyphLayout.height / 2
                );
    }

    private void restart(){
        flappyBird.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        score = 0;
        obstacles.clear();
    }

    private void drawDebugObstacle(ShapeRenderer shapeRenderer){
        for(Obstacle obstacle : obstacles){

            obstacle.drawDebug(shapeRenderer);
        }
    }


}

