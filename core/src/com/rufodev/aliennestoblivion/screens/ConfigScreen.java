package com.rufodev.aliennestoblivion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.data.ConfigObject;
import com.rufodev.aliennestoblivion.fx.SoundManager;

public class ConfigScreen implements Screen {
    BitmapFont font;
    BitmapFont fontSmall;
    Label labelTitle;
    LabelStyle labelStyle;
    LabelStyle labelStyleSmall;
    TextButtonStyle buttonStyle;
    Skin skinButton;
    TextureAtlas buttonAtlas;
    Boolean canPlay = false;
    LabelStyle labelStyleMicro;
    BitmapFont fontMicro;
    ScreenViewport uiViewport;
    OrthographicCamera uiCamera;
    MyGame game;
    float scale;
    Texture back;
    TextButton buttonOk;
    TextureAtlas arrowAtlas;
    Skin skinArrows;
    ButtonStyle arrowLeftStyle;
    ButtonStyle arrowRightStyle;
    Button arrowArmorLeft;
    Button arrowArmorRight;
    Button arrowPowerLeft;
    Button arrowPowerRight;
    Button arrowChargesLeft;
    Button arrowChargesRight;
    Button arrowFxLeft;
    Button arrowFxRight;
    Button arrowMusicLeft;
    Button arrowMusicRight;
    Label labelPoints;
    Label labelTitleArmor;
    Label labelTitleCharges;
    Label labelTitlePower;
    Label labelTitleFx;
    Label labelTitleMusic;
    Label labelArmor;
    Label labelCharges;
    Label labelPower;
    Label labelFx;
    Label labelMusic;
    Table table;

    int volFx=1;
    int volMusic=1;

