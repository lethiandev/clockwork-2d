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
package vault.clockwork.editor.props;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import vault.clockwork.Vault;
import vault.clockwork.actors.BackgroundActor;
import vault.clockwork.editor.PropActor;
import vault.clockwork.editor.PropSerialized;

/**
 * General turret editor prop.
 * @author Konrad Nowakowski https://github.com/konrad92
 */
public class BackgroundProp extends PropSerialized {
	/**
	 * Background path.
	 */
	public String background = Vault.BGA_DESERT;
	
	/**
	 * Przesuniecie tla.
	 */
	public float offset_x = 0.f, offset_y = 0.f;
	
	/**
	 * Background parallax multiplier.
	 * Effective range between 0.0 - 1.0
	 */
	public float parallax_x = 1.f, parallax_y = 1.f;
	
	/**
	 * Skala (rozciagniecie) tla.
	 */
	public float scaled = 1.f;
	
	/**
	 * Multiplikator odleglosci kamery.
	 */
	public float zoomed = 1.f;
	
	/**
	 * Ctor.
	 */
	public BackgroundProp() {
		this.layer = 0;
	}
	
	/**
	 * Draw the turret radius as bounds.
	 * @param gizmo 
	 */
	@Override
	public void draw(ShapeRenderer gizmo) {
	}

	/**
	 * TurretActor class.
	 * @return 
	 */
	@Override
	public Class<? extends PropActor> getActorClass() {
		return BackgroundActor.class;
	}
}
