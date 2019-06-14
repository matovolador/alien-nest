package com.rufodev.aliennestoblivion.entities;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

public class RedBulletItem extends Item {
	Sound sound;
	public RedBulletItem(float x , float y,float speed,ArrayList<Sprite> images) {
		super( x ,  y,speed, images);
		this.width = 60;
		this.height = 25;
	}

	public RedBulletItem(float x , float y,float speed,ArrayList<Sprite> images, boolean spawnsRight) {
		super( x ,  y,speed, images, spawnsRight);
		this.width = 60;
		this.height = 25;
	}
	
	
	public void update (float delta) {
		updateCenter();
		//CHECK WALL COLLISIONS-----------------------------
		float tempX = this.position.x;
		boolean hitsX = false;
		//check X:
		if(spawnsRight) {
			this.position.x -= this.speed * delta;
		}else{
			this.position.x += this.speed * delta;
		}
		for (int i = 0; i< StageHandler.collisions.size(); i++){
			if (Collision.checkWalls(StageHandler.collisions.get(i),this)) hitsX = true;
		}
		if (hitsX) this.position.x = tempX;
		////------------------------------------------------

		if (spawnsRight){
			if (position.x< 0 - width || hitsX){
				this.recycle();
			}
		}else{
			if (position.x > PlayScreen.gameViewport.getWorldWidth() || hitsX){
				this.recycle();
			}
		}


		for (int i = PlayScreen.entities.size()-1; i>=0; i-- ){
			if ((PlayScreen.entities.get(i) instanceof Player)){
				if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, PlayScreen.entities.get(i))){
					((Player) PlayScreen.entities.get(i)).bulletId=2;
					((Player) PlayScreen.entities.get(i)).newBulletTimeLeft=5000;
					((Player) PlayScreen.entities.get(i)).newBulletTimeInit=System.currentTimeMillis();
					SoundManager.capsuleSound();
					this.recycle();
				}
			}
		}
		
	}
	
	public void render(){
		super.animator.animate(this.position.x,this.position.y);
	}

}
