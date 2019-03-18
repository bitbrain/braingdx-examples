package de.bitbrain.braingdx.examples;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.examples.assets.Assets;
import de.bitbrain.braingdx.examples.screens.RPGScreen;
import de.bitbrain.braingdx.screens.AbstractScreen;

public class BrainGdxExampleGame extends BrainGdxGame {

	@Override
	protected GameAssetLoader getAssetLoader() {
		return new SmartAssetLoader(Assets.class);
	}

	@Override
	protected AbstractScreen<?> getInitialScreen() {
		return new RPGScreen(this);
	}
}
