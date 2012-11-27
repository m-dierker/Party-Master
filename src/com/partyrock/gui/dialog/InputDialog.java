package com.partyrock.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.wb.swt.SWTResourceManager;

/**
 * An InputDialog is a generic class to display an input dialog, similar to
 * JOptionPane. Usage:
 * 
 * InputDialog dialog = new InputDialog(shell, "Message", "Title");
 * String result = dialog.open();
 * 
 * @author Matthew
 * 
 */
public class InputDialog extends Dialog {

	protected Shell shell;
	private String result;
	private Text text;
	private Button btnSubmit;
	private Composite composite;
	private Button btnCancel;
	private String message;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */
	public InputDialog(Shell parent, String message) {
		this(parent, message, "Dialog");
	}

	public InputDialog(Shell parent, String message, String title) {
		super(parent, SWT.NONE);
		this.message = message;
		setText(title);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	public void dispose() {
		result = text.getText();
		shell.dispose();
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(456, 174);
//		shell.setSize(451, 164);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));

		text = new Text(shell, SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) { // pressed enter
					dispose();
				}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblMessage = new Label(shell, SWT.WRAP | SWT.CENTER);
		lblMessage.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblMessage.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
		lblMessage.setText(message);

		composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 1, 1));
		composite.setLayout(new GridLayout(2, false));

		btnSubmit = new Button(composite, SWT.NONE);
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				dispose();
			}
		});
		btnSubmit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnSubmit.setText("Okay");

		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				dispose();
			}
		});
		btnCancel.setText("Cancel");

	}
}
