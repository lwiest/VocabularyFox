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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class Page3 extends WizardPage {
	private ScrolledComposite scrolledComposite;

	private StyledLabel slblEvaluation;

	private StyledLabel slblQuizType;
	private StyledLabel slblUnit;
	private StyledLabel slblUnitTitle;
	private StyledLabel slblTimestamp;

	private Composite compProgress;

	private StyledLabel slblPercent;
	private StyledLabel slblScore;

	private Composite compGrid;

	private ImageButton ibtnDownloadPage;
	private Button btnRepeatQuiz;
	private Button btnNewQuiz;

	private List<Widget> widgetsToDispose = new ArrayList<Widget>();

	private Date now;

	public Page3(Wizard wizard) {
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
		this.scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(this.scrolledComposite);
		this.scrolledComposite.setExpandVertical(true);
		this.scrolledComposite.setExpandHorizontal(true);

		final Composite compCanvas = createScrollableContent(this.scrolledComposite);
		this.scrolledComposite.setContent(compCanvas);
		this.scrolledComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle rect = Page3.this.scrolledComposite.getClientArea();
				Page3.this.scrolledComposite.setMinSize(compCanvas.computeSize(rect.width, SWT.DEFAULT));
			}
		});
	}

	private Composite createScrollableContent(ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);
		GridLayoutFactory.swtDefaults().margins(Resources.INDENT * 5, Resources.INDENT * 3).applyTo(composite);
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		this.slblEvaluation = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblEvaluation);

		createBlankLabel(composite);

		this.slblQuizType = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblQuizType);

		this.slblUnit = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblUnit);

		this.slblUnitTitle = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblUnitTitle);

		this.slblTimestamp = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblTimestamp);

		createBlankLabel(composite);

		this.slblPercent = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblPercent);

		this.slblScore = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(this.slblScore);

		this.compProgress = createProgressVisualization(composite);

		createBlankLabel(composite);

		this.compGrid = new Composite(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(this.compGrid);
		GridLayoutFactory.swtDefaults().numColumns(4).spacing(Resources.INDENT, Resources.INDENT).applyTo(this.compGrid);
		this.compGrid.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		return composite;
	}

	private void createBlankLabel(Composite parent) {
		new Label(parent, SWT.NONE);
	}

	private Composite createProgressVisualization(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(composite);
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(composite);
		return composite;
	}

	private void createButtons(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(composite);
		GridLayoutFactory.swtDefaults().applyTo(composite);

		Composite compButtons = new Composite(composite, SWT.NONE);
		compButtons.setLayout(new GridLayout(4, false));
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(compButtons);

		this.ibtnDownloadPage = new ImageButton(compButtons, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.ibtnDownloadPage);
		this.ibtnDownloadPage.setImage(Resources.getImage(Resources.IMG_DOWNLOAD_PAGE));
		this.ibtnDownloadPage.setClickImage(Resources.getImage(Resources.IMG_DOWNLOAD_PAGE_CLICK));
		this.ibtnDownloadPage.setHoverImage(Resources.getImage(Resources.IMG_DOWNLOAD_PAGE_HOVER));
		this.ibtnDownloadPage.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				downloadPageButtonSelected();
			}
		});

		createHorizontalSpacer(compButtons);

		this.btnRepeatQuiz = new Button(compButtons, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.btnRepeatQuiz);
		Utils.setButtonDefaultFontAndWidth(this.btnRepeatQuiz);
		this.btnRepeatQuiz.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				repeatQuizButtonClicked();
			}
		});

		this.btnNewQuiz = new Button(compButtons, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.btnNewQuiz);
		Utils.setButtonDefaultFontAndWidth(this.btnNewQuiz);
		this.btnNewQuiz.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				newQuizButtonClicked();
			}
		});
	}

	private void createHorizontalSpacer(Composite composite) {
		Label lblHorizontalSpacer = new Label(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(lblHorizontalSpacer);
	}

	private void downloadPageButtonSelected() {
		Quiz quiz = this.wizard.getQuiz();

		String filenamePrefix = I18N.getString(I18N.EVALUATION_FILENAME_PREFIX);
		String filename = String.format("%s_%s_%3$tY-%3$tm-%3$td_%3$tH-%3$tM-%3$tS.html", filenamePrefix, quiz.getKey(), this.now);

		loadPreferences();
		String strFolderpath = getPreferenceFolderpath();
		FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
		fileDialog.setFilterPath(strFolderpath);
		fileDialog.setText(I18N.getString(I18N.SAVE_QUIZ_RESULTS));
		fileDialog.setFileName(filename);
		String strNewFullFilepath = fileDialog.open();
		if (strNewFullFilepath != null) {
			saveQuizResult(strNewFullFilepath, quiz);

			File newFullFilepath = new File(strNewFullFilepath);
			String strNewFolderpath = newFullFilepath.getParentFile().getAbsolutePath();
			setPreferenceFolderpath(strNewFolderpath);
			savePreferences();
		}
	}

	private void repeatQuizButtonClicked() {
		Quiz quiz = this.wizard.getQuiz();
		Quiz newQuiz = quiz.createInitializedCopy();

		int correctCount = getCorrectCount(quiz);
		int totalCount = getTotalCount(quiz);
		if ((correctCount > 0) && (correctCount < totalCount)) {
			boolean isBtnNewQuizEnabled = this.btnNewQuiz.isEnabled();
			boolean isBtnRepeatQuizEnabled = this.btnRepeatQuiz.isEnabled();
			this.btnNewQuiz.setEnabled(false);
			this.btnRepeatQuiz.setEnabled(false);

			RepeatQuizDialog dialog = new RepeatQuizDialog(Display.getCurrent().getActiveShell());
			dialog.setBlockOnOpen(true);
			int result = dialog.open();

			this.btnNewQuiz.setEnabled(isBtnNewQuizEnabled);
			this.btnRepeatQuiz.setEnabled(isBtnRepeatQuizEnabled);

			if (result == Window.CANCEL) {
				return;
			}

			boolean isRepeatAllQuestions = dialog.isRepeatAllQuestions();
			if (isRepeatAllQuestions == false) {
				List<Question> questions = quiz.getQuestions();
				for (int i = questions.size() - 1; i > -1; i--) {
					Question question = questions.get(i);
					if (question.getGrade() == Grade.CORRECT) {
						newQuiz.getQuestions().remove(i);
					}
				}

				String unit = newQuiz.getUnit();
				String strSelection = I18N.getString(I18N.SELECTION);

				if (unit.endsWith(strSelection) == false) {
					unit += " " + strSelection;
					newQuiz.setUnit(unit);
				}
			}
		}
		this.wizard.setQuiz(newQuiz);
		previousButtonClicked();
	}

	private void previousButtonClicked() {
		this.wizard.previousPage();
	}

	private void newQuizButtonClicked() {
		this.wizard.nextPage();
	}

	@Override
	public void updateI18NTexts() {
		this.slblEvaluation.setStyledText(I18N.getString(I18N.EVALUATION), Resources.TS_SEMI_TITLE);
		this.slblQuizType.setStyledText(quizTypeToString(this.wizard.getQuizType()));
		this.ibtnDownloadPage.setToolTipText(I18N.getString(I18N.SAVE_QUIZ_RESULTS));
		this.btnRepeatQuiz.setText(I18N.getString(I18N.REPEAT_QUIZ));
		this.btnNewQuiz.setText(I18N.getString(I18N.NEW_QUIZ));

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Utils.setButtonDefaultFontAndWidth(Page3.this.btnRepeatQuiz);
				Utils.setButtonDefaultFontAndWidth(Page3.this.btnNewQuiz);
				Shell shell = Display.getCurrent().getActiveShell();
				shell.layout(true, true);
			}
		});
	}

	private String quizTypeToString(QuizType quizType) {
		String strQuizType = "";
		if (quizType == QuizType.HOMEWORK) {
			strQuizType = I18N.getString(I18N.HOME_WORK);
		} else if (quizType == QuizType.TEST) {
			strQuizType = I18N.getString(I18N.TEST);
		} else if (quizType == QuizType.EXAM) {
			strQuizType = I18N.getString(I18N.EXAM);
		}
		return strQuizType;
	}

	@Override
	public void focus() {
		updateI18NTexts();

		// The following is commented, so you don't accidentally skip the displayed results by pressing RETURN
		// Shell shell = Display.getCurrent().getActiveShell();
		// shell.setDefaultButton(this.btnNewQuiz);

		updateScrollableContent(this.compGrid);
		this.scrolledComposite.layout(true, true);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Rectangle rect = Page3.this.scrolledComposite.getClientArea();
				Page3.this.scrolledComposite.setMinSize(Page3.this.scrolledComposite.getContent().computeSize(rect.width, SWT.DEFAULT));
				Page3.this.scrolledComposite.setOrigin(0, 0);
			}
		});
	}

	private static final String STR_M_DASH = "\u2015";

	private void updateScrollableContent(Composite parent) {
		Quiz quiz = this.wizard.getQuiz();

		this.slblUnit.setStyledText(quiz.getUnit());
		this.slblUnitTitle.setStyledText(quiz.getTitle(), Resources.TS_DEFAULT_BOLD);

		this.now = new Date();
		this.slblTimestamp.setStyledText(I18N.getFullDate(this.now));

		int percentCorrect = getPercentCorrect(quiz);
		int correctCount = getCorrectCount(quiz);
		int totalCount = getTotalCount(quiz);

		String strPercentCorrect = I18N.getString(I18N.N_PERCENT_CORRECT, "" + percentCorrect);
		String strCorrectOutOfTotal = "(" + I18N.getString(I18N.N_OF_M, "" + correctCount, "" + totalCount) + ")";
		this.slblPercent.setStyledText(strPercentCorrect, Resources.TS_SEMI_TITLE);
		this.slblScore.setStyledText(strCorrectOutOfTotal);

		// discard widgets and images
		for (Widget widget : this.widgetsToDispose) {
			widget.dispose();
		}

		boolean wasAllQuestionsOfQuiz = (quiz.getUnit().endsWith(I18N.getString(I18N.SELECTION)) == false);
		boolean isFullScore = (getPercentCorrect(quiz) == 100);
		if (wasAllQuestionsOfQuiz && isFullScore) {
			Label lblFoxIcon = new Label(this.compProgress, SWT.NONE);
			lblFoxIcon.setImage(Resources.getImage(Resources.IMG_FOX32x32));
			lblFoxIcon.setBackground(Resources.getColor(Resources.COLOR_WHITE));
			GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lblFoxIcon);
			this.widgetsToDispose.add(lblFoxIcon);

			StyledLabel slblProgressText = new StyledLabel(this.compProgress, SWT.LEFT);
			GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(slblProgressText);
			slblProgressText.setStyledText(I18N.getString(I18N.REAL_FOX));
			this.widgetsToDispose.add(slblProgressText);
		} else {
			Label lblProgressBar = new Label(this.compProgress, SWT.NONE);
			final Image imgProgressBar = createProgressBarImage(percentCorrect);
			lblProgressBar.setImage(imgProgressBar);
			lblProgressBar.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					imgProgressBar.dispose();
				}
			});

			lblProgressBar.setBackground(Resources.getColor(Resources.COLOR_WHITE));
			GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lblProgressBar);
			this.widgetsToDispose.add(lblProgressBar);

			Label lblFoxIcon = new Label(this.compProgress, SWT.NONE);
			lblFoxIcon.setImage(Resources.getImage(Resources.IMG_FOX16x16));
			lblFoxIcon.setBackground(Resources.getColor(Resources.COLOR_WHITE));
			GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lblFoxIcon);
			this.widgetsToDispose.add(lblFoxIcon);
		}

		// create new widgets
		List<Question> questions = quiz.getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);

			StyledLabel slblIndex = new StyledLabel(this.compGrid, SWT.NONE);
			GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(slblIndex);
			slblIndex.setStyledText("" + (i + 1));
			this.widgetsToDispose.add(slblIndex);

			StyledLabel slblQuestion = new StyledLabel(this.compGrid, SWT.NONE);
			GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).grab(true, false).applyTo(slblQuestion);
			slblQuestion.setStyledText(StyledString.parse(question.getQuestion(), Resources.TS_DEFAULT, Resources.TS_GREY));
			this.widgetsToDispose.add(slblQuestion);

			StyledLabel slblActualAnswer = new StyledLabel(this.compGrid, SWT.NONE);
			GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).grab(true, false).applyTo(slblActualAnswer);
			String actualAnswer = question.getActualAnswer();

			if (actualAnswer.isEmpty()) {
				slblActualAnswer.setStyledText(STR_M_DASH, Resources.TS_GREY);
			} else if (question.getGrade() == Grade.CORRECT) {
				TextStyle textStyle = getTextStyle(question.getExpectedGender());
				slblActualAnswer.setStyledText(actualAnswer, textStyle);
			} else if (question.getGrade() == Grade.ALMOST_CORRECT) {
				slblActualAnswer.setStyledText(actualAnswer, Resources.TS_ALMOST_CORRECT);
			} else {
				slblActualAnswer.setStyledText(actualAnswer, Resources.TS_WRONG);
			}
			this.widgetsToDispose.add(slblActualAnswer);

			if (question.getGrade() == Grade.CORRECT) {
				Label lblBlank = new Label(this.compGrid, SWT.NONE);
				this.widgetsToDispose.add(lblBlank);
			} else {
				StyledLabel slblExpectedAnswer = new StyledLabel(this.compGrid, SWT.NONE);
				GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).grab(true, false).applyTo(slblExpectedAnswer);

				String expectedAnswer = question.getExpectedAnswer();
				TextStyle textStyle = getTextStyle(question.getExpectedGender());
				slblExpectedAnswer.setStyledText(expectedAnswer, textStyle);
				this.widgetsToDispose.add(slblExpectedAnswer);
			}
		}
	}

	private TextStyle getTextStyle(Gender expectedGender) {
		TextStyle textStyle = Resources.TS_DEFAULT_BOLD;
		if (expectedGender == Gender.M) {
			textStyle = Resources.TS_M_BOLD;
		} else if (expectedGender == Gender.F) {
			textStyle = Resources.TS_F_BOLD;
		}
		return textStyle;
	}

	private int getPercentCorrect(Quiz quiz) {
		int correctCount = getCorrectCount(quiz);
		int totalCount = getTotalCount(quiz);
		return Math.round((100f * correctCount) / totalCount);
	}

	private int getCorrectCount(Quiz quiz) {
		int correctCount = 0;
		for (Question question : quiz.getQuestions()) {
			if (question.getGrade() == Grade.CORRECT) {
				correctCount++;
			}
		}
		return correctCount;
	}

	private int getTotalCount(Quiz quiz) {
		return quiz.getQuestions().size();
	}

	private Image createProgressBarImage(int percentCorrect) {
		final int WIDTH = 350;
		final int HEIGHT = 14;
		Image img = new Image(Display.getCurrent(), WIDTH, HEIGHT);

		GC gc = new GC(img);
		gc.setBackground(Resources.getColor(Resources.COLOR_LIGHT_FOX_ORANGE));
		gc.fillRectangle(0, 0, WIDTH, HEIGHT);
		gc.setBackground(Resources.getColor(Resources.COLOR_FOX_ORANGE));
		gc.fillRectangle(0, 0, (WIDTH * percentCorrect) / 100, HEIGHT);
		gc.setForeground(Resources.getColor(Resources.COLOR_LIGHT_GREY));
		gc.drawRectangle(0, 0, WIDTH - 1, HEIGHT - 1);
		gc.dispose();
		return img;
	}

	// Preferences ///////////////////////////////////////////////////////////////

	private static final String ID = "Page3";
	private static final String PREFERENCE_ID_FOLDERPATH = ".folderpath";
	private PreferenceStore prefStore;

	private void loadPreferences() {
		this.prefStore = new PreferenceStore(Resources.PREFS_FILENAME);
		try {
			this.prefStore.load();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	private void savePreferences() {
		try {
			this.prefStore.save();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	private void setPreferenceFolderpath(String strFolderpath) {
		final String id = ID + PREFERENCE_ID_FOLDERPATH;
		this.prefStore.setValue(id, strFolderpath);
	}

	private String getPreferenceFolderpath() {
		final String id = ID + PREFERENCE_ID_FOLDERPATH;
		String value = this.prefStore.contains(id) ? this.prefStore.getString(id) : "";
		return value;
	}

	// HTML output ///////////////////////////////////////////////////////////////

	private static final String HTML_M_DASH = "&mdash;";
	private static final String HTML_NBSP = "&nbsp;";

	private void saveQuizResult(String strFilepath, Quiz quiz) {
		List<String> list = new ArrayList<String>();
		String strHtml = generateHtml(quiz);
		list.add(strHtml);
		try {
			Files.write(new File(strFilepath).toPath(), list, Charset.forName("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	private static final String CR = System.getProperty("line.separator");

	private String generateHtml(Quiz quiz) {
		StringBuffer sb = new StringBuffer();

		appendHtmlProlog(sb);

		boolean isFullScore = getPercentCorrect(quiz) == 100;
		int numCols = isFullScore ? 3 : 4;

		appendLine(sb, "<table>");
		appendLine(sb, "<tr>");

		appendSpannedLine(sb, numCols, I18N.getString(I18N.EVALUATION), "header");
		appendSpannedLine(sb, numCols, HTML_NBSP);
		appendSpannedLine(sb, numCols, quizTypeToString(this.wizard.getQuizType()), "unit");
		appendSpannedLine(sb, numCols, quiz.getUnit(), "unit");
		appendSpannedLine(sb, numCols, quiz.getTitle(), "title");
		appendSpannedLine(sb, numCols, I18N.getFullDate(this.now), "timestamp");
		appendSpannedLine(sb, numCols, HTML_NBSP);

		int percentCorrect = getPercentCorrect(quiz);
		String strPercentCorrect = I18N.getString(I18N.N_PERCENT_CORRECT, "" + percentCorrect);
		appendSpannedLine(sb, numCols, strPercentCorrect, "percentage");

		int correctCount = getCorrectCount(quiz);
		int totalCount = getTotalCount(quiz);
		String strCorrectOutOfTotal = "(" + I18N.getString(I18N.N_OF_M, "" + correctCount, "" + totalCount) + ")";
		appendSpannedLine(sb, numCols, strCorrectOutOfTotal, "correctoutoftotal");

		appendLine(sb, "<tr>");
		appendLine(sb, "<td colspan=\"" + numCols + "\" align=\"center\">");
		if (isFullScore) {
			String dataUrlImgFox = getDataUrl(Resources.getImage(Resources.IMG_FOX32x32));

			appendLine(sb, "<table>");
			appendLine(sb, "<tr>");
			appendLine(sb, "<td>");
			appendLine(sb, String.format("<img class=\"foxicon32\" src=\"%s\">", dataUrlImgFox));
			appendLine(sb, "</td>");
			appendLine(sb, "<td>");
			appendLine(sb, I18N.getString(I18N.REAL_FOX));
			appendLine(sb, "</td>");
			appendLine(sb, "</tr>");
			appendLine(sb, "</table>");
		} else {
			Image imageProgressBar = createProgressBarImage(percentCorrect);
			String dataUrlImgProgressBar = getDataUrl(imageProgressBar);
			imageProgressBar.dispose();
			String dataUrlImgFox = getDataUrl(Resources.getImage(Resources.IMG_FOX16x16));

			appendLine(sb, "<table>");
			appendLine(sb, "<tr>");
			appendLine(sb, "<td class=\"td-progressbar\">");
			appendLine(sb, String.format("<img src=\"%s\">", dataUrlImgProgressBar));
			appendLine(sb, "</td>");
			appendLine(sb, "<td class=\"td-foxicon16\">");
			appendLine(sb, String.format("<img class=\"foxicon16\" src=\"%s\">", dataUrlImgFox));
			appendLine(sb, "</td>");
			appendLine(sb, "</tr>");
			appendLine(sb, "</table>");
		}
		appendLine(sb, "</td>");
		appendLine(sb, "</tr>");

		appendSpannedLine(sb, numCols, HTML_NBSP);

		for (int i = 0; i < quiz.getQuestions().size(); i++) {
			Question question = quiz.getQuestions().get(i);

			appendLine(sb, "<tr class=\"tr-plain\">");

			appendLine(sb, "<td>");
			appendLine(sb, "" + (i + 1));
			appendLine(sb, "</td>");

			String htmlQuestion = parseToHtml(question.getQuestion(), "", "grey");

			appendLine(sb, "<td>");
			appendLine(sb, htmlQuestion);
			appendLine(sb, "</td>");

			String cssActualAnswer = "";
			String strActualAnswer = question.getActualAnswer();

			if (strActualAnswer.isEmpty()) {
				cssActualAnswer = "grey";
				strActualAnswer = HTML_M_DASH;
			} else if (question.getGrade() == Grade.CORRECT) {
				cssActualAnswer = "bold";
				Gender expectedGender = question.getExpectedGender();
				if (expectedGender == Gender.M) {
					cssActualAnswer = "m-bold";
				} else if (expectedGender == Gender.F) {
					cssActualAnswer = "f-bold";
				}
			} else if (question.getGrade() == Grade.ALMOST_CORRECT) {
				cssActualAnswer = "almost-correct";
			} else {
				cssActualAnswer = "wrong";
			}

			appendLine(sb, "<td>");
			appendLine(sb, String.format("<span class=\"%s\">%s</span>", cssActualAnswer, strActualAnswer));
			appendLine(sb, "</td>");

			String cssExpectedAnswer = "";
			String strExpectedAnswer = question.getExpectedAnswer();

			if (question.getGrade() == Grade.CORRECT) {
				strExpectedAnswer = HTML_NBSP;
			} else {
				cssExpectedAnswer = "bold";
				Gender expectedGender = question.getExpectedGender();
				if (expectedGender == Gender.M) {
					cssExpectedAnswer = "m-bold";
				} else if (expectedGender == Gender.F) {
					cssExpectedAnswer = "f-bold";
				}
			}

			appendLine(sb, "<td>");
			appendLine(sb, String.format("<span class=\"%s\">%s</span>", cssExpectedAnswer, strExpectedAnswer));
			appendLine(sb, "</span>");
			appendLine(sb, "</td>");

			appendLine(sb, "</tr>");
		}

		appendLine(sb, "</table>");
		appendHtmlEpilog(sb);

		return sb.toString();
	}

	private void appendSpannedLine(StringBuffer sb, int colSpan, String text) {
		appendSpannedLine(sb, colSpan, text, null);
	}

	private void appendSpannedLine(StringBuffer sb, int colSpan, String text, String cssClass) {
		appendLine(sb, "<tr>");
		appendLine(sb, "<td colspan=\"" + colSpan + "\" align=\"center\">");
		if (cssClass != null) {
			appendLine(sb, String.format("<span class=\"%s\">%s</span>", cssClass, text));
		} else {
			appendLine(sb, text);
		}
		appendLine(sb, "</td>");
		appendLine(sb, "</tr>");
	};

	private static String parseToHtml(String text, String cssNormal, String cssGrey) {
		StringBuffer sb = new StringBuffer();

		String[] frags = text.split("_");
		boolean isGrey = text.startsWith("_");

		if (isGrey) {
			frags = Arrays.copyOfRange(frags, 1, frags.length);
		}

		for (String frag : frags) {
			String cssStyle = isGrey ? cssGrey : cssNormal;
			sb.append(String.format("<span class=\"%s\">%s</span>", cssStyle, frag));
			isGrey = !isGrey;
		}
		return sb.toString();
	}

	private void appendHtmlProlog(StringBuffer sb) {
		appendLine(sb, "<!DOCTYPE html>");
		appendLine(sb, "<html>");
		appendLine(sb, "<head>");
		appendLine(sb, "<meta charset=\"utf-8\">");
		appendLine(sb, "<style>");
		appendLine(sb, ".global { font-family: Calibri; }");
		appendLine(sb, ".center_outer { width: 100%; text-align: center; }");
		appendLine(sb, ".center_inner { display: inline-block; }");
		appendLine(sb, ".header { font-size: 145%; }");
		appendLine(sb, ".unit {}");
		appendLine(sb, ".title { font-weight: bold }");
		appendLine(sb, ".timestamp {}");
		appendLine(sb, ".percentage { font-size: 145%; }");
		appendLine(sb, ".correctoutoftotal {}");
		appendLine(sb, ".foxicon16 {}");
		appendLine(sb, ".foxicon32 {}");
		appendLine(sb, ".td-progressbar { padding-right: 2px; }");
		appendLine(sb, ".td-foxicon16 { padding-left: 2px; }");
		appendLine(sb, ".tr-plain { text-align: left }");
		appendLine(sb, "td { padding-left: 0.5em; padding-right: 0.5em; }");
		appendLine(sb, ".bold { font-weight: bold; }");
		appendLine(sb, ".m-bold { font-weight: bold; color: " + toCssRgb(Resources.COLOR_M) + "; }");
		appendLine(sb, ".f-bold { font-weight: bold; color: " + toCssRgb(Resources.COLOR_F) + "; }");
		appendLine(sb, ".grey { color: " + toCssRgb(Resources.COLOR_GREY) + "; }");
		appendLine(sb, ".wrong { color: " + toCssRgb(Resources.COLOR_RED) + "; text-decoration: line-through; }");
		appendLine(sb, ".almost-correct { color: " + toCssRgb(Resources.COLOR_ORANGE) + "; text-decoration: line-through; }");

		appendLine(sb, "</style>");
		appendLine(sb, "</head>");
		appendLine(sb, "<body>");
		appendLine(sb, "<div class=\"global center_outer\">");
		appendLine(sb, "<div class=\"center_inner\">");
	}

	private static void appendHtmlEpilog(StringBuffer sb) {
		appendLine(sb, "</div>");
		appendLine(sb, "</div>");
		appendLine(sb, "</body>");
		appendLine(sb, "</html>");
	}

	private static String toCssRgb(String colorId) {
		Color color = Resources.getColor(colorId);
		return String.format("rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
	}

	private static void appendLine(StringBuffer sb, String text) {
		append(sb, text);
		append(sb, CR);
	}

	private static void append(StringBuffer sb, String text) {
		sb.append(text);
	}

	private String getDataUrl(Image img) {
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { img.getImageData() };
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		imageLoader.save(out, SWT.IMAGE_PNG);
		try {
			out.close();
		} catch (IOException e) {
			// ignore
		}
		byte[] bytes = out.toByteArray();
		String strBase64Enccoded = Utils.base64Encode(bytes);
		String dataUrl = "data:image/png;base64, " + strBase64Enccoded;
		return dataUrl;
	}
}
// 747
