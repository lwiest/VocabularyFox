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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class Resources {
	public static final int INDENT = Utils.scaleToDisplay(10);
	public static final int HALF_INDENT = INDENT / 2;

	private static final String CR = System.getProperty("line.separator");

	public static final String PREFS_FILENAME = ".preferences_vocabularyfox";
	public static final String QUIZ_FOLDERNAME = "quizzes";
	public static final String SAMPLE_QUIZ_FILENAME = "sample_quiz.txt";
	public static final String SAMPLE_QUIZ_SOURCEPATH = "samples/sample_quiz.txt";

	private static FontRegistry FONT_REGISTRY;

	public static final String FONT_NAME = "Calibri";

	public static final String FONT_TITLE = "font-title";
	public static final String FONT_SEMI_TITLE = "font-semi-title";
	public static final String FONT_SEMI_TITLE_BOLD = "font-semi-title-bold";
	public static final String FONT_SEMI_TITLE_BOLD_ITALICS = "font-semi-title-bold-italics";
	public static final String FONT_DEFAULT = "font-default";
	public static final String FONT_DEFAULT_BOLD = "font-default-bold";
	public static final String FONT_LETTER_PICKER = "font-letter-picker";

	private static final Map<String, TextStyle> TEXT_STYLE_REGISTRY = new HashMap<String, TextStyle>();

	public static TextStyle TS_TITLE;
	public static TextStyle TS_SEMI_TITLE;
	public static TextStyle TS_SEMI_TITLE_BOLD;
	public static TextStyle TS_SEMI_TITLE_GREY;
	public static TextStyle TS_DEFAULT;
	public static TextStyle TS_DEFAULT_BOLD;
	public static TextStyle TS_M_BOLD;
	public static TextStyle TS_F_BOLD;

	public static TextStyle TS_ERROR_MARKER_WRONG;
	public static TextStyle TS_ERROR_MARKER_ALMOST_CORRECT;

	public static TextStyle TS_GREY;

	public static TextStyle TS_SEMI_TITLE_M_BOLD;
	public static TextStyle TS_SEMI_TITLE_F_BOLD;

	public static TextStyle TS_DEFAULT_WRONG;
	public static TextStyle TS_M_WRONG;
	public static TextStyle TS_F_WRONG;

	private static ColorRegistry COLOR_REGISTRY;

	public static final String COLOR_WHITE = "white";
	public static final String COLOR_RED = "red";
	public static final String COLOR_FOX_ORANGE = "fox-orange";
	public static final String COLOR_LIGHT_FOX_ORANGE = "fox-orange-light";
	public static final String COLOR_ORANGE = "orange";
	public static final String COLOR_BLACK = "black";
	public static final String COLOR_GREY = "grey";
	public static final String COLOR_LIGHT_GREY = "grey-light";
	public static final String COLOR_M = "m";
	public static final String COLOR_M_HOVER = "m-hover";
	public static final String COLOR_M_DARK = "m-dark";
	public static final String COLOR_F = "f";
	public static final String COLOR_F_HOVER = "f-hover";
	public static final String COLOR_F_DARK = "f-dark";
	public static final String COLOR_LETTER_PICKER = "letter-picker";
	public static final String COLOR_LETTER_PICKER_HOVER = "letter-picker-hover";
	public static final String COLOR_LETTER_PICKER_CLICK = "letter-pciker-click";

	private static final Map<String, Image> IMAGE_REGISTRY = new HashMap<String, Image>();

	public static final String IMG_SPACER = "icons/spacer1x28.png";
	public static final String IMG_GEAR = "icons/gear28x28.png";
	public static final String IMG_GEAR_CLICK = "icons/gear28x28_click.png";
	public static final String IMG_GEAR_HOVER = "icons/gear28x28_hover.png";
	public static final String IMG_DOWNLOAD_PAGE = "icons/downloadpage28x28.png";
	public static final String IMG_DOWNLOAD_PAGE_CLICK = "icons/downloadpage28x28_click.png";
	public static final String IMG_DOWNLOAD_PAGE_HOVER = "icons/downloadpage28x28_hover.png";
	public static final String IMG_CLEAR_ENABLED = "icons/clear_enabled.gif";
	public static final String IMG_ENGLISH15x15 = "icons/english15x15.png";
	public static final String IMG_FRENCH15x15 = "icons/french15x15.png";
	public static final String IMG_ENGLISH16x16 = "icons/english16x16.png";
	public static final String IMG_FRENCH16x16 = "icons/french16x16.png";
	public static final String IMG_FOLDER16x16 = "icons/folder16x16.png";
	public static final String IMG_FOX16x16 = "icons/fox16x16.png";
	public static final String IMG_FOX32x32 = "icons/fox32x32.png";
	public static final String IMG_FOX32x32_QUESTION = "icons/fox32x32_question.png";
	public static final String IMG_FOX32x32_STAR = "icons/fox32x32_star.png";
	public static final String IMG_FOX32x32_EXCLAMATION = "icons/fox32x32_exclamation.png";
	public static final String IMG_FOX48x48 = "icons/fox48x48.png";

	static {
		IMAGE_REGISTRY.put(IMG_SPACER, Resources.readImage(IMG_SPACER));
		IMAGE_REGISTRY.put(IMG_GEAR, Resources.readImage(IMG_GEAR));
		IMAGE_REGISTRY.put(IMG_GEAR_CLICK, Resources.readImage(IMG_GEAR_CLICK));
		IMAGE_REGISTRY.put(IMG_GEAR_HOVER, Resources.readImage(IMG_GEAR_HOVER));
		IMAGE_REGISTRY.put(IMG_DOWNLOAD_PAGE, Resources.readImage(IMG_DOWNLOAD_PAGE));
		IMAGE_REGISTRY.put(IMG_DOWNLOAD_PAGE_CLICK, Resources.readImage(IMG_DOWNLOAD_PAGE_CLICK));
		IMAGE_REGISTRY.put(IMG_DOWNLOAD_PAGE_HOVER, Resources.readImage(IMG_DOWNLOAD_PAGE_HOVER));
		IMAGE_REGISTRY.put(IMG_CLEAR_ENABLED, Resources.readImage(IMG_CLEAR_ENABLED));
		IMAGE_REGISTRY.put(IMG_FRENCH15x15, Resources.readImage(IMG_FRENCH15x15));
		IMAGE_REGISTRY.put(IMG_ENGLISH15x15, Resources.readImage(IMG_ENGLISH15x15));
		IMAGE_REGISTRY.put(IMG_FRENCH16x16, Resources.readImage(IMG_FRENCH16x16));
		IMAGE_REGISTRY.put(IMG_ENGLISH16x16, Resources.readImage(IMG_ENGLISH16x16));
		IMAGE_REGISTRY.put(IMG_FOLDER16x16, Resources.readImage(IMG_FOLDER16x16));
		IMAGE_REGISTRY.put(IMG_FOX16x16, Resources.readImage(IMG_FOX16x16));
		IMAGE_REGISTRY.put(IMG_FOX32x32, Resources.readImage(IMG_FOX32x32));
		IMAGE_REGISTRY.put(IMG_FOX32x32_QUESTION, Resources.readImage(IMG_FOX32x32_QUESTION));
		IMAGE_REGISTRY.put(IMG_FOX32x32_STAR, Resources.readImage(IMG_FOX32x32_STAR));
		IMAGE_REGISTRY.put(IMG_FOX32x32_EXCLAMATION, Resources.readImage(IMG_FOX32x32_EXCLAMATION));
		IMAGE_REGISTRY.put(IMG_FOX48x48, Resources.readImage(IMG_FOX48x48));

		COLOR_REGISTRY = new ColorRegistry(Display.getCurrent());
		COLOR_REGISTRY.put(COLOR_WHITE, new RGB(255, 255, 255));
		COLOR_REGISTRY.put(COLOR_GREY, new RGB(180, 180, 180));
		COLOR_REGISTRY.put(COLOR_BLACK, new RGB(0, 0, 0));
		COLOR_REGISTRY.put(COLOR_RED, new RGB(220, 57, 18));
		COLOR_REGISTRY.put(COLOR_ORANGE, new RGB(255, 153, 0));

		COLOR_REGISTRY.put(COLOR_M, new RGB(51, 102, 204));
		COLOR_REGISTRY.put(COLOR_M_HOVER, new RGB(85, 140, 252));
		COLOR_REGISTRY.put(COLOR_M_DARK, new RGB(1, 52, 154));

		COLOR_REGISTRY.put(COLOR_F, new RGB(184, 46, 46));
		COLOR_REGISTRY.put(COLOR_F_HOVER, new RGB(230, 79, 79));
		COLOR_REGISTRY.put(COLOR_F_DARK, new RGB(134, 0, 0));

		COLOR_REGISTRY.put(COLOR_LETTER_PICKER, new RGB(229, 243, 251));
		COLOR_REGISTRY.put(COLOR_LETTER_PICKER_HOVER, new RGB(203, 232, 246));
		COLOR_REGISTRY.put(COLOR_LETTER_PICKER_CLICK, new RGB(203, 232, 246));

		COLOR_REGISTRY.put(COLOR_FOX_ORANGE, new RGB(249, 113, 6));
		COLOR_REGISTRY.put(COLOR_LIGHT_FOX_ORANGE, new RGB(255, 223, 198));
		COLOR_REGISTRY.put(COLOR_LIGHT_GREY, new RGB(172, 172, 172));

		FONT_REGISTRY = new FontRegistry(Display.getCurrent());
		FONT_REGISTRY.put(FONT_TITLE, new FontData[] { new FontData(FONT_NAME, 30, SWT.NORMAL) });
		FONT_REGISTRY.put(FONT_SEMI_TITLE, new FontData[] { new FontData(FONT_NAME, 16, SWT.NORMAL) });
		FONT_REGISTRY.put(FONT_SEMI_TITLE_BOLD, new FontData[] { new FontData(FONT_NAME, 16, SWT.BOLD) });
		FONT_REGISTRY.put(FONT_SEMI_TITLE_BOLD_ITALICS, new FontData[] { new FontData(FONT_NAME, 16, SWT.BOLD | SWT.ITALIC) });
		FONT_REGISTRY.put(FONT_DEFAULT, new FontData[] { new FontData(FONT_NAME, 11, SWT.NORMAL) });
		FONT_REGISTRY.put(FONT_DEFAULT_BOLD, new FontData[] { new FontData(FONT_NAME, 11, SWT.BOLD) });
		FONT_REGISTRY.put(FONT_LETTER_PICKER, new FontData[] { new FontData(FONT_NAME, 20, SWT.BOLD) });

		TS_TITLE = new TextStyle();
		TS_TITLE.font = FONT_REGISTRY.get(FONT_TITLE);

		TS_SEMI_TITLE = new TextStyle();
		TS_SEMI_TITLE.font = FONT_REGISTRY.get(FONT_SEMI_TITLE);

		TS_SEMI_TITLE_BOLD = new TextStyle();
		TS_SEMI_TITLE_BOLD.font = FONT_REGISTRY.get(FONT_SEMI_TITLE_BOLD);

		TS_SEMI_TITLE_M_BOLD = new TextStyle();
		TS_SEMI_TITLE_M_BOLD.foreground = Resources.getColor(COLOR_M);
		TS_SEMI_TITLE_M_BOLD.font = FONT_REGISTRY.get(FONT_SEMI_TITLE_BOLD);

		TS_SEMI_TITLE_F_BOLD = new TextStyle();
		TS_SEMI_TITLE_F_BOLD.foreground = Resources.getColor(COLOR_F);
		TS_SEMI_TITLE_F_BOLD.font = FONT_REGISTRY.get(FONT_SEMI_TITLE_BOLD);

		TS_SEMI_TITLE_GREY = new TextStyle();
		TS_SEMI_TITLE_GREY.font = FONT_REGISTRY.get(FONT_SEMI_TITLE);
		TS_SEMI_TITLE_GREY.foreground = Resources.getColor(COLOR_GREY);

		TS_DEFAULT = new TextStyle();
		TS_DEFAULT.font = FONT_REGISTRY.get(FONT_DEFAULT);

		TS_DEFAULT_WRONG = new TextStyle();
		TS_DEFAULT_WRONG.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);
		TS_DEFAULT_WRONG.foreground = Resources.getColor(COLOR_RED);
		TS_DEFAULT_WRONG.strikeout = true;

		TS_M_WRONG = new TextStyle();
		TS_M_WRONG.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);
		TS_M_WRONG.foreground = Resources.getColor(COLOR_M);
		TS_M_WRONG.strikeout = true;

		TS_F_WRONG = new TextStyle();
		TS_F_WRONG.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);
		TS_F_WRONG.foreground = Resources.getColor(COLOR_F);
		TS_F_WRONG.strikeout = true;

		TS_DEFAULT_BOLD = new TextStyle();
		TS_DEFAULT_BOLD.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);

		TS_M_BOLD = new TextStyle();
		TS_M_BOLD.foreground = Resources.getColor(COLOR_M);
		TS_M_BOLD.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);

		TS_F_BOLD = new TextStyle();
		TS_F_BOLD.foreground = Resources.getColor(COLOR_F);
		TS_F_BOLD.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);

		TS_ERROR_MARKER_WRONG = new TextStyle();
		TS_ERROR_MARKER_WRONG.foreground = Resources.getColor(COLOR_RED);
		TS_ERROR_MARKER_WRONG.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);

		TS_ERROR_MARKER_ALMOST_CORRECT = new TextStyle();
		TS_ERROR_MARKER_ALMOST_CORRECT.foreground = Resources.getColor(COLOR_ORANGE);
		TS_ERROR_MARKER_ALMOST_CORRECT.font = FONT_REGISTRY.get(FONT_DEFAULT_BOLD);

		TS_GREY = new TextStyle();
		TS_GREY.foreground = Resources.getColor(COLOR_GREY);
		TS_GREY.font = FONT_REGISTRY.get(FONT_DEFAULT);
	}

	public static TextStyle getTextStyle(String textStyle) {
		if (TEXT_STYLE_REGISTRY.containsKey(textStyle)) {
			return TEXT_STYLE_REGISTRY.get(textStyle);
		} else {
			System.out.println("ERROR: Text style registry does not contain text style \"" + textStyle + "\".");
			return null;
		}
	}

	public static void putColor(String colorId, Color color) {
		COLOR_REGISTRY.put(colorId, color.getRGB());
	}

	public static Color getColor(String colorId) {
		if (containsColor(colorId)) {
			return COLOR_REGISTRY.get(colorId);
		} else {
			System.out.println("ERROR: Color registry does not contain color \"" + colorId + "\".");
			return null;
		}
	}

	public static boolean containsColor(String colorId) {
		return COLOR_REGISTRY.hasValueFor(colorId);
	}

	public static Font getFont(String fontId) {
		if (FONT_REGISTRY.hasValueFor(fontId)) {
			return FONT_REGISTRY.get(fontId);
		} else {
			System.out.println("ERROR: Font registry does not contain font \"" + fontId + "\".");
			return null;
		}
	}

	public static Image getImage(String imageId) {
		if (IMAGE_REGISTRY.containsKey(imageId)) {
			return IMAGE_REGISTRY.get(imageId);
		} else {
			System.out.println("ERROR: Image registry does not contain image \"" + imageId + "\".");
			return null;
		}
	}

	public static String readTextFile(String filepath) {
		StringBuffer buffer = new StringBuffer();

		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filepath);
		BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		try {
			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				buffer.append(line + CR);
			}
		} catch (IOException e) {
			// ignore
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e2) {
					// ignore
				}
			}
		}
		return buffer.toString();
	}

	private static Image readImage(String filepath) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(filepath);
		Image image = new Image(Display.getCurrent(), stream);
		return image;
	}

	private Resources() {
		// can't be instantiated
	}
}