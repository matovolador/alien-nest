package com.rufodev.aliennestoblivion.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.ConfigObject;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity{
	public float speed;
	public boolean initialized=false;
	public Player holder;
    double scale_X;
    double scale_Y;
    int number=0; // if not 0, its triple bullet
    ArrayList<Bullet> copy;
	int id;
	
    public Bullet(int id,int number,int power,ArrayList<Sprite> images,float speed, Player holder,int width, int height) {
		super(new Vector2(holder.bulletSpawn.x - width/2  ,holder.bulletSpawn.y - height/2 ),1,width,height, false);
		this.id = id;
		this.holder=holder;
		this.speed=speed * 20;
		if (holder.isShip) this.speed = 3500;
		if (holder.isShip && this.id == 3) this.speed = 1500;
		super.animator=new Animator(images,0);
		if (id == 3 || id == 1) {
			super.animator.rotationTimer = 1;
			super.animator.rotationAmount = 30;
		}
        this.number=number;
        super.power=power + ConfigObject.powerIncrement;
	}
	private void fireBullet( float delta){  //direction :  0= to the right, 1 = to the left
		
		if (!initialized) {
            /*with some weird ass offset that doesnt seem to work nor i understand, so fuck this ill comment this method.
           position.x = holder.bulletSpawn.x - (float)(this.width/2 * holder.rotation);
            position.y = holder.bulletSpawn.y - (float)(this.height/2 * holder.rotation);
            */
            //without the weird ass offset:
            position.x = holder.bulletSpawn.x - this.width/2;
            position.y = holder.bulletSpawn.y - this.height/2;
			double angle = holder.rotation;
			if (this.number != 0) {
				if (this.number == 1) {
					this.scale_X = Math.cos(angle);
					this.scale_Y = Math.sin(angle);

				}
				if (this.number == 2) {
					angle += 0.4;
					this.scale_X = Math.cos(angle);
					this.scale_Y = Math.sin(angle);

				}

				if (this.number == 3) {
					angle -= 0.4;
					this.scale_X = Math.cos(angle);
					this.scale_Y = Math.sin(angle);

					SoundManager.bulletFired();

				}
			} else {
				this.scale_X = Math.cos(angle);
				this.scale_Y = Math.sin(angle);
				SoundManager.bulletFired();
			}
			initialized = true;
			return;
		}
	}
	
	public void input(float delta) {
		
	}


	public void update(float delta){

        holder.updateBulletSpawn();
		updateCenter();
		this.fireBullet(delta);


		this.position.x += speed * delta * scale_X;
		this.position.y += speed * delta * scale_Y;

		for (int i =0;i< PlayScreen.entities.size(); i++ ) {
			if (!(PlayScreen.entities.get(i) instanceof Player) && (PlayScreen.entities.get(i).isEnemy)) {
				if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, PlayScreen.entities.get(i))) {
					PlayScreen.entities.get(i).hurt(this.power);
					this.health -= 1;
					if (this.health <= 0) {
						copy = new ArrayList<Bullet>(holder.bulletArray);
						copy.remove(this);
						holder.bulletArray = copy;
					}
				}
			}
		}
		//Check if hits Boss Landmines:
		for (int i =0;i< PlayScreen.entities.size(); i++ ) {
			if (PlayScreen.entities.get(i) instanceof Boss){
				((Boss) PlayScreen.entities.get(i)).landmines.trimToSize();
				for (int h=0;h<((Boss) PlayScreen.entities.get(i)).landmines.size();h++) {
					if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, ((Boss) PlayScreen.entities.get(i)).landmines.get(h))&&(((Boss) PlayScreen.entities.get(i)).landmines.get(h).isPositioned)) {
						((Boss) PlayScreen.entities.get(i)).landmines.get(h).hurt(this.power);
						h-=1;
						this.health -= 1;
						if (this.health <= 0) {
							copy = new ArrayList<Bullet>(holder.bulletArray);
							copy.remove(this);
							holder.bulletArray = copy;
						}
					}
				}
			}
		}
		//IS OUT OF BOUNDS?
		if ((this.position.x  > Gdx.graphics.getWidth()||(this.position.x<0)||(this.position.y<0)||(this.position.y>Gdx.graphics.getHeight()))){

			copy=new ArrayList<Bullet>(holder.bulletArray);
			copy.remove(this);
			holder.bulletArray=copy;
		}

		//Wall collision:
		boolean hitsWall = false;
		for (int i = 0; i< StageHandler.collisions.size(); i++){
			if (Collision.checkWalls(StageHandler.collisions.get(i),this)) hitsWall = true;
		}
		if (hitsWall){
			copy=new ArrayList<Bullet>(holder.bulletArray);
			copy.remove(this);
			holder.bulletArray=copy;
		}
	}
	
	public void render(){
		double angle = holder.rotation;
		if (this.number==2) angle +=0.4;
		if (this.number==3) angle -=0.4;
		angle= (float)(MathUtils.radiansToDegrees * angle);
		if (angle < 0) angle += 360;
		if (! (angle ==0) && ! (angle == 360)) super.animator.degrees =(float) angle;
		super.animator.animate(this.position.x,this.position.y);
	}


	@Override
	public void hurt(int amount) {
		
	}
}
