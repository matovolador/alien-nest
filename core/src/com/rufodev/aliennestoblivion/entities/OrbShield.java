package com.rufodev.aliennestoblivion.entities;

import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

/**
 * Created by papad on 5/5/2016.
 */
public class OrbShield extends Entity {
    Player holder;
    boolean init;
    int id;
    float offset;
    public OrbShield(Vector2 position, int health, int width, int height, Player holder, int id) {
        super(position, 1, width, height, false);
        super.speed = 100;
        this.holder = holder;
        this.power = 5;
        this.init = false;
        this.id = id;
        this.offset = 0;
    }

    @Override
    public void input(float delta) {

    }

    @Override
    public void update(float delta) {
        if (!init){
            init = true;
            //position orbs:
            switch(id){  //always take holder.width cause holder.width > holder.height
                case 0:  //right
                    this.position.x = holder.center.x + holder.width + offset;
                    this.position.y = holder.center.y;
                    break;
                case 1: //bottom
                    this.position.x = holder.center.x;
                    this.position.y = holder.center.y + holder.width;
                    break;
                case 2: //left
                    this.position.x = holder.center.x - holder.width;
                    this.position.y = holder.center.y;
                    break;
                case 3: //top
                    this.position.x = holder.center.x;
                    this.position.y = holder.center.y - holder.width;
                    break;
            }

        }

        ///make them rotate within a circle:
        //TODO





        //check collisions:
        for (int i = PlayScreen.entities.size()-1; i>=0; i-- ){
            if (!(PlayScreen.entities.get(i) instanceof Player) && (PlayScreen.entities.get(i).isEnemy) ){
                if (com.rufodev.aliennestoblivion.data.Collision.checkCollision(this, PlayScreen.entities.get(i))){
                    PlayScreen.entities.get(i).hurt(this.power);
                    this.health-=1;
                    if (this.health<=0){
                        ArrayList<OrbShield> copy=new ArrayList<OrbShield>(holder.orbArray);
                        copy.remove(this);
                        holder.orbArray=copy;
                    }
                }
            }
        }

    }

    @Override
    public void render() {

    }

    @Override
    public void hurt(int amount) {

    }
}
