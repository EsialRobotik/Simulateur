package esialrobotik.simulateur.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Camera pour le simulateur (objects 3D/2D)
 * @author Clement P
 *
 */
public class SimulateurCamera extends OrthographicCamera {
	
	/**
	 * Constructeur par defaut (OrthographicCamera)
	 * @param height hauteur de la fenetre
	 * @param width largeur de la fenetre
	 * @param scale échelle
	 */
	public SimulateurCamera(float height, float width, int scale) {
		super(scale, scale*(height/width));
		this.update();
	}

	/**
	 * Centrer la position de la camera
	 */
	public void center() {
		this.position.set(this.viewportWidth / 2f, this.viewportHeight / 2f, 0);
	}
}
