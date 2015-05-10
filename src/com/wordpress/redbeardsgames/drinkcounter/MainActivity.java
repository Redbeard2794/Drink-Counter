package com.wordpress.redbeardsgames.drinkcounter;

import java.io.IOException;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;
import android.view.MotionEvent;

public class MainActivity extends BaseGameActivity implements IUpdateHandler{
	
	//width and height of the camera "lens"
	private static final int CAMERA_WIDTH = 600;
	private static final int CAMERA_HEIGHT = 430;
	
	//create a scene
	private Scene mScene;
	
	//button texture
	private ITextureRegion drinkButtonTextureRegion;
	private Sprite drinkButtonSprite;
	
	//background sprite
	private ITextureRegion backgroundTextureRegion;
	private Sprite backgroundSprite;
	
	//the smooth camera
	SmoothCamera mSmoothCamera;
	
	private Font f;
	private Text drinkCountText;
	private String myText = "Drinks: ";
	int numDrinks = 0;
	
	int seconds=0;
	int minutes=0;
	int hours=0;
	int secTimer = 0;
	private Text timeText;
	
	Random rand = new Random();
	Sound drinkDrinkDrink;
	Sound canOpen;
	Sound drinkToVictory;

	@Override
	public EngineOptions onCreateEngineOptions()  
	{
		//params are x, y, width, height, xVelocity, yVelocity, zoom factor
		mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,
				100, 0, 1.0f);
		
		//params are fullScreen, orientation, resolution and the camera we ant to use
		EngineOptions engine = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				mSmoothCamera);
		engine.getAudioOptions().setNeedsSound(true);
		return engine;
		
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		loadGfx();
		
		try
		{
			drinkDrinkDrink = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/DrinkDrinkDrink.ogg");
		}
		catch (IOException e)
		{
			Debug.e("Cant find sound file 'DrinkDrinkDrink.ogg'");
		}
		
		try
		{
			canOpen = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/canOpen.ogg");
		}
		catch (IOException e)
		{
			Debug.e("Cant find sound file 'canOpen.ogg'");
		}
		
		try
		{
			drinkToVictory = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/drinkToVictory.ogg");
		}
		catch (IOException e)
		{
			Debug.e("Cant find sound file 'drinkToVictory.ogg'");
		}
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}
	
	private void loadGfx()
	{	
		f = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC), 41,Color.WHITE.hashCode());
		f.load();
		
		//set the base path for assets
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		//load the background
		BitmapTextureAtlas backgroundTexture = new BitmapTextureAtlas(
				getTextureManager(), 3500, 480);
		backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTexture, this,
						"background.png", 0, 0);
		backgroundTexture.load();
		
		BitmapTextureAtlas drinkButtonTexture = new BitmapTextureAtlas(
				getTextureManager(), 314,75);
		drinkButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(drinkButtonTexture, this,
						"drinkButton66.png",0,0);
		drinkButtonTexture.load();
		

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		this.mScene = new Scene();
		
		pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		
		backgroundSprite = new Sprite(0, 0, backgroundTextureRegion,
				this.mEngine.getVertexBufferObjectManager());
		backgroundSprite.setZIndex(7);
		mScene.attachChild(backgroundSprite);
		

		
		drinkButtonSprite = new Sprite(CAMERA_WIDTH/4,CAMERA_HEIGHT/2,drinkButtonTextureRegion,
				this.mEngine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();
				switch (myEventAction)
				{
					case MotionEvent.ACTION_DOWN:{
						break;}
					case MotionEvent.ACTION_MOVE: {
						break;}
					case MotionEvent.ACTION_UP:{
						numDrinks+=1;
						drinkCountText.setText( myText + numDrinks);
						seconds=0;
						minutes=0;
						hours=0;
						timeText.setText( "Time since last drink: " + hours+":"+minutes+":"+seconds);
						int randomNum = rand.nextInt((3 - 1) + 1) + 1;
						if(randomNum == 1)
							drinkDrinkDrink.play();
						else if(randomNum == 2)
							canOpen.play();
						else if(randomNum == 3)
							drinkToVictory.play();
					break;}
			}
			return true;
			}
			};
		drinkButtonSprite.setZIndex(8);
		mScene.attachChild(drinkButtonSprite);
		mScene.registerTouchArea(drinkButtonSprite);
		
		drinkCountText = new Text(CAMERA_WIDTH/3+20,CAMERA_HEIGHT/3,f,myText,50,new TextOptions(HorizontalAlign.CENTER),this.getVertexBufferObjectManager());
		drinkCountText.setColor(1.0f, 0.0f, 0.0f);
		drinkCountText.setText( myText + numDrinks);
		drinkCountText.setZIndex(14);
		drinkCountText.setColor(0, 1, 1,1);
		mScene.attachChild(drinkCountText);
		
		timeText = new Text(20,20,f,"Time since last drink: ",50,new TextOptions(HorizontalAlign.CENTER),this.getVertexBufferObjectManager());
		timeText.setColor(1.0f, 0.0f, 0.0f);
		timeText.setText( "Time since last drink: " + hours+":"+minutes+":"+seconds);
		timeText.setZIndex(14);
		timeText.setColor(0, 1, 1,1);
		mScene.attachChild(timeText);
		
		//sort the scene's children based on z values
		mScene.sortChildren();
		
		this.mEngine.registerUpdateHandler(this);
		
		//tell the camera to follow the player sprite
		//mSmoothCamera.setChaseEntity(playerSprite);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {

		//playerSprite.setX(playerSprite.getX()+1f);
		if(secTimer < 60)
			secTimer+=1;
		else if(secTimer >= 60)
		{
			seconds+=1;
			secTimer = 0;
		}
		if (seconds >= 60)
		{
			minutes+=1;
			seconds = 0;
		}
		if(minutes >=60)
		{
			hours+=1;
			minutes+=1;
		}
		timeText.setText( "Time since last drink: " + hours+":"+minutes+":"+seconds);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
