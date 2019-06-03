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

// LW 15-OCT-2018 Created

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.PageBook;

public class VocabularyFox extends ApplicationWindow {
	public static final String VERSION = "2019-06-03 18:40:00";

	private static final Point INITAL_SIZE = new Point(500, 400);

	private PageBook pageBook;

	private WizardPage page1;
	private WizardPage page2;
	private WizardPage page3;

	@Override
	protected Point getInitialSize() {
		return INITAL_SIZE;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setMinimumSize(INITAL_SIZE);
	}

	@Override
	protected Control createContents(Composite parent) {
		this.pageBook = new PageBook(parent, SWT.NONE);

		Wizard wizard = new Wizard(this.pageBook);

		this.page1 = new Page1(wizard);
		this.page2 = new Page2(wizard);
		this.page3 = new Page3(wizard);

		this.page1.setNextPage(this.page2);
		this.page1.setPreviousPage(null);
		this.page2.setNextPage(this.page3);
		this.page2.setPreviousPage(this.page1);
		this.page3.setNextPage(this.page1);
		this.page3.setPreviousPage(this.page2);

		wizard.addPage(this.page1);
		wizard.addPage(this.page2);
		wizard.addPage(this.page3);
		wizard.open();

		return this.pageBook;
	}

	public VocabularyFox(Shell parentShell) {
		super(parentShell);
	}

	public static void main(String[] args) {
		VocabularyFox application = new VocabularyFox(null);
		application.setBlockOnOpen(true);
		application.open();
	}
}