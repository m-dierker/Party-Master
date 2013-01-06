package com.partyrock.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A MessageDialog is a generic class to display an question or warning, similar
 * to
 * JOptionPane. Usage:
 * 
 * MessageDialog dialog = new InputDialog(shell, "Message", "Title");
 * String result = dialog.open();
 * 
 * @author Matthew
 * 
 */
public class MessageDialog extends Dialog {

	protected Shell shell;
	private boolean result;
	private Button btnSubmit;
	private Composite composite;
	private Button btnCancel;
	private String message;
	private String[] buttonStrings;

	/**
	 * Create the dialog.
	 */
	public MessageDialog(Shell parent, String message) {
		this(parent, message, "Dialog");
	}

	public MessageDialog(Shell parent, String message, String title) {
		this(parent, message, title, new String[] { "Okay", "Cancel" });
	}

	/**
	 * Create a dialog
	 * @param parent The shell parent
	 * @param message The message to display
	 * @param title The title to display
	 * @param buttonStrings The strings to put on the buttons (length of array =
	 *            2, yes option first, then no option)
	 * @wbp.parser.constructor
	 */
	public MessageDialog(Shell parent, String message, String title, String[] buttonStrings) {
		super(parent, SWT.NONE);
		this.message = message;
		setText(title);
		this.buttonStrings = buttonStrings;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open() {
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
				result = true;
				dispose();
			}
		});
		btnSubmit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnSubmit.setText(buttonStrings[0]);

		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				result = false;
				dispose();
			}
		});
		btnCancel.setText(buttonStrings[1]);

	}
}
