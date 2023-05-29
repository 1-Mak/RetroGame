package com.my.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGame implements Screen {
	private final Texture enemyImage;
	private Texture laserImage;
	private Texture spaceshipImage;
	private Sound shotSound;
	private Sound explosionSound;
	private Music spaceMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle spaceship;
	private Array<Rectangle> enemydrops;
	private Array<Rectangle> lasershots;
	private long lastDropTime;
	private long lastShotTime;
	private int score;
	private BitmapFont font;
	final AmongStars game;
	int count;


	public MyGame(final AmongStars gam) {
		this.game = gam;

		enemyImage = new Texture(Gdx.files.internal("enemy.png"));
		spaceshipImage = new Texture(Gdx.files.internal("spaceship.png"));
		laserImage = new Texture(Gdx.files.internal("laser.png"));


		spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("music.wav"));
		shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));


		//spaceMusic.setLooping(true);
		//spaceMusic.play();


		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 640);
		batch = new SpriteBatch();


		spaceship = new Rectangle();
		spaceship.x = 1200 / 2 - 64 / 2;
		spaceship.y = 20;
		spaceship.width = 64;
		spaceship.height = 64;
		enemydrops = new Array<Rectangle>();
		spawnEnemy();
		lasershots = new Array<Rectangle>();
		spawnlaser();
	}

	private void spawnlaser() {
		Rectangle shot = new Rectangle();
		shot.x = spaceship.x+32;
		shot.y = spaceship.y+55;
		shot.width = 64;
		shot.height = 64;
		lasershots.add(shot);
		lastShotTime = TimeUtils.nanoTime();
	}

	private void spawnEnemy() {
		Rectangle enemydrop = new Rectangle();
		enemydrop.x = MathUtils.random(0, 1200-64);
		enemydrop.y = 640;
		enemydrop.width = 88;
		enemydrop.height = 54;
		enemydrops.add(enemydrop);
		lastDropTime = TimeUtils.nanoTime();
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.font.draw(game.batch, "Enemies killed: " + count, 0, 600);
		game.batch.draw(spaceshipImage, spaceship.x, spaceship.y);
		for (Rectangle enemydrop : enemydrops) {
			game.batch.draw(enemyImage, enemydrop.x, enemydrop.y);
		}
		for (Rectangle shot : lasershots) {
			game.batch.draw(laserImage, shot.x, shot.y);
		}
		game.batch.end();


		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			spaceship.x = touchPos.x - 64 / 2;
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) spaceship.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) spaceship.x += 200 * Gdx.graphics.getDeltaTime();
		if (TimeUtils.nanoTime() - lastShotTime > 700000000) shotSound.play(0.1f);



		if (spaceship.x < 0) spaceship.x = 0;
		if (spaceship.x > 1200 - 64) spaceship.x = 1200 - 64;
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnEnemy();
		if (TimeUtils.nanoTime() - lastShotTime > 700000000) spawnlaser();


		for (Iterator<Rectangle> iter = enemydrops.iterator(); iter.hasNext(); ) {
			Rectangle enemydrop = iter.next();
			enemydrop.y -= 150 * Gdx.graphics.getDeltaTime();
			if (enemydrop.y + 64 < 0) {
				iter.remove();

			}



			for (Iterator<Rectangle> iten = lasershots.iterator(); iten.hasNext(); ) {
				Rectangle shot = iten.next();
				shot.y += 200 * Gdx.graphics.getDeltaTime();
				if (shot.y > 1200) iten.remove();
				if (shot.overlaps(enemydrop)) {

					count++;
					explosionSound.play(0.5f);
					iter.remove();
					iten.remove();
				}
			}

		}
	}

	@Override
	public void resize(int width, int height) {

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

		enemyImage.dispose();
		spaceshipImage.dispose();
		spaceMusic.dispose();
		batch.dispose();
	}
}