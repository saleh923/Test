package com.mygdx.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controller;
import com.mygdx.game.Fruits;

import com.mygdx.game.Items.Apple;
import com.mygdx.game.Items.Apples;
import com.mygdx.game.Items.Banana;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemDef;
import com.mygdx.game.Items.Orange;
import com.mygdx.game.Items.Rocks;
import com.mygdx.game.Items.Strawberry;
import com.mygdx.game.Items.Watermelon;
import com.mygdx.game.Scene.Hud;
import com.mygdx.game.Sprites.Basket;
import com.mygdx.game.Sprites.Collector;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by user on 30/05/2016.
 */
public class PlayScreen implements Screen {
    private Fruits game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private int globalcounter;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Collector player;
    private boolean right=true;
    private boolean left=true;
    private boolean gameisover;
    private Apple apple;
    private Apple apple1;
    public float counter;
    public float counter1,counter2,counter3,counter4,counter5;
    public float temp;
    public Basket basket;
    private Controller controller;
    private Array<Item> items;
    private ArrayList<Apple> apples;
    private ArrayList<Rocks> rocks;
    private ArrayList<Watermelon> watermelons;
    private ArrayList<Orange> oranges;
    private ArrayList<Strawberry> strawberries;
    private ArrayList<Banana> bananas;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private Music music;
    private float appleNum;
    private float rocksNum;
    private float orangeNum;
    private float strawberryNum;
    private float watermelonNum;
    private float bananaNum;
    private float velocity;
    private float level;
    Preferences prefs;
    private boolean pausestate;

