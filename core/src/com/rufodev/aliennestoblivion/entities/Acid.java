package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.fx.Animator;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

public class Acid extends Entity{

	public float speed;
	public Animator animator;
	public boolean initialized=false;
	public Entity holder;
	Player target;
    double scale_X;
    double scale_Y;
    int number=0; // if not 0, its triple bullet
    ArrayList<Acid> copy;

    public Acid(int number, ArrayList<Sprite> images, Entity holder) {
		super(new Vector2(holder.position.x+holder.width/2-16/2,holder.position.y+holder.height/2-6/2),0,16,6,true);
		this.speed=400;
		super.animator=new Animator(images,0);
		this.holder=holder;
        this.number=number;
		super.power = 2;
		this.center.x = this.position.x - this.width/2;
		this.center.y = this.position.y - this.height/2;
	}
	
	
	private void fireAcid( float delta){  //direction :  0= to the right, 1 = to the left
		if (target==null){
			for (int i = 0; i< PlayScreen.entities.size(); i++){
				if (PlayScreen.entities.get(i) instanceof Player)
					target = (Player) PlayScreen.entities.get(i);
			}

		}
		if (!initialized){

			

		
			double angle = Math.atan2( target.center.y - this.center.y ,  target.center.x - this.center.x  ) ;

			if (this.number==0){
				this.scale_X = Math.cos(angle) ;
				this.scale_Y = Math.sin(angle);

			}
			if (this.number==1){
				angle+=0.3;
				this.scale_X = Math.cos(angle) ;
				this.scale_Y = Math.sin(angle);

			}
			if (this.number==2){
				angle+=0.3 * 2;
				this.scale_X = Math.cos(angle) ;
				this.scale_Y = Math.sin(angle);
			}
			if (this.number==3){
				angle-=0.3;
				this.scale_X = Math.cos(angle) ;
				this.scale_Y = Math.sin(angle);

			}
			if (this.number==4){
				angle-=0.3*2;
				this.scale_X = Math.cos(angle) ;
				this.scale_Y = Math.sin(angle);

			}
			double auxAngle = MathUtils.radiansToDegrees * angle;
			if (auxAngle<0) auxAngle+=360;
			super.animator.degrees = ((float)auxAngle);


			//SoundManager.alienSpit();

		}
		initialized=true;
		return;		
		
	}
	
	public void input(float delta) {
		
	}
	
	public void update(float delta){
		updateCenter();
		this.fireAcid(delta);

		this.position.x += speed * delta * scale_X;
		this.position.y += speed * delta * scale_Y;
		this.center.x = this.position.x - this.width/2;
		this.center.y = this.position.y - this.height/2;
		//hits player?
		for (int i = PlayScreen.entities.size()-1; i>=0; i-- ){
			if ((PlayScreen.entities.get(i) instanceof Player)){
				if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, PlayScreen.entities.get(i))){
					PlayScreen.entities.get(i).hurt(this.power);
					this.power-=power;
					if (this.power<=0){
						copy=new ArrayList<Acid>(holder.acidArray);
						copy.remove(this);
						holder.acidArray=copy;
					}
					
				}
			}
		}

		///destroy if out of bounds
		if (this.position.x<0){
			copy=new ArrayList<Acid>(holder.acidArray);
			copy.remove(this);
			holder.acidArray=copy;
		}
		if (this.position.y<0){
			copy=new ArrayList<Acid>(holder.acidArray);
			copy.remove(this);
			holder.acidArray=copy;
		}
		if (this.position.y  >Gdx.graphics.getHeight()){
			copy=new ArrayList<Acid>(holder.acidArray);
			copy.remove(this);
			holder.acidArray=copy;
		}

		//Wall collision:
		boolean hitsWall = false;
		for (int i = 0; i< StageHandler.collisions.size(); i++){
			if (Collision.checkWalls(StageHandler.collisions.get(i),this)) hitsWall = true;
		}
		if (hitsWall){
			copy=new ArrayList<Acid>(holder.acidArray);
			copy.remove(this);
			holder.acidArray=copy;
		}

	}
	
	public void render(){
		super.animator.animate(this.position.x,this.position.y);
	}


	@Override
	public void hurt(int amount) {
		//overwriten by "collision with player"
		
	}
}
