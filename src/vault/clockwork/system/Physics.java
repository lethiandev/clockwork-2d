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
package vault.clockwork.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import vault.clockwork.Game;
import vault.clockwork.scene.Actor;

/**
 * Manage the physics world separately.
 * @author Konrad Nowakowski https://github.com/konrad92
 */
public class Physics implements System, ContactListener {
	/**
	 * Sceen to world scale.
	 */
	static public final float SCALE = 0.01f;
	
	/**
	 * World to screen scale (inverted SCALE).
	 */
	static public final float SCALE_INV = 1.f/SCALE;
	
	/**
	 * Box2D physics world.
	 */
	public final World world;
	
	/**
	 * Debug renderer.
	 */
	private final Box2DDebugRenderer debugRenderer;
	
	/**
	 * Step performing accumulator.
	 */
	private float accumulator;
	
	/**
	 * Initialize Box2D system.
	 */
	static {
		Box2D.init();
	}
	
	/**
	 * Ctor.
	 */
	public Physics() {
		this.world = new World(new Vector2(0.f, -10.f), true);
		this.debugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);
		
		// apply contact listener
		this.world.setContactListener(this);
	}
	
	/**
	 * Perform physics step.
	 */
	@Override
	public void perform() {
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		
		// perform accumulated step
		while(accumulator >= 1.f/60.f) {
			this.world.step(1/60.f, 6, 2);
			accumulator -= 1.f/60.f;
		}
	}
	
	/**
	 * Perform debug information render if enabled.
	 */
	@Override
	public void postPerform() {
		if(Game.DEBUG_INFO) {
			// scale `world to screen`
			Matrix4 projMatrix;
			if(Game.mainCamera != null) {
				projMatrix = Game.mainCamera.combined.cpy();
			} else {
				projMatrix = new Matrix4().setToOrtho2D(0.f, 0.f,
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight()
				);
			}
			projMatrix.scl(SCALE_INV);
			
			// perform debug rendering
			this.debugRenderer.render(this.world, projMatrix);
		}
	}

	/**
	 * @see Disposable#dispose() 
	 */
	@Override
	public void dispose() {
		if(Game.DEBUG_INFO) {
			this.debugRenderer.dispose();
		}
		
		this.world.dispose();
	}

	/**
	 * @see ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact) 
	 * @param contact 
	 */
	@Override
	public void beginContact(Contact contact) {
		Object c1 = contact.getFixtureA().getUserData();
		Object c2 = contact.getFixtureB().getUserData();
		
		// fixture A
		if(c1 instanceof Actor) {
			if(c2 instanceof Actor) {
				((Actor)c1).onHit((Actor)c2, contact);
			} else {
				((Actor)c1).onHit(null, contact);
			}
		}
		
		// fixture B
		if(c2 instanceof Actor) {
			if(c1 instanceof Actor) {
				((Actor)c2).onHit((Actor)c1, contact);
			} else {
				((Actor)c2).onHit(null, contact);
			}
		}
	}

	/**
	 * @see ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact) 
	 * @param contact 
	 */
	@Override
	public void endContact(Contact contact) {
	}

	/**
	 * @see ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold) 
	 * @param contact
	 * @param oldManifold 
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	/**
	 * @see ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse) 
	 * @param contact
	 * @param impulse 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	/**
	 * Compare given fixture with the contact fixtures.
	 * Using OR operator.
	 * @param check
	 * @param by
	 * @return 
	 */
	static public boolean OR(Fixture check, Contact by) {
		return check == by.getFixtureA() || check == by.getFixtureB();
	}
}
