package esialrobotik.simulateur.bullet.object.eau;

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

public class Loquet extends BulletObject{

	public Loquet() {
		float sizeX = 2.8f;
		float sizeY = 2.8f;
		float sizeZ = 10f;
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(sizeX, sizeY, sizeZ, 
				new Material(ColorAttribute.createDiffuse(Color.GRAY),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		// Loquet 1
		ModelInstance instance = new ModelInstance(model);
		btBoxShape boxShape = new btBoxShape(new Vector3(sizeX/2f, sizeY/2f, sizeZ/2f));
		instance.transform.translate(84f, 24.2f, sizeZ/2f+0.2f);
		btRigidBodyConstructionInfo boxInfo = new btRigidBodyConstructionInfo(0f, null, boxShape, Vector3.Zero);
		btDefaultMotionState boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		btRigidBody boxBody = new btRigidBody(boxInfo);
		boxBody.setMotionState(boxMotionState);
		addInstance(instance, boxShape, boxInfo, boxMotionState, boxBody);
		// Loquet 2
		ModelInstance instance2 = new ModelInstance(model);
		btBoxShape boxShape2 = new btBoxShape(new Vector3(sizeX/2f, sizeY/2f, sizeZ/2f));
		instance2.transform.translate(84f, 24.2f, -sizeZ/2f+300.8f);
		btRigidBodyConstructionInfo boxInfo2 = new btRigidBodyConstructionInfo(0f, null, boxShape2, Vector3.Zero);
		btDefaultMotionState boxMotionState2 = new btDefaultMotionState();
		boxMotionState2.setWorldTransform(instance2.transform);
		btRigidBody boxBody2 = new btRigidBody(boxInfo2);
		boxBody2.setMotionState(boxMotionState2);
		addInstance(instance2, boxShape2, boxInfo2, boxMotionState2, boxBody2);
		// Loquet 3
		ModelInstance instance3 = new ModelInstance(model);
		btBoxShape boxShape3 = new btBoxShape(new Vector3(sizeX/2f, sizeY/2f, sizeZ/2f));
		instance3.transform.translate(200.2f+sizeX/2f, 24.2f, 61f);
		instance3.transform.rotate(Vector3.Y, 90);
		btRigidBodyConstructionInfo boxInfo3 = new btRigidBodyConstructionInfo(0f, null, boxShape3, Vector3.Zero);
		btDefaultMotionState boxMotionState3 = new btDefaultMotionState();
		boxMotionState3.setWorldTransform(instance3.transform);
		btRigidBody boxBody3 = new btRigidBody(boxInfo3);
		boxBody3.setMotionState(boxMotionState3);
		addInstance(instance3, boxShape3, boxInfo3, boxMotionState3, boxBody3);
		// Loquet 4
		ModelInstance instance4 = new ModelInstance(model);
		btBoxShape boxShape4 = new btBoxShape(new Vector3(sizeX/2f, sizeY/2f, sizeZ/2f));
		instance4.transform.translate(200.2f+sizeX/2f, 24.2f, 239f);
		instance4.transform.rotate(Vector3.Y, 90);
		btRigidBodyConstructionInfo boxInfo4 = new btRigidBodyConstructionInfo(0f, null, boxShape4, Vector3.Zero);
		btDefaultMotionState boxMotionState4 = new btDefaultMotionState();
		boxMotionState4.setWorldTransform(instance4.transform);
		btRigidBody boxBody4 = new btRigidBody(boxInfo4);
		boxBody4.setMotionState(boxMotionState4);
		addInstance(instance4, boxShape4, boxInfo4, boxMotionState4, boxBody4);
	}
}
