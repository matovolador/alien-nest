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

public class StoryScreen implements Screen{
	MyGame game;
	OrthographicCamera uiCamera;
	Texture background;
	BitmapFont font;
	BitmapFont fontSmall;
	Label labelText;
	LabelStyle labelStyleText;
	TextButtonStyle buttonStyle;
	TextButton buttonNext;


	Skin skinButton;
	TextureAtlas buttonAtlas;
	Boolean canPlay = false;
	private BitmapFont fontMicro;
	private ScreenViewport uiViewport;

	float scale;
	int defaultShow =-1;

	public StoryScreen(final MyGame game) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;

		scale = 1;
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		initAssets();
		setText();


	}
	public StoryScreen(final MyGame game, int defaultShow) {
		Gdx.input.setCatchBackKey(true);
		this.game = game;
		this.defaultShow = defaultShow;
        /* COMMENTED GOOGLE PLAY GAMES CONNECTION : beacuse its fucking annoying for gameplay testing
		game.playServices.signIn();*/
		scale = 1;
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(true);
		uiViewport = new ScreenViewport(uiCamera);
		uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		initAssets();
		setText();


	}
	
	public void initAssets(){
		
		background = game.manager.get("img/computerScreen.png", Texture.class);
		
		
        //pm.dispose();
		
		
		///STAGE CRAP
		
		
		font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),true);
		fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),true);
		fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),true);
		labelStyleText= new LabelStyle(fontMicro, new Color(Color.rgb888(101,231,23)));
		labelText = new Label("Loading...", labelStyleText);
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
		buttonStyle.fontColor= Color.WHITE;

		buttonNext = new TextButton ("Next >",buttonStyle);
		buttonNext.setWidth(250);
		buttonNext.setHeight(80);
		
		buttonNext.addListener(new InputListener(){

			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {

				return true;
			}


			@Override
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				SoundManager.playClick();
				if (defaultShow==0){
					game.setScreen(new Campaign(game));
					dispose();
				}else {
					if (ConfigObject.gameStage == 6) {
						game.setScreen(new FinalScreen(game));
						dispose();
					} else {
						if (ConfigObject.gameStage != 0) {
							game.setScreen(new PlayScreen(game));
							dispose();
						} else {
							game.setScreen(new Campaign(game));
							dispose();
						}

					}
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

		labelText.setFontScale(scale);
		labelText.setWidth(labelText.getWidth()/this.scale);
		labelText.setHeight(labelText.getHeight()/this.scale);
		labelText.setWidth(labelText.getWidth() * scale);
		labelText.setHeight(labelText.getHeight() * scale);
		labelText.setPosition(Gdx.graphics.getWidth()/2-labelText.getWidth()/2,Gdx.graphics.getHeight()/2-labelText.getHeight()/2);
		
		buttonNext.setWidth(buttonNext.getWidth()/this.scale);
		buttonNext.setHeight(buttonNext.getHeight()/this.scale);
		buttonNext.setWidth(buttonNext.getWidth() * scale);
		buttonNext.setHeight(buttonNext.getHeight() * scale);
		buttonNext.setX(Gdx.graphics.getWidth() - buttonNext.getWidth() );
		buttonNext.setY(Gdx.graphics.getHeight() - buttonNext.getHeight());
		buttonNext.getLabel().setFontScale(scale);

		this.scale= scale;
	}

	public void update(float delta){
		if (game.manager.update()){
			
			if (canPlay == false){
				canPlay = true;
				game.stage.addActor(labelText);
				game.stage.addActor(buttonNext);

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
		/*
		MyGame.batch.begin();
		MyGame.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		MyGame.batch.end();
		*/
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
		game.stage.addActor(labelText);
		Gdx.input.setInputProcessor(game.stage);

	}


	public void setText(){
		String text ="";
		if (defaultShow!=0) {
			switch (ConfigObject.gameStage) {
				case 0:
					text = "INTRO:\n" +
							"A few years ago the Federation sent some of its army into the Sector B47 in a mission to secure the planets within for mineral extraction. One of the Soldiers was Anna, an old friend of yours from childhood. The mission was a failure, the entire unit was overrun by aliens and was unable to complete the task, ending in death of all the soldiers assigned to it. At least that’s what the Federation assumed.\n" +
							"Recently, your Engineer friend Kaplosk informed you that he received a coded transmission from the sector B47 using the Federation’s encryption. When you hear this message you can translate the following: 'Frank, help me! It’s Anna!' Immediately after you hear this message you request a Patrol ship from the Federation to undergo on a search mission for survivors on said sector. The Federation declined but you managed to steal one of the ships on deck and set course to Sector B47.\n" +
							"This is how it all starts…";
					break;
				case 1:
					text = "MISSION 1: The Welcome Committee. \n" +
							"You reach the sector, landing on Planet Erend, packing everything you need from the ship to undergo on your rescue mission to save Anna, the first step is to discover where she’s being held captive or where she might be hiding. The Federation’s original mission files point out that this race of aliens tend to follow commands from a massive mind located in their breeding nests which are highly protected by their forces. If the aliens where to consciously keep Anna captive, that’s where she should be. Another theory thought, is that Anna is not captive but hidden somewhere in one of the sector’s planets, in which case she might have left clues about her location." +
							"As you wonder around the planet for clues, a stampede of aliens attack you from the East…\n";
					break;
				case 2:
					text = "MISSION 2: The First Lead.\n" +
							"After defeating that wave of hostiles, you find one of the bodies to have a beeping signal in its brain. Using your suit’s radar, you realize the original signal comes from Planet Argant, where the Federation’s mission was assigned to land.\n" +
							"After reaching the planet you realize it’s full of minerals and several Federation camps, although no signs of living soldiers. You proceed to search inside one of the camp’s bases, and discover a device used for radio transmissions. Even if broken, you can hear Anna’s encrypted message through it. Using the base’s computers you detect her signal coming from planet Thoryad. All of the sudden you hear alien screams closing in to your location. You load your weapon and head to the main entrance… ";
					break;
				case 3:
					text = "MISSION 3: Hostile Space.\n" +
							"In the Federation base’s computer you find the information needed to travel to Thoryad. As you set course to the planet your radar alerts you from incoming ships going your direction. These ships are known by the database as the Red Star forces, a group of pirates that are currently in war with the Federation. The battle is inevitable…";
					break;
				case 4:
					text = "MISSION 4: Spoils of War\n" +
							"The landing wasn’t easy. The ship took too much heat from the battle. The radar reports signals from the Federation Army’s equipment in the area. After some time examining the damage done to the dead soldiers’ suits, you recover some important armor pieces. Reaching back to the ship you manage to assemble some new parts for your suit and weapon. 'Fair enough' you think to yourself. Time to scout the area for new leads. You set course by feet to a high zone of the planet to perform some radio monitoring, see if you can pick the distress signal again. Up in the dark mountain, you hear some radio static in your suit belonging to Anna’s message. Even though the message is not clear, the signal level is higher than ever. You know she’s here somewhere. But it doesn’t take the aliens long to detect you in such high altitude…";
					break;
				case 5:
					text = "MISSION 5: Close Encounters\n" +
							"You finally pinpoint the location of Anna. She’s sending the transmissions from a cave not far from your position. After getting there, she speaks to you with a distorted voice: 'At last, soldier. You bring me future. You bring me food. Behold now, soldier, for I have been transformed. I am evolved. Now with your ship’s technology our breed will have access to your computers. Behold, soldier, for you have brought war upon your kind.' Suddenly she emerges from the cave in the form of an alien creature, determined to kill you…";
					break;
				case 6:
					text = "ENDING:\n" +
							"When you finally come back to the Federation’s base, you are welcomed by your officers for ending a very possible threat to our race. You see Kaplosk behind everybody and you reach over to him with a hug. 'You saved us. You couldn’t save Anna, but you saved the rest of us.'- he says while you take off your helmet and reach over to the computer, accessing the report for Sector B47’s first mission. There you enter 'No soldiers left alive - Confirmed'.";
					break;
				case 999:
					text = "TRAINING ROOM:\n" +
							"Welcome to the Federation’s Military training room. Master your skills in weaponry and assault while keeping your suit away from harm. Your results will be saved in the Federation Database to compete against other soldiers from the base. Please step inside…";
					break;
				default:
					text = "NO TEXT SET FOR THIS STAGE ID";
					break;

			}
		}else{
			text = "INTRO:\n" +
					"A few years ago the Federation sent some of its army into the Sector B47 in a mission to secure the planets within for mineral extraction. One of the Soldiers was Anna, an old friend of yours from childhood. The mission was a failure, the entire unit was overrun by aliens and was unable to complete the task, ending in death of all the soldiers assigned to it. At least that’s what the Federation assumed.\n" +
					"Recently, your Engineer friend Kaplosk informed you that he received a coded transmission from the sector B47 using the Federation’s encryption. When you hear this message you can translate the following: “Frank, help me! It’s Anna!” Immediately after you hear this message you request a Patrol ship from the Federation to undergo on a search mission for survivors on said sector. The Federation declined but you managed to steal one of the ships on deck and set course to Sector B47.\n" +
					"This is how it all starts…";
		}
		labelText.setText(text);
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
