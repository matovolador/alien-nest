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

public class ScoreScreen implements Screen{
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
	TextButton buttonMain;
	TextButton buttonSubmit;
	int score;
	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private Label labelInfo;
	private LabelStyle labelStyleMicro;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;

	float scale;
	boolean flagRunAd = true;


	public ScoreScreen(final MyGame game,int score) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		this.score= score;
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scale = 1;
		initAssets();


	}
	
	public void initAssets(){
		
		initScreen = game.startUpManager.get("img/initScreen.jpg", Texture.class);
		game.playServices.signIn();
		game.playServices.submitScore(score);

		///STAGE CRAP
		
		
		font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),true);
		labelStyle= new LabelStyle(font, Color.WHITE);
		labelStyleSmall= new LabelStyle(fontSmall, Color.WHITE);
		labelStyleMicro= new LabelStyle(fontMicro, Color.WHITE);
		labelTitle = new Label("Game Over\nScore: "+this.score, labelStyle);
		
		labelLoading = new Label("Loading...", labelStyleSmall);
		
		labelInfo = new Label("To exit game press Escape and close window.", labelStyleMicro);
		
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("buttons/button.pack");
		skinButton.addRegions(buttonAtlas);
		buttonStyle= new TextButtonStyle();
		buttonStyle.up = skinButton.getDrawable("button");
		buttonStyle.down = skinButton.getDrawable("button-pressed");
		buttonStyle.font = fontSmall;
		
		buttonMain = new TextButton ("Main Menu",buttonStyle);
		buttonMain.setWidth(250);
		buttonMain.setHeight(80);
		
		buttonSubmit = new TextButton ("Scores",buttonStyle);
		buttonSubmit.setWidth(250);
		buttonSubmit.setHeight(80);

		buttonMain.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				ConfigObject.gameStage = 0;
				SoundManager.playClick();
				game.setScreen(new MainScreen(game));
				dispose();
			}
		});

		buttonSubmit.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
                game.playServices.signIn();
				game.playServices.showScore();

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
		
        //labelLoading.setPosition(Gdx.graphics.getWidth()/2 - labelLoading.getWidth()/2, Gdx.graphics.getHeight()/2 + 100);
		labelInfo.setFontScale(scale);
		labelInfo.setWidth(labelInfo.getWidth()/this.scale);
		labelInfo.setHeight(labelInfo.getHeight()/this.scale);
		labelInfo.setWidth(labelInfo.getWidth() * scale);
		labelInfo.setHeight(labelInfo.getHeight() * scale);
		labelInfo.setPosition(Gdx.graphics.getWidth()/2 - labelInfo.getWidth()/2, Gdx.graphics.getHeight()/2 + 100);

		buttonMain.setWidth(buttonMain.getWidth()/this.scale);
		buttonMain.setHeight(buttonMain.getHeight()/this.scale);
		buttonMain.setWidth(buttonMain.getWidth() * scale);
		buttonMain.setHeight(buttonMain.getHeight() * scale);
		buttonMain.setX(Gdx.graphics.getWidth() / 2 - buttonMain.getWidth() / 2);
		buttonMain.setY(Gdx.graphics.getHeight() / 2);
		buttonMain.getLabel().setFontScale(scale);

		buttonSubmit.setWidth(buttonSubmit.getWidth()/this.scale);
		buttonSubmit.setHeight(buttonSubmit.getHeight()/this.scale);
		buttonSubmit.setWidth(buttonSubmit.getWidth() * scale);
		buttonSubmit.setHeight(buttonSubmit.getHeight() * scale);
		buttonSubmit.setX(Gdx.graphics.getWidth() / 2 - buttonSubmit.getWidth() / 2);
		buttonSubmit.setY(Gdx.graphics.getHeight() / 2 + buttonSubmit.getHeight() + buttonSubmit.getHeight() / 3);
		buttonSubmit.getLabel().setFontScale(scale);

		this.scale = scale;
	}

	public void update(float delta){
        if (canPlay == false){

            game.stage.addActor(buttonMain);
            game.stage.addActor(buttonSubmit);
            canPlay = true;
            labelLoading.remove();

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
		
		
		Gdx.gl.glClearColor(0, 0, 0 , 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		uiCamera.update();

		MyGame.batch.setProjectionMatrix(uiCamera.combined);
		game.stage.getViewport().apply();
		MyGame.batch.begin();
		MyGame.batch.draw(initScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		MyGame.batch.end();
		game.stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		uiViewport.update(width,height);
		uiCamera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
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
