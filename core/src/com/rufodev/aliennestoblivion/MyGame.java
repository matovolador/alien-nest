package com.rufodev.aliennestoblivion;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.rufodev.aliennestoblivion.data.DataManager;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.screens.PlayScreen;
import com.rufodev.aliennestoblivion.screens.MainScreen;
import com.badlogic.gdx.Net.*;


import java.util.ArrayList;

public class MyGame extends Game{
	public static PlayServices playServices;
	public static AssetManager manager;
	public static AssetManager startUpManager;
	public static SpriteBatch batch;
	public static TextureAtlas spriteSheet1 ;
	public Stage stage;
	GameState gameState = GameState.Start;
	public static final int WORLD_WIDTH = 1024;
	public static final int WORLD_HEIGHT = 576;
	public static float SCALE_X = 0;
	public static float SCALE_Y = 0;
	public static final int UI_WIDTH = 800;
	public static final int UI_HEIGHT = 600;
	public static float UI_ASPECT = UI_WIDTH/UI_HEIGHT;
	public DataManager gameData;

	
	
	///Virtual viewport //UNUSED!!
	public static final int VIRTUAL_WIDTH = MyGame.WORLD_WIDTH;
	public static final int VIRTUAL_HEIGHT = MyGame.WORLD_HEIGHT;
    public static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    public static Rectangle viewport;
	private boolean connectionStatus;

	public static GameStateChangeListener mGameStateChangeListener;

    
	public MyGame(PlayServices playServices) {
		super();
		this.playServices = playServices;


	}


	@Override
	public void create() {
		gameData = new DataManager();
		stage = new Stage();
		batch = new SpriteBatch();
		manager = new AssetManager();
		startUpManager = new AssetManager();

		loadAssets();


		setScreen(new MainScreen(this));
		
		
	}
	
	
	public void loadAssets(){
		//LOAD ENGINE ASSETS:
		
		
		//LOAD GAME ASSESTS
		startUpManager.load("img/initScreen.jpg", Texture.class);
		startUpManager.load("sound/click.mp3", Sound.class);
		manager.load("img/computerScreen.png", Texture.class);
		manager.load("img/grey.jpg", Texture.class);
		manager.load("sound/laser-zone.mp3", Music.class);
		manager.load("sound/commander-lounge.mp3", Music.class);
		manager.load("sound/strange-vectors.mp3", Music.class);
		manager.load("sound/smooth-gravity.mp3", Music.class);
		manager.load("sound/power-surge.mp3", Music.class);
		manager.load("sound/stellar-nucleus.mp3", Music.class);
		manager.load("sound/laser.mp3", Sound.class);
		manager.load("sound/capsule.mp3", Sound.class);
		manager.load("sound/hurt.mp3", Sound.class);
		manager.load("sound/yeah.mp3", Sound.class);
		manager.load("sound/spit.mp3", Sound.class);
		manager.load("sound/explosion.mp3", Sound.class);
		manager.load("sound/ship-damage.mp3", Sound.class);


		manager.load("img/back2.jpg", Texture.class);
		manager.load("img/back3.jpg", Texture.class);
		manager.load("img/back-mountain.jpg", Texture.class);
		manager.load("img/base-background.jpg", Texture.class);
		manager.load("img/back-stars.jpg", Texture.class);
		manager.load("img/config-background.jpg", Texture.class);
		manager.load("img/defeat-back.jpg", Texture.class);
		manager.load("img/victory-back.jpg", Texture.class);

		manager.load("img/spriteSheet1.pack", TextureAtlas.class);
		
		
		while(!manager.update()||!startUpManager.update()){
		//	renderStartScreen();
		}

		SoundManager.click= startUpManager.get("sound/click.mp3", Sound.class);
		initAssets();
		setSounds();
		
	}

