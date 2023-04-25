package org.main;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import org.json.simple.parser.ParseException;
import org.main.textfilefc.TextFileView;
import org.main.textfilefc.TextFilter;
import org.main.textfilefc.TextPreview;
import org.main.components.*;
import org.main.components.Button;
import org.main.components.TextField;
import org.main.checkbox.CheckListItem;
import org.main.checkbox.CheckListRenderer;
import org.main.datavalidator.Rule1ValidatorEngine;
import org.main.datavalidator.Rule2ValidatorEngine;
import org.main.engine.ReaderEngine;
import org.main.filechooser.ImageFileView;
import org.main.filechooser.ImageFilter;
import org.main.filechooser.ImagePreview;
import org.main.jtable.CustomRenderer;
import org.main.jtable.Rule1.Rule1Model;
import org.main.jtable.Rule1.Rule1TableModel;
import org.main.jtable.Rule1.Rule1FieldsWindow;
import org.main.jtable.Rule2.Rule2FieldsWindow;
import org.main.jtable.Rule2.Rule2Model;
import org.main.jtable.Rule2.Rule2TableModel;
import org.main.jtable.TableDark;
import org.main.loadrulefc.RuleFileView;
import org.main.loadrulefc.RuleFilter;
import org.main.loadrulefc.RulePreview;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import static org.main.components.FondClass.fontOptions;

/*
 * @auther :  Sakthivel Iyappan
 * @date: 26 April 2023
 */
public class App extends JPanel implements ActionListener {

    int hz = 500;
    int msec = 100;
    double vol = 0.3;
    JPanel rightJpanel;
    JPanel leftJPanel;
    private int maxX;
    private int maxY;
    private JFileChooser fc;
    private JFileChooser fcLoadRule;
    private JFileChooser fcDownloadOutput;
    private TextField filePath;
    Button uploadButton;
    Button reload;
    Button loadData;
    Button downloadRule;
    Button uploadRule;
    Button run;
    JTextArea output;
    private String headerDirection = "Row";
    TextField indexOfHead;
    private static String[] sizeOptions = {"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28"};
    Button changeFont = new Button("ChangeFont");
    Button download = new Button("Download");
    JLabel fontLabelText = new JLabel("Font: ");
    JLabel fontSizeLabel = new JLabel("Size: ");

    ComboBox fontName = new ComboBox(fontOptions);
    ComboBox fontSize = new ComboBox(sizeOptions);

