package com.rufodev.aliennestoblivion.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.rufodev.aliennestoblivion.data.Collision;
import com.rufodev.aliennestoblivion.data.StageHandler;
import com.rufodev.aliennestoblivion.entities.Player;
import com.rufodev.aliennestoblivion.screens.PlayScreen;

public class MyInput {
	static int hotspotX;
	static int hotspotY;
	private static int sensitivity = 15;
	private static boolean joystickActivated = false;
	private static boolean joystick2Activated = false;
    private static float scale = 0;
	public MyInput(){
	}
	
	//Joystick Y axis is inverted. 0,0 is the top left corner.
	public static void handlePlayerInput(Player player) {
        boolean rotated =false;
        boolean moved = false;
        float delta = Gdx.graphics.getDeltaTime();
        //CHANGING DEFAULT BEHAVIOUR:
        joystickActivated=true;
        joystick2Activated=true;
        /////
        if (Gdx.input.isTouched()) {
            for (int i =0;i<2;i++) {
                if (Gdx.input.isTouched(i)) {
                    float x = Gdx.input.getX(i);
                    float y = Gdx.input.getY(i);
                    //MOVEMENT
                    if (x >= player.joystickOutter.getX() && x<= player.joystickOutter.getWidth()+player.joystickOutter.getX() && y <= player.joystickOutter.getHeight()+player.joystickOutter.getY() && y >= player.joystickOutter.getY()) {
                        moved = true;
                        player.isMoving = true;
                        if (!joystickActivated) {
                            joystickActivated = true;
                            player.joystickInner.setX(x - player.joystickInner.getWidth() / 2);
                            player.joystickInner.setY(y - player.joystickInner.getHeight() / 2);
                            player.joystickOutter.setX(x - player.joystickOutter.getWidth() / 2);
                            player.joystickOutter.setY(y - player.joystickOutter.getHeight() / 2);
                        }
                        player.joystickInner.setX(x - player.joystickInner.getWidth() / 2);
                        player.joystickInner.setY(y - player.joystickInner.getHeight() / 2);


                        if (x >= player.joystickOutter.getX() + player.joystickOutter.getWidth() / 2 + player.joystickOutter.getWidth() / sensitivity) {
                            player.moveRight = true;
                        } else {
                            player.moveRight = false;
                        }
                        if (x <= player.joystickOutter.getX() + player.joystickOutter.getWidth() / 2 - player.joystickOutter.getWidth() / sensitivity) {
                            player.moveLeft = true;
                        } else {
                            player.moveLeft = false;
                        }

                        if (y <= player.joystickOutter.getY() + player.joystickOutter.getHeight() / 2 - player.joystickOutter.getHeight() / sensitivity) {
                            player.moveUp = true;
                        } else {
                            player.moveUp = false;
                        }
                        if (y >= player.joystickOutter.getY() + player.joystickOutter.getHeight() / 2 + player.joystickOutter.getHeight() / sensitivity) {
                            player.moveDown = true;
                        } else {
                            player.moveDown = false;
                        }

                    }
                    ///Rotation joystick:
                    if (x >= player.joystick2Outter.getX() && x<= player.joystick2Outter.getWidth()+player.joystick2Outter.getX() && y <= player.joystick2Outter.getHeight()+player.joystick2Outter.getY() && y >= player.joystick2Outter.getY()) {
                        player.isRotating = true;
                        rotated = true;
                        if (!joystick2Activated) {
                            joystick2Activated = true;
                            player.joystick2Inner.setX(x - player.joystick2Inner.getWidth() / 2);
                            player.joystick2Inner.setY(y - player.joystick2Inner.getHeight() / 2);
                            player.joystick2Outter.setX(x - player.joystick2Outter.getWidth() / 2);
                            player.joystick2Outter.setY(y - player.joystick2Outter.getHeight() / 2);
                        }
                        player.joystick2Inner.setX(x - player.joystick2Inner.getWidth() / 2);
                        player.joystick2Inner.setY(y - player.joystick2Inner.getHeight() / 2);
                        y = Gdx.graphics.getHeight() - y;
                        if (y - (Gdx.graphics.getHeight() - player.joystick2Outter.getY() + player.joystick2Outter.getHeight() / 2) != 0 && x - (player.joystick2Outter.getX() + player.joystick2Outter.getWidth() / 2) != 0) {
                            float angle =  MathUtils.atan2(y - (Gdx.graphics.getHeight() - player.joystick2Outter.getY() - player.joystick2Outter.getHeight() / 2), x - (player.joystick2Outter.getX() + player.joystick2Outter.getWidth() / 2));
                            if (player.canMove){
                                player.rotate(angle);
                            }


                        }
                    }
                    /*
                    ///Bomb UI:

                    if (x>= PlayScreen.bombOpen.getX() && x<= PlayScreen.bombOpen.getWidth() + PlayScreen.bombOpen.getX() && y >= PlayScreen.bombOpen.getY() && y<= PlayScreen.bombOpen.getHeight() +  PlayScreen.bombOpen.getY() ){
                        if (player.bombEnabled && StageHandler.isCombatEnabled){
                            player.spawnBomb = true;
                        }
                    }
                    */
                }
            }

            //////////////--------------------
        }
        if (!moved || !player.canMove){
            player.moveUp = false;
            player.moveDown = false;
            player.moveRight = false;
            player.moveLeft = false;
            player.isMoving = false;
            joystickActivated = false;
            fixLeftJoystick(player);
        }
        if (!rotated || !player.canMove) {
            joystick2Activated = false;
            player.isRotating = false;
            fixRightJoystick(player);
        }
        //CHECK WALL COLLISIONS-----------------------------
        float tempX = player.position.x;
        float tempY = player.position.y;
        boolean hitsX = false;
        boolean hitsY = false;

        if (player.moveLeft) player.position.x -= player.speed * delta;
        if (player.moveRight) player.position.x += player.speed * delta;
        //check X:
        for (int i=0;i< StageHandler.collisions.size();i++){
            if (Collision.checkWalls(StageHandler.collisions.get(i),player)) hitsX = true;
        }
        if (hitsX) player.position.x = tempX;

        if (player.moveUp) player.position.y += player.speed * delta;
        if (player.moveDown) player.position.y -= player.speed * delta;
        //check Y:
        for (int i=0;i< StageHandler.collisions.size();i++){
            if (Collision.checkWalls(StageHandler.collisions.get(i),player)) hitsY = true;
        }
        if (hitsY) player.position.y = tempY;
        ////------------------------------------------------



        
        
    		
	}

