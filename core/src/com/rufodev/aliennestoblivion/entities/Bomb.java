package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

//TODO
public class Bomb extends Entity {
    boolean fired ;
    Animator animator;
    double firedTime;
    double duration;
    boolean ended;
    Player holder;
    double lastDamageTime;
    double damageTimer;
    double renderDuration;
    double lastRenderTime;
    int power;
    final int width = 180;
    final int height = 180;
    boolean needsRender = false;
    boolean rotateIt = false;
    public Bomb(Sprite img, Player holder){
        super( new Vector2(0,0) , 0, 0, 0, false);
        fired = false;
        ended = false;
        ArrayList<Sprite> sprites = new ArrayList<Sprite>();
        sprites.add(img);
        this.animator = new Animator(sprites,50);
        duration = 4000;
        this.holder= holder;
        this.damageTimer = 400;
        this.renderDuration = 200;
        this.power = 3;
        this.animator.setAlpha(0.5f);
    }
    @Override
    public void input(float delta) {

    }

    @Override
    public void update(float delta) {
        if (!fired){
            this.fired = true;
            this.firedTime = System.currentTimeMillis();
            this.lastDamageTime = 0;
            this.lastRenderTime = 0;

        }
        if (this.firedTime + this.duration <= System.currentTimeMillis()){
            this.ended=true;
        }

        if (!ended){
            //run object code:
            if (this.lastDamageTime + this.damageTimer <= System.currentTimeMillis()) {
                needsRender = true;
                rotateIt = true;
                this.lastDamageTime = System.currentTimeMillis();
                this.lastRenderTime = System.currentTimeMillis();
                for (int i = 0; i < PlayScreen.entities.size(); i++) {
                    if (!(PlayScreen.entities.get(i) instanceof Player) && (PlayScreen.entities.get(i).isEnemy))
                        (PlayScreen.entities.get(i)).hurt(this.power);
                }
            }
            if (System.currentTimeMillis() >= lastRenderTime + renderDuration){
                needsRender = false;
            }

        }else{
            //remove this entity from the game loop
            ArrayList<Entity> copy = new ArrayList<Entity>(PlayScreen.entities);
            copy.remove(this);
            PlayScreen.entities=copy;
        }

    }

    @Override
    public void render() {
        if (this.needsRender){
            int multX = (int) PlayScreen.gameViewport.getWorldWidth()/this.width;
            int multY = (int) PlayScreen.gameViewport.getWorldHeight()/this.height;
            if (rotateIt){
                this.animator.degrees+=90;
                rotateIt = false;
            }
            for (int i=0;i<multX+1;i++){
                for (int h=0;h<multY+1;h++){
                    this.animator.animate(this.position.x+this.width*i,this.position.y+this.height*h);
                }
            }
        }
    }

    @Override
    public void hurt(int amount) {

    }
}
