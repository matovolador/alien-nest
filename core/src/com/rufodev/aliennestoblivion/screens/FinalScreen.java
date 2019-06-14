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
import com.rufodev.aliennestoblivion.PlayServices;
import com.rufodev.aliennestoblivion.fx.SoundManager;

public class FinalScreen implements Screen{
	MyGame game;
	OrthographicCamera uiCamera;
	BitmapFont font;
	BitmapFont fontSmall;
	Label labelTitle;
	Label labelLoading;
	LabelStyle labelStyle;
	LabelStyle labelStyleSmall;
	TextButtonStyle buttonStyle;
	TextButton buttonExit;

	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private Label labelText;
	private LabelStyle labelStyleMicro;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;

	float scale;


	public FinalScreen(final MyGame game) {
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


		
        //pm.dispose();

		
		
		///STAGE CRAP
		
		
		font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),true);
		labelStyle= new LabelStyle(font, Color.WHITE);
		labelStyleSmall= new LabelStyle(fontSmall, Color.WHITE);
		labelStyleMicro= new LabelStyle(fontMicro, Color.WHITE);
		labelTitle = new Label("You beated the game!", labelStyle);
		
		labelLoading = new Label("Loading...", labelStyleSmall);
		
		labelText = new Label("Credits:\nThanks to my family and friends for believing in me. And thank you, the player, for giving this game a shot. I hope you enjoyed the game!\nStay tuned for updates and be sure to check out the Challenge Mode for multiplayer experience.\nIf you may, please take a moment to rate the app and give us feedback. Your feedback is very important!", labelStyleMicro);
		labelText.setWrap(true);
		labelText.setWidth(820);
		labelText.setHeight(600);
		
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;

		buttonExit = new TextButton ("Back >",buttonStyle);
        buttonExit.setWidth(250);
        buttonExit.setHeight(80);

		buttonExit.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				game.setScreen(new MainScreen(game));
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
		labelTitle.setWidth(labelTitle.getWidth()/this.scale);
		labelTitle.setHeight(labelTitle.getHeight()/this.scale);
        labelTitle.setWidth(labelTitle.getWidth() * scale);
        labelTitle.setHeight(labelTitle.getHeight() * scale);
        labelTitle.setPosition(width / 2 - labelTitle.getWidth() / 2, 50 * scale );
		
        //labelLoading.setPosition(Gdx.graphics.getWidth() / 2 - labelLoading.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
		labelText.setFontScale(scale);
		labelText.setWidth(labelText.getWidth()/this.scale);
		labelText.setHeight(labelText.getHeight()/this.scale);
		labelText.setWidth(labelText.getWidth() * scale);
		labelText.setHeight(labelText.getHeight() * scale);
		labelText.setPosition(Gdx.graphics.getWidth()/2-labelText.getWidth()/2,Gdx.graphics.getHeight()/2-labelText.getHeight()/2);


		buttonExit.setWidth(buttonExit.getWidth()/this.scale);
		buttonExit.setHeight(buttonExit.getHeight()/this.scale);
		buttonExit.setWidth(buttonExit.getWidth() * scale);
		buttonExit.setHeight(buttonExit.getHeight() * scale);
		buttonExit.setX(Gdx.graphics.getWidth() / 2 - buttonExit.getWidth() / 2);
		buttonExit.setY(Gdx.graphics.getHeight() - buttonExit.getHeight()-15*scale);
		buttonExit.getLabel().setFontScale(scale);





		this.scale = scale;
	}

	public void update(float delta){
		if (game.manager.update()){
			
			if (canPlay == false){
				game.stage.addActor(labelText);
				game.stage.addActor(buttonExit);
				canPlay = true;
				//TODO REPLACE THIS MUSIC WITH SOMETHING EPIC
				SoundManager.startMusic(0);

			}
			
			
		}else{
///
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
