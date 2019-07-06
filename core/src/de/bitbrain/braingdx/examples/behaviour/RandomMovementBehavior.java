package de.bitbrain.braingdx.examples.behaviour;

import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

public class RandomMovementBehavior extends RasteredMovementBehavior {

   private float interval;

   private final DeltaTimer timer = new DeltaTimer();


   public RandomMovementBehavior(TiledMapContext context, float interval) {
      super(context);
      interval(interval).rasterSize(context.getCellWidth(), context.getCellHeight());
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      timer.update(delta);
      if (timer.reached(interval)) {
         interval = (float) (2f + Math.random() * 2f);
         timer.reset();
         moveIntoRandomDirection();
      }
   }

   private void moveIntoRandomDirection() {
      Orientation orientation = Orientation.values()[(int) (Math.random() * Orientation.values().length)];
      move(orientation);
   }
}
