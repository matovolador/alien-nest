package com.rufodev.aliennestoblivion.fx;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.rufodev.aliennestoblivion.data.ConfigObject;

public class SoundManager {
	

	public static ArrayList<Music> musicArray;
	public static Sound playerHurt;
	public static Sound playerYeah;
	public static Sound capsule;
	public static Sound bulletFired;
	public static Sound alienSpit;
	public static Sound click;
	public static Sound explosion;
	public static Sound shipDamage;
	
	//TIMERS:
	public static long lastSoundLock = 0;
	public static long soundLockTimer = 2000;
	
	public static long lastSound = System.currentTimeMillis();
	public static long soundTimer = 1500;
	
	public static long lastBulletSound = System.currentTimeMillis();
	public static long soundBulletTimer = 100;
	
	public static long lastAlienSound = System.currentTimeMillis();
	public static long soundAlienTimer = 700;
	
	
	public static void setMusicArray(ArrayList<Music> songs){
		SoundManager.musicArray = songs;
	}
	


	
	public static void startMusic(int id){
		if (musicArray.get(id).isPlaying()){
			return;
		}
		for (int i=0;i<musicArray.size();i++){
			musicArray.get(i).stop();
		}
		if (musicArray.get(id)!=null) {
			Music song = musicArray.get(id);
			song.setVolume(ConfigObject.volMusic);
			song.setLooping(true);
			song.play();
		}
	}
	
	
	
	public static void changeMusicSound(){
		for (int i=0;i<musicArray.size();i++){
			musicArray.get(i).setVolume(ConfigObject.volMusic);
		}
	}

	public static void effectHandler(float delta){

	}

	public static void playSound(Sound sound) {
		if (sound!=null){
			//this interacts with "achiev()"
			if (System.currentTimeMillis() - lastSoundLock >= soundLockTimer){
				sound.play(ConfigObject.volFx);

				lastSound = System.currentTimeMillis();
			}
		}
		
	}

	public static void playClick() {
		if (ConfigObject.volFx == 0) return;
		SoundManager.click.play(ConfigObject.volFx * 2);
	}
	
	public static void playerHurt(){
		playSound(playerHurt);
	}
	
	//plays a sound that locks every other sound with playSound() check on this var update
	public static void achiev(){
		stopAllSounds();
		playSound(playerYeah);
		lastSoundLock = System.currentTimeMillis();

	}
	
	public static void bulletFired(){
		if (System.currentTimeMillis() - lastBulletSound >= soundBulletTimer){
			playSound(bulletFired);
			lastBulletSound = System.currentTimeMillis();
		}
	}


	public static void alienDied() {
		
		if (System.currentTimeMillis() - lastAlienSound >= soundAlienTimer){
			//playSound(alienDeath);
			lastAlienSound = System.currentTimeMillis();
		}
	}

	public static void capsuleSound() {
		playSound(capsule);
		
	}
	
	public static void stopAllSounds(){
		playerHurt.stop();
		playerYeah.stop();
		capsule.stop();
		bulletFired.stop();
	}
}