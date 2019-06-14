package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;

import java.util.ArrayList;

//TODO
public class Laser extends Entity {
    Animator animator;
    Player holder;
    public Laser(int health, int width, int height, ArrayList<Sprite> sprites, Player holder){
        super(new Vector2(holder.bulletSpawn) , health, width, height, false);
        super.power = 3;
        this.animator=new Animator(sprites,200);


    }
    @Override
    public void input(float delta) {

    }

    @Override
    public void update(float delta) {
        updateCenter();
    }

    @Override
    public void render() {
        super.animator.animate(this.position.x,this.position.y);

    }

    @Override
    public void hurt(int amount) {

    }
}
