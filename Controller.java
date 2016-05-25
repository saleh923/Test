package com.saleh.mariobro.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.saleh.mariobro.MarioBros;
import com.saleh.mariobro.Screens.PlayScreen;
import com.saleh.mariobro.Sprites.Mario;

/**
 * Created by user on 24/05/2016.
 */
public class Controller {// after creating draw method and typing stage.draw then left click on controller and generate getter for
    // down left right up images
    Viewport viewport;
    Stage stage;
    boolean upPressed,downPressed,leftPressed,rightPressed;
    OrthographicCamera cam;
    public Controller()
    {
        cam=new OrthographicCamera();
        viewport=new FitViewport(800,400,cam);
        stage=new Stage(viewport,MarioBros.batch);
        Gdx.input.setInputProcessor(stage);

        Table table=new Table();
        table.right().bottom();
        Image upImage=new Image(new Texture("flatDark25.png"));
        upImage.setSize(50,50);
        upImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });
        Image downImage=new Image(new Texture("flatDark26.png"));
        downImage.setSize(50,50);
        downImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });
        final Image leftImage=new Image(new Texture("flatDark23.png"));
        leftImage.setSize(50,50);
        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        Image rightImage=new Image(new Texture("flatDark24.png"));
        rightImage.setSize(50, 50);
        rightImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });
        //now we want to add images to the table, the table should be like tic tac toe game with 9 cells
        table.add();
        table.add(upImage).size(upImage.getWidth(), upImage.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight());
        table.add();
        table.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImage).size(downImage.getWidth(), downImage.getHeight());
        table.add();
        stage.addActor(table);

    }
    public void draw()
    {
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
    public void resize(int width,int height)
    {
        viewport.update(width,height);
    }
}
