package esialrobotik.simulateur.bullet.object.table;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class Table extends BulletObject{
	private Plateau plateau;
	private List<BulletObject> rebords = new LinkedList<BulletObject>();
	private List<ModelInstance> instances = new LinkedList<ModelInstance>();
	private List<btRigidBody> bodies = new LinkedList<btRigidBody>();
	private static float sizeX = 215.0f;
	private static float sizeZ = 301.0f;
	private static float hauteurRebord = 10f;
	private static float epaisseurRebord = 2.2f;

	public Table() {
		super();
		model = new Model();
		plateau = new Plateau(sizeX, sizeZ);
		rebords.add(new RebordsLargeur(sizeX, sizeZ, hauteurRebord, epaisseurRebord));
		rebords.add(new RebordsLongueur(sizeX, sizeZ, hauteurRebord, epaisseurRebord));
		instances.addAll(plateau.getInstance());
		bodies.addAll(plateau.getRigidBody());
		for (BulletObject object : rebords) {
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
		plateau.dispose();
		for (BulletObject bulletObject : rebords) {
			bulletObject.dispose();	
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
		plateau.motion();
		for (BulletObject bulletObject : rebords) {
			bulletObject.motion();	
		}
	}
}