    public static void fixJoystick(Player player, float scaleVar) {
        //set back to original size:
        if (scale!=0) {
            player.joystickInner.setSize(player.joystickInner.getWidth() / scale, player.joystickInner.getHeight() / scale);
            player.joystickOutter.setSize(player.joystickOutter.getWidth() / scale, player.joystickOutter.getHeight() / scale);
            player.joystick2Inner.setSize(player.joystick2Inner.getWidth() / scale, player.joystick2Inner.getHeight() / scale);
            player.joystick2Outter.setSize(player.joystick2Outter.getWidth() / scale, player.joystick2Outter.getHeight() / scale);
            /*PlayScreen.bombOpen.setSize(PlayScreen.bombOpen.getWidth() / scale, PlayScreen.bombOpen.getHeight() / scale);
            PlayScreen.bombClosed.setSize(PlayScreen.bombClosed.getWidth() / scale, PlayScreen.bombClosed.getHeight() / scale);*/
        }
        //resize with new scale:
        //moar size just 'cause:
        scaleVar*=1.5;
        scale = scaleVar;
        player.joystickInner.setSize(scale * player.joystickInner.getWidth(), scale * player.joystickInner.getHeight());
        player.joystickOutter.setSize(scale * player.joystickOutter.getWidth(), scale * player.joystickOutter.getHeight());
        player.joystick2Inner.setSize(scale * player.joystick2Inner.getWidth(), scale * player.joystick2Inner.getHeight());
        player.joystick2Outter.setSize(scale * player.joystick2Outter.getWidth(), scale * player.joystick2Outter.getHeight());
        /*PlayScreen.bombOpen.setSize(PlayScreen.bombOpen.getWidth() * scale , PlayScreen.bombOpen.getHeight() * scale );
        PlayScreen.bombClosed.setSize(PlayScreen.bombClosed.getWidth() * scale , PlayScreen.bombClosed.getHeight() * scale );*/

        player.joystickOutter.setPosition(0,Gdx.graphics.getHeight() - player.joystickOutter.getHeight());
        player.joystickInner.setPosition(player.joystickOutter.getX() + player.joystickOutter.getWidth()/2 - player.joystickInner.getWidth()/2,player.joystickOutter.getY() + player.joystickOutter.getHeight()/2 - player.joystickInner.getHeight()/2 );
        player.joystick2Outter.setPosition(Gdx.graphics.getWidth() - player.joystick2Outter.getWidth(),Gdx.graphics.getHeight() - player.joystick2Outter.getHeight());
        player.joystick2Inner.setPosition(player.joystick2Outter.getX() + player.joystick2Outter.getWidth()/2 - player.joystick2Inner.getWidth()/2,player.joystick2Outter.getY() + player.joystick2Outter.getHeight()/2 - player.joystick2Inner.getHeight()/2 );
        /*PlayScreen.bombOpen.setPosition(player.joystick2Outter.getX() + player.joystick2Outter.getWidth() - PlayScreen.bombOpen.getWidth(),player.joystick2Outter.getY() - PlayScreen.bombOpen.getHeight());
        PlayScreen.bombClosed.setPosition(PlayScreen.bombOpen.getX(), PlayScreen.bombOpen.getY());*/

    }

    private static void fixRightJoystick(Player player) {
        player.joystick2Inner.setPosition(player.joystick2Outter.getX() + player.joystick2Outter.getWidth()/2 - player.joystick2Inner.getWidth()/2,player.joystick2Outter.getY() + player.joystick2Outter.getHeight()/2 - player.joystick2Inner.getHeight()/2 );
    }

    private static void fixLeftJoystick(Player player) {
        player.joystickInner.setPosition(player.joystickOutter.getX() + player.joystickOutter.getWidth()/2 - player.joystickInner.getWidth()/2,player.joystickOutter.getY() + player.joystickOutter.getHeight()/2 - player.joystickInner.getHeight()/2 );
    }
}
