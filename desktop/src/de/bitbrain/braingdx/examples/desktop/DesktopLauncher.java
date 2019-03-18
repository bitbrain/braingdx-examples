package de.bitbrain.braingdx.examples.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.bitbrain.braingdx.examples.BrainGdxExampleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.title = "braingdx examples";
		config.forceExit = false;
		config.width = 1000;
		config.height = 700;
		new LwjglApplication(new BrainGdxExampleGame(), config);
	}
}
