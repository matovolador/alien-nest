package com.rufodev.aliennestoblivion.fx;

import com.badlogic.gdx.math.Vector2;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

import java.util.ArrayList;

/**
 * Created by papad on 8/6/2016.
 */
public class VisualEffect {
    Animator animation;
    long duration;
    long initTime;
    Vector2 pos;
    boolean started;

    public VisualEffect(Animator animation, long duration, Vector2 pos){
        this.animation = animation;
        this.duration = duration;
        this.pos = pos;
        this.started = false;

    }

    public void render(){
        if (!started){
            started = true;
            this.initTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.initTime < this.duration){
            //continue animation:
            this.animation.animate(this.pos.x,this.pos.y);

        }else{
            //animation ends, so remove it:
            ArrayList<VisualEffect> copy;
            copy=new ArrayList<VisualEffect>(PlayScreen.visualEffects);
            copy.remove(this);
            PlayScreen.visualEffects=copy;
        }


    }
}
