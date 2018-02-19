package esialrobotik.simulateur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import esialrobotik.simulateur.bullet.BulletWorld;
import esialrobotik.simulateur.camera.SimulateurCamera;
import esialrobotik.simulateur.simu2D.World2D;

public class Simulateur extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	int progress = 100;
	ShapeRenderer sr;
	World2D world2D;
	BulletWorld world3D;
	SimulateurCamera uiCam;
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		img = new Texture("RobotCities2018-Logo.png");
		uiCam = new SimulateurCamera(h, w, 640);
		uiCam.center();
		uiCam.update();
		world2D = new World2D(h, w);
		world3D = new BulletWorld();
	}

	@Override
	public void render () {
		uiCam.update();
		batch.setProjectionMatrix(uiCam.combined);
		if(progress < 100) {
			progress += (int) (Math.random()*3);
			progress = (progress > 100) ? 100 : progress;
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 70, 100);
		//batch.end();
		if(progress >= 100) {
			world3D.update();
		}
		//sr.setProjectionMatrix(uiCam.combined);
		//sr.begin(ShapeType.Filled);
		//sr.setColor(Color.PINK);
		//sr.rect(30, 80, progress*uiCam.viewportWidth/100-60, 20);
		//sr.end();
		//world2D.update();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		world3D.dispose();
	}
}
