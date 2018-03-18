package esialrobotik.simulateur.bullet.object.table;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class Plateau extends BulletObject {

	public Plateau(float x, float z) {
		super();
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createRect(
				-x/2f,
				0f,
				-z/2f,
				-x/2f,
				0f,
				z/2f,
				x/2f,
				0f,
				z/2f,
				x/2f,
				0f,
				-z/2f,
				0,
				1,
				0,
				new Material(ColorAttribute.createDiffuse(Color.GRAY),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		ModelInstance instance = new ModelInstance(model);
		instance.transform.translate(new Vector3(x/2f, 0f, z/2f));
		btBoxShape groundShape = new btBoxShape(new Vector3(x/2f, 0f, z/2f));
		btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
		btDefaultMotionState groundMotionState = new btDefaultMotionState();
		groundMotionState.setWorldTransform(instance.transform);
		btRigidBody groundBody = new btRigidBody(groundInfo);
		groundBody.setMotionState(groundMotionState);
		groundBody.setRestitution(1f);
		groundBody.setFriction(0f);
		addInstance(instance, groundShape, groundInfo, groundMotionState, groundBody);
	}

}
