package de.bitbrain.braingdx.examples.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.examples.assets.Assets;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;

import java.util.HashSet;
import java.util.Set;

public class ParticleScreen extends BrainGdxScreen2D<BrainGdxGame> {

   private GameContext2D context;

   private Set<ParticleEffect> effectSet = new HashSet<ParticleEffect>();

   public ParticleScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      this.context = context;
      context.setBackgroundColor(Color.BLACK);
      context.getGameCamera().setDefaultZoomFactor(0.3f);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (Gdx.input.isTouched()) {
         effectSet.add(context.getParticleManager().spawnEffect(Assets.Particles.BYTE, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
         for (ParticleEffect effect : effectSet) {
            for (ParticleEmitter emitter : effect.getEmitters()) {
               emitter.setContinuous(false);
            }
         }
      }
   }
}