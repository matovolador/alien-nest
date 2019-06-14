package com.rufodev.aliennestoblivion.android;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.example.games.basegameutils.*;
import com.google.android.gms.games.Games;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rufodev.aliennestoblivion.MyGame;
import com.rufodev.aliennestoblivion.PlayServices;
import com.rufodev.aliennestoblivion.MyGame.GameState;
import com.rufodev.aliennestoblivion.MyGame.GameStateChangeListener;

public class AndroidLauncher extends AndroidApplication implements PlayServices, GameStateChangeListener {
	private GameHelper gameHelper;
	private final static int requestCode = 1;
    private static final String INTERSTITIAL_AD_UNIT_ID = "<PUT-YOUR-AD-UNIT-ID>";
    InterstitialAd interstitialAd;
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		///GPS
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);
		gameHelper.setMaxAutoSignInAttempts(0);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed() {
            }

            @Override
			public void onSignInSucceeded(){ }
		};
		gameHelper.setup(gameHelperListener);

        //AD
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);

        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest ad = builder.build();

        interstitialAd.loadAd(ad);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		MyGame myGame = new MyGame(this);
		myGame.setGameStateChangeListener(this);
		initialize(myGame, config);

	}

	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame()
	{
		String str = "https://play.google.com/store/apps/details?id=com.rufodev.aliennestoblivion.android"; // this no longer exists.
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void unlockAchievement()
	{
		//Games.Achievements.unlock(gameHelper.getApiClient(),getString(R.string.achievement_dum_dum));
	}

	@Override
	public void submitScore(int highScore)
	{
		if (isSignedIn() == true)
		{
			Games.Leaderboards.submitScore(gameHelper.getApiClient(),
					getString(R.string.leaderboard_leaderboard), highScore);
		}
	}

	@Override
	public void showAchievement()
	{
		if (isSignedIn() == true)
		{
			//startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient(),getString(R.string.achievement_dum_dum)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public void showScore()
	{
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					getString(R.string.leaderboard_leaderboard)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public boolean isSignedIn()
	{
		return gameHelper.isSignedIn();
	}


	@Override
	public void onGameStateChanged(GameState newState) {
		if(newState == GameState.GameOver){
            showInterstitialAd(new Runnable(){

                @Override
                public void run() {
                    //NOTHING GOES HERE.
                }
            });
		}
	}
    public void showInterstitialAd(final Runnable then) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (then != null) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Gdx.app.postRunnable(then);
                            AdRequest.Builder builder = new AdRequest.Builder();
                            AdRequest ad = builder.build();
                            interstitialAd.loadAd(ad);
                        }
                    });
                }
                interstitialAd.show();
            }
        });
    }
}
