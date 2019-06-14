package com.rufodev.aliennestoblivion.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.data.ConfigObject;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.data.Utils;
import com.rufodev.aliennestoblivion.entities.Entity;
import com.rufodev.aliennestoblivion.entities.Item;
import com.rufodev.aliennestoblivion.entities.Player;
import com.rufodev.aliennestoblivion.fx.Map;
import com.rufodev.aliennestoblivion.fx.SoundManager;
import com.rufodev.aliennestoblivion.fx.VisualEffect;
import com.rufodev.aliennestoblivion.input.MyInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlayScreen implements Screen{
	public static ArrayList<Sprite> spaceMissile;
	MyGame game;
	OrthographicCamera gameCamera;
	OrthographicCamera uiCamera;
	public Player player;
	ApplicationType appType;
	static int milestone=50;
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<VisualEffect> visualEffects = new ArrayList<VisualEffect>();
	public static Random random = new Random();
	public static long lastASpawn = System.currentTimeMillis();
	public static long spawnATimer= 500;
	public static long lastBSpawn = System.currentTimeMillis();
	public static long spawnBTimer = 25000;
	public static long lastCapsuleSpawn = System.currentTimeMillis();
	public static long SpawnTimerCapsule = 2000;
    public static long lastHealthSpawn = System.currentTimeMillis();
	public static long SpawnTimerHealth=20000;
	public static Map map;
	public static ArrayList<Sprite> walkAnimation;
	public static ArrayList<Sprite> standAnimation;
	public static ArrayList<Sprite> alienImages;
	public static ArrayList<Sprite> alien2Images;
	public static ArrayList<Sprite> alien3Images;
	public static ArrayList<Sprite> bossImages;
	public static Sprite bossWeaponBigImages;
	public static Sprite bossWeaponBigLongImages;
	public static ArrayList<Sprite> bulletImages;
	public static ArrayList<Sprite> bulletBlueImages;
	public static ArrayList<Sprite> bulletRedImages;
	public static ArrayList<Sprite> itemRedImages;
	public static ArrayList<Sprite> itemBlueImages;
	public static ArrayList<Sprite> itemGreenImages;
	public static Sprite playerShip;
	public static Sprite enemyShipOne;
	public static Sprite acidImage;
	public static Sprite landmine;
	public static ArrayList<Sprite> explosionImages;
	public static ArrayList<Sprite> alienDeathImages;
	public static ArrayList<Sprite> spaceBullets;
	Texture grey;
	/*public static Sprite bombEffect;
	public static Sprite bombOpen;
	public static Sprite bombClosed;*/
	BitmapFont font;
	public Label labelHealth;
	public Label labelInfo;
	LabelStyle labelStyle;
	float delta;
	public static FitViewport gameViewport;
	private ScreenViewport uiViewport;
	public static FitViewport backgroundViewport;
	private OrthographicCamera backgroundCamera;
	public static int gameState = 0;  // 0 = paused, 1 = resumed
	public static Sprite joystickInner;
	public static Sprite joystickOutter;
	public static Sprite joystick2Inner;
	public static Sprite joystick2Outter;
	public StageHandler stageHandler;
	float scale;
	public static ArrayList<Sprite> itemPurpleImages;
	public static ArrayList<Sprite> itemBlackImages;

	private boolean shouldExit = false;
	private Label labelMessage;
	private Skin skinButton;
	private TextureAtlas buttonAtlas;
	private TextButton.TextButtonStyle buttonStyle;
	private BitmapFont fontSmall;
	private TextButton buttonExit;

	public PlayScreen(final MyGame game) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		this.stageHandler = new StageHandler(ConfigObject.gameStage);
		//Stage setup from StageHandler

		entities = new ArrayList<Entity>();
		items = new ArrayList<Item>();
		visualEffects = new ArrayList<VisualEffect>();
		lastASpawn = System.currentTimeMillis();
		spawnATimer= stageHandler.SpawnATimer;
		lastBSpawn = System.currentTimeMillis();
		spawnBTimer = stageHandler.SpawnBTimer;
		lastCapsuleSpawn = System.currentTimeMillis();
		SpawnTimerCapsule = stageHandler.SpawnTimerCapsule;
		lastHealthSpawn = System.currentTimeMillis();
		SpawnTimerHealth = stageHandler.SpawnTimerHealth;
	///

		gameCamera = new OrthographicCamera();
		gameCamera.setToOrtho(false);
		gameViewport = new FitViewport(MyGame.WORLD_WIDTH,MyGame.WORLD_HEIGHT,gameCamera);
		gameViewport.setScreenBounds(0,0,MyGame.WORLD_WIDTH,MyGame.WORLD_HEIGHT);
		backgroundCamera = new OrthographicCamera();
		backgroundCamera.setToOrtho(false);
		backgroundViewport = new FitViewport(MyGame.WORLD_WIDTH,MyGame.WORLD_HEIGHT,backgroundCamera);
		backgroundViewport.setScreenBounds(0,0,MyGame.WORLD_WIDTH,MyGame.WORLD_HEIGHT);
		
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true );
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		scale = 1;
		appType = Gdx.app.getType();
		
		
		try {
			initAssets();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Player Creation Logic:----------------
		float speed = stageHandler.getPlayerSpeed();
		Vector2 position = stageHandler.getPlayerPosition();
		player = new Player(position,walkAnimation,standAnimation,joystickInner,joystickOutter,joystick2Inner,joystick2Outter,playerShip);
		player.setSpeed(speed);
		player.score=0;
		///Check if its space combat:
		if (stageHandler.isPlayerShip()){
			player.setAsShip(true);
		}else{
			player.setAsShip(false);
		}
		///Add the player:
		entities.add(player);
		//-----------------------------------------
        gameCamera.position.set(MyGame.WORLD_WIDTH/2, MyGame.WORLD_HEIGHT/2, 0);
		//gameCamera.position.set(player.position.x + MyGame.WORLD_WIDTH / 2, player.position.y + player.height/2, 0);
	}
	
	
	
	
	public void initAssets() throws IOException{
        MyGame.mGameStateChangeListener.onGameStateChanged(MyGame.GameState.Running);

		grey = MyGame.manager.get("img/grey.jpg", Texture.class);
        //STAGE:
		font = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		labelStyle= new LabelStyle(font, Color.WHITE);
		labelHealth = new Label("Health:", labelStyle);
		
		labelInfo = new Label("Score:", labelStyle);
		//
		labelMessage = new Label("Go back to main menu?",labelStyle);
		labelMessage.setVisible(false);

		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButton.TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;
		buttonExit = new TextButton("Exit",buttonStyle);
		buttonExit.setVisible(false);

		buttonExit.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				game.setScreen(new MainScreen(game));
			}
		});

		
		gameState= 1;

		
	}
	
	public void setUI(int width,int height){
		float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        if(aspectRatio > MyGame.UI_ASPECT)
        {
            scale = (float)height/(float)MyGame.UI_HEIGHT;
        }
        else if(aspectRatio < MyGame.UI_ASPECT)
        {
            scale = (float)width/(float)MyGame.UI_WIDTH;
        }
        else
        {
           scale = (float)width/(float)MyGame.UI_WIDTH;
        }
		

		labelHealth.setPosition(0, 0);
		labelHealth.setWidth(labelHealth.getWidth()/this.scale);
		labelHealth.setHeight(labelHealth.getHeight()/this.scale);
		labelHealth.setWidth(labelHealth.getWidth() *scale);
		labelHealth.setHeight(labelHealth.getHeight() * scale);
		labelHealth.setFontScale(scale);
		labelInfo.setWidth(labelInfo.getWidth()/this.scale);
		labelInfo.setHeight(labelInfo.getHeight()/this.scale);
		labelInfo.setWidth(labelInfo.getWidth() *scale);
		labelInfo.setHeight(labelInfo.getHeight() * scale);
        labelInfo.setPosition(0, labelHealth.getHeight());
        labelInfo.setFontScale(scale);


		labelMessage.setWidth(labelMessage.getWidth()/this.scale);
		labelMessage.setHeight(labelMessage.getHeight()/this.scale);
		labelMessage.setWidth(labelMessage.getWidth() *scale);
		labelMessage.setHeight(labelMessage.getHeight() * scale);
		labelMessage.setPosition(Gdx.graphics.getWidth()/2 - labelMessage.getWidth()/2,Gdx.graphics.getHeight()/2);
		labelMessage.setFontScale(scale);

		buttonExit.setWidth(buttonExit.getWidth()/this.scale);
		buttonExit.setHeight(buttonExit.getHeight()/this.scale);
		buttonExit.setWidth(buttonExit.getWidth() * scale);
		buttonExit.setHeight(buttonExit.getHeight() * scale);
		buttonExit.setPosition(Gdx.graphics.getWidth()/2 - buttonExit.getWidth()/2,labelMessage.getY() + buttonExit.getHeight());
		buttonExit.getLabel().setFontScale(scale);




		MyInput.fixJoystick(player,scale);

		this.scale = scale;

	}
	
	





	public void input (float delta){
		if (shouldExit){
			game.setScreen(new MainScreen(game));
		}
		if (Gdx.input.isKeyJustPressed(Keys.BACK)){
			if (PlayScreen.gameState==1) {
				PlayScreen.gameState = 0;
			}else{
				PlayScreen.gameState = 1;
			}
		}
		if (gameState==1){
			labelMessage.setVisible(false);
			buttonExit.setVisible(false);
			labelInfo.setVisible(true);
			labelHealth.setVisible(true);
		}else{
			labelMessage.setVisible(true);
			buttonExit.setVisible(true);
			labelInfo.setVisible(false);
			labelHealth.setVisible(false);
		}
		game.stage.act(delta);
		if (PlayScreen.gameState == 0) return;

		this.delta=delta;

		//entities input
		for (Entity entity : entities){
			entity.input(delta);
		}

	}
	
	
	public void update(float delta){
		if (gameState == 0) return;
		stageHandler.update(this,delta);
		game.stage.act();
		
	}


	@Override
	public void render(float delta) {
		entities.trimToSize();
		items.trimToSize();
		visualEffects.trimToSize();
		player.bulletArray.trimToSize();
		

		
		input(Gdx.graphics.getDeltaTime());
		
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0,0,0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Color c = MyGame.batch.getColor();
		MyGame.batch.setColor(c.r, c.g, c.b, 1f);
		// update camera
        gameCamera.update();
        uiCamera.update();
        backgroundCamera.update();
		
		// set virtual viewport 
        //Gdx.gl.glViewport((int) MyGame.viewport.x, (int) MyGame.viewport.y,(int) MyGame.viewport.width, (int) MyGame.viewport.height);
		//gameViewport.apply();  //not using when using virtual viewport
			
		
		
		//RENDER ELEMENTS -------------------
		//
		//Background map ---------------
        MyGame.batch.setProjectionMatrix(backgroundCamera.combined);
		backgroundViewport.apply();
		MyGame.batch.begin();
		map.performSideScrolling(delta);
		MyGame.batch.end();
		//
		
		
		//render entities
		//
		MyGame.batch.setProjectionMatrix(gameCamera.combined);
		gameViewport.apply();
		MyGame.batch.begin();

		for (int i = visualEffects.size()-1; i>=0;i--){
			visualEffects.get(i).render();
		}

		for (int i = entities.size()-1; i>=0;i--){
			entities.get(i).render();
		}
		
		for (int i = items.size()-1; i>=0;i--){
			items.get(i).render();
		}
		
		MyGame.batch.end();

		/*
		MyGame.batch.setProjectionMatrix(backgroundCamera.combined);
		backgroundViewport.apply();
		MyGame.batch.begin();
		smoke.performSideScrolling(Gdx.graphics.getDeltaTime());
		MyGame.batch.end();
		*/
		MyGame.batch.setProjectionMatrix(uiCamera.combined);
		game.stage.getViewport().apply();
		MyGame.batch.begin();
		player.joystickOutter.draw(MyGame.batch,0.5f);
		player.joystickInner.draw(MyGame.batch,0.75f);
		player.joystick2Outter.draw(MyGame.batch,0.5f);
		player.joystick2Inner.draw(MyGame.batch,0.75f);
		/* BOMB
		if (!player.isShip) {
			if (player.bombEnabled && StageHandler.isCombatEnabled) {
				PlayScreen.bombOpen.draw(MyGame.batch);
			} else {
				PlayScreen.bombClosed.draw(MyGame.batch);
			}
		}
		*/
		MyGame.batch.end();
		game.stage.draw();
		SoundManager.effectHandler(delta);

		if (gameState==0){
			MyGame.batch.setProjectionMatrix(uiCamera.combined);
			game.stage.getViewport().apply();
			MyGame.batch.setColor(c.r, c.g, c.b, 0.6f);
			MyGame.batch.begin();
			MyGame.batch.draw(grey,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			MyGame.batch.end();
			game.stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		///resize virtual viewport
		// calculate new viewport
		//MyGame.resizeViewport(width,height);
		//////////-----------------
		uiViewport.update(width,height,true);
		gameViewport.update(width,height);  //not used when using the virtual viewport in the render method.
		backgroundViewport.update(width, height, true);

		this.setUI(width,height);
		
	}

	@Override
	public void show() {
		game.stage = new Stage(uiViewport);
		
		game.stage.addActor(labelHealth);
		game.stage.addActor(labelInfo);
		game.stage.addActor(labelMessage);
		game.stage.addActor(buttonExit);
		Gdx.input.setInputProcessor(game.stage);
                
		game.stage.act();
		if (ConfigObject.gameStage>0 && ConfigObject.gameStage<=5){
			SoundManager.startMusic(ConfigObject.gameStage);
		}
		if (ConfigObject.gameStage==999){
			int i = Utils.doRandom(1,5);
			SoundManager.startMusic(i);
		}

		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
	}




	public void endGame(boolean victory) {
		SoundManager.stopAllSounds();
		if (ConfigObject.gameStage!=999){
			game.setScreen(new EndScreen(game,victory));
			dispose();
		}else{
			game.setScreen(new ScoreScreen(game,player.score));
		}
	}

	

	

}
