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

public class CubeConstruction implements BulletObject{
	private Model model;
	private ModelInstance instance;
	private btCollisionShape boxShape;
	private btRigidBodyConstructionInfo boxInfo;
	private btDefaultMotionState boxMotionState;
	private btRigidBody boxBody;
	private Vector3 tempVector = new Vector3();

	public CubeConstruction() {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5.8f, 5.8f, 5.8f, 
				new Material(ColorAttribute.createDiffuse(Color.PINK),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.trn(100f, 300f, 150f);
		instance.transform.rotate(Vector3.X, 42f);
		instance.transform.rotate(Vector3.Z, 42f);
		boxShape = new btBoxShape(new Vector3(2.9f, 2.9f, 2.9f));
		boxShape.calculateLocalInertia(1f, tempVector);
		boxInfo = new btRigidBodyConstructionInfo(1f, null, boxShape, tempVector);
		boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		boxBody = new btRigidBody(boxInfo);
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
	}
}
