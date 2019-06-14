package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.data.Utils;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.fx.VisualEffect;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

public class EnemyShipOne extends Entity {
    Player target;
    ArrayList<BulletEnemy> bulletArray;
    public static final int width = 56;
    public static final int height = 45;
    Vector2 bulletSpawn;
    double fireTimer;
    double lastFireTime;
    boolean moveUp;
    boolean moveDown;
    boolean moveLeft;
    boolean moveRight;
    double moveTimer;
    double lastMoveTime;
    boolean isPositioned;
    public EnemyShipOne(Vector2 position, int health, Sprite sprite, boolean isEnemy, Player player) {
        super(position,50, width, height, isEnemy);
        super.position.x = PlayScreen.gameViewport.getWorldWidth();
        super.position.y = PlayScreen.gameViewport.getWorldHeight()/2 - this.height/2;
        ArrayList<Sprite> sprites = new ArrayList<Sprite>();
        sprites.add(sprite);
        super.animator = new Animator(sprites,300);
        super.setSpeed(300);
        target = player;
        bulletArray = new ArrayList<BulletEnemy>();
        fireTimer = 1000;
        rotation = 0;
        lastFireTime = System.currentTimeMillis();
        moveTimer = 500;
        lastMoveTime = System.currentTimeMillis();
        isPositioned = false;
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        this.bulletSpawn = new Vector2(0,0);
        super.updateCenter();
        this.updateBulletSpawn();

    }
    public void updateBulletSpawn() {
        this.bulletSpawn.x = (float)(this.center.x + (this.position.x -this.center.x) * Math.cos(rotation) - (this.position.y + this.height/2-this.center.y) * Math.sin(rotation));
        this.bulletSpawn.y = (float)(this.center.y + (this.position.x -this.center.x) * Math.sin(rotation) + (this.position.y + this.height/2-this.center.y) * Math.cos(rotation));
        return;
    }

    public void rotate(float angle){
        super.rotate(angle);
    }

    @Override
    public void input(float delta) {

    }

    public void update(float delta) {

        super.updateCenter();
        this.updateBulletSpawn();
        if (!isPositioned){
            this.positionSelf(delta);
            return;
        }
        float angle =  MathUtils.atan2(this.position.y - target.position.y, this.position.x - target.position.x);
        this.rotate(angle);


        if (System.currentTimeMillis() - this.lastFireTime > this.fireTimer ){
            this.lastFireTime = System.currentTimeMillis();
            this.bulletArray.add(new BulletEnemy(0,1, PlayScreen.spaceBullets,150,this,10,10));
        }
        for (int i=0;i<bulletArray.size();i++){
            bulletArray.get(i).update(delta);

        }

        ///MOVEMENT:
        if (System.currentTimeMillis() - lastMoveTime   > moveTimer   ) {
            int moveCap = 40;
            lastMoveTime = System.currentTimeMillis();
            int rand = Utils.doRandom(1, 100);
            if (rand >= 1 && rand < moveCap) {
                moveUp = true;
                moveDown = false;
            } else {
                if (rand >= moveCap && rand < moveCap * 2) {
                    moveDown = true;
                    moveUp = false;
                } else {
                    moveDown = false;
                }
            }
            rand = Utils.doRandom(1, 100);
            if (rand >= 1 && rand < moveCap) {
                moveLeft = true;
                moveRight = false;
            } else {
                if (rand >= moveCap && rand < moveCap * 2) {
                    moveRight = true;
                    moveLeft = false;
                } else {
                    moveRight = false;
                }
            }
        }
        if (moveUp) {
            this.position.y += this.speed * delta;
        }
        if (moveDown) {
            this.position.y -= this.speed * delta;
        }
        if (moveLeft) {
            this.position.x -= this.speed * delta;
        }
        if (moveRight) {
            this.position.x += this.speed * delta;
        }

        if (this.position.x > PlayScreen.gameViewport.getWorldWidth() - this.width)
            this.position.x = PlayScreen.gameViewport.getWorldWidth()- this.width;
        if (this.position.x < PlayScreen.gameViewport.getWorldWidth()/1.5) this.position.x = (float)( PlayScreen.gameViewport.getWorldWidth()/1.5);
        if (this.position.y > PlayScreen.gameViewport.getWorldHeight() - this.height)
            this.position.y = PlayScreen.gameViewport.getWorldHeight() - this.height;
        if (this.position.y < 0) this.position.y = 0;
    }

    private void positionSelf(float delta) {
        if (this.isPositioned) return;
        if (this.position.x > PlayScreen.gameViewport.getWorldWidth() - this.width * 2){
            this.position.x -= speed/2 * delta;
            this.isPositioned = false;
            StageHandler.isCombatEnabled = false;
        }
        if (this.position.x <= PlayScreen.gameViewport.getWorldWidth() - this.width * 2) {
            this.position.x = PlayScreen.gameViewport.getWorldWidth() - this.width * 2;
            this.isPositioned = true;
            StageHandler.isCombatEnabled = true;
        }
        return;
    }


    @Override
    public void render() {
        super.animator.animate(this.position.x,this.position.y);
        for(int i=0;i<this.bulletArray.size();i++){
            this.bulletArray.get(i).render();
        }
    }

    @Override
    public void hurt(int amount) {
        if (target == null) return;
        if (!this.isPositioned) return;
        this.health-=amount;
        if (this.health<=0){
            target.updateScore(1);
            ArrayList<Entity> copy;
            copy=new ArrayList<Entity>(PlayScreen.entities);
            copy.remove(this);
            PlayScreen.entities=copy;


            //Create explosion animation:
            Animator destroyed = new Animator(PlayScreen.explosionImages,200);
            VisualEffect explosion = new VisualEffect(destroyed,600,new Vector2(this.center.x-40,this.center.y-40));
            PlayScreen.visualEffects.add(explosion);
            SoundManager.playSound(SoundManager.explosion);


        }else{
            SoundManager.playSound(SoundManager.shipDamage);
        }
    }
}
