package esialrobotik.simulateur.bullet;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import esialrobotik.simulateur.bullet.object.Box;
import esialrobotik.simulateur.bullet.object.BulletObject;
import esialrobotik.simulateur.bullet.object.Ground;

public class BulletWorld {
	private PerspectiveCamera cam;
	private List<BulletObject> objects;
	private ModelBatch modelBatch;
	private Environment environment;
	public CameraInputController camController;
	btCollisionConfiguration collisionConfiguration;
	btCollisionDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btConstraintSolver solver;
	btDynamicsWorld collisionWorld;
	Vector3 gravity = new Vector3(0, -9.81f, 0);
	Vector3 tempVector = new Vector3();
	
	public BulletWorld() {
		Bullet.init();
		modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 50f, 0f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        // Create the box
        objects = new LinkedList<BulletObject>();
        objects.add(new Box());
        objects.add(new Ground());
        // Create environment (lights)
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        // Controller camera
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    	// Create the bullet world
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		broadphase = new btDbvtBroadphase();
		solver = new btSequentialImpulseConstraintSolver();
		collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		collisionWorld.setGravity(gravity);
		for (BulletObject bulletObject : objects) {
			if(bulletObject.getRigidBody() != null)
				collisionWorld.addRigidBody(bulletObject.getRigidBody());
		}
	}

	public void dispose() {
		for (BulletObject bulletObject : objects) {
			bulletObject.dispose();
		}
		modelBatch.dispose();
		collisionWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfiguration.dispose();
	}

	public void update() {
		collisionWorld.debugDrawWorld();
		((btDynamicsWorld)collisionWorld).stepSimulation(Gdx.graphics.getDeltaTime(), 1000);
        modelBatch.begin(cam);
        for (BulletObject bulletObject : objects) {
            modelBatch.render(bulletObject.getInstance(), environment);
            bulletObject.motion();
		}
        modelBatch.end();
        camController.update();
	}
}
