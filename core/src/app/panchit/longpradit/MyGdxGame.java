package app.panchit.longpradit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Color;

import javax.print.attribute.standard.OrientationRequested;

public class MyGdxGame extends ApplicationAdapter {
	// explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont; // จะเขียนตัวหนังสือบนเกม เช่น ชื่อเกม

	@Override

	public void create() {
		batch = new SpriteBatch();

		// กำหนดขนาดของจอที่ต้องการ
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1200, 800); // ให้สี่เหลี่ยมของจอเป็น 1200 x 800 และจะได้นำไปเล่นได้ทุกเครื่อง

		// set up wallpaper
		wallpaperTexture = new Texture("mywallpaper.gif");

		// set up BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(com.badlogic.gdx.graphics.Color.RED);
		nameBitmapFont.setScale(4); // ขนาดของตัวหนังสือ


	} // create เอาไว้กำหนดค่า

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// ต้อง set up screen ตรงนี้ด้วย
		objOrthographicCamera.update();
		batch.setProjectionMatrix(objOrthographicCamera.combined); // วาดภาพให้มีขนาดเท่ากับจอของเรา

		// เอาไว้วาด object
		batch.begin();

		// drawable wallpaper
		batch.draw(wallpaperTexture, 0, 0);

		// Drawable BitMapFont
		nameBitmapFont.draw(batch,"Coins PBRU", 50, 600);

		batch.end();
	} // render นี่คือ การวน loop
} // Main Class
