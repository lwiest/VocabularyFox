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

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class Utils {

	public static void setButtonDefaultFontAndWidth(Button button) {
		Font defaultFont = Resources.getFont(Resources.FONT_DEFAULT);
		button.setFont(defaultFont);

		GC gc = new GC(button);
		gc.setFont(defaultFont);
		FontMetrics fontMetrics = gc.getFontMetrics();
		int widthHint = Dialog.convertVerticalDLUsToPixels(fontMetrics, 45 /* IDialogConstants.BUTTON_WIDTH */);
		gc.dispose();

		Point defaultSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		int newWidth = Math.max(widthHint, defaultSize.x);
		((GridData) button.getLayoutData()).widthHint = newWidth;
	}

	public static int scaleToDisplay(int value) {
		final int WINDOWS_OS_DEFAULT_DISPLAY_RESOLUTION = 96;
		final int[] WINDOWS_OS_DISPLAY_RESOLUTIONS = { 96, 120, 144, 192 };

		int displayResolution = Display.getCurrent().getDPI().x;
		// hack to detect Windows OS, for which we scale the value only
		if (Arrays.binarySearch(WINDOWS_OS_DISPLAY_RESOLUTIONS, displayResolution) > 0) {
			int scaledValue = (int) (((value / (float) WINDOWS_OS_DEFAULT_DISPLAY_RESOLUTION) * displayResolution) + 0.5);
			return scaledValue;
		}
		return value;
	}

	public static String base64Encode(byte[] bytes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int numTripleBytes = (bytes.length / 3) * 3;
		for (int i = 0; i < numTripleBytes; i += 3) {
			writeTriple(out, 3, bytes[i], bytes[i + 1], bytes[i + 2]);
		}

		int numRestBytes = bytes.length % 3;
		if (numRestBytes == 2) {
			writeTriple(out, numRestBytes, bytes[numTripleBytes], bytes[numTripleBytes + 1], 0);
			out.write('=');
		} else if (numRestBytes == 1) {
			writeTriple(out, numRestBytes, bytes[numTripleBytes], 0, 0);
			out.write('=');
			out.write('=');
		}
		return out.toString();
	}

	private static final String BASE64_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	private static void writeTriple(ByteArrayOutputStream out, int numBytes, int b1, int b2, int b3) {
		int threeBytes = ((b1 & 0xff) << 16) | ((b2 & 0xff) << 8) | (b3 & 0xff);
		int shiftRight = 18;
		for (int i = numBytes; i >= 0; i--) {
			int index = (threeBytes >> shiftRight) & 0x3f;
			int code = BASE64_CHARACTERS.charAt(index);
			out.write(code);
			shiftRight -= 6;
		}
	}
}
