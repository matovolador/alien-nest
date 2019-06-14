package com.rufodev.aliennestoblivion.entities;



import java.util.*;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.data.ConfigObject;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.input.MyInput;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

public class Player extends Entity {
	
	public static final int  width=60;
	public static final int height=50;
	public boolean spawnBomb;
	public boolean moveLeft=false;
	public boolean moveRight=false;
	public boolean moveUp=false;
	public boolean moveDown=false;
	public long lastBSpawn = 0;
	public long bSpawnTimer = 100;
	boolean isHurt = false;
	public long hurtTimer = 2000;
	public long hurtTime = 0;
	long newBulletTimeLeft = 0;
	long newBulletTimeInit = 0;
	public int bulletId=0;
	public int id=0;
	public boolean isMoving=false;
	public boolean isRotating=false;
	public Sprite joystickInner;
	public Sprite joystickOutter;
    public Sprite joystick2Inner;
    public Sprite joystick2Outter;
    //Exernal controler class
	MyInput myInput;
	public Animator standAnimation;
	public Vector2 bulletSpawn;
	public int score;
    public int bombSpawnCharge;
	public int bombCurrentCharge;
    public boolean bombEnabled;
	public boolean canMove;
	public ArrayList<Bullet> bulletArray;
	public ArrayList<OrbShield> orbArray;
	public Animator shipAnimator;
	public boolean isShip;

//TODO add another weapon built with score amount and triggered with a new UI button.
	
	public Player( Vector2 position,ArrayList<Sprite> walkAnimation, ArrayList<Sprite> standAnimation, Sprite joystickInner, Sprite joystickOutter,Sprite joystick2Inner, Sprite joystick2Outter, Sprite shipSprite) {
		super(position,0,width,height,false);
		super.maxHealth = 10 + ConfigObject.armorIncrement;
		super.health = super.maxHealth;
		super.setSpeed(150);
		super.animator=new Animator(walkAnimation, 300);
		this.standAnimation = new Animator(standAnimation,100);
		ArrayList shipSprites = new ArrayList<Sprite>();
		shipSprites.add(shipSprite);
		this.shipAnimator = new Animator(shipSprites,0);
		this.bulletArray=new ArrayList<Bullet>();
        this.orbArray = new ArrayList<OrbShield>();
        this.joystickInner = joystickInner;
        this.joystickOutter = joystickOutter;
        this.joystick2Inner = joystick2Inner;
        this.joystick2Outter = joystick2Outter;
		myInput = new MyInput();
        this.rotation = 0;
		this.score=0;
		this.bombSpawnCharge = 100;
		this.bombCurrentCharge = 0;
        this.bombEnabled = false;
		this.spawnBomb = false;
		this.bulletSpawn = new Vector2(0,0);
		this.canMove = true;
		this.isShip = false;
        this.updateCenter();
        this.updateBulletSpawn();
	}

	public void updateBulletSpawn() {
        this.bulletSpawn.x = (float)(this.center.x + (this.position.x + this.width-this.center.x) * Math.cos(rotation) - (this.position.y + this.height/2-this.center.y) * Math.sin(rotation));
        this.bulletSpawn.y = (float)(this.center.y + (this.position.x + this.width -this.center.x) * Math.sin(rotation) + (this.position.y + this.height/2-this.center.y) * Math.cos(rotation));
        return;
	}


	public void setAsShip(boolean flag){
		this.isShip = flag;
		return;
	}
	public void rotate(float angle){
		rotation = angle; // keep Radians
		angle = MathUtils.radiansToDegrees * angle;
		if (angle < 0) angle += 360;
		super.animator.degrees=angle; //Pass Degrees
		this.standAnimation.degrees=angle;
		this.shipAnimator.degrees = angle;
	}
	
	
	public void input (float delta){
		
		//EXTERNAL CLASS CONTROL METHOD ---------------------
		MyInput.handlePlayerInput(this);
		
		//--------------------------
		
		
	}
	