    public PlayScreen(Fruits game, float level)
    {
        this.level=level;
        this.game=game;
        gamecam=new OrthographicCamera();
        gameport=new FitViewport(Fruits.V_WIDTH/Fruits.PPM,Fruits.V_HIEGT/Fruits.PPM,gamecam);
        hud=new Hud(game.batch,level);
        atlas=new TextureAtlas("mypack.pack");
        mapLoader=new TmxMapLoader();
        if(level<7)
            map=mapLoader.load("World1.tmx");
        else if(level>=7&&level<13)
            map=mapLoader.load("World2.tmx");
        else if(level>=13&&level<19)
            map=mapLoader.load("World3.tmx");
        else if(level>=19)
            map=mapLoader.load("World4.tmx");
        renderer=new OrthogonalTiledMapRenderer(map,1/Fruits.PPM);
        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2f,0);
        globalcounter=100;
        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();
        new B2WorldCreator(this);
        player=new Collector(world,this,level);
        world.setContactListener(new WorldContactListener());
        controller=new Controller(game.batch);
        appleNum=130;
        rocksNum=200;
        watermelonNum=130;
        orangeNum=130;
        strawberryNum=130;
        bananaNum=130;
        //apple=new Apple(world,this);
        items=new Array<Item>();
        itemsToSpawn=new LinkedBlockingQueue<ItemDef>();
        apples=new ArrayList<Apple>();
        rocks=new ArrayList<Rocks>();
        strawberries=new ArrayList<Strawberry>();
        oranges=new ArrayList<Orange>();
        watermelons=new ArrayList<Watermelon>();
        bananas=new ArrayList<Banana>();
        velocity=0;
        apple=new Apple(world,this,100,200);
        temp=0;
        gameisover=false;
        counter=0;
        counter1=counter2=counter3=counter4=counter5=0;
        //basket=new Basket(this,)
        music=Fruits.manager.get("music/Backmusic.ogg", Music.class);
        music.setLooping(true);
        music.play();
        pausestate=false;
        levelParameters(level,1);
        Gdx.app.log(Float.toString(level), "current level");


    }
    @Override
    public void show()
    {

    }
    public Apple show1(float dt) {
        //if(dt>2)
          //  apple1=new Apple(world,this);
        return apple1;
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
    public void addItems()
    {
        Random random=new Random();
        boolean b=true;
        int x=random.nextInt(350 - 30 ) + 30;

        apples.add(new Apple(world, this, x, 100));

    }
    public void addWatermelon()
    {
        Random random=new Random();
        boolean b=true;
        int x=random.nextInt(350 - 30 ) + 30;

        watermelons.add(new Watermelon(world, this, x, 100));

    }
    public void addOrange()
    {
        Random random=new Random();
        boolean b=true;
        int x=random.nextInt(350 - 30 ) + 30;

        oranges.add(new Orange(world, this, x, 100));

    }
    public void addStrawberry()
    {
        Random random=new Random();
        boolean b=true;
        int x=random.nextInt(350 - 30 ) + 30;

        strawberries.add(new Strawberry(world, this, x, 100));

    }
    public void addBanana()
    {
        Random random=new Random();
        boolean b=true;
        int x=random.nextInt(350 - 30 ) + 30;

        bananas.add(new Banana(world, this, x, 100));

    }
    public void addRocks()
    {
        Random random=new Random();
        boolean b=true;
        int x=random.nextInt(350 - 30 ) + 30;

        rocks.add(new Rocks(world, this, x, 100,level));

    }
    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    public void handleinput(float dt)
    {
        if (controller.isRightPressed() && player.b2body.getLinearVelocity().x<velocity)
        {
           // if(right)
                player.b2body.applyLinearImpulse(new Vector2(0.15f, 0), player.b2body.getWorldCenter(), true);
      }

        if (controller.isLeftPressed()&& player.b2body.getLinearVelocity().x>-velocity ) {
            //if(left)
                player.b2body.applyLinearImpulse(new Vector2(-0.15f, 0), player.b2body.getWorldCenter(), true);//5
         }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            if (pausestate)
                pausestate = false;
            else
                pausestate=true;
        }
       /* if(hud.isPauseClick())
        {
            if(pausestate){
                pause();
                pausestate=false;
            }
            else
            {
                resume();
                pausestate=true;
            }
        }*/
    }
    public void update(float dt)
    {
        counter++;
        counter1++;
        counter2++;
        counter3++;
        counter4++;
        counter5++;
        handleinput(dt);
        if(counter>appleNum) {
            addItems();
            levelParameters(level,1);
            counter=0;
        }
        if(counter1>rocksNum) {
            addRocks();
            levelParameters(level,6);
            counter1=0;
        }
        if(counter2>strawberryNum) {
            addStrawberry();
            levelParameters(level, 5);
            counter2=0;
        }
        if(counter3>watermelonNum) {
            addWatermelon();
            levelParameters(level, 4);
            counter3=0;
        }
        if(counter4>orangeNum) {
            addOrange();
            levelParameters(level, 3);
            counter4=0;
        }
        if(counter5>bananaNum) {
            addBanana();
            levelParameters(level, 2);
            counter5=0;
        }
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        apple.update(dt);
        hud.update(dt);

                //apple1.update(dt);
        for(Rocks rock:rocks)
            rock.update(dt);
        for(Apple apple:apples)
            apple.update(dt);
        for (Strawberry strawberry:strawberries)
            strawberry.update(dt);
        for(Watermelon watermelon:watermelons)
            watermelon.update(dt);
        for (Orange orange:oranges)
            orange.update(dt);
        for (Banana banana:bananas)
            banana.update(dt);
        for (Item item: items)
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


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(pausestate) {

                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

        }
        else
        {
            update(delta);
        }

        renderer.render();
        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        //Gdx.app.log("hi","hello");
        counter+=delta;
        player.draw(game.batch);
        apple.draw(game.batch);

        for(Apple apple:apples)
            apple.draw(game.batch);
        for(Rocks rock:rocks)
            rock.draw(game.batch);
        for (Strawberry strawberry:strawberries)
            strawberry.draw(game.batch);
        for(Watermelon watermelon:watermelons)
            watermelon.draw(game.batch);
        for (Orange orange:oranges)
            orange.draw(game.batch);
        for (Banana banana:bananas)
            banana.draw(game.batch);
        /*if(counter>2) {
            apple1 = new Apple(world, this);
            apple1.draw(game.batch);
            counter = 0;
        }*/
        for (Item item: items)
            item.draw(game.batch);
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();
        if(gameOver())//24
        {
            gameisover=true;
            globalcounter--;
            Gdx.app.log(Float.toString(globalcounter), "counter");
            if(globalcounter==0) {
                game.setScreen(new GameOverScreen(game, level));
                dispose();
            }
        }
        if(gameisover==true) {
            globalcounter--;
            Gdx.app.log(Float.toString(globalcounter), "counter");
            if (globalcounter == 0) {
                game.setScreen(new GameOverScreen(game, level));
                dispose();
            }
        }
        if(nextLevel())
        {
            prefs = Gdx.app.getPreferences("levels");
            String name=prefs.getString("level", " ");
            if(name==" ") {
                prefs.putString("level", Float.toString(level + 1));
                prefs.flush();
            }
            else if(Float.valueOf(name)>level)
                Gdx.app.log(name,"levelstrored");
            else {
                Gdx.app.log(name, "level");
                prefs.putString("level", Float.toString(level+1));
                prefs.flush();
            }
            game.setScreen(new Worlds(game,level+1));
            dispose();
        }
    }

    public boolean gameOver()//25
    {
        if(player.currentState== Collector.State.DEAD )// will show the game over screen after 3 seconds
        {

            return true;
        }
        return false;
    }
    public boolean nextLevel()//25
    {
        if(player.currentState== Collector.State.SUCCESS)// will show the game over screen after 3 seconds
        {
            return true;
        }
        return false;
    }
    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
        controller.resize(width, height);
    }

    @Override
    public void pause() {
       // this.pause();

    }

    @Override
    public void resume() {
        //this.resume();
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
    public void levelParameters(float level,int type)
    {
        Random random=new Random();
        int low,x;
        int height;
       for(int i=1;i<7;i++)
       {
           low=50-(i*2);
           height=500-(i*15);
           if(level==i)
           {
               if(type==1) {
                    x=random.nextInt(height - low ) + low;
                   appleNum = x;
               }
               else if(type==2) {
                   x=random.nextInt(height - low ) + low;
                   bananaNum = x;
               }
               else if(type==3) {
                   x=random.nextInt(height - low ) + low;
                   orangeNum = x;
               }
               else if(type==4) {
                   x=random.nextInt(height - low ) + low;
                   watermelonNum = x;
               }
               else if(type==5) {
                   x=random.nextInt(height - low ) + low;
                   strawberryNum = x;
               }
              // bananaNum=bananaNum-((i*6)+(6/i));
               else if(type==6) {
                   x=random.nextInt(height - low ) + low;
                   rocksNum = x;
               }
                   rocksNum=rocksNum;
           }
           if(level<3)
           velocity=1.3f;
           if(level>=3&& level<8)
               velocity=1.5f;
           if(level>=8)
               velocity=1.7f;
       }
    }
}
