package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.ConfigObject;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

public class BulletBoss extends Entity{
	public float speed;
	public Animator animator;
	public boolean initialized=false;
	public Boss holder;
    double scale_X;
    double scale_Y;
    int number=0; // if not 0, its triple bullet
    ArrayList<BulletBoss> copy;
	Player target;

    public BulletBoss(int number, int power, ArrayList<Sprite> images, float speed, Boss holder, int width, int height, Player player) {
		super(new Vector2(holder.bulletSpawn.x - width/2  ,holder.bulletSpawn.y - height/2 ),1,width,height, false);
		this.speed=500;
		super.animator=new Animator(images,0);
		this.holder=holder;
        this.number=number;
        super.power=power;
		this.target = player;
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
			double angle = Math.atan2( target.center.y - this.center.y ,  target.center.x - this.center.x  ) ;
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
		updateCenter();
        holder.updateBulletSpawn();
		this.fireBullet(delta);


		this.position.x += speed * delta * scale_X;
		this.position.y += speed * delta * scale_Y;


		for (int i = PlayScreen.entities.size()-1; i>=0; i-- ){
			if ((PlayScreen.entities.get(i) instanceof Player)  ){
				if (Collision.checkCollision(this, PlayScreen.entities.get(i))){
					PlayScreen.entities.get(i).hurt(this.power);
					this.health-=1;
					if (this.health<=0){
						copy=new ArrayList<BulletBoss>(holder.bulletArray);
						copy.remove(this);
						holder.bulletArray=copy;
					}
				}
			}
		}
		//IS OUT OF BOUNDS?
		if ((this.position.x  > Gdx.graphics.getWidth()||(this.position.x<0)||(this.position.y<0)||(this.position.y>Gdx.graphics.getHeight()))){

			copy=new ArrayList<BulletBoss>(holder.bulletArray);
			copy.remove(this);
			holder.bulletArray=copy;
		}

		//Wall collision:
		boolean hitsWall = false;
		for (int i = 0; i< StageHandler.collisions.size(); i++){
			if (Collision.checkWalls(StageHandler.collisions.get(i),this)) hitsWall = true;
		}
		if (hitsWall){
			copy=new ArrayList<BulletBoss>(holder.bulletArray);
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
		super.animator.degrees =(float) angle;
		super.animator.animate(this.position.x,this.position.y);
	}


	@Override
	public void hurt(int amount) {
		
	}
}
