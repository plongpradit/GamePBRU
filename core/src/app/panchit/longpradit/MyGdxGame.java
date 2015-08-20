package app.panchit.longpradit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.awt.Color;

import javax.print.attribute.standard.OrientationRequested;

public class MyGdxGame extends ApplicationAdapter {
	// explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture, cloudTexture, pigTexture; // รูปภาพจะอยู่ที่นี่ทั้งหมด
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont; // จะเขียนตัวหนังสือบนเกม เช่น ชื่อเกม
	private int xCloudAnInt, yCloudAnInt = 600; // y มาจากความสูงของภาพ
	private boolean cloudABoolean = true;
	private Rectangle pigRectangle; // ของ badlogic เท่านั้น สำหรับเขียน control
	private Vector3 objVector3;
	private Sound pigSound; // ต้องเป็นของ badlogic เท่านั้น


	@Override
	public void create() {
		batch = new SpriteBatch();

		// กำหนดขนาดของจอที่ต้องการ
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1200, 800); // ให้สี่เหลี่ยมของจอเป็น 1200 x 800 และจะได้นำไปเล่นได้ทุกเครื่อง

		// set up wallpaper
		wallpaperTexture = new Texture("mywallpaper3.png");

		// set up BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(com.badlogic.gdx.graphics.Color.RED);
		nameBitmapFont.setScale(4); // ขนาดของตัวหนังสือ

		// set up cloud
		cloudTexture = new Texture("cloud.png");

		// set up pig
		pigTexture = new Texture("pig.png");

		// set up rectangle pig
		pigRectangle = new Rectangle();
		pigRectangle.x = 568; // เมื่อกำหนดให้หมูอยู่ตรงกลาง ตรงกลางคือ 600 ดังนั้น 600-32
		pigRectangle.y = 100;
		pigRectangle.width = 64; // ขนาดของรูปภาพหมู
		pigRectangle.height = 64; // ขนาดของรูปภาพหมู

		// set up pig sound
		pigSound = Gdx.audio.newSound(Gdx.files.internal("pig.wav"));

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

		// วาง cloud ตรงนี้เพื่อไม่ให้อยู่บนสุด จะบังตัวอักษร
		batch.draw(cloudTexture, xCloudAnInt, yCloudAnInt);

		// Drawable BitMapFont
		nameBitmapFont.draw(batch, "Coins PBRU", 50, 750);

		// Drawable pig
		batch.draw(pigTexture, pigRectangle.x, pigRectangle.y);

		batch.end();

		// move cloud
		moveCloud();

		// การ active when touch screen
		activeTouchScreen();


	} // render นี่คือ การวน loop

	private void activeTouchScreen() {
		if (Gdx.input.isTouched()) {

			// sound effect ของ pig
			pigSound.play();

			objVector3 = new Vector3(); // ทำหน้าที่เก็บค่าที่เมื่อนิ้วโดนจอ จะรู้ตำแหน่งของจอ
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0); // เมื่อมีการคลิก ให้ดึงค่า x y มาให้เรา

			if (objVector3.x < 600) {
				//pigRectangle.x -= 10; // โค้ดนี้ไม่ได้คำนึ่งหมูสามารถวิ่งตกกรอบ เลยเปลี่ยนเป็นด้านล่าง
				if (pigRectangle.x < 0) {
					pigRectangle.x = 0;
				} else {
					pigRectangle.x -= 10;
				}
			} else {
				if (pigRectangle.x > 1136) {
					pigRectangle.x = 1136;
				} else {
					pigRectangle.x += 10;
				}
			}
		} // if
	} // activeTouchScreen




	private void moveCloud() {
		// move cloud
		if (cloudABoolean) {
			if (xCloudAnInt < 937) {
				xCloudAnInt += 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		} else {
			if (xCloudAnInt > 0) {
				xCloudAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		}


	}

} // Main Class
