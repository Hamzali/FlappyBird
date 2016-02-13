package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;


public class Bird {

    private static final float COLLISON_RADIUS = 24f;
    private final Circle collisonCircle;

    private static final float DIVE_ACCEL = 0.3f;
    private static final float FLY_ACCEL = 5f;
    private float ySpeed = 0;


    private float x = 0;
    private float y = 0;


    public Bird(){
        collisonCircle = new Circle(x, y, COLLISON_RADIUS);
    }

    public float getX(){
        return x;
    }

    public float gety(){
        return y;
    }

    public Circle getCollisonCircle(){
        return collisonCircle;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisonCircle();
    }

    public void updateCollisonCircle(){
        collisonCircle.setX(x);
        collisonCircle.setY(y);
    }

    public void flyUp(){
        ySpeed = FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }


    public void update(){
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.circle(collisonCircle.x,
                collisonCircle.y,
                collisonCircle.radius);

    }


}
