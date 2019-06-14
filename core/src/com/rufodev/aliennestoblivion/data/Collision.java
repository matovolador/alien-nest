package com.rufodev.aliennestoblivion.data;

import com.badlogic.gdx.math.Rectangle;
import com.rufodev.aliennestoblivion.entities.Entity;
import com.rufodev.aliennestoblivion.entities.Item;


public class Collision {

	public Collision() {
		
	}
	public static boolean checkWalls(Rectangle wall, Entity ent){
		Rectangle rect=new Rectangle(ent.position.x,ent.position.y,ent.width,ent.height);
		return (rect.overlaps(wall));
	}
	public static boolean checkWalls(Rectangle wall, Item ent){
		Rectangle rect=new Rectangle(ent.position.x,ent.position.y,ent.width,ent.height);
		return (rect.overlaps(wall));
	}
	public static boolean checkWalls(Rectangle wall, Rectangle ent){
		return (ent.overlaps(wall));
	}
	public static boolean checkCollision(Entity ent1, Entity ent2) {
		 Rectangle rect=new Rectangle(ent1.position.x,ent1.position.y,ent1.width,ent1.height);
		 return (rect.overlaps(new Rectangle(ent2.position.x,ent2.position.y,ent2.width,ent2.height)));
	}
	
	public static boolean checkCollision(Item ent1, Entity ent2) {
		 Rectangle rect=new Rectangle((int)ent1.position.x,(int)ent1.position.y,(int)ent1.width,(int)ent1.height);
		 return (rect.overlaps(new Rectangle((int)ent2.position.x,(int)ent2.position.y,(int)ent2.width,(int)ent2.height)));
	}

	public static boolean checkCollision(Rectangle rect1, Rectangle rect2){
		return (rect1.overlaps(rect2));
	}
	
	

}
