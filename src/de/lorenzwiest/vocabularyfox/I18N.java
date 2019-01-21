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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18N {
	public static final Locale LOCALE_DE = new Locale("de");
	public static final Locale LOCALE_US = new Locale("en", "US");

	public static final String ALL_WORDS = "ALL_WORDS";
	public static final String APPLICATION_TITLE = "APPLICATION_TITLE";
	public static final String CANCEL = "CANCEL";
	public static final String CLEAR = "CLEAR";
	public static final String CORRECT_BUT_DIFFERENT_WORD = "CORRECT_BUT_DIFFERENT_WORD";
	public static final String DO_YOU_WANT_TO_END_QUIZ = "DO_YOU_WANT_TO_END_QUIZ";
	public static final String DONT_SHOW_TIP = "DONT_SHOW_TIP";
	public static final String END = "END";
	public static final String END_QUIZ = "END_QUIZ";
	public static final String ENGLISH = "ENGLISH";
	public static final String EVALUATION = "EVALUATION";
	public static final String EVALUATION_FILENAME_PREFIX = "EVALUATION_FILENAME_PREFIX";
	public static final String EXAM = "EXAM";
	public static final String EXAM_1 = "EXAM_1";
	public static final String EXAM_2 = "EXAM_2";
	public static final String FULL_DATE_FORMAT = "FULL_DATE_FORMAT";
	public static final String GERMAN = "GERMAN";
	public static final String HELLO = "HELLO";
	public static final String HOME_WORK = "HOME_WORK";
	public static final String HOME_WORK_1 = "HOME_WORK_1";
	public static final String HOME_WORK_2 = "HOME_WORK_2";
	public static final String HOME_WORK_3 = "HOME_WORK_3";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String N_OF_M = "N_OF_M";
	public static final String N_PERCENT_CORRECT = "N_PERCENT_CORRECT";
	public static final String NEW_QUIZ = "NEW_QUIZ";
	public static final String NEXT = "NEXT";
	public static final String OK = "OK";
	public static final String OR = "OR";
	public static final String PREFERENCES = "PREFERENCES";
	public static final String QUESTION = "QUESTION";
	public static final String QUIZ_TYPE = "QUIZ_TYPE";
	public static final String REAL_FOX = "REAL_FOX";
	public static final String REPEAT_QUIZ = "REPEAT_QUIZ";
	public static final String RESUME = "RESUME";
	public static final String SAVE_QUIZ_RESULTS = "SAVE_QUIZ_RESULTS";
	public static final String SEARCH_QUIZ = "SEARCH_QUIZ";
	public static final String SELECTION = "SELECTION";
	public static final String SHOW_ALL_TIPS = "SHOW_ALL_TIPS";
	public static final String SHUFFLE_WORDS = "SHUFFLE_WORDS";
	public static final String SKIP = "SKIP";
	public static final String START_QUIZ = "START_QUIZ";
	public static final String TEST = "TEST";
	public static final String TEST_1 = "TEST_1";
	public static final String TEST_2 = "TEST_2";
	public static final String TIP = "TIP";
	public static final String TIPS = "TIPS";
	public static final String TITLE = "TITLE";
	public static final String UNIT = "UNIT";
	public static final String USE_FOLLOWING_ABBREVIATIONS = "USE_FOLLOWING_ABBREVIATIONS";
	public static final String VERSION = "VERSION";
	public static final String WHICH_WORDS_TO_ASK_AGAIN = "WHICH_WORDS_TO_ASK_AGAIN";
	public static final String WRONG_0 = "WRONG_0";
	public static final String WRONG_1 = "WRONG_1";
	public static final String WRONG_2 = "WRONG_2";
	public static final String WRONG_TRY_AGAIN_0 = "WRONG_TRY_AGAIN_0";
	public static final String WRONG_TRY_AGAIN_1 = "WRONG_TRY_AGAIN_1";
	public static final String WRONG_TRY_AGAIN_2 = "WRONG_TRY_AGAIN_2";
	public static final String WRONG_TRY_AGAIN_3 = "WRONG_TRY_AGAIN_3";
	public static final String WRONG_TRY_AGAIN_4 = "WRONG_TRY_AGAIN_4";
	public static final String WRONG_TRY_AGAIN_5 = "WRONG_TRY_AGAIN_5";
	public static final String WRONG_WORDS_ONLY = "WRONG_WORDS_ONLY";

	private static final String[] EMPTY_ARRAY = new String[0];

	private static Locale locale;

	private static ResourceBundle resourceBundle;

	private I18N() {
		// can't be instantiated
	}

	static {
		setLocale(getDefaultLocale());
	}

	public static Locale getDefaultLocale() {
		Locale locale = Locale.getDefault();
		if (locale.getLanguage().toString().equals(I18N.LOCALE_DE.toString())) {
			locale = I18N.LOCALE_DE;
		} else {
			locale = I18N.LOCALE_US;
		}
		return locale;
	}

	public static void setLocale(Locale locale) {
		I18N.locale = locale;
		I18N.resourceBundle = ResourceBundle.getBundle("messages", locale);
	}

	public static Locale getLocale() {
		return I18N.locale;
	}

	public static String getString(String key) {
		return getString(key, EMPTY_ARRAY);
	}

	public static String getString(String key, String... args) {
		try {
			String result = I18N.resourceBundle.getString(key);
			for (int i = 0; i < args.length; i++) {
				String placeholder = "{" + i + "}";
				String replaceWith = args[i];
				result = result.replace(placeholder, replaceWith);
			}
			return result;
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getFullDate(Date date) {
		String fullDateFormat = I18N.resourceBundle.getString(I18N.FULL_DATE_FORMAT);
		return new SimpleDateFormat(fullDateFormat, I18N.locale).format(date);
	}
}
