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

public class RebordsLargeur extends BulletObject{

	public RebordsLargeur(float tableSizeX, float tableSizeZ, float hauteurRebord, float epaisseurRebord) {
		float sizeX = tableSizeX;
		float sizeY = hauteurRebord;
		float sizeZ = epaisseurRebord;
		float tableZ = tableSizeZ;
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(sizeX, sizeY, sizeZ, 
				new Material(ColorAttribute.createDiffuse(Color.GRAY),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		// Largeur 1
		ModelInstance instance = new ModelInstance(model);
		btBoxShape boxShape = new btBoxShape(new Vector3(sizeX/2f, sizeY/2f, sizeZ/2f));
		instance.transform.translate(sizeX/2f, sizeY/2f, sizeZ/2f);
		btRigidBodyConstructionInfo boxInfo = new btRigidBodyConstructionInfo(0f, null, boxShape, Vector3.Zero);
		btDefaultMotionState boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		btRigidBody boxBody = new btRigidBody(boxInfo);
		boxBody.setMotionState(boxMotionState);
		addInstance(instance, boxShape, boxInfo, boxMotionState, boxBody);
		// Largeur 2
		ModelInstance instance2 = new ModelInstance(model);
		instance2.transform.translate(sizeX/2f, sizeY/2f, tableZ);
		btDefaultMotionState boxMotionState2 = new btDefaultMotionState();
		boxMotionState2.setWorldTransform(instance2.transform);
		btRigidBody boxBody2 = new btRigidBody(boxInfo);
		boxBody2.setMotionState(boxMotionState2);
		addInstance(instance2, boxShape, boxInfo, boxMotionState2, boxBody2);
	}
}
