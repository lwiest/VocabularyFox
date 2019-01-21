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

public class Question {
	private int index;
	private String question;
	private String expectedAnswer;
	private String actualAnswer;
	private Gender expectedGender;
	private Gender actualGender;
	private Grade grade;

	private Question(int index, String question, String expectedAnswer, String actualAnswer, Gender expectedGender, Gender actualGender, Grade grade) {
		this.index = index;
		this.question = question;
		this.expectedAnswer = expectedAnswer;
		this.actualAnswer = actualAnswer;
		this.expectedGender = expectedGender;
		this.actualGender = actualGender;
		this.grade = grade;
	}

	public Question(int index, String question, String expectedAnswer, Gender expectedGender) {
		this(index, question, expectedAnswer, "", expectedGender, Gender.INITIAL, Grade.INITIAL);
	}

	public int getIndex() {
		return this.index;
	}

	public String getQuestion() {
		return this.question;
	}

	public String getExpectedAnswer() {
		return this.expectedAnswer;
	}

	public void setActualAnswer(String actualAnswer) {
		this.actualAnswer = actualAnswer;
	}

	public String getActualAnswer() {
		return this.actualAnswer;
	}

	public void setActualGender(Gender actualGender) {
		this.actualGender = actualGender;
	}

	public Gender getExpectedGender() {
		return this.expectedGender;
	}

	public Gender getActualGender() {
		return this.actualGender;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Grade getGrade() {
		return this.grade;
	}
}
