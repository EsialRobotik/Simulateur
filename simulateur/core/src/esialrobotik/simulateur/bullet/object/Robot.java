package esialrobotik.simulateur.bullet.object;

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

public class Robot extends BulletObject{
	private static float size = 30f;
	private int move = 10;
	private Vector3 moveit;

	public Robot() {
		super();
		moveit = new Vector3(0f, 0f, 0f);
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(size, size, size, 
				new Material(ColorAttribute.createDiffuse(Color.PINK),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		ModelInstance instance = new ModelInstance(model);
		instance.transform.trn(size/2f, size/2f, size/2f);
		btBoxShape boxShape = new btBoxShape(new Vector3(size/2f, size/2f, size/2f));
		boxShape.calculateLocalInertia(1f, tempVector);
		btRigidBodyConstructionInfo boxInfo = new btRigidBodyConstructionInfo(1f, null, boxShape, tempVector);
		btDefaultMotionState boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		btRigidBody boxBody = new btRigidBody(boxInfo);
		boxBody.setMotionState(boxMotionState);
		addInstance(instance, boxShape, boxInfo, boxMotionState, boxBody);
	}
	@Override
	public void motion() {
		super.motion();
		move -= 1;
		if(move < 0) {
			moveit = new Vector3(-50f+(float)Math.random()*100f, 0f, -50f+(float)Math.random()*100f);
			super.bodies.get(0).applyCentralImpulse(moveit);
			move = 1;
		}
	}
}
