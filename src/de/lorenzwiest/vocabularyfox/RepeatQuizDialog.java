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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class RepeatQuizDialog extends Dialog {
	private boolean isRepeatAllQuestions = true;

	public RepeatQuizDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(I18N.getString(I18N.QUESTION));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite compDialogArea = (Composite) super.createDialogArea(parent);
		compDialogArea.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		Composite composite = new Composite(compDialogArea, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);

		StyledLabel slbl = new StyledLabel(composite, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.TOP).grab(true, false).applyTo(slbl);
		slbl.setStyledText(I18N.getString(I18N.WHICH_WORDS_TO_ASK_AGAIN));

		createBlankLabel(composite);

		Button radioRepeatAllQuestions = new Button(composite, SWT.RADIO);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(radioRepeatAllQuestions);
		radioRepeatAllQuestions.setText(I18N.getString(I18N.ALL_WORDS));
		radioRepeatAllQuestions.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		radioRepeatAllQuestions.setSelection(true);
		radioRepeatAllQuestions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RepeatQuizDialog.this.isRepeatAllQuestions = true;
			}
		});

		Button radioRepeatQuestionsWithErrros = new Button(composite, SWT.RADIO);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(radioRepeatQuestionsWithErrros);
		radioRepeatQuestionsWithErrros.setText(I18N.getString(I18N.WRONG_WORDS_ONLY));
		radioRepeatQuestionsWithErrros.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		radioRepeatQuestionsWithErrros.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RepeatQuizDialog.this.isRepeatAllQuestions = false;
			}
		});

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				getButton(IDialogConstants.OK_ID).setFocus();
			}
		});

		return compDialogArea;
	}

	private void createBlankLabel(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		GridDataFactory.swtDefaults().applyTo(label);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout parentGridLayout = (GridLayout) parent.getLayout();
		parentGridLayout.makeColumnsEqualWidth = false;
		parentGridLayout.numColumns = 1;

		GridData gridData = (GridData) parent.getLayoutData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;

		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(composite);
		GridLayoutFactory.swtDefaults().margins(0, 0).applyTo(composite);

		createHorizontalSpacer(composite);

		Button btnOk = createButton(composite, IDialogConstants.OK_ID, I18N.getString(I18N.OK), true);
		Utils.setButtonDefaultFontAndWidth(btnOk);
		Button btnCancel = createButton(composite, IDialogConstants.CANCEL_ID, I18N.getString(I18N.CANCEL), false);
		Utils.setButtonDefaultFontAndWidth(btnCancel);
	}

	private void createHorizontalSpacer(Composite composite) {
		Label lblHorizontalSpacer = new Label(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(lblHorizontalSpacer);
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {
		Point ptParentLoc = getParentShell().getLocation();
		Point ptParentSize = getParentShell().getSize();
		Point ptDialogSize = getInitialSize();

		int newX = ptParentLoc.x + ((ptParentSize.x - ptDialogSize.x) / 2);
		int newY = ptParentLoc.y + ((ptParentSize.y - ptDialogSize.y) / 2);
		return new Point(newX, newY);
	}

	public boolean isRepeatAllQuestions() {
		return this.isRepeatAllQuestions;
	}
}
