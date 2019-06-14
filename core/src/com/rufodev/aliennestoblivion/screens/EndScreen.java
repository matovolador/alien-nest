package com.rufodev.aliennestoblivion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.data.ConfigObject;
import com.rufodev.aliennestoblivion.fx.SoundManager;

public class EndScreen implements Screen{
	MyGame game;
	OrthographicCamera uiCamera;
	Texture initScreen;
	BitmapFont font;
	BitmapFont fontSmall;
	Label labelTitle;
	LabelStyle labelStyle;
	LabelStyle labelStyleSmall;
	TextButtonStyle buttonStyle;
	TextButton buttonContinue;


	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private Label labelInfo;
	private LabelStyle labelStyleMicro;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;
	private Texture victoryBack;
	private Texture defeatBack;
	private boolean victory;
	float scale;
	boolean flagRunAd = true;

	public EndScreen(final MyGame game, boolean victory) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		this.scale = 1;
		this.victory = victory;
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		initAssets();
		saveGame();


	}

	private void saveGame() {

		if (victory){
			ConfigObject.upgradeStage(ConfigObject.gameStage);
			ConfigObject.gameStage+=1;
			game.gameData.saveGame();
		}

	}

	public void initAssets(){

		victoryBack = game.manager.get("img/victory-back.jpg", Texture.class);
		defeatBack = game.manager.get("img/defeat-back.jpg", Texture.class);


        //pm.dispose();
		
		
		///STAGE CRAP
		
		
		font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),true);
		labelStyle= new LabelStyle(font, Color.WHITE);
		labelStyleSmall= new LabelStyle(fontSmall, Color.WHITE);
		labelStyleMicro= new LabelStyle(fontMicro, Color.WHITE);
		if (victory) {
			labelTitle = new Label("VICTORY", labelStyle);
		}else{
			labelTitle = new Label("DEFEAT", labelStyle);
		}

		labelInfo = new Label("Game Saved",labelStyleSmall);
		
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;
		if (victory){
			buttonContinue = new TextButton ("Continue",buttonStyle);
			buttonContinue.setWidth(250);
			buttonContinue.setHeight(80);
		}else{
			buttonContinue = new TextButton ("Restart",buttonStyle);
			buttonContinue.setWidth(250);
			buttonContinue.setHeight(80);
		}
		

		buttonContinue.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				//TEMP:
				SoundManager.playClick();
				if (ConfigObject.gameStage==6){
					game.setScreen(new StoryScreen(game));
				}else {
					game.setScreen(new ConfigScreen(game));
				}
				dispose();

			}

		});

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

		labelTitle.setFontScale(scale);
		labelTitle.setWidth(labelTitle.getWidth() / this.scale);
		labelTitle.setHeight(labelTitle.getHeight() / this.scale);
		labelTitle.setWidth(labelTitle.getWidth() * scale);
		labelTitle.setHeight(labelTitle.getHeight() * scale);
		labelTitle.setPosition(width / 2 - labelTitle.getWidth() / 2, 50 * scale);

		buttonContinue.setWidth(buttonContinue.getWidth()/this.scale);
		buttonContinue.setHeight(buttonContinue.getHeight()/this.scale);
		buttonContinue.setWidth(buttonContinue.getWidth() * scale);
		buttonContinue.setHeight(buttonContinue.getHeight() * scale);
		buttonContinue.setX(Gdx.graphics.getWidth() / 2 - buttonContinue.getWidth() / 2);
		buttonContinue.setY(Gdx.graphics.getHeight() / 2);
		buttonContinue.getLabel().setFontScale(scale, scale);

		labelInfo.setFontScale(scale);
		labelInfo.setWidth(labelInfo.getWidth() / this.scale);
		labelInfo.setHeight(labelInfo.getHeight() / this.scale);
		labelInfo.setWidth(labelInfo.getWidth() * scale);
		labelInfo.setHeight(labelInfo.getHeight() * scale);
		labelInfo.setPosition(Gdx.graphics.getWidth() / 2 - labelInfo.getWidth() / 2, buttonContinue.getY()/2 + buttonContinue.getHeight() + 15*scale);
	}

	public void update(float delta){
		if (game.manager.update()){
			
			if (canPlay == false){
				game.stage.addActor(buttonContinue);
				canPlay = true;
				if (victory)
				game.stage.addActor(labelInfo);

			}
			
			
		}else{
///
		}

		game.stage.act();

		if (flagRunAd){
			flagRunAd= false;
			MyGame.mGameStateChangeListener.onGameStateChanged(MyGame.GameState.GameOver);
		}
		
	}
	public void input(float delta){
		if (Gdx.input.isKeyJustPressed(Keys.BACK)){
			game.setScreen(new MainScreen(game));
		}
	}
	
	public void render(float delta) {
		input(delta);
		update(delta);


		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		uiCamera.update();

		MyGame.batch.setProjectionMatrix(uiCamera.combined);
		uiViewport.apply();
		MyGame.batch.begin();
		if (victory) {
			MyGame.batch.draw(victoryBack, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}else{
			MyGame.batch.draw(defeatBack, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		MyGame.batch.end();
		game.stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		///resize virtual viewport
		// calculate new viewport
		//MyGame.resizeViewport(width,height);
		//////////-----------------
		uiViewport.update(width,height,true);

		if (flagRunAd)
		this.setUI(width,height);
	}

	@Override
	public void show() {
		game.stage = new Stage(uiViewport);
		game.stage.addActor(labelTitle);
		Gdx.input.setInputProcessor(game.stage);

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

}
