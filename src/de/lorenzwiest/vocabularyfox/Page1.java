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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Page1 extends WizardPage {
	private StyledLabel slblHeader;

	private Text searchText;
	private Label searchTextIcon;

	private TreeViewer viewer;
	private TreeNode selectedTreeNode;

	private NodeMatcher nodeMatcher;

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
		this.viewer = createTree(composite);
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
		this.searchText.setTextLimit(100);

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
		this.searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				searchTextKeyPressed(e);
			}
		});
	}

	private void searchIconMouseDown() {
		this.searchText.setText("");
	}

	private void searchTextModified() {
		boolean isIconEnabled = this.searchText.getText().length() > 0;
		this.searchTextIcon.setVisible(isIconEnabled);

		String patternText = toLowerCasePlainText(this.searchText.getText().trim().toLowerCase());
		this.nodeMatcher.setPattern(patternText);

		if (patternText.isEmpty()) {
			this.viewer.setAutoExpandLevel(0);
			this.viewer.setSelection(null);
		} else {
			this.viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		}

		this.viewer.setInput(this.viewer.getInput());

		if (patternText.isEmpty() == false) {
			selectFirstLeaf();
		}

		boolean isEnabled = isLeafSelected();
		this.btnNext.setEnabled(isEnabled);
	}

	private void searchTextFocusGained() {
		boolean hasText = this.searchText.getText().length() > 0;
		if (hasText) {
			this.searchText.selectAll();
		}
	}

	private void searchTextKeyPressed(KeyEvent e) {
		boolean hasItems = this.viewer.getTree().getItemCount() > 0;
		boolean isArrowDown = (e.keyCode == SWT.ARROW_DOWN);
		if (hasItems && isArrowDown) {
			this.viewer.getTree().setFocus();
		}
	}

	private TreeViewer createTree(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);
		GridLayoutFactory.swtDefaults().numColumns(1).margins(0, 0).applyTo(composite);

		final TreeViewer viewer = new TreeViewer(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(viewer.getTree());

		viewer.setAutoExpandLevel(0);

		this.nodeMatcher = new NodeMatcher();
		viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new FilteredTreeLabelProvider(this.nodeMatcher)));
		viewer.setContentProvider(new FilteredTreeContentProvider());
		viewer.setFilters(new ViewerFilter[] { new FilteredTreeViewerFilter(this.nodeMatcher) });

		populateTree(viewer);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Page1.this.selectedTreeNode = (TreeNode) viewer.getStructuredSelection().getFirstElement();
				boolean isEnabled = isLeafSelected();
				Page1.this.btnNext.setEnabled(isEnabled);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Page1.this.selectedTreeNode = (TreeNode) viewer.getStructuredSelection().getFirstElement();
				if (isLeafSelected()) {
					nextButtonSelected();
				}
			}
		});

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.getTree().setFocus();
			}
		});

		return viewer;
	}

	private boolean isLeafSelected() {
		boolean isLeafSelected = (Page1.this.selectedTreeNode != null) && (Page1.this.selectedTreeNode.hasChildren() == false);
		return isLeafSelected;
	}

	//////////////////////////////////////////////////////////////////////////////

	private static class QuizInfo {
		String key;
		String path;
		Language targetLanguage;
		File file;

		public QuizInfo(String key, String unit, String title, Language targetLanguage, File file) {
			this.key = key;
			this.path = unit + " - " + title;
			this.targetLanguage = targetLanguage;
			this.file = file;
		}

		public String getKey() {
			return this.key;
		}

		public String getPath() {
			return this.path;
		}

		public Language getTargetLanguage() {
			return this.targetLanguage;
		}

		public File getFile() {
			return this.file;
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private void populateTree(TreeViewer viewer) {
		List<QuizInfo> quizInfos = createQuizList();
		TreeNode[] rootNodes = createTreeNodes(quizInfos);
		viewer.setInput(rootNodes);
	}

	private List<QuizInfo> createQuizList() {
		List<QuizInfo> quizInfos = new ArrayList<QuizInfo>();

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
				quizInfos.add(new QuizInfo(key, unit, title, targetLanguage, quizFile));
			}
		}

		quizInfos.sort(new Comparator<QuizInfo>() {
			@Override
			public int compare(QuizInfo quizInfo1, QuizInfo quizInfo2) {
				return quizInfo1.getKey().compareTo(quizInfo2.getKey());
			}
		});
		return quizInfos;
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

	private static TreeNode[] createTreeNodes(List<QuizInfo> quizInfos) {
		TreeNode rootNode = new TreeNode("root", null, null);

		for (QuizInfo quizInfo : quizInfos) {
			String strPath = quizInfo.getPath();
			List<String> pathElements = Arrays.asList(strPath.split("\\|"));
			for (int i = 0; i < pathElements.size(); i++) {
				pathElements.set(i, pathElements.get(i).trim());
			}
			addTreeNodeRecursively(rootNode, pathElements.get(0), pathElements.subList(1, pathElements.size()), quizInfo);
		}
		return rootNode.getChildren();
	}

	private static void addTreeNodeRecursively(TreeNode parentNode, String name, List<String> restNames, QuizInfo quizInfo) {
		if (restNames.isEmpty()) {
			Language targetLanguage = quizInfo.getTargetLanguage();
			File file = quizInfo.getFile();
			parentNode.addChild(new TreeNode(name, targetLanguage, file));
			return;
		}

		TreeNode childFolderNode = null;
		for (TreeNode node : parentNode.getChildren()) {
			if (name.equals(node.getText())) {
				childFolderNode = node;
				break;
			}
		}
		if (childFolderNode == null) {
			childFolderNode = new TreeNode(name, null, null);
			parentNode.addChild(childFolderNode);
		}

		addTreeNodeRecursively(childFolderNode, restNames.get(0), restNames.subList(1, restNames.size()), quizInfo);
	}

	private void selectFirstLeaf() {
		TreeItem firstLeafItem = getFirstLeaf(this.viewer.getTree());
		if (firstLeafItem != null) {
			this.viewer.getTree().setSelection(firstLeafItem);
		} else {
			this.viewer.getTree().setSelection(new TreeItem[] {});
		}
		this.viewer.setSelection(this.viewer.getSelection(), true);
	}

	private TreeItem getFirstLeaf(Tree tree) {
		for (TreeItem rootItem : tree.getItems()) {
			TreeItem childLeafItem = getChildLeafRecursively(rootItem);
			if (childLeafItem != null) {
				return childLeafItem;
			}
		}
		return null;
	}

	private TreeItem getChildLeafRecursively(TreeItem treeItem) {
		if (treeItem.getItemCount() == 0) {
			return treeItem;
		}

		for (TreeItem childItem : treeItem.getItems()) {
			TreeItem childLeafItem = getChildLeafRecursively(childItem);
			if (childLeafItem != null) {
				return childLeafItem;
			}
		}
		return null;
	}

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
		File quizFile = this.selectedTreeNode.getFile();
		Quiz quiz = Quiz.readQuiz(quizFile);

		// HACK
		quiz.setUnit(quiz.getUnit().replace("|", " "));

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

		// FIXME: Reload quizzes

		Shell shell = this.slblHeader.getShell();
		shell.setImage(Resources.getImage(Resources.IMG_FOX16x16));
		shell.setText(I18N.getString(I18N.APPLICATION_TITLE));
		shell.setDefaultButton(this.btnNext);
	}

	private static String toLowerCasePlainText(String text) {
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

	public static class TreeNode {
		static private final TreeNode[] EMPTY_NODES = new TreeNode[0];
		static private final List<TreeNode> EMPTY_LIST = new ArrayList<TreeNode>();

		private TreeNode parent;
		private final String text;
		private final String plainText;
		private final Language targetLanguage;
		private final File file;
		private List<TreeNode> children;

		private TreeNode(String text, Language targetLanguage, File file) {
			this.text = text;
			this.plainText = toLowerCasePlainText(text);
			this.targetLanguage = targetLanguage;
			this.file = file;
			this.parent = null;
			this.children = EMPTY_LIST;
		}

		public String getText() {
			return this.text;
		}

		public String getPlainText() {
			return this.plainText;
		}

		public Language getTargetLanguage() {
			return this.targetLanguage;
		}

		public File getFile() {
			return this.file;
		}

		public TreeNode getParent() {
			return this.parent;
		}

		public TreeNode[] getChildren() {
			if (this.children == EMPTY_LIST) {
				return EMPTY_NODES;
			}
			return this.children.toArray(EMPTY_NODES);
		}

		public boolean hasChildren() {
			return this.children.size() > 0;
		}

		public void addChild(TreeNode treeNode) {
			if (this.children == EMPTY_LIST) {
				this.children = new ArrayList<TreeNode>();
			}
			this.children.add(treeNode);
			treeNode.parent = this;
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

	private static class FilteredTreeLabelProvider implements IStyledLabelProvider {
		private NodeMatcher nodeMatcher;

		public FilteredTreeLabelProvider(NodeMatcher nodeMatcher) {
			this.nodeMatcher = nodeMatcher;
		}

		@Override
		public org.eclipse.jface.viewers.StyledString getStyledText(Object element) {
			TreeNode treeNode = (TreeNode) element;

			String text = treeNode.getText();
			String textToMatch = treeNode.getPlainText();

			StyledString styledString = new StyledString();
			if (this.nodeMatcher.matches(textToMatch)) {
				int posStartMatch = this.nodeMatcher.start();
				int posEndMatch = this.nodeMatcher.end();
				styledString.append(text.substring(0, posStartMatch), STYLER_DEFAULT);
				styledString.append(text.substring(posStartMatch, posEndMatch), STYLER_BOLD);
				styledString.append(text.substring(posEndMatch), STYLER_DEFAULT);
			} else {
				styledString.append(text, STYLER_DEFAULT);
			}
			return styledString;
		}

		@Override
		public Image getImage(Object element) {
			TreeNode treeNode = (TreeNode) element;
			Language targetLanguage = treeNode.getTargetLanguage();
			if (targetLanguage == Language.EN) {
				return Resources.getImage(Resources.IMG_ENGLISH16x16);
			} else if (targetLanguage == Language.FR) {
				return Resources.getImage(Resources.IMG_FRENCH16x16);
			} else {
				return Resources.getImage(Resources.IMG_FOLDER16x16);
			}
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// ignore
		}

		@Override
		public void dispose() {
			// ignore
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// ignore
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private static final class NodeMatcher {
		private Pattern pattern;
		private int start;
		private int end;

		public NodeMatcher() {
			setPattern("");
		}

		public void setPattern(String pattern) {
			this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
			this.start = -1;
			this.end = -1;
		}

		public boolean matches(String stringToMatch) {
			Matcher matcher = this.pattern.matcher(stringToMatch);
			boolean isMatch = matcher.find();
			this.start = isMatch ? matcher.start() : -1;
			this.end = isMatch ? matcher.end() : -1;
			return isMatch;
		}

		public int start() {
			return this.start;
		}

		public int end() {
			return this.end;
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	private static class FilteredTreeViewerFilter extends ViewerFilter {
		private final NodeMatcher nodeMatcher;

		public FilteredTreeViewerFilter(NodeMatcher nodeMatcher) {
			this.nodeMatcher = nodeMatcher;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			TreeNode treeNode = (TreeNode) element;
			boolean isSelected = isSelected(treeNode);
			return isSelected;
		}

		private boolean isSelected(TreeNode treeNode) {
			if (treeNode.hasChildren() == false) {
				TreeNode node = treeNode;
				while (node != null) {
					if (this.nodeMatcher.matches(node.getPlainText())) {
						return true;
					}
					node = node.getParent();
				}
				return false;
			}

			if (this.nodeMatcher.matches(treeNode.getPlainText())) {
				return true;
			}

			for (TreeNode childNode : treeNode.getChildren()) {
				if (isSelected(childNode)) {
					return true;
				}
			}

			return false;
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	public static class FilteredTreeContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parent) {
			TreeNode treeNode = (TreeNode) parent;
			return treeNode.getChildren();
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof TreeNode) {
				TreeNode treeNode = (TreeNode) element;
				return treeNode.getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			TreeNode treeNode = (TreeNode) element;
			return treeNode.hasChildren();
		}
	}
}
