package com.rufodev.aliennestoblivion.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.entities.Alien;
import com.rufodev.aliennestoblivion.entities.AcidAlien;
import com.rufodev.aliennestoblivion.entities.BlackBulletItem;
import com.rufodev.aliennestoblivion.entities.BlueBulletItem;
import com.rufodev.aliennestoblivion.entities.Bomb;
import com.rufodev.aliennestoblivion.entities.Boss;
import com.rufodev.aliennestoblivion.entities.EnemyShipOne;
import com.rufodev.aliennestoblivion.entities.FastAlien;
import com.rufodev.aliennestoblivion.entities.GreenCapsuleItem;
import com.rufodev.aliennestoblivion.entities.Player;
import com.rufodev.aliennestoblivion.entities.PurpleCapsuleItem;
import com.rufodev.aliennestoblivion.entities.RedBulletItem;
import com.rufodev.aliennestoblivion.fx.Map;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

public class StageHandler {

    public long lastCapsuleSpawn;
    public long SpawnTimerCapsule;
    public  long lastHealthSpawn;
    public long SpawnTimerHealth;
    public long lastASpawn;
    public long SpawnATimer;
    public long lastBSpawn;
    public long SpawnBTimer;
    public long lastCSpawn;
    public long SpawnCTimer;
    public int stageId;
    public boolean extraction;
    public boolean survive;
    public boolean untilDeath;
    public boolean boss;
    public boolean scoreBased;
    public long extraTimer;
    public int scoreGoal;
    public float eventX;
    public boolean isBossSpawned;
    public boolean isPlayerExtracted;
    public long stageEndTimer;
    public long stageZeroTimer;
    public static ArrayList<Rectangle> collisions;
    public int spawnIterator;
    public int killCount;
    public static boolean isCombatEnabled;
    private long lastBombSpawn;
    private long SpawnTimerBomb;
    public boolean killBased;


    public StageHandler(int stageId){
        this.stageId = stageId;
        this.extraction = false;
        this.survive = false;
        this.untilDeath = false;
        this.boss=false;
        this.isBossSpawned = false;
        this.isPlayerExtracted = false;
        this.stageZeroTimer = System.currentTimeMillis();
        this.eventX = 0;
        this.stageEndTimer = 0;
        this.lastASpawn = System.currentTimeMillis();
        this.SpawnATimer = 500;
        this.lastBSpawn = System.currentTimeMillis();
        this.SpawnBTimer = 10000;
        this.lastCSpawn = System.currentTimeMillis();
        this.SpawnCTimer = 650;
        this.lastCapsuleSpawn = System.currentTimeMillis();
        this.SpawnTimerCapsule = 2000;
        this.lastHealthSpawn = System.currentTimeMillis();
        this.SpawnTimerHealth = 5000;
        this.SpawnTimerCapsule =  2000;
        this.SpawnTimerBomb = 10500;
        this.lastBombSpawn = System.currentTimeMillis();
        this.stageZeroTimer = System.currentTimeMillis();
        this.stageEndTimer = 0;
        this.collisions = new ArrayList<Rectangle>(); //inverted Y
        this.spawnIterator = 0;
        this.killCount = 0;
        isCombatEnabled = false;
        this.scoreBased = false;
        this.scoreGoal = 0;
        this.extraTimer = 0;
        this.killBased = false;

        //TODO SET TIMERS DEPENDING ON gameStage
        switch(stageId){
            case 1:
                scoreBased = true;
                scoreGoal = 750;
                this.extraTimer = 0;
                break;
            case 2:
                scoreBased = true;
                scoreGoal = 850;
                this.extraTimer = 0;
                collisions.add(0,new Rectangle(0,0,56,576));
                collisions.add(1,new Rectangle(0,0,1024,41));
                collisions.add(2,new Rectangle(0,535,1024,41));
                break;
            case 3:
                this.spawnIterator = 0;
                this.killCount=10;
                this.killBased = true;
                this.SpawnTimerCapsule =  3500;
                this.extraTimer = 0;
                break;
            case 4:
                scoreBased = true;
                scoreGoal = 450;
                this.extraTimer = 800;
                break;
            case 5:
                boss = true;
                this.extraTimer = 0;
                break;
            case 999:
                untilDeath = true;
                this.extraTimer = 0;
                break;
            default:
                untilDeath= true;
                this.extraTimer = 0;
                break;
        }
        this.setBackground(stageId);
    }



