package com.partyrock.gui.elements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

/**
 * Listener which will tell an ElementsTableEditor when show a text field over
 * fields in a Table, and will alert an ElementsTableEditor when it has changed
 * @author Matthew
 * 
 */
public class ElementsDoubleClickListener implements Listener {

	private ElementsTableEditor listener;
	private Table table;
	private TableEditor editor;

	public ElementsDoubleClickListener(ElementsTableEditor listener, Table table) {
		this.listener = listener;
		this.table = table;

		editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}

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
					TableColumn col = table.getColumn(i);

					// check the optional column data - if it's not here, assume
					// editable
					if (col.getData() instanceof ElementsEditorColumnData) {
						ElementsEditorColumnData data = (ElementsEditorColumnData) col.getData();
						if (!data.isEditable()) {
							return;
						}
					}

					final Text text = new Text(table, SWT.NONE);
					Listener textListener = new Listener() {
						public void handleEvent(final Event e) {
							switch (e.type) {
							case SWT.FocusOut:
								listener.updateItemWithText(item, column, text.getText());
								listener.getMaster().getLocationManager().unsavedChanges();
								text.dispose();
								break;
							case SWT.Traverse:
								switch (e.detail) {
								case SWT.TRAVERSE_RETURN:
									listener.updateItemWithText(item, column, text.getText());
									listener.getMaster().getLocationManager().unsavedChanges();
									// we intend to fall through here as we have to dispose of the text field either way
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
}
