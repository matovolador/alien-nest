package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;

import java.util.ArrayList;
import java.util.HashMap;

public class EnemySpawner {
    public float width;
    public float height;
    public Vector2 position;
    public Animator animator;
    //TODO FIX THIS: public HashMap<Integer,Float, Float> whatSpawns;
    public EnemySpawner(Vector2 position,float width, float height, ArrayList<Sprite> sprites, HashMap<Integer,Float> whatSpawns){
        this.position = position;
        this.height = height;
        this.width = width;
        this.animator = new Animator(sprites,200);


    }


}
