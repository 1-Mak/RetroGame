package com.my.game;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;


public class IntroScreen implements Screen, InputProcessor {
        private int width, height;
        public float ppuY;
        public float ppuX;
        final AmongStars game;
        OrthographicCamera camera;
        public Map<String, Texture> textures;
        boolean btn;
        private Texture bgTexture;
        private SpriteBatch spriteBatch;
        float CAMERA_WIDTH = 800F;
        float CAMERA_HEIGHT = 480F;
        public OrthographicCamera cam;
        public IntroScreen(final AmongStars gam) {
            game = gam;
            camera = new OrthographicCamera();
            camera.setToOrtho(false, 800, 480);
        }


        @Override
        public void show() {
            textures = new HashMap<String, Texture>();
            btn = false;
            spriteBatch = new SpriteBatch();
            loadTextures();
            ppuX = (float)width / CAMERA_WIDTH;
            ppuY = (float)height / CAMERA_HEIGHT;
            this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
            Gdx.input.setInputProcessor(this);
        }
    private void loadTextures() {
        bgTexture = new Texture(Gdx.files.internal("Main_menu_bground.png"));
        textures.put("cover_button_start_up", new Texture(Gdx.files.internal("cover_button_start_up.png")));
        textures.put("cover_button_start_down", new Texture(Gdx.files.internal("cover_button_start_down.png")));
    }
    private void showMenu() {
        if (btn)
            spriteBatch.draw(textures.get("cover_button_start_down"), 653, 183, 256, 128);
        else
            spriteBatch.draw(textures.get("cover_button_start_up"), 653, 183, 256, 128);
    }
    public void showBG() {
            spriteBatch.draw(bgTexture,0, 0, 1920 , 1076);
        }
        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();

            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            //showBG();
            game.font.draw(game.batch, "Welcome to Drop!", 100, 150);
            game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
            //showMenu();
            game.batch.end();

          if (Gdx.input.isTouched()){
               game.setScreen(new MyGame(game));
               dispose();
            }

        }
        @Override
        public void resize(int width, int height) {

        }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
        public boolean touchDown(int x, int y, int pointer, int button) {

        if((height-y)/ppuY >= 213 && (height-y)/ppuY <= 283 && x/ppuX>=660 && x/ppuX<=780) {
            btn = true;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android))
            return false;
        if(btn){
            dispose();
            game.setScreen(new MyGame(game));
        }

        btn = false;
        return true;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
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

        }
    }