	public void update (float delta) {
		this.updateCenter();
        this.updateBulletSpawn();
		if ( StageHandler.isCombatEnabled) this.shoot();
		//POSITION UPDATE
		if (this.position.x<0) this.position.x=0;
		if (this.position.x> PlayScreen.gameViewport.getWorldWidth() - width) this.position.x= PlayScreen.gameViewport.getWorldWidth() - width;
		if (this.position.y<0 ) this.position.y = 0;
		if (this.position.y> PlayScreen.gameViewport.getWorldHeight() - height) this.position.y = PlayScreen.gameViewport.getWorldHeight() - height;
		///
        if (this.bombSpawnCharge<=this.bombCurrentCharge){
			this.bombEnabled = true;
		}else{
			this.bombEnabled = false;
		}


		for (int i=0;i<bulletArray.size();i++){
			bulletArray.get(i).update(delta);
			
		}
		if (this.isHurt){
			if (System.currentTimeMillis() - this.hurtTime >= this.hurtTimer + delta   ){
				this.hurtTime= 0; 
				this.isHurt=false;
				super.animator.setAlpha();
				this.standAnimation.setAlpha();
				this.shipAnimator.setAlpha();
			}
		}else{
			this.hurtTime= 0; 
			this.isHurt=false;
			super.animator.setAlpha();
			this.shipAnimator.setAlpha();
			this.standAnimation.setAlpha();
		}
		if (this.bulletId!=0){
			if (System.currentTimeMillis() - this.newBulletTimeInit  >=   this.newBulletTimeLeft + delta + ConfigObject.chargeIncrement){
			    this.bulletId=0; 
				this.newBulletTimeInit=0;
				this.newBulletTimeLeft=0;
			}
		
		}
	}

	private void shoot(){
		////AUTO SHOOT
		if (System.currentTimeMillis() - this.lastBSpawn >= this.bSpawnTimer && !this.isShip){
			this.lastBSpawn= System.currentTimeMillis();

			//sensitivityple Bullets---

			//Normal
			if (this.bulletId==0)
				this.bulletArray.add(new Bullet(0,0,1, PlayScreen.bulletImages,200,this,10,10));

			//Blue Bullet
			if (this.bulletId==1)
				this.bulletArray.add(new Bullet(1,0,3, PlayScreen.bulletBlueImages,200,this,30,30));

			//Triple bullet
			if (this.bulletId==2){
				this.bulletArray.add(new Bullet(2,1,2, PlayScreen.bulletImages,200,this,10,10));
				this.bulletArray.add(new Bullet(2,2,2, PlayScreen.bulletImages,200,this,10,10));
				this.bulletArray.add(new Bullet(2,3,2, PlayScreen.bulletImages,200,this,10,10));
			}

		}else{
			if (System.currentTimeMillis() - this.lastBSpawn >= 300 && this.isShip){
				this.lastBSpawn= System.currentTimeMillis();
				//Normal
				if (this.bulletId==0)
					this.bulletArray.add(new Bullet(0,0,1, PlayScreen.spaceBullets,200,this,10,10));

				//Blue Bullet
				if (this.bulletId==1)
					this.bulletArray.add(new Bullet(1,0,3, PlayScreen.bulletBlueImages,200,this,30,30));

				//Triple bullet
				if (this.bulletId==2){
					this.bulletArray.add(new Bullet(2,1,2, PlayScreen.bulletRedImages,200,this,20,10));
					this.bulletArray.add(new Bullet(2,2,2, PlayScreen.bulletRedImages,200,this,20,10));
					this.bulletArray.add(new Bullet(2,3,2, PlayScreen.bulletRedImages,200,this,20,10));

				}
				//Space missile
				if (this.bulletId==3)
					this.bulletArray.add(new Bullet(3,0,1, PlayScreen.spaceMissile,200,this,25,25));
			}
		}
	}
	
	
	
	public void render(){
		if (!this.isShip) {
			if ((moveUp == false && moveDown == false && moveLeft == false && moveDown == false && moveRight == false)) {
				this.standAnimation.animate(position.x, position.y);
			} else {
				super.animator.animate(position.x, position.y);
			}
		}else{
			this.shipAnimator.animate(position.x,position.y);
		}
		for (int i=0;i<bulletArray.size();i++){
			bulletArray.get(i).render();
		}
		
	}
	public void heal(int amount){
		this.health+=amount;
		if (this.health > this.maxHealth) this.health=this.maxHealth;
		return;
	}
	public void hurt(int amount){
        if (this.isHurt){
			return;
		}
		if (this.isShip){
			SoundManager.playSound(SoundManager.shipDamage);
		}else {
			SoundManager.playerHurt();
		}
		
		this.health-=amount;
		if (this.health<0) {
			this.health=0;

		}
		this.isHurt=true;
		this.hurtTime=System.currentTimeMillis();
		super.animator.setAlpha(0.5f);
		this.shipAnimator.setAlpha(0.5f);
		this.standAnimation.setAlpha(0.5f);

        //TEMP FOR TESTING OF GAMEPLAY
        //this.health=this.maxHealth;
	}

	public void updateScore(int score) {
		this.score+=score;

	}


}
