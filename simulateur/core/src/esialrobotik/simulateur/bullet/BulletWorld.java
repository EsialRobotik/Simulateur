package esialrobotik.simulateur.bullet;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import esialrobotik.simulateur.bullet.object.BulletObject;
import esialrobotik.simulateur.bullet.object.CubeConstruction;
import esialrobotik.simulateur.bullet.object.Robot;
import esialrobotik.simulateur.bullet.object.chateaudeau.ChateauDEau;
import esialrobotik.simulateur.bullet.object.eau.EauUsee;
import esialrobotik.simulateur.bullet.object.eau.Loquet;
import esialrobotik.simulateur.bullet.object.eau.Tubes;
import esialrobotik.simulateur.bullet.object.table.Table;

public class BulletWorld {
	private Camera cam;
	private List<BulletObject> objects;
	private ModelBatch modelBatch;
	private Environment environment;
	public CameraInputController camController;
	btCollisionConfiguration collisionConfiguration;
	btCollisionDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btConstraintSolver solver;
	btDynamicsWorld collisionWorld;
	Vector3 gravity = new Vector3(0, -981f, 0);
	Vector3 tempVector = new Vector3();
	private boolean perspective = true;
	
	public BulletWorld() {
		Bullet.init();
		modelBatch = new ModelBatch();
		if(perspective) {
			cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.position.set(0f, 250f, 0f);
		}else {
			cam = new OrthographicCamera(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
			cam.position.set(107.5f, 250f, 150.5f);
	        cam.rotateAround(cam.position, Vector3.Y, -90f);
		}
        cam.lookAt(107.5f, 0f, 150.5f);
        cam.near = 10f;
        cam.far = 500f;
        cam.update();
        // Create the box
        objects = new LinkedList<BulletObject>();
        objects.add(new Robot());
        objects.add(new ChateauDEau());
        objects.add(new EauUsee(Color.ORANGE));
        objects.add(new CubeConstruction());
        objects.add(new Table());
        objects.add(new Tubes(3.1177f, 35));
        objects.add(new Loquet());
        // Create environment (lights)
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        // Controller camera
        camController = new CameraInputController(cam);
        camController.scrollFactor = 2f;
        camController.target = new Vector3(107.5f, 0f, 150.5f);
        Gdx.input.setInputProcessor(camController);
    	// Create the bullet world
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		broadphase = new btDbvtBroadphase();
		solver = new btSequentialImpulseConstraintSolver();
		collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		collisionWorld.setGravity(gravity);
		for (BulletObject bulletObject : objects) {
			if(bulletObject.getRigidBody() != null) {
				for (btRigidBody body : bulletObject.getRigidBody()) {
					collisionWorld.addRigidBody(body);					
				}
			}
		}
	}

	public void dispose() {
		modelBatch.dispose();
		collisionWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfiguration.dispose();
		for (BulletObject bulletObject : objects) {
			bulletObject.dispose();
		}
	}

	public void update() {
		//((btDynamicsWorld)collisionWorld).stepSimulation(Gdx.graphics.getDeltaTime()*0.5f,1);
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		((btDynamicsWorld)collisionWorld).stepSimulation(delta, 100, 1f/600f);
        modelBatch.begin(cam);
        for (BulletObject bulletObject : objects) {
            modelBatch.render(bulletObject.getInstance(), environment);
            bulletObject.motion();
		}
        modelBatch.end();
        camController.update();
	}
}
