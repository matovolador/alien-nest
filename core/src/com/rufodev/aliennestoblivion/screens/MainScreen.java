package com.rufodev.aliennestoblivion.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
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
import com.rufodev.aliennestoblivion.fx.SoundManager;

public class MainScreen implements Screen{
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
	TextButton buttonCampaign;
	TextButton buttonRate;
	
	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private Label labelInfo;
	private LabelStyle labelStyleMicro;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;
	
	float scale;
	
	
	public MainScreen(final MyGame game) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		this.scale = 1;
		MyGame.mGameStateChangeListener.onGameStateChanged(MyGame.GameState.Start);

		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		this.game.startUpManager.finishLoading();
		initAssets();
	}
	
	public void initAssets(){
		
		initScreen = game.startUpManager.get("img/initScreen.jpg", Texture.class);
		MyGame.mGameStateChangeListener.onGameStateChanged(MyGame.GameState.Running);
		
        //pm.dispose();
		
		
		///STAGE CRAP
		
		
		font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),true);
		labelStyle= new LabelStyle(font, Color.WHITE);
		labelStyleSmall= new LabelStyle(fontSmall, Color.WHITE);
		labelStyleMicro= new LabelStyle(fontMicro, Color.WHITE);
		labelTitle = new Label("ALIEN NEST: Oblivion", labelStyle);
		
		labelLoading = new Label("Loading...", labelStyleSmall);
		
		labelInfo = new Label("Loading...", labelStyleMicro);
		
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;

		buttonCampaign = new TextButton ("Campaign",buttonStyle);
		buttonChallenge = new TextButton ("Challenge",buttonStyle);
        buttonCampaign.setWidth(250);
        buttonCampaign.setHeight(80);
        buttonChallenge.setWidth(250);
        buttonChallenge.setHeight(80);
		buttonRate = new TextButton ("Rate Game",buttonStyle);
		buttonRate.setWidth(250);
		buttonRate.setHeight(80);

		buttonChallenge.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				game.setScreen(new Challenge(game));

				dispose();
				//super.touchUp(event, x, y, pointer, button);
			}

		});

		buttonCampaign.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				game.setScreen(new StoryScreen(game,0));
				dispose();

			}

		});

		buttonRate.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				MyGame.playServices.rateGame();

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
		labelTitle.setWidth(labelTitle.getWidth()/this.scale);
		labelTitle.setHeight(labelTitle.getHeight()/this.scale);
        labelTitle.setWidth(labelTitle.getWidth() * scale);
        labelTitle.setHeight(labelTitle.getHeight() * scale);
        labelTitle.setPosition(width / 2 - labelTitle.getWidth() / 2, 50 * scale );
		
        //labelLoading.setPosition(Gdx.graphics.getWidth() / 2 - labelLoading.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
		labelInfo.setFontScale(scale);
		labelInfo.setWidth(labelInfo.getWidth()/this.scale);
		labelInfo.setHeight(labelInfo.getHeight()/this.scale);
		labelInfo.setWidth(labelInfo.getWidth() * scale);
		labelInfo.setHeight(labelInfo.getHeight() * scale);




		buttonCampaign.setWidth(buttonCampaign.getWidth()/this.scale);
		buttonCampaign.setHeight(buttonCampaign.getHeight()/this.scale);
		buttonCampaign.setWidth(buttonCampaign.getWidth() * scale);
		buttonCampaign.setHeight(buttonCampaign.getHeight() * scale);
		buttonCampaign.setX(Gdx.graphics.getWidth() / 2 - buttonCampaign.getWidth() / 2);
		buttonCampaign.setY(Gdx.graphics.getHeight() / 2);
		buttonCampaign.getLabel().setFontScale(scale);

		buttonChallenge.setWidth(buttonChallenge.getWidth()/this.scale);
		buttonChallenge.setHeight(buttonChallenge.getHeight()/this.scale);
		buttonChallenge.setWidth(buttonChallenge.getWidth() * scale);
		buttonChallenge.setHeight(buttonChallenge.getHeight() * scale);
		buttonChallenge.setX(Gdx.graphics.getWidth() / 2 - buttonChallenge.getWidth() / 2);
		buttonChallenge.setY(Gdx.graphics.getHeight() / 2 + buttonChallenge.getHeight()+5*scale);
		buttonChallenge.getLabel().setFontScale(scale);

		buttonRate.setWidth(buttonRate.getWidth()/this.scale);
		buttonRate.setHeight(buttonRate.getHeight()/this.scale);
		buttonRate.setWidth(buttonRate.getWidth() * scale);
		buttonRate.setHeight(buttonRate.getHeight() * scale);
		buttonRate.setX(Gdx.graphics.getWidth() - buttonRate.getWidth());
		buttonRate.setY(Gdx.graphics.getHeight() - buttonRate.getHeight());
		buttonRate.getLabel().setFontScale(scale);


		labelInfo.setPosition(Gdx.graphics.getWidth() / 2 - labelInfo.getWidth() / 2, buttonCampaign.getY() + buttonCampaign.getHeight() + 5*scale);

		this.scale = scale;
	}

	public void update(float delta){
		if (game.manager.update()){
			
			if (canPlay == false){

				game.stage.addActor(buttonChallenge);
				game.stage.addActor(buttonCampaign);
				game.stage.addActor(buttonRate);
				canPlay = true;
				SoundManager.startMusic(0);

			}
			
			
		}else{
///
		}

		game.stage.act();
		
	}
	public void input(float delta){
		if (Gdx.input.isKeyJustPressed(Keys.BACK)){
			Gdx.app.exit();
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
