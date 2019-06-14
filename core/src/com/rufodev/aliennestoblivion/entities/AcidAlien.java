package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.fx.VisualEffect;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;
import java.util.Random;


public class AcidAlien extends Entity{
	public static Random random = new Random();
	public static final int height= 80;
	public static final int width=80;
	public Player player;
	float minSpeed;
	boolean isPositioned = false;
	long acidSpawnTimer = 5000;
	long lastAcidSpawn = 0;
	ArrayList<Sprite> acidImages;


	public AcidAlien(Vector2 position, ArrayList<Sprite> alienImages, Sprite acidImage, float speed, Player player) {
		super(position, 50, width, height,true);
		super.setSpeed(speed);
		super.animator=new Animator(alienImages, 100);
		this.minSpeed = speed;
		acidImages = new ArrayList<Sprite>();
		this.acidImages.add(acidImage);
		super.acidArray= new ArrayList<Acid>();
		acidSpawnTimer = 2000;
		lastAcidSpawn = 0;
		this.power = 3;
		this.player = player;
	}

	public AcidAlien(Vector2 position, ArrayList<Sprite> alienImages, Sprite acidImage, float speed, Player player, boolean spawnsRight) {
		super(position, 50, width, height,true, spawnsRight);
		super.setSpeed(speed);
		super.animator=new Animator(alienImages, 100);
		if (!spawnsRight) animator.flipH();
		this.minSpeed = speed;
		acidImages = new ArrayList<Sprite>();
		this.acidImages.add(acidImage);
		super.acidArray= new ArrayList<Acid>();
		acidSpawnTimer = 2000;
		lastAcidSpawn = 0;
		this.power = 3;
		this.player = player;
	}


	public void input(float delta) {

		
	}


	public void update(float delta) {
		super.updateCenter();
		if (player == null){

			for (Entity entity : PlayScreen.entities){
				if (entity instanceof Player){
					player = (Player)entity;
				}
			}
		}else {
			this.positionSelf(delta);


			//is dead?
			ArrayList<Entity> copy;
			if (this.health <= 0) {
				copy = new ArrayList<Entity>(PlayScreen.entities);
				copy.remove(this);
				PlayScreen.entities = copy;

			}
			/*if (position.x < 0 - width && spawnsRight){
				copy = new ArrayList<Entity>(PlayScreen.entities);
				copy.remove(this);
				PlayScreen.entities = copy;

			}
			if (!spawnsRight && position.x > PlayScreen.gameViewport.getWorldWidth()){
				copy = new ArrayList<Entity>(PlayScreen.entities);
				copy.remove(this);
				PlayScreen.entities = copy;
			}
			*/
			//collision with player?
			if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, player)){
				if (player.isHurt == false){
					player.hurt(this.power);
					copy=new ArrayList<Entity>(PlayScreen.entities);
					copy.remove(this);
					PlayScreen.entities=copy;
				}

			}
			///fire acid:
			if (this.isPositioned) {
				//this works!
				this.fireAcid(delta);
				for (int i = 0; i < this.acidArray.size(); i++) {
					this.acidArray.get(i).update(delta);
				}
			}

		}
	}

	public void render(){
		for (int i=0;i<acidArray.size();i++)
			acidArray.get(i).render();
		super.animator.animate(this.position.x,this.position.y);
	}


	public void hurt(int amount) {
        if (player == null) return;
		this.health-=amount;
		if (this.health<=0){
			SoundManager.alienDied();
			player.updateScore(10);
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

	public void positionSelf(float delta){
		if (this.isPositioned) return;
		if (spawnsRight){
			if (this.position.x > PlayScreen.gameViewport.getWorldWidth() - this.width * 2){
				this.position.x -= this.speed * delta;
				this.isPositioned = false;
			}
			if (this.position.x <= PlayScreen.gameViewport.getWorldWidth() - this.width * 2) {
				this.position.x = PlayScreen.gameViewport.getWorldWidth() - this.width * 2;
				this.isPositioned = true;
			}
		}else{
			if (this.position.x < this.width * 2){
				this.position.x += this.speed * delta;
				this.isPositioned = false;
			}
			if (this.position.x >= this.width * 2) {
				this.position.x = this.width * 2;
				this.isPositioned = true;
			}
		}
		return;
	}

	public void fireAcid(float delta){
		if (System.currentTimeMillis() - this.lastAcidSpawn >= this.acidSpawnTimer + delta) {
			this.lastAcidSpawn = System.currentTimeMillis();
			SoundManager.playSound(SoundManager.alienSpit);
			super.acidArray.add(new Acid(0,this.acidImages,this));
			super.acidArray.add(new Acid(1,this.acidImages,this));
			super.acidArray.add(new Acid(2,this.acidImages,this));
			super.acidArray.add(new Acid(3,this.acidImages,this));
			super.acidArray.add(new Acid(4,this.acidImages,this));

		}
		return;
	}

}