    public ConfigScreen(MyGame game){
        Gdx.input.setCatchBackKey(true);
        this.game = game;
        this.scale = 1;
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false);
        uiViewport = new ScreenViewport(uiCamera);
        uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        initAssets();


    }

    private void initAssets() {
        back = game.manager.get("img/config-background.jpg", Texture.class);

        MyGame.mGameStateChangeListener.onGameStateChanged(MyGame.GameState.Running);

        font = new BitmapFont(Gdx.files.internal("data/gamefont.fnt"),false);
        fontSmall = new BitmapFont(Gdx.files.internal("data/gamefont-small.fnt"),false);
        fontMicro = new BitmapFont(Gdx.files.internal("data/lilCrap.fnt"),false);
        labelStyle= new Label.LabelStyle(font, Color.WHITE);
        labelStyleSmall= new Label.LabelStyle(fontSmall, Color.WHITE);
        labelStyleMicro= new Label.LabelStyle(fontMicro, Color.WHITE);
        labelTitle = new Label("SETTINGS", labelStyle);
        labelPoints = new Label("Upgrade Points: " + ConfigObject.points, labelStyleMicro);
        labelTitleArmor = new Label("Armor Increment",labelStyleMicro);
        labelTitleCharges = new Label("Weapon Charges",labelStyleMicro);
        labelTitlePower = new Label("Bullet Power",labelStyleMicro);
        labelTitleFx = new Label("Sound Volume",labelStyleMicro);
        labelTitleMusic = new Label("Music Volume",labelStyleMicro);
        labelArmor = new Label("0",labelStyleMicro);
        labelCharges = new Label("0",labelStyleMicro);
        labelPower = new Label("0",labelStyleMicro);
        labelFx = new Label("0",labelStyleMicro);
        labelMusic = new Label("0",labelStyleMicro);

        volFx =(int) (ConfigObject.volFx*10);
        volMusic =(int) (ConfigObject.volMusic*10);

        skinButton = new Skin();
        buttonAtlas = new TextureAtlas("buttons/button.pack");
        skinButton.addRegions(buttonAtlas);
        buttonStyle= new TextButtonStyle();
        buttonStyle.up = skinButton.getDrawable("button");
        buttonStyle.down = skinButton.getDrawable("button-pressed");
        buttonStyle.font = fontSmall;
        skinArrows = new Skin();
        arrowAtlas = new TextureAtlas("buttons/arrows.pack");
        skinArrows.addRegions(arrowAtlas);
        arrowRightStyle = new ButtonStyle();
        arrowRightStyle.up = skinArrows.getDrawable("arrow1");
        arrowRightStyle.down = skinArrows.getDrawable("arrow2");
        arrowLeftStyle = new ButtonStyle();
        arrowLeftStyle.up = skinArrows.getDrawable("arrow3");
        arrowLeftStyle.down = skinArrows.getDrawable("arrow4");

        arrowArmorLeft = new Button(arrowLeftStyle);
        arrowArmorRight = new Button(arrowRightStyle);
        arrowPowerLeft = new Button(arrowLeftStyle);
        arrowPowerRight = new Button(arrowRightStyle);
        arrowChargesLeft = new Button(arrowLeftStyle);
        arrowChargesRight = new Button(arrowRightStyle);
        arrowFxLeft = new Button(arrowLeftStyle);
        arrowFxRight = new Button(arrowRightStyle);
        arrowMusicLeft = new Button(arrowLeftStyle);
        arrowMusicRight = new Button(arrowRightStyle);
        buttonOk = new TextButton("OK >",buttonStyle);
        buttonOk.setWidth(250);
        buttonOk.setHeight(80);

        ////-----TABLE DESIGN:
        table = new Table();
        table.setFillParent(true);
       // table.setDebug(true);

        table.row().expand();
            table.add(labelTitle).colspan(9).center();
        table.row().expand();
            table.add(labelTitleArmor).colspan(3);
            table.add(labelTitleCharges).colspan(3);
            table.add(labelTitlePower).colspan(3);
        table.row().expand();
            table.add(arrowArmorLeft);
            table.add(labelArmor);
            table.add(arrowArmorRight);
            table.add(arrowChargesLeft);
            table.add(labelCharges);
            table.add(arrowChargesRight);
            table.add(arrowPowerLeft);
            table.add(labelPower);
            table.add(arrowPowerRight);
        table.row().expand();
            table.add(labelTitleMusic).colspan(3);
            table.add(labelTitleFx).colspan(3);
        table.row().expand();
            table.add(arrowMusicLeft);
            table.add(labelMusic);
            table.add(arrowMusicRight);
            table.add(arrowFxLeft);
            table.add(labelFx);
            table.add(arrowFxRight);
            table.add(labelPoints).colspan(3);
        table.row().expand();
            table.add(buttonOk).colspan(9).right().padRight(260);
///////////////////-------------------------------

        //Inputs
        buttonOk.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                if (ConfigObject.points!=0){
                    //TODO set message: "You still have upgrade points to spend."
                }else{
                    SoundManager.playClick();
                    game.setScreen(new StoryScreen(game));
                    dispose();
                }
            }
        });

        arrowArmorLeft.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (ConfigObject.armorIncrement<=0 ){
                    //do nothing
                }else{
                    ConfigObject.points+=1;
                    ConfigObject.armorIncrement-=1;
                }
            }
        });
        arrowArmorRight.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (ConfigObject.points<=0 ){
                    //do nothing
                }else{
                    ConfigObject.points-=1;
                    ConfigObject.armorIncrement+=1;
                }
            }
        });

        arrowPowerLeft.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (ConfigObject.powerIncrement<=0 ){
                    //do nothing
                }else{
                    ConfigObject.points+=1;
                    ConfigObject.powerIncrement-=1;
                }
            }
        });
        arrowPowerRight.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (ConfigObject.points<=0 ){
                    //do nothing
                }else{
                    ConfigObject.points-=1;
                    ConfigObject.powerIncrement+=1;
                }
            }
        });

        arrowChargesLeft.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (ConfigObject.chargeIncrement<=0 ){
                    //do nothing
                }else{
                    ConfigObject.points+=1;
                    ConfigObject.chargeIncrement-=1;
                }
            }
        });
        arrowChargesRight.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (ConfigObject.points<=0 ){
                    //do nothing
                }else{
                    ConfigObject.points-=1;
                    ConfigObject.chargeIncrement+=1;
                }
            }
        });

        arrowMusicLeft.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (volMusic<=0 ){
                    //do nothing
                }else{
                    volMusic-=1;
                    ConfigObject.volMusic=(float)volMusic/10;
                    SoundManager.changeMusicSound();
                }

            }
        });
        arrowMusicRight.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (volMusic>=10 ){
                    //do nothing
                }else{
                    volMusic+=1;
                    ConfigObject.volMusic=(float)volMusic/10;
                    SoundManager.changeMusicSound();
                }
            }
        });

        arrowFxLeft.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (volFx<=0 ){
                    //do nothing
                }else{
                    volFx-=1;
                    ConfigObject.volFx=(float)volFx/10;
                }
            }
        });
        arrowFxRight.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                SoundManager.playClick();
                if (volFx>=10 ){
                    //do nothing
                }else{
                    volFx+=1;
                    ConfigObject.volFx=(float)volFx/10;
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
        labelTitle.setWidth(labelTitle.getWidth()/this.scale);
        labelTitle.setHeight(labelTitle.getHeight()/this.scale);
        labelTitle.setWidth(labelTitle.getWidth() * scale);
        labelTitle.setHeight(labelTitle.getHeight() * scale);
        labelPoints.setFontScale(scale);
        labelPoints.setWidth(labelPoints.getWidth()/this.scale);
        labelPoints.setHeight(labelPoints.getHeight()/this.scale);
        labelPoints.setWidth(labelPoints.getWidth() * scale);
        labelPoints.setHeight(labelPoints.getHeight() * scale);
        labelTitleArmor.setFontScale(scale);
        labelTitleArmor.setWidth(labelTitleArmor.getWidth()/this.scale);
        labelTitleArmor.setHeight(labelTitleArmor.getHeight()/this.scale);
        labelTitleArmor.setWidth(labelTitleArmor.getWidth() * scale);
        labelTitleArmor.setHeight(labelTitleArmor.getHeight() * scale);
        labelTitleCharges.setFontScale(scale);
        labelTitleCharges.setWidth(labelTitleCharges.getWidth()/this.scale);
        labelTitleCharges.setHeight(labelTitleCharges.getHeight()/this.scale);
        labelTitleCharges.setWidth(labelTitleCharges.getWidth() * scale);
        labelTitleCharges.setHeight(labelTitleCharges.getHeight() * scale);
        labelTitlePower.setFontScale(scale);
        labelTitlePower.setWidth(labelTitlePower.getWidth()/this.scale);
        labelTitlePower.setHeight(labelTitlePower.getHeight()/this.scale);
        labelTitlePower.setWidth(labelTitlePower.getWidth() * scale);
        labelTitlePower.setHeight(labelTitlePower.getHeight() * scale);
        labelArmor.setFontScale(scale);
        labelArmor.setWidth(labelArmor.getWidth()/this.scale);
        labelArmor.setHeight(labelArmor.getHeight()/this.scale);
        labelArmor.setWidth(labelArmor.getWidth() * scale);
        labelArmor.setHeight(labelArmor.getHeight() * scale);
        labelPower.setFontScale(scale);
        labelPower.setWidth(labelPower.getWidth()/this.scale);
        labelPower.setHeight(labelPower.getHeight()/this.scale);
        labelPower.setWidth(labelPower.getWidth() * scale);
        labelPower.setHeight(labelPower.getHeight() * scale);
        labelCharges.setFontScale(scale);
        labelCharges.setWidth(labelCharges.getWidth()/this.scale);
        labelCharges.setHeight(labelCharges.getHeight()/this.scale);
        labelCharges.setWidth(labelCharges.getWidth() * scale);
        labelCharges.setHeight(labelCharges.getHeight() * scale);
        labelTitleMusic.setFontScale(scale);
        labelTitleMusic.setWidth(labelTitleMusic.getWidth()/this.scale);
        labelTitleMusic.setHeight(labelTitleMusic.getHeight()/this.scale);
        labelTitleMusic.setWidth(labelTitleMusic.getWidth() * scale);
        labelTitleMusic.setHeight(labelTitleMusic.getHeight() * scale);
        labelTitleFx.setFontScale(scale);
        labelTitleFx.setWidth(labelTitleFx.getWidth()/this.scale);
        labelTitleFx.setHeight(labelTitleFx.getHeight()/this.scale);
        labelTitleFx.setWidth(labelTitleFx.getWidth() * scale);
        labelTitleFx.setHeight(labelTitleFx.getHeight() * scale);
        labelMusic.setFontScale(scale);
        labelMusic.setWidth(labelMusic.getWidth()/this.scale);
        labelMusic.setHeight(labelMusic.getHeight()/this.scale);
        labelMusic.setWidth(labelMusic.getWidth() * scale);
        labelMusic.setHeight(labelMusic.getHeight() * scale);
        labelFx.setFontScale(scale);
        labelFx.setWidth(labelFx.getWidth()/this.scale);
        labelFx.setHeight(labelFx.getHeight()/this.scale);
        labelFx.setWidth(labelFx.getWidth() * scale);
        labelFx.setHeight(labelFx.getHeight() * scale);
        buttonOk.setWidth(buttonOk.getWidth()/this.scale);
        buttonOk.setHeight(buttonOk.getHeight()/this.scale);
        buttonOk.setWidth(buttonOk.getWidth() * scale);
        buttonOk.setHeight(buttonOk.getHeight() * scale);
        buttonOk.getLabel().setFontScale(scale);


        this.scale = scale;

    }

    @Override
    public void show() {
        game.stage = new Stage(uiViewport);

        game.stage.addActor(table);

        Gdx.input.setInputProcessor(game.stage);

    }

    public void input(float delta){
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            game.setScreen(new MainScreen(game));
        }
    }

    public void update(float delta){

        labelFx.setText(""+volFx);
        labelMusic.setText(""+volMusic);
        labelArmor.setText(""+ConfigObject.armorIncrement);
        labelPower.setText(""+ConfigObject.powerIncrement);
        labelCharges.setText(""+ConfigObject.chargeIncrement);
        labelPoints.setText("Upgrade Points: " + ConfigObject.points);



        game.stage.act();
    }


    @Override
    public void render(float delta) {
        input(delta);
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiCamera.update();

        MyGame.batch.setProjectionMatrix(uiCamera.combined);
        uiViewport.apply();
        MyGame.batch.begin();
        MyGame.batch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        MyGame.batch.end();
        game.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width,height,true);
        this.setUI(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