    JToolBar tool = new JToolBar() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new java.awt.Color(255, 255, 240));
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    };

    public static Font fontTitle = new Font("Comic Sans Ms", Font.BOLD, 12);
    Color themeColor = new java.awt.Color(255, 255, 255);
    JList listRules = new JList(new CheckListItem[]{
            new CheckListItem("Rule 1 :- Find empty Cells"),
            new CheckListItem("Rule 2 :- Validate Cell format")
    });

    HashMap<Integer, String> selectedRules = new HashMap<>();
    private MaterialTabbed materialTabbed1 = new MaterialTabbed();

    // Engine variables
    Map<String, Map<String, Map<String, String>>> inputExcelData = null;
    final ReaderEngine readerEngine = new ReaderEngine();

    // Table1 creation variables
    Button add1;
    Button remove1;
    Button edit1;
    private javax.swing.JScrollPane jScrollPane1;
    private Rule1TableModel tableModel = new Rule1TableModel();
    private TableDark table = new TableDark(tableModel) {
        public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
            //Always toggle on single selection
            super.changeSelection(rowIndex, columnIndex, !extend, extend);
        }
    };

    // Table2  creation variables
    private javax.swing.JScrollPane jScrollPane2;
    Button add2;
    Button remove2;
    Button edit2;
    private Rule2TableModel tableModel2 = new Rule2TableModel();
    private TableDark table2 = new TableDark(tableModel2) {
        public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
            //Always toggle on single selection
            super.changeSelection(rowIndex, columnIndex, !extend, extend);
        }
    };

    public App() throws IOException, ParseException {


        jScrollPane1 = new javax.swing.JScrollPane();
        table.fixTable(jScrollPane1);
        jScrollPane1.setViewportView(table);

        //Set your own renderer.  You'll have to set this for Number and Boolean too if you're using those
        CustomRenderer cr = new CustomRenderer(table.getDefaultRenderer(Object.class), Color.darkGray, Color.darkGray, Color.darkGray, Color.darkGray);
        table.setDefaultRenderer(Object.class, cr);


        jScrollPane2 = new javax.swing.JScrollPane();
        table2.fixTable(jScrollPane2);
        jScrollPane2.setViewportView(table2);

        //Set your own renderer.  You'll have to set this for Number and Boolean too if you're using those
        CustomRenderer cr2 = new CustomRenderer(table2.getDefaultRenderer(Object.class), Color.darkGray, Color.darkGray, Color.darkGray, Color.darkGray);
        table2.setDefaultRenderer(Object.class, cr2);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        maxX = screenSize.width - 100;
        maxY = screenSize.height - 100;
        setPreferredSize(new Dimension(maxX, maxY));
        setLayout(new BorderLayout());

        add(new JPanel() {
            {
                String headerText = String.join(
                        "\n"
                        , ""

                );

                add(new JLabel(headerText));
                setBackground(themeColor);

            }

        }, BorderLayout.PAGE_START);


        // ---------------------- right side
        rightJpanel = new JPanel(new BorderLayout());
        rightJpanel.setBackground(themeColor);
        rightJpanel.setPreferredSize(new Dimension(maxX / 2 - 100, maxY - 150));
        JPanel fields = new JPanel(new BorderLayout());
        fields.setBackground(themeColor);
        rightJpanel.setBorder(createTitleBorder(" Console Output :- "));

        // Editor
        output = new JTextArea();
        output.setBackground(new java.awt.Color(255, 255, 240));
        changeFont.addActionListener(this);
        changeFont.setToolTipText("Change the Font");
        changeFont.setBackground(new java.awt.Color(103, 103, 103));
        changeFont.setForeground(new java.awt.Color(255, 255, 255));
        download.addActionListener(this);
        download.setToolTipText("Download output as text file");
        download.setBackground(new java.awt.Color(103, 103, 103));
        download.setForeground(new java.awt.Color(255, 255, 255));
        fontLabelText.setToolTipText("Set the kind of Font");
        fontSizeLabel.setToolTipText("Set the size of the Font");
        tool.add(fontLabelText);
        tool.add(fontName);
        tool.addSeparator();
        tool.add(fontSizeLabel);
        tool.add(fontSize);
        tool.addSeparator();
        tool.add(changeFont);
        tool.addSeparator();
        tool.add(download);
        tool.setBackground(themeColor);
        fields.add(tool, BorderLayout.PAGE_START);
        output.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(103, 103, 103)));
        output.setLineWrap(true);
        fields.add(output, BorderLayout.CENTER);
        rightJpanel.add(fields, BorderLayout.CENTER);


        //  leftJPanel --------------
        leftJPanel = new JPanel(new BorderLayout());
        leftJPanel.setBackground(new Color(255, 255, 255));
        leftJPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(103, 103, 103)));
        leftJPanel.setPreferredSize(new Dimension(maxX / 2, maxY - 150));

        JPanel fileUpload = new JPanel(new BorderLayout());
        JPanel bottomBtnG = new JPanel(new BorderLayout());
        fileUpload.setBackground(new java.awt.Color(255, 255, 240));
        bottomBtnG.setBackground(themeColor);
        leftJPanel.add(fileUpload, BorderLayout.PAGE_START);
        fileUpload.add(bottomBtnG, BorderLayout.PAGE_END);
        fileUpload.setBorder(createTitleBorder("Upload Excel:(only .xlsx file)"));
        JPanel jPanelBtn = new JPanel(new FlowLayout());
        jPanelBtn.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(103, 103, 103)));
        jPanelBtn.setBackground(new java.awt.Color(255, 255, 240));
        downloadRule = new Button("Download Rule .");
        jPanelBtn.add(downloadRule);
        downloadRule.setBackground(new java.awt.Color(103, 103, 103));
        downloadRule.setForeground(new java.awt.Color(255, 255, 255));
        downloadRule.addActionListener(this);
        uploadRule = new Button("Upload Rule .");
        jPanelBtn.add(uploadRule);
        uploadRule.setBackground(new java.awt.Color(103, 103, 103));
        uploadRule.setForeground(new java.awt.Color(255, 255, 255));
        uploadRule.addActionListener(this);
        reload = new Button("   Reload     .");
        reload.setBackground(new java.awt.Color(103, 103, 103));
        reload.setForeground(new java.awt.Color(255, 255, 255));
        reload.addActionListener(this);
        run = new Button("  Run      .");
        //jPanelBtn.add(run);
        run.setBackground(new java.awt.Color(103, 103, 103));
        run.setForeground(new java.awt.Color(255, 255, 255));
        run.setEnabled(false);
        run.addActionListener(this);
        filePath = new TextField();
        fileUpload.add(filePath, BorderLayout.CENTER);
        JPopupMenu menu = new JPopupMenu();
        Action cut = new DefaultEditorKit.CutAction();
        cut.putValue(Action.NAME, "Cut");
        cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        menu.add(cut);
        Action copy = new DefaultEditorKit.CopyAction();
        copy.putValue(Action.NAME, "Copy");
        copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        menu.add(copy);
        Action paste = new DefaultEditorKit.PasteAction();
        paste.putValue(Action.NAME, "Paste");
        paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        menu.add(paste);
        Action selectAll = new SelectAll();
        menu.add(selectAll);
        filePath.setComponentPopupMenu(menu);
        JPanel jPanelFileUpload = new JPanel(new BorderLayout());
        uploadButton = new Button(" Select ");
        uploadButton.setBackground(new java.awt.Color(103, 103, 103));
        uploadButton.setForeground(new java.awt.Color(255, 255, 255));
        uploadButton.addActionListener(this);
        jPanelFileUpload.add(uploadButton, BorderLayout.WEST);
        jPanelFileUpload.setBackground(themeColor);
        JPanel radioBtnLoadPanel = new JPanel(new BorderLayout());
        radioBtnLoadPanel.setBackground(themeColor);
        jPanelBtn.add(radioBtnLoadPanel);
        loadData = new Button(" Load   .");
        loadData.setBackground(new java.awt.Color(103, 103, 103));
        loadData.setForeground(new java.awt.Color(255, 255, 255));
        loadData.setEnabled(false);
        loadData.addActionListener(this);
        indexOfHead = new TextField();
        indexOfHead.setHorizontalAlignment(JTextField.CENTER);
        JRadioButton row = new JRadioButton("Row?");
        JRadioButton column = new JRadioButton("Column?");
        row.setSelected(true);
        row.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (row.isSelected()) {
                    headerDirection = "Row";
                }
            }
        });
        column.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (column.isSelected()) {
                    headerDirection = "Column";
                }
            }
        });
        ButtonGroup bg = new ButtonGroup();
        bg.add(row);
        bg.add(column);
        JPanel bgPanel = new JPanel();
        GridLayout gridLayoutRadio = new GridLayout(1, 0, 0, 0);
        bgPanel.setLayout(gridLayoutRadio);
        bgPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        bgPanel.add(row);
        bgPanel.add(column);
        bgPanel.add(loadData);
        bgPanel.setBackground(new java.awt.Color(255, 255, 240));
        jPanelBtn.add(bgPanel);
        jPanelBtn.add(run);
        fileUpload.add(jPanelFileUpload, BorderLayout.EAST);
        bottomBtnG.add(jPanelBtn, BorderLayout.CENTER); // buttons
        JPanel RuleTableParent = new JPanel(new BorderLayout());
        leftJPanel.add(RuleTableParent, BorderLayout.CENTER);
        RuleTableParent.setBackground(themeColor);
        JPanel ruleListParent = new JPanel(new BorderLayout());
        JPanel tableParent = new JPanel(new BorderLayout());
        tableParent.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.red));
        RuleTableParent.add(ruleListParent, BorderLayout.PAGE_START);
        RuleTableParent.add(tableParent, BorderLayout.CENTER);
        ruleListParent.setBackground(themeColor);
        tableParent.setBackground(themeColor);
        // list the rules ruleListParent
        JPanel listDes = new JPanel(new BorderLayout());
        JPanel listDes2 = new JPanel(new BorderLayout());
        JTextArea desLable2 = new JTextArea("");
        desLable2.setEnabled(false);
        listDes2.add(desLable2);
        desLable2.setBackground(new java.awt.Color(255, 255, 240));
        JPanel listLayout = new JPanel(new BorderLayout());
        ruleListParent.add(listDes, BorderLayout.PAGE_START);
        ruleListParent.add(listDes2, BorderLayout.PAGE_END);
        ruleListParent.add(listLayout, BorderLayout.CENTER);
        JTextArea desLable = new JTextArea("");
        listDes.add(desLable, BorderLayout.CENTER);
        desLable.setEnabled(false);
        desLable.setBackground(new java.awt.Color(255, 255, 240));

        listRules.setCellRenderer(new CheckListRenderer());
        listRules.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listRules.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {

                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                // clicked
                CheckListItem item = (CheckListItem) list.getModel()
                        .getElementAt(index);
                item.setSelected(!item.isSelected()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index));// Repaint cell
                if (item.isSelected()) {
                    selectedRules.put(index, item.toString());
                } else {
                    selectedRules.remove(index);
                }
                if (selectedRules.size() > 0) {
                    run.setEnabled(true);
                } else {
                    run.setEnabled(false);
                }
            }
        });
        listLayout.add(listRules, BorderLayout.CENTER);
        listLayout.setBackground(new java.awt.Color(255, 255, 240));

        // Table 1 ---------
        JPanel rule1Panel = new JPanel(new BorderLayout());
        rule1Panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rule1Panel.setBackground(new Color(255, 255, 240));
        JPanel rule1HeaderPanel = new JPanel(new BorderLayout());
        JPanel rule1TablePanel = new JPanel(new BorderLayout());
        rule1Panel.add(rule1HeaderPanel, BorderLayout.PAGE_START);
        rule1Panel.add(rule1TablePanel, BorderLayout.CENTER);
        JPanel rule1HeaderDesPanel = new JPanel(new BorderLayout());
        rule1HeaderDesPanel.add(new JLabel("  Find empty Cells"), BorderLayout.CENTER);
        rule1HeaderPanel.add(rule1HeaderDesPanel, BorderLayout.CENTER);
        rule1HeaderDesPanel.setBackground(new Color(255, 255, 240));
        rule1HeaderPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(103, 103, 103)));
        JPanel rule1HeaderBtnPanel = new JPanel(new FlowLayout());
        rule1HeaderBtnPanel.setBackground(new Color(255, 255, 240));
        add1 = new Button("Add");
        add1.setBackground(new java.awt.Color(103, 103, 103));
        add1.setForeground(new java.awt.Color(255, 255, 255));
        rule1HeaderBtnPanel.add(add1);
        add1.addActionListener(this);
        edit1 = new Button("Edit");
        edit1.setBackground(new java.awt.Color(103, 103, 103));
        edit1.setForeground(new java.awt.Color(255, 255, 255));
        rule1HeaderBtnPanel.add(edit1);
        edit1.addActionListener(this);
        remove1 = new Button("Delete");
        remove1.setBackground(new java.awt.Color(103, 103, 103));
        remove1.setForeground(new java.awt.Color(255, 255, 255));
        rule1HeaderBtnPanel.add(remove1);
        remove1.addActionListener(this);
        rule1HeaderPanel.add(rule1HeaderBtnPanel, BorderLayout.EAST); // btns
        // Table creation starts - rule1TablePanel
        rule1TablePanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(103, 103, 103)));
        JScrollPane scrollPane = new JScrollPane(table);
        rule1TablePanel.add(scrollPane);
        materialTabbed1.addTab("Rule 1 ", rule1Panel);
        materialTabbed1.setBackgroundAt(0, themeColor);
        // Table 2 ---------
        JPanel rule2Panel = new JPanel(new BorderLayout());
        rule2Panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rule2Panel.setBackground(new Color(255, 255, 240));
        JPanel rule2HeaderPanel = new JPanel(new BorderLayout());
        JPanel rule2TablePanel = new JPanel(new BorderLayout());
        rule2Panel.add(rule2HeaderPanel, BorderLayout.PAGE_START);
        rule2Panel.add(rule2TablePanel, BorderLayout.CENTER);
        JPanel rule2HeaderDesPanel = new JPanel(new BorderLayout());
        rule2HeaderDesPanel.setBackground(new Color(255, 255, 240));
        rule2HeaderDesPanel.add(new JLabel("  Verify and validate cell Data format"), BorderLayout.CENTER);
        rule2HeaderPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(103, 103, 103)));
        rule2HeaderPanel.add(rule2HeaderDesPanel, BorderLayout.CENTER);
        JPanel rule2HeaderBtnPanel = new JPanel(new FlowLayout());
        rule2HeaderBtnPanel.setBackground(new Color(255, 255, 240));
        add2 = new Button("Add");
        add2.setBackground(new java.awt.Color(103, 103, 103));
        add2.setForeground(new java.awt.Color(255, 255, 255));
        rule2HeaderBtnPanel.add(add2);
        add2.addActionListener(this);
        edit2 = new Button("Edit");
        edit2.setBackground(new java.awt.Color(103, 103, 103));
        edit2.setForeground(new java.awt.Color(255, 255, 255));
        rule2HeaderBtnPanel.add(edit2);
        edit2.addActionListener(this);
        remove2 = new Button("Delete");
        remove2.setBackground(new java.awt.Color(103, 103, 103));
        remove2.setForeground(new java.awt.Color(255, 255, 255));
        rule2HeaderBtnPanel.add(remove2);
        remove2.addActionListener(this);
        rule2HeaderPanel.add(rule2HeaderBtnPanel, BorderLayout.EAST);
        rule2TablePanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(103, 103, 103)));
        // Table creation starts - rule2TablePanel
        rule2TablePanel.add(new JScrollPane(table2));
        materialTabbed1.addTab("Rule 2 ", rule2Panel);

        // size of tab pane
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(tableParent);
        tableParent.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );

        // -------------------------------------
        JPanel mainSplitPane = new JPanel();
        GridLayout gridMainSplit = new GridLayout(1, 0, 0, 0);
        mainSplitPane.setLayout(gridMainSplit);
        mainSplitPane.setBorder(new EmptyBorder(0, 5, 5, 5));
        mainSplitPane.add(leftJPanel);
        mainSplitPane.add(rightJpanel);
        add(mainSplitPane, BorderLayout.CENTER);

    }

    public TitledBorder createTitleBorder(String title) {
        return BorderFactory.createTitledBorder(BorderFactory
                .createLineBorder(new java.awt.Color(103, 103, 103)), title);
    }

    static class SelectAll extends TextAction {
        public SelectAll() {
            super("Select All");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent component = getFocusedComponent();
            component.selectAll();
            component.requestFocusInWindow();
        }
    }

    public void setWarningAlert(String msg) {
        JOptionPane optionPane = new JOptionPane(msg, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    public void setInfoAlert(String msg) {
        JOptionPane optionPane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Info!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() throws IOException, ParseException {
        //Create and set up the window.
        JFrame frame = new JFrame(" Excel Validator ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getRootPane().putClientProperty("JRootPane.titleBarBackground", new Color(23,180,252));
        //frame.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.YELLOW);
        // frame.setBackground(new Color(255,25,255));
        //Add content to the window.
        frame.add(new App());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        if (SystemInfo.isMacOS) {
            // enable screen menu bar
            // (moves menu bar from JFrame window to top of screen)
            System.setProperty("apple.laf.useScreenMenuBar", "true");

            // application name used in screen menu bar
            // (in first menu after the "apple" menu)
            System.setProperty("apple.awt.application.name", "My Application");

            // appearance of window title bars
            // possible values:
            //   - "system": use current macOS appearance (light or dark)
            //   - "NSAppearanceNameAqua": use light appearance
            //   - "NSAppearanceNameDarkAqua": use dark appearance
            // (must be set on main thread and before AWT/Swing is initialized;
            //  setting it on AWT thread does not work)
            System.setProperty("apple.awt.application.appearance", "system");
        }
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                FlatLightLaf.setup();

                try {
                    createAndShowGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == uploadButton) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            //Set up the file chooser.
            if (fc == null) {

                fc = new JFileChooser();
                //show hidden files if false then make it true to disable
                fc.setFileHidingEnabled(false);
                //Add a custom file filter and disable the default
                //(Accept All) file filter.
                fc.addChoosableFileFilter(new ImageFilter());
                fc.setAcceptAllFileFilterUsed(false);

                //Add custom icons for file types.
                fc.setFileView(new ImageFileView());

                //Add the preview pane.
                fc.setAccessory(new ImagePreview(fc));
            }

            //Show it.
            int returnVal = fc.showDialog(App.this,
                    "Attach");
            File file = fc.getSelectedFile();
            //Process the results.
            output.setText("");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filePath.setText(file.getAbsolutePath());
                output.setText("Uploaded!");
                setInfoAlert("Pick Row? or Column? and click on Load button");
                loadData.setEnabled(true);
            } else {
                output.setText("Attachment cancelled by user.");
            }
            output.setCaretPosition(output.getDocument().getLength());

            //Reset the file chooser for the next time it's shown.
            fc.setSelectedFile(null);

        } else if (e.getSource() == loadData) {

            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            if (filePath.getText().trim().length() > 0) {
                // Read input excel file
                try {
                    inputExcelData = readerEngine.readCompleteExcelUpdated(filePath.getText().trim(), headerDirection);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("inputExcelData  " + inputExcelData);

                if (inputExcelData.size() == 0) {
                    output.setText("");
                    output.setText(readerEngine.getException());
                } else {
                    output.setText("");
                    output.setText("Input data source loaded ");
                }

                String[] COL_NAMES;
                if (headerDirection.equals("Column")) {
                    COL_NAMES = new String[]{"Validate?", "Sheet", "Target Header", "Run[All rows/Custom]", "Column No: From", "Column No: To"};
                    for (int i = 0; i < COL_NAMES.length; i++) {
                        table.getColumnModel().getColumn(i).setHeaderValue(COL_NAMES[i]);
                        table2.getColumnModel().getColumn(i).setHeaderValue(COL_NAMES[i]);
                    }
                } else {
                    COL_NAMES = new String[]{"Validate?", "Sheet", "Target Header", "Run[All rows/Custom]", "Row No: From", "Row No: To"};
                    for (int i = 0; i < COL_NAMES.length; i++) {
                        table.getColumnModel().getColumn(i).setHeaderValue(COL_NAMES[i]);
                        table2.getColumnModel().getColumn(i).setHeaderValue(COL_NAMES[i]);
                    }
                }
                table.getTableHeader().repaint();
                if (headerDirection.equals("Column")) {
                    COL_NAMES = new String[]{"Validate?", "Sheet", "Target Header", "Format", "Run[All rows/Custom]", "Column No: From", "Column No: To",};
                    ;
                    for (int i = 0; i < COL_NAMES.length; i++) {
                        table2.getColumnModel().getColumn(i).setHeaderValue(COL_NAMES[i]);
                    }
                } else {
                    COL_NAMES = new String[]{"Validate?", "Sheet", "Target Header", "Format", "Run[All rows/Custom]", "Row No: From", "Row No: To",};
                    for (int i = 0; i < COL_NAMES.length; i++) {
                        table2.getColumnModel().getColumn(i).setHeaderValue(COL_NAMES[i]);
                    }
                }
                table2.getTableHeader().repaint();

            } else {
                setWarningAlert("Please Upload Input excel file and click on Load button");
            }

            setInfoAlert("Now choose from available Rules(org.main.checkBox) and start adding entries in Tables");


        }
        // reload button
        else if (e.getSource() == reload) {

            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            if (inputExcelData != null) {

                // Read input excel file
                try {
                    inputExcelData = readerEngine.readCompleteExcelUpdated(filePath.getText().trim(), headerDirection);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("inputExcelData reloaded " + inputExcelData);

                if (inputExcelData.size() == 0) {
                    output.setText("");
                    output.setText(readerEngine.getException());
                } else {
                    output.setText("");
                    output.setText("Input data source reloaded ");
                }

            } else {
                setWarningAlert("Please Upload Input excel file.");
            }


        } else if (e.getSource() == remove1) {

            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            if (inputExcelData != null) {
                int[] rows = table.getSelectedRows();
                if (rows.length > 0) {
                    Rule1TableModel tm = (Rule1TableModel) table.getModel();
                    for (int i = rows.length - 1; i >= 0; i--) {
                        tm.deleteRow(rows[i]);
                    }
                } else {
                    setInfoAlert("Please select Row to Remove.");
                }
            } else {
                setWarningAlert("Please Upload Input excel file.");
            }


        } else if (e.getSource() == remove2) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            if (inputExcelData != null) {
                int[] rows = table2.getSelectedRows();
                if (rows.length > 0) {
                    Rule2TableModel tm = (Rule2TableModel) table2.getModel();
                    for (int i = rows.length - 1; i >= 0; i--) {
                        tm.deleteRow(rows[i]);
                    }
                } else {
                    setInfoAlert("Please select Row to Remove.");
                }
            } else {
                setWarningAlert("Please Upload Input excel file.");
            }

        } else if (e.getSource() == edit1) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            if (inputExcelData != null && inputExcelData.size() != 0) {

                Rule1FieldsWindow newRowPanel = new Rule1FieldsWindow(headerDirection);

                int row = table.getSelectedRow();

                if (row == -1) {

                    setInfoAlert("Please select Row to Edit.");

                } else {
                    Rule1TableModel tm = (Rule1TableModel) table.getModel();
                    Rule1Model model = tm.getTableRow(row);
                    // push selected row into newRowPanel
                    newRowPanel.pushDataIntoForm(model, inputExcelData);

                    int reply = JOptionPane.showConfirmDialog(table,
                            newRowPanel.getMainPanel(),
                            "Edit Rule 1",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);

                    if (reply == JOptionPane.OK_OPTION) {
                        Rule1Model item = newRowPanel.getSelectedItem(); // edited data
                        tableModel.updateTableRow(item, row);
                    }

                }


            } else {
                setWarningAlert("Please Upload Input excel file.");
            }

        } else if (e.getSource() == edit2) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            if (inputExcelData != null && inputExcelData.size() != 0) {

                Rule2FieldsWindow newRowPanel = new Rule2FieldsWindow(headerDirection);
                int row = table2.getSelectedRow();
                if (row == -1) {

                    setInfoAlert("Please select Row to Edit.");

                } else {
                    Rule2TableModel tm = (Rule2TableModel) table2.getModel();
                    Rule2Model model = tm.getTableRow(row);
                    // push selected row into newRowPanel
                    newRowPanel.pushDataIntoForm(model, inputExcelData);

                    int reply = JOptionPane.showConfirmDialog(table2,
                            newRowPanel.getMainPanel(),
                            "Edit Rule 1",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);

                    if (reply == JOptionPane.OK_OPTION) {

                        Rule2Model item = newRowPanel.getSelectedItem(); // edited data
                        tableModel2.updateTableRow(item, row);
                    }
                }

            } else {
                setWarningAlert("Please Upload Input excel file.");
            }

        } else if (e.getSource() == downloadRule) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            boolean run = false;

            if (tableModel.getRule1ModelArrayList().size() > 0) {
                run = true;
            } else if (tableModel2.getRule2ModelArrayList().size() > 0) {
                run = true;
            } else {
                setWarningAlert("Please add at least one rule");
            }

            if (run) {

                //Set up the file chooser.
                if (fcLoadRule == null) {
                    fcLoadRule = new JFileChooser();
                    //show hidden files if false then make it true to disable
                    fcLoadRule.setFileHidingEnabled(false);
                    //Add a custom file filter and disable the default
                    //(Accept All) file filter.
                    fcLoadRule.addChoosableFileFilter(new RuleFilter());
                    fcLoadRule.setAcceptAllFileFilterUsed(false);
                    //Add custom icons for file types.
                    fcLoadRule.setFileView(new RuleFileView());
                    //Add the preview pane.
                    fcLoadRule.setAccessory(new RulePreview(fcLoadRule));
                }
                //Show it.
                int returnVal = fcLoadRule.showDialog(App.this,
                        "Attach");
                //Process the results.
                output.setText("");
                File file = fcLoadRule.getSelectedFile();
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    output.setText(file.getAbsolutePath());
                } else {
                    output.setText("Attachment cancelled by user.");
                }
                output.setCaretPosition(output.getDocument().getLength());
                //Reset the file chooser for the next time it's shown.
                fcLoadRule.setSelectedFile(null);

                String path = CreateFile(file.getAbsolutePath() + ".rule");

                try {
                    FileWriter writer = new FileWriter(path);
                    // rule 1
                    int size = tableModel.getRule1ModelArrayList().size();
                    for (int i = 0; i < size; i++) {
                        String str = tableModel.getRule1ModelArrayList().get(i).toString();
                        writer.write(str);
                        if (i < size - 1)
                            writer.write("\n");
                    }

                    // rule 2
                    writer.write("\n");
                    int size2 = tableModel2.getRule2ModelArrayList().size();
                    for (int i = 0; i < size2; i++) {
                        String str = tableModel2.getRule2ModelArrayList().get(i).toString();
                        writer.write(str);
                        if (i < size - 1)
                            writer.write("\n");
                    }

                    writer.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                output.setText("");
                output.setText("Rules file downloaded at: " + path);
                setInfoAlert("Rules file downloaded at: " + path);

            }
        } else if (e.getSource() == download) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            String data = output.getText();

            //Set up the file chooser.
            if (fcDownloadOutput == null) {
                fcDownloadOutput = new JFileChooser();
                //show hidden files if false then make it true to disable
                fcDownloadOutput.setFileHidingEnabled(false);
                //Add a custom file filter and disable the default
                //(Accept All) file filter.
                fcDownloadOutput.addChoosableFileFilter(new TextFilter());
                fcDownloadOutput.setAcceptAllFileFilterUsed(false);
                //Add custom icons for file types.
                fcDownloadOutput.setFileView(new TextFileView());
                //Add the preview pane.
                fcDownloadOutput.setAccessory(new TextPreview(fcDownloadOutput));
            }
            //Show it.
            int returnVal = fcDownloadOutput.showDialog(App.this,
                    "Attach");
            //Process the results.
            File file = fcDownloadOutput.getSelectedFile();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                //output.setText(file.getAbsolutePath());
            } else {
                //output.setText("Attachment cancelled by user.");
            }
            output.setCaretPosition(output.getDocument().getLength());
            //Reset the file chooser for the next time it's shown.
            fcDownloadOutput.setSelectedFile(null);

            String path = CreateFile(file.getAbsolutePath() + ".txt");

            try {
                FileWriter writer = new FileWriter(path);

                int size = data.split("\n").length;

                for (int i = 0; i < size; i++) {

                    String str = data.split("\n")[i];

                    writer.write(str);
                    if (i < size - 1)
                        writer.write("\n");
                }


                writer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            setInfoAlert(" file downloaded at: " + path);


        } else if (e.getSource() == uploadRule) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            //Set up the file chooser.
            if (fcLoadRule == null) {
                fcLoadRule = new JFileChooser();
                //show hidden files if false then make it true to disable
                fcLoadRule.setFileHidingEnabled(false);
                //Add a custom file filter and disable the default
                //(Accept All) file filter.
                fcLoadRule.addChoosableFileFilter(new RuleFilter());
                fcLoadRule.setAcceptAllFileFilterUsed(false);
                //Add custom icons for file types.
                fcLoadRule.setFileView(new RuleFileView());
                //Add the preview pane.
                fcLoadRule.setAccessory(new RulePreview(fcLoadRule));
            }

            //Show it.
            int returnVal = fcLoadRule.showDialog(App.this,
                    "Attach");

            //Process the results.
            output.setText("");
            File file = fcLoadRule.getSelectedFile();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                output.setText(file.getAbsolutePath());
            } else {
                output.setText("Attachment cancelled by user.");
            }
            output.setCaretPosition(output.getDocument().getLength());

            //Reset the file chooser for the next time it's shown.
            fcLoadRule.setSelectedFile(null);

            // Rule1 file upload
            List<Rule1Model> rule1ModelArrayList = new ArrayList<>();
            String fileName;
            FileReader fileReader = null;
            try {
                fileName = file.getAbsolutePath();
                fileReader = new FileReader(fileName);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    if (line.contains("Rule1Row")) {

                        String[] modelArr = line.split("::");
                        if (modelArr.length > 5) {
                            rule1ModelArrayList.add(new Rule1Model(modelArr[1], modelArr[2], modelArr[3], modelArr[4], modelArr[5], modelArr[6]));
                        } else {
                            rule1ModelArrayList.add(new Rule1Model(modelArr[1], modelArr[2], modelArr[3], modelArr[4]));
                        }

                    }

                }
            } catch (IOException ex) {
            }
            tableModel.loadTableRows(rule1ModelArrayList);
