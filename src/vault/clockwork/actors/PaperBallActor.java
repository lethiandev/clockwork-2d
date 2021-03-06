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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import vault.clockwork.Game;
import vault.clockwork.Vault;
import vault.clockwork.scene.Actor;
import vault.clockwork.system.Physics;

/**
 *
 * @author Konrad Nowakowski https://github.com/konrad92
 */
public class PaperBallActor extends ObstacleActor {
	/**
	 * Klasa do rysowania trajektorii.
	 */
	public class PathTrace {
		/**
		 * Pozycja trajektorii.
		 */
		public final Vector2 position = new Vector2();
		public float angle;
		
		/**
		 * Dlugosc linii.
		 */
		static public final float LENGTH = 10.f;
		
		/**
		 * Ctor.
		 * @param position
		 * @param angle DEGREES
		 */
		public PathTrace(Vector2 position, float angle) {
			this.position.set(position);
			this.angle = angle;
		}
		
		/**
		 * Rysuj czesc trajektorii.
		 * Wywolac nalezy miedzy batch.begin() a batch.end().
		 * @param renderer 
		 */
		public void drawUp(ShapeRenderer renderer) {
			renderer.line(
				Vector2.X.cpy().setAngle(angle).scl(-LENGTH * .5f).add(position),
				Vector2.X.cpy().setAngle(angle).scl(LENGTH * .5f).add(position)
			);
		}
	}
	
	/**
	 * Sciezka do tekstury papierowej kulki.
	 */
	static public final String PAPERBALL_TEXTURE = "assets/paperball.png";
	
	/**
	 * Preload the actor resources.
	 */
	static public void preload() {
		Game.assets.load(PAPERBALL_TEXTURE, Texture.class);
	}
	
	/**
	 * Physics body.
	 */
	private final Body body;
	
	/**
	 * The body single fixture.
	 */
	private final Fixture fixture;
	
	/**
	 * Paper ball sprite to be drawn.
	 */
	private final Sprite sprBall;
	
	/**
	 * Trajektoria lotu.
	 */
	private final Array<PathTrace> paths = new Array<>();
	
	/**
	 * Ctor.
	 * Create new physic body on the world.
	 * @see Actor#Actor(int) 
	 * @param id Turret unique id.
	 */
	public PaperBallActor(int id) {
		super(id);
		
		// body shape
		//PolygonShape shape = new PolygonShape();
		CircleShape shape = new CircleShape();
		shape.setRadius(20.f * Physics.SCALE);
		
		// make the circle
		/*Vector2[] circleBuilt = new Vector2[8];
		for(int i = 0; i < 8; i++) {
			circleBuilt[i] = new Vector2(
				20.f*(float)Math.cos((double)i*(Math.PI/4))*Physics.SCALE,
				20.f*(float)Math.sin((double)i*(Math.PI/4))*Physics.SCALE
			);
		}
		
		shape.set(circleBuilt);*/
		
		// create physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((float)Math.random()*1.f, (float)Math.random()*1.f);
		bodyDef.fixedRotation = true;
		body = Game.physics.world.createBody(bodyDef);
		
		// make the fixtures
		fixture = body.createFixture(shape, 5.f);
		fixture.setRestitution(.4f);
		fixture.setFriction(.35f);
		fixture.setUserData(this);
		
		shape.dispose();
		
		// create the ball sprite
		sprBall = new Sprite(Game.assets.get(PAPERBALL_TEXTURE, Texture.class));
		sprBall.setBounds(-25.f, -25.f, 50.f, 50.f);
		sprBall.setOriginCenter();
		
		// dodanie dzwiekow do odegrania
		impactSounds.addAll(
			Game.assets.get(Vault.SOUND_PAPERHIT, Sound.class)
		);
	}
	
	/**
	 * @see Actor#update(float) 
	 * @param delta 
	 */
	@Override
	public void update(float delta) {
		if(paths.size == 0 || body.getPosition().scl(Physics.SCALE_INV).dst(paths.peek().position) > 25.f) {
			paths.add(new PathTrace(body.getPosition().scl(Physics.SCALE_INV), body.getLinearVelocity().angle()));
		}
	}
	
	/**
	 * @see Actor#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch) 
	 * @param batch 
	 */
	@Override
	public void draw(SpriteBatch batch) {
		sprBall.setCenter(
			body.getPosition().x * Physics.SCALE_INV,
			body.getPosition().y * Physics.SCALE_INV
		);
		
		// obroc sprite, zamieniajac radiany body na zwykly kat
		sprBall.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		
		// draw path
		Game.scene.gizmo.begin(ShapeRenderer.ShapeType.Line);
		Game.scene.gizmo.setColor(1.f, 1.f, 1.f, .5f);
		for(PathTrace pt : paths) {
			pt.drawUp(Game.scene.gizmo);
		}
		Game.scene.gizmo.end();
		
		// draw sprite
		batch.begin();
		sprBall.draw(batch);
		batch.end();
	}
	
	/**
	 * Remove actor on impact with WielokatActor.
	 * @param actor
	 * @param contact 
	 */
	@Override
	public void onHit(Actor actor, Contact contact) {
		if(actor instanceof DustbinActor) {
			if(Physics.OR(((DustbinActor)actor).fixture, contact)) {
				this.remove();
			}
		}
		
		if(actor instanceof ObstacleActor){
			((ObstacleActor)actor).playImpactSound();
		}
	}
	
	/**
	 * Nadaje sily aktorowi.
	 * @param newForce 
	 */
	public void applyForce(Vector2 newForce) {
		body.setTransform(body.getPosition(), (float)(Math.random()*Math.PI*2));
		body.applyForceToCenter(newForce, true);
	}
	
	/**
	 * Remove physic body from the world.
	 * @see Actor#dispose() 
	 */
	@Override
	public void dispose() {
		Game.physics.world.destroyBody(body);
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
}
