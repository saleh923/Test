package com.mygdx.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Fruits;
import com.mygdx.game.Screen.PlayScreen;

import java.security.PublicKey;


/**
 * Created by user on 04/06/2016.
 */

public class Apple extends Sprite {
    public PlayScreen screen;
    public World world;
    public Apple apple;
    public Body b2body;
    public Vector2 velocity;
    public float counter;
    private TextureRegion Apple;
    //private Animation collectorRu;
    private float stateTimer;
    private boolean toDestroy;
    private boolean destroyed;
    public Apple(World world,PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("boys3"));
        this.screen=screen;
        this.world=world;
        stateTimer=0;
        defineApple();
        Apple=new TextureRegion(getTexture(),0,0,120,120);
        setBounds(0, 0, 20 / Fruits.PPM, 20 / Fruits.PPM);//here we can change the size of our Animation
        setRegion(Apple);
        toDestroy=false;
        destroyed=false;
        velocity=new Vector2(0,-2);
        counter=0;
    }
    public void defineApple() {
        BodyDef bdef=new BodyDef();

        bdef.position.set(100/Fruits.PPM,200/Fruits.PPM);
        bdef.type=BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);
        FixtureDef fdef=new FixtureDef();
        //PolygonShape shape=new PolygonShape();
        CircleShape shape=new CircleShape();
        shape.setRadius(7 / Fruits.PPM);
        fdef.filter.categoryBits=Fruits.APPLE_BIT;
        fdef.filter.maskBits=Fruits.GROUND_BIT | Fruits.COLLECTOR_BIT;
        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);

    }
    public void update(float dt)
    {

        stateTimer += dt;
        if(toDestroy&&!destroyed)
        {
            world.destroyBody(b2body);
            destroyed=true;
            stateTimer=0;
        }
        if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        counter+=dt;
        Gdx.app.log(Float.toString(counter),"time");
        if(counter>2)
        {
            apple=new Apple(world,screen);
            counter=0;
        }

    }
    public void draw(Batch batch)
    {
        if(!destroyed )
        {
            super.draw(batch);
        }
    }
    public void hit()
    {
        toDestroy=true;

    }

}
