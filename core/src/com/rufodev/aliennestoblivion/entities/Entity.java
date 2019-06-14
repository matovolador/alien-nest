package com.rufodev.aliennestoblivion.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.fx.Animator;

public abstract class Entity {
	public Vector2 position;
	public float speed = 0.2f;
	public int health;
	public int maxHealth;
	public float  width;
	public float height;
	int power=1;
	public boolean spawnsRight;

	public ArrayList<Acid> acidArray;
	public String name;
	public Vector2 center;
	public ArrayList<Vector2> originSpawns;  //Used to spawn bullets and shit from
	Animator animator;
	public double rotation; //radians
	public boolean isEnemy;

	
	public Entity( Vector2 position , int health, int width, int height, boolean isEnemy) {
		this.position=position;
		this.spawnsRight = true;
		this.health=health;
		this.maxHealth=health;
		this.width=width;
		this.height=height;
		this.rotation =0;
		this.center = new Vector2(0,0);
		this.isEnemy = isEnemy;
		originSpawns = new ArrayList<Vector2>();


	}

	public Entity(Vector2 position, int health, int width, int height, boolean isEnemy, boolean spawnsRight) {
		this.position=position;
		this.spawnsRight = spawnsRight;
		this.health=health;
		this.maxHealth=health;
		this.width=width;
		this.height=height;
		this.rotation =0;
		this.center = new Vector2(0,0);
		this.isEnemy = isEnemy;
		originSpawns = new ArrayList<Vector2>();

	}

	public void rotate(float angle){
		rotation = angle; // keep Radians
		angle = MathUtils.radiansToDegrees * angle;
		if (angle < 0) angle += 360;
		this.animator.degrees=angle; //Pass Degrees


	}

	public void updateCenter(){


		this.center.x = this.position.x + this.width/2;
		this.center.y = this.position.y + this.height/2;
	}
	
	public void setSpeed (float speed) {
		this.speed = speed;
	}
	
	public abstract void input(float delta);
	public abstract void update(float delta);
	public abstract void render();
	public abstract void hurt(int amount);

}
