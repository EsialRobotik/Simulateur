package esialrobotik.simulateur.bullet.object.table;

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
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class Table4 implements BulletObject{
	private Model model;
	private ModelInstance instance;
	private btCollisionShape boxShape;
	private btRigidBodyConstructionInfo boxInfo;
	private btDefaultMotionState boxMotionState;
	private btRigidBody boxBody;

	public Table4() {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(0.022f, 0.10f, 3.010f, 
				new Material(ColorAttribute.createDiffuse(Color.RED),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		boxShape = new btBoxShape(new Vector3(0.011f, 0.05f, 1.505f));
		instance.transform.translate(2.150f, 0.05f, 1.505f);
		boxInfo = new btRigidBodyConstructionInfo(0f, null, boxShape, Vector3.Zero);
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
