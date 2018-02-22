package esialrobotik.simulateur.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import esialrobotik.simulateur.Simulateur;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.height = 720;
		config.width = 1280;
		new LwjglApplication(new Simulateur(), config);
	}
}
