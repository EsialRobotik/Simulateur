package esialrobotik.simulateur.bullet.object.chateaudeau;

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

	public RebordsLargeur(float sizeX, float sizeZ, float hauteur, float epaisseur, float posX, float posZ, Color c, boolean left) {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(sizeX, hauteur, epaisseur, 
				new Material(ColorAttribute.createDiffuse(c),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		// Largeur 1
		ModelInstance instance = new ModelInstance(model);
		btBoxShape boxShape = new btBoxShape(new Vector3(sizeX/2f, hauteur/2f, epaisseur/2f));
		instance.transform.translate(sizeX/2f, hauteur/2f, epaisseur/2f);
		if(left) {
			instance.transform.trn(posX, 0f, posZ);
		}else {
			instance.transform.scale(1f, 1.5f, 1f);
			instance.transform.trn(posX, 10f, posZ);
		}
		btRigidBodyConstructionInfo boxInfo = new btRigidBodyConstructionInfo(0f, null, boxShape, Vector3.Zero);
		btDefaultMotionState boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		btRigidBody boxBody = new btRigidBody(boxInfo);
		boxBody.setMotionState(boxMotionState);
		addInstance(instance, boxShape, boxInfo, boxMotionState, boxBody);
		// Largeur 2
		ModelInstance instance2 = new ModelInstance(model);
		instance2.transform.translate(sizeX/2f, hauteur/2f, sizeZ-epaisseur/2f);
		if(!left) {
			instance2.transform.trn(posX, 0f, posZ);
		}else {
			instance2.transform.scale(1f, 1.5f, 1f);
			instance2.transform.trn(posX, 10f, posZ);
		}
		btDefaultMotionState boxMotionState2 = new btDefaultMotionState();
		boxMotionState2.setWorldTransform(instance2.transform);
		btRigidBody boxBody2 = new btRigidBody(boxInfo);
		boxBody2.setMotionState(boxMotionState2);
		addInstance(instance2, boxShape, boxInfo, boxMotionState2, boxBody2);
	}
}
