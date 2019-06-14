package com.rufodev.aliennestoblivion.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by papad on 18/4/2016.
 */
public class DataManager {
    Preferences prefs;
    public DataManager(){
        prefs = Gdx.app.getPreferences("Alien Nest Oblivion");
    }

    public void saveGame(){
        prefs.putFloat("music",ConfigObject.volMusic);
        prefs.putFloat("soundFx",ConfigObject.volFx);
        prefs.putInteger("stage",ConfigObject.gameStage);
        prefs.putInteger("power",ConfigObject.powerIncrement);
        prefs.putInteger("charge",ConfigObject.chargeIncrement);
        prefs.putInteger("armor",ConfigObject.armorIncrement);

        prefs.flush();
    }

    public boolean loadGame(){
        ConfigObject.volMusic = prefs.getFloat("music",1f);
        ConfigObject.volFx = prefs.getFloat("volFx",0.6f);
        ConfigObject.powerIncrement = prefs.getInteger("power",0);
        ConfigObject.chargeIncrement = prefs.getInteger("charge",0);
        ConfigObject.armorIncrement = prefs.getInteger("armor",0);
        ConfigObject.gameStage = prefs.getInteger("stage",0);
        if (ConfigObject.gameStage == 0){
            return false;
        }
        return true;
    }

    public boolean canLoadGame(){
        int flag = prefs.getInteger("stage",0);
        if (flag == 0){
            return false;
        }
        return true;
    }

}
