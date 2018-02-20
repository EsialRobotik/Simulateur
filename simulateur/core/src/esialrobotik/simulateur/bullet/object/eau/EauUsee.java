package esialrobotik.simulateur.bullet.object.eau;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

import esialrobotik.simulateur.bullet.object.BulletObject;

public class EauUsee extends BulletObject{
	private static float size = 4.4f;
	private Color[] colors = new Color[]{Color.ORANGE, Color.GREEN};
	private float[][] positions = new float[][] {
			{84f, 27f, 0f}, // GREEN
			{200f, 27f, 61f}, // MIXTE
			{200f, 27f, 239f}, //MIXTE
			{84f, 27f, 301f} //ORANGE
	};

	public EauUsee(Color c) {
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createSphere(size, size, size, 10, 10,
				new Material(ColorAttribute.createDiffuse(c),
						ColorAttribute.createSpecular(Color.WHITE), 
						FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		for (int i = 0; i < positions.length; i++) {
			for (int j = 0; j < 8; j++) {
				float cosX = 0f;
				float sinY = 0f;
				float cosZ = 0f;
				switch (i) {
				case 0:
					cosZ = (float)Math.cos(Math.toRadians(100));
					sinY = (float)Math.sin(Math.toRadians(100));
					model.materials.first().set(ColorAttribute.createDiffuse(colors[0]));
					break;
				case 1:
					cosX = (float)Math.cos(Math.toRadians(80));
					sinY = (float)Math.sin(Math.toRadians(80));
					model.materials.first().set(ColorAttribute.createDiffuse(colors[j%colors.length]));
					break;
				
				case 2:
					cosX = (float)Math.cos(Math.toRadians(80));
					sinY = (float)Math.sin(Math.toRadians(80));
					model.materials.first().set(ColorAttribute.createDiffuse(colors[j%colors.length]));
					break;
				
				case 3:
					cosZ = (float)Math.cos(Math.toRadians(80));
					sinY = (float)Math.sin(Math.toRadians(80));
					model.materials.first().set(ColorAttribute.createDiffuse(colors[1]));
					break;

				default:
					model.materials.first().set(ColorAttribute.createDiffuse(Color.PINK));
					break;
				}
				
				ModelInstance instance = new ModelInstance(model);
				instance.transform.trn((float)(positions[i][0]+j*size*cosX), (float)(positions[i][1]+j*size*sinY), positions[i][2]+j*size*cosZ);
				btSphereShape boxShape = new btSphereShape(size/2f);
				boxShape.calculateLocalInertia(0.020f, tempVector);
				btRigidBodyConstructionInfo boxInfo = new btRigidBodyConstructionInfo(0.020f, null, boxShape, tempVector);
				btMotionState boxMotionState = new btDefaultMotionState();
				boxMotionState.setWorldTransform(instance.transform);
				btRigidBody boxBody = new btRigidBody(boxInfo);
				//boxBody.setAngularVelocity(new Vector3(MathUtils.random(0f, 0.1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f)));
				boxBody.setRestitution(1f);
				//boxBody.setFriction(0.0008f);
				boxBody.setMotionState(boxMotionState);
				addInstance(instance, boxShape, boxInfo, boxMotionState, boxBody);
			}
		}
	}
}
