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

public class Table2 implements BulletObject{
	private Model model;
	private ModelInstance instance;
	private btCollisionShape boxShape;
	private btRigidBodyConstructionInfo boxInfo;
	private btDefaultMotionState boxMotionState;
	private btRigidBody boxBody;

	public Table2() {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(2.150f, 0.10f, 0.022f, 
				new Material(ColorAttribute.createDiffuse(Color.RED),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		boxShape = new btBoxShape(new Vector3(1.075f, 0.05f, 0.011f));
		instance.transform.translate(1.075f, 0.05f, 3.021f);
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
