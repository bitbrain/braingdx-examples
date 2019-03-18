package de.bitbrain.braingdx.examples.behaviour;

import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

public class RandomMovementBehavior extends RasteredMovementBehavior {

   private float interval;

   private Movement<Movement> movement;

   private final DeltaTimer timer = new DeltaTimer();


   public RandomMovementBehavior(TiledMapAPI api) {
      super(api);
   }

   @Override
   public void update(GameObject source, float delta) {
      timer.update(delta);
      super.update(source, delta);
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
