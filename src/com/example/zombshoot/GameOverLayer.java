package com.example.zombshoot;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;



public class GameOverLayer extends CCColorLayer
{
    protected CCLabel _label;
 
    public static CCScene scene(String message)
    {
        CCScene scene = CCScene.node();
        GameOverLayer layer = new GameOverLayer(ccColor4B.ccc4(255, 255, 255, 255));
 
        layer.getLabel().setString(message);
 
        scene.addChild(layer);
        
        return scene;
    }
 
    public CCLabel getLabel()
    {
        return _label;
    }
 
    protected GameOverLayer(ccColor4B color)
    {
        super(color);
 
        this.setIsTouchEnabled(true);
 
        CGSize winSize = CCDirector.sharedDirector().displaySize();
 
        _label = CCLabel.makeLabel("Won't See Me", "DroidSans", 32);
        _label.setColor(ccColor3B.ccBLACK);
        _label.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
        addChild(_label);
 
        CCBitmapFontAtlas playLabel = CCBitmapFontAtlas.bitmapFontAtlas ("Rejouer", "bionic.fnt");
    	
        
        CCMenuItemLabel playItem = CCMenuItemLabel.item(playLabel , this, "playCallback");
        
        CCMenu menu = CCMenu.menu() ;
		menu.addChild(playItem);
		menu.alignItemsVertically(20);
		final CGPoint p = menu.getPositionRef();
		menu.setPosition(CGPoint.ccpSub(p, CGPoint.ccp(-420,0))) ;
		addChild(menu);
        //this.runAction(CCSequence.actions(CCDelayTime.action(10.0f), CCCallFunc.action(this, "gameOverDone")));
    }
 
    public void gameOverDone()
    {
        CCDirector.sharedDirector().replaceScene(GameScene.scene());
    }
 
    //@Override
    /*public boolean ccTouchesEnded(MotionEvent event)
    {
        gameOverDone();
 
        return true;
    }*/
    
    public void playCallback(Object sender) {
    	gameOverDone();
		
	}
    
}