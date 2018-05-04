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
	private static final float size = 30f;
	private int move = 10;
	private Vector3 moveit;
	private Vector3 lastPosition = new Vector3(size/2f, 0f, size/2f);
	private static final float entraxeCod = 15f;
	private static final float codOffsetY = 0f;
	private static final float codOffsetX = 0f;
	private static final float codOffsetZ = 0f;
	private float tick = 0f;
	private float consigne = 30f;
	private float errorCod = 0f;
	private float sumErrorCod = 0f;
	private float lastErrorCod = 0f;
	private float commandeCod = 0f;
	private int stable = 0;
	private final float Kp = 100f;
	private final float Ki = 0.001f;
	private final float Kd = 50f;
	private final float erreurAdmissible = 0.0001f;
	private final int asservStable = 1;
	private static int initFrames = 50; // Wait for 10 frames before doing anything

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
		instance.transform.trn(100f+size/2f, size/2f, 50f+size/2f);
		instance.transform.rotate(Vector3.Y, 45);
		btBoxShape boxShape = new btBoxShape(new Vector3(size/2f, size/2f, size/2f));
		boxShape.calculateLocalInertia(1f, tempVector);
		btRigidBodyConstructionInfo boxInfo = new btRigidBodyConstructionInfo(1f, null, boxShape, tempVector);
		btDefaultMotionState boxMotionState = new btDefaultMotionState();
		boxMotionState.setWorldTransform(instance.transform);
		btRigidBody boxBody = new btRigidBody(boxInfo);
		boxBody.setMotionState(boxMotionState);
		boxBody.setFriction(0f);
		addInstance(instance, boxShape, boxInfo, boxMotionState, boxBody);
	}
	
	private void updatePositions() {
		lastPosition = super.bodies.get(0).getCenterOfMassPosition().cpy();
	}
	
	private float getDirection() {
		return super.bodies.get(0).getOrientation().getAngle();
	}
	
	private void updateTicks() {
		//updatePositions();
		tick = (consigne < 0 ? -1f : 1f) * super.bodies.get(0).getCenterOfMassPosition().dst(lastPosition);
	}
	
	public void resetAll() {
		tick = 0f;
		sumErrorCod = 0f;
		commandeCod = 0f;
		lastErrorCod = errorCod;
		updatePositions();
	}
	
	public boolean asserv() {
		/** Asservissement
		 * erreur = consigne - mesure;
		 * somme_erreurs += erreur;
		 * variation_erreur = erreur - erreur_précédente;
		 * commande = Kp * erreur + Ki * somme_erreurs + Kd * variation_erreur;
		 * erreur_précédente = erreur
		 **/
		if(stable >= asservStable) {
			resetAll();
			move(Vector3.Zero);
			return true;
		}
		float error = consigne - tick;
		errorCod = (error < erreurAdmissible && error > erreurAdmissible) ? 0f : error;
		sumErrorCod += errorCod;
		commandeCod = Kp * errorCod + Ki * sumErrorCod + Kd * (errorCod - lastErrorCod);
		lastErrorCod = errorCod;
		if(Math.abs(errorCod) <= 0.2f) {
			stable += 1;
		}
		return false;
	}
	
	private void move(Vector3 nextPosition) {
		if(moveit.len2() <= 10) {
			super.bodies.get(0).setAngularVelocity(Vector3.Zero);
			super.bodies.get(0).setLinearVelocity(Vector3.Zero);
		}else {
			super.bodies.get(0).applyCentralImpulse(moveit);
		}
		super.bodies.get(0).setLinearVelocity(super.bodies.get(0).getLinearVelocity().limit2(1000.0f));
	}
	
	@Override
	public void motion() {
		super.motion();
		if(initFrames > 0) {
			updateTicks();
			asserv();
			initFrames--;
		}else if (initFrames == 0) {
			resetAll();
			initFrames--;
		}else {
			initFrames--;
			updateTicks();
			if(true || initFrames%5==0) {
				if(asserv()) {
					consigne = consigne == 30f ? -30f : 30f;
					stable = 0;
					System.out.println(consigne);
				}
				moveit = new Vector3(commandeCod, 0f, 0f);
				moveit.rotate(Vector3.Y, getDirection());
				move(moveit);
			}
		}
	}
}
