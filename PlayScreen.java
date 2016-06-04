package com.mygdx.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controller;
import com.mygdx.game.Fruits;

import com.mygdx.game.Items.Apple;
import com.mygdx.game.Items.Apples;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemDef;
import com.mygdx.game.Scene.Hud;
import com.mygdx.game.Sprites.Collector;
import com.mygdx.game.Sprites.InteractiveTileObject;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by user on 30/05/2016.
 */
public class PlayScreen implements Screen {
    private Fruits game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Collector player;
    private boolean right=true;
    private boolean left=true;
    private Apple apple;

    private Controller controller;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(Fruits game)
    {
        this.game=game;
        gamecam=new OrthographicCamera();
        gameport=new FitViewport(Fruits.V_WIDTH/Fruits.PPM,Fruits.V_HIEGT/Fruits.PPM,gamecam);
        hud=new Hud(game.batch);
        atlas=new TextureAtlas("mypack.pack");
        mapLoader=new TmxMapLoader();
        map=mapLoader.load("level3.tmx");
        renderer=new OrthogonalTiledMapRenderer(map,1/Fruits.PPM);
        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2f,0);

        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();
        new B2WorldCreator(this);
        player=new Collector(world,this);
        world.setContactListener(new WorldContactListener());
        controller=new Controller(game.batch);
        apple=new Apple(world,this);
        items=new Array<Item>();
        itemsToSpawn=new LinkedBlockingQueue<ItemDef>();

    }
    @Override
    public void show() {

    }
    public void spawnItem(ItemDef idef)
    {
        itemsToSpawn.add(idef);
    }
    public void handleSpawningItems()
    {
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef=itemsToSpawn.poll();
            if(idef.type== Apples.class)
                items.add(new Apples(this,idef.position.x,idef.position.y));
        }
    }
    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    public void handleinput(float dt)
    {
        if (controller.isRightPressed()&&player.b2body.getPosition().x<3.7f)
        {
            if(right)
                player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), player.b2body.getWorldCenter(), true);
            else {
                player.b2body.setLinearVelocity(0, 0);
                right=true;
                left=false;
            }
            //player.b2body.setTransform(player.b2body.getPosition().x+0.05f,player.b2body.getPosition().y,player.b2body.getAngle());
        }
        if (controller.isLeftPressed()&&player.b2body.getPosition().x>0.26f ) {
            if(left)
                player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), player.b2body.getWorldCenter(), true);//5
            else {
                player.b2body.setLinearVelocity(0, 0);
                right=false;
                left=true;
            }
            //player.b2body.setTransform(player.b2body.getPosition().x - 0.05f, player.b2body.getPosition().y, player.b2body.getAngle());
        }
        if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0) {

            player.b2body.applyLinearImpulse(new Vector2(0, 4), player.b2body.getWorldCenter(), true);

        }
    }
    public void update(float dt)
    {
        handleinput(dt);
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        apple.update(dt);
        for(Item item:items)
            item.update(dt);
        gamecam.update();
        renderer.setView(gamecam);
    }
    public TiledMap getMap()//12 this is for Goomba
    {
        return map;
    }
    public World getWorld()
    {
        return world;
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        apple.draw(game.batch);
        for(Item item : items)
            item.draw(game.batch);
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();

    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
        controller.resize(width, height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
