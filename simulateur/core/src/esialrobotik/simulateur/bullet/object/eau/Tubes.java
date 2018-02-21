package esialrobotik.simulateur.bullet.object.eau;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class Tubes extends BulletObject {

	public Tubes(float x, float y) {
		super();
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createRect(
				-x/2f,
				-y/2f,
				0f,
				-x/2f,
				y/2f,
				0f,
				x/2f,
				y/2f,
				0f,
				x/2f,
				-y/2f,
				0f,
				0,
				0,
				1,
				new Material(ColorAttribute.createDiffuse(Color.RED)),
				Usage.Position | Usage.Normal);
		for (int i = 0; i < 6; i++) {
			ModelInstance instance = new ModelInstance(model);
			instance.transform.translate(new Vector3((float)Math.sin(Math.toRadians(60*i))*2.7f, y/2f+27f, (float)Math.cos(Math.toRadians(60*i))*2.7f));
			instance.transform.rotate(Vector3.Y, 60*i);
			instance.transform.rotate(new Vector3((float)Math.cos(Math.toRadians(60*i)), 0f, (float)Math.sin(Math.toRadians(60*i))), -10);
			instance.transform.trn(84f, 0f, 4.4f);
			btBoxShape groundShape = new btBoxShape(new Vector3(x/2f, y/2f, 0f));
			btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
			btDefaultMotionState groundMotionState = new btDefaultMotionState();
			groundMotionState.setWorldTransform(instance.transform);
			btRigidBody groundBody = new btRigidBody(groundInfo);
			groundBody.setMotionState(groundMotionState);
			groundBody.setRestitution(0.8f);
			groundBody.setFriction(0.8f);
			addInstance(instance, groundShape, groundInfo, groundMotionState, groundBody);
		}
		for (int i = 0; i < 6; i++) {
			ModelInstance instance = new ModelInstance(model);
			instance.transform.translate(new Vector3((float)Math.sin(Math.toRadians(60*i))*2.7f, y/2f+27f, (float)Math.cos(Math.toRadians(60*i))*2.7f));
			instance.transform.rotate(Vector3.Y, 60*i);
			instance.transform.rotate(new Vector3((float)Math.cos(Math.toRadians(60*i)), 0f, (float)Math.sin(Math.toRadians(60*i))), 10);
			instance.transform.trn(84f, 0f, 301-4.4f);
			btBoxShape groundShape = new btBoxShape(new Vector3(x/2f, y/2f, 0f));
			btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
			btDefaultMotionState groundMotionState = new btDefaultMotionState();
			groundMotionState.setWorldTransform(instance.transform);
			btRigidBody groundBody = new btRigidBody(groundInfo);
			groundBody.setMotionState(groundMotionState);
			groundBody.setRestitution(0.8f);
			groundBody.setFriction(0.8f);
			addInstance(instance, groundShape, groundInfo, groundMotionState, groundBody);
		}
		for (int i = 0; i < 6; i++) {
			ModelInstance instance = new ModelInstance(model);
			instance.transform.translate(new Vector3((float)Math.sin(Math.toRadians(60*i))*2.7f, y/2f+27f, (float)Math.cos(Math.toRadians(60*i))*2.7f));
			instance.transform.rotate(Vector3.Y, 60*i);
			instance.transform.rotate(new Vector3(-(float)Math.sin(Math.toRadians(60*i)), 0f, (float)Math.cos(Math.toRadians(60*i))), -10);
			instance.transform.trn(200f+4.4f, 0f, 61f);
			btBoxShape groundShape = new btBoxShape(new Vector3(x/2f, y/2f, 0f));
			btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
			btDefaultMotionState groundMotionState = new btDefaultMotionState();
			groundMotionState.setWorldTransform(instance.transform);
			btRigidBody groundBody = new btRigidBody(groundInfo);
			groundBody.setMotionState(groundMotionState);
			groundBody.setRestitution(0.8f);
			groundBody.setFriction(0.8f);
			addInstance(instance, groundShape, groundInfo, groundMotionState, groundBody);
		}
		for (int i = 0; i < 6; i++) {
			ModelInstance instance = new ModelInstance(model);
			instance.transform.translate(new Vector3((float)Math.sin(Math.toRadians(60*i))*2.7f, y/2f+27f, (float)Math.cos(Math.toRadians(60*i))*2.7f));
			instance.transform.rotate(Vector3.Y, 60*i);
			instance.transform.rotate(new Vector3(-(float)Math.sin(Math.toRadians(60*i)), 0f, (float)Math.cos(Math.toRadians(60*i))), -10);
			instance.transform.trn(200f+4.4f, 0f, 239f);
			btBoxShape groundShape = new btBoxShape(new Vector3(x/2f, y/2f, 0f));
			btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
			btDefaultMotionState groundMotionState = new btDefaultMotionState();
			groundMotionState.setWorldTransform(instance.transform);
			btRigidBody groundBody = new btRigidBody(groundInfo);
			groundBody.setMotionState(groundMotionState);
			groundBody.setRestitution(0.8f);
			groundBody.setFriction(0.8f);
			addInstance(instance, groundShape, groundInfo, groundMotionState, groundBody);
		}
	}

}
