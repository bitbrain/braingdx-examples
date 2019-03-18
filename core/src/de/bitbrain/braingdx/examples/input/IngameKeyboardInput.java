package de.bitbrain.braingdx.examples.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.util.Updateable;

public class IngameKeyboardInput extends InputAdapter implements Updateable {

   private final Movement<Orientation> movement;

   public IngameKeyboardInput(Movement<Orientation> movement) {
      this.movement = movement;
   }

   @Override
   public void update(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
         movement.move(Orientation.UP);
      } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
         movement.move(Orientation.LEFT);
      } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
         movement.move(Orientation.DOWN);
      } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
         movement.move(Orientation.RIGHT);
      }
   }
}