package com.example.zombshoot;
import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.utils.CCFormatter;

import android.content.Context;
import android.view.MotionEvent;


public class GameScene extends CCLayer { 

	private static final int SCORE_LABEL_TAG = 0;
	private static CGSize screenSize;
	
	protected ArrayList<CCSprite> _targets;
	protected ArrayList<CCSprite> _projectiles;
	
	int score;
	
	protected int _projectilesDestroyed;
	
	public GameScene() {
		super();
    	this.setIsTouchEnabled(true);
    	
    	_targets = new ArrayList<CCSprite>();
    	_projectiles = new ArrayList<CCSprite>();
    	_projectilesDestroyed = 0;
    	
    	screenSize = CCDirector.sharedDirector().winSize();
    	CCSprite background = CCSprite.sprite("street.png");
    	background.setScale(screenSize.width / background.getContentSize().width);
    	background.setAnchorPoint(CGPoint.ccp(0f,1f));
    	background.setPosition(CGPoint.ccp(0, screenSize.height));
    	addChild(background,-5);
    	
    	// Add score Label 
    	CCBitmapFontAtlas scoreLabel = CCBitmapFontAtlas.bitmapFontAtlas ("Score : 000", "bionic.fnt");
    	scoreLabel.setScale(0.8f);
    	scoreLabel.setAnchorPoint(1f,0f);
    	scoreLabel.setColor(ccColor3B.ccc3(50, 205, 50));
    	scoreLabel.setPosition( CGPoint.ccp( 250, screenSize.height - 100));
    	addChild(scoreLabel,-2,SCORE_LABEL_TAG);
    	
    	
    	
    	Context context = CCDirector.sharedDirector().getActivity();
    	SoundEngine.sharedEngine().preloadEffect(context, R.raw.pew_pew_lei);
    	SoundEngine.sharedEngine().playSound(context, R.raw.background_music_aac, true);
    	
    	/*CCSprite zombie = CCSprite.sprite("zombieHeadShot.atlas/zombie-HeadShot-1.png");
    	zombie.setScale(3);
    	zombie.setPosition( CGPoint.ccp( screenSize.width - 100, screenSize.height - 100));
    	addChild(zombie);
    	CCAnimation animation = CCAnimation.animation("walk", 0.15f);
        for (int i = 1; i < 8; i++) {
        	animation.addFrame(CCFormatter.format("zombieHeadShot.atlas/zombie-HeadShot-%d.png", i));
            CCIntervalAction animAction = CCAnimate.action(0.2f, animation, false);
            zombie.runAction(animAction);
        }

        CCIntervalAction action = CCAnimate.action(animation);
        zombie.runAction(CCRepeat.action(action, 1));
    	
    	
    	CCMoveBy act1 = CCMoveBy.action(3, (CGPoint.ccp(-screenSize.width, 0)));
        zombie.runAction(act1);*/
        
        /*CCBitmapFontAtlas pauseLabel = CCBitmapFontAtlas.bitmapFontAtlas ("II", "bionic.fnt");
    	
        
        CCMenuItemLabel pauseItem = CCMenuItemLabel.item(pauseLabel , this, "pauseCallback");
        
        CCMenu menu = CCMenu.menu() ;
		menu.addChild(pauseItem);
		menu.alignItemsHorizontally(20);
		final CGPoint p = menu.getPositionRef();
		menu.setPosition(CGPoint.ccpSub(p, CGPoint.ccp(-420,screenSize.height + 100))) ;
		addChild(menu);*/
        
    	this.schedule("gameLogic", 1.0f);
    	this.schedule("update");
	}

