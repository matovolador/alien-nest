package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.Utils;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.fx.VisualEffect;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;


public class Boss extends Entity {

    public static final int bossHealth = 6000;

    Player player;
    boolean isPositioned;
    public int phase;
    Sprite bossWeaponOrigin;
    Sprite bossWeaponLong;
    boolean renderWeapon;
    Vector2 bulletSpawn;
    long bulletTimer;
    long laserTimer;
    long lastBulletSpawn;
    long landmineTimer;
    long lastLaserSpawn;
    long lastLandmineSpawn;
    long minionTimer;
    long lastMinionSpawn;
    boolean canMove;
    ArrayList<BulletBoss> bulletArray;
    ArrayList<Landmine> landmines;
    private long lastMoveTime;
    private long moveTimer;
    private boolean moveUp;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveDown;
    Rectangle laserRect;
    long laserDuration;
    public static int width = 100;
    public static int height = 99;
    boolean laserStarted;
    long laserInit;

    public Boss(Vector2 position,Player player) {
        super(position,bossHealth, width, height, true);
        super.setSpeed(200);
        this.player = player;
        this.player.score = Boss.bossHealth;
        super.animator = new Animator(PlayScreen.bossImages,100);
        isPositioned = false;
        phase = 0;
        bossWeaponOrigin = PlayScreen.bossWeaponBigImages;
        bossWeaponLong = PlayScreen.bossWeaponBigLongImages;
        renderWeapon = false;
        bulletTimer = 1000;
        landmineTimer = 2000;
        laserTimer = 2600;
        laserDuration = 1000;
        lastLaserSpawn = System.currentTimeMillis();
        lastBulletSpawn = System.currentTimeMillis();
        lastLandmineSpawn = System.currentTimeMillis();
        bulletSpawn = new Vector2(0,0);
        bulletArray = new ArrayList<BulletBoss>();
        landmines = new ArrayList<Landmine>();
        lastMinionSpawn = System.currentTimeMillis();
        minionTimer = 1300;
        canMove = true;
        laserRect = new Rectangle(0,0,0,0);
        laserStarted = false;
        laserInit = 0;
        moveTimer = 500;

    }

    @Override
    public void input(float delta) {

    }

    @Override
    public void update(float delta) {
        //SET PHASE ACCORDING TO HEALTH:
        if (health>Boss.bossHealth * 0.75) phase = 1;
        if (health<=Boss.bossHealth * 0.75) phase = 2;
        if (health<=Boss.bossHealth * 0.5)  phase = 3;
        if (health<=Boss.bossHealth * 0.25) phase = 4;
        updatePositions(delta);
        this.landmines.trimToSize();
        this.bulletArray.trimToSize();
        if (!this.isPositioned){
            this.positionSelf(delta);
        }else{
            if (phase == 1){
                moveSelf(delta);
                fireBullet(delta);
                shootLandmines(delta);
            }
            if (phase == 2){
                moveSelf(delta);
                fireBullet(delta);
                shootLandmines(delta);
                shootLaser(delta);
            }
            if (phase == 3){
                clearLaser();
                moveSelf(delta);
                shootLandmines(delta);
                spawnMinions(delta);
            }
            if (phase == 4){
                moveSelf(delta);
                fireBullet(delta);
                shootLandmines(delta);
                shootLaser(delta);
                spawnMinions(delta);
            }

        }

    }

