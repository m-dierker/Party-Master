package com.partyrock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.wb.swt.SWTResourceManager;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.lights.LightController;
import com.partyrock.gui.dialog.InputDialog;

public class ElementsEditor implements ElementTableRenderer {

	protected Shell shlElementsEditor;
	private Table table;
	private TableEditor editor;
	private LightWindow main;

	/**
	 * Launch the application. This is here for the SWT Designer
	 * @param args
	 */
//	public static void main(String[] args) {
//		try {
//			ElementsEditor window = new ElementsEditor(null);
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public ElementsEditor() {
		this(null);
	}

	public ElementsEditor(LightWindow main) {
		this.main = main;
	}

	/**
	 * Shows a window to add new light(s)
	 */
	public void addNewLights() {
		InputDialog dialog = new InputDialog(shlElementsEditor, "How many lights would you like to add?", "Add Lights");
		String amountString = dialog.open();

		int amount = 0;
		try {
			amount = Integer.parseInt(amountString);
		} catch (Exception e) {
			System.out.println("Invalid amount specified");
			return;
		}

		for (int a = 0; a < amount; a++) {
			LightController controller = new LightController(main.getMaster(), "Strand "
					+ main.getMaster().getElements().size(), "l" + (main.getMaster().getElements().size()));
			main.getMaster().addElement(controller);
		}

		System.out.println("About to update elements");

		// Update the elements in the window manager
		main.updateElements();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlElementsEditor.open();
		shlElementsEditor.layout();
		while (!shlElementsEditor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlElementsEditor = new Shell();
		shlElementsEditor.setSize(722, 530);
		shlElementsEditor.setText("Elements Editor");
		shlElementsEditor.setLayout(new GridLayout(1, false));

		Label lblAddElements = new Label(shlElementsEditor, SWT.CENTER);
		lblAddElements.setFont(SWTResourceManager.getFont("Lucida Grande", 20, SWT.NORMAL));
		lblAddElements.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblAddElements.setText("Elements Editor");

		table = new Table(shlElementsEditor, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnType = new TableColumn(table, SWT.CENTER);
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");

		TableColumn tblclmnName = new TableColumn(table, SWT.CENTER);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");

		TableColumn tblclmnId = new TableColumn(table, SWT.CENTER);
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");

		editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(e.x, e.y);
				int index = table.getTopIndex();
				while (index < table.getItemCount()) {
					boolean visible = false;
					final TableItem item = table.getItem(index);
					for (int i = 0; i < table.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							final int column = i;
							final Text text = new Text(table, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event e) {
									switch (e.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										text.dispose();
										break;
									case SWT.Traverse:
										switch (e.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											// FALL THROUGH
										case SWT.TRAVERSE_ESCAPE:
											text.dispose();
											e.doit = false;
										}
										break;
									}
								}
							};
							text.addListener(SWT.FocusOut, textListener);
							text.addListener(SWT.Traverse, textListener);
							editor.setEditor(text, item, i);
							text.setText(item.getText(i));
							text.selectAll();
							text.setFocus();
							return;
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

		Label label = new Label(shlElementsEditor, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setAlignment(SWT.CENTER);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(shlElementsEditor, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				addLights();
			}
		});
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnNewButton.setBounds(0, 0, 94, 28);
		btnNewButton.setText("Add Lights");

		Button btnAddLsers = new Button(composite, SWT.NONE);
		btnAddLsers.setText("Add Lasers");

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setText("Add LEDs");

	}

	/**
	 * When a button is clicked, should add new lights
	 */
	public void addLights() {
		this.addNewLights();

		System.out.println("Updating elements");

		ElementUpdater.updateElements(this, table);
	}

	/**
	 * Adds an element to the table
	 */
	public void addElementAsRow(ElementController element) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { element.getTypeName(), element.getName(), element.getID() });
		item.setData(element);
	}

	public LightMaster getMaster() {
		return main.getMaster();
	}
}
