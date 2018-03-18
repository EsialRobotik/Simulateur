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
	private Vector3 [] lastCod = {new Vector3(), new Vector3()}; 
	private float [] tick = {0f, 0f};
	private float [] consigne = {60f, 60f};
	private float [] errorCod = {0f, 0f};
	private float [] sumErrorCod = {0f, 0f};
	private float [] lastErrorCod = {0f, 0f};
	private float [] commandeCod = {0f, 0f};
	private int stable = 0;
	private final float Kp = 10f;
	private final float Ki = 0.001f;
	private final float Kd = 100f;
	private final float erreurAdmissible = 0.001f;
	private final int asservStable = 300;
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
		instance.transform.trn(size/2f, size/2f, size/2f);
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
	
	private Vector3 positionCodeuse(Vector3 positionRobot, boolean left) {
		float offset = left ? entraxeCod : -entraxeCod;
		Vector3 pos = new Vector3(offset+codOffsetX, codOffsetY, codOffsetZ);
		float orientation = super.bodies.get(0).getOrientation().getAngle();
		pos.rotate(Vector3.Y, orientation);
		pos.add(positionRobot);
		return pos;
	}
	
	private void updatePositions() {
		lastPosition = super.bodies.get(0).getCenterOfMassPosition();
		lastCod[0] = positionCodeuse(lastPosition, false);
		lastCod[1] = positionCodeuse(lastPosition, true);
	}
	
	private Vector3 getDirection() {
		float orientation = super.bodies.get(0).getOrientation().getAngle()+270f;
		return new Vector3(Vector3.X).rotate(Vector3.Y, orientation);
	}
	
	private void updateTicks() {
		Vector3 lastL = lastCod[0].cpy();
		Vector3 lastR = lastCod[1].cpy();
		updatePositions();
		Vector3 direction = getDirection();
		float deltaTickLeft = lastL.dst2(lastCod[0]);
		float deltaTickRight = lastR.dst2(lastCod[1]);
		boolean invertDirLeft = direction.hasSameDirection(lastCod[0].cpy().sub(lastL));
		boolean invertDirRight = direction.hasSameDirection(lastCod[1].cpy().sub(lastR));
		tick[0] += (invertDirLeft ? 1f : -1f) * deltaTickLeft*10f;
		tick[1] += (invertDirRight ? 1f : -1f) * deltaTickRight*10f;
	}
	
	public void resetAll() {
		for (int i = 0; i < commandeCod.length; i++) {
			tick[i] = 0f;
			sumErrorCod[i] = 0f;
			commandeCod[i] = 0f;
			lastErrorCod[i] = errorCod[i];
		}
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
			return true;
		}
		for (int i = 0; i < commandeCod.length; i++) {
			float error = consigne[i] - tick[i];
			errorCod[i] = (error < erreurAdmissible && error > erreurAdmissible) ? 0f : error;
			sumErrorCod[i] += errorCod[i];
			commandeCod[i] = Kp * errorCod[i] + Ki * sumErrorCod[i] + Kd * (errorCod[i] - lastErrorCod[i]);
			lastErrorCod[i] = errorCod[i];
		}
		if(Math.abs(errorCod[0]) <= 0.2f && Math.abs(errorCod[1]) <= 0.2f) {
			stable += 1;
		}
		return false;
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
			if(initFrames%5==0) {
				//super.bodies.get(0).setAngularVelocity(Vector3.Zero);
				//super.bodies.get(0).setLinearVelocity(Vector3.Zero);
				asserv();
				for (int i = 0; i < 2; i++) {
					moveit = new Vector3(commandeCod[i], 0f, 0f);
					moveit.rotate(Vector3.Y, super.bodies.get(0).getOrientation().getAngle()+270f);
					super.bodies.get(0).applyForce(moveit, lastCod[i].cpy().sub(lastPosition));
				}
				System.out.println(lastErrorCod[0]);
			}
		}
	}
}
