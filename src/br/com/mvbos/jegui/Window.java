/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui;

import br.com.mvbos.jegui.external.FileUtil;
import br.com.mvbos.jegui.ui.table.PropertyElementTable;
import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.element.SelectorElement;
import br.com.mvbos.jeg.engine.GraphicTool;
import br.com.mvbos.jeg.scene.IScene;
import br.com.mvbos.jegui.ui.tree.ElementMutableTreeNode;
import br.com.mvbos.jeg.window.Camera;
import br.com.mvbos.jeg.window.IMemory;
import br.com.mvbos.jeg.window.impl.MemoryImpl;
import br.com.mvbos.jegee.ExportElement;
import br.com.mvbos.jegee.Scene;
import br.com.mvbos.jegui.dialogs.DialogNewImageElement;
import br.com.mvbos.jegui.el.ButtonElement;
import br.com.mvbos.jegui.external.JarUtil;
import br.com.mvbos.jegui.prev.PreviewWindow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Marcus Becker
 */
public class Window extends javax.swing.JFrame {

    private static final FileNameExtensionFilter XML_EXTENSION_FILE_FILTER = new FileNameExtensionFilter("XML files", "xml");
    private static final FileNameExtensionFilter PROJECT_EXTENSION_FILE_FILTER = new FileNameExtensionFilter("JEG-UI Projects", "jeg");

    private final Camera cam = Camera.createNew();
    private final PropertyElementTable elementTable = new PropertyElementTable();

    private Project project = new Project("New Project");

    private JPanel canvas;
    private final DefaultMutableTreeNode root;
    private ElementModel selectedElement;

    private IScene scene;
    private final SelectorElement selector = new SelectorElement("selector");

    private final ButtonElement[] camMoveButton = new ButtonElement[4];

    private static final int SCENE_WIDTH = 1200;
    private static final int SCENE_HEIGHT = 899;

    private static final int SCREEN_WIDTH = 991;
    private static final int SCREEN_HEIGHT = 743;

    private int id = 1;
    private Point mousePos = new Point();
    private Point startDrag;

    private final ElementModel[] stageElements = new ElementModel[30];

    private final ElementModel mouseElement = new ElementModel(10, 10, "mouseElement");
    private short menuOldSize = 350;

    private boolean invertColor;
    private File openedFile;

    public Window() {

        selector.setColor(Color.WHITE);

        /*ElementModel temp = new ElementModel(0, 0, "Background");
         temp.setImage(new ImageIcon("images\\bg.png"));
         all.add(temp);

         temp = new ElementModel(15, 15, "Bloco");
         all.add(temp);*/
        //
        //
        initComponents();

        root = new DefaultMutableTreeNode("Memory");

        //
        initMyComponents();

        iniciaAnimacao();

    }