	private void initAssets(){
		MyGame.spriteSheet1= manager.get("img/spriteSheet1.pack", TextureAtlas.class);
		PlayScreen.joystickInner = MyGame.spriteSheet1.createSprite("joystick-inner");
		PlayScreen.joystickOutter = MyGame.spriteSheet1.createSprite("joystick-outter");
		PlayScreen.alienImages =new ArrayList<Sprite>();
		PlayScreen.alienImages.add(MyGame.spriteSheet1.createSprite("alien-1-1"));
		PlayScreen.alienImages.add(MyGame.spriteSheet1.createSprite("alien-1-2"));

		PlayScreen.alien2Images =new ArrayList<Sprite>();
		PlayScreen.alien2Images.add(MyGame.spriteSheet1.createSprite("alien-2-1"));
		PlayScreen.alien2Images.add(MyGame.spriteSheet1.createSprite("alien-2-2"));

		PlayScreen.alien3Images =new ArrayList<Sprite>();
		PlayScreen.alien3Images.add(MyGame.spriteSheet1.createSprite("alien-3-1"));
		PlayScreen.alien3Images.add(MyGame.spriteSheet1.createSprite("alien-3-2"));

		PlayScreen.alienDeathImages =new ArrayList<Sprite>();
		PlayScreen.alienDeathImages.add(MyGame.spriteSheet1.createSprite("alien-death-1"));
		PlayScreen.alienDeathImages.add(MyGame.spriteSheet1.createSprite("alien-death-2"));
		PlayScreen.alienDeathImages.add(MyGame.spriteSheet1.createSprite("alien-death-3"));

		PlayScreen.acidImage = MyGame.spriteSheet1.createSprite("acid");

		PlayScreen.enemyShipOne = MyGame.spriteSheet1.createSprite("enemy-ship-1");

		PlayScreen.bulletImages=new ArrayList<Sprite>();
		PlayScreen.bulletImages.add(MyGame.spriteSheet1.createSprite("bullet1"));

		PlayScreen.bulletBlueImages=new ArrayList<Sprite>();
		PlayScreen.bulletBlueImages.add(MyGame.spriteSheet1.createSprite("bullet2"));

		PlayScreen.bulletRedImages=new ArrayList<Sprite>();
		PlayScreen.bulletRedImages.add(MyGame.spriteSheet1.createSprite("bullet3"));

		PlayScreen.spaceBullets = new ArrayList<Sprite>();
		PlayScreen.spaceBullets.add(MyGame.spriteSheet1.createSprite("space-bullet"));
		PlayScreen.spaceMissile= new ArrayList<Sprite>();
		PlayScreen.spaceMissile.add(MyGame.spriteSheet1.createSprite("space-missile"));

		PlayScreen.itemRedImages=new ArrayList<Sprite>();
		PlayScreen.itemRedImages.add(MyGame.spriteSheet1.createSprite("rCapsule"));
		PlayScreen.itemBlueImages=new ArrayList<Sprite>();
		PlayScreen.itemBlueImages.add(MyGame.spriteSheet1.createSprite("bCapsule"));
		PlayScreen.itemGreenImages = new ArrayList<Sprite>();
		PlayScreen.itemGreenImages.add(MyGame.spriteSheet1.createSprite("healthCapsule"));
		PlayScreen.itemPurpleImages = new ArrayList<Sprite>();
		PlayScreen.itemPurpleImages.add(MyGame.spriteSheet1.createSprite("bombCapsule"));
		PlayScreen.itemBlackImages = new ArrayList<Sprite>();
		PlayScreen.itemBlackImages.add(MyGame.spriteSheet1.createSprite("missileCapsule"));

		PlayScreen.walkAnimation =new ArrayList<Sprite>();
		PlayScreen.walkAnimation.add(MyGame.spriteSheet1.createSprite("character-walk1"));
		PlayScreen.walkAnimation.add(MyGame.spriteSheet1.createSprite("character-walk2"));
		PlayScreen.standAnimation = new ArrayList<Sprite>();
		PlayScreen.standAnimation.add(MyGame.spriteSheet1.createSprite("character-stand"));
		PlayScreen.playerShip = MyGame.spriteSheet1.createSprite("player-ship");
		PlayScreen.bossImages = new ArrayList<Sprite>();
		PlayScreen.bossImages.add(MyGame.spriteSheet1.createSprite("boss-1-1"));
		PlayScreen.bossImages.add(MyGame.spriteSheet1.createSprite("boss-1-2"));
		PlayScreen.bossWeaponBigImages = MyGame.spriteSheet1.createSprite("boss-laser-0");
		PlayScreen.bossWeaponBigLongImages = MyGame.spriteSheet1.createSprite("boss-laser-long");
		PlayScreen.landmine = MyGame.spriteSheet1.createSprite("landmine");
		PlayScreen.explosionImages = new ArrayList<Sprite>();
		PlayScreen.explosionImages.add(MyGame.spriteSheet1.createSprite("explosion-1"));
		PlayScreen.explosionImages.add(MyGame.spriteSheet1.createSprite("explosion-2"));
		PlayScreen.explosionImages.add(MyGame.spriteSheet1.createSprite("explosion-3"));



		PlayScreen.joystickInner = MyGame.spriteSheet1.createSprite("joystick-inner");
		PlayScreen.joystick2Inner = MyGame.spriteSheet1.createSprite("joystick-inner");
		PlayScreen.joystickOutter = MyGame.spriteSheet1.createSprite("joystick-outter");
		PlayScreen.joystick2Outter = MyGame.spriteSheet1.createSprite("joystick-outter");
		/*PlayScreen.bombEffect = MyGame.spriteSheet1.createSprite("bomb-effect");
		PlayScreen.bombClosed = MyGame.spriteSheet1.createSprite("bomb-closed");
		PlayScreen.bombOpen = MyGame.spriteSheet1.createSprite("bomb-open");*/



	}
	private void setSounds() {
		//FX
		SoundManager.bulletFired= manager.get("sound/laser.mp3", Sound.class);
		SoundManager.playerHurt = manager.get("sound/hurt.mp3", Sound.class);
		SoundManager.playerYeah = manager.get("sound/yeah.mp3", Sound.class);
		SoundManager.capsule = manager.get("sound/capsule.mp3", Sound.class);
		SoundManager.alienSpit = manager.get("sound/spit.mp3", Sound.class);
		SoundManager.explosion = manager.get("sound/explosion.mp3", Sound.class);
		SoundManager.shipDamage = manager.get("sound/ship-damage.mp3", Sound.class);
		//MUSIC
		ArrayList<Music> music = new ArrayList<Music>();
		music.add(manager.get("sound/commander-lounge.mp3", Music.class));
		music.add(manager.get("sound/strange-vectors.mp3", Music.class));
		music.add(manager.get("sound/stellar-nucleus.mp3", Music.class));
		music.add(manager.get("sound/laser-zone.mp3", Music.class));
		music.add(manager.get("sound/smooth-gravity.mp3", Music.class));
		music.add(manager.get("sound/power-surge.mp3", Music.class));



		SoundManager.setMusicArray(music);

	}
	public void render(){
		super.render();
		
	}

	
	public void dispose() {
		batch.dispose();
		manager.dispose();
		startUpManager.dispose();
		System.exit(0);
	}
	
