package app.panchit.longpradit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Color;
import java.util.Iterator;

import javax.print.attribute.standard.OrientationRequested;

public class MyGdxGame extends ApplicationAdapter {
	// explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture, cloudTexture, pigTexture, coinsTexture; // รูปภาพจะอยู่ที่นี่ทั้งหมด
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont, scoreBitmapFont; // จะเขียนตัวหนังสือบนเกม เช่น ชื่อเกม
	private int xCloudAnInt, yCloudAnInt = 600; // y มาจากความสูงของภาพ
	private boolean cloudABoolean = true;
	private Rectangle pigRectangle, coinsRectangle; // ของ badlogic เท่านั้น สำหรับเขียน control
	// การทำ overlap ระหว่าง rectangle ทั้ง 2 pig และ coin

	private Vector3 objVector3;
	private Sound pigSound,waterDropSound, coinsDropSound; // ต้องเป็นของ badlogic เท่านั้น
	private Array<Rectangle> coinsArray; // ของ badlogic เท่านั้น สำหรับเขียน control แล้วพิมพ์ R เลือก Rectangle ของ badlogic
	private long lastDropCoins;// ให้ปล่อยเหรียญแบบไม่มีที่สิ้นสุด จะเป็นการ random ของการปล่อยเหรียญที่จะไม่ซ้ำต่ำแหน่งหลังสุด
	private Iterator<Rectangle> coinsIterator; // Iterator ของ java util / Rectangle ของ badlogic
	private int scoreAnInt; // set score equals to 0 in the begining


	@Override
	public void create() {
		batch = new SpriteBatch();

		// กำหนดขนาดของจอที่ต้องการ
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1200, 800); // ให้สี่เหลี่ยมของจอเป็น 1200 x 800 และจะได้นำไปเล่นได้ทุกเครื่อง

		// set up wallpaper
		wallpaperTexture = new Texture("mywallpaper4.png");

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

		// set up coins
		coinsTexture = new Texture("coins.png");

		// create coinsArray ในการปล่อยเหรียญ เหรียญแต่ละเหรียญคือ array อันแรก คือ array[0]
		coinsArray = new Array<Rectangle>(); // Rectangle ของ badlogic
		coinsRandomDrop(); // จะสุ่มหาตำแหน่งปล่อยเหรียญ มีตำแหน่งทั้งหมด 1200 การสุ่มจึงอยู่ระหว่าง 0-1200

		// set up water drop sound
		waterDropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));

		// set up coins drop sound
		coinsDropSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));

		// set up scoreBitMapFont
		scoreBitmapFont = new BitmapFont();
		scoreBitmapFont.setColor(com.badlogic.gdx.graphics.Color.BLUE);
		scoreBitmapFont.setScale(4); // index value, we can make it bigger



	} // create เอาไว้กำหนดค่า

	private void coinsRandomDrop() {
		coinsRectangle = new Rectangle(); // Rectangle ของ badlogic
		coinsRectangle.x = MathUtils.random(0,1136); // พิมพ์ Mau จะขึ้นของ badlogic, เลือก random ที่มี float start, 0-(1200-64) ขนาดของเหรียญ
		// เมื่อ deltatime เปลี่ยน เหรียญก็จะตกลงมาเรื่อย ๆ ต่อเนื่อง

		coinsRectangle.y = 800; // คงที่ระดับความสูงเดิม
		coinsRectangle.width = 64; // ความกว้างของภาพเหรียญ
		coinsRectangle.height = 64; // ความสูงของภาพเหรียญ
		coinsArray.add(coinsRectangle);
		lastDropCoins = TimeUtils.nanoTime(); // timeutil ของ badlogic, nanotime หมายถึง ถ้าค่า random ซ้ำ จะไม่ปล่อยจากที่เดียวกัน



	} // coinsRandomDrop

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

		// Drawable coins
		for (Rectangle forCoins : coinsArray) {
			batch.draw(coinsTexture, forCoins.x, forCoins.y);
		}

        // drawable score
		scoreBitmapFont.draw(batch, "Score = " + Integer.toString(scoreAnInt), 750, 750); // 1000 is x, 750 is y (same height as Coins PBRU





		batch.end();

		// move cloud
		moveCloud();

		// การ active when touch screen
		activeTouchScreen();

		// Random drop coins
		randomDropCoins(); // ทำหน้าที่หย่อนเหรียญ

		//


	} // render นี่คือ การวน loop

	private void randomDropCoins() {
		// timeUtil ของ badlogic
		// 1E9 means 1 power 9 // random every 1 second, and call coinsRandomDrop()
		if (TimeUtils.nanoTime() - lastDropCoins > 1E9) {
			coinsRandomDrop();
		}
		coinsIterator = coinsArray.iterator();
		while (coinsIterator.hasNext()) {
			Rectangle myCoinsRectangle = coinsIterator.next();
			myCoinsRectangle.y -= 50 * Gdx.graphics.getDeltaTime(); // y axis changed, but x no change in case
			// 50 because coins need to drop slower than pig run

			// when coins into floor, we need to wipe and return the memory allocation, or the memory will be full
			if (myCoinsRectangle.y + 64 < 0) {
				waterDropSound.play(); // when the coin hits the floor, the water drop will sound

				coinsIterator.remove(); // when coin drops from y axis and goes over the width of coin (64_,
				// the coin will be removed from the screen
			} // if

			// when coins overlap (not necessary to have the same size between pig and coins, when a part hits each other, thing can happen)
			// the pig, we want this following happens
			if (myCoinsRectangle.overlaps(pigRectangle)) {
				coinsDropSound.play();
				coinsIterator.remove(); // when coins touch the pig, they will be removed.
			} // if



		} // while loop
	} // randomDropCoins

	private void activeTouchScreen() {
		if (Gdx.input.isTouched()) {

			// sound effect ของ pig
			pigSound.play();

			objVector3 = new Vector3(); // ทำหน้าที่เก็บค่าที่เมื่อนิ้วโดนจอ จะรู้ตำแหน่งของจอ
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0); // เมื่อมีการคลิก ให้ดึงค่า x y มาให้เรา

			// จะใช้ bjVector3.x < 600 ก็ได้
			if (objVector3.x < Gdx.graphics.getWidth()/2) {
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
