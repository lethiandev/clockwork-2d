/*
 * The MIT License
 *
 * Copyright 2015 Konrad Nowakowski <konrad.x92@gmail.com>.
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
package vault.clockwork.editor.gui;

/**
 *
 * @author Konrad Nowakowski <konrad.x92@gmail.com>
 */
public interface GUIActionListener {
	/**
	 * Dispatched on cursor enters over the element.
	 * @param element GUI target element.
	 */
	public void enter(GUIElement element);
	
	/**
	 * Dispatched on cursor leaves the element.
	 * @param element GUI target element.
	 */
	public void leave(GUIElement element);
	
	/**
	 * Dispatched on action performed.
	 * Basic befaviour means single mouse click over the element.
	 * @param element GUI target element.
	 */
	public void action(GUIElement element);
	
	/**
	 * Dispatched on focused keyboard typing.
	 * @param element GUI target element.
	 */
	public void typed(GUIElement element);
}
