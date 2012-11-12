package com.partyrock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.partyrock.LightMaster;

/**
 * The main GUI window
 * @author Matthew
 * 
 */
public class LightWindow {
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

		// Create Toolbar
		toolbar = new ToolBar(shell, SWT.HORIZONTAL);

		// Generate Toolbar items
		for (int i = 0; i < 8; i++) {
			ToolItem item = new ToolItem(toolbar, SWT.PUSH);
			item.setText("Item " + (i + 1));
		}

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

		// Make the rows here, in another method

		// Do listeners, but abstract away

	}
}
