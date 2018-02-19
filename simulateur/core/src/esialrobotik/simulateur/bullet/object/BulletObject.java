package esialrobotik.simulateur.bullet.object;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public interface BulletObject {

	public void dispose();

	public ModelInstance getInstance();
	
	public btRigidBody getRigidBody();

	public void motion();
}
