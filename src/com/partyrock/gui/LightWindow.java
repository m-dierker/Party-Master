package com.partyrock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.gui.elements.ElementTableRenderer;
import com.partyrock.gui.elements.ElementUpdater;
import com.partyrock.gui.elements.ElementsEditor;

/**
 * The main GUI window
 * @author Matthew
 * 
 */
public class LightWindow implements ElementTableRenderer {
	private LightMaster master;
	private LightWindowManager manager;
	private Shell shell;
	private ToolBar toolbar;
	private ScrolledComposite tableScroll;
	private Table table;

	public LightWindow(LightMaster master, LightWindowManager manager) {
		this.master = master;
		this.manager = manager;

		// Construct the GUI shell
		this.shell = new Shell(manager.getDisplay());
		shell.setText("Light Master");

		// Layout with 1 column, and no equal width columns
		shell.setLayout(new GridLayout(1, false));

		// Create the action listener (which handles things like the toolbar
		// button clicks)
		LightWindowActionManager actionManager = new LightWindowActionManager(this);

		// Create Toolbar
		toolbar = new ToolBar(shell, SWT.HORIZONTAL);

		// Generate Toolbar items
		ToolItem addElementButton = new ToolItem(toolbar, SWT.PUSH);
		addElementButton.setData(GUIAction.EDIT_ELEMENTS);
		addElementButton.setText("Add Element");
		addElementButton.addListener(SWT.Selection, actionManager);

		// Finish Toolbar init
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolbar.pack();

		// Main table scroll container
		tableScroll = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tableScroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableScroll.setExpandHorizontal(true);
		tableScroll.setExpandVertical(true);

		// Actually make the table
		table = new Table(tableScroll, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.setToolTipText("");
//		table.addKeyListener(this);

		// Set the scroll contianer's content
		tableScroll.setContent(table);

		// Construct the table's columns (label + content)
		int columnCount = 2;
		for (int a = 0; a < columnCount; a++) {
			TableColumn column = new TableColumn(table, SWT.NONE);

			// This is hidden anyways, only here to help debugging if necessary
			column.setText("Column " + a);
		}

		// Load in TableItems
		ElementUpdater.updateElements(this, table);

		shell.open();

	}

	/**
	 * Performs the GUI loop. This method will not return until the GUI closes.
	 */
	public void loop() {
		while (!shell.isDisposed()) {
			if (!manager.getDisplay().readAndDispatch()) {
				manager.getDisplay().sleep();
			}
		}

		manager.getDisplay().dispose();
	}

	/**
	 * Adds a specific element to the end of the table, and sets the element as
	 * the item's data.
	 * @param element the element to add
	 */
	public void addElementAsRow(ElementController element) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(element.getName());
		item.setData(element);
	}

	public LightMaster getMaster() {
		return master;
	}

	public Shell getShell() {
		return shell;
	}

	/**
	 * Shows an ElementsEditor
	 */
	public void showElementsEditor() {
		ElementsEditor editor = new ElementsEditor(this);
		editor.open();
	}

	/**
	 * Updates the elements in the table
	 */
	public void updateElements() {
		ElementUpdater.updateElements(this, table);
	}
}
