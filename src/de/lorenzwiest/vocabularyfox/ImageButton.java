/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Lorenz Wiest
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package de.lorenzwiest.vocabularyfox;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class ImageButton extends Canvas {

	private enum MouseState {
		UP, //
		HOVER, //
		DOWN
	}

	private Image img;
	private Image hoverImg;
	private Image clickImg;

	private MouseState mouseState = MouseState.UP;

	private boolean isPressed = false;

	public ImageButton(Composite parent, int style) {
		super(parent, style);

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if ((ImageButton.this.mouseState == MouseState.UP) && (ImageButton.this.img != null)) {
					e.gc.drawImage(ImageButton.this.img, 0, 0);
				} else if ((ImageButton.this.mouseState == MouseState.HOVER) && (ImageButton.this.hoverImg != null)) {
					e.gc.drawImage(ImageButton.this.hoverImg, 0, 0);
				} else if ((ImageButton.this.mouseState == MouseState.DOWN) && (ImageButton.this.clickImg != null)) {
					e.gc.drawImage(ImageButton.this.clickImg, 0, 0);
				}
			}
		});
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if (ImageButton.this.isPressed == false) {
					return;
				}
				ImageButton.this.mouseState = MouseState.DOWN;
				if ((e.x < 0) || (e.y < 0) || (e.x > getBounds().width) || (e.y > getBounds().height)) {
					ImageButton.this.mouseState = MouseState.UP;
				}
				redraw();
			}
		});
		addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				ImageButton.this.mouseState = MouseState.HOVER;
				redraw();
			}

			@Override
			public void mouseExit(MouseEvent e) {
				ImageButton.this.mouseState = MouseState.UP;
				redraw();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				ImageButton.this.isPressed = true;
				ImageButton.this.mouseState = MouseState.DOWN;
				redraw();
			}

			@Override
			public void mouseUp(MouseEvent e) {
				ImageButton.this.isPressed = false;
				ImageButton.this.mouseState = MouseState.HOVER;
				if ((e.x < 0) || (e.y < 0) || (e.x > getBounds().width) || (e.y > getBounds().height)) {
					ImageButton.this.mouseState = MouseState.UP;
				}
				redraw();
				if (ImageButton.this.mouseState == MouseState.HOVER) {
					notifyListeners(SWT.Selection, new Event());
				}
			}
		});
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		if (this.img != null) {
			Rectangle bounds = this.img.getBounds();
			return new Point(bounds.width, bounds.height);
		} else {
			return new Point(0, 0);
		}
	}

	public void setImage(Image img) {
		this.img = img;
	}

	public void setHoverImage(Image img) {
		this.hoverImg = img;
	}

	public void setClickImage(Image img) {
		this.clickImg = img;
	}
}
