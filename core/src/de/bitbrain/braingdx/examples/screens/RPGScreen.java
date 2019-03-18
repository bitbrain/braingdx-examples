package de.bitbrain.braingdx.examples.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.ai.pathfinding.Path;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RandomVelocityMovementBehavior;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.examples.animation.PlayerAnimationEnabler;
import de.bitbrain.braingdx.examples.assets.Assets;
import de.bitbrain.braingdx.examples.behaviour.RandomMovementBehavior;
import de.bitbrain.braingdx.examples.input.IngameKeyboardInput;
import de.bitbrain.braingdx.examples.rpg.NPC;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.animation.*;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapEvents;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;

public class RPGScreen extends AbstractScreen<BrainGdxGame> {

   private AnimationSpriteSheet spriteSheet;
   private RasteredMovementBehavior behavior;

   private class AStarRenderer implements RenderLayer {

      private final TiledMapManager tiledMapManager;
      private Path path;
      private Texture texture;

      AStarRenderer(TiledMapManager manager, GameEventManager eventManager) {
         this.tiledMapManager = manager;
         texture = GraphicsFactory.createTexture(2, 2, Color.GREEN);
         eventManager.register(onEnterCellListener, TiledMapEvents.OnEnterCellEvent.class);
         eventManager.register(onLayerChangeListener, TiledMapEvents.OnLayerChangeEvent.class);
      }

      @Override
      public void beforeRender() {
         // TODO Auto-generated method stub

      }

      @Override
      public void render(Batch batch, float delta) {
         if (path != null) {
            batch.begin();
            for (int i = 0; i < path.getLength(); ++i) {
               batch.draw(texture,
                     path.getX(i) * tiledMapManager.getAPI().getCellWidth(),
                     path.getY(i) * tiledMapManager.getAPI().getCellHeight(),
                     tiledMapManager.getAPI().getCellWidth(),
                     tiledMapManager.getAPI().getCellHeight());
            }
            batch.end();
         }
         //batch.begin();
         //TextureRegion[] regions = spriteSheet.getFrames(0, 0, 0, 0);
         //batch.draw(regions[0], 32f, 32f, 32, 32);
         //batch.end();
      }

      private final GameEventListener<TiledMapEvents.OnEnterCellEvent> onEnterCellListener = new GameEventListener<TiledMapEvents.OnEnterCellEvent>() {
         @Override
         public void onEvent(TiledMapEvents.OnEnterCellEvent event) {
            path = tiledMapManager.getPathFinder().findPath(player, 0, 0);
         }
      };

      private final GameEventListener<TiledMapEvents.OnLayerChangeEvent> onLayerChangeListener = new GameEventListener<TiledMapEvents.OnLayerChangeEvent>() {
         @Override
         public void onEvent(TiledMapEvents.OnLayerChangeEvent event) {
            path = tiledMapManager.getPathFinder().findPath(player, 0, 0);
         }
      };
   }

   private GameObject player;

   public RPGScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext context) {
      context.getGameCamera().setDefaultZoomFactor(0.15f);
      context.getGameCamera().setTargetTrackingSpeed(1f);
      context.getGameCamera().setZoomScalingFactor(0f);
      TiledMap map = SharedAssetManager.getInstance().get(Assets.TiledMaps.MAP, TiledMap.class);
      final TiledMapManager tiledMapManager = context.getTiledMapManager();
      context.getLightingManager().setAmbientLight(new Color(0.2f, 0.3f, 0.6f, 0.4f));
      //tiledMapManager.getAPI().setDebug(true);
      tiledMapManager.load(map, context.getGameCamera().getInternalCamera(), TiledMapType.ORTHOGONAL);

      player = null;
      for (GameObject o : context.getGameWorld()) {
         context.getBehaviorManager().apply(new PointLightBehavior(new Color(1f, 0.7f, 0.7f, 1f), 190f, context.getLightingManager()),
               o);
         if (o.getType().equals("CLERIC_MALE")) {
            player = o;
         } else {
            for (NPC npc : NPC.values()) {
               if (npc.name().equals(o.getType())) {
                  context.getBehaviorManager().apply(new RandomMovementBehavior(context.getTiledMapManager().getAPI()), o);
               }
            }
         }
         o.setAttribute(Orientation.class, Orientation.DOWN);
      }
      context.getGameCamera().setTrackingTarget(player);

      setupAnimations(context);


      behavior = new RasteredMovementBehavior(tiledMapManager.getAPI())
            .interval(0.3f)
            .rasterSize(tiledMapManager.getAPI().getCellWidth(), tiledMapManager.getAPI().getCellHeight());
      context.getBehaviorManager().apply(behavior, player);

      setupInput(context);

      //AStarRenderer renderer = new AStarRenderer(context.getTiledMapManager(), context.getEventManager());
      //context.getRenderPipeline().putAfter(RenderPipeIds.LIGHTING, "astar", renderer);
   }

   private void setupInput(GameContext context) {
      context.getInputManager().register(new IngameKeyboardInput(behavior));
   }

   private void setupAnimations(GameContext context) {
      final Texture texture = SharedAssetManager.getInstance().get(Assets.Textures.CHARACTER_TILESET);
      spriteSheet = new AnimationSpriteSheet(texture, 32, 48);
      for (NPC npc : NPC.values()) {
         AnimationConfig config = AnimationConfig.builder()
               .registerFrames(Orientation.DOWN, AnimationFrames.builder()
                     .frames(3)
                     .playMode(Animation.PlayMode.LOOP_PINGPONG)
                     .direction(AnimationFrames.Direction.HORIZONTAL)
                     .origin(npc.getIndexX(), npc.getIndexY())
                     .duration(0.2f)
                     .resetIndex(1)
                     .build())
               .registerFrames(Orientation.LEFT, AnimationFrames.builder()
                     .frames(3)
                     .playMode(Animation.PlayMode.LOOP_PINGPONG)
                     .direction(AnimationFrames.Direction.HORIZONTAL)
                     .origin(npc.getIndexX(), npc.getIndexY() + 1)
                     .duration(0.2f)
                     .resetIndex(1)
                     .build())
               .registerFrames(Orientation.RIGHT, AnimationFrames.builder()
                     .frames(3)
                     .playMode(Animation.PlayMode.LOOP_PINGPONG)
                     .direction(AnimationFrames.Direction.HORIZONTAL)
                     .origin(npc.getIndexX(), npc.getIndexY() + 2)
                     .duration(0.2f)
                     .resetIndex(1)
                     .build())
               .registerFrames(Orientation.UP, AnimationFrames.builder()
                     .frames(3)
                     .playMode(Animation.PlayMode.LOOP_PINGPONG)
                     .direction(AnimationFrames.Direction.HORIZONTAL)
                     .origin(npc.getIndexX(), npc.getIndexY() + 3)
                     .duration(0.2f)
                     .resetIndex(1)
                     .build())
               .build();
         context.getRenderManager().register(npc.name(), new AnimationRenderer(spriteSheet, config, new AnimationTypeResolver<GameObject>() {
            @Override
            public Object getAnimationType(GameObject object) {
               return object.getAttribute(Orientation.class);
            }
         }, new PlayerAnimationEnabler()));
      }

   }
}
