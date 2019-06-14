package com.rufodev.aliennestoblivion.fx;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

public class Animator {
	public long lastAnimationTime = 0;
	public long lastRotationTime = 0;
	public long animationSpeed;
	public long rotationTimer = 0;
	public boolean rotateClockwise = true;
	public int count=0;
	public boolean init=false;
	float alpha = 1;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	ArrayList<Texture> textures = new ArrayList<Texture>();
	public boolean flipH = false ;
	public boolean flipV = false ;
	public float degrees = 0;  //NOT radians
	public double rotationAmount = 5;
	
	public Animator(ArrayList<Sprite> images, long speed) {
		animationSpeed=speed;
		for (int i=0;i<images.size();i++){
			sprites.add(i,new Sprite (images.get(i)));
			sprites.get(i).setOriginCenter();
		}
	}

	public Animator(ArrayList<Texture> imgs, long speed, boolean textureIsSource) {
		animationSpeed=speed;
		for (int i=0;i<imgs.size();i++){
			sprites.add(i,new Sprite (imgs.get(i)));
		}
	}


	public void animate(float x, float y){
		if ((System.currentTimeMillis() - lastRotationTime  >= rotationTimer )  && (rotationTimer!=0)){
			lastRotationTime=System.currentTimeMillis();
			if (rotateClockwise){
				this.degrees = (float)(this.degrees - this.rotationAmount);
			}else{
				this.degrees = (float)(this.degrees  + this.rotationAmount);
			}
		}
		if (init==false || this.sprites.size() == 1 || this.textures.size() == 1){
			//sprites.get(0).flip(flipH, flipV);
			sprites.get(0).setRotation(degrees);
			sprites.get(0).setPosition(x, y);
			sprites.get(0).draw(MyGame.batch,alpha);
			init=true;
			lastAnimationTime=System.currentTimeMillis();
			lastRotationTime=System.currentTimeMillis();
			return;
		}

		if (System.currentTimeMillis() - lastAnimationTime >= animationSpeed){
			lastAnimationTime=System.currentTimeMillis();
			if (PlayScreen.gameState != 0) count++;  //If game is paused, sprite iterator wont change
			if (count>=sprites.size()){
				count=0;
			}
		}
		sprites.get(count).setPosition(x, y);
		sprites.get(count).setRotation(degrees);
		sprites.get(count).draw(MyGame.batch,alpha);
		return;
		
	}

	public void setAlpha(float toWhat){
		alpha = toWhat;
	}
	public void setAlpha(){
		alpha = 1;
	}

	public void flipH(){
		for (int i=0; i<this.sprites.size();i++){
			sprites.get(i).flip(true, false);
		}
	}
	
	

}
