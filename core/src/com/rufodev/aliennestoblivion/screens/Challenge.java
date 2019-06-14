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

public class Challenge implements Screen{
	MyGame game;
	OrthographicCamera uiCamera;
	Texture initScreen;
	BitmapFont font;
	BitmapFont fontSmall;
	Label labelTitle;
	Label labelLoading;
	LabelStyle labelStyle;
	LabelStyle labelStyleSmall;
	TextButtonStyle buttonStyle;
	TextButton buttonChallenge;
	TextButton buttonScores;
	TextButton buttonConnect;


	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private Label labelInfo;
	private LabelStyle labelStyleMicro;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;

	float scale;


	public Challenge(final MyGame game) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		game.playServices.signIn();
		scale = 1;
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		initAssets();
		if (game.playServices.isSignedIn()){
			buttonConnect.setVisible(false);
			labelInfo.setText("Connected to Google Play Services");
		}else{
			buttonConnect.setVisible(true);
			labelInfo.setText("Can't Connect to Google Play Services");
		}
	}
	
	public void initAssets(){
		
		initScreen = game.startUpManager.get("img/initScreen.jpg", Texture.class);
		
		
        //pm.dispose();
		
		
		///STAGE CRAP
		
		
		font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),true);
		labelStyle= new LabelStyle(font, Color.WHITE);
		labelStyleSmall= new LabelStyle(fontSmall, Color.WHITE);
		labelStyleMicro= new LabelStyle(fontMicro, Color.WHITE);
		labelTitle = new Label("ALIEN NEST: Oblivion\nChallenge Mode", labelStyle);
		
		labelLoading = new Label("Loading...", labelStyleSmall);
		
		labelInfo = new Label("Loading...", labelStyleMicro);
		
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;

		buttonChallenge = new TextButton ("Start!",buttonStyle);
		buttonScores = new TextButton ("Scores",buttonStyle);
		buttonConnect = new TextButton ("Retry Connection",buttonStyle);
		buttonChallenge.setWidth(250);
		buttonChallenge.setHeight(80);
		buttonScores.setWidth(250);
		buttonScores.setHeight(80);
		buttonConnect.setWidth(300);
		buttonConnect.setHeight(80);

		buttonChallenge.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				ConfigObject.setChallengeStage();
				game.setScreen(new ConfigScreen(game));
				dispose();
			}

		});

		buttonScores.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.playServices.showScore();

			}

		});

		buttonConnect.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				game.playServices.signIn();
				if (game.playServices.isSignedIn()){
					buttonConnect.setVisible(false);
					labelInfo.setText("Connected to Google Play Services");
				}else{
					buttonConnect.setVisible(true);
					labelInfo.setText("Can't Connect to Google Play Services");
				}
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
		
        labelLoading.setPosition(Gdx.graphics.getWidth() / 2 - labelLoading.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
		labelInfo.setFontScale(scale);
		labelInfo.setWidth(400 * scale);
		labelInfo.setHeight(30 * scale);


		buttonChallenge.setWidth(buttonChallenge.getWidth()/this.scale);
		buttonChallenge.setHeight(buttonChallenge.getHeight()/this.scale);
		buttonChallenge.setWidth(buttonChallenge.getWidth() * scale);
		buttonChallenge.setHeight(buttonChallenge.getHeight() * scale);
		buttonChallenge.setX(Gdx.graphics.getWidth() / 2 - buttonChallenge.getWidth() / 2);
		buttonChallenge.setY(Gdx.graphics.getHeight() / 2);
		buttonChallenge.getLabel().setFontScale(scale);

		buttonScores.setWidth(buttonScores.getWidth()/this.scale);
		buttonScores.setHeight(buttonScores.getHeight()/this.scale);
		buttonScores.setWidth(buttonScores.getWidth() * scale);
		buttonScores.setHeight(buttonScores.getHeight() * scale);
		buttonScores.setX(Gdx.graphics.getWidth() / 2 - buttonScores.getWidth() / 2);
		buttonScores.setY(Gdx.graphics.getHeight() / 2 + buttonScores.getHeight() + buttonScores.getHeight() / 3);
		buttonScores.getLabel().setFontScale(scale);

		buttonConnect.setWidth(buttonConnect.getWidth()/this.scale);
		buttonConnect.setHeight(buttonConnect.getHeight()/this.scale);
		buttonConnect.setWidth(buttonConnect.getWidth() * scale);
		buttonConnect.setHeight(buttonConnect.getHeight() * scale);
		buttonConnect.setX(buttonScores.getX() + buttonScores.getWidth() + 10 * scale);
		buttonConnect.setY(buttonScores.getY());
		buttonConnect.getLabel().setFontScale(scale);

		labelInfo.setPosition(Gdx.graphics.getWidth() / 2 - labelInfo.getWidth() / 2, buttonScores.getY() + buttonScores.getHeight() + 5*scale);

		this.scale= scale;
	}

	public void update(float delta){
		if (game.manager.update()){
			
			if (canPlay == false){

				game.stage.addActor(buttonChallenge);
				game.stage.addActor(buttonScores);
				game.stage.addActor(buttonConnect);
				canPlay = true;
				game.stage.addActor(labelInfo);

			}
			
			
		}else{
///
		}
		if (game.playServices.isSignedIn()){
			buttonConnect.setVisible(false);
			labelInfo.setText("Connected to Google Play Services");
		}else{
			buttonConnect.setVisible(true);
			labelInfo.setText("Can't Connect to Google Play Services");
		}

		game.stage.act();
		
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
		MyGame.batch.draw(initScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
