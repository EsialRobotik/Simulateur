package esialrobotik.simulateur.bullet.object.eau;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class EauUsee implements BulletObject{
	private Model model;
	private ModelInstance instance;
	private btCollisionShape boxShape;
	private btRigidBodyConstructionInfo boxInfo;
	private btDefaultMotionState boxMotionState;
	private btRigidBody boxBody;
	private Vector3 tempVector = new Vector3();

	public EauUsee(Color c) {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createSphere(4.4f, 4.4f, 4.4f, 10, 10,
				new Material(ColorAttribute.createDiffuse(c),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.trn(3f+MathUtils.random()*50f, 27f, 3f+MathUtils.random()*50f);
		boxShape = new btSphereShape(2.2f);
		boxShape.calculateLocalInertia(0.020f, tempVector);
		boxInfo = new btRigidBodyConstructionInfo(0.020f, null, boxShape, tempVector);
		boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		boxBody = new btRigidBody(boxInfo);
		//boxBody.setAngularVelocity(new Vector3(MathUtils.random(0f, 0.1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f)));
		boxBody.setRestitution(1f);
		//boxBody.setFriction(0.0008f);
		boxBody.setMotionState(boxMotionState);
	}
	
	@Override
	public void dispose() {
		model.dispose();
		boxShape.dispose();
		boxInfo.dispose();
		boxMotionState.dispose();
		boxBody.dispose();
	}

	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	@Override
	public btRigidBody getRigidBody() {
		return boxBody;
	}

	@Override
	public void motion() {
		this.boxMotionState.getWorldTransform(this.instance.transform);
		instance.transform.getTranslation(tempVector);
		if(tempVector.y < -10f) {
			boxBody.setMassProps(0, Vector3.Zero);
		}
	}
}
