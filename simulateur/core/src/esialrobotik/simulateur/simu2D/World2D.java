package esialrobotik.simulateur.simu2D;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import esialrobotik.simulateur.camera.SimulateurCamera;
import esialrobotik.simulateur.simu2D.object.Ball2D;
import esialrobotik.simulateur.simu2D.object.Ground2D;

/**
 * Simple 2D World with balls that fall down
 * @author Clement
 *
 */
public class World2D {
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private SimulateurCamera camera;

	public World2D(float h, float w) {
		Box2D.init();
		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new SimulateurCamera(h, w, 2);
		camera.center();
		camera.update();
		// Create 42 balls
		for (int i = 0; i < 42; i++) {
			Ball2D ball = new Ball2D(camera.viewportWidth, camera.viewportHeight, 0.03f, world);
		}
		// Create the ground	
		Ground2D ground = new Ground2D(camera.viewportWidth, camera.viewportHeight, world);
	}
	
	public void update() {
		world.step(1/45f, 6, 2);
		camera.update();
		debugRenderer.render(world, camera.combined);
	}
}
