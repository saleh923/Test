package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Fruits;
import com.mygdx.game.Screen.PlayScreen;

/**
 * Created by user on 31/05/2016.
 */
public class Collector extends Sprite {
    public enum State{STANDING,RUNNING,DEAD};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion collectorStand;
    private Animation collectorRun;
    private float stateTimer;
    private boolean runningRight;

    public Collector(World world,PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("myboy1"));
        this.world=world;
        currentState=State.STANDING;
        previousState=State.STANDING;
        stateTimer=0;
        runningRight=true;
        Array<TextureRegion> frames=new Array<TextureRegion>();
        for(int i=0;i<3;i++)
        frames.add(new TextureRegion(getTexture(),i*90,122,90,300));
        //frames.add(new TextureRegion(getTexture(),90,122,110,300));
        //frames.add(new TextureRegion(getTexture(),200,122,110,300));

        collectorRun=new Animation(0.1f,frames);
        frames.clear();


        collectorStand=new TextureRegion(getTexture(), 0, 122, 89, 300);
        //collectorStand=new TextureRegion(getTexture(),90,122,110,300);
        //collectorStand=new TextureRegion(getTexture(),200,122,110,300);
        defineCollector();
        setBounds(0, 0, 50 / Fruits.PPM, 100 / Fruits.PPM);//here we can change the size of our Animation


        setRegion(collectorStand);
    }
    public TextureRegion myregion(float dt)
    {
        TextureRegion region;
        region=collectorStand;

        Gdx.app.log(String.valueOf(b2body.getLinearVelocity().x),"hi");
        if(b2body.getLinearVelocity().x<0 )
        {
            region.flip(true,false);


        }

        return region;
    }
    public void update(float dt)
    {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.8f);

        //setRegion(myregion(dt));
        //collectorStand.flip(true,false);
        setRegion(getFrame(dt));
    }
    public TextureRegion getFrame(float dt)// return the appropriate frames for the sprite to be drawn
    {
        currentState=getState();
        Gdx.app.log(currentState.toString(),"hi");
        TextureRegion region;
        switch (currentState)
        {
            case RUNNING:
                region=collectorRun.getKeyFrame(stateTimer,true);
                break;
            case STANDING:
            default:
                region=collectorStand;
                break;
        }
        if((b2body.getLinearVelocity().x<0 || !runningRight)&& !region.isFlipX())
        {
            region.flip(true,false);
            runningRight=false;
        }
        else if((b2body.getLinearVelocity().x>0 || runningRight)&& region.isFlipX())
        {
            region.flip(true,false);
            runningRight=true;
        }

        stateTimer=currentState==previousState?stateTimer+dt:0;
        previousState=currentState;

        return region;
    }
    public State getState()
    {
        Gdx.app.log(Float.toString(b2body.getLinearVelocity().x),"hi");
        if(b2body.getLinearVelocity().x!=0)
            return State.RUNNING;
        else
            return State.STANDING;
    }
    public void defineCollector()
    {
        BodyDef bdef=new BodyDef();
        bdef.position.set(32/Fruits.PPM,32/Fruits.PPM);
        bdef.type=BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);
        FixtureDef fdef=new FixtureDef();
        //PolygonShape shape=new PolygonShape();
        CircleShape shape=new CircleShape();
        shape.setRadius(10/Fruits.PPM);
        fdef.shape=shape;
        b2body.createFixture(fdef);
    }
}
