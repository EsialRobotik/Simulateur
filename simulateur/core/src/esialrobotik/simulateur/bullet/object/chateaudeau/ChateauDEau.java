package esialrobotik.simulateur.bullet.object.chateaudeau;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class ChateauDEau extends BulletObject{
	private List<BulletObject> plateaux = new LinkedList<BulletObject>();
	private List<BulletObject> rebords = new LinkedList<BulletObject>();
	private List<ModelInstance> instances = new LinkedList<ModelInstance>();
	private List<btRigidBody> bodies = new LinkedList<btRigidBody>();
	private static float sizeX = 37.0f;
	private static float sizeZ = 37.0f;
	private static float hauteurRebord = 40f;
	private static float epaisseurRebord = 1f;

	public ChateauDEau() {
		super();
		model = new Model();
		plateaux.add(new Fond(sizeX, sizeZ, -sizeX-2.2f, 0f, Color.ORANGE));
		rebords.add(new RebordsLargeur(sizeX, sizeZ, hauteurRebord, epaisseurRebord, -sizeX-2.2f, 0f, Color.ORANGE, false));
		rebords.add(new RebordsLongueur(sizeX, sizeZ, hauteurRebord, epaisseurRebord, -sizeX-2.2f, 0f, Color.ORANGE));
		plateaux.add(new Fond(sizeX, sizeZ, -sizeX-2.2f, 264f, Color.GREEN));
		rebords.add(new RebordsLargeur(sizeX, sizeZ, hauteurRebord, epaisseurRebord, -sizeX-2.2f, 264f, Color.GREEN, true));
		rebords.add(new RebordsLongueur(sizeX, sizeZ, hauteurRebord, epaisseurRebord, -sizeX-2.2f, 264f, Color.GREEN));
		for (BulletObject object : plateaux) {
			instances.addAll(object.getInstance());
			bodies.addAll(object.getRigidBody());
		}
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
		for (BulletObject bulletObject : plateaux) {
			bulletObject.dispose();	
		}
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
		for (BulletObject bulletObject : plateaux) {
			bulletObject.motion();	
		}
		for (BulletObject bulletObject : rebords) {
			bulletObject.motion();	
		}
	}
}
