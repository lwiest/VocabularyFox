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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TipDialog extends Dialog {
	private Button checkboxDontShowTip;

	private Language targetLanguage;

	public TipDialog(Shell parentShell, Language targetLanguage) {
		super(parentShell);
		this.targetLanguage = targetLanguage;
		setShellStyle(SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL | SWT.CLOSE); // Adding SWT.CLOSE is a hack to make the shell icon appear
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(I18N.getString(I18N.TIP));

		if (this.targetLanguage == Language.EN) {
			shell.setImage(Resources.getImage(Resources.IMG_ENGLISH16x16));
		} else if (this.targetLanguage == Language.FR) {
			shell.setImage(Resources.getImage(Resources.IMG_FRENCH16x16));
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite compDialogArea = (Composite) super.createDialogArea(parent);
		compDialogArea.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		Composite composite = new Composite(compDialogArea, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		StyledLabel slbl0 = new StyledLabel(composite, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.TOP).grab(true, false).span(2, 1).applyTo(slbl0);
		slbl0.setStyledText(I18N.getString(I18N.USE_FOLLOWING_ABBREVIATIONS));

		createBlankLabel(composite);
		createBlankLabel(composite);

		String[][] abbreviationPairs = this.targetLanguage.getAbbreviationPairs();
		for (String[] abbreviationPair : abbreviationPairs) {
			StyledLabel slbl1 = new StyledLabel(composite, SWT.PUSH);
			GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.TOP).applyTo(slbl1);
			slbl1.setStyledText(abbreviationPair[0]);

			StyledLabel slbl2 = new StyledLabel(composite, SWT.PUSH);
			GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.TOP).applyTo(slbl2);
			for (int i = 1; i < (abbreviationPair.length - 1); i++) {
				if (i > 1) {
					slbl2.addStyledText(" " + I18N.getString(I18N.OR) + " ");
				}
				slbl2.addStyledText(abbreviationPair[i], Resources.TS_DEFAULT_BOLD);
			}
		}

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				getButton(IDialogConstants.OK_ID).setFocus();
			}
		});

		return compDialogArea;
	}

	private void createBlankLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).applyTo(label);
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
		GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).applyTo(composite);

		this.checkboxDontShowTip = new Button(composite, SWT.CHECK);
		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(this.checkboxDontShowTip);
		this.checkboxDontShowTip.setText(I18N.getString(I18N.DONT_SHOW_TIP));
		this.checkboxDontShowTip.setSelection(PreferencesDialog.getPreferenceDontShowTip(this.targetLanguage));
		this.checkboxDontShowTip.setFont(Resources.getFont(Resources.FONT_DEFAULT));

		createHorizontalSpacer(composite);

		Button btnOk = createButton(composite, IDialogConstants.OK_ID, I18N.getString(I18N.OK), true);
		Utils.setButtonDefaultFontAndWidth(btnOk);
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

	@Override
	public boolean close() {
		boolean isDontShowTip = this.checkboxDontShowTip.getSelection();
		PreferencesDialog.setPreferenceDontShowTip(this.targetLanguage, isDontShowTip);
		return super.close();
	}

	public boolean isDontShowTip() {
		boolean isDontShowTip = PreferencesDialog.getPreferenceDontShowTip(this.targetLanguage);
		return isDontShowTip;
	}
}
