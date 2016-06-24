package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Fruits;
import com.mygdx.game.Items.Rocks;
import com.mygdx.game.Scene.Hud;
import com.mygdx.game.Screen.PlayScreen;

/**
 * Created by user on 31/05/2016.
 */
public class Collector extends Sprite {
    public enum State{STANDING,RUNNING,DEAD,SUCCESS};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion collectorStand;
    private Animation collectorRun;
    private float stateTimer;
    private boolean runningRight;
    private boolean collectoIsDead;
    private int rocknum;
    private float previousPosition;
    private float level;

    public Collector(World world,PlayScreen screen,float level)
    {
        super(screen.getAtlas().findRegion("myboy1"));
        this.level=level;
        this.world=world;
        currentState=State.STANDING;
        previousState=State.STANDING;
        stateTimer=0;
        rocknum=3;
        runningRight=true;
        collectoIsDead=false;
        Array<TextureRegion> frames=new Array<TextureRegion>();
        //for(int i=0;i<3;i++)
        frames.add(new TextureRegion(getTexture(),0,153,90,300));
        frames.add(new TextureRegion(getTexture(),90,153,100,300));
        frames.add(new TextureRegion(getTexture(),200,153,100,300));

        collectorRun=new Animation(0.1f,frames);
        frames.clear();


        collectorStand=new TextureRegion(getTexture(), 0, 153, 89, 300);
        defineCollector();
        setBounds(0, 0, 35 / Fruits.PPM, 75 / Fruits.PPM);//here we can change the size of our Animation
        setRegion(collectorStand);
    }
    public TextureRegion myregion(float dt)
    {
        TextureRegion region;
        region=collectorStand;

        if(b2body.getLinearVelocity().x<0 )
        {
            region.flip(true, false);
        }
        return region;
    }
    public void update(float dt)
    {
        float b=0;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.8f);
        setRegion(getFrame(dt));


    }
    public TextureRegion getFrame(float dt)// return the appropriate frames for the sprite to be drawn
    {
        currentState=getState();
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
        if(b2body.getPosition().x>3.7f)
            b2body.setLinearVelocity(0,0);
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
    public float getStateTimer()//25
    {
        return stateTimer;
    }
    public boolean Rockhit()
    {
        if(Rocks.isRock) {
            rocknum--;
            Hud.collectorLives(rocknum);
            Rocks.isRock=false;
            if (rocknum == 0)
                return true;
            else
                return false;

        }
        else
            Hud.collectorLives(rocknum);
        return false;
    }
    public State getState()
    {
        //Gdx.app.log(Float.toString(b2body.getLinearVelocity().x),"hi");
        if ((Hud.getTime()<0)) {
            Fruits.manager.get("music/Backmusic.ogg", Music.class).stop();
            Fruits.manager.get("music/fail.mp3", Sound.class).play();
            collectoIsDead=true;
            Filter filter = new Filter();
            filter.maskBits = Fruits.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
            //b2body.applyLinearImpulse(new Vector2(0,-2.5f), b2body.getWorldCenter(), true);
            Hud.setTime();// to prevent the action of applying more times

            return State.DEAD;
        }
        if ( Rockhit()) {
            Fruits.manager.get("music/Backmusic.ogg", Music.class).stop();
            Fruits.manager.get("music/fail.mp3", Sound.class).play();
            collectoIsDead=true;
            Filter filter = new Filter();
            filter.maskBits = Fruits.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
            //b2body.applyLinearImpulse(new Vector2(0,-2.5f), b2body.getWorldCenter(), true);
            return State.DEAD;
        }
        if((Hud.getTime()==0&& Hud.getScore()>=(level*30)+50)|| (Hud.getScore()>=(level*30)+50)) {
            Fruits.manager.get("music/Backmusic.ogg", Music.class).stop();
            Fruits.manager.get("music/cheering.mp3", Sound.class).play();
            return State.SUCCESS;
        }
        if (b2body.getLinearVelocity().x!=0)
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
        fdef.filter.categoryBits=Fruits.COLLECTOR_BIT;
        fdef.filter.maskBits=Fruits.GROUND_BIT | Fruits.APPLE_BIT |Fruits.ROCK_BIT |Fruits.ORANGE_BIT
        |Fruits.WATERMELON_BIT|Fruits.STRAWBERRY_BIT | Fruits.BANANA_BIT;
        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);
        PolygonShape head=new PolygonShape();//14
        Vector2[]vertics=new Vector2[4];//14
        vertics[0]=new Vector2(-0.5f,3.5f).scl(12/Fruits.PPM);//14
        vertics[1]=new Vector2(0.6f,3.5f).scl(12/Fruits.PPM);//14
        vertics[2]=new Vector2(-0.5f,2).scl(12/Fruits.PPM);//14
        vertics[3]=new Vector2(0.6f,2).scl(12/Fruits.PPM);//14
        head.set(vertics);
        fdef.shape=head;//14
        //fdef.restitution=0.5f;// this will enable mario to jumb slowly from goomba head
        fdef.filter.categoryBits=Fruits.COLLECTOR_BIT;
        fdef.filter.maskBits=Fruits.GROUND_BIT | Fruits.APPLE_BIT|Fruits.ROCK_BIT|Fruits.ORANGE_BIT
                |Fruits.WATERMELON_BIT|Fruits.STRAWBERRY_BIT |Fruits.BANANA_BIT;
        b2body.createFixture(fdef);


    }
}
