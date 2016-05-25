package com.saleh.mariobro.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.saleh.mariobro.Items.Item;
import com.saleh.mariobro.Items.ItemDef;
import com.saleh.mariobro.Items.Mushroom;
import com.saleh.mariobro.MarioBros;
import com.saleh.mariobro.Scenes.Hud;
import com.saleh.mariobro.Sprites.Enemy;
import com.saleh.mariobro.Sprites.Goomba;
import com.saleh.mariobro.Sprites.Mario;
import com.saleh.mariobro.Tools.B2WorldCreators;
import com.saleh.mariobro.Tools.Controller;
import com.saleh.mariobro.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by user on 26/04/2016.
 */
public class PlayScreen implements Screen {
    private MarioBros game;//1
    private TextureAtlas atlas;// 7 but we have to use GDX Texture Packer
    private OrthographicCamera gamecam; // define cam to view the world of the game
    private Viewport gameport;//1
    private Hud hud;// containns info about the game information
    //tiled map variables
    private TmxMapLoader mapLoader;// to load the map game
    private TiledMap map;// reference to the map itself
    private OrthogonalTiledMapRenderer renderer; //render map to the screen
    //Box2d variables//4
    private World world;
    private Box2DDebugRenderer b2dr; // graphical representation of frictions and bodies inside box2d world
    private B2WorldCreators creator;//16

    private Mario player; // Mario class object
    private Music music; // 11
    //private Goomba goomba;//12 this is used to test of goombas
    private Array<Item> items;//18
    private LinkedBlockingQueue<ItemDef> itemsToSpawn; //18
    Controller controller;


    public PlayScreen(MarioBros game)
    {
        atlas=new TextureAtlas("Mario GFX/Mario_and_Enemies.pack");//7
        this.game=game;//1
       // texture=new Texture("badlogic.jpg");
        //create cam to follow mario through world cam
        gamecam=new OrthographicCamera();
        //create Fitview to control virtual aspects ratio
        //editing to make it divided by PPM is in 5 step //5
        gameport=new FitViewport(MarioBros.V_WIDTH/MarioBros.PPM,MarioBros.V_HIEGT/MarioBros.PPM,gamecam);// this is related to resize method//1
        //Create game Hud for scores/timers/level info
        hud=new Hud(game.batch);
        //3

        mapLoader=new TmxMapLoader();
        map=mapLoader.load("Mario GFX/Level1.tmx");
        //editing by adding the second parameter is done in 5 step // 5
        renderer = new OrthogonalTiledMapRenderer(map,1/MarioBros.PPM);// here will render the map but cam will not be centered//3

        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0); // to center the cam //3

        //4
        //--vector2 is used for gravity,, true to sleep object to avoid calcualating inside box2d
        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();
       // new B2WorldCreators(world,map);//4
       // new B2WorldCreators(this);//12
        creator=new B2WorldCreators(this);//16

       //player = new Mario(world,this); // initialization of  Mario class object//5
        player = new Mario(this); // initialization of  Mario class object//12
        world.setContactListener(new WorldContactListener());//9

        music =MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);//11
        music.setLooping(true);//11
        music.play();//11
        items=new Array<Item>();//18
        itemsToSpawn=new LinkedBlockingQueue<ItemDef>();//18

        controller=new Controller();
       // goomba=new Goomba(this,5.64f,.16f);//12

    }
    public void spawnItem(ItemDef idef)//18
    {
        itemsToSpawn.add(idef);
    }
    public void handleSpawnItems()//18
    {
        if(!itemsToSpawn.isEmpty())
        {
            ItemDef idef=itemsToSpawn.poll(); //it is like pop from queue
            if(idef.type== Mushroom.class)
            {
                items.add(new Mushroom(this,idef.position.x,idef.position.y));
            }
        }
    }
    public TextureAtlas getAtlas(){//7
        return atlas;
    }
    @Override
    public void show() {

    }
    public void handleinput(float dt)//3
    {
       /* // 24 check if mario is dead
        if(player.currentState!= Mario.State.DEAD) {//24
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))//5 we edit the original one
                //read from instructions there are many options//5
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);//5
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))//5 we edit the original one
                //read from instructions there are many options//5
                player.b2body.applyLinearImpulse(new Vector2(0, -2f), player.b2body.getWorldCenter(), true);//5
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);//5
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);//5
        }*/
        if(controller.isRightPressed())
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);//5
        else if (controller.isLeftPressed())
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);//5
        else
            player.b2body.applyLinearImpulse(new Vector2(0, -2f), player.b2body.getWorldCenter(), true);//5
        if (controller.isUpPressed() )
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    }
    public void update(float dt)
    {// first we have to check if any inputs happened// 3
        handleinput(dt);
        handleSpawnItems();//18
        world.step(1 / 60f, 6, 2);
        player.update(dt);//7.2
        //goomba.update(dt);//12
        for (Enemy enemy : creator.getEnemies()){//16
        enemy.update(dt);
            //17 wake up the enemy
            if(enemy.getX()<player.getX()+1.5)
                enemy.b2body.setActive(true);
        }
        for(Item item : items)//18
        item.update(dt);//18
        hud.update(dt);//11
        // 24 here we will stop the cam movement if mario is dead, if statement for 24
        if(player.currentState!=Mario.State.DEAD)//24
          gamecam.position.x=player.b2body.getPosition().x;// this to move with mario as moving
        gamecam.update();
        renderer.setView(gamecam);// to render th map//3
    }

    @Override
    public void render(float delta) {
        update(delta);// to call it over and over
        Gdx.gl.glClearColor(0, 0, 0, 1);// first we need to clear the texture//0
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        b2dr.render(world, gamecam.combined);//5 or 6
        //7 to draw and set cam to little mario
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();// to open the box that we had put all textures inside
        player.draw(game.batch);// we gave the sprite the game to draw
       // goomba.draw(game.batch);//12 render of goomba

        for (Enemy enemy : creator.getEnemies()) {//16
            enemy.draw(game.batch);
        }
        for(Item item : items)//18
        item.draw(game.batch);//18
        game.batch.end(); // close the box
        //to draw the hud
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();
        if(gameOver())//24
        {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }
    public boolean gameOver()//25
    {
        if(player.currentState== Mario.State.DEAD && player.getStateTimer()>3)// will show the game over screen after 3 seconds
        {
            return true;
        }
        return false;

    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
        controller.resize(width,height);

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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {  //6
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}