    private void initMyComponents() {
        cam.config(SCENE_WIDTH, SCENE_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        cam.setAllowOffset(true);

        tfSceneWidth.setText(String.valueOf(SCENE_WIDTH));
        tfSceneHeight.setText(String.valueOf(SCENE_HEIGHT));

        tfWindowWidth.setText(String.valueOf(SCREEN_WIDTH));
        tfWindowHeight.setText(String.valueOf(SCREEN_HEIGHT));

        treeStage.removeAll();
        treeStage.setModel(new DefaultTreeModel(root));

        /*tree.setDragEnabled(true);
         tree.setDropMode(DropMode.ON_OR_INSERT);
         tree.setTransferHandler(new TransferHandler(){
            
         });
         tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
         */
        for (String k : project.getSceneElements().keySet()) {
            DefaultMutableTreeNode t = new DefaultMutableTreeNode(k);

            for (ElementModel el : project.getSceneElements().get(k)) {
                t.add(new ElementMutableTreeNode(el));
            }

            root.add(t);
        }

        //root.add(new ElementMutableTreeNode(el));
        btnCanvasColor.setBackground(pnCanvas.getBackground());

        tableLibrary.setModel(new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return project.getLibElements().size();
            }

            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                ElementModel el = project.getLibElements().get(rowIndex);
                return String.format("%d %s", el.getId(), el.getName());
            }

            //
            @Override
            public String getColumnName(int column) {
                return "Elements";
            }

        });

        scene = initScene();

        for (int i = 0; i < camMoveButton.length; i++) {
            camMoveButton[i] = new ButtonElement(20, 20, null);
        }
    }

    private void addElementOnStage(ElementModel el) {

        DefaultMutableTreeNode parentNode;
        TreePath parentPath = treeStage.getSelectionPath();

        if (parentPath == null || parentPath.getLastPathComponent() == root) {
            parentNode = (DefaultMutableTreeNode) root.getFirstChild();
        } else {
            if (parentPath.getLastPathComponent() instanceof ElementMutableTreeNode) {
                parentNode = (DefaultMutableTreeNode) parentPath.getParentPath().getLastPathComponent();
            } else {
                parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            }
        }

        if (parentNode instanceof ElementMutableTreeNode) {
            throw new IllegalArgumentException("Invalid node location");
        }

        System.out.println("eee " + el.getClass().getSimpleName());

        project.getSceneElements().get(parentNode.toString()).add(el);
        parentNode.add(new ElementMutableTreeNode(el));
        treeStage.updateUI();
    }

    private void removeElement(DefaultMutableTreeNode node) {
        if (!(node instanceof ElementMutableTreeNode)) {
            throw new IllegalArgumentException("Invalid node location");
        }

        DefaultTreeModel model = (DefaultTreeModel) treeStage.getModel();
        ElementModel el = (ElementModel) node.getUserObject();
        TreeNode parent = node.getParent();

        project.getSceneElements().get(parent.toString()).remove(el);
        model.removeNodeFromParent(node);
        treeStage.updateUI();

        singleSelection(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fc = new javax.swing.JFileChooser();
        dialodNewElement = new javax.swing.JFrame();
        jLabelID = new javax.swing.JLabel();
        jLabelName = new javax.swing.JLabel();
        jLabelType = new javax.swing.JLabel();
        dlgTfID = new javax.swing.JTextField();
        dlgTfName = new javax.swing.JTextField();
        dlgNewElementCancel = new javax.swing.JButton();
        dlgNewElementOK = new javax.swing.JButton();
        cbNewElementType = new javax.swing.JComboBox();
        pnHead = new javax.swing.JPanel();
        pnEditTools = new javax.swing.JPanel();
        btnEdTLSelect = new javax.swing.JToggleButton();
        btnEdTLHand = new javax.swing.JToggleButton();
        pnBody = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        pnCanvas = createCanvas();
        pnMenu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = elementTable;
        tabLibrary = new javax.swing.JTabbedPane();
        pnLibrary = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableLibrary = new javax.swing.JTable();
        btnAddNewElement = new javax.swing.JButton();
        btnRemoveElement = new javax.swing.JButton();
        btnEditElement = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnTree = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeStage = new javax.swing.JTree();
        bntAddElement = new javax.swing.JButton();
        btnRemoveElementTree = new javax.swing.JButton();
        pnBottom = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        pnScreenCam = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfSceneWidth = new javax.swing.JTextField();
        tfSceneHeight = new javax.swing.JTextField();
        tfCamPx = new javax.swing.JTextField();
        tfCamPy = new javax.swing.JTextField();
        btnCanvasColor = new javax.swing.JButton();
        pnElementPosition = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfElPy = new javax.swing.JTextField();
        tfElPx = new javax.swing.JTextField();
        tfElName = new javax.swing.JTextField();
        lblCanvasInfo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tfWindowWidth = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfWindowHeight = new javax.swing.JTextField();
        btnPreview = new javax.swing.JButton();
        tfCamInitPx = new javax.swing.JTextField();
        tfCamInitPy = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbScenePreview = new javax.swing.JComboBox();
        menuMain = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miOpen = new javax.swing.JMenuItem();
        mnSave = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();
        miData = new javax.swing.JMenu();
        miExpXml = new javax.swing.JMenuItem();

        dialodNewElement.setTitle("Add new Element");

        jLabelID.setText("ID:");

        jLabelName.setText("Name:");

        jLabelType.setText("Type:");

        dlgTfID.setText("1");

        dlgTfName.setText("Element");

        dlgNewElementCancel.setText("Cancel");
        dlgNewElementCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgNewElementCancelActionPerformed(evt);
            }
        });

        dlgNewElementOK.setText("OK");
        dlgNewElementOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgNewElementOKActionPerformed(evt);
            }
        });

        cbNewElementType.setModel(new DefaultComboBoxModel(Project.elements.toArray()));
        cbNewElementType.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String item = ((Class<? extends ElementModel>)value).getSimpleName();

                return super.getListCellRendererComponent(list, item, index, isSelected, cellHasFocus);
            }
        });

        javax.swing.GroupLayout dialodNewElementLayout = new javax.swing.GroupLayout(dialodNewElement.getContentPane());
        dialodNewElement.getContentPane().setLayout(dialodNewElementLayout);
        dialodNewElementLayout.setHorizontalGroup(
            dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialodNewElementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialodNewElementLayout.createSequentialGroup()
                        .addComponent(jLabelID, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgTfID))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialodNewElementLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(dlgNewElementOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgNewElementCancel))
                    .addGroup(dialodNewElementLayout.createSequentialGroup()
                        .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelType, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbNewElementType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dlgTfName, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))))
                .addContainerGap())
        );
        dialodNewElementLayout.setVerticalGroup(
            dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialodNewElementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelID)
                    .addComponent(dlgTfID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelName)
                    .addComponent(dlgTfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNewElementType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelType))
                .addGap(18, 18, 18)
                .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dlgNewElementOK)
                    .addComponent(dlgNewElementCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        pnHead.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnEdTLSelect.setSelected(true);
        btnEdTLSelect.setText("S");
        btnEdTLSelect.setToolTipText("Selector");
        btnEdTLSelect.setName("SELECTOR"); // NOI18N
        btnEdTLSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdTLSelectActionPerformed(evt);
            }
        });

        btnEdTLHand.setText("H");
        btnEdTLHand.setToolTipText("Hand");
        btnEdTLHand.setName("HAND"); // NOI18N
        btnEdTLHand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdTLHandActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnEditToolsLayout = new javax.swing.GroupLayout(pnEditTools);
        pnEditTools.setLayout(pnEditToolsLayout);
        pnEditToolsLayout.setHorizontalGroup(
            pnEditToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEditToolsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEdTLSelect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdTLHand)
                .addContainerGap(75, Short.MAX_VALUE))
        );
        pnEditToolsLayout.setVerticalGroup(
            pnEditToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEditToolsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnEditToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdTLSelect)
                    .addComponent(btnEdTLHand))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnHeadLayout = new javax.swing.GroupLayout(pnHead);
        pnHead.setLayout(pnHeadLayout);
        pnHeadLayout.setHorizontalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnEditTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnHeadLayout.setVerticalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnEditTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPane.setDividerLocation(700);
        splitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splitPanePropertyChange(evt);
            }
        });

        pnCanvas.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout pnCanvasLayout = new javax.swing.GroupLayout(pnCanvas);
        pnCanvas.setLayout(pnCanvasLayout);
        pnCanvasLayout.setHorizontalGroup(
            pnCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 699, Short.MAX_VALUE)
        );
        pnCanvasLayout.setVerticalGroup(
            pnCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 765, Short.MAX_VALUE)
        );

        splitPane.setLeftComponent(pnCanvas);

        table.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setViewportView(table);

        pnLibrary.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tableLibrary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableLibrary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableLibraryMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableLibraryMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableLibraryMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tableLibrary);

        btnAddNewElement.setText("+");
        btnAddNewElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewElementActionPerformed(evt);
            }
        });

        btnRemoveElement.setText("-");

        btnEditElement.setText("*");
        btnEditElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditElementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnLibraryLayout = new javax.swing.GroupLayout(pnLibrary);
        pnLibrary.setLayout(pnLibraryLayout);
        pnLibraryLayout.setHorizontalGroup(
            pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(pnLibraryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddNewElement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveElement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditElement)
                .addContainerGap(183, Short.MAX_VALUE))
        );
        pnLibraryLayout.setVerticalGroup(
            pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLibraryLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddNewElement)
                    .addComponent(btnRemoveElement)
                    .addComponent(btnEditElement))
                .addContainerGap())
        );

        tabLibrary.addTab("Library", pnLibrary);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)
        );

        tabLibrary.addTab("Config", jPanel2);

        pnTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        treeStage.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeStageValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(treeStage);

        bntAddElement.setText("+");

        btnRemoveElementTree.setText("-");
        btnRemoveElementTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveElementTreeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTreeLayout = new javax.swing.GroupLayout(pnTree);
        pnTree.setLayout(pnTreeLayout);
        pnTreeLayout.setHorizontalGroup(
            pnTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(pnTreeLayout.createSequentialGroup()
                .addComponent(bntAddElement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveElementTree)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnTreeLayout.setVerticalGroup(
            pnTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTreeLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bntAddElement)
                    .addComponent(btnRemoveElementTree)))
        );

        javax.swing.GroupLayout pnMenuLayout = new javax.swing.GroupLayout(pnMenu);
        pnMenu.setLayout(pnMenuLayout);
        pnMenuLayout.setHorizontalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMenuLayout.createSequentialGroup()
                .addComponent(tabLibrary)
                .addGap(1, 1, 1))
            .addComponent(pnTree, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        pnMenuLayout.setVerticalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addComponent(tabLibrary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnTree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        splitPane.setRightComponent(pnMenu);

        javax.swing.GroupLayout pnBodyLayout = new javax.swing.GroupLayout(pnBody);
        pnBody.setLayout(pnBodyLayout);
        pnBodyLayout.setHorizontalGroup(
            pnBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane)
        );
        pnBodyLayout.setVerticalGroup(
            pnBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane)
        );

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pnScreenCam.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("x");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(",");

        tfSceneWidth.setText("1200");

        tfSceneHeight.setText("899");

        tfCamPx.setText("0");
        tfCamPx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfCamPxKeyReleased(evt);
            }
        });

        tfCamPy.setText("0");
        tfCamPy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfCamPyKeyReleased(evt);
            }
        });

        btnCanvasColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanvasColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnScreenCamLayout = new javax.swing.GroupLayout(pnScreenCam);
        pnScreenCam.setLayout(pnScreenCamLayout);
        pnScreenCamLayout.setHorizontalGroup(
            pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnScreenCamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tfCamPx, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfSceneWidth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfSceneHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(tfCamPy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCanvasColor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnScreenCamLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel3, jLabel4});

        pnScreenCamLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfCamPx, tfCamPy, tfSceneHeight, tfSceneWidth});

        pnScreenCamLayout.setVerticalGroup(
            pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnScreenCamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfSceneWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfSceneHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnScreenCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(tfCamPx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfCamPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCanvasColor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnScreenCamLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfCamPx, tfCamPy, tfSceneHeight, tfSceneWidth});

        pnScreenCamLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jLabel4});

        pnElementPosition.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("x,y");

        tfElPy.setText("0");
        tfElPy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfElPyKeyReleased(evt);
            }
        });

        tfElPx.setText("0");
        tfElPx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfElPxKeyReleased(evt);
            }
        });

        tfElName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfElNameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnElementPositionLayout = new javax.swing.GroupLayout(pnElementPosition);
        pnElementPosition.setLayout(pnElementPositionLayout);
        pnElementPositionLayout.setHorizontalGroup(
            pnElementPositionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnElementPositionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnElementPositionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfElName)
                    .addGroup(pnElementPositionLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfElPx, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfElPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnElementPositionLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfElPx, tfElPy});

        pnElementPositionLayout.setVerticalGroup(
            pnElementPositionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnElementPositionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfElName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnElementPositionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfElPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(tfElPx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        lblCanvasInfo.setText("Cam: 0,0");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tfWindowWidth.setText("800");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("x");

        tfWindowHeight.setText("600");

        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        tfCamInitPx.setText("0");
        tfCamInitPx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfCamInitPxKeyReleased(evt);
            }
        });

        tfCamInitPy.setText("0");
        tfCamInitPy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfCamInitPyKeyReleased(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(",");

        cbScenePreview.setModel(new DefaultComboBoxModel(Project.scenes.toArray()));
        cbScenePreview.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String item = ((Class<? extends IScene>)value).getSimpleName();

                return super.getListCellRendererComponent(list, item, index, isSelected, cellHasFocus);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfWindowWidth, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(tfCamInitPx))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfCamInitPy, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfWindowHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(cbScenePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPreview)))
                .addGap(15, 15, 15))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfCamInitPx, tfCamInitPy, tfWindowHeight, tfWindowWidth});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfWindowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfWindowHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(btnPreview))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfCamInitPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfCamInitPx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbScenePreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfCamInitPx, tfCamInitPy, tfWindowHeight, tfWindowWidth});

        javax.swing.GroupLayout pnBottomLayout = new javax.swing.GroupLayout(pnBottom);
        pnBottom.setLayout(pnBottomLayout);
        pnBottomLayout.setHorizontalGroup(
            pnBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnBottomLayout.createSequentialGroup()
                        .addComponent(pnScreenCam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnElementPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(130, 130, 130))
                    .addGroup(pnBottomLayout.createSequentialGroup()
                        .addComponent(lblCanvasInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnBottomLayout.setVerticalGroup(
            pnBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBottomLayout.createSequentialGroup()
                .addGroup(pnBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnBottomLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton1))
                    .addGroup(pnBottomLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnScreenCam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnElementPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCanvasInfo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnBottomLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel1, pnElementPosition, pnScreenCam});

        jMenu1.setText("File");

        miOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        miOpen.setText("Open");
        miOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenActionPerformed(evt);
            }
        });
        jMenu1.add(miOpen);

        mnSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnSave.setText("Save");
        mnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSaveActionPerformed(evt);
            }
        });
        jMenu1.add(mnSave);

        miExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        jMenu1.add(miExit);

        menuMain.add(jMenu1);

        miData.setText("Data");

        miExpXml.setText("Export XML");
        miExpXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExpXmlActionPerformed(evt);
            }
        });
        miData.add(miExpXml);

        menuMain.add(miData);

        setJMenuBar(menuMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(pnBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        elementTable.printData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void treeStageValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeStageValueChanged

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeStage.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();
        //if (node.isLeaf()) {
        if (nodeInfo instanceof ElementModel) {
            selectElementOnStage((ElementModel) nodeInfo);
        }

    }//GEN-LAST:event_treeStageValueChanged

    private void btnCanvasColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanvasColorActionPerformed
        if (dialog == null) {
            colorChooser = new JColorChooser();
            ActionListener ac = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    pnCanvas.setBackground(colorChooser.getColor());
                    btnCanvasColor.setBackground(pnCanvas.getBackground());

                    Color x = colorChooser.getColor();

                    if (invertColor) {
                        Color c = (x.getRed() + x.getGreen() + x.getBlue()) / 3 < 127 ? Color.WHITE : Color.BLACK;
                        selector.setColor(c);
                    } else {
                        selector.setColor(x);
                    }
                }
            };

            dialog = JColorChooser.createDialog(this, "Choose the color", true, colorChooser, ac, null);
        }

        dialog.setVisible(true);

    }//GEN-LAST:event_btnCanvasColorActionPerformed

    private void tableLibraryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLibraryMousePressed


    }//GEN-LAST:event_tableLibraryMousePressed

    private void tableLibraryMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLibraryMouseReleased

        //selectedElement = null;

    }//GEN-LAST:event_tableLibraryMouseReleased

    private void tableLibraryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLibraryMouseClicked

        int sel = tableLibrary.getSelectedRow();
        if (sel < 0) {
            return;
        }

        selectElementOnLibrary(project.getLibElements().get(sel));

    }//GEN-LAST:event_tableLibraryMouseClicked

    private void btnRemoveElementTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveElementTreeActionPerformed

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeStage.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        if (node instanceof ElementMutableTreeNode) {
            removeElement(node);
        }


    }//GEN-LAST:event_btnRemoveElementTreeActionPerformed

    private void tfElPxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfElPxKeyReleased

        setNewPxy();

    }//GEN-LAST:event_tfElPxKeyReleased

    private void tfElPyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfElPyKeyReleased

        setNewPxy();

    }//GEN-LAST:event_tfElPyKeyReleased

    private void tfElNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfElNameKeyReleased

        setNewPxy();

    }//GEN-LAST:event_tfElNameKeyReleased

    private void tfCamPxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCamPxKeyReleased

        positionCam();

    }//GEN-LAST:event_tfCamPxKeyReleased

    private void tfCamPyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCamPyKeyReleased

        positionCam();

    }//GEN-LAST:event_tfCamPyKeyReleased

    private void mnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSaveActionPerformed

        if (openedFile == null) {

            fc.resetChoosableFileFilters();
            fc.setFileFilter(PROJECT_EXTENSION_FILE_FILTER);

            fc.showSaveDialog(this);
            openedFile = fc.getSelectedFile();
        }

        if (openedFile != null) {
            setTitle(openedFile.getAbsolutePath());
            FileUtil.saveProject(openedFile, project);
        }

    }//GEN-LAST:event_mnSaveActionPerformed


    private void btnEdTLSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdTLSelectActionPerformed

        selEditTool((JToggleButton) evt.getSource());

    }//GEN-LAST:event_btnEdTLSelectActionPerformed

    private void btnEdTLHandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdTLHandActionPerformed

        selEditTool((JToggleButton) evt.getSource());

    }//GEN-LAST:event_btnEdTLHandActionPerformed

    private void btnAddNewElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewElementActionPerformed

        dlgTfID.setText(String.valueOf(project.getLibElements().size() + 1));
        dialodNewElement.pack();
        dialodNewElement.setLocationRelativeTo(this);

        dialodNewElement.setVisible(true);

    }//GEN-LAST:event_btnAddNewElementActionPerformed

    private void btnEditElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditElementActionPerformed

        if (selectedElement != null) {
            openEditElement(selectedElement);
        }

    }//GEN-LAST:event_btnEditElementActionPerformed

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed

        Dimension wDim = new Dimension(Util.getInt(tfSceneWidth), Util.getInt(tfSceneHeight));
        Dimension sDim = new Dimension(Util.getInt(tfWindowWidth), Util.getInt(tfWindowHeight));

        IScene s = Project.getScene(cbScenePreview.getSelectedIndex());

        int i = 0;
        IMemory[] arr = new IMemory[project.getSceneElements().size()];

        for (String k : project.getSceneElements().keySet()) {
            List<ElementModel> l = project.getSceneElements().get(k);
            arr[i] = new MemoryImpl(l.size());

            for (ElementModel e : l) {
                arr[i].registerElement(JarUtil.copy(e));
            }

            i++;
        }

        PreviewWindow prev = new PreviewWindow(s, arr, wDim, sDim);
        prev.setCamPosition(new Point(Util.getInt(tfCamInitPx), Util.getInt(tfCamInitPy)));
        prev.setVisible(true);

    }//GEN-LAST:event_btnPreviewActionPerformed

    private void tfCamInitPxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCamInitPxKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCamInitPxKeyReleased

    private void tfCamInitPyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCamInitPyKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCamInitPyKeyReleased

    private void splitPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_splitPanePropertyChange

        //menuOldSize = (short) splitPane.getBottomComponent().getWidth();
        //System.out.println("menuOldSize " + menuOldSize);

    }//GEN-LAST:event_splitPanePropertyChange

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        splitPane.setDividerLocation(getWidth() - menuOldSize);
        //System.out.println("menuOldSize " + menuOldSize);
        //System.out.println("splitPane.getWidth() " + getWidth());
        //splitPane.setDividerLocation(splitPane.getWidth() - menuOldSize);
        //splitPane.getBottomComponent().setSize(menuOldSize, splitPane.getBottomComponent().getHeight());

    }//GEN-LAST:event_formComponentResized

    private void dlgNewElementOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNewElementOKActionPerformed

        ElementModel el = Project.getElement(cbNewElementType.getSelectedIndex());

        System.out.println("el " + (el.getClass()));

        el.setId(Util.getInt(dlgTfID));
        el.setName(dlgTfName.getText());
        el.setSize(20, 20);

        dialodNewElement.dispose();
        openEditElement(el);


    }//GEN-LAST:event_dlgNewElementOKActionPerformed

    private void dlgNewElementCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNewElementCancelActionPerformed

        dialodNewElement.setVisible(false);

    }//GEN-LAST:event_dlgNewElementCancelActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed

        System.exit(0);

    }//GEN-LAST:event_miExitActionPerformed

    private void miOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenActionPerformed

        fc.resetChoosableFileFilters();
        fc.setFileFilter(PROJECT_EXTENSION_FILE_FILTER);

        fc.showOpenDialog(this);

        if (fc.getSelectedFile() != null) {
            openedFile = fc.getSelectedFile();
            project = FileUtil.openProject(openedFile);

            //cbNewElementType.setModel(new DefaultComboBoxModel(App.jarUtil.getElements().toArray()));
            //cbScenePreview.setModel(new DefaultComboBoxModel(App.jarUtil.getScenes().toArray()));
            //tableLibrary.updateUI();
        }

    }//GEN-LAST:event_miOpenActionPerformed

    private void miExpXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExpXmlActionPerformed

        fc.resetChoosableFileFilters();
        fc.setFileFilter(XML_EXTENSION_FILE_FILTER);

        fc.showSaveDialog(this);

        if (fc.getSelectedFile() != null) {
            Scene sc = new Scene();
            sc.setTitle(project.getTitle());

            List<ElementModel> all = new ArrayList<>(30);
            for (String s : project.getSceneElements().keySet()) {
                all.addAll(project.getSceneElements().get(s));
            }

            sc.setElement(ExportElement.elementToExpo(all));

            ExportElement.save(fc.getSelectedFile(), sc);
        }

    }//GEN-LAST:event_miExpXmlActionPerformed

    private JDialog dialog;
    private JColorChooser colorChooser;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntAddElement;
    private javax.swing.JButton btnAddNewElement;
    private javax.swing.JButton btnCanvasColor;
    private javax.swing.JToggleButton btnEdTLHand;
    private javax.swing.JToggleButton btnEdTLSelect;
    private javax.swing.JButton btnEditElement;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnRemoveElement;
    private javax.swing.JButton btnRemoveElementTree;
    private javax.swing.JComboBox cbNewElementType;
    private javax.swing.JComboBox cbScenePreview;
    private javax.swing.JFrame dialodNewElement;
    private javax.swing.JButton dlgNewElementCancel;
    private javax.swing.JButton dlgNewElementOK;
    private javax.swing.JTextField dlgTfID;
    private javax.swing.JTextField dlgTfName;
    private javax.swing.JFileChooser fc;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCanvasInfo;
    private javax.swing.JMenuBar menuMain;
    private javax.swing.JMenu miData;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miExpXml;
    private javax.swing.JMenuItem miOpen;
    private javax.swing.JMenuItem mnSave;
    private javax.swing.JPanel pnBody;
    private javax.swing.JPanel pnBottom;
    private javax.swing.JPanel pnCanvas;
    private javax.swing.JPanel pnEditTools;
    private javax.swing.JPanel pnElementPosition;
    private javax.swing.JPanel pnHead;
    private javax.swing.JPanel pnLibrary;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnScreenCam;
    private javax.swing.JPanel pnTree;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabLibrary;
    private javax.swing.JTable table;
    private javax.swing.JTable tableLibrary;
    private javax.swing.JTextField tfCamInitPx;
    private javax.swing.JTextField tfCamInitPy;
    private javax.swing.JTextField tfCamPx;
    private javax.swing.JTextField tfCamPy;
    private javax.swing.JTextField tfElName;
    private javax.swing.JTextField tfElPx;
    private javax.swing.JTextField tfElPy;
    private javax.swing.JTextField tfSceneHeight;
    private javax.swing.JTextField tfSceneWidth;
    private javax.swing.JTextField tfWindowHeight;
    private javax.swing.JTextField tfWindowWidth;
    private javax.swing.JTree treeStage;
    // End of variables declaration//GEN-END:variables

    private void updateSelectedOnTable(ElementModel el) {
        if (el == null) {
            return;
        }
        elementTable.setElement(el);
        //selected = el;
        //System.out.println(el);
    }

    private int fps = 1000 / 20; // 50
    private boolean anima = true;

    private void iniciaAnimacao() {
        Thread t = new Thread() {

            @Override
            public void run() {
                long prxAtualizacao = 0;
                while (anima) {
                    if (System.currentTimeMillis() >= prxAtualizacao) {
                        updateStage();
                        canvas.repaint();
                        prxAtualizacao = System.currentTimeMillis() + fps;
                    }
                }
            }

        };

        t.start();

    }

    private ElementModel copy(float px, float py, ElementModel source) {
        ElementModel el = JarUtil.copy(source);
        el.setPxy(px, py);

        el.setId(id++);
        el.setColor(source.getColor());
        el.setImage(source.getImage());

        return el;
    }

    private ElementModel hasColision(ElementModel element) {

        String[] tabs = project.getSceneElements().keySet().toArray(new String[0]);

        for (int i = tabs.length - 1; i >= 0; i--) {
            String k = tabs[i];

            for (int j = project.getSceneElements().get(k).size() - 1; j >= 0; j--) {
                ElementModel el = project.getSceneElements().get(k).get(j);
                if (GraphicTool.g().bcollide(el, element)) {
                    return el;
                }
            }
            /*
             for (ElementModel el : treeMap.get(k)) {

             if (GraphicTool.g().bcollide(el, element)) {
             return el;
             }
             }*/
        }

        return null;
    }

    private boolean isValidSelecion(ElementModel element) {
        for (ElementModel el : stageElements) {
            if (el == null) {
                return false;
            }

            if (GraphicTool.g().bcollide(el, element)) {
                return true;
            }
        }

        return false;
    }

    private void updateStage() {

        int temp = 3;

        for (int i = 0; i < camMoveButton.length; i++) {

            if (camMoveButton[i] == null) {
                continue;
            }

            int size = camMoveButton[i].getWidth() / 2;

            switch (i) {
                case 0:
                    camMoveButton[i].setPxy(-size, canvas.getHeight() / 2);
                    break;
                case 1:
                    camMoveButton[i].setPxy(canvas.getWidth() - size, canvas.getHeight() / 2);
                    break;
                case 2:
                    camMoveButton[i].setPxy(canvas.getWidth() / 2, -size);
                    break;
                case 3:
                    camMoveButton[i].setPxy(canvas.getWidth() / 2, canvas.getHeight() - size);
                    break;
            }

            camMoveButton[i].setFocus(false);

            if (GraphicTool.g().intersec(mousePos.x, mousePos.y, camMoveButton[i])) {

                if (i == 0) {
                    camMoveButton[i].setFocus(true);
                    cam.rollX(-temp);
                    tfCamPx.setText(String.valueOf(cam.getPx()));
                } else if (i == 1) {
                    camMoveButton[i].setFocus(true);
                    cam.rollX(temp);
                    tfCamPx.setText(String.valueOf(cam.getPx()));
                } else if (i == 2) {
                    camMoveButton[i].setFocus(true);
                    cam.rollY(-temp);
                    tfCamPy.setText(String.valueOf(cam.getPy()));
                } else {
                    camMoveButton[i].setFocus(true);
                    cam.rollY(temp);
                    tfCamPy.setText(String.valueOf(cam.getPy()));
                }
            }
        }

    }

    private JPanel createCanvas() {

        canvas = new JPanel() {

            @Override
            protected void paintComponent(Graphics gg) {
                super.paintComponent(gg);

                //System.out.println("selector " + selector);
                Graphics2D g = (Graphics2D) gg;

                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(btnCanvasColor.getBackground());
                g.fillRect(-cam.getPx(), -cam.getPy(), SCENE_WIDTH, SCENE_HEIGHT);

                if (scene != null) {
                    //scene.drawElements(g);
                    for (String k : project.getSceneElements().keySet()) {
                        for (ElementModel el : project.getSceneElements().get(k)) {
                            //el.drawMe(g);
                            cam.draw(g, el);
                        }
                    }
                }

                if (selectedElement != null) {
                    if (mousePos.x < 0 || mousePos.y < 0 || mousePos.x > getWidth() || mousePos.y > getHeight()) {
                        return;
                    }

                    //g.setColor(Color.LIGHT_GRAY);
                    //g.drawRect(p.x - 5, p.y - 5, 10, 10);
                    if (selectedElement.isValidImage()) {
                        g.drawImage(selectedElement.getImage().getImage(), mousePos.x - 5, mousePos.y - 5, null);
                    } else {
                        g.setColor(selectedElement.getColor());
                        g.drawRect(mousePos.x - 5, mousePos.y - 5, selectedElement.getWidth(), selectedElement.getHeight());
                    }

                } else {
                    int sp = 5;
                    g.setColor(Color.WHITE);

                    for (ElementModel el : stageElements) {
                        if (el == null) {
                            break;
                        }

                        g.drawRect(cam.fx(el.getPx() - sp), cam.fy(el.getPy() - sp), el.getWidth() + sp * 2, el.getHeight() + sp * 2);
                    }

                    if (startDrag != null) {
                        int npx = mousePos.x - startDrag.x;
                        int npy = mousePos.y - startDrag.y;
                        for (ElementModel el : stageElements) {
                            if (el == null) {
                                break;
                            }

                            g.drawRect(cam.fx(el.getPx() + npx), cam.fy(el.getPy() + npy), el.getWidth(), el.getHeight());
                        }
                    }

                    selector.drawMe(g);
                }

                for (ButtonElement b : camMoveButton) {
                    if (b != null) {
                        b.drawMe(g);
                    }
                }

                g.setColor(btnCanvasColor.getBackground());
                g.drawRect(-cam.getPx(), -cam.getPy(), SCENE_WIDTH, SCENE_HEIGHT);

                g.setColor(Color.RED);
                g.drawRect(-cam.getPx(), -cam.getPy(), SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        };

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {

                    if (selectedElement != null) {
                        addElementOnStage(copy(e.getX() + cam.getCpx(), e.getY() + cam.getCpy(), selectedElement));

                        //TODO add if control key is pressed
                        //selectedElement = null;
                        //tableLibrary.clearSelection();
                        //
                    } else {
                        mouseElement.setPxy(e.getX() + cam.getCpx(), e.getY() + cam.getCpy());

                        if (EditTool.SELECTOR == getEditTool()) {
                            selectElementOnStage(hasColision(mouseElement));
                        }
                    }

                } else {
                    selectedElement = null;
                    singleSelection(null);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {

                    mouseElement.setPxy(e.getX() + cam.getCpx(), e.getY() + cam.getCpy());

                    if (EditTool.SELECTOR == getEditTool()) {

                        //System.out.println("stageElements[0] == null " + stageElements[0] == null);
                        //System.out.println("isValidSelecion " + isValidSelecion(mouseElement));
                    /* && hasColision(mouseElement) == null*/

                        /*if (stageElements[0] == null && hasColision(mouseElement) != null) {
                         startDrag = e.getPoint();
                        
                         } else*/
                        if (stageElements[0] == null || !isValidSelecion(mouseElement)) {
                            selector.setEnabled(true);
                            selector.setPxy(e.getX(), e.getY());
                        } else {
                            startDrag = e.getPoint();
                        }
                    } else if (EditTool.HAND == getEditTool()) {

                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (selector.isEnabled()) {
                        //System.out.println("Erro1");
                        //scene.releaseElement(selector);
                        selector.adjustInvertSelection();
                        selector.setPx(selector.getPx() + cam.getCpx());
                        selector.setPy(selector.getPy() + cam.getCpy());

                        singleSelection(null);

                        for (String k : project.getSceneElements().keySet()) {
                            for (ElementModel el : project.getSceneElements().get(k)) {

                                if (GraphicTool.g().bcollide(el, selector)) {
                                    for (int i = 0; i < stageElements.length; i++) {
                                        if (stageElements[i] != null) {
                                            continue;
                                        }
                                        stageElements[i] = el;
                                        break;
                                    }

                                }
                            }
                        }

                    } else if (startDrag != null) {
                        int npx = e.getPoint().x - startDrag.x;
                        int npy = e.getPoint().y - startDrag.y;
                        for (ElementModel el : stageElements) {
                            if (el == null) {
                                break;
                            }
                            el.incPx(npx);
                            el.incPy(npy);
                        }

                    }

                    selector.setEnabled(false);
                    startDrag = null;
                    updateSelectedProperties(stageElements[0]);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousePos.x = -1;
            }

        });

        canvas.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //p = e.getPoint();

                if (selector.isVisible()) {
                    selector.setWidth(e.getX());
                    selector.setHeight(e.getY());
                }

                if (EditTool.HAND == getEditTool()) {
                    positionCam(e);
                }

                updateMousePosition(e);

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateMousePosition(e);
            }

            private void updateMousePosition(MouseEvent e) {
                mousePos = e.getPoint();
                lblCanvasInfo.setText(String.format("%d %d Cam: %.0f %.0f ", mousePos.x, mousePos.y, mousePos.x + cam.getCpx(), mousePos.y + cam.getCpy()));
            }
        });

        canvas.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                //TODO if controle pressed set px
                positionCam(0, (int) e.getPreciseWheelRotation() * 3);
            }
        });

        return canvas;
    }
    private static final Color BACKGROUND_COLOR = new Color(153, 153, 153);

    private void updateSelectedProperties(ElementModel el) {
        if (el != null) {
            tfElName.setText(el.getName());
            tfElPx.setText(String.valueOf(el.getPx()));
            tfElPy.setText(String.valueOf(el.getPy()));
        } else {
            tfElName.setText(null);
            tfElPx.setText(null);
            tfElPy.setText(null);
        }
    }

    private ElementModel getElementFromTree() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeStage.getLastSelectedPathComponent();

        if (node != null) {

            Object nodeInfo = node.getUserObject();

            if (nodeInfo instanceof ElementModel) {
                return (ElementModel) nodeInfo;
            }
        }

        return null;
    }

    private void setNewPxy() {

        ElementModel el = stageElements[0];

        if (el == null) {
            el = getElementFromTree();
        }

        if (el != null) {

            int px, py;

            try {
                px = Util.getInt(tfElPx);
                py = Util.getInt(tfElPy);

                el.setPxy(px, py);
                el.setName(tfElName.getText());

                treeStage.updateUI();

            } catch (NumberFormatException e) {

            }
        }
    }

    private IScene initScene() {
        return new IScene() {

            @Override
            public void updateScene() {
            }

            @Override
            public void changeSceneEvent() {
            }

            @Override
            public void closeWindow() {
            }

            @Override
            public boolean startScene() {
                return true;
            }

            @Override
            public IMemory[] getElements() {
                return null;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public void drawScene(Graphics2D g) {
                for (String k : project.getSceneElements().keySet()) {
                    for (ElementModel el : project.getSceneElements().get(k)) {
                        el.drawMe(g);
                    }
                }
            }

            @Override
            public void setElements(IMemory[] memory) {

            }

            @Override
            public void focusWindow() {

            }

            @Override
            public void lostFocusWindow() {

            }

            @Override
            public void resizeWindow(int width, int height) {

            }

        };
    }

    private void singleSelection(ElementModel elementModel) {
        for (int i = 1; i < stageElements.length; i++) {
            stageElements[i] = null;

        }

        stageElements[0] = elementModel;
    }

    private void positionCam() {
        int px = Util.getInt(tfCamPx);
        int py = Util.getInt(tfCamPy);

        if (px != -1 && py != -1) {
            cam.move(px, py);
        }
    }

    private void positionCam(int px, int py) {
        cam.rollX(px);
        cam.rollY(py);
    }

    private void positionCam(MouseEvent e) {
        cam.rollX(mousePos.x - e.getX());
        cam.rollY(mousePos.y - e.getY());
    }

    private void log(ElementModel... el) {
        for (ElementModel e : el) {
            System.out.println(e);
        }
    }

    private void selEditTool(JToggleButton toggleButton) {
        for (Component c : pnEditTools.getComponents()) {
            JToggleButton btn = (JToggleButton) c;

            if (btn == toggleButton) {
                btn.setSelected(true);
            } else {
                btn.setSelected(false);
            }
        }
    }

    private EditTool getEditTool() {
        for (Component c : pnEditTools.getComponents()) {
            JToggleButton btn = (JToggleButton) c;
            if (btn.isSelected()) {
                return EditTool.valueOf(btn.getName());
            }
        }

        return EditTool.SELECTOR;
    }

    private void selectElementOnStage(ElementModel elementModel) {
        updateSelectedOnTable(elementModel);
        singleSelection(elementModel);
        updateSelectedProperties(elementModel);

        //TODO selectionar elemento na tree
        tableLibrary.clearSelection();
    }

    private void selectElementOnLibrary(ElementModel elementModel) {
        selectedElement = elementModel;
        //updateSelectedOnTable(elementModel);
        treeStage.clearSelection();
    }

    private void openEditElement(ElementModel el) {
        DialogNewImageElement dlg = new DialogNewImageElement(this, true);

        dlg.setElement(el);

        dlg.setVisible(true);

        if (dlg.isOk()) {
            project.getLibElements().add(dlg.getElement());
            tableLibrary.updateUI();
        }
    }

    private enum EditTool {

        SELECTOR, HAND;
    }

}