    private void initialize() {
    }

    public void update(PlayScreen screen, float delta) {
        if (screen.player.health<=0) screen.endGame(false);
        if (screen.player.score >= scoreGoal && scoreBased ) screen.endGame(true);
        if (screen.player.score>=killCount && killBased) screen.endGame(true);
        if (boss==true && isBossSpawned && screen.player.score <= 0) screen.endGame(true);



        //IF NOT:
        spawnStuff(screen, delta);
        /*
        if (screen.player.spawnBomb) {
            screen.entities.add(new Bomb(PlayScreen.bombEffect, screen.player));
            screen.player.bombCurrentCharge = 0;
            screen.player.bombEnabled = false;
            screen.player.spawnBomb = false;
        }
            */
    }

    private void spawnStuff(PlayScreen screen, float delta) {
        //Spawn shit according to stageId:
        if (stageId!=3 && stageId!=5) {
            isCombatEnabled = true;
            int where;
            boolean spawnRight = true;
            float speed;


            //Alien Spawn
            if (System.currentTimeMillis() - lastASpawn >= SpawnATimer + extraTimer) {
                speed = (float)(Utils.doRandom(250,400));
                lastASpawn = System.currentTimeMillis();
                int count = 3;
                if (stageId==4) count = 1;
                for (int i = 0; i < count; i++) {
                    if (stageId==4){
                        speed = (float) (speed*0.75);
                        where = Utils.doRandom(0,100);
                        if (where < 50){
                            spawnRight = true;
                        }else{
                            spawnRight = false;
                        }

                    }
                    if (spawnRight) {
                        int x = (int) screen.gameViewport.getWorldWidth();
                        int y = 0;
                        boolean hits = true;
                        while (hits) {
                            hits = false;
                            y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - Alien.height);
                            for (int h = 0; h < collisions.size(); h++) {
                                if (Collision.checkWalls(collisions.get(h), new Rectangle(x - Alien.width, y, Alien.width, Alien.height)))
                                    hits = true;
                            }
                        }
                        screen.entities.add(new Alien(new Vector2(x, y), 1, screen.alienImages, speed, screen.player));
                    }else{
                        int x = 0 - Alien.width;
                        int y = 0;
                        boolean hits = true;
                        while (hits) {
                            hits = false;
                            y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - Alien.height);
                            for (int h = 0; h < collisions.size(); h++) {
                                if (Collision.checkWalls(collisions.get(h), new Rectangle(x + Alien.width*2 , y, Alien.width, Alien.height)))
                                    hits = true;
                            }
                        }
                        screen.entities.add(new Alien(new Vector2(x, y), 1, screen.alienImages, speed, screen.player,false));
                    }
                }
            }
            //AcidAlien Spawn
            if (System.currentTimeMillis() - lastBSpawn >= SpawnBTimer + extraTimer) {
                speed = 200;
                lastBSpawn = System.currentTimeMillis();
                if (stageId==4){
                    speed = speed/2;
                    where = Utils.doRandom(0,100);
                    if (where < 50){
                        spawnRight = true;
                    }else{
                        spawnRight = false;
                    }

                }
                if (spawnRight) {
                    int x = (int) screen.gameViewport.getWorldWidth();
                    int y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - AcidAlien.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x - AcidAlien.width, y, AcidAlien.width, AcidAlien.height)))
                                hits = true;
                        }
                    }
                    screen.entities.add(new AcidAlien(new Vector2(x, y), screen.alien2Images, screen.acidImage, speed, screen.player));
                }else{
                    int x = 0 - AcidAlien.width;
                    int y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - AcidAlien.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x + AcidAlien.width*2, y, AcidAlien.width, AcidAlien.height)))
                                hits = true;
                        }
                    }
                    screen.entities.add(new AcidAlien(new Vector2(x, y), screen.alien2Images, screen.acidImage, speed, screen.player,false));
                }
            }
            //FastAlien Spawn
            if (System.currentTimeMillis() - lastCSpawn >= SpawnCTimer + extraTimer) {
                speed= Utils.doRandom(250,400);
                lastCSpawn = System.currentTimeMillis();
                if (stageId==4){
                    speed = speed/2;
                    where = Utils.doRandom(0,100);
                    if (where < 50){
                        spawnRight = true;
                    }else{
                        spawnRight = false;
                    }

                }
                if (spawnRight) {
                    int x = (int) screen.gameViewport.getWorldWidth();
                    int y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - FastAlien.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x - FastAlien.width, y, FastAlien.width, FastAlien.height)))
                                hits = true;
                        }
                    }
                    screen.entities.add(new FastAlien(new Vector2(x, y), 1, screen.alien3Images,speed, screen.player));
                }else{
                    int x = 0 - FastAlien.width;
                    int y =0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - FastAlien.height);
                        for (int h=0;h<collisions.size();h++){
                            if (Collision.checkWalls(collisions.get(h),new Rectangle(x-FastAlien.width*2,y,FastAlien.width,FastAlien.height))) hits = true;
                        }
                    }
                    screen.entities.add(new FastAlien(new Vector2(x, y), 1, screen.alien3Images, speed,screen.player,false));
                }

            }

            //Item Spawn
            if (System.currentTimeMillis() - lastCapsuleSpawn >= SpawnTimerCapsule ) {
                speed = 500;
                int x = 0;
                int y = 0;
                lastCapsuleSpawn = System.currentTimeMillis();
                if (stageId==4){
                    speed = speed/2;
                    where = Utils.doRandom(0,100);
                    if (where < 50){
                        spawnRight = true;
                    }else{
                        spawnRight = false;
                    }

                }
                if (spawnRight) {
                    x = (int) screen.gameViewport.getWorldWidth();
                    y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x -  BlueBulletItem.width, y, BlueBulletItem.width, BlueBulletItem.height)))
                                hits = true;
                        }
                    }
                }else{
                    x = 0 - BlueBulletItem.width;
                    y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x + BlueBulletItem.width *2, y, BlueBulletItem.width, BlueBulletItem.height)))
                                hits = true;
                        }
                    }
                }

                int capsule = screen.random.nextInt(2);

                if (capsule == 0) {
                    if (spawnRight) {
                        screen.items.add(new RedBulletItem(x, y, speed, screen.itemRedImages));
                    }else{
                        screen.items.add(new RedBulletItem(x, y, speed, screen.itemRedImages,false));
                    }
                } else if (capsule == 1) {
                    if (spawnRight) {
                        screen.items.add(new BlueBulletItem(x, y, speed, screen.itemBlueImages));
                    }else{
                        screen.items.add(new BlueBulletItem(x, y, speed, screen.itemBlueImages,false));
                    }
                }

            }
            if (System.currentTimeMillis() - lastHealthSpawn >= SpawnTimerHealth) {
                speed = 500;
                lastHealthSpawn = System.currentTimeMillis();
                if (stageId==4){
                    speed = speed/2;
                    where = Utils.doRandom(0,100);
                    if (where < 50){
                        spawnRight = true;
                    }else{
                        spawnRight = false;
                    }

                }
                if (spawnRight) {
                    int x = (int) screen.gameViewport.getWorldWidth();
                    int y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x - BlueBulletItem.width, y, BlueBulletItem.width, BlueBulletItem.height)))
                                hits = true;
                        }
                    }
                    screen.items.add(new GreenCapsuleItem(x, y, speed, screen.itemGreenImages));
                }else{
                    int x = 0 - BlueBulletItem.width;
                    int y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x + BlueBulletItem.width*2, y, BlueBulletItem.width, BlueBulletItem.height)))
                                hits = true;
                        }
                    }
                    screen.items.add(new GreenCapsuleItem(x, y, speed, screen.itemGreenImages,false));
                }
            }
            /*if (System.currentTimeMillis() - lastBombSpawn >= SpawnTimerBomb) {
                lastBombSpawn = System.currentTimeMillis();
                int x = (int) screen.gameViewport.getWorldWidth();
                int y =0;
                boolean hits = true;
                while (hits) {
                    hits = false;
                    y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - PurpleCapsuleItem.height);
                    for (int h=0;h<collisions.size();h++){
                        if (Collision.checkWalls(collisions.get(h),new Rectangle(x,y,PurpleCapsuleItem.width,PurpleCapsuleItem.height))) hits = true;
                    }
                }
                screen.items.add(new PurpleCapsuleItem(x, y, 500, screen.itemPurpleImages));
            }*/
        }else{
            if (stageId==3) {
                //SPACE STAGE
                ///SPAWN ENEMY SHIP:
                //TODO
                if (PlayScreen.entities.size() == 1) {
                    isCombatEnabled = false;
                    //no enemy spawned, so spawn:
                    if (spawnIterator >= killCount) {
                        //TODO END STAGE
                    } else {
                        System.out.println("SPAWNS SHIP");
                        spawnIterator++;
                        screen.entities.add(new EnemyShipOne(new Vector2(0, 0), 100, screen.enemyShipOne, true, screen.player));

                    }
                }
                //Items:
                //Item Spawn
                if (System.currentTimeMillis() - lastCapsuleSpawn >= SpawnTimerCapsule) {
                    lastCapsuleSpawn = System.currentTimeMillis();
                    int x = (int) screen.gameViewport.getWorldWidth();
                    int y = 0;
                    boolean hits = true;
                    while (hits) {
                        hits = false;
                        y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                        for (int h = 0; h < collisions.size(); h++) {
                            if (Collision.checkWalls(collisions.get(h), new Rectangle(x, y, BlueBulletItem.width, BlueBulletItem.height)))
                                hits = true;
                        }
                    }
                    screen.items.add(new BlackBulletItem(x, y, 1000, screen.itemBlackImages));


                }
            }else{
                if (stageId == 5){
                    isCombatEnabled = true;
                    float speed;
                    boolean spawnRight=true;
                    int where=0;
                    //TODO FINAL STAGE
                    if (!isBossSpawned){
                        //TODO
                        screen.entities.add(new Boss(new Vector2(PlayScreen.gameViewport.getWorldWidth(),PlayScreen.gameViewport.getWorldHeight()/2 - Boss.height/2),screen.player));
                        isBossSpawned=true;
                    }
                    //Item Spawn
                    if (System.currentTimeMillis() - lastCapsuleSpawn >= SpawnTimerCapsule ) {
                        speed = 500;
                        int x = 0;
                        int y = 0;
                        lastCapsuleSpawn = System.currentTimeMillis();
                        if (stageId==4){
                            speed = speed/2;
                            where = Utils.doRandom(0,100);
                            if (where < 50){
                                spawnRight = true;
                            }else{
                                spawnRight = false;
                            }

                        }
                        if (spawnRight) {
                            x = (int) screen.gameViewport.getWorldWidth();
                            y = 0;
                            boolean hits = true;
                            while (hits) {
                                hits = false;
                                y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                                for (int h = 0; h < collisions.size(); h++) {
                                    if (Collision.checkWalls(collisions.get(h), new Rectangle(x -  BlueBulletItem.width, y, BlueBulletItem.width, BlueBulletItem.height)))
                                        hits = true;
                                }
                            }
                        }else{
                            x = 0 - BlueBulletItem.width;
                            y = 0;
                            boolean hits = true;
                            while (hits) {
                                hits = false;
                                y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                                for (int h = 0; h < collisions.size(); h++) {
                                    if (Collision.checkWalls(collisions.get(h), new Rectangle(x + BlueBulletItem.width *2, y, BlueBulletItem.width, BlueBulletItem.height)))
                                        hits = true;
                                }
                            }
                        }

                        int capsule = screen.random.nextInt(2);

                        if (capsule == 0) {
                            if (spawnRight) {
                                screen.items.add(new RedBulletItem(x, y, speed, screen.itemRedImages));
                            }else{
                                screen.items.add(new RedBulletItem(x, y, speed, screen.itemRedImages,false));
                            }
                        } else if (capsule == 1) {
                            if (spawnRight) {
                                screen.items.add(new BlueBulletItem(x, y, speed, screen.itemBlueImages));
                            }else{
                                screen.items.add(new BlueBulletItem(x, y, speed, screen.itemBlueImages,false));
                            }
                        }

                    }
                    if (System.currentTimeMillis() - lastHealthSpawn >= SpawnTimerHealth) {
                        speed = 500;
                        lastHealthSpawn = System.currentTimeMillis();
                        if (stageId == 4) {
                            speed = speed / 2;
                            where = Utils.doRandom(0, 1);
                            if (where < 50) {
                                spawnRight = true;
                            } else {
                                spawnRight = false;
                            }

                        }
                        if (spawnRight) {
                            int x = (int) screen.gameViewport.getWorldWidth();
                            int y = 0;
                            boolean hits = true;
                            while (hits) {
                                hits = false;
                                y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                                for (int h = 0; h < collisions.size(); h++) {
                                    if (Collision.checkWalls(collisions.get(h), new Rectangle(x - BlueBulletItem.width, y, BlueBulletItem.width, BlueBulletItem.height)))
                                        hits = true;
                                }
                            }
                            screen.items.add(new GreenCapsuleItem(x, y, speed, screen.itemGreenImages));
                        } else {
                            int x = 0 - BlueBulletItem.width;
                            int y = 0;
                            boolean hits = true;
                            while (hits) {
                                hits = false;
                                y = screen.random.nextInt((int) screen.gameViewport.getWorldHeight() - BlueBulletItem.height);
                                for (int h = 0; h < collisions.size(); h++) {
                                    if (Collision.checkWalls(collisions.get(h), new Rectangle(x + BlueBulletItem.width * 2, y, BlueBulletItem.width, BlueBulletItem.height)))
                                        hits = true;
                                }
                            }
                            screen.items.add(new GreenCapsuleItem(x, y, speed, screen.itemGreenImages, false));
                        }
                    }
                    //TODO
                }
            }
        }

        //run .update(delta) on all objects
        for (int i=0; i<screen.entities.size();i++){
            screen.entities.get(i).update(delta);
        }

        for (int i=0; i<screen.items.size();i++){
            screen.items.get(i).update(delta);
        }

        //labels set:
        screen.labelHealth.setText("Health: " + screen.player.health + "/" + screen.player.maxHealth);
        if (scoreBased) {
            screen.labelInfo.setText("Score: " + screen.player.score + "/" + scoreGoal);
        }else{
            if (killCount>0){
                screen.labelInfo.setText("Score: " + screen.player.score + "/" + killCount);
            }else{
                if (boss){
                    screen.labelInfo.setText("Boss: "+screen.player.score + "/" + Boss.bossHealth);
                }else{
                    screen.labelInfo.setText("");
                }

            }
        }
        if (this.stageId == 999) {
            screen.labelInfo.setText("Score: " + screen.player.score);
        }


    }

    public void render(float delta){

    }

    public void dispose(){

    }

    public Vector2 getPlayerPosition() {
        Vector2 position;
        switch (stageId){
            case 1:
                position = new Vector2(0,MyGame.WORLD_HEIGHT/2 - Player.height/2 );
                break;
            case 2:
                position = new Vector2(57,MyGame.WORLD_HEIGHT/2 - Player.height/2 );
                break;
            case 3:
                position = new Vector2(0,MyGame.WORLD_HEIGHT/2 - Player.height/2 );
                break;
            case 4:
                position = new Vector2(MyGame.WORLD_WIDTH/2 - Player.width/2,MyGame.WORLD_HEIGHT/2 - Player.height/2);
                break;
            case 5:
                position = new Vector2(0,MyGame.WORLD_HEIGHT/2 - Player.height/2 );
                break;
            case 999:
                position = new Vector2(0,MyGame.WORLD_HEIGHT/2 - Player.height/2 );
                break;
            default:
                position = new Vector2(0,MyGame.WORLD_HEIGHT/2 - Player.height/2 );
                break;
        }
        return position;
    }

    public float getPlayerSpeed(){
        float speed;
        switch(stageId){
            case 3:
                speed = 300;
                break;
            default:
                speed = 200;
                break;
        }
        return speed;
    }

    public boolean isPlayerShip(){
        boolean flag=false;
        switch(stageId){
            case 3:
                flag=true;
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    private void setBackground(int stageId) {
        Texture texture;
        switch (stageId){
            case 1:
                texture = MyGame.manager.get("img/back2.jpg", Texture.class);
                break;
            case 2:
                texture = MyGame.manager.get("img/base-background.jpg", Texture.class);
                break;
            case 3:
                texture = MyGame.manager.get("img/back-stars.jpg", Texture.class);
                break;
            case 4:
                texture = MyGame.manager.get("img/back-mountain.jpg", Texture.class);
                break;
            case 5:
                texture = MyGame.manager.get("img/back3.jpg", Texture.class);
                break;
            case 999:
                texture = MyGame.manager.get("img/back2.jpg", Texture.class);
                break;
            default:
                //avoid null Texture object:
                texture = MyGame.manager.get("img/back2.jpg", Texture.class);
                break;
        }

        PlayScreen.map = new Map(texture,getMapSpeed());

    }

    public float getMapSpeed(){
        float speed = 0;
        switch (stageId){
            case 2:
                speed = 0;
                break;
            case 3:
                speed = 300;
                break;
            case 4:
                speed = 0;
                break;
            default:
                speed = 100;
                break;
        }
        return speed;
    }
}
