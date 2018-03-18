package esialrobotik.simulateur.bullet.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btQuaternion;

public class Robot extends BulletObject{
	private static float size = 30f;
	private Vector3 moveit = new Vector3(100f, 0, 100f);

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
		Vector3 position = super.bodies.get(0).getCenterOfMassPosition();
		moveit = new Vector3(40f, 15f, 40f);
		Vector3 delta = moveit.lerp(position, 0.1f);
		System.out.println("delta");
		System.out.println(delta);
		System.out.println("position");
		System.out.println(position);
		System.out.println("moveit");
		System.out.println(moveit);
		Vector3 force = delta.sub(position).scl(25f).limit(100f);
		if (force.len() > 0.1f && position.x < 35f) {
			super.bodies.get(0).applyCentralForce(force);
		} else {
			Quaternion angular = super.bodies.get(0).getOrientation();
			Quaternion expectedAngle = new Quaternion(Vector3.Y, 90f);
			Quaternion delta1 = expectedAngle.mul(angular.mul(-1f));
			btQuaternion delta2 = new btQuaternion(delta1.x, delta1.y, delta1.z, delta1.w);
			System.out.println("delta2");
			System.out.println(delta2);
			System.out.println("angular");
			System.out.println(angular);
			System.out.println("moveit");
			System.out.println(moveit);
			super.bodies.get(0).setAngularVelocity(delta2.getAxis());
		}
	}
}
