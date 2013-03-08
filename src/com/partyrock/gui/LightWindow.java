package com.partyrock.gui;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import com.partyrock.anim.Animation;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.anim.execute.SingleAnimationExecutor;
import com.partyrock.comm.uc.Microcontroller;
import com.partyrock.config.PartyConstants;
import com.partyrock.element.ElementController;
import com.partyrock.gui.dialog.DropdownDialogObject;
import com.partyrock.gui.elements.ElementDisplay;
import com.partyrock.gui.elements.ElementTableRenderer;
import com.partyrock.gui.elements.ElementUpdater;
import com.partyrock.gui.elements.ElementsEditor;
import com.partyrock.gui.music.MusicRenderer;
import com.partyrock.gui.select.Selection;
import com.partyrock.gui.select.SelectionRenderer;
import com.partyrock.gui.timeline.TimelineRenderer;
import com.partyrock.gui.uc.UCEditor;
import com.partyrock.id.ID;
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
    private SelectionRenderer selectionRenderer;

    private final Canvas timeline;
    private final Scale scale;
    private Display display;
    private Selection selection = null;

    private int lastMouseDownX;
    private boolean dragging;

    public LightWindow(final LightMaster master, LightWindowManager manager) {
        this.master = master;
        this.windowManager = manager;
        display = manager.getDisplay();

        timelineRenderer = new TimelineRenderer(this);
        musicRenderer = new MusicRenderer(this);
        selectionRenderer = new SelectionRenderer(this);

        tableRenderer = new LightTableRenderer(master, this);

        this.windowManager.addElementDisplay(this);

        // Construct the GUI shell
        this.shell = new Shell(display);
        shell.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tableKeyReleased(e);
            }
        });
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

        @SuppressWarnings("unused")
        ToolItem toolItem_1 = new ToolItem(toolbar, SWT.SEPARATOR);

        ToolItem tltmPlay = new ToolItem(toolbar, SWT.NONE);
        tltmPlay.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                master.getShowManager().play();
            }
        });
        tltmPlay.setText("Play");

        ToolItem tltmPause = new ToolItem(toolbar, SWT.NONE);
        tltmPause.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                master.getShowManager().pause();
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
                tableMouseDown(e);
            }

            @Override
            public void mouseUp(MouseEvent e) {
                // tableMouseUp(e);
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                tableDoubleClick(e);
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

        @SuppressWarnings("unused")
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
        mntmSaveShow.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveShowFile();
            }
        });
        mntmSaveShow.setText("Save Show");

        MenuItem mntmSaveShowAs = new MenuItem(menu_1, SWT.NONE);
        mntmSaveShowAs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                saveShowFileAs();
            }
        });
        mntmSaveShowAs.setText("Save Show As");

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
        timeline.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                mMove(e);
            }
        });
        timeline.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                mUp(e);
            }

            public void mouseDown(MouseEvent e) {
                mDown(e);
            }
        });
        timeline.setSize(new Point(0, 30));
        timeline.setLayout(null);
        GridData gd_timeline = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_timeline.widthHint = 477;
        gd_timeline.heightHint = 50;
        timeline.setLayoutData(gd_timeline);
        timeline.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                timelineRenderer.renderTimeline(e.gc, timeline.getClientArea());
            }
        });

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

        redrawOverTime();

        shell.open();

        table.forceFocus();
    }

    protected void setCurrentTime(double time) {
        master.getShowManager().setCurrentTime(time);
        redraw();
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

        System.out.println("done");
        display.dispose();
    }

    /**
     * Adds a specific element to the end of the table, and sets the element as the item's data.
     * 
     * @param element the element to add
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
        File f = getShowFileFromDialog(SWT.OPEN);
        if (f != null) {
            master.getShowManager().loadShow(f);
        }
    }

    public void saveShowFile() {
        if (master.getShowManager().getShow() == null) {
            // We don't have a show right now, so make a new one
            this.saveShowFileAs();
        } else {
            // The file exists, just save it
            master.getShowManager().saveShowFile();
        }
    }

    public void saveShowFileAs() {
        File f = getShowFileFromDialog(SWT.SAVE);

        if (f.exists()) {
            boolean override = PartyToolkit.openConfirm(shell,
                    "Are you sure you wish to override the existing show file " + f.getName() + "?", "Overwrite?");
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
            master.getShowManager().saveShowToFile(f);
        }
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
     * @param filterTypes The filter types to display
     * @param filterNames The filter names to use
     * @param defaultName The default name for the file
     * @param path The path for the file dialog to display
     * @param style SWT constant, like SWT.OPEN or SWT.SAVE
     * @return
     */
    public File getFileFromDialog(String[] filterTypes, String[] filterNames, String defaultName, String path, int style) {
        FileDialog dialog = new FileDialog(shell, style);
        dialog.setFilterNames(filterNames);
        dialog.setFilterExtensions(filterTypes);
        dialog.setFilterPath(path);
        dialog.setFileName(defaultName);
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

    public void tableDoubleClick(MouseEvent e) {
        Point click = new Point(getAbsoluteCoordinate(e.x), e.y);
        for (int a = 0; a < table.getItemCount(); a++) {
            TableItem item = table.getItem(a);
            Rectangle animationColumn = item.getBounds(1);
            if (animationColumn.contains(click)) {
                // See if we clicked an animation
                ElementController element = (ElementController) item.getData();
                for (Animation animation : master.getShowManager().getAnimationsForElement(element)) {
                    int startPixel = animation.getRenderer().getStartX() + PartyConstants.ELEMENT_NAME_COLUMN_SIZE;
                    int width = animation.getRenderer().getWidth();

                    if (startPixel >= e.x && e.x <= startPixel + width) {
                        // The click was on this animation

                        final Menu animationMenu = new Menu(table.getShell(), SWT.POP_UP);
                        animationMenu.setData(animation);
                        Point dispPoint = table.toDisplay(click);
                        animationMenu.setLocation(dispPoint);

                        MenuItem deleteItem = new MenuItem(animationMenu, SWT.PUSH);
                        deleteItem.setText("Delete Animation");
                        deleteItem.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                master.getShowManager().deleteAnimation((ElementAnimation) animationMenu.getData());
                            }
                        });

                        MenuItem setupItem = new MenuItem(animationMenu, SWT.PUSH);
                        setupItem.setText("Re-setup Animation");
                        setupItem.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                ElementAnimation animation = (ElementAnimation) animationMenu.getData();
                                animation.setup(table.getShell());
                            }
                        });

                        animationMenu.setVisible(true);

                        break;
                    }
                }
            }
        }
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

    public void tableMouseDown(MouseEvent event) {
        // 3rd button, right click
        if (event.button == 3) {
            // Dynamically construct the popup menu based on what's selected
            Menu menu = new Menu(table.getShell(), SWT.POP_UP);

            MenuItem addAnimation = new MenuItem(menu, SWT.CASCADE);
            addAnimation.setText("Add Animation");
            Menu addAnimationMenu = new Menu(addAnimation);

            MenuItem previewAnimation = new MenuItem(menu, SWT.CASCADE);
            previewAnimation.setText("Preview Animation");
            Menu previewAnimationMenu = new Menu(previewAnimation);

            final ArrayList<ElementController> selectedElements = getSelectedElements();
            ArrayList<Class<? extends ElementAnimation>> animationList = master.getAnimationManager()
                    .getAnimationListForElements(selectedElements);

            // Add each available animation to the menu
            for (final Class<? extends ElementAnimation> c : animationList) {
                // Add to the preview menu
                MenuItem previewMenuItem = new MenuItem(previewAnimationMenu, SWT.PUSH);
                previewMenuItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        ElementAnimation animation = null;
                        try {
                            animation = c.getConstructor(LightMaster.class, int.class, String.class, ArrayList.class,
                                    double.class).newInstance(master, -1, ID.genID("an"), selectedElements, 2.0);
                            previewAnimation(animation);
                        } catch (Exception ex) {
                            System.out.println("There was an error previewing the animation " + animation
                                    + " - Check that it has the correct constructor");
                            ex.printStackTrace();
                        }
                    }
                });
                previewMenuItem.setText(c.getSimpleName());
                previewMenuItem.setData(c);

                // Add to the add menu
                MenuItem addMenuItem = new MenuItem(addAnimationMenu, SWT.PUSH);
                addMenuItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        ElementAnimation animation = null;
                        try {
                            Selection selection = getSelection();
                            if (selection == null) {
                                PartyToolkit
                                        .openConfirm(
                                                getShell(),
                                                "You don't have a portion of time selected! Select something and try to add the animation again.",
                                                "Cannot add animation");
                                return;
                            }
                            animation = c.getConstructor(LightMaster.class, int.class, String.class, ArrayList.class,
                                    double.class).newInstance(master, (int) (selection.start * 1000), ID.genID("an"),
                                    selectedElements, selection.duration);
                            addAnimation(animation);
                            master.getShowManager().unsavedChanges();
                        } catch (Exception ex) {
                            System.out.println("There was an error in adding the animation " + animation
                                    + " - Check that it has the correct constructor");
                            ex.printStackTrace();
                        }
                    }
                });
                addMenuItem.setText(c.getSimpleName());
                addMenuItem.setData(c);
            }

            previewAnimation.setMenu(previewAnimationMenu);
            addAnimation.setMenu(addAnimationMenu);

            MenuItem setUC = new MenuItem(menu, SWT.PUSH);
            setUC.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    ArrayList<DropdownDialogObject> options = new ArrayList<DropdownDialogObject>();
                    ArrayList<ElementController> selectedElements = getSelectedElements();

                    if (selectedElements.size() == 0) {
                        return;
                    }

                    for (Microcontroller uc : master.getControllers()) {
                        options.add(new DropdownDialogObject(uc.getName(), uc));
                    }

                    Microcontroller uc = (Microcontroller) PartyToolkit.openDropdown(shell,
                            "Which microcontroller would you like to use for these " + selectedElements.size()
                                    + " elements?", "uC Setup", options);

                    for (ElementController element : selectedElements) {
                        element.getExecutor().setMicrocontroller(uc);
                    }

                }
            });
            setUC.setText("Set Microcontroller");

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
        SingleAnimationExecutor.runAnimation(this, animation);
    }

    /**
     * Adds an animation to the animation list to be executed when the show is run
     * 
     * @param animation The animation, complete with start time and duration, to add
     */
    public void addAnimation(ElementAnimation animation) {
        animation.setup(getShell());
        master.getShowManager().addAnimation(animation);
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
                display.timerExec(100, this);
            }
        };

        display.timerExec(100, runnable);
    }

    public void tableKeyReleased(KeyEvent e) {
        if (e.keyCode == SWT.BS) {
            deleteSelectedElements();
        } else if (e.keyCode == SWT.SPACE) {
            togglePlayPause();
        }
    }

    /**
     * If playing, pauses. If pauses, plays. That is all.
     */
    public void togglePlayPause() {
        master.getShowManager().toggle();
    }

    /**
     * Launch the application. This is here for the SWT Designer
     * 
     * @param args
     */
    @SuppressWarnings("unused")
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

    public SelectionRenderer getSelectionRenderer() {
        return selectionRenderer;
    }

    /**
     * Returns the current selection, which may be null
     * 
     * @return
     */
    public Selection getSelection() {
        return selection;
    }

    /**
     * Called when the mouse buttons are pressed down
     */
    public void mDown(MouseEvent e) {
        if (e.button == 1) {
            lastMouseDownX = e.x;
            dragging = true;
        }
    }

    /**
     * Mouse up
     */
    public void mUp(MouseEvent e) {
        if (e.button == 1) {
            if (Math.abs(e.x - lastMouseDownX) >= 5) {
                setSelectionTo(e.x);
            } else if (selection != null) {
                selection = null;
            } else {
                e.x = getAbsoluteCoordinate(e.x);
                setCurrentTime(1.0 * (e.x - PartyConstants.ELEMENT_NAME_COLUMN_SIZE) / PartyConstants.PIXELS_PER_SECOND);
            }
            dragging = false;
        }
    }

    /**
     * Mouse moved
     */
    public void mMove(MouseEvent e) {
        if (dragging) {
            if (Math.abs(e.x - lastMouseDownX) >= 5) {
                setSelectionTo(e.x);
            } else {
                selection = null;
            }
        }
    }

    public void setSelectionTo(int x) {
        if (x < PartyConstants.ELEMENT_NAME_COLUMN_SIZE) {
            x = PartyConstants.ELEMENT_NAME_COLUMN_SIZE;
        }

        if (lastMouseDownX < PartyConstants.ELEMENT_NAME_COLUMN_SIZE) {
            lastMouseDownX = PartyConstants.ELEMENT_NAME_COLUMN_SIZE;
        }

        int startX = getAbsoluteCoordinate(Math.min(x, lastMouseDownX));
        int endX = getAbsoluteCoordinate(Math.max(x, lastMouseDownX));

        double start = 1.0 * (startX - PartyConstants.ELEMENT_NAME_COLUMN_SIZE) / PartyConstants.PIXELS_PER_SECOND;
        double duration = 1.0 * (endX - PartyConstants.ELEMENT_NAME_COLUMN_SIZE) / PartyConstants.PIXELS_PER_SECOND
                - start;
        selection = new Selection(start, duration);
    }

    /**
     * Returns an absolute coordinate factoring in the xOffset junk
     * 
     * @param relative the relative coordinate (such as returned from a MouseEvent)
     */
    public int getAbsoluteCoordinate(int relative) {
        return relative + -1 * timelineRenderer.getXOffset(timeline.getClientArea());
    }

    public LightElementSimulator getSimulator() {
        return simulator;
    }
}
