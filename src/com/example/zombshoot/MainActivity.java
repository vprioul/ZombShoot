package com.example.zombshoot;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	protected CCGLSurfaceView _glSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		_glSurfaceView = new CCGLSurfaceView(this);
		setContentView(_glSurfaceView);
	}

	@Override
	public void onStart()
	{
	super.onStart();
	CCDirector director = CCDirector.sharedDirector();
	 director.attachInView(_glSurfaceView);
	 director.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft); // set orientation
	 CCDirector.sharedDirector().setDisplayFPS(true);  //display fps
	CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);  //set frame rate
	CCScene scene = GameScene.scene(); //
	CCDirector.sharedDirector().runWithScene(scene);
	}
	
	@Override
	public void onPause()
	{
	super.onPause();
	CCDirector.sharedDirector().pause();
	}
	 
	@Override
	public void onResume()
	{
	super.onResume();
	CCDirector.sharedDirector().resume();
	}
	 
	@Override
	public void onStop()
	{
	super.onStop();
	CCDirector.sharedDirector().end();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
