package de.bitbrain.braingdx.examples;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.examples.assets.Assets;
import de.bitbrain.braingdx.examples.screens.RPGScreen;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;

public class BrainGdxExampleGame extends BrainGdxGame {

	@Override
	protected GameAssetLoader getAssetLoader() {
		return new SmartAssetLoader(Assets.class);
	}

	@Override
	protected BrainGdxScreen2D<BrainGdxGame> getInitialScreen() {
		return new RPGScreen(this);
	}
}
