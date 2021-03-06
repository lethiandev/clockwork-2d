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
package vault.clockwork.screens;

import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import vault.clockwork.Game;
import vault.clockwork.Vault;
import vault.clockwork.actors.GameLogoActor;
import vault.clockwork.actors.GroundActor;
import vault.clockwork.actors.HandActor;
import vault.clockwork.actors.PaperBallActor;
import vault.clockwork.controllers.CameraController;
import vault.clockwork.controllers.MenuController;
import vault.clockwork.editor.PropHolder;
import vault.clockwork.editor.PropSerialized;
import vault.clockwork.scene.Actor;
import vault.clockwork.system.Scene;

/**
 * Playable stage screen.
 * @author Konrad Nowakowski https://github.com/konrad92
 */
public class StageScreen implements GameScreen {
	/**
	 * Sciezka do pliku poziomu.
	 */
	private final String filename;
	
	/**
	 * Kontroluje kamere, tj. podazanie za aktorem.
	 */
	private final CameraController camera = new CameraController();
	
	/**
	 * Ctor.
	 * @param filename 
	 */
	public StageScreen(String filename) {
		this.filename = Game.LEVELS_PATH + filename;
	}
	
	/**
	 * Preload all screen resources here.
	 * @see GameScreen#prepare() 
	 */
	@Override
	public void prepare() {
		Game.assets.load("assets/turret.png", Texture.class);
		Game.assets.load("assets/blueprint.png", Texture.class);
		Game.assets.load("assets/dragonball.png", Texture.class);
		Game.assets.load("assets/dbin.png", Texture.class);
                Game.assets.load("assets/dbinbg.png", Texture.class);
		Game.assets.load("assets/paperball.png", Texture.class);
		Game.assets.load("assets/longtrunk.png", Texture.class);
		Game.assets.load("assets/mediumtrunk.png", Texture.class);
		Game.assets.load("assets/shorttrunk.png", Texture.class);
		Game.assets.load("assets/planet.png", Texture.class);
		Game.assets.load("assets/space.png", Texture.class);
		Game.assets.load(Vault.BGA_DESERT, Texture.class);
		Game.assets.load(Vault.BGB_DESERT, Texture.class);
		Game.assets.load("assets/kamyk.png", Texture.class);
		Game.assets.load("assets/kamyk2.png", Texture.class);
		Game.assets.load("assets/kamyk3.png", Texture.class);
		Game.assets.load("assets/klocek.png", Texture.class);
		Game.assets.load("assets/poducha.png", Texture.class);
		Game.assets.load("assets/hill.png", Texture.class);
		Game.assets.load("assets/face.png", Texture.class);
		Game.assets.load("assets/rock.png", Texture.class);
		Game.assets.load("assets/stone.png", Texture.class);
		Game.assets.load(Vault.SOUND_PAPERHIT, Sound.class);
		Game.assets.load(Vault.SOUND_WOODBOUNCE, Sound.class);
		Game.assets.load(Vault.SOUND_KOSZ1, Sound.class);
		Game.assets.load(Vault.SOUND_KOSZ2, Sound.class);
		Game.assets.load(Vault.SOUND_KOSZ3, Sound.class);
		Game.assets.load(Vault.SOUND_KOSZ4, Sound.class);
		Game.assets.load(Vault.SOUND_KOSZ5, Sound.class);
		
		Game.assets.load(Vault.MENU_BACK_TO_MENU, Texture.class);
		
		// preload resources
		GroundActor.preload();
		GameLogoActor.preload();
		HandActor.preload();
		PaperBallActor.preload();
	}

	/**
	 * Prepare the scene to show-up.
	 */
	@Override
	public void show() {
		reConfigure();
		
		// add scene controllers
		Game.scene.controllers.add(camera);
		Game.scene.controllers.add(new MenuController());
		
		// register input processors
		Game.inputMultiplexer.addProcessor(camera);
		
		// wczytaj scene
		this.load(filename);
	}

	/**
	 * Update screen logic and perform the systems.
	 * @see Screen#render(float) 
	 */
	@Override
	public void render(float delta) {
        // clear target buffer
        gl.glClearColor(0.1f, 0.2f, 0.1f, 1.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// update shaders
		if(Vault.comicShader.isCompiled()) {
			Vault.comicShader.begin();
			Vault.comicShader.setUniformf("u_ticks", (float)(Math.random()*2*Math.PI));
			Vault.comicShader.setUniformf("u_strength",(float)(Math.random()*0.0015f));
			Vault.comicShader.end();
		}
		
		// perform game systems
		Game.performSystems();
	}
	
	/**
	 * Wczytaj scene z pliku.
	 * @param filename Sciezka do poziomu.
	 */
	public void load(String filename) {
		PropHolder props = PropHolder.load(filename);
		
		// sortuj aktorow po ID
		props.sort();
		
		// instance props onto the scene
		for(PropSerialized prop : props) {
			Actor actor = (Actor)prop.instance();
			
			// place the instanced actor
			if(actor != null) {
				Scene.Layer layer = Game.scene.BACKGROUND;
				
				// select layer
				switch(prop.layer) {
					case 1: layer = Game.scene.ACTION_1; break;
					case 2: layer = Game.scene.ACTION_2; break;
					case 3: layer = Game.scene.ACTION_3; break;
					case 4: layer = Game.scene.FOREGROUND; break;
					case 5: layer = Game.scene.GUI;break;
					case 6: layer = Game.scene.DEBUG; break;
				}
				
				// place the actor
				layer.add(actor);
			}
		}
	}
}
