package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.fx.VisualEffect;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;



public class Landmine extends Entity {
    boolean isPositioned;
    Player target;
    Vector2 targetPos;
    boolean init;
    double scale_X;
    double scale_Y;
    private boolean minorX;
    private boolean minorY;
    Boss holder;

    public Landmine(Vector2 position, Player player, Boss holder) {
        super(position, 1, 30, 30, true);
        super.setSpeed(200);
        ArrayList<Sprite> sprites = new ArrayList<Sprite>();
        sprites.add(PlayScreen.landmine);
        super.animator = new Animator(sprites,200);
        this.target = player;
        targetPos = new Vector2(player.center.x,player.center.y);
        init = false;
        this.holder=holder;
    }

    @Override
    public void input(float delta) {

    }

    @Override
    public void update(float delta) {
        updateCenter();
        if (!isPositioned){
            positionSelf(delta);

        }else{
            //Check if collision with player
            if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, target)) {
                if (target.isHurt == false) {
                    target.hurt(this.power);
                }
                this.destroy();
            }
        }
    }

    private void positionSelf(float delta){
        if (!init){
            init = true;
            minorX = false;
            minorY = false;
            if (targetPos.x<this.position.x + width/2) minorX = true;
            if (targetPos.y<this.position.y + width/2) minorY = true;

            double angle = Math.atan2( target.center.y - this.center.y ,  target.center.x - this.center.x  ) ;
            this.scale_X = Math.cos(angle);
            this.scale_Y = Math.sin(angle);
            SoundManager.bulletFired();
        }
        if (targetPos.x != this.position.x + width/2) {
            this.position.x += speed * scale_X * delta;
            if (minorX) {
                if (this.position.x + width/2< targetPos.x) this.position.x = targetPos.x-this.width/2;
            } else {
                if (this.position.x+ width/2 > targetPos.x) this.position.x = targetPos.x-this.width/2;
            }
            isPositioned = false;
            super.animator.rotationTimer = 1;
            super.animator.rotationAmount = -30;
        }
        if (targetPos.y != this.position.y+height/2) {
            this.position.y+=speed*scale_Y*delta;
            if (minorY) {
                if (this.position.y+this.height/2 < targetPos.y) this.position.y = targetPos.y - this.height/2;
            }else{
                if (this.position.y+this.height/2 > targetPos.y) this.position.y = targetPos.y -this.height/2;
            }
            isPositioned = false;
            super.animator.rotationTimer = 1;
            super.animator.rotationAmount = -30;
        }
        if (targetPos.x == this.position.x+width/2 && targetPos.y == this.position.y+height/2) {
            isPositioned = true;
            super.animator.rotationTimer = 0;
            super.animator.rotationAmount = 0;

        }

    }

    private void destroy(){
        ArrayList<Landmine> copy;
        copy = new ArrayList<Landmine>(holder.landmines);
        copy.remove(this);
        holder.landmines = copy;

        //Create explosion animation:
        Animator destroyed = new Animator(PlayScreen.explosionImages, 200);
        VisualEffect explosion = new VisualEffect(destroyed, 600, new Vector2(this.center.x - 40, this.center.y - 40));
        PlayScreen.visualEffects.add(explosion);
        SoundManager.playSound(SoundManager.explosion);
    }
    @Override
    public void render() {
        double angle = this.rotation;
        angle= (float)(MathUtils.radiansToDegrees * angle);
        if (angle < 0) angle += 360;
        if (! (angle ==0) && ! (angle == 360)) super.animator.degrees =(float) angle;
        super.animator.animate(this.position.x,this.position.y);

    }

    @Override
    public void hurt(int amount) {
        if (target == null) return;
        if (!this.isPositioned) return;
        this.health-=amount;
        if (this.health<=0) {
            ArrayList<Landmine> copy;
            copy = new ArrayList<Landmine>(holder.landmines);
            copy.remove(this);
            holder.landmines = copy;


            //Create explosion animation:
            Animator destroyed = new Animator(PlayScreen.explosionImages, 200);
            VisualEffect explosion = new VisualEffect(destroyed, 600, new Vector2(this.center.x - 40, this.center.y - 40));
            PlayScreen.visualEffects.add(explosion);
            SoundManager.playSound(SoundManager.explosion);
        }
    }
}
