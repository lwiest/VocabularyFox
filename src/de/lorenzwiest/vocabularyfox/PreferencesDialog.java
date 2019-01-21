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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PreferencesDialog extends Dialog {
	private StyledLabel slblAppTitle;
	private StyledLabel slblVersion;

	private StyledLabel slblLanguage;
	private Combo dropdownLanguage;

	private StyledLabel slblQuizType;
	private Button radioHomework;
	private StyledLabel slblHomework1;
	private StyledLabel slblHomework2;
	private StyledLabel slblHomework3;
	private Button radioTest;
	private StyledLabel slblTest1;
	private StyledLabel slblTest2;
	private Button radioExam;
	private StyledLabel slblExam1;
	private StyledLabel slblExam2;

	private Button checkboxIsShuffleQuestions;

	private StyledLabel slblTips;
	private Button btnShowAllTips;

	private Wizard wizard;

	public PreferencesDialog(Shell parentShell, Wizard wizard) {
		super(parentShell);
		this.wizard = wizard;
		setShellStyle(SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL);
		loadPreferences();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(I18N.getString(I18N.PREFERENCES));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite compDialogArea = (Composite) super.createDialogArea(parent);
		compDialogArea.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		Composite composite = new Composite(compDialogArea, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(3).spacing(Resources.INDENT * 2, 5).applyTo(composite);

		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);

		Label lblIcon = new Label(composite, SWT.CENTER);
		GridDataFactory.swtDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(lblIcon);
		lblIcon.setImage(Resources.getImage(Resources.IMG_FOX48x48));

		this.slblAppTitle = new StyledLabel(composite, SWT.CENTER);
		GridDataFactory.swtDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblAppTitle);

		this.slblVersion = new StyledLabel(composite, SWT.CENTER);
		GridDataFactory.swtDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblVersion);

		createBlankLabel(composite, 3);
		this.slblLanguage = new StyledLabel(composite, SWT.RIGHT);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.slblLanguage);

		this.dropdownLanguage = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.LEFT, SWT.CENTER).applyTo(this.dropdownLanguage);
		this.dropdownLanguage.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		this.dropdownLanguage.add(I18N.getString(I18N.GERMAN), 0);
		this.dropdownLanguage.add(I18N.getString(I18N.ENGLISH), 1);
		this.dropdownLanguage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				languageSelected();
			}
		});

		int index = -1;
		Locale locale = getPreferenceLocale();
		if (locale == I18N.LOCALE_DE) {
			index = 0;
		} else if (locale == I18N.LOCALE_US) {
			index = 1;
		}
		this.dropdownLanguage.select(index);

		createBlankLabelHalfHeight(composite, 3);
		this.slblQuizType = new StyledLabel(composite, SWT.RIGHT);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.slblQuizType);

		SelectionAdapter radioSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				quizTypeSelected();
			}
		};

		this.radioHomework = new Button(composite, SWT.RADIO);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.LEFT, SWT.CENTER).applyTo(this.radioHomework);
		this.radioHomework.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		this.radioHomework.addSelectionListener(radioSelectionAdapter);

		createBlankLabel(composite, 2);
		this.slblHomework1 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.slblHomework1);

		createBlankLabel(composite, 2);
		this.slblHomework2 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.slblHomework2);

		createBlankLabel(composite, 2);
		this.slblHomework3 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).grab(true, false).applyTo(this.slblHomework3);

		createBlankLabel(composite, 1);
		this.radioTest = new Button(composite, SWT.RADIO);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.LEFT, SWT.CENTER).applyTo(this.radioTest);
		this.radioTest.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		this.radioTest.addSelectionListener(radioSelectionAdapter);

		createBlankLabel(composite, 2);
		this.slblTest1 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.slblTest1);

		createBlankLabel(composite, 2);
		this.slblTest2 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.slblTest2);

		createBlankLabel(composite, 1);
		this.radioExam = new Button(composite, SWT.RADIO);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.LEFT, SWT.CENTER).applyTo(this.radioExam);
		this.radioExam.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		this.radioExam.addSelectionListener(radioSelectionAdapter);

		QuizType quizType = getPreferenceQuizType();
		if (quizType == QuizType.HOMEWORK) {
			this.radioHomework.setSelection(true);
		} else if (quizType == QuizType.TEST) {
			this.radioTest.setSelection(true);
		} else if (quizType == QuizType.EXAM) {
			this.radioExam.setSelection(true);
		}

		createBlankLabel(composite, 2);
		this.slblExam1 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.slblExam1);

		createBlankLabel(composite, 2);
		this.slblExam2 = new StyledLabel(composite, SWT.LEFT);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.slblExam2);

		createBlankLabelHalfHeight(composite, 3);

		createBlankLabel(composite, 1);
		this.checkboxIsShuffleQuestions = new Button(composite, SWT.CHECK);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.LEFT, SWT.CENTER).applyTo(this.checkboxIsShuffleQuestions);
		this.checkboxIsShuffleQuestions.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		this.checkboxIsShuffleQuestions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isShuffleQuestionsSelected();
			}
		});

		boolean isShuffleQuestions = getPreferenceIsShuffleQuestions();
		this.checkboxIsShuffleQuestions.setSelection(isShuffleQuestions);

		createBlankLabelHalfHeight(composite, 3);
		this.slblTips = new StyledLabel(composite, SWT.RIGHT);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.slblTips);

		this.btnShowAllTips = new Button(composite, SWT.PUSH);
		boolean isButtonShowAllTipsEnabled = isButtonShowAllTipsEnabled();
		this.btnShowAllTips.setEnabled(isButtonShowAllTipsEnabled);

		GridDataFactory.swtDefaults().span(2, 1).align(SWT.LEFT, SWT.CENTER).applyTo(this.btnShowAllTips);
		this.btnShowAllTips.setFont(Resources.getFont(Resources.FONT_DEFAULT));
		this.btnShowAllTips.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonShowAllTipsSelected();
				PreferencesDialog.this.btnShowAllTips.setEnabled(false);
			}
		});

		updateI18NTexts();

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				getButton(IDialogConstants.OK_ID).setFocus();
			}
		});

		return compDialogArea;
	}

	private void createBlankLabel(Composite composite, int hSpan) {
		Label label = new Label(composite, SWT.NONE);
		GridDataFactory.swtDefaults().span(hSpan, 1).applyTo(label);
	}

	private void createBlankLabelHalfHeight(Composite composite, int hSpan) {
		Label label = new Label(composite, SWT.NONE);
		GridDataFactory.swtDefaults().span(3, 1).hint(SWT.DEFAULT, Resources.INDENT / 2).applyTo(label);
	}

	private void languageSelected() {
		Locale locale = null;
		int index = this.dropdownLanguage.getSelectionIndex();
		if (index == -1) {
			return;
		} else if (index == 0) {
			locale = I18N.LOCALE_DE;
		} else if (index == 1) {
			locale = I18N.LOCALE_US;
		}
		setPreferenceLocale(locale);
		I18N.setLocale(locale);
		updateI18NTexts();
		this.wizard.updateI18NTexts();
	}

	private void quizTypeSelected() {
		QuizType quizType = null;
		if (this.radioHomework.getSelection()) {
			quizType = QuizType.HOMEWORK;
		} else if (this.radioTest.getSelection()) {
			quizType = QuizType.TEST;
		} else if (this.radioExam.getSelection()) {
			quizType = QuizType.EXAM;
		}
		setPreferenceQuizType(quizType);
	}

	private void isShuffleQuestionsSelected() {
		boolean isSelected = this.checkboxIsShuffleQuestions.getSelection();
		setPreferenceIsShuffleQuestions(isSelected);
	}

	private boolean isButtonShowAllTipsEnabled() {
		for (Language language : Language.values()) {
			boolean isDontShowTip = getPreferenceDontShowTip(this.prefStore, language);
			if (isDontShowTip) {
				return true;
			}
		}
		return false;
	}

	private void buttonShowAllTipsSelected() {
		for (Language language : Language.values()) {
			boolean isDontShowTip = getPreferenceDontShowTip(this.prefStore, language);
			if (isDontShowTip) {
				setPreferenceDontShowTip(this.prefStore, language, false);
			}
		}
	}

	private void updateI18NTexts() {
		getShell().getParent().getShell().setText(I18N.getString(I18N.APPLICATION_TITLE));

		this.slblAppTitle.setStyledText(I18N.getString(I18N.APPLICATION_TITLE));
		this.slblVersion.setStyledText(I18N.getString(I18N.VERSION, VocabularyFox.VERSION));
		this.slblLanguage.setStyledText(I18N.getString(I18N.LANGUAGE), Resources.TS_DEFAULT_BOLD);
		this.slblQuizType.setStyledText(I18N.getString(I18N.QUIZ_TYPE), Resources.TS_DEFAULT_BOLD);
		this.radioHomework.setText(I18N.getString(I18N.HOME_WORK));

		this.slblHomework1.setStyledText(I18N.getString(I18N.HOME_WORK_1));
		this.slblHomework2.setStyledText(I18N.getString(I18N.HOME_WORK_2));
		this.slblHomework3.setStyledText(I18N.getString(I18N.HOME_WORK_3));
		this.radioTest.setText(I18N.getString(I18N.TEST));

		this.slblTest1.setStyledText(I18N.getString(I18N.TEST_1));
		this.slblTest2.setStyledText(I18N.getString(I18N.TEST_2));
		this.radioExam.setText(I18N.getString(I18N.EXAM));

		this.slblExam1.setStyledText(I18N.getString(I18N.EXAM_1));
		this.slblExam2.setStyledText(I18N.getString(I18N.EXAM_2));
		this.checkboxIsShuffleQuestions.setText(I18N.getString(I18N.SHUFFLE_WORDS));

		this.slblTips.setStyledText(I18N.getString(I18N.TIPS), Resources.TS_DEFAULT_BOLD);
		this.btnShowAllTips.setText(I18N.getString(I18N.SHOW_ALL_TIPS));

		getShell().setText(I18N.getString(I18N.PREFERENCES));
		Button btnOk = getButton(IDialogConstants.OK_ID); // TODO: Replace null guard by call at another point in time?
		if (btnOk != null) {
			btnOk.setText(I18N.getString(I18N.OK));
		}
		Button btnCancel = getButton(IDialogConstants.CANCEL_ID); // TODO: Replace null guard by call at another point in time?
		if (btnCancel != null) {
			btnCancel.setText(I18N.getString(I18N.CANCEL));
		}

		resizeDialog();
	}

	private void resizeDialog() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Shell shell = getShell();

				Point location = shell.getLocation();
				int width = shell.getSize().x;

				shell.setRedraw(false);
				shell.layout(true, true);
				Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				shell.setSize(newSize);
				shell.setLocation(location.x + ((width - newSize.x) / 2), location.y);
				shell.setRedraw(true);
			}
		});
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

	// Preferences ///////////////////////////////////////////////////////////////

	private static final String ID = "PreferencesDialog";

	private PreferenceStore prefStore;

	@Override
	protected void okPressed() {
		savePreferences();
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		I18N.setLocale(PreferencesDialog.getDefaultPreferenceLocale());
		this.wizard.updateI18NTexts();
		super.cancelPressed();
	}

	private void loadPreferences() {
		this.prefStore = loadPrefStore();
	}

	private void savePreferences() {
		try {
			this.prefStore.save();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	// Preference Locale

	private static final String PREFERENCE_ID_LOCALE = ".locale";

	private void setPreferenceLocale(Locale locale) {
		String strLocale = locale.toString();
		final String id = ID + PREFERENCE_ID_LOCALE;
		this.prefStore.setValue(id, strLocale);
	}

	private Locale getPreferenceLocale() {
		return getPreferenceLocale(this.prefStore);
	}

	private static Locale getPreferenceLocale(PreferenceStore prefStore) {
		Locale locale = I18N.getDefaultLocale();

		final String id = ID + PREFERENCE_ID_LOCALE;
		String value = prefStore.contains(id) ? prefStore.getString(id) : "";
		if (value.equals(I18N.LOCALE_DE.toString())) {
			locale = I18N.LOCALE_DE;
		} else if (value.equals(I18N.LOCALE_US.toString())) {
			locale = I18N.LOCALE_US;
		}
		return locale;
	}

	private static PreferenceStore loadPrefStore() {
		PreferenceStore prefStore = new PreferenceStore(Resources.PREFS_FILENAME);
		try {
			prefStore.load();
		} catch (FileNotFoundException e) {
			// pref store file does not exist
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return prefStore;
	}

	public static Locale getDefaultPreferenceLocale() {
		PreferenceStore prefStore = loadPrefStore();
		Locale locale = getPreferenceLocale(prefStore);
		return locale;
	}

	// Preference Quiz Type

	private static final String PREFERENCE_ID_QUIZ_TYPE = ".quizType";
	private static final QuizType DEFAULT_PREFERENCE_QUIZ_TYPE = QuizType.TEST;

	private static final String QUIZ_TYPE_HOMEWORK = "0";
	private static final String QUIZ_TYPE_TEST = "1";
	private static final String QUIZ_TYPE_EXAM = "2";

	private void setPreferenceQuizType(QuizType quizType) {
		String strQuizType = "";
		if (quizType == QuizType.HOMEWORK) {
			strQuizType = QUIZ_TYPE_HOMEWORK;
		} else if (quizType == QuizType.TEST) {
			strQuizType = QUIZ_TYPE_TEST;
		} else if (quizType == QuizType.EXAM) {
			strQuizType = QUIZ_TYPE_EXAM;
		}
		final String id = ID + PREFERENCE_ID_QUIZ_TYPE;
		this.prefStore.setValue(id, strQuizType);
	}

	private QuizType getPreferenceQuizType() {
		return getPreferenceQuizType(this.prefStore);
	}

	private static QuizType getPreferenceQuizType(PreferenceStore prefStore) {
		QuizType quizType = DEFAULT_PREFERENCE_QUIZ_TYPE;

		final String id = ID + PREFERENCE_ID_QUIZ_TYPE;
		String value = prefStore.contains(id) ? prefStore.getString(id) : "";
		if (value.equals(QUIZ_TYPE_HOMEWORK)) {
			quizType = QuizType.HOMEWORK;
		} else if (value.equals(QUIZ_TYPE_TEST)) {
			quizType = QuizType.TEST;
		} else if (value.equals(QUIZ_TYPE_EXAM)) {
			quizType = QuizType.EXAM;
		}
		return quizType;
	}

	public static QuizType getDefaultQuizType() {
		PreferenceStore prefStore = loadPrefStore();
		QuizType quizType = getPreferenceQuizType(prefStore);
		return quizType;
	}

	// Preference Shuffle Questions

	private static final String PREFERENCE_ID_IS_SHUFFLE_QUESTIONS = ".isShuffleQuestions";

	private void setPreferenceIsShuffleQuestions(boolean isShuffleQuestions) {
		final String id = ID + PREFERENCE_ID_IS_SHUFFLE_QUESTIONS;
		this.prefStore.setValue(id, isShuffleQuestions ? "true" : "false");
	}

	private boolean getPreferenceIsShuffleQuestions() {
		return getPreferenceIsShuffleQuestions(this.prefStore);
	}

	private static boolean getPreferenceIsShuffleQuestions(PreferenceStore prefStore) {
		final String id = ID + PREFERENCE_ID_IS_SHUFFLE_QUESTIONS;
		String value = prefStore.contains(id) ? prefStore.getString(id) : "";
		boolean result = value.equals("true");
		return result;
	}

	public static boolean getDefaultShuffleQuestions() {
		PreferenceStore prefStore = loadPrefStore();
		boolean result = getPreferenceIsShuffleQuestions(prefStore);
		return result;
	}

	// Preference Locale

	private static final String PREFERENCE_ID_DONT_SHOW_TIP = ".dontShowTip";

	private static void setPreferenceDontShowTip(PreferenceStore prefStore, Language language, boolean isDontShowTip) {
		final String id = ID + PREFERENCE_ID_DONT_SHOW_TIP + "_" + language.toString();
		prefStore.setValue(id, isDontShowTip ? "true" : "false");
	}

	public static void setPreferenceDontShowTip(Language language, boolean isDontShowTip) {
		PreferenceStore prefStore = loadPrefStore();
		setPreferenceDontShowTip(prefStore, language, isDontShowTip);
		try {
			prefStore.save();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	private static boolean getPreferenceDontShowTip(PreferenceStore prefStore, Language language) {
		final String id = ID + PREFERENCE_ID_DONT_SHOW_TIP + "_" + language.toString();
		String value = prefStore.contains(id) ? prefStore.getString(id) : "";
		boolean result = value.equals("true");
		return result;
	}

	public static boolean getPreferenceDontShowTip(Language language) {
		PreferenceStore prefStore = loadPrefStore();
		boolean result = getPreferenceDontShowTip(prefStore, language);
		return result;
	}
}
