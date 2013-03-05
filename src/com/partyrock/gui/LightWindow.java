package com.partyrock.gui;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.config.PartyConstants;
import com.partyrock.element.ElementController;
import com.partyrock.gui.elements.ElementDisplay;
import com.partyrock.gui.elements.ElementTableRenderer;
import com.partyrock.gui.elements.ElementUpdater;
import com.partyrock.gui.elements.ElementsEditor;
import com.partyrock.gui.music.MusicRenderer;
import com.partyrock.gui.timeline.TimelineRenderer;
import com.partyrock.gui.uc.UCEditor;
import com.partyrock.tools.PartyToolkit;

/**
 * The main GUI window.
 * 
 * @author Matthew
 * 
 */
public class LightWindow implements ElementTableRenderer, ElementDisplay {
    private LightMaster master;
    private LightWindowManager windowManager;
    private Shell shell;
    private ToolBar toolbar;
    private ScrolledComposite tableScroll;
    private Table table;
    private ElementsEditor elementsEditor;
    private UCEditor ucEditor;
    private LightElementSimulator simulator;
    private LightTableRenderer tableRenderer;
    private TimelineRenderer timelineRenderer;
    private MusicRenderer musicRenderer;
    private final Canvas timeline;
    private final Scale scale;
    private Display display;

    public LightWindow(final LightMaster master, LightWindowManager manager) {
        this.master = master;
        this.windowManager = manager;
        display = manager.getDisplay();

        timelineRenderer = new TimelineRenderer(this);
        musicRenderer = new MusicRenderer(this);

        tableRenderer = new LightTableRenderer(master, this);

        this.windowManager.addElementDisplay(this);

        // Construct the GUI shell
        this.shell = new Shell(display);
        shell.setSize(new Point(900, 600));
        shell.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event) {
                master.onDispose();
            }
        });
        shell.setText("Party Master");

        // Layout with 1 column, and no equal width columns
        shell.setLayout(new GridLayout(1, false));

        // Create Toolbar
        toolbar = new ToolBar(shell, SWT.HORIZONTAL);

        ToolItem tltmSimulator = new ToolItem(toolbar, SWT.NONE);
        tltmSimulator.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                showSimulator();
            }
        });
        tltmSimulator.setText("Simulator");

        @SuppressWarnings("unused")
        ToolItem toolItem = new ToolItem(toolbar, SWT.SEPARATOR);

        // Generate Toolbar items
        ToolItem elementsEditorButton = new ToolItem(toolbar, SWT.PUSH);
        elementsEditorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                showElementsEditor();
            }
        });
        elementsEditorButton.setText("Elements Editor");

        ToolItem ucEditorButton = new ToolItem(toolbar, SWT.PUSH);
        ucEditorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                showUCEditor();
            }
        });
        ucEditorButton.setText("µC Editor");

        // Finish Toolbar init
        toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        toolbar.pack();

        ToolItem toolItem_1 = new ToolItem(toolbar, SWT.SEPARATOR);

        ToolItem tltmPlay = new ToolItem(toolbar, SWT.NONE);
        tltmPlay.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                master.getShowManager().playMusic();
            }
        });
        tltmPlay.setText("Play");

        ToolItem tltmPause = new ToolItem(toolbar, SWT.NONE);
        tltmPause.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                master.getShowManager().pauseMusic();
            }
        });
        tltmPause.setText("Pause");

        // Main table scroll container
        tableScroll = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        tableScroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        tableScroll.setExpandHorizontal(true);
        tableScroll.setExpandVertical(true);

        // Actually make the table
        table = new Table(tableScroll, SWT.FULL_SELECTION | SWT.MULTI);
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tableKeyReleased(e);
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                tableClick(e);
            }
        });
        table.setLinesVisible(false);
        table.setToolTipText("");

        // Do the custom rendering stuff for the table
        tableRenderer.addCustomListeners(table, this);

        // Set the scroll contianer's content
        tableScroll.setContent(table);

        TableColumn tblclmnColumn = new TableColumn(table, SWT.NONE);
        tblclmnColumn.setResizable(false);
        tblclmnColumn.setWidth(100);
        tblclmnColumn.setText("Column 1");

        TableColumn tblclmnColumn_1 = new TableColumn(table, SWT.NONE);
        tblclmnColumn_1.setWidth(100);
        tblclmnColumn_1.setText("Column 2");

        // Load in TableItems
        ElementUpdater.updateElements(this, table);

        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);

        MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
        mntmFile.setText("File");

        Menu menu_1 = new Menu(mntmFile);
        mntmFile.setMenu(menu_1);

        MenuItem mntmSaveLocation = new MenuItem(menu_1, SWT.NONE);
        mntmSaveLocation.setAccelerator(SWT.MOD1 + 'S');
        mntmSaveLocation.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                saveLocationFile();
            }
        });
        mntmSaveLocation.setText("Save Elements");

        MenuItem mntmSaveLocationAs = new MenuItem(menu_1, SWT.NONE);
        mntmSaveLocationAs.setAccelerator(SWT.MOD1 + SWT.SHIFT + 'S');
        mntmSaveLocationAs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                saveLocationFileAs();
            }
        });
        mntmSaveLocationAs.setText("Save Elements As");

        MenuItem mntmLoadLocation = new MenuItem(menu_1, SWT.NONE);
        mntmLoadLocation.setAccelerator(SWT.MOD1 + 'O');
        mntmLoadLocation.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                loadLocation();
            }
        });
        mntmLoadLocation.setText("Load Elements");

        MenuItem menuItem = new MenuItem(menu_1, SWT.SEPARATOR);

        MenuItem mntmLoadShow = new MenuItem(menu_1, SWT.NONE);
        mntmLoadShow.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                loadShow();
            }
        });
        mntmLoadShow.setText("Load Show");

        MenuItem mntmSaveShow = new MenuItem(menu_1, SWT.NONE);
        mntmSaveShow.setText("Save Show");

        new MenuItem(menu_1, SWT.SEPARATOR);

        MenuItem mntmLoadMusic = new MenuItem(menu_1, SWT.NONE);
        mntmLoadMusic.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadMusic();
            }
        });
        mntmLoadMusic.setText("Load Music");

        timeline = new Canvas(shell, SWT.NONE);
        timeline.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                System.out.println("up");
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                System.out.println("double click");
            }
        });
        timeline.setSize(new Point(0, 30));
        timeline.setLayout(null);
        GridData gd_timeline = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_timeline.widthHint = 477;
        gd_timeline.heightHint = 50;
        timeline.setLayoutData(gd_timeline);

        Composite composite = new Composite(shell, SWT.NONE);
        GridLayout gl_composite = new GridLayout(2, false);
        composite.setLayout(gl_composite);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        Label lblPartyMaster = new Label(composite, SWT.NONE);
        lblPartyMaster.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        lblPartyMaster.setAlignment(SWT.CENTER);
        lblPartyMaster.setOrientation(SWT.RIGHT_TO_LEFT);
        lblPartyMaster.setText("Welcome to Party Master");

        scale = new Scale(composite, SWT.NONE);
        scale.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                setPixelsPerSecond(15);
            }
        });
        scale.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                setPixelsPerSecond(scale.getSelection());
            }
        });
        scale.setMinimum(1);
        scale.setSelection(15);
        timeline.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                timelineRenderer.renderTimeline(e.gc, timeline.getClientArea());
            }
        });

        redrawOverTime();

        shell.open();
    }

    /**
     * Performs the GUI loop. This method will not return until the GUI closes.
     */
    public void loop() {
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }

    /**
     * Adds a specific element to the end of the table, and sets the element as the item's data.
     * 
     * @param element
     *            the element to add
     */
    public void addElementAsRow(ElementController element) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setData(element);

        // This is awkwardly necessary here
        ElementUpdater.packTable(table);
    }

    /**
     * Saves the location file, or pops open the dialog if it doesn't exist
     */
    public void saveLocationFile() {
        if (master.getLocationManager().getLocation() == null) {
            // We don't have a location right now, so make a new one
            this.saveLocationFileAs();
        } else {
            // The file exists, just save it
            master.getLocationManager().saveLocationFile();
        }
    }

    /**
     * Saves a new location file (pops up the file prompt)
     */
    public void saveLocationFileAs() {
        File f = getLocationFileFromDialog(SWT.SAVE);

        if (f.exists()) {
            boolean override = PartyToolkit.openConfirm(shell,
                    "Are you sure you wish to override the existing location file " + f.getName() + "?", "Overwrite?");
            if (override) {
                // If we don't delete, PersistentSettings automatically merges
                // the files which would be very bad (some of the old elements
                // might hang around, but only some, and it would depend on the
                // number of new elements. Basically, it would be very bad.)

                f.delete();
            } else {
                return;
            }
        }
        if (f != null) {
            master.getLocationManager().saveLocationToFile(f);
        }
    }

    public void loadLocation() {
        File f = getLocationFileFromDialog(SWT.OPEN);
        if (f != null) {
            master.getLocationManager().loadLocation(f);
        }
    }

    /**
     * Loads a show file
     */
    public void loadShow() {

    }

    public void loadMusic() {
        File f = getMP3FileFromDialog(SWT.OPEN);
        if (f != null) {
            master.getShowManager().loadMusic(f);
            shell.redraw();
        }
    }

    /**
     * Opens a file dialog to return a file of a certain type
     * 
     * @param filterTypes
     *            The filter types to display
     * @param filterNames
     *            The filter names to use
     * @param defaultName
     *            The default name for the file
     * @param path
     *            The path for the file dialog to display
     * @param style
     *            SWT constant, like SWT.OPEN or SWT.SAVE
     * @return
     */
    public File getFileFromDialog(String[] filterTypes, String[] filterNames, String defaultName, String path, int style) {
        FileDialog dialog = new FileDialog(shell, style);
        dialog.setFilterNames(filterNames);
        dialog.setFilterExtensions(filterTypes);
        dialog.setFilterPath(path);
        dialog.setFileName("location.loc");
        String fileName = dialog.open();
        if (fileName != null && !fileName.trim().equals("")) {
            try {
                File file = new File(fileName);
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.err.println("Error loading file");
        return null;
    }

    /**
     * Gets a new location file from a new FileDialog
     * 
     * @param style
     * @return
     */
    public File getLocationFileFromDialog(int style) {
        return getFileFromDialog(new String[] { "*.loc", "*.*" }, new String[] { "Location Files", "All Files (*.*)" },
                "location.loc", ".", style);
    }

    public File getShowFileFromDialog(int style) {
        return getFileFromDialog(new String[] { "*.pri", "*.*" }, new String[] { "Party Rock Show Files",
                "All Files (*.*)" }, "show.pri", ".", style);
    }

    public File getMP3FileFromDialog(int style) {
        return getFileFromDialog(new String[] { "*.mp3", "*.*" }, new String[] { "MP3 Files", "All Files (*.*)" }, "",
                "music", style);
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
        if (elementsEditor != null && !elementsEditor.isDisposed()) {
            elementsEditor.getShell().forceActive();
        } else {
            elementsEditor = new ElementsEditor(this);
            windowManager.addElementDisplay(elementsEditor);
            elementsEditor.open();
        }
    }

    /**
     * Updates the elements in the table
     */
    public void updateElements() {
        ElementUpdater.updateElements(this, table);
    }

    public void showUCEditor() {
        if (ucEditor != null && !ucEditor.isDisposed()) {
            ucEditor.getShell().forceActive();
        } else {
            ucEditor = new UCEditor(this);
            ucEditor.open();
        }
    }

    public void showSimulator() {
        if (simulator != null && !simulator.isDisposed()) {
            simulator.getShell().forceActive();
        } else {
            simulator = new LightElementSimulator(this);
            simulator.open();
        }
    }

    @Override
    public boolean isDisposed() {
        return shell.isDisposed();
    }

    public LightWindowManager getWindowManager() {
        return windowManager;
    }

    /**
     * Returns the width of the window
     */
    public int getWidth() {
        return shell.getSize().x;
    }

    /**
     * Returns the height of the window
     */
    public int getHeight() {
        return shell.getSize().y;
    }

    /**
     * Returns the size of the window
     * 
     * @return
     */
    public Point getSize() {
        return shell.getSize();
    }

    public Display getDisplay() {
        return display;
    }

    public ArrayList<ElementController> getSelectedElements() {
        ArrayList<ElementController> ret = new ArrayList<ElementController>();
        for (TableItem item : table.getSelection()) {
            ElementController element = (ElementController) item.getData();
            ret.add(element);
        }
        return ret;
    }

    public void tableClick(MouseEvent event) {
        // 3rd button, right click
        if (event.button == 3) {
            // Dynamically construct the popup menu based on what's selected
            Menu menu = new Menu(table.getShell(), SWT.POP_UP);

            MenuItem previewAnimation = new MenuItem(menu, SWT.CASCADE);
            previewAnimation.setText("Preview Animation");

            Menu previewAnimationMenu = new Menu(previewAnimation);
            final ArrayList<ElementController> selectedElements = getSelectedElements();
            ArrayList<Class<? extends ElementAnimation>> animationList = master.getAnimationManager()
                    .getAnimationListForElements(selectedElements);

            // Add each available animation to the menu
            for (final Class<? extends ElementAnimation> c : animationList) {
                MenuItem menuItem = new MenuItem(previewAnimationMenu, SWT.PUSH);
                menuItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        ElementAnimation animation = null;
                        try {
                            animation = c.getConstructor(LightMaster.class, int.class, ArrayList.class).newInstance(
                                    master, -1, selectedElements);
                            previewAnimation(animation);
                        } catch (Exception ex) {
                            System.out.println("There was an error previewing the animation " + animation
                                    + " - Check that it has the correct constructor");
                            ex.printStackTrace();
                        }
                    }
                });
                menuItem.setText(c.getSimpleName());
                menuItem.setData(c);
            }

            previewAnimation.setMenu(previewAnimationMenu);
            Point eventPoint = new Point(event.x, event.y);
            eventPoint = table.toDisplay(eventPoint);
            menu.setLocation(eventPoint);
            menu.setVisible(true);

        }
    }

    /**
     * Executes an animation immediately
     * 
     * @param animation
     */
    public void previewAnimation(ElementAnimation animation) {
        animation.setup(getShell());
        animation.trigger();
    }

    public void deleteSelectedElements() {
        ArrayList<ElementController> selected = getSelectedElements();

        if (selected.size() > 0) {
            boolean delete = PartyToolkit.openQuestion(this.getShell(),
                    "Are you sure you want to delete " + selected.size() + " element"
                            + (selected.size() > 1 ? "s" : "") + "?", "Delete elements?");
            if (delete) {
                for (ElementController victim : selected) {
                    master.removeElement(victim);
                }

                // Update all elements
                master.getWindowManager().updateElements();
            }
        }
    }

    /**
     * Sets the pixels per second and updates what needs to be updated
     * 
     * @param newPPS
     */
    public void setPixelsPerSecond(int newPPS) {
        PartyConstants.PIXELS_PER_SECOND = newPPS;
        scale.setSelection(newPPS);
        redraw();

    }

    public void redrawOverTime() {
        Runnable runnable = new Runnable() {
            public void run() {
                redraw();
                display.timerExec(33, this);
            }
        };

        display.timerExec(33, runnable);
    }

    public void tableKeyReleased(KeyEvent e) {
        if (e.keyCode == SWT.BS) {
            deleteSelectedElements();
        }
    }

    /**
     * Launch the application. This is here for the SWT Designer
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            LightWindow window = new LightWindow(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Table getMainTable() {
        return table;
    }

    public Canvas getTimeline() {
        return timeline;
    }

    /**
     * Called when something that affects the main element table and timeline and such needs to be redrawn
     */
    public void redraw() {
        if (!table.isDisposed()) {
            table.redraw();
        }

        if (!timeline.isDisposed()) {
            timeline.redraw();
        }
    }

    public MusicRenderer getMusicRenderer() {
        return musicRenderer;
    }
}
