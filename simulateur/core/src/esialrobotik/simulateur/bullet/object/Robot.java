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
	private Vector3 moveit;
	private Vector3 rotit;
	
	// Asservissement Position
	private final float posKp = 100f;
	private final float posKi = 0.001f;
	private final float posKd = 50f;
	private final float posErreurAdmissible = 0.0001f;
	private final int posAsservStable = 1;
	private Vector3 posLast = new Vector3(size/2f, 0f, size/2f);
	private float posTick = 0f;
	private float posConsigne = 30f;
	private float posError = 0f;
	private float posSumError = 0f;
	private float posLastError = 0f;
	private float posCommande = 0f;
	private int posStable = 0;
	// Asservissement Angle
	private final float angKp = 100f;
	private final float angKi = 0.001f;
	private final float angKd = 50f;
	private final float angErreurAdmissible = 0.0001f;
	private final int angAsservStable = 1;
	private float angLast = 0f;
	private float angTick = 0f;
	private float angConsigne = -360f;
	private float angError = 0f;
	private float angSumError = 0f;
	private float angLastError = 0f;
	private float angCommande = 0f;
	private int angStable = 0;
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
		posLast = super.bodies.get(0).getCenterOfMassPosition().cpy();
		angLast = getDirection();
	}
	
	private float getDirection() {
		return super.bodies.get(0).getOrientation().getAngle();
	}
	
	private void updateTicks() {
		//updatePositions();
		posTick = (posConsigne < 0 ? -1f : 1f) * super.bodies.get(0).getCenterOfMassPosition().dst(posLast);
		angTick = (super.bodies.get(0).getOrientation().getYaw() - angLast)%360f;//*(angConsigne < 0 ? -1f : 1f))%360f;
		//Math.
	}
	
	public void resetAll() {
		posTick = 0f;
		posSumError = 0f;
		posCommande = 0f;
		posLastError = posError;
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
		if(posStable >= posAsservStable) {
			resetAll();
			move(Vector3.Zero);
			return true;
		}
		float error = posConsigne - posTick;
		posError = (error < posErreurAdmissible && error > posErreurAdmissible) ? 0f : error;
		posSumError += posError;
		posCommande = posKp * posError + posKi * posSumError + posKd * (posError - posLastError);
		posLastError = posError;
		if(Math.abs(posError) <= 0.2f) {
			posStable += 1;
		}
		error = angConsigne - angTick;
		angError = (error < angErreurAdmissible && error > angErreurAdmissible) ? 0f : error;
		angSumError += angError;
		angCommande = angKp * angError + angKi * angSumError + angKd * (angError - angLastError);
		angLastError = angError;
		if(Math.abs(angError) <= 0.2f) {
			angStable += 1;
		}
		return false;
	}
	
	private void move(Vector3 nextPosition) {
		if(nextPosition.len2() <= 10) {
			super.bodies.get(0).setLinearVelocity(Vector3.Zero);
		}else {
			super.bodies.get(0).applyCentralImpulse(nextPosition);
		}
		super.bodies.get(0).setLinearVelocity(super.bodies.get(0).getLinearVelocity().limit2(1000.0f));
	}
	
	private void rot(float nextAngle) {
		if(Math.abs(nextAngle) <= 1) {
			super.bodies.get(0).setAngularVelocity(Vector3.Zero);
		}else {
			Vector3 torque = new Vector3(0, nextAngle, 0);
			super.bodies.get(0).applyTorqueImpulse(torque);
		}
		super.bodies.get(0).setAngularVelocity(super.bodies.get(0).getAngularVelocity().limit2(10.0f));
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
					posConsigne = posConsigne == 30f ? -30f : 30f;
					posStable = 0;
					System.out.println(posConsigne);
				}
				System.out.println(angError);
				moveit = new Vector3(posCommande, 0f, 0f);
				moveit.rotate(Vector3.Y, getDirection());
				move(moveit);
				//rotit = new Vector3(0f, angCommande, 0f);
				rot(angCommande);
			}
		}
	}
}
