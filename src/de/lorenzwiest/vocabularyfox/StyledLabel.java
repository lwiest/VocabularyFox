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

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;

public class StyledLabel extends StyledText {

	public StyledLabel(Composite parent, int style) {
		super(parent, style);
		setEditable(false);
		setEnabled(false);
	}

	public void setStyledText(String text) {
		setStyledText(text, Resources.TS_DEFAULT);
	}

	public void setStyledText(String text, TextStyle textStyle) {
		setText("");
		setStyleRange(null);
		addStyledText(text, textStyle);
	}

	public void setStyledText(StyledString styledString) {
		setText(styledString.getText());
		setStyleRanges(styledString.getStyleRanges());
	}

	public void addStyledText(String text) {
		addStyledText(text, Resources.TS_DEFAULT);
	}

	public void addStyledText(String text, TextStyle textStyle) {
		String oldText = getText();
		StyleRange[] oldStyleRanges = this.getStyleRanges(true);

		int newOffset = oldText.length();
		String newText = oldText + text;
		setText(newText); // always before setStyleRanges()

		if (textStyle == null) {
			textStyle = new TextStyle();
		}
		StyleRange styleRange = new StyleRange(textStyle);
		styleRange.start = newOffset;
		styleRange.length = text.length();

		StyleRange[] newStyleRanges = new StyleRange[oldStyleRanges.length + 1];
		System.arraycopy(oldStyleRanges, 0, newStyleRanges, 0, oldStyleRanges.length);
		newStyleRanges[newStyleRanges.length - 1] = styleRange;

		setStyleRanges(newStyleRanges);
	}
}
