package com.rufodev.aliennestoblivion.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Map {
	
	public Vector2 offset; 
	public ArrayList<ArrayList<Texture>> textures;
	public float speed;
	public boolean move;
	
	
	public Map(Vector2 offset, ArrayList<ArrayList<Texture>> textures, float speed, boolean move) {
		this.offset = offset;
		this.textures = textures;
		this.speed = speed;
		this.move = move;
	}
	
	
	public void update (long delta){
		
		//CHECK IF STOPS MOVING TODO
		
		if (this.move) {
			this.offset.x -= this.speed + delta;
		}
		
	}
	
	public void render (SpriteBatch batch){
		//TODO
	}

}
