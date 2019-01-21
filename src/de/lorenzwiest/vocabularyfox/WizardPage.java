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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class WizardPage {
	protected Wizard wizard;
	private WizardPage previousPage;
	private WizardPage nextPage;

	public WizardPage(Wizard wizard) {
		this.wizard = wizard;
	}

	abstract public Control createContent(Composite parent, int swtStyle);

	public void setPreviousPage(WizardPage page) {
		this.previousPage = page;
	}

	public WizardPage getPreviousPage() {
		return this.previousPage;
	}

	public void setNextPage(WizardPage page) {
		this.nextPage = page;
	}

	public WizardPage getNextPage() {
		return this.nextPage;
	}

	public void focus() {
		// empty implementation
	}

	public void unfocus() {
		// empty implementation
	}

	public void updateI18NTexts() {
		// empty implementation
	}
}
