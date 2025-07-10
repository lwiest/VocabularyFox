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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Quiz {
	private String key;
	private Language targetLanguage;
	private String unit;
	private String title;
	private List<Question> questions;

	public Quiz(String key, Language targetLanguage, String unit, String title, List<Question> questions) {
		this.key = key;
		this.targetLanguage = targetLanguage;
		this.unit = unit;
		this.title = title;
		this.questions = questions;
	}

	public String getKey() {
		return this.key;
	}

	public Language getTargetLanguage() {
		return this.targetLanguage;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return this.unit;
	}

	public String getTitle() {
		return this.title;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public Quiz createInitializedCopy() {
		String newKey = getKey();
		String newUnit = getUnit();
		String newTitle = getTitle();
		Language newTargetLanguage = getTargetLanguage();
		List<Question> newQuestions = new ArrayList<Question>();

		for (int i = 0; i < getQuestions().size(); i++) {
			Question question = getQuestions().get(i);
			String newStrQuestion = question.getQuestion();
			String newStrExpectedAnswer = question.getExpectedAnswer();
			Gender newExpectedGender = question.getExpectedGender();
			Question newQuestion = new Question(i, newStrQuestion, newStrExpectedAnswer, newExpectedGender);
			newQuestions.add(newQuestion);
		}

		Quiz newQuiz = new Quiz(newKey, newTargetLanguage, newUnit, newTitle, newQuestions);
		return newQuiz;
	}

	private static final String BOM_UTF_8 = "\uFEFF";
	private static final String STR_COMMENT_INTRO = "//";
	private static final String ATTRIB_KEY = "@Key=";
	private static final String ATTRIB_TITLE = "@Title=";
	private static final String ATTRIB_UNIT = "@Unit=";
	private static final String ATTRIB_TARGET_LANGUAGE = "@TargetLanguage=";
	private static final String ATTRIB_ATTRIB = "@";
	private static final String VALUE_EN = "EN";
	private static final String VALUE_FR = "FR";
	private static final String PROP_GENDER_M = "@M@";
	private static final String PROP_GENDER_F = "@F@";
	private static final String SEPARATOR = "|";

	public static Quiz readQuiz(File quizFile) {
		String key = null;
		Language targetLanguage = null;
		String unit = null;
		String title = null;
		List<Question> questions = new ArrayList<Question>();

		try {
			List<String> lines = Files.readAllLines(quizFile.toPath(), StandardCharsets.UTF_8);

			// remove UTF-8 Byte-Order-Mark (BOM), if present
			lines.set(0, lines.get(0).replace(BOM_UTF_8, ""));

			int questionIndex = 0;
			for (String line : lines) {
				if (line.contains(STR_COMMENT_INTRO)) {
					int pos = line.indexOf(STR_COMMENT_INTRO);
					line = line.substring(0, pos);
				}

				if (line.startsWith(ATTRIB_KEY)) {
					key = line.substring(ATTRIB_KEY.length()).trim();
					continue;
				} else if (line.startsWith(ATTRIB_TITLE)) {
					title = line.substring(ATTRIB_TITLE.length()).trim();
					continue;
				} else if (line.startsWith(ATTRIB_UNIT)) {
					unit = line.substring(ATTRIB_UNIT.length()).trim();
					continue;
				} else if (line.startsWith(ATTRIB_TARGET_LANGUAGE)) {
					String strTargetLanguage = line.substring(ATTRIB_TARGET_LANGUAGE.length()).trim();
					if (strTargetLanguage.equals(VALUE_EN)) {
						targetLanguage = Language.EN;
					} else if (strTargetLanguage.equals(VALUE_FR)) {
						targetLanguage = Language.FR;
					}
					continue;
				} else if (line.startsWith(ATTRIB_ATTRIB)) {
					continue;
				} else if (line.trim().isEmpty()) {
					continue;
				}

				int sepPos = line.indexOf(SEPARATOR);
				if (sepPos < 0) {
					continue;
				}
				String strExpectedAnswer = line.substring(0, sepPos).trim();

				Gender expectedGender = Gender.DONT_CARE;
				if (strExpectedAnswer.contains(PROP_GENDER_M)) {
					expectedGender = Gender.MASCULINE;
					strExpectedAnswer = strExpectedAnswer.replace(PROP_GENDER_M, "").trim();
				} else if (strExpectedAnswer.contains(PROP_GENDER_F)) {
					expectedGender = Gender.FEMININE;
					strExpectedAnswer = strExpectedAnswer.replace(PROP_GENDER_F, "").trim();
				}

				String strQuestion = line.substring(sepPos + 1).trim();
				questions.add(new Question(questionIndex, strQuestion, strExpectedAnswer, expectedGender));
				questionIndex++;
			}
		} catch (IOException e) {
			System.out.println(e);
		}

		Quiz quiz = new Quiz(key, targetLanguage, unit, title, questions);
		return quiz;
	}

	public void sortQuestionsByIndex() {
		this.questions.sort(new Comparator<Question>() {
			@Override
			public int compare(Question question1, Question question2) {
				int index1 = question1.getIndex();
				int index2 = question2.getIndex();
				return index1 - index2;
			}
		});
	}

	public void shuffleQuestions() {
		int size = this.questions.size();
		for (int oldIndex = 0; oldIndex < size; oldIndex++) {
			Question oldQuestion = this.questions.get(oldIndex);
			int newIndex = (int) (Math.random() * size);
			Question newQuestion = this.questions.get(newIndex);
			this.questions.set(oldIndex, newQuestion);
			this.questions.set(newIndex, oldQuestion);
		}
	}
}