    private void moveSelf(float delta) {
        if (canMove){
            ///MOVEMENT:
            if (System.currentTimeMillis() - lastMoveTime   > moveTimer   ) {
                int moveCap = 20;
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
            if (this.position.x < PlayScreen.gameViewport.getWorldWidth() - this.width * 1.5 ) this.position.x = (float)( PlayScreen.gameViewport.getWorldWidth() - this.width * 1.5 );
            if (this.position.y > PlayScreen.gameViewport.getWorldHeight() - this.height*2)
                this.position.y = PlayScreen.gameViewport.getWorldHeight() - this.height*2;
            if (this.position.y < 0 + height) this.position.y = 0+height;

        }
    }

    private void fireBullet(float delta) {
        if (System.currentTimeMillis() - this.lastBulletSpawn > this.bulletTimer + delta ){
            this.lastBulletSpawn = System.currentTimeMillis();
            bulletArray.add(new BulletBoss(0,1, PlayScreen.spaceBullets,150,this,10,10, player));
        }
    }

    private void updatePositions(float delta){
        super.updateCenter();
        this.updateBulletSpawn();
        for (int i=0;i<bulletArray.size();i++){
            bulletArray.get(i).update(delta);
        }
        for (int i=0;i<landmines.size();i++) {
            landmines.get(i).update(delta);
        }
        this.bossWeaponOrigin.setPosition(this.position.x, this.position.y+Boss.height-20);
        this.bossWeaponLong.setPosition(0,this.position.y+Boss.height-20);
        this.laserRect = new Rectangle (0,this.position.y+Boss.height-20,0+this.position.x,20);
    }

    public void updateBulletSpawn() {
        this.bulletSpawn.x = this.position.x - 10;
        this.bulletSpawn.y = this.position.y + this.height - 77;
    }

    private void spawnMinions(float delta){
        if (System.currentTimeMillis() - this.lastMinionSpawn > this.minionTimer + delta  ) {
            this.lastMinionSpawn = System.currentTimeMillis();
            for (int i = 0; i < 6; i++) {
                int x = (int) PlayScreen.gameViewport.getWorldWidth();
                int y = PlayScreen.random.nextInt((int) PlayScreen.gameViewport.getWorldHeight() - BossMinion.height);
                PlayScreen.entities.add(new BossMinion(new Vector2(x, y), 2, PlayScreen.alienImages, 300, player));
            }
        }
    }

    private void clearLaser(){
        this.laserStarted = false;
        this.renderWeapon = false;
        this.canMove = true;
    }

    private void shootLaser(float delta){
        if ((System.currentTimeMillis() - this.lastLaserSpawn > this.laserTimer + delta)) {
            this.lastLaserSpawn = System.currentTimeMillis();
            if (!laserStarted) {
                laserStarted = true;
                this.laserInit = System.currentTimeMillis();
            }
        }
        if (System.currentTimeMillis() - this.laserInit < this.laserDuration) {
            renderWeapon = true;
            canMove = false;
        } else {
            renderWeapon = false;
            canMove = true;
            laserStarted = false;
        }
        if (renderWeapon) {
            if (Collision.checkCollision(this.laserRect, new Rectangle(player.position.x, player.position.y, player.width, player.height))) {
                player.hurt(3);
            }
        }
    }

    private void shootLandmines(float delta){
        if ((System.currentTimeMillis() - this.lastLandmineSpawn > this.landmineTimer + delta)) {
            this.lastLandmineSpawn = System.currentTimeMillis();
            landmines.add(new Landmine(new Vector2(this.center.x - 15, this.center.y - 15), player,this));
        }


    }

    private void positionSelf(float delta) {
        if (this.position.x > PlayScreen.gameViewport.getWorldWidth() - this.width * 1.5 ){
            this.position.x -= speed * delta;
            this.isPositioned = false;
        }
        if (this.position.x < PlayScreen.gameViewport.getWorldWidth() - this.width * 1.5 ){
            this.position.x = (float)(PlayScreen.gameViewport.getWorldWidth() - this.width * 1.5);
            this.isPositioned = true;
            phase = 1;
        }


    }



    @Override
    public void render() {
        ///render self
        super.animator.animate(this.position.x,this.position.y);
        //render laser weapon if should

        //render landmines
        for(int i=0;i<this.landmines.size();i++){
            this.landmines.get(i).render();
        }
        if (renderWeapon){
            MyGame.batch.draw(bossWeaponOrigin,bossWeaponOrigin.getX(),bossWeaponOrigin.getY());
            MyGame.batch.draw(bossWeaponLong,0,bossWeaponOrigin.getY(),bossWeaponOrigin.getX(),20);
        }
        //render his bullets
        for(int i=0;i<this.bulletArray.size();i++){
            this.bulletArray.get(i).render();
        }
    }

    @Override
    public void hurt(int amount) {
        if (player == null) return;
        this.health-=amount;
        player.score=health;
        if (this.health<=0){
            player.score = 0;
            SoundManager.alienDied();
            //player.updateScore(Boss.bossHealth);
            ArrayList<Entity> copy;
            copy=new ArrayList<Entity>(PlayScreen.entities);
            copy.remove(this);
            PlayScreen.entities=copy;
            //Create explosion animation:
            Animator destroyed = new Animator(PlayScreen.alienDeathImages,100);
            VisualEffect explosion = new VisualEffect(destroyed,300,new Vector2(this.center.x-40,this.center.y-40));
            PlayScreen.visualEffects.add(explosion);


        }
    }
}
