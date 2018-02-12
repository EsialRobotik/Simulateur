package esialrobotik.simulateur.simu2D.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground2D {

	private Body groundBody, leftBody, rightBody;
	private PolygonShape groundBox, leftBox, rightBox;
	
	public Ground2D(float max_x, float max_y, World world) {
		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(0, 0));  

		// Create a body from the definition and add it to the world
		groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 0.1 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(max_x, 0.1f);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
		
		// Create our body definition
		BodyDef leftPanelBodyDef = new BodyDef();  
		// Set its world position
		leftPanelBodyDef.position.set(new Vector2(0, 0));  

		// Create a body from the definition and add it to the world
		leftBody = world.createBody(leftPanelBodyDef);  

		// Create a polygon shape
		leftBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 0.1 high
		// (setAsBox takes half-width and half-height as arguments)
		leftBox.setAsBox(0.1f, max_y);
		// Create a fixture from our polygon shape and add it to our ground body  
		leftBody.createFixture(leftBox, 0.0f); 
		// Clean up after ourselves
		leftBox.dispose();
		
		// Create our body definition
		BodyDef rightPanelBodyDef = new BodyDef();  
		// Set its world position
		rightPanelBodyDef.position.set(new Vector2(max_x, 0));  

		// Create a body from the definition and add it to the world
		rightBody = world.createBody(rightPanelBodyDef);  

		// Create a polygon shape
		rightBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 0.1 high
		// (setAsBox takes half-width and half-height as arguments)
		rightBox.setAsBox(0.1f, max_y);
		// Create a fixture from our polygon shape and add it to our ground body  
		rightBody.createFixture(rightBox, 0.0f); 
		// Clean up after ourselves
		rightBox.dispose();
	}
}
