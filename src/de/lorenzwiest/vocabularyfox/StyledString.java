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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.TextStyle;

public class StyledString {
	private String text;
	private StyleRange[] styleRanges;

	public static StyledString parse(String text, TextStyle textStyleNormal, TextStyle textStyleGrey) {
		StyledString styledString = new StyledString();

		List<String> frags = new ArrayList<String>(Arrays.asList(text.split("_")));
		boolean isGrey = text.startsWith("_");

		if (isGrey) {
			frags.remove(0);
		}

		for (String frag : frags) {
			TextStyle textStyle = isGrey ? textStyleGrey : textStyleNormal;
			styledString.addStyledText(frag, textStyle);
			isGrey = !isGrey;
		}

		return styledString;
	}

	public void addStyledText(String text) {
		addStyledText(text, Resources.TS_DEFAULT);
	}

	public void addStyledText(String text, TextStyle textStyle) {
		if (this.text == null) {
			this.text = "";
		}
		if (this.styleRanges == null) {
			this.styleRanges = new StyleRange[0];
		}

		String oldText = this.text;
		StyleRange[] oldStyleRanges = this.styleRanges;

		int newOffset = oldText.length();
		this.text += text;

		if (textStyle == null) {
			textStyle = new TextStyle();
		}
		StyleRange styleRange = new StyleRange(textStyle);
		styleRange.start = newOffset;
		styleRange.length = text.length();

		StyleRange[] newStyleRanges = new StyleRange[oldStyleRanges.length + 1];
		System.arraycopy(oldStyleRanges, 0, newStyleRanges, 0, oldStyleRanges.length);
		newStyleRanges[newStyleRanges.length - 1] = styleRange;

		this.styleRanges = newStyleRanges;
	}

	public String getText() {
		return this.text;
	}

	public StyleRange[] getStyleRanges() {
		return this.styleRanges;
	}
}
