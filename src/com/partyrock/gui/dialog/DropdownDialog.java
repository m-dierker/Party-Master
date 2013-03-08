package com.partyrock.gui.dialog;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class DropdownDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private String message;
    private ArrayList<DropdownDialogObject> options;
    private Combo combo;
    private int selected;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public DropdownDialog(Shell parent, String message, String title, ArrayList<DropdownDialogObject> options) {
        super(parent, SWT.NONE);
        setText(title);
        this.message = message;
        this.options = options;
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        if (selected == -1) {
            return null;
        }

        return options.get(selected).data;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setSize(450, 300);
        shell.setText(getText());
        shell.setLayout(new GridLayout(1, false));

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        lblNewLabel.setText(message);

        combo = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER);
        for (DropdownDialogObject obj : options) {
            combo.add(obj.option);
        }

        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Button btnOk = new Button(shell, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                selected = combo.getSelectionIndex();
                shell.dispose();
            }
        });
        btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        btnOk.setText("Okay");

    }
}