// Rule2 file upload

            // Rule1 file upload
            List<Rule2Model> rule2ModelArrayList = new ArrayList<>();
            String fileName2;
            FileReader fileReader2 = null;
            try {
                fileName2 = file.getAbsolutePath();
                fileReader2 = new FileReader(fileName2);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            try (BufferedReader bufferedReader = new BufferedReader(fileReader2)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    if (line.contains("Rule2Row")) {
                        String[] modelArr = line.split("::");
                        if (modelArr.length > 6) {
                            rule2ModelArrayList.add(new Rule2Model(modelArr[1], modelArr[2], modelArr[3], modelArr[4], modelArr[5], modelArr[6], modelArr[7]));
                        } else {
                            rule2ModelArrayList.add(new Rule2Model(modelArr[1], modelArr[2], modelArr[3], modelArr[4], modelArr[5]));
                        }
                    }

                }
            } catch (IOException ex) {
            }
            tableModel2.loadTableRows(rule2ModelArrayList);

        } else if (e.getSource() == run) {
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            String[] rulesArr = new String[10];
            for (String value : selectedRules.values()) {
                if (value.contains("Rule 1")) {
                    rulesArr[0] = "R1";
                } else if (value.contains("Rule 2")) {
                    rulesArr[1] = "R2";
                }
            }

            boolean run = false;
            if (selectedRules.size() > 0) {
                if (tableModel.getRule1ModelArrayList().size() > 0) {
                    run = true;
                } else if (tableModel2.getRule2ModelArrayList().size() > 0) {
                    run = true;
                } else {
                    setWarningAlert("Please add at least one rule in Table");
                }
            } else {
                run = false;
                setWarningAlert("Please select at lease one Rule Checkbox to Run!");
            }

            if (run) {

                List<Object> masterList = new ArrayList<>();
                masterList.add(0, tableModel.getRule1ModelArrayList());
                masterList.add(1, tableModel2.getRule2ModelArrayList());

                int noOfThreads = rulesArr.length;
                final App runner;
                try {
                    runner = new App();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);
                Set<Callable<String>> callables = new HashSet<>();

                for (int i = 0; i < noOfThreads; i++) {
                    int finalI = i;

                    callables.add(new Callable<String>() {
                        public String call() {
                            // System.out.println(" Thread name: " + Thread.currentThread().getName());
                            return runner.executeRule(rulesArr[finalI], inputExcelData, masterList);
                        }
                    });
                }

                List<Future<String>> futures = null;
                try {
                    futures = executorService.invokeAll(callables);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                List<String> reportList = new ArrayList<>();

                for (Future<String> future : futures) {
                    //System.out.println("future.get = " + future.isDone());
                    try {
                        // this is to return anything after that particular thread completed.
                        // In our case, we are returning errors list,  just print them on the console.
                        if (future.get().contains("&")) {

                            String[] infoArr = future.get().split("&");
                            for (String arr : infoArr) {
                                String[] item = arr.split(",");
                                if (headerDirection.equals("Column")) {
                                    reportList.add("For " + item[0] + ",in sheet " + item[1] + " , Column No:" + item[2] + " for Header  " + item[3] + " >>> INFO: " + item[4]);
                                } else {
                                    reportList.add("For " + item[0] + ",in sheet " + item[1] + " , Row No:" + item[2] + " for Header  " + item[3] + " >>> INFO: " + item[4]);
                                }
                            }
                        } else {
                            reportList.add(future.get().toString());

                        }

                    } catch (ExecutionException e1) {
                        // this is best place to see program failure reason, why?
                        e1.printStackTrace();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                output.setText(join(reportList, "\n"));
                executorService.shutdown();

                setInfoAlert("Rules validation completed!");

            } else {


            }


        } else if (e.getSource() == add1) {

            Rule1FieldsWindow newRowPanel = new Rule1FieldsWindow(headerDirection);

            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            if (inputExcelData != null && inputExcelData.size() != 0) {
                int reply = JOptionPane.showConfirmDialog(table,
                        newRowPanel.getMainPanel(inputExcelData),
                        "Rule 1 fields ",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (reply == JOptionPane.OK_OPTION) {
                    Rule1Model item = newRowPanel.getSelectedItem();
                    tableModel.addRow(item);
                }
            } else {
                setWarningAlert("Please Upload Input excel file and click on Load button");
            }


        } else if (e.getSource() == add2) {

            Rule2FieldsWindow newRowPanel = new Rule2FieldsWindow(headerDirection);
            try {
                SoundUtils.tone(hz, msec, vol);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            if (inputExcelData != null && inputExcelData.size() != 0) {
                int reply = JOptionPane.showConfirmDialog(table2,
                        newRowPanel.getMainPanel(inputExcelData),
                        "Rule 2 fields ",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (reply == JOptionPane.OK_OPTION) {
                    Rule2Model item = newRowPanel.getSelectedItem();
                    tableModel2.addRow(item);
                }
            } else {
                setWarningAlert("Please Upload Input excel file and click on Load button");
            }

        } else if (e.getSource() == changeFont) {
            String fontNameSet;
            String fontSizeSetTemp;
            int fontSizeSet;
            fontNameSet = (String) fontName.getSelectedItem();
            fontSizeSetTemp = (String) fontSize.getSelectedItem();
            fontSizeSet = Integer.parseInt(fontSizeSetTemp);
            output.setFont(new Font(fontNameSet, Font.PLAIN, fontSizeSet));
        }

    }

    public static String join(List<String> list, String delim) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for (String s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }

        return sb.toString();
    }


    public String executeRule(String value, Map<String, Map<String, Map<String, String>>> inputExcelData, List<Object> masterList) {

        String getErrorListSTR = "";

        switch (value) {

            case "R1":
                Rule1ValidatorEngine rule1ValidatorEngine = new Rule1ValidatorEngine();
                rule1ValidatorEngine.validateRule4(inputExcelData, (List<Rule1Model>) masterList.get(0));
                if (rule1ValidatorEngine.getRuleListSize() > 0) {
                    getErrorListSTR = rule1ValidatorEngine.getErrorsList();
                } else {
                    getErrorListSTR = "";
                }
                break;
            case "R2":
                Rule2ValidatorEngine rule2ValidatorEngine = new Rule2ValidatorEngine();
                rule2ValidatorEngine.validateRule2(inputExcelData, (List<Rule2Model>) masterList.get(1));
                if (rule2ValidatorEngine.getRuleListSize() > 0) {
                    getErrorListSTR = rule2ValidatorEngine.getErrorsList();
                } else {
                    getErrorListSTR = "";
                }
                break;

            default:
                System.out.println("input Rule not present to proceed validation. please input available Rules ");

        }

        return getErrorListSTR;

    }
    void addCompForBorder(Border border,
                          String description,
                          Container container) {
        JPanel comp = new JPanel(new GridLayout(1, 1), false);
        JLabel label = new JLabel(description, JLabel.CENTER);
        comp.add(label);
        comp.setBorder(border);

        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(comp);
    }

    public String CreateFile(String fileName) {
        File myObj = new File(fileName);
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return myObj.getAbsolutePath();
    }

}
