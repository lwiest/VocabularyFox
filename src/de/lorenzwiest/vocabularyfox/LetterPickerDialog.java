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
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class LetterPickerDialog extends Dialog {
	private static final String[] FRENCH_LETTERS = new String[] { //
		"À", "Â", "Æ", "Ç", "È", "É", "Ê", "Ë", "Î", "Ï", "Ô", "Œ", "Ù", "Û", "Ü", //
		"à", "â", "æ", "ç", "è", "é", "ê", "ë", "î", "ï", "ô", "œ", "ù", "û", "ü" //
	};

	private static final int LETTER_ICON_WIDTH = Utils.scaleToDisplay(34);
	private static final int LETTER_ICON_HEIGHT = Utils.scaleToDisplay(34);
	private static final String KEY_DATA_LETTER = "LETTER";

	private List<Table> letterIconTables = new ArrayList<Table>();

	private Font font;

	private String selectedLetter;

	public LetterPickerDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.NO_TRIM | SWT.ON_TOP);
		this.font = Resources.getFont(Resources.FONT_LETTER_PICKER);
		this.selectedLetter = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);

		final int NUM_CHARS_IN_ROW = 12;
		final int NUM_CHARS_IN_COL = 3;
		final int SPACING_PX = 3; // don't scale with display resolution
		final int INTRO_PX = 3; // don't scale with display resolution
		final int OUTRO_PX = 3; // don't scale with display resolution
		final int PICKER_WIDTH = INTRO_PX + (NUM_CHARS_IN_ROW * LETTER_ICON_WIDTH) + ((NUM_CHARS_IN_ROW - 1) * SPACING_PX) + OUTRO_PX;
		final int PICKER_HEIGHT = INTRO_PX + (NUM_CHARS_IN_COL * LETTER_ICON_HEIGHT) + ((NUM_CHARS_IN_COL - 1) * SPACING_PX) + OUTRO_PX;
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).hint(PICKER_WIDTH, PICKER_HEIGHT).applyTo(composite);

		composite.setBackground(Resources.getColor(Resources.COLOR_WHITE));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		composite.setLayout(new RowLayout());
		composite.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Rectangle rect = composite.getBounds();
				e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				e.gc.setLineWidth(1);
				e.gc.setLineStyle(SWT.LINE_SOLID);
				e.gc.drawRectangle(0, 0, rect.width - 1, rect.height - 1);
			}
		});

		for (String letterIconText : FRENCH_LETTERS) {
			addLetterIcon(composite, letterIconText);
		}

		return composite;
	}

	private void addLetterIcon(Composite parent, String letterIconText) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new RowData(LETTER_ICON_WIDTH, LETTER_ICON_HEIGHT));
		GridLayoutFactory.swtDefaults().margins(0, 0).applyTo(composite);
		createTable(composite, letterIconText);
	}

	private void createTable(Composite composite, final String letterIconText) {
		final Table letterIconTable = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).hint(LETTER_ICON_WIDTH, LETTER_ICON_HEIGHT).applyTo(letterIconTable);
		this.letterIconTables.add(letterIconTable);

		letterIconTable.setHeaderVisible(false);
		letterIconTable.setLinesVisible(false);
		letterIconTable.setData(KEY_DATA_LETTER, letterIconText);

		new TableItem(letterIconTable, SWT.NONE);

		Listener paintListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				final int MAGIC_OFFSET = 4; // magic number

				if (e.item instanceof TableItem) {
					switch (e.type) {
						case SWT.MeasureItem: {
							e.width = LETTER_ICON_WIDTH - MAGIC_OFFSET;
							e.height = LETTER_ICON_HEIGHT;
							break;
						}
						case SWT.PaintItem: {
							e.width = LETTER_ICON_WIDTH;
							e.height = LETTER_ICON_HEIGHT;
							e.gc.setTextAntialias(SWT.ON);
							e.gc.setFont(LetterPickerDialog.this.font);

							int textX = e.x;
							int textY = e.y;
							int offsetX = ((LETTER_ICON_WIDTH - e.gc.stringExtent(letterIconText).x) / 2) - MAGIC_OFFSET;
							e.gc.drawText(letterIconText, textX + offsetX, textY, true);
							break;
						}
						case SWT.EraseItem: {
							e.detail &= ~SWT.FOREGROUND;
							break;
						}
						default:
							break;
					}
				}
			}
		};
		letterIconTable.addListener(SWT.MeasureItem, paintListener);
		letterIconTable.addListener(SWT.PaintItem, paintListener);
		letterIconTable.addListener(SWT.EraseItem, paintListener);
		letterIconTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableSelected(letterIconTable);
			}
		});
		letterIconTable.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tableSelected(null);
			}
		});
		letterIconTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.keyCode & (SWT.SHIFT | SWT.ALT | SWT.CTRL)) > 0) {
					return;
				}

				e.doit = false;
				tableSelected(null);
			}
		});
	}

	private void tableSelected(Table selectedLetterIconTable) {
		String selectedLetter = null;
		if (selectedLetterIconTable != null) {
			selectedLetter = (String) selectedLetterIconTable.getData(KEY_DATA_LETTER);
		}
		this.selectedLetter = selectedLetter;

		getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				close();
			}
		});
	}

	public String getSelectedLetter() {
		return this.selectedLetter;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
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
}
