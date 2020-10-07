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

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Page2 extends WizardPage {
	private static final int MF_LABEL_WIDTH = Utils.scaleToDisplay(32);
	private static final int MF_LABEL_HEIGHT = Utils.scaleToDisplay(26);
	private static final int LETTER_PICKER_OFFSET_Y = 2; // don't scale with display resolution

	private static final String STR_WRONG_TRY_AGAIN[] = { //
		I18N.getString(I18N.WRONG_TRY_AGAIN_0), //
		I18N.getString(I18N.WRONG_TRY_AGAIN_1), //
		I18N.getString(I18N.WRONG_TRY_AGAIN_2), //
		I18N.getString(I18N.WRONG_TRY_AGAIN_3), //
		I18N.getString(I18N.WRONG_TRY_AGAIN_4), //
		I18N.getString(I18N.WRONG_TRY_AGAIN_5) //
	};

	private static final String STR_WRONG[] = { //
		I18N.getString(I18N.WRONG_0), //
		I18N.getString(I18N.WRONG_1), //
		I18N.getString(I18N.WRONG_2) //
	};

	private StyledLabel slblNOutOfM;
	private StyledLabel slblQuestion;
	private StyledLabel slblRightOrWrong;
	private StyledLabel slblCorrectAnswer;

	private Text txtAnswer;
	private Label lblM;
	private Label lblF;
	private Label lblLetterPicker;

	private final boolean[] isMHover = new boolean[] { false };
	private final boolean[] isFHover = new boolean[] { false };
	private final boolean[] isLetterPickerHover = new boolean[] { false };

	private Button btnPrevious;
	private Button btnSkip;
	private Button btnNext;

	private int questionIndex = 0;
	private int triesCount = 0;

	public Page2(Wizard wizard) {
		super(wizard);
	}

	@Override
	public Control createContent(Composite parent, int swtBits) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().margins(0, 0).applyTo(composite);

		createDialogArea(composite);
		createButtons(composite);
		updateI18NTexts();
		return composite;
	}

	private void createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);
		GridLayoutFactory.swtDefaults().margins(Resources.INDENT * 2, Resources.INDENT * 3).applyTo(composite);
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		this.slblNOutOfM = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblNOutOfM);

		createBlankLabel(composite);

		this.slblQuestion = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblQuestion);

		createBlankLabel(composite);

		createInput(composite);

		createBlankLabel(composite);

		this.slblRightOrWrong = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblRightOrWrong);

		this.slblCorrectAnswer = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblCorrectAnswer);

		createBlankLabel(composite);
	}

	private void createBlankLabel(Composite parent) {
		new Label(parent, SWT.NONE);
	}

	private void createInput(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().margins(0, 0).numColumns(5).applyTo(composite);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(composite);
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		this.lblM = new Label(composite, SWT.CENTER);
		GridDataFactory.swtDefaults().hint(MF_LABEL_WIDTH, MF_LABEL_HEIGHT).align(SWT.FILL, SWT.CENTER).applyTo(this.lblM);
		this.lblM.setText("m.");
		this.lblM.setFont(Resources.getFont(Resources.FONT_SEMI_TITLE_BOLD_ITALICS));
		this.lblM.setForeground(Resources.getColor(Resources.COLOR_WHITE));
		this.lblM.setBackground(Resources.getColor(Resources.COLOR_M));
		this.lblM.setVisible(false);
		this.lblM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String strColor = Page2.this.isMHover[0] ? Resources.COLOR_M_HOVER : Resources.COLOR_M;
				Page2.this.lblM.setBackground(Resources.getColor(strColor));
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Page2.this.txtAnswer.setForeground(Resources.getColor(Resources.COLOR_M));
				Page2.this.wizard.getQuiz().getQuestions().get(Page2.this.questionIndex).setActualGender(Gender.M);
				Page2.this.btnNext.setEnabled(true);
				stopPulsing();
			}
		});
		this.lblM.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				Page2.this.isMHover[0] = true;
				Page2.this.lblM.setBackground(Resources.getColor(Resources.COLOR_M_HOVER));
				setHoverPulsingColorsM();
			}

			@Override
			public void mouseExit(MouseEvent e) {
				Page2.this.isMHover[0] = false;
				Page2.this.lblM.setBackground(Resources.getColor(Resources.COLOR_M));
				setNormalPulsingColors();
			}
		});

		this.lblF = new Label(composite, SWT.CENTER);
		this.lblF.setBackground(Resources.getColor(Resources.COLOR_F));
		GridDataFactory.swtDefaults().hint(MF_LABEL_WIDTH, MF_LABEL_HEIGHT).align(SWT.FILL, SWT.CENTER).applyTo(this.lblF);
		this.lblF.setText("f.");
		this.lblF.setFont(Resources.getFont(Resources.FONT_SEMI_TITLE_BOLD_ITALICS));
		this.lblF.setForeground(Resources.getColor(Resources.COLOR_WHITE));
		this.lblF.setVisible(false);
		this.lblF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String strColor = Page2.this.isFHover[0] ? Resources.COLOR_F_HOVER : Resources.COLOR_F;
				Page2.this.lblF.setBackground(Resources.getColor(strColor));
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Page2.this.txtAnswer.setForeground(Resources.getColor(Resources.COLOR_F));
				Page2.this.wizard.getQuiz().getQuestions().get(Page2.this.questionIndex).setActualGender(Gender.F);
				Page2.this.btnNext.setEnabled(true);
				stopPulsing();
			}
		});
		this.lblF.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				Page2.this.isFHover[0] = true;
				Page2.this.lblF.setBackground(Resources.getColor(Resources.COLOR_F_HOVER));
				setHoverPulsingColorsF();
			}

			@Override
			public void mouseExit(MouseEvent e) {
				Page2.this.isFHover[0] = false;
				Page2.this.lblF.setBackground(Resources.getColor(Resources.COLOR_F));
				setNormalPulsingColors();
			}
		});

		this.txtAnswer = new Text(composite, SWT.CENTER);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(this.txtAnswer);
		this.txtAnswer.setText("");
		this.txtAnswer.setFont(Resources.getFont(Resources.FONT_SEMI_TITLE_BOLD));
		this.txtAnswer.setTextLimit(100);

		Label lblHorizontalSpacer = new Label(composite, SWT.NONE);
		lblHorizontalSpacer.setVisible(false);

		this.lblLetterPicker = new Label(composite, SWT.CENTER);
		GridDataFactory.swtDefaults().hint(MF_LABEL_WIDTH * 2, MF_LABEL_HEIGHT).align(SWT.FILL, SWT.CENTER).applyTo(this.lblLetterPicker);
		this.lblLetterPicker.setText("авз\u2026");
		this.lblLetterPicker.setFont(Resources.getFont(Resources.FONT_SEMI_TITLE_BOLD_ITALICS));
		this.lblLetterPicker.setBackground(Resources.getColor(Resources.COLOR_LETTER_PICKER));
		this.lblLetterPicker.setVisible(false);
		this.lblLetterPicker.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String strColor = Page2.this.isLetterPickerHover[0] ? Resources.COLOR_LETTER_PICKER_HOVER : Resources.COLOR_LETTER_PICKER;
				Page2.this.lblLetterPicker.setBackground(Resources.getColor(strColor));
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Page2.this.lblLetterPicker.setBackground(Resources.getColor(Resources.COLOR_LETTER_PICKER_CLICK));
				letterPickerClicked();
			}
		});
		this.lblLetterPicker.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				Page2.this.isLetterPickerHover[0] = true;
				Page2.this.lblLetterPicker.setBackground(Resources.getColor(Resources.COLOR_LETTER_PICKER_HOVER));
			}

			@Override
			public void mouseExit(MouseEvent e) {
				Page2.this.isLetterPickerHover[0] = false;
				Page2.this.lblLetterPicker.setBackground(Resources.getColor(Resources.COLOR_LETTER_PICKER));
			}
		});

		int indent = (this.lblM.getSize().x + this.lblF.getSize().x) - this.lblLetterPicker.getSize().x;
		GridDataFactory.swtDefaults().hint(indent, SWT.DEFAULT).applyTo(lblHorizontalSpacer);
	}

	private boolean isLetterPickerDialogOpen = false;

	private void letterPickerClicked() {
		if (this.isLetterPickerDialogOpen) {
			return;
		}

		Shell shell = Display.getCurrent().getActiveShell();
		LetterPickerDialog dialog = new LetterPickerDialog(shell);
		dialog.create();

		Shell dialogShell = dialog.getShell();
		dialogShell.pack();

		Point dialogSize = dialogShell.getSize();
		Point lblLetterPickerLocation = toDisplay(this.lblLetterPicker);
		Point lblLetterPickerSize = this.lblLetterPicker.getSize();

		int dialogLocationX = (lblLetterPickerLocation.x + lblLetterPickerSize.x) - dialogSize.x;
		int dialogLocationY = lblLetterPickerLocation.y + lblLetterPickerSize.y + LETTER_PICKER_OFFSET_Y;
		dialogShell.setLocation(dialogLocationX, dialogLocationY);

		dialog.setBlockOnOpen(true);

		boolean isBtnNextEnabled = this.btnNext.isEnabled();
		boolean isBtnPreviousEnabled = this.btnPrevious.isEnabled();

		this.btnNext.setEnabled(false);
		this.btnPrevious.setEnabled(false);
		this.isLetterPickerDialogOpen = true;

		dialog.open();

		// FIXME: Click the window's close box to crash the program
		this.isLetterPickerDialogOpen = false;
		this.btnNext.setEnabled(isBtnNextEnabled);
		this.btnPrevious.setEnabled(isBtnPreviousEnabled);

		this.txtAnswer.setFocus();

		String selectedLetter = dialog.getSelectedLetter();
		if (selectedLetter != null) {
			String strAnswer = this.txtAnswer.getText();
			Point selection = this.txtAnswer.getSelection();
			String strNewAnswer = strAnswer.substring(0, selection.x) + selectedLetter + strAnswer.substring(selection.y);
			this.txtAnswer.setText(strNewAnswer);
			this.txtAnswer.setSelection(selection.x + selectedLetter.length());
		}
	}

	private static Point toDisplay(Control control) {
		final int DUMMY_X = 0;
		final int DUMMY_Y = 0;
		final int DUMMY_WIDTH = 10;
		final int DUMMY_HEIGHT = 10;

		int x = 0;
		int y = 0;
		Control parent = control;
		do {
			Point location = parent.getLocation();
			x += location.x;
			y += location.y;
			if (parent instanceof Shell) {
				Rectangle rect = ((Shell) parent).computeTrim(DUMMY_X, DUMMY_Y, DUMMY_WIDTH, DUMMY_HEIGHT);
				x += DUMMY_X - rect.x;
				y += DUMMY_Y - rect.y;
			}
			parent = parent.getParent();
		} while (parent != null);
		return new Point(x, y);
	}

	private void createButtons(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(composite);
		GridLayoutFactory.swtDefaults().applyTo(composite);

		Composite compButtons = new Composite(composite, SWT.NONE);
		compButtons.setLayout(new GridLayout(4, false));
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(compButtons);

		this.btnPrevious = new Button(compButtons, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.btnPrevious);
		Utils.setButtonDefaultFontAndWidth(this.btnPrevious);
		this.btnPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				previousButtonClicked();
			}
		});

		createHorizontalSpacer(compButtons);

		this.btnSkip = new Button(compButtons, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.btnSkip);
		Utils.setButtonDefaultFontAndWidth(this.btnSkip);
		this.btnSkip.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				skipButtonClicked();
			}
		});

		this.btnNext = new Button(compButtons, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.btnNext);
		Utils.setButtonDefaultFontAndWidth(this.btnNext);
		this.btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextButtonClicked();
			}
		});
	}

	private void createHorizontalSpacer(Composite composite) {
		Label lblHorizontalSpacer = new Label(composite, SWT.NONE);
		lblHorizontalSpacer.setImage(Resources.getImage(Resources.IMG_SPACER));
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(lblHorizontalSpacer);
	}

	private void previousButtonClicked() {
		boolean isBtnPreviousEnabled = this.btnPrevious.isEnabled();
		boolean isBtnNextEnabled = this.btnNext.isEnabled();
		this.btnPrevious.setEnabled(false);
		this.btnNext.setEnabled(false);

		Dialog dialog = new AbortDialog(Display.getCurrent().getActiveShell());
		dialog.setBlockOnOpen(true);
		int result = dialog.open();

		this.btnPrevious.setEnabled(isBtnPreviousEnabled);
		this.btnNext.setEnabled(isBtnNextEnabled);

		if (result == Window.OK) {
			this.wizard.nextPage();
		} else {
			this.txtAnswer.setFocus();
		}
	}

	private void skipButtonClicked() {
		List<Question> questions = this.wizard.getQuiz().getQuestions();
		Question questionToSkip = questions.remove(this.questionIndex);
		questions.add(questionToSkip);
		showNewQuestion();
	}

	private void nextButtonClicked() {
		Quiz quiz = this.wizard.getQuiz();
		Language targetLanguage = quiz.getTargetLanguage();
		List<Question> questions = quiz.getQuestions();
		Question question = questions.get(this.questionIndex);

		// cleanse answer
		String rawAnswer = this.txtAnswer.getText();
		String answer = rawAnswer.trim().replaceAll("\\s\\s+", " ");
		String normalizedAnswer = replaceAbbreviations(answer, targetLanguage);

		String expectedAnswerToCompare = replaceAbbreviations(question.getExpectedAnswer(), targetLanguage);

		boolean isCorrectAnswer = normalizedAnswer.equals(expectedAnswerToCompare);
		boolean isCorrectGender = true;
		Gender expectedGender = question.getExpectedGender();
		if (expectedGender != Gender.DONT_CARE) {
			Gender actualGender = question.getActualGender();
			isCorrectGender = expectedGender == actualGender;
		}

		if ((isCorrectAnswer == false) && isCorrectGender) {
			boolean existsQuestionDouble = false;
			String strQuestion = question.getQuestion();
			for (int i = 0; i < questions.size(); i++) {
				if (i == this.questionIndex) {
					continue;
				}
				Question questionToCompare = questions.get(i);
				String tmpExpectedAnswerToCompare = replaceAbbreviations(question.getExpectedAnswer(), targetLanguage); // TODO: Lots of abbreviation replacements. Cache them?
				if (strQuestion.equals(questionToCompare.getQuestion()) && normalizedAnswer.equals(tmpExpectedAnswerToCompare)) {
					existsQuestionDouble = true;
					break;
				}
			}
			if (existsQuestionDouble) {
				// FIXME: "Next" button is enabled, but nothing happens when you click it!
				this.slblRightOrWrong.setStyledText(I18N.getString(I18N.CORRECT_BUT_DIFFERENT_WORD));
				this.slblRightOrWrong.getParent().layout(true, true);
				this.txtAnswer.setFocus();
				return;
			}
		}

		boolean isCorrectAnswerAndGender = isCorrectAnswer && isCorrectGender;
		if (isCorrectAnswerAndGender) {
			if (this.triesCount == 0) {
				question.setActualAnswer(answer);
				question.setGrade(Grade.CORRECT);
			} else {
				question.setGrade(Grade.ALMOST_CORRECT);
			}
		} else {
			question.setGrade(Grade.WRONG);

			QuizType quizType = this.wizard.getQuizType();
			if (quizType == QuizType.EXAM) {
				question.setActualAnswer(answer);
			} else if (this.triesCount == 0) {
				question.setActualAnswer(answer);

				int indexPhrase = (int) (Math.random() * STR_WRONG_TRY_AGAIN.length);
				String strPhrase = STR_WRONG_TRY_AGAIN[indexPhrase];
				this.slblRightOrWrong.setStyledText(strPhrase);
				this.slblRightOrWrong.getParent().layout(true, true);

				this.triesCount++;
				this.txtAnswer.setFocus();
				return;
			} else if (this.triesCount == 1) {
				question.setActualAnswer(answer);

				TextStyle textStyle = Resources.TS_SEMI_TITLE_BOLD;
				if (expectedGender == Gender.M) {
					textStyle = Resources.TS_SEMI_TITLE_M_BOLD;
				} else if (expectedGender == Gender.F) {
					textStyle = Resources.TS_SEMI_TITLE_F_BOLD;
				}
				String expectedAnswer = question.getExpectedAnswer();
				this.slblCorrectAnswer.setStyledText(expectedAnswer, textStyle);

				int indexPhrase = (int) (Math.random() * STR_WRONG.length);
				String strPhrase = STR_WRONG[indexPhrase];
				this.slblRightOrWrong.setStyledText(strPhrase);
				this.slblRightOrWrong.getParent().layout(true, true);

				this.txtAnswer.addFocusListener(this.disableFocus);
				this.txtAnswer.setForeground(Resources.getColor(Resources.COLOR_RED));

				this.lblLetterPicker.setVisible(false);
				this.lblM.setVisible(false);
				this.lblF.setVisible(false);

				this.triesCount++;
				return;
			}
		}
		this.questionIndex++;
		if (this.questionIndex < questions.size()) {
			showNewQuestion();
		} else {
			this.wizard.nextPage();
		}
	}

	private String replaceAbbreviations(String text, Language targetLanguage) {
		String[][] abbreviationPairs = targetLanguage.getAbbreviationPairs();
		for (String[] abbreviationPair : abbreviationPairs) {
			String searchFor = abbreviationPair[abbreviationPair.length - 1];
			String replaceWith = abbreviationPair[1];
			text = text.replaceAll(searchFor, replaceWith);
		}
		return text;
	}

	private FocusListener disableFocus = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			Page2.this.btnNext.setFocus();
		}
	};

	private void showNewQuestion() {
		Quiz quiz = this.wizard.getQuiz();
		Question question = quiz.getQuestions().get(this.questionIndex);

		String strNOutOfM = I18N.getString(I18N.N_OF_M, "" + (this.questionIndex + 1), "" + quiz.getQuestions().size());
		this.slblNOutOfM.setStyledText(strNOutOfM);

		String strQuestion = question.getQuestion();
		this.slblQuestion.setStyledText(StyledString.parse(strQuestion, Resources.TS_SEMI_TITLE, Resources.TS_SEMI_TITLE_GREY));

		this.txtAnswer.setForeground(Resources.getColor(Resources.COLOR_BLACK));
		this.txtAnswer.setText("");
		this.txtAnswer.removeFocusListener(this.disableFocus);

		this.slblRightOrWrong.setStyledText("");
		this.slblCorrectAnswer.setStyledText("");

		Gender expectedGender = question.getExpectedGender();
		if (expectedGender == Gender.DONT_CARE) {
			this.lblM.setVisible(false);
			this.lblF.setVisible(false);
			this.btnNext.setEnabled(true);
		} else {
			this.lblM.setVisible(true);
			this.lblF.setVisible(true);
			this.btnNext.setEnabled(false);
			question.setActualGender(Gender.INITIAL);
			startPulsing();
		}

		boolean isLetterPickerVisible = (quiz.getTargetLanguage() == Language.FR);
		this.lblLetterPicker.setVisible(isLetterPickerVisible);

		this.txtAnswer.setFocus();
		this.txtAnswer.setSelection(this.txtAnswer.getText().length());

		updateSkipButton();

		this.slblNOutOfM.getParent().layout(true, true);

		this.triesCount = 0;
	}

	private void updateSkipButton() {
		boolean isSkipButtonVisible = false;
		boolean isSkipButtonEnabled = false;

		if (this.wizard.getQuizType() == QuizType.HOMEWORK) {
			isSkipButtonVisible = true;
		}
		if (this.questionIndex < (this.wizard.getQuiz().getQuestions().size() - 1)) {
			isSkipButtonEnabled = true;
		}
		this.btnSkip.setVisible(isSkipButtonVisible);
		this.btnSkip.setEnabled(isSkipButtonEnabled);
	}

	@Override
	public void updateI18NTexts() {
		this.btnPrevious.setText(I18N.getString(I18N.END_QUIZ));
		this.btnSkip.setText(I18N.getString(I18N.SKIP));
		this.btnNext.setText(I18N.getString(I18N.NEXT));

		STR_WRONG_TRY_AGAIN[0] = I18N.getString(I18N.WRONG_TRY_AGAIN_0);
		STR_WRONG_TRY_AGAIN[1] = I18N.getString(I18N.WRONG_TRY_AGAIN_1);
		STR_WRONG_TRY_AGAIN[2] = I18N.getString(I18N.WRONG_TRY_AGAIN_2);
		STR_WRONG_TRY_AGAIN[3] = I18N.getString(I18N.WRONG_TRY_AGAIN_3);
		STR_WRONG_TRY_AGAIN[4] = I18N.getString(I18N.WRONG_TRY_AGAIN_4);
		STR_WRONG_TRY_AGAIN[5] = I18N.getString(I18N.WRONG_TRY_AGAIN_5);

		STR_WRONG[0] = I18N.getString(I18N.WRONG_0);
		STR_WRONG[1] = I18N.getString(I18N.WRONG_1);
		STR_WRONG[2] = I18N.getString(I18N.WRONG_2);

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Utils.setButtonDefaultFontAndWidth(Page2.this.btnPrevious);
				Utils.setButtonDefaultFontAndWidth(Page2.this.btnSkip);
				Utils.setButtonDefaultFontAndWidth(Page2.this.btnNext);
				Shell shell = Display.getCurrent().getActiveShell();
				shell.layout(true, true);
			}
		});
	}

	@Override
	public void focus() {
		this.questionIndex = 0;

		Shell shell = Display.getCurrent().getActiveShell();
		shell.setDefaultButton(this.btnNext);

		Quiz quiz = this.wizard.getQuiz();
		Language targetLanguage = quiz.getTargetLanguage();
		if (targetLanguage == Language.EN) {
			shell.setImage(Resources.getImage(Resources.IMG_ENGLISH16x16));
		} else if (targetLanguage == Language.FR) {
			shell.setImage(Resources.getImage(Resources.IMG_FRENCH16x16));
		}
		shell.setText(quiz.getUnit());

		showNewQuestion();
		showTipDialog();
	}

	private void showTipDialog() {
		boolean isBtnNextEnabled = this.btnNext.isEnabled();
		boolean isBtnSkipEnabled = this.btnSkip.isEnabled();
		boolean isBtnPreviousEnabled = this.btnPrevious.isEnabled();

		this.btnNext.setEnabled(false);
		this.btnSkip.setEnabled(false);
		this.btnPrevious.setEnabled(false);

		Language targetLanguage = this.wizard.getQuiz().getTargetLanguage();
		TipDialog tipDialog = new TipDialog(Display.getCurrent().getActiveShell(), targetLanguage);

		if (tipDialog.isDontShowTip() == false) {
			tipDialog.setBlockOnOpen(true);
			tipDialog.open();
		}

		this.btnNext.setEnabled(isBtnNextEnabled);
		this.btnSkip.setEnabled(isBtnSkipEnabled);
		this.btnPrevious.setEnabled(isBtnPreviousEnabled);
	}

	@Override
	public void unfocus() {
		if (this.wizard.getQuiz().getTargetLanguage() == Language.FR) {
			this.lblM.setVisible(false);
			this.lblF.setVisible(false);
			this.lblLetterPicker.setVisible(false);

			stopPulsing();
		}
	}

	/// Pulsing colors ///////////////////////////////////////////////////////////

	private Color colPulsingMLow;
	private Color colPulsingMHigh;
	private Color colPulsingFLow;
	private Color colPulsingFHigh;

	private boolean isPulsing = false;

	private void startPulsing() {
		setNormalPulsingColors();

		Runnable runnable = new Runnable() {
			private static final int RGB_STEPS = 8;

			private int count = 0;
			private int step = 0;

			@Override
			public void run() {
				while (Page2.this.isPulsing) {
					RGB rgbMLow = Page2.this.colPulsingMLow.getRGB();
					RGB rgbMHigh = Page2.this.colPulsingMHigh.getRGB();
					final RGB newRGBM = interpolate(rgbMLow, rgbMHigh, RGB_STEPS, this.count);

					RGB rgbFLow = Page2.this.colPulsingFLow.getRGB();
					RGB rgbFHigh = Page2.this.colPulsingFHigh.getRGB();
					// label "f." pulses counter-rhythmically to label "m."
					final RGB newRGBF = interpolate(rgbFLow, rgbFHigh, RGB_STEPS, Math.abs(this.count - RGB_STEPS));

					this.step++;
					if (this.step > (2 * RGB_STEPS)) {
						this.count = 0;
						this.step = 0;
					} else if (this.step > RGB_STEPS) {
						this.count--;
					} else {
						this.count++;
					}

					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							Page2.this.lblM.setBackground(getColor("RGBM", newRGBM));
							Page2.this.lblF.setBackground(getColor("RGBF", newRGBF));
						}

						private Color getColor(String id, RGB rgb) {
							Color color = null;
							String colorId = id + Integer.toHexString(rgb.hashCode());
							if (Resources.containsColor(colorId) == false) {
								color = new Color(Display.getCurrent(), rgb);
								Resources.putColor(colorId, color);
							} else {
								color = Resources.getColor(colorId);
							}
							return color;
						}
					});
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				}
			}
		};

		this.isPulsing = true;
		new Thread(runnable).start();
	}

	private void stopPulsing() {
		this.isPulsing = false;
		setNonPulsingColors();
	}

	private static RGB interpolate(RGB rgbLow, RGB rgbHigh, int steps, int counter) {
		int newR = rgbLow.red + (((rgbHigh.red - rgbLow.red) / steps) * counter);
		int newG = rgbLow.green + (((rgbHigh.green - rgbLow.green) / steps) * counter);
		int newB = rgbLow.blue + (((rgbHigh.blue - rgbLow.blue) / steps) * counter);
		return new RGB(newR, newG, newB);
	}

	private void setNormalPulsingColors() {
		this.colPulsingMLow = Resources.getColor(Resources.COLOR_M_DARK);
		this.colPulsingMHigh = Resources.getColor(Resources.COLOR_M_HOVER);
		this.colPulsingFLow = Resources.getColor(Resources.COLOR_F_DARK);
		this.colPulsingFHigh = Resources.getColor(Resources.COLOR_F_HOVER);
	}

	private void setHoverPulsingColorsM() {
		this.colPulsingMLow = Resources.getColor(Resources.COLOR_M_HOVER);
		this.colPulsingMHigh = Resources.getColor(Resources.COLOR_M_HOVER);
		this.colPulsingFLow = Resources.getColor(Resources.COLOR_F_DARK);
		this.colPulsingFHigh = Resources.getColor(Resources.COLOR_F_HOVER);
	}

	private void setHoverPulsingColorsF() {
		this.colPulsingMLow = Resources.getColor(Resources.COLOR_M_DARK);
		this.colPulsingMHigh = Resources.getColor(Resources.COLOR_M_HOVER);
		this.colPulsingFLow = Resources.getColor(Resources.COLOR_F_HOVER);
		this.colPulsingFHigh = Resources.getColor(Resources.COLOR_F_HOVER);
	}

	private void setNonPulsingColors() {
		this.lblM.setBackground(Resources.getColor(Resources.COLOR_M));
		this.lblF.setBackground(Resources.getColor(Resources.COLOR_F));
	}
}
// 802