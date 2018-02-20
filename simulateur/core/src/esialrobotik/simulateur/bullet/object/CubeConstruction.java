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

public class CubeConstruction extends BulletObject{
	private static float size = 5.8f;
	private static Color[] colors = new Color[] {
			Color.YELLOW, // centre
			Color.BLACK, // EST
			Color.BLUE, // OUEST
			Color.RED, // NORD
			Color.GREEN // SUD
	};
	private static float[][] positions = new float[][] {
		{54f, 0f, 85f}, // 1
		{54f-size, 0f, 85f}, // 1E
		{54f+size, 0f, 85f}, // 1O
		{54f, 0f, 85f+size}, // 1N
		{54f, 0f, 85f-size}, // 1S
		{54f, 0f, 215f}, // 2
		{54f-size, 0f, 215f}, // 2E
		{54f+size, 0f, 215f}, // 2O
		{54f, 0f, 215f+size}, // 2N
		{54f, 0f, 215f-size}, // 2S
		{119f, 0f, 31f}, // 3
		{119f-size, 0f, 31f}, // 3E
		{119f+size, 0f, 31f}, // 3O
		{119f, 0f, 31f+size}, // 3N
		{119f, 0f, 31f-size}, // 3S
		{119f, 0f, 270f}, // 4
		{119f-size, 0f, 270f}, // 4E
		{119f+size, 0f, 270f}, // 4O
		{119f, 0f, 270f+size}, // 4N
		{119f, 0f, 270f-size}, // 4S
		{150f, 0f, 110f}, // 5
		{150f-size, 0f, 110f}, // 5E
		{150f+size, 0f, 110f}, // 5O
		{150f, 0f, 110f+size}, // 5N
		{150f, 0f, 110f-size}, // 5S
		{150f, 0f, 190f}, // 6
		{150f-size, 0f, 190f}, // 6E
		{150f+size, 0f, 190f}, // 6O
		{150f, 0f, 190f+size}, // 6N
		{150f, 0f, 190f-size}, // 6S
		};

	public CubeConstruction() {
		super();
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(size, size, size, 
				new Material(ColorAttribute.createDiffuse(Color.PINK),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		for (int i = 0; i < positions.length; i++) {
			model.materials.first().set(ColorAttribute.createDiffuse(colors[i%colors.length]));
			ModelInstance instance = new ModelInstance(model);
			instance.transform.trn(positions[i][0], positions[i][1]+0.01f, positions[i][2]);
			btBoxShape shape = new btBoxShape(new Vector3(size/2f,size/2f, size/2f));
			shape.calculateLocalInertia(1f, tempVector);
			btRigidBodyConstructionInfo info = new btRigidBodyConstructionInfo(1f, null, shape, tempVector);
			btDefaultMotionState motionState = new btDefaultMotionState();
			motionState.setWorldTransform(instance.transform);
			btRigidBody body = new btRigidBody(info);
			body.setMotionState(motionState);
			addInstance(instance, shape, info, motionState, body);
		}
	}

}
