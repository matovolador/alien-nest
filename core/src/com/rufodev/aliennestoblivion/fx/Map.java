package com.rufodev.aliennestoblivion.fx;



import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

public class Map {
	public boolean mode;  //false = bacjkground , true = tile
	public static Random random = new Random();
	public Texture image;
	public Texture image2;
	Vector2 position; //position of the middle image out of the 9 cuadrants: I
	//I-I2
	float totalWidth;
	float totalHeight;
	public long lastAnimationTime = 0;
	public float speed;
	public boolean init=false;
	boolean alpha = false;
	float width;
	float height;
	public float xDone;
	int layoutHeight;
	int layoutWidth;
	//Only for ScrollTop Functionality
	Sprite tile;
	float speedy;  //map movement speed;
	float moveFrom = 0; //moves map when character reaches "moveFrom" distance in its movement direction
	Vector2 positionTotalMap;  //for whole map movement on ScrollTop functionality
	public Map(Texture backImage, float speed) {
		image=backImage;
		this.speed=speed;
		image2 = image;
		totalWidth = image.getWidth() * 2;
		totalHeight = image.getHeight() * 1;
		width = image.getWidth();
		height = image.getHeight();
		mode= false;
	}


	public void performSideScrolling(float delta){

			if (init==false){
				position = new Vector2(0,0);
			}
			if (PlayScreen.gameState != 0){
				//create effect here
				//rand 0 to 8  ... limit movement to width and height of the total of 9 parcels
				if (speed!=0) position.x -= speed * delta;
			}
			if (!alpha){
				drawEm();
			}else{
				Color color = new Color(MyGame.batch.getColor());
				float oldAlpha = color.a; //save its alpha
				color.a = oldAlpha / 2; //ex. scale = 0.5 will make alpha halved
				MyGame.batch.setColor(color); //set it
				drawEm(); 
				//Set it back to orginial alpha when you're done with your alpha manipulation
				color.a = oldAlpha;
				MyGame.batch.setColor(color);
			}
			
			init=true;
			lastAnimationTime=System.currentTimeMillis();
			fixPositions();
			return;
	}


	private void fixPositions() {
		float imgWidth = PlayScreen.backgroundViewport.getWorldWidth();
		if ( position.x * (-1) > imgWidth  ) position.x = 0;

	}

	public void drawEm( ) {
		float imgWidth = PlayScreen.backgroundViewport.getWorldWidth();
		float imgHeight = PlayScreen.backgroundViewport.getWorldHeight();
		MyGame.batch.draw(image,position.x, position.y,imgWidth,imgHeight);
		MyGame.batch.draw(image2,position.x + imgWidth , position.y,imgWidth,imgHeight);
		
	}

	public void setAlpha(boolean toWhat){
		alpha = toWhat;
	}
	

	
}