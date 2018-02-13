package esialrobotik.simulateur.bullet.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

public class Ground implements BulletObject {
	
	private Model model;
	private ModelInstance instance;
	private btCollisionShape groundShape;
	private btRigidBodyConstructionInfo groundInfo;
	private btDefaultMotionState groundMotionState;
	private btRigidBody groundBody;

	public Ground() {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createRect(
				20f,
				0f,
				-20f,
				-20f,
				0f,
				-20f,
				-20f,
				0f,
				20f,
				20f,
				0f,
				20f,
				0,
				1,
				0,
				new Material(ColorAttribute.createDiffuse(Color.BLUE),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		// Create the shapes and body construction infos
		Vector3 tempVector = new Vector3();
		groundShape = new btBoxShape(tempVector.set(20, 0, 20));
		groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
		groundMotionState = new btDefaultMotionState();
		groundMotionState.setWorldTransform(instance.transform);
		groundBody = new btRigidBody(groundInfo);
		groundBody.setMotionState(groundMotionState);
	}

	@Override
	public void dispose() {
		model.dispose();
		groundShape.dispose();
		groundInfo.dispose();
		groundMotionState.dispose();
		groundBody.dispose();
	}

	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	@Override
	public btRigidBody getRigidBody() {
		return groundBody;
	}

	@Override
	public void motion() {
		this.groundMotionState.getWorldTransform(this.instance.transform);
	}

}
