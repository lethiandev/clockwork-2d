/*
 * The MIT License
 *
 * Copyright 2015 Konrad Nowakowski https://github.com/konrad92.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package vault.clockwork.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import vault.clockwork.Game;
import vault.clockwork.Vault;
import vault.clockwork.system.Physics;

/**
 * Drewniany klocek (DynamicBody)
 * @author Agnieszka Makowska https://github.com/Migemiley
 */
public class PlankBlockActor extends ObstacleActor{
	private Body body;
	private Fixture fixture;
	private final Sprite sprBlock;

	public PlankBlockActor(int id){
		super(id);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50.f * Physics.SCALE, 50.f * Physics.SCALE);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(350.f * Physics.SCALE, -130.f * Physics.SCALE);
		body = Game.physics.world.createBody(bodyDef);
		fixture = body.createFixture(shape, 25.f);
		fixture.setUserData(this);
		
		shape.dispose();
		
		// create the plank sprite
		sprBlock = new Sprite(Game.assets.get("assets/shorttrunk.png", Texture.class));
		sprBlock.setBounds(0.f, 0.f, 100.f, 100.f);
		
		// dodanie dzwiekow do odegrania
		impactSounds.add(
			Game.assets.get(Vault.SOUND_WOODBOUNCE, Sound.class)
		);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		sprBlock.setCenter(
			body.getPosition().x * Physics.SCALE_INV,
			body.getPosition().y * Physics.SCALE_INV
		);
		
		batch.begin();
		sprBlock.draw(batch);
		batch.end();
	}
	
	/**
	 * @see Actor#getPosition() 
	 * @return 
	 */
	@Override
	public Vector2 getPosition() {
		return body.getTransform().getPosition().scl(Physics.SCALE_INV);
	}
	
	/**
	 * @see Actor#setPosition(com.badlogic.gdx.math.Vector2) 
	 * @param newPosition 
	 */
	@Override
	public void setPosition(Vector2 newPosition) {
		body.setTransform(newPosition.cpy().scl(Physics.SCALE), body.getTransform().getRotation());
	}
	
	/**
	 * @see Actor#getRotation() 
	 * @return 
	 */
	@Override
	public float getRotation() {
		return body.getTransform().getRotation();
	}
	
	/**
	 * @see Actor#setRotation(float) 
	 * @param newAngle
	 */
	@Override
	public void setRotation(float newAngle) {
		body.getTransform().setRotation(newAngle);
	}
	
	@Override
	public void dispose() {
		Game.physics.world.destroyBody(body);
	}
	
}