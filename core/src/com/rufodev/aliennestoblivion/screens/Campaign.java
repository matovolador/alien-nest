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

public class Campaign implements Screen{
	MyGame game;
	OrthographicCamera uiCamera;
	Texture initScreen;
	BitmapFont font;
	BitmapFont fontSmall;
	Label labelTitle;
	LabelStyle labelStyle;
	LabelStyle labelStyleSmall;
	TextButtonStyle buttonStyle;
	TextButton buttonNewGame;
	TextButton buttonLoadGame;


	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private LabelStyle labelStyleMicro;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;
	boolean gameExists = false;
	float scale;

	public Campaign(final MyGame game) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		this.scale = 1;
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		initAssets();

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
		labelTitle = new Label("ALIEN NEST: Oblivion\nCampaign Mode", labelStyle);


		
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;

		buttonNewGame = new TextButton ("New Game",buttonStyle);
		buttonLoadGame = new TextButton ("Load Game",buttonStyle);

		buttonNewGame.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				ConfigObject.newGame();
				game.setScreen(new ConfigScreen(game));
				dispose();
			}
		});

		buttonLoadGame.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				gameExists = game.gameData.loadGame();

				if (gameExists){
					if (ConfigObject.gameStage==6){
						game.setScreen(new FinalScreen(game));
					}else {
						ConfigObject.upgradeStage(ConfigObject.gameStage);
						game.setScreen(new ConfigScreen(game));
					}
					dispose();
				}

			}

		});

	}
	
	public void setUI(int width,int height) {

		float aspectRatio = (float) width / (float) height;
		float scale = 1f;
		if (aspectRatio > MyGame.UI_ASPECT) {
			scale = (float) height / (float) MyGame.UI_HEIGHT;
		} else if (aspectRatio < MyGame.UI_ASPECT) {
			scale = (float) width / (float) MyGame.UI_WIDTH;
		} else {
			scale = (float) width / (float) MyGame.UI_WIDTH;
		}

		labelTitle.setFontScale(scale);
		labelTitle.setWidth(labelTitle.getWidth() / this.scale);
		labelTitle.setHeight(labelTitle.getHeight() / this.scale);
		labelTitle.setWidth(labelTitle.getWidth() * scale);
		labelTitle.setHeight(labelTitle.getHeight() * scale);
		labelTitle.setPosition(width / 2 - labelTitle.getWidth() / 2, 50 * scale);

		buttonNewGame.setWidth(250 * scale);
		buttonNewGame.setHeight(80 * scale);
		buttonNewGame.setX(Gdx.graphics.getWidth() / 2 - buttonNewGame.getWidth() / 2);
		buttonNewGame.setY(Gdx.graphics.getHeight() / 2);
		buttonNewGame.getLabel().setFontScale(scale, scale);

		buttonLoadGame.setWidth(250 * scale);
		buttonLoadGame.setHeight(80 * scale);
		buttonLoadGame.setX(Gdx.graphics.getWidth() / 2 - buttonLoadGame.getWidth() / 2);
		buttonLoadGame.setY(Gdx.graphics.getHeight() / 2 + buttonNewGame.getHeight() + buttonNewGame.getHeight() / 3);
		buttonLoadGame.getLabel().setFontScale(scale, scale);
	}

	public void update(float delta){
		if (game.manager.update()){
			
			if (canPlay == false){
				gameExists = game.gameData.canLoadGame();
				game.stage.addActor(buttonNewGame);
				if (gameExists)	game.stage.addActor(buttonLoadGame);
				canPlay = true;


			}
			
			
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
