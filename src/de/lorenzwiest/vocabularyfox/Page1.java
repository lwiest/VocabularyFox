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
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class Page1 extends WizardPage {
	private StyledLabel slblHeader;

	private Text searchText;
	private Label searchTextIcon;

	private TableViewer viewer;
	private TableColumn tableColumn1;
	private TableColumn tableColumn2;

	private TableElement selectedTableElement;

	private Button btnNext;
	private ImageButton ibtnPreferences;

	public Page1(Wizard wizard) {
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
		GridLayoutFactory.swtDefaults().margins(Resources.INDENT * 5, Resources.INDENT * 3).applyTo(composite);
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		this.slblHeader = new StyledLabel(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).grab(true, false).applyTo(this.slblHeader);

		createBlankLabel(composite);
		createSearch(composite);
		this.viewer = createTable(composite);
	}

	private void createBlankLabel(Composite composite) {
		new Label(composite, SWT.NONE);
	}

	private void createSearch(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(composite);
		GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).applyTo(composite);
		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));

		this.searchText = new Text(composite, SWT.SINGLE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(this.searchText);
		this.searchText.setFont(Resources.getFont(Resources.FONT_DEFAULT));

		this.searchTextIcon = new Label(composite, SWT.NULL);
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(this.searchTextIcon);
		this.searchTextIcon.setImage(Resources.getImage(Resources.IMG_CLEAR_ENABLED));
		this.searchTextIcon.setBackground(Resources.getColor(Resources.COLOR_WHITE));
		this.searchTextIcon.setVisible(false);
		this.searchTextIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				searchIconMouseDown();
			}
		});
		this.searchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				searchTextModified();
			}
		});
		this.searchText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				searchTextFocusGained();
			}
		});
	}

	private void searchIconMouseDown() {
		this.searchText.setText("");
	}

	private void searchTextModified() {
		boolean isIconEnabled = this.searchText.getText().length() > 0;
		this.searchTextIcon.setVisible(isIconEnabled);

		String patternText = toLowerCasePlainText(this.searchText.getText().toLowerCase());
		this.elementMatcher.setPattern(patternText);
		this.viewer.refresh();

		boolean isEnabled = this.viewer.getTable().getItemCount() > 0;
		this.btnNext.setEnabled(isEnabled);
		if (isEnabled) {
			this.viewer.getTable().select(0);
			this.selectedTableElement = (TableElement) this.viewer.getStructuredSelection().getFirstElement();
		}
	}

	private void searchTextFocusGained() {
		boolean hasText = this.searchText.getText().length() > 0;
		if (hasText) {
			this.searchText.selectAll();
		}
	}

	private TableViewer createTable(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();

		composite.setLayout(tableColumnLayout);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);
		final TableViewer viewer = new TableViewer(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(table);

		// TODO temporarily disabled - table.setHeaderVisible(true)
		// table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn0 = new TableColumn(table, SWT.LEFT);
		tableColumn0.setResizable(false);
		tableColumnLayout.setColumnData(tableColumn0, new ColumnPixelData(24));
		TableViewerColumn tbvColumn0 = new TableViewerColumn(viewer, tableColumn0);
		tbvColumn0.setLabelProvider(new DelegatingStyledCellLabelProvider(new StyledLabelProvider(this.elementMatcher, 0)));

		this.tableColumn1 = new TableColumn(table, SWT.LEFT);
		this.tableColumn1.setResizable(true);
		tableColumnLayout.setColumnData(this.tableColumn1, new ColumnWeightData(1));
		TableViewerColumn tbvColumn1 = new TableViewerColumn(viewer, this.tableColumn1);
		tbvColumn1.setLabelProvider(new DelegatingStyledCellLabelProvider(new StyledLabelProvider(this.elementMatcher, 1)));

		this.tableColumn2 = new TableColumn(table, SWT.LEFT);
		this.tableColumn2.setResizable(true);
		tableColumnLayout.setColumnData(this.tableColumn2, new ColumnWeightData(1));
		TableViewerColumn tbvColumn2 = new TableViewerColumn(viewer, this.tableColumn2);
		tbvColumn2.setLabelProvider(new DelegatingStyledCellLabelProvider(new StyledLabelProvider(this.elementMatcher, 2)));

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setFilters(new ViewerFilter[] { new TableViewerFilter(this.elementMatcher) });
		viewer.setComparator(new TableViewerSorter());

		populateTable(viewer);

		// TODO temporarily disabled - autoSizeViewerColumns(viewer)
		// autoSizeViewerColumns(viewer);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Page1.this.selectedTableElement = (TableElement) viewer.getStructuredSelection().getFirstElement();
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Page1.this.selectedTableElement = (TableElement) viewer.getStructuredSelection().getFirstElement();
				nextButtonSelected();
			}
		});

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.getTable().setSelection(0);
				Page1.this.selectedTableElement = (TableElement) viewer.getStructuredSelection().getFirstElement();
				viewer.getTable().setFocus();
			}
		});

		return viewer;
	}

	private void populateTable(TableViewer viewer) {
		List<TableElement> tableElements = new ArrayList<TableElement>();

		File quizFolder = new File(Resources.QUIZ_FOLDERNAME);
		ensureQuizFolderExists(quizFolder);

		File[] quizFiles = quizFolder.listFiles();
		for (File quizFile : quizFiles) {
			if (quizFile.isDirectory()) {
				continue;
			}

			Quiz quiz = Quiz.readQuiz(quizFile);

			String key = quiz.getKey();
			String title = quiz.getTitle();
			String unit = quiz.getUnit();
			Language targetLanguage = quiz.getTargetLanguage();

			if ((key != null) && (title != null) && (unit != null) && (targetLanguage != null)) {
				TableElement tableElement = new TableElement(key, targetLanguage, unit, title, quizFile);
				tableElements.add(tableElement);
			}
		}

		viewer.setInput(tableElements);
	}

	private void ensureQuizFolderExists(File quizFolder) {
		if (quizFolder.exists() == false) {
			quizFolder.mkdir();
		}

		File[] quizFiles = quizFolder.listFiles();
		if ((quizFiles != null) && (quizFiles.length == 0)) {
			String textFile = Resources.readTextFile(Resources.SAMPLE_QUIZ_SOURCEPATH);
			Path dstSampleQuizFile = new File(quizFolder, Resources.SAMPLE_QUIZ_FILENAME).toPath();
			try {
				List<String> dummy = new ArrayList<String>();
				dummy.add(textFile);
				Files.write(dstSampleQuizFile, dummy, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//	private void autoSizeViewerColumns(final TableViewer viewer) {
	//		Display.getDefault().asyncExec(new Runnable() {
	//			@Override
	//			public void run() {
	//				Table table = viewer.getTable();
	//				table.setRedraw(false);
	//
	//				for (int col = 0; col < table.getColumnCount(); col++) {
	//					TableColumn tableColumn = table.getColumn(col);
	//					if (tableColumn.getResizable()) {
	//						tableColumn.pack();
	//					}
	//				}
	//
	//				int tableWidth = table.getClientArea().width;
	//				int totalColumnWidth = 0;
	//				for (int col = 0; col < table.getColumnCount(); col++) {
	//					totalColumnWidth += table.getColumn(col).getWidth();
	//				}
	//				int lastColumnIndex = table.getColumnCount() - 1;
	//				int lastColumnWidth = table.getColumn(lastColumnIndex).getWidth();
	//				int newLastColumnWidth = lastColumnWidth + (tableWidth - totalColumnWidth);
	//				table.getColumn(lastColumnIndex).setWidth(newLastColumnWidth);
	//
	//				table.setRedraw(true);
	//			}
	//		});
	//	}

	public void createButtons(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(composite);
		GridLayoutFactory.swtDefaults().applyTo(composite);

		Composite compButtons = new Composite(composite, SWT.NONE);
		compButtons.setLayout(new GridLayout(3, false));
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(compButtons);

		this.ibtnPreferences = new ImageButton(compButtons, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(this.ibtnPreferences);
		this.ibtnPreferences.setImage(Resources.getImage(Resources.IMG_GEAR));
		this.ibtnPreferences.setClickImage(Resources.getImage(Resources.IMG_GEAR_CLICK));
		this.ibtnPreferences.setHoverImage(Resources.getImage(Resources.IMG_GEAR_HOVER));
		this.ibtnPreferences.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				preferencesButtonSelected();
			}
		});

		createHorizontalSpacer(compButtons);

		this.btnNext = new Button(compButtons, SWT.PUSH);
		GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(this.btnNext);
		Utils.setButtonDefaultFontAndWidth(this.btnNext);
		this.btnNext.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				nextButtonSelected();
			}
		});
	}

	private void createHorizontalSpacer(Composite composite) {
		Label lblHorizontalSpacer = new Label(composite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(lblHorizontalSpacer);
	}

	private void preferencesButtonSelected() {
		boolean isBtnNextEnabled = this.btnNext.isEnabled();
		this.btnNext.setEnabled(false);

		PreferencesDialog aboutDialog = new PreferencesDialog(Display.getCurrent().getActiveShell(), this.wizard);
		aboutDialog.setBlockOnOpen(true);
		aboutDialog.open();

		this.btnNext.setEnabled(isBtnNextEnabled);
	}

	private void nextButtonSelected() {
		File quizFile = this.selectedTableElement.getFile();
		Quiz quiz = Quiz.readQuiz(quizFile);
		this.wizard.setQuiz(quiz);
		this.wizard.nextPage();
	}

	@Override
	public void updateI18NTexts() {
		this.slblHeader.setStyledText(I18N.getString(I18N.HELLO), Resources.TS_TITLE);
		this.ibtnPreferences.setToolTipText(I18N.getString(I18N.PREFERENCES));
		this.btnNext.setText(I18N.getString(I18N.START_QUIZ));

		this.searchTextIcon.setToolTipText(I18N.getString(I18N.CLEAR));
		this.searchText.setMessage(I18N.getString(I18N.SEARCH_QUIZ));
		this.tableColumn1.setText(I18N.getString(I18N.UNIT));
		this.tableColumn2.setText(I18N.getString(I18N.TITLE));

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Utils.setButtonDefaultFontAndWidth(Page1.this.btnNext);
				Shell shell = Display.getCurrent().getActiveShell();
				shell.layout(true, true);
			}
		});
	}

	@Override
	public void focus() {
		Shell shell = this.slblHeader.getShell();
		shell.setImage(Resources.getImage(Resources.IMG_FOX16x16));
		shell.setText(I18N.getString(I18N.APPLICATION_TITLE));
		shell.setDefaultButton(this.btnNext);
	}

	private String toLowerCasePlainText(String text) {
		final String[][] REPLACEMENTS = { //
			{ "à", "a" }, //
			{ "â", "a" }, //
			{ "ç", "c" }, //
			{ "è", "e" }, //
			{ "é", "e" }, //
			{ "ê", "e" }, //
			{ "ë", "e" }, //
			{ "î", "i" }, //
			{ "ï", "i" }, //
			{ "ô", "o" }, //
			{ "ù", "u" }, //
			{ "û", "u" }, //
			{ "ü", "u" }, //
		};

		text = text.toLowerCase();
		for (String[] replacement : REPLACEMENTS) {
			text = text.replaceAll(replacement[0], replacement[1]);
		}
		return text;
	}

	//////////////////////////////////////////////////////////////////////////////

	private class TableElement {
		private String key;
		private Language targetLanguage;
		private String unit;
		private String plainTextUnit;
		private String title;
		private String plainTextTitle;
		private File file;

		public TableElement(String key, Language targetLanguage, String unit, String title, File file) {
			this.key = key;
			this.targetLanguage = targetLanguage;
			this.unit = unit;
			this.plainTextUnit = toLowerCasePlainText(unit);
			this.title = title;
			this.plainTextTitle = toLowerCasePlainText(title);
			this.file = file;
		}

		public String getKey() {
			return this.key;
		}

		public Language getTargetLanguage() {
			return this.targetLanguage;
		}

		public String getUnit() {
			return this.unit;
		}

		public String getPlainTextUnit() {
			return this.plainTextUnit;
		}

		public String getTitle() {
			return this.title;
		}

		public String getPlainTextTitle() {
			return this.plainTextTitle;
		}

		public File getFile() {
			return this.file;
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private static final Styler STYLER_DEFAULT = new Styler() {
		@Override
		public void applyStyles(TextStyle textStyle) {
			textStyle.font = Resources.getFont(Resources.FONT_DEFAULT);
		}
	};

	private static final Styler STYLER_BOLD = new Styler() {
		@Override
		public void applyStyles(TextStyle textStyle) {
			textStyle.font = Resources.getFont(Resources.FONT_DEFAULT_BOLD);
		}
	};

	//////////////////////////////////////////////////////////////////////////////

	private static class StyledLabelProvider implements IStyledLabelProvider {
		private static final StyledString EMPTY_STYLED_STRING = new StyledString();

		private ElementMatcher elementMatcher;
		private int columnIndex;

		public StyledLabelProvider(ElementMatcher elementMatcher, int columnIndex) {
			this.elementMatcher = elementMatcher;
			this.columnIndex = columnIndex;
		}

		@Override
		public org.eclipse.jface.viewers.StyledString getStyledText(Object element) {
			if (this.columnIndex == 1) {
				TableElement tableElement = (TableElement) element;
				return applyStyles(tableElement.getUnit(), tableElement.getPlainTextUnit());
			} else if (this.columnIndex == 2) {
				TableElement tableElement = (TableElement) element;
				return applyStyles(tableElement.getTitle(), tableElement.getPlainTextTitle());
			}
			return EMPTY_STYLED_STRING;
		}

		@Override
		public Image getImage(Object element) {
			if (this.columnIndex == 0) {
				TableElement tableElement = (TableElement) element;
				switch (tableElement.getTargetLanguage()) {
					case EN:
						return Resources.getImage(Resources.IMG_ENGLISH15x15);
					case FR:
						return Resources.getImage(Resources.IMG_FRENCH15x15);
				}
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// empty
		}

		@Override
		public void dispose() {
			// empty
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// empty
		}

		private StyledString applyStyles(String text, String textToMatch) {
			StyledString styledString = new StyledString();
			if (this.elementMatcher.matches(textToMatch)) {
				int posStartMatch = this.elementMatcher.getStart();
				int posEndMatch = this.elementMatcher.getEnd();
				styledString.append(text.substring(0, posStartMatch), STYLER_DEFAULT);
				styledString.append(text.substring(posStartMatch, posEndMatch), STYLER_BOLD);
				styledString.append(text.substring(posEndMatch), STYLER_DEFAULT);
				return styledString;
			}
			return styledString.append(text, STYLER_DEFAULT);
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private static class TableViewerSorter extends ViewerComparator {
		private static final Collator COLLATOR = Collator.getInstance();

		@Override
		public int compare(Viewer viewer, Object o1, Object o2) {
			TableElement tableElement1 = (TableElement) o1;
			TableElement tableElement2 = (TableElement) o2;
			return COLLATOR.compare(tableElement1.getKey(), tableElement2.getKey());
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private static class TableViewerFilter extends ViewerFilter {
		private final ElementMatcher elementMatcher;

		public TableViewerFilter(ElementMatcher elementMatcher) {
			this.elementMatcher = elementMatcher;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof TableElement) {
				TableElement tableElement = (TableElement) element;
				String plainTextUnit = tableElement.getPlainTextUnit();
				if (this.elementMatcher.matches(plainTextUnit)) {
					return true;
				}
				String plainTextTitle = tableElement.getPlainTextTitle();
				if (this.elementMatcher.matches(plainTextTitle)) {
					return true;
				}
			}
			return false;
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private ElementMatcher elementMatcher = new ElementMatcher();

	private static class ElementMatcher {
		private Pattern pattern;
		private Matcher matcher;
		private int matchStart = -1;
		private int matchEnd = -1;

		public ElementMatcher() {
			setPattern(""); //$NON-NLS-1$
		}

		public void setPattern(String pattern) {
			this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
		}

		public boolean matches(String stringToMatch) {
			this.matcher = this.pattern.matcher(stringToMatch);
			boolean isFind = this.matcher.find();
			this.matchStart = isFind ? this.matcher.start() : -1;
			this.matchEnd = isFind ? this.matcher.end() : -1;
			return isFind;
		}

		public int getStart() {
			return this.matchStart;
		}

		public int getEnd() {
			return this.matchEnd;
		}
	}
}
// 574