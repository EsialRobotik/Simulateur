package esialrobotik.simulateur.simu2D.object;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Ball 2D avec box2d package
 * @author Clement P
 *
 */
public class Ball2D{
	private Body body;
	private CircleShape shape;
	private Fixture fixture;

	public Ball2D(float max_x, float max_y, float radius, World world) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set((float)Math.random()*max_x,
				             (float)Math.random()*max_y);
		body = world.createBody(bodyDef);
		// Create a circle shape and set its radius
		shape = new CircleShape();
		shape.setRadius(radius);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = this.shape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}
}
