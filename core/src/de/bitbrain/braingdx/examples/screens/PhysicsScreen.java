package de.bitbrain.braingdx.examples.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.*;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.examples.assets.Assets;
import de.bitbrain.braingdx.graphics.lighting.LightingConfig;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.SimpleWorldBounds;

import static java.lang.Math.random;

public class PhysicsScreen extends BrainGdxScreen2D<BrainGdxGame> {

   private GameContext2D context;

   public PhysicsScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      this.context = context;
      context.setBackgroundColor(Color.SKY);
      context.getGameWorld().setBounds(new SimpleWorldBounds(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
      LightingConfig config = new LightingConfig();
      config.rays(800);
      context.getLightingManager().setConfig(config);
      context.getLightingManager().setAmbientLight(new Color(0.3f, 0f, 0.1f, 0.5f));
      context.getLightingManager().addPointLight("point-light1", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 3f, Gdx.graphics.getWidth() * 2f, Color.MAGENTA);
      context.getLightingManager().addPointLight("point-light2", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 3f * 2f, Gdx.graphics.getWidth() * 2f, Color.BLUE);
      context.getPhysicsManager().setPositionIterations(160);
      context.getPhysicsManager().setVelocityIterations(160);
      context.getPhysicsManager().setGravity(0f, -50f);
      for (int i = 0; i < 5; ++i) {
         float x = (float) (Gdx.graphics.getWidth() / 2f + random() * 150f);
         float y = (float) (Gdx.graphics.getHeight() / 2f + random() * 150f);
         spawnBall(x, y);
      }

      BodyDef floorBodyDef = new BodyDef();
      floorBodyDef.type = BodyDef.BodyType.StaticBody;
      floorBodyDef.position.x = Gdx.graphics.getWidth() / 2f;
      floorBodyDef.position.y = 0f;
      context.getPhysicsManager().addBody(floorBodyDef, Gdx.graphics.getWidth() * 2f, 10f, "FLOOR");
      setupRenderer(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (Gdx.input.isTouched()) {
         spawnBall(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
      }
   }

   private void setupRenderer(GameContext2D context) {
      context.getRenderManager().register("BALL", new SpriteRenderer(Assets.Textures.BALL));
   }

   private Body spawnBall(float x, float y) {
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyDef.BodyType.DynamicBody;
      bodyDef.fixedRotation = false;
      bodyDef.position.x = x;
      bodyDef.position.y = y;
      bodyDef.gravityScale = 1f;
      bodyDef.bullet = false;
      FixtureDef def = new FixtureDef();
      def.shape = new CircleShape();
      def.shape.setRadius(32f);
      def.friction = 0.4f;
      def.restitution = 1.5f;
      def.density = 8f;
      Body body = context.getPhysicsManager().addBody(bodyDef, def, "BALL");
      GameObject obj = (GameObject) body.getUserData();
      obj.setColor(new Color(Color.rgb888((float)random(), (float)random(), (float)random())));
      return body;
   }
}
