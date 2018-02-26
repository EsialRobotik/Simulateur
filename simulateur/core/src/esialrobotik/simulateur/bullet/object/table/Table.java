package esialrobotik.simulateur.bullet.object.table;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class Table extends BulletObject{
	private List<BulletObject> objects = new LinkedList<BulletObject>();
	private static float sizeX = 215.0f;
	private static float sizeZ = 301.0f;
	private static float hauteurRebord = 10f;
	private static float epaisseurRebord = 2.2f;

	public Table() {
		super();
		model = new Model();
		objects.add(new Plateau(sizeX, sizeZ));
		objects.add(new RebordsLargeur(sizeX, sizeZ, hauteurRebord, epaisseurRebord));
		objects.add(new RebordsLongueur(sizeX, sizeZ, hauteurRebord, epaisseurRebord));
		for (BulletObject object : objects) {
			instances.addAll(object.getInstance());
			bodies.addAll(object.getRigidBody());
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (btRigidBody body : bodies) {
			body.dispose();
		}
		for (BulletObject object : objects) {
			object.dispose();
		}
	}

	@Override
	public List<ModelInstance> getInstance() {
		return instances;
	}

	@Override
	public List<btRigidBody> getRigidBody() {
		return bodies;
	}

	@Override
	public void motion() {
		for (BulletObject bulletObject : objects) {
			bulletObject.motion();	
		}
	}
}
