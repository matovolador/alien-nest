package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.data.Utils;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.fx.VisualEffect;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;
import java.util.Random;


public class BossMinion extends Entity{
	public static Random random = new Random();
	public static final int width=60;
	public static final int height= 45;
	Player player;


	public BossMinion(Vector2 position, int health, ArrayList<Sprite> alienImages, float speed, Player player) {
		super( position,1,width,height,true);
		super.setSpeed(Utils.doRandom(250,400));
		super.animator=new Animator(alienImages, 100);
		this.power = 1;
		this.player = player;


	}

	public BossMinion(Vector2 position, int health, ArrayList<Sprite> alienImages, float speed, Player player, boolean spawnsRight) {
		super( position,1,width,height,true, spawnsRight);
		super.setSpeed(Utils.doRandom(250,400));
		super.animator=new Animator(alienImages, 100);
		this.power = 1;
		this.player = player;
		if (!spawnsRight) animator.flipH();

	}


	public void input(float delta) {

		
	}


	public void update(float delta) {
		super.updateCenter();
		if (player == null){
                        
			for (Entity entity : PlayScreen.entities){
				if (entity instanceof Player){
					player = (Player) entity;
				}
			}
		}else{
			float zx = width/2 + this.position.x;
			float zy= height/2 + this.position.y;
			
			float px = Player.width/2 + player.position.x;
			float py=  Player.height/2 + player.position.y;
			float scaleX;
			float scaleY;
			float angle;
			if (zx>=px){
				angle = (float)Math.atan2(py - zy , px - zx);
				
				
				scaleY = (float)Math.sin(angle);
			}else{
				angle = (float)Math.atan2(py - zy , px - zx);
				
				scaleY = (float)Math.sin(angle);
			}
			if (spawnsRight){
				scaleX=-1;
			}else{
				scaleX = 1;
			}

			//CHECK WALL COLLISIONS-----------------------------
			float tempX = this.position.x;
			float tempY = this.position.y;
			boolean hitsX = false;
			boolean hitsY = false;
			//check X:
			this.position.x += scaleX*speed*delta;
			for (int i=0;i< StageHandler.collisions.size();i++){
				if (Collision.checkWalls(StageHandler.collisions.get(i),this)) hitsX = true;
			}
			if (hitsX) this.position.x = tempX;
			//check Y:
			this.position.y += scaleY*speed*delta*(0.2);
			for (int i=0;i< StageHandler.collisions.size();i++){
				if (Collision.checkWalls(StageHandler.collisions.get(i),this)) hitsY = true;
			}
			if (hitsY) this.position.y = tempY;
			////------------------------------------------------

			ArrayList<Entity> copy;
			if (spawnsRight){
				if (position.x< 0 - width || hitsX){
					copy=new ArrayList<Entity>(PlayScreen.entities);
					copy.remove(this);
					PlayScreen.entities=copy;
				}
			}else{
				if (position.x > PlayScreen.gameViewport.getWorldWidth() || hitsX){
					copy = new ArrayList<Entity>(PlayScreen.entities);
					copy.remove(this);
					PlayScreen.entities = copy;
				}
			}

			
			if (Collision.checkCollision(this, player)){
				if (player.isHurt == false){
					player.hurt(this.power);
					copy=new ArrayList<Entity>(PlayScreen.entities);
					copy.remove(this);
					PlayScreen.entities=copy;
				}

			}
			
		}
               /* if (Utils.doRandom(0, 100)>40){
                    this.speed = this.speed + Utils.doRandom(50,100)/10;
                }
                */
	}

	public void render(){
		super.animator.animate(this.position.x,this.position.y);
	}


	public void hurt(int amount) {
        if (player == null) return;
		this.health-=amount;
		if (this.health<=0){
			SoundManager.alienDied();
			//player.updateScore(1);
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