	public static void resizeViewport(int width, int height) {
		float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > MyGame.ASPECT_RATIO)
        {
            scale = (float)height/(float)MyGame.VIRTUAL_HEIGHT;
            crop.x = (width - MyGame.VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < MyGame.ASPECT_RATIO)
        {
            scale = (float)width/(float)MyGame.VIRTUAL_WIDTH;
            crop.y = (height - MyGame.VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)MyGame.VIRTUAL_WIDTH;
        }

        float w = (float)MyGame.VIRTUAL_WIDTH*scale;
        float h = (float)MyGame.VIRTUAL_HEIGHT*scale;
        MyGame.viewport = new Rectangle(crop.x, crop.y, w, h);
		
	}


	public static float scaleUI(int width, int height) {
		float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        if(aspectRatio > MyGame.ASPECT_RATIO)
        {
            scale = (float)height/(float)MyGame.VIRTUAL_HEIGHT;
        }
        else if(aspectRatio < MyGame.ASPECT_RATIO)
        {
            scale = (float)width/(float)MyGame.VIRTUAL_WIDTH;
        }
        else
        {
            scale = (float)width/(float)MyGame.VIRTUAL_WIDTH;
        }

        return scale;
	}
	public boolean isConnected(){
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("https://google.com/").build();

		Net.HttpResponseListener httpResponseListener = new HttpResponseListener() {
			public void handleHttpResponse (Net.HttpResponse httpResponse) {
				HttpStatus status = httpResponse.getStatus();
				if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
					connectionStatus = true;
				} else {
					connectionStatus = false;
				}
			}

			@Override
			public void failed(Throwable t) {
			}

			@Override
			public void cancelled() {
			}
		};

		Gdx.net.sendHttpRequest(httpRequest, httpResponseListener);
		return connectionStatus;
	}

	public static enum GameState {
		Start, Running, GameOver
	}


	public static interface GameStateChangeListener {
		void onGameStateChanged(GameState newState);
	}

	public static void setGameStateChangeListener(GameStateChangeListener listener) {
		mGameStateChangeListener = listener;
	}


}
