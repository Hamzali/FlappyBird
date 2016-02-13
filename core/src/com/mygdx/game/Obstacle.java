package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;


public class Obstacle {
    private static final float COLLISON_RECT_WIDTH = 13f;
    private static final float COLLISON_RECT_HEIGHT = 447f;
    private static final float COLLISON_CIRCLE_RADIUS = 33f;

    private final Circle floorCollisonCircle;
    private final Rectangle floorCollisonRect;
    private final Circle ceilingCollisonCircle;
    private final Rectangle ceilingCollisonRect;


    private static final float MAX_SPEED_PER_SEC = 100f;
    public static final float WIDTH = COLLISON_CIRCLE_RADIUS;
    private static final float HEIGHT_OFFSET = -400f;
    private static final float DISTANCE_FLOOR_CEILING = 225f;

    private float x = 0;
    private float y = 0;

    public float getX(){
        return x;
    }

    public Obstacle(){
        this.y = MathUtils.random(HEIGHT_OFFSET);

        this.floorCollisonRect = new Rectangle(x, y,
                COLLISON_RECT_WIDTH, COLLISON_RECT_HEIGHT);
        this.floorCollisonCircle = new Circle(x + floorCollisonRect.width / 2,
                y + floorCollisonRect.height, COLLISON_CIRCLE_RADIUS);

        this.ceilingCollisonRect = new Rectangle(x, floorCollisonCircle.y + DISTANCE_FLOOR_CEILING,
                COLLISON_RECT_WIDTH, COLLISON_RECT_HEIGHT);

        this.ceilingCollisonCircle = new Circle(x + ceilingCollisonRect.width / 2,
                ceilingCollisonRect.y , COLLISON_CIRCLE_RADIUS);


    }

    public void setPosition(float x){
        this.x = x;
        updateCollisonRect();
        updateCollisonCircle();
    }

    private void updateCollisonRect(){
        floorCollisonRect.setX(x);
        ceilingCollisonRect.setX(x);
    }

    private void updateCollisonCircle(){
        ceilingCollisonCircle.setX(x + ceilingCollisonRect.width / 2);

        floorCollisonCircle.setX(x + floorCollisonRect.width / 2);

    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(floorCollisonRect.getX(),
                floorCollisonRect.getY(),
                floorCollisonRect.width,
                floorCollisonRect.height);

        shapeRenderer.circle(floorCollisonCircle.x,
                floorCollisonCircle.y,
                COLLISON_CIRCLE_RADIUS);


        shapeRenderer.rect(ceilingCollisonRect.getX(),
                ceilingCollisonRect.getY(),
                ceilingCollisonRect.width,
                ceilingCollisonRect.height);

        shapeRenderer.circle(ceilingCollisonCircle.x,
                ceilingCollisonCircle.y,
                COLLISON_CIRCLE_RADIUS);
    }

    public void update(float delta){
        setPosition(x - (MAX_SPEED_PER_SEC * delta));
    }

    public boolean isColliding(Bird bird){

        return Intersector.overlaps(bird.getCollisonCircle(), floorCollisonCircle)
                || Intersector.overlaps(bird.getCollisonCircle(), ceilingCollisonCircle) ||
                Intersector.overlaps(bird.getCollisonCircle(), ceilingCollisonRect) ||
                Intersector.overlaps(bird.getCollisonCircle(), floorCollisonRect);

    }

    // For not to double check after passing an obstacle

    private boolean pointClaimed = false;

    public boolean isPointClaimed(){
        return pointClaimed;
    }

    public void markPointClaimed(){
        pointClaimed = true;
    }


}
