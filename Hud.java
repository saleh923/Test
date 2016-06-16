package com.mygdx.game.Scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Fruits;
import com.mygdx.game.Screen.PlayScreen;

/**
 * Created by user on 30/05/2016.
 */
public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private static Integer worldTimer;
    private float timecount;
    private static Integer score;
    private static Integer noApple, noBanana, noOrange, noWatermelon, noStrawberry;
    Label countdownLabel;
    private static Label scorelabel;
    private static Label liveLabel;
    private static Integer lives;
    private Label timelabel;
    private Label levellabel;
    private Label worldlabel;
    private Label collectorlabel;
    private Image loading;
    private Image boy;
    private static Label appleNum, bananaNum, strawNum, watermelonNum, orangeNum;
    private Table table;
    private float level;
    private Image pausing;



    private boolean pauseClick;

    public Hud(SpriteBatch sb, float level) {
        this.level = level;
        worldTimer = 60 + (4 * Math.round(level));
        timecount = 0;
        score = 0;
        lives = 3;
        
        viewport = new FitViewport(Fruits.V_WIDTH, Fruits.V_HIEGT, new OrthographicCamera());//2
        stage = new Stage(viewport, sb);//stage is as box and try to put widget and organize things inside that table
        countdownLabel = new Label(String.format("%01d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        liveLabel = new Label(String.format("%01d", lives), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        scorelabel = new Label(String.format("%02d", score), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        bananaNum = new Label(String.format("%01d", noBanana), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        appleNum = new Label(String.format("%01d", noApple), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        strawNum = new Label(String.format("%01d", noStrawberry), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        watermelonNum = new Label(String.format("%01d", noWatermelon), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        orangeNum = new Label(String.format("%01d", noOrange), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        timelabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
       worldlevel(level);
        worldlabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        collectorlabel = new Label("Score: ", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        boy = new Image(new Texture("boy4.png"));
        pausing = new Image(new Texture("Pause.png"));
        pausing.setSize(80, 60);
        pausing.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pauseClick = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pauseClick = false;
            }
        });
        loading = new Image(new Texture("0%.png"));
        table = new Table();
        table.top().left();//table at top of our stage
        table.setFillParent(true);//table is now fill all the stage
        //loading.setSize(40, 10);

        table.row().pad(5, 3, 0, 0);
        table.add(timelabel).expandX();
        table.add(boy).width(20).height(20);

        table.add(worldlabel).spaceRight(20);
        table.add(collectorlabel);
        table.add(loading).width(50).height(15);


        table.row().pad(2, 3, 0, 0);

        table.add(countdownLabel).expandX();

        table.add(liveLabel);
        table.add(levellabel).expandX();
        table.add(scorelabel);
        table.add(pausing).width(40).height(30);
        stage.addActor(table);

    }

    public void update(float dt) {

        timecount += dt;
        if (timecount > 1) {
            worldTimer--;
            if (worldTimer >= 0) {
                countdownLabel.setText(String.format("%02d", worldTimer));
                updatetable();
            }

            timecount = 0;
            //Toast.makeText(Hud.this, "hi", Toast.LENGTH_SHORT).show();
        }
    }

    public static int getTime() {

        return worldTimer;
    }

    public void updatetable() {
        if (score < ((level * 15) + 50) / 10)
            loading = new Image(new Texture("0%.png"));
    
        if (score >= (((level * 15) + 50) / 10) * 8 && score < (((level * 18) + 50) / 10) * 9) {
            loading = new Image(new Texture("90%.png"));

        }
        if (score >= ((level * 15) + 50)) {
            loading = new Image(new Texture("100%.png"));
        }

        table.reset();
        table.top().left();//table at top of our stage
        table.setFillParent(true);//table is now fill all the stage
        table.row().pad(5, 3, 0, 0);
        table.add(timelabel).expandX();
        table.add(boy).width(20).height(20);

        table.add(worldlabel).spaceRight(20);
        table.add(collectorlabel);
        table.add(loading).width(50).height(15);


        table.row().pad(2, 3, 0, 0);

        table.add(countdownLabel).expandX();

        table.add(liveLabel);
        table.add(levellabel).expandX();
        table.add(scorelabel);
        table.add(pausing).width(40).height(30);
        stage.addActor(table);
    }

    public static void addScore(int value, int type) {
        score += value;
        scorelabel.setText(String.format("%02d", score));
         }

    public static void collectorLives(int value) {
        lives = value;
        liveLabel.setText(String.format("%02d", lives));

    }

    public static int getScore() {
        return score;
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
    public void draw() {
        stage.draw();
    }

    public boolean isPauseClick() {
        return pauseClick;
    }

    public void worldlevel(float level) {
        if (level == 1)
            levellabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        if (level == 2)
            levellabel = new Label("1-2", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
        if (level == 3)
            levellabel = new Label("1-3", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));//label for gdx
    }
}
