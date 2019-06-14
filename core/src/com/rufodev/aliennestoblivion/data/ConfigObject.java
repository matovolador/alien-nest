package com.rufodev.aliennestoblivion.data;

public class ConfigObject {
    public static int difficulty = 2;
    public final static int maxDifficulty = 5;
    public static int gameStage = 0;  //999 = challenge mode
    public static int points = 0;
    public static int powerIncrement = 0;
    public static int chargeIncrement = 0;
    public static int armorIncrement = 0;
    public static float volMusic = 1f;
    public static float volFx =0.6f;


    public static void newGame(){
        gameStage=1;
        points = 3;
        powerIncrement = 0;
        chargeIncrement = 0;
        armorIncrement = 0;
        volMusic = 1f;
        volFx =0.6f;
    }
    public static void upgradeStage(int stage) {
        gameStage = stage;
        points = 3;
    }

    public static void setChallengeStage(){
        gameStage = 999;
        points = 3;
        powerIncrement = 0;
        chargeIncrement = 0;
        armorIncrement = 0;
        volMusic = 1f;
        volFx =0.6f;
    }

}


