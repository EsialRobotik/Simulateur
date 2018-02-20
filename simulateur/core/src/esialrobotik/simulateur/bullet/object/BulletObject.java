package esialrobotik.simulateur.bullet.object;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class BulletObject {
	protected Model model;
	protected List<ModelInstance> instances;
	protected List<btCollisionShape> shapes;
	protected List<btRigidBodyConstructionInfo> infos;
	protected List<btMotionState> motionStates;
	protected List<btRigidBody> bodies;
	protected Vector3 tempVector = new Vector3();
	
	public BulletObject() {
		instances = new LinkedList<ModelInstance>();
		shapes = new LinkedList<btCollisionShape>();
		infos = new LinkedList<btRigidBody.btRigidBodyConstructionInfo>();
		motionStates = new LinkedList<btMotionState>();
		bodies = new LinkedList<btRigidBody>();
	}

	public void dispose() {
		model.dispose();
		for (btCollisionShape shape :shapes) {
			shape.dispose();
		}
		for (btRigidBodyConstructionInfo info : infos) {
			info.dispose();
		}
		for (btMotionState	motionState : motionStates) {
			motionState.dispose();
		}
		for (btRigidBody body : bodies) {
			body.dispose();
		}		
	}

	public List<ModelInstance> getInstance() {
		return instances;
	}
	
	public List<btRigidBody> getRigidBody() {
		return bodies;
	}

	public void motion() {
		for (int i = 0; i < instances.size(); i++) {
			motionStates.get(i).getWorldTransform(instances.get(i).transform);
			instances.get(i).transform.getTranslation(tempVector);
			if(tempVector.y < -10f) {
				bodies.get(i).setMassProps(0, Vector3.Zero);
			}
		}
	}
	
	public void addInstance(ModelInstance instance, btCollisionShape shape, btRigidBodyConstructionInfo info, btMotionState motionState, btRigidBody body) {
		instances.add(instance);
		shapes.add(shape);
		infos.add(info);
		motionStates.add(motionState);
		bodies.add(body);
	}
}
