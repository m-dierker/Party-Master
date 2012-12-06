package com.partyrock.gui.uc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.wb.swt.SWTResourceManager;

import com.partyrock.LightMaster;
import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.gui.LightWindow;

/**
 * GUI editor for microcontrollers
 * @author Matthew
 * 
 */
public class UCEditor implements UCTableRenderer {

	protected Shell shlUCEditor;
	private Table table;

	private LightWindow main;

	/**
	 * Launch the application. This is here for the SWT Designer
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UCEditor window = new UCEditor(null);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UCEditor() {
		this(null);
	}

	public UCEditor(LightWindow main) {
		this.main = main;
	}

	/**
	 * Shows a window to add new microcontroller(s)
	 */
	public void addNewLocalArduino() {
//		InputDialog dialog = new InputDialog(shlUCEditor, "How many lights would you like to add?", "Add Lights");
//		String amountString = dialog.open();
//
//		int amount = 0;
//		try {
//			amount = Integer.parseInt(amountString);
//		} catch (Exception e) {
//			System.out.println("Invalid amount specified");
//			return;
//		}
//
//		for (int a = 0; a < amount; a++) {
//			LightController controller = new LightController(main.getMaster(), "Strand "
//					+ main.getMaster().getElements().size(), "l" + (main.getMaster().getElements().size()));
//			main.getMaster().addElement(controller);
//		}
//
//		System.out.println("About to update elements");
//
//		// Update the elements in the window manager
//		main.updateElements();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlUCEditor.open();
		shlUCEditor.layout();
		while (!shlUCEditor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlUCEditor = new Shell();
		shlUCEditor.setSize(722, 530);
		shlUCEditor.setText("µC Editor");
		shlUCEditor.setLayout(new GridLayout(1, false));

		Label lblAddElements = new Label(shlUCEditor, SWT.CENTER);
		lblAddElements.setFont(SWTResourceManager.getFont("Lucida Grande", 20, SWT.NORMAL));
		lblAddElements.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblAddElements.setText("Microcontroller Editor");

		table = new Table(shlUCEditor, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnType = new TableColumn(table, SWT.CENTER);
		tblclmnType.setWidth(123);
		tblclmnType.setText("Type");
		// Set not editable
//		tblclmnType.setData(new UCEditorColumnData(false));

		TableColumn tblclmnName = new TableColumn(table, SWT.CENTER);
		tblclmnName.setWidth(193);
		tblclmnName.setText("Name");

		TableColumn tblclmnId = new TableColumn(table, SWT.CENTER);
		tblclmnId.setWidth(184);
		tblclmnId.setText("Port");

		// Allow the table to be edited
//		table.addListener(SWT.MouseDoubleClick, new ElementsDoubleClickListener(this, table));

		Label label = new Label(shlUCEditor, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setAlignment(SWT.CENTER);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

		Label lblDoubleClickTo = new Label(shlUCEditor, SWT.NONE);
		lblDoubleClickTo.setText("Double click a microcontroller to make changes. ");

		Composite composite = new Composite(shlUCEditor, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addController();
			}
		});
		btnNewButton.setBounds(0, 0, 94, 28);
		btnNewButton.setText("Add Local Arduino");

	}

	/**
	 * The controller was updated in the table, so we should update the uC
	 * @param item The item that was updated
	 * @param col The column that was updated
	 * @param text The new text
	 */
	public void updateItemWithText(TableItem item, int col, String text) {
		switch (col) {
		case 0: // type
			break; // shouldn't be editable
		case 1: // name
			Microcontroller controller = (Microcontroller) item.getData();
			item.setText(col, text);
			controller.setName(text);
			System.out.println(controller);
			break;
		case 2: // port
			controller = (Microcontroller) item.getData();
			item.setText(col, text);
			controller.establishNewConnectionToPort(text);
			System.out.println(controller);
			break;
		}
	}

	/**
	 * When a button is clicked, should add new lights
	 */
	public void addController() {
		this.addNewLocalArduino();

		UCUpdater.updateElements(this, table);
	}

	/**
	 * Adds an element to the table
	 */
	public void addControllerAsRow(Microcontroller controller) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { controller.getType().getName(), controller.getName(), controller.getPort() });
		item.setData(controller);
	}

	public LightMaster getMaster() {
		return main.getMaster();
	}
}