	protected void addTarget()
	{
	    Random rand = new Random();
	    CCSprite target = CCSprite.sprite("zombie-1.png");
	    target.setScale(3);
	    // Determine where to spawn the target along the Y axis
	    int minY = (int)(target.getContentSize().height / 2.0f);
	    int maxY = (int)(screenSize.height - target.getContentSize().height / 2.0f);
	    int rangeY = maxY - minY;
	    int actualY = rand.nextInt(rangeY) + minY;
	 
	    // Create the target slightly off-screen along the right edge,
	    // and along a random position along the Y axis as calculated above
	    
	    target.setPosition(screenSize.width + (target.getContentSize().width / 2.0f), actualY);

    	addChild(target);
	    
	    target.setTag(1);
	    _targets.add(target);

	    CCAnimation animation = CCAnimation.animation("walk", 0.1f);
        for (int i = 1; i <= 8; i++) {
        	animation.addFrame(CCFormatter.format("zombie-%d.png", i));
            CCIntervalAction animAction = CCAnimate.action(0.2f, animation, false);
            target.runAction(animAction);
        }

        CCIntervalAction action = CCAnimate.action(animation);
        target.runAction(CCRepeat.action(action, 5));
    	
	    // Determine speed of the target
	    int minDuration = 2;
	    int maxDuration = 4;
	    int rangeDuration = maxDuration - minDuration;
	    int actualDuration = rand.nextInt(rangeDuration) + minDuration;
	    
	    // Create the actions
	    CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(-target.getContentSize().width / 2.0f, actualY));
	    CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
	    CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);

	    target.runAction(actions);

	    
	}
	
	public void spriteMoveFinished(Object sender)
	{
	    CCBitmapFontAtlas scorelabel = (CCBitmapFontAtlas) getChildByTag(SCORE_LABEL_TAG);
	    CCSprite sprite = (CCSprite)sender;
	    this.removeChild(sprite, true);
	    if (sprite.getTag() == 1)
	    {
	    	SoundEngine.sharedEngine().pauseSound();
	        _targets.remove(sprite);
	        _projectilesDestroyed = 0;
	        CCDirector.sharedDirector().replaceScene(GameOverLayer.scene("Perdu !! Ton score est de "+score));
	    }
	    else if (sprite.getTag() == 2){
	        _projectiles.remove(sprite);
	    /*CCSprite zombie = CCSprite.sprite("zombieHeadShot.atlas/zombie-HeadShot-8.png");
    	zombie.setScale(3);
    	zombie.setPosition(10,10);
    	addChild(zombie);
    	CCAnimation animation = CCAnimation.animation("walk", 0.15f);
        for (int i = 1; i <= 8; i++) {
        	animation.addFrame(CCFormatter.format("zombieHeadShot.atlas/zombie-HeadShot-%d.png", i));
            CCIntervalAction animAction = CCAnimate.action(0.2f, animation, false);
            zombie.runAction(animAction);
        }

        CCIntervalAction action = CCAnimate.action(animation);
        zombie.runAction(CCRepeat.action(action, 1));*/

        //removeChild(zombie,true);
	    }
	}
	
	public void gameLogic(float dt)
	{
	    addTarget();
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
	    // Choose one of the touches to work with
	    CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
	 
	    // Set up initial location of projectile
	    CCSprite projectile = CCSprite.sprite("shot.png");
	    projectile.setScale(2);
	    CCBitmapFontAtlas scorelabel = (CCBitmapFontAtlas) getChildByTag(SCORE_LABEL_TAG);
    	score--;
    	scorelabel.setString("Score : " + CCFormatter.format("%03d", score));
	    
	    Context context = CCDirector.sharedDirector().getActivity();
	    SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
	    
	    projectile.setPosition(20, screenSize.height / 2.0f);
	 
	    // Determine offset of location to projectile
	    int offX = (int)(location.x - projectile.getPosition().x);
	    int offY = (int)(location.y - projectile.getPosition().y);
	 
	    // Bail out if we are shooting down or backwards
	    if (offX <= 0)
	        return true;
	 
	    // Ok to add now - we've double checked position
	    addChild(projectile);
	    
	    projectile.setTag(2);
	    _projectiles.add(projectile);
	    
	    // Determine where we wish to shoot the projectile to
	    int realX = (int)(screenSize.width + (projectile.getContentSize().width / 2.0f));
	    float ratio = (float)offY / (float)offX;
	    int realY = (int)((realX * ratio) + projectile.getPosition().y);
	    CGPoint realDest = CGPoint.ccp(realX, realY);
	 
	    // Determine the length of how far we're shooting
	    int offRealX = (int)(realX - projectile.getPosition().x);
	    int offRealY = (int)(realY - projectile.getPosition().y);
	    float length = (float)Math.sqrt((offRealX * offRealX) + (offRealY * offRealY));
	    float velocity = 1080.0f / 1.0f; // 480 pixels / 1 sec
	    float realMoveDuration = length / velocity;
	 
	    // Move projectile to actual endpoint
	    projectile.runAction(CCSequence.actions(
	            CCMoveTo.action(realMoveDuration, realDest),
	            CCCallFuncN.action(this, "spriteMoveFinished")));
	 
	    return true;
	}
	
	public void update(float dt)
	{
		CCBitmapFontAtlas scorelabel = (CCBitmapFontAtlas) getChildByTag(SCORE_LABEL_TAG);
	    ArrayList<CCSprite> projectilesToDelete = new ArrayList<CCSprite>();
	 
	    for (CCSprite projectile : _projectiles)
	    {
	        CGRect projectileRect = CGRect.make(projectile.getPosition().x - (projectile.getContentSize().width),
	                                            projectile.getPosition().y - (projectile.getContentSize().height),
	                                            projectile.getContentSize().width,
	                                            projectile.getContentSize().height);
	 
	        ArrayList<CCSprite> targetsToDelete = new ArrayList<CCSprite>();
	 
	        for (CCSprite target : _targets)
	        {
	            CGRect targetRect = CGRect.make(target.getPosition().x - (target.getContentSize().width / 2),
	                                            target.getPosition().y - (target.getContentSize().height /2),
	                                            target.getContentSize().width,
	                                            target.getContentSize().height);
	 
	            if (CGRect.intersects(projectileRect, targetRect)){
	                targetsToDelete.add(target);

		           /* CCSprite zombie = CCSprite.sprite("zombieHeadShot.atlas/zombie-HeadShot-1.png");
		        	zombie.setScale(3);
		        	zombie.setPosition(target.getPosition());
		        	addChild(zombie);
		        	CCAnimation animation = CCAnimation.animation("walk", 0.1f);
		            for (int i = 1; i < 8; i++) {
		            	animation.addFrame(CCFormatter.format("zombieHeadShot.atlas/zombie-HeadShot-%d.png", i));
		                CCIntervalAction animAction = CCAnimate.action(0.2f, animation, false);
		                zombie.runAction(animAction);
		            }
	
		            CCIntervalAction action = CCAnimate.action(animation);
		            zombie.runAction(action);*/
	            }
	        }
	 
	        for (CCSprite target : targetsToDelete)
	        {
	            _targets.remove(target);
	            removeChild(target, true);
	        }
	 
	        if (targetsToDelete.size() > 0){
	            projectilesToDelete.add(projectile);
	        }
	    }
	 
	    for (CCSprite projectile : projectilesToDelete)
	    {
	    	score=score+10;
	    	scorelabel.setString("Score : " + CCFormatter.format("%03d", score));
	    	//position=projectile.getPosition();
	        _projectiles.remove(projectile);
	        removeChild(projectile, true);
	        if (++_projectilesDestroyed >= 5)
	        {
		    	SoundEngine.sharedEngine().pauseSound();
	            _projectilesDestroyed = 0;
	            CCDirector.sharedDirector().replaceScene(GameOverLayer.scene("Gagner !! Ton score est de "+score));
	        }
	    }
	}
	
	/*public boolean ccTouchesBegan (MotionEvent event)
    {
    CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
    
  //Use the tag to get a reference to the Background image
    //CCSprite sprite = (CCSprite)getChildByTag(SHOT_TAG ) ;
    CCSprite sprite = CCSprite.sprite("shot.png");
	sprite.setPosition(CGPoint.ccp(0, 0));
	addChild(sprite);
    
	CCMoveBy act1 = CCMoveBy.action(1, (CGPoint.ccp(location.x,location.y)));
    sprite.runAction(CCRepeat.action(act1, 2));
    sprite.setPosition(0, screenSize.getHeight()/2);
     
    return true ;
    }*/
	
	/*public void pauseCallback(Object sender) {
		CCDirector.sharedDirector().onPause();
		
	}*/
	
	public static CCScene scene()
    {
        CCScene scene = CCScene.node();
        CCLayer layer = new GameScene();
        scene.addChild(layer);
        return scene;
    }

}
