package com.rufodev.aliennestoblivion.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

public abstract class Item {
	public Vector2 position;
	public Vector2 center;
	public float speed;
	public static int width = 55;
	public static int height = 25;
	public Animator animator;
	public boolean spawnsRight;
	public Item(float x , float y,float speed, ArrayList<Sprite> images) {
		this.position= new Vector2(x,y);
		this.center= new Vector2(x+width/2,y+height/2);
		this.speed=speed;
		animator= new Animator(images,300);
		spawnsRight = true;
	}

	public Item(float x, float y, float speed, ArrayList<Sprite> images, boolean spawnRight) {
		this.position= new Vector2(x,y);
		this.speed=speed;
		animator= new Animator(images,300);
		spawnsRight = spawnsRight;
	}

	public abstract void update (float delta);

	public void updateCenter(){
		this.center= new Vector2(this.position.x+width/2,this.position.y+height/2);
	}
	public abstract void render();
	
	public void recycle(){
		ArrayList<Item> copy = PlayScreen.items;
		copy.remove(this);
		PlayScreen.items= copy;
		PlayScreen.items.trimToSize();
	}

}
