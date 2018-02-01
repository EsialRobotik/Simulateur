package esialrobotik.simulateur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import com.badlogic.gdx.utils.Array;

public class Simulateur extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	int progress = 0;
	ShapeRenderer sr;
	World world;
	Box2DDebugRenderer debugRenderer;
	Camera camera;
	Camera cam2;
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		img = new Texture("RobotCities2018-Logo.png");
		Box2D.init();
		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(2, 2*(h/w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		cam2 = new OrthographicCamera(640, 640*(h/w));
		cam2.position.set(cam2.viewportWidth / 2f, cam2.viewportHeight / 2f, 0);
		cam2.update();
		for (int i = 0; i < 42; i++) {
			// First we create a body definition
			BodyDef bodyDef = new BodyDef();
			// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
			bodyDef.type = BodyType.DynamicBody;
			// Set our body's starting position in the world
			bodyDef.position.set((float)Math.random()*(camera.viewportWidth-0.03f)+0.03f, (float)Math.random()*camera.viewportHeight+1f);
			// Create our body in the world using our body definition
			Body body = world.createBody(bodyDef);

			// Create a circle shape and set its radius to 6
			CircleShape circle = new CircleShape();
			circle.setRadius(0.03f);

			// Create a fixture definition to apply our shape to
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.5f; 
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.6f; // Make it bounce a little bit

			// Create our fixture and attach it to the body
			Fixture fixture = body.createFixture(fixtureDef);

			// Remember to dispose of any shapes after you're done with them!
			// BodyDef and FixtureDef don't need disposing, but shapes do.
			circle.dispose();	
		}
				
		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(0, 0));  

		// Create a body from the defintion and add it to the world
		Body groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 0.1 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(camera.viewportWidth, 0.1f);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
		
		// Create our body definition
		BodyDef leftPanelBodyDef = new BodyDef();  
		// Set its world position
		leftPanelBodyDef.position.set(new Vector2(0, 0));  

		// Create a body from the defintion and add it to the world
		Body leftPanelBody = world.createBody(leftPanelBodyDef);  

		// Create a polygon shape
		PolygonShape leftPanelBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 0.1 high
		// (setAsBox takes half-width and half-height as arguments)
		leftPanelBox.setAsBox(0.1f, camera.viewportHeight);
		// Create a fixture from our polygon shape and add it to our ground body  
		leftPanelBody.createFixture(leftPanelBox, 0.0f); 
		// Clean up after ourselves
		leftPanelBox.dispose();
		
		// Create our body definition
		BodyDef rightPanelBodyDef = new BodyDef();  
		// Set its world position
		rightPanelBodyDef.position.set(new Vector2(camera.viewportWidth, 0));  

		// Create a body from the defintion and add it to the world
		Body rightPanelBody = world.createBody(rightPanelBodyDef);  

		// Create a polygon shape
		PolygonShape rightPanelBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 0.1 high
		// (setAsBox takes half-width and half-height as arguments)
		rightPanelBox.setAsBox(0.1f, camera.viewportHeight);
		// Create a fixture from our polygon shape and add it to our ground body  
		rightPanelBody.createFixture(rightPanelBox, 0.0f); 
		// Clean up after ourselves
		rightPanelBox.dispose();
	}

	@Override
	public void render () {
		cam2.update();
		batch.setProjectionMatrix(cam2.combined);
		if(progress < 100) {
			progress += (int) (Math.random()*3);
			progress = (progress > 100) ? 100 : progress;
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 70, 100);
		batch.end();
		sr.setProjectionMatrix(cam2.combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.PINK);
		sr.rect(30, 80, progress*cam2.viewportWidth/100-60, 20);
		sr.end();
		camera.update();
		world.step(1/45f, 6, 2);
		debugRenderer.render(world, camera.combined);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
