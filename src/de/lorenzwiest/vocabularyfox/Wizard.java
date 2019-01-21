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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.PageBook;

public class Wizard {
	private PageBook pageBook;
	private List<WizardPage> pages = new ArrayList<WizardPage>();
	private Map<WizardPage, Control> pageControls = new HashMap<WizardPage, Control>();
	private WizardPage currentPage;
	private Quiz quiz;

	public Wizard(PageBook pageBook) {
		this.pageBook = pageBook;
	}

	public void open() {
		setPage(this.pages.get(0));
	}

	public void previousPage() {
		setPage(this.currentPage.getPreviousPage());
	}

	public void nextPage() {
		setPage(this.currentPage.getNextPage());
	}

	public void addPage(WizardPage page) {
		this.pages.add(page);
		Control control = page.createContent(this.pageBook, SWT.NONE);
		this.pageControls.put(page, control);
	}

	private void setPage(WizardPage page) {
		if (this.currentPage != null) {
			this.currentPage.unfocus();
		}

		Control pageControl = this.pageControls.get(page);
		this.pageBook.showPage(pageControl);

		this.currentPage = page;
		this.currentPage.focus();
	}

	public void setQuiz(Quiz quiz) {
		boolean isShuffleQuestions = PreferencesDialog.getDefaultShuffleQuestions();
		if (isShuffleQuestions) {
			quiz.shuffleQuestions();
		} else {
			quiz.sortQuestionsByIndex();
		}
		this.quiz = quiz;
	}

	public Quiz getQuiz() {
		return this.quiz;
	}

	public void updateI18NTexts() {
		for (WizardPage page : this.pages) {
			page.updateI18NTexts();
		}
	}

	public QuizType getQuizType() {
		QuizType quizType = PreferencesDialog.getDefaultQuizType();
		return quizType;
	}
}
