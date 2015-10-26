/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui;

import br.com.mvbos.jegui.ui.table.PropertyElementTable;
import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.element.ElementMovableModel;
import br.com.mvbos.jeg.element.IButtonElement;
import br.com.mvbos.jeg.element.SelectorElement;
import br.com.mvbos.jeg.engine.GraphicTool;
import br.com.mvbos.jeg.scene.Click;
import br.com.mvbos.jeg.scene.IScene;
import br.com.mvbos.jegui.ui.tree.ElementMutableTreeNode;
import br.com.mvbos.jeg.window.Camera;
import br.com.mvbos.jeg.window.IMemory;
import br.com.mvbos.jegui.el.ButtonElement;
import java.awt.Color;
import java.awt.Component;
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
import java.util.List;
import java.util.Map;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
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

    private final PropertyElementTable elementTable = new PropertyElementTable();

    private final List<ElementModel> all = FileUtil.loadLib();
    private final Map<String, List<ElementModel>> treeMap = FileUtil.loadList();

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

    /**
     * Creates new form Window
     */
    public Window() {

        selector.setColor(Color.WHITE);

        /*ElementModel temp = new ElementModel(0, 0, "Background");
         temp.setImage(new ImageIcon("images\\bg.png"));
         all.add(temp);

         temp = new ElementModel(15, 15, "Bloco");
         all.add(temp);*/
        initComponents();

        root = new DefaultMutableTreeNode("Memory");

        initMyComponents();

        iniciaAnimacao();

    }

    private void initMyComponents() {
        Camera.c().config(SCENE_WIDTH, SCENE_HEIGHT);
        tfSceneWidth.setText(String.valueOf(SCENE_WIDTH));
        tfSceneHeight.setText(String.valueOf(SCENE_HEIGHT));

        tfWindowWidth.setText(String.valueOf(SCREEN_WIDTH));
        tfWindowHeight.setText(String.valueOf(SCREEN_HEIGHT));

        tree.removeAll();
        tree.setModel(new DefaultTreeModel(root));

        /*tree.setDragEnabled(true);
         tree.setDropMode(DropMode.ON_OR_INSERT);
         tree.setTransferHandler(new TransferHandler(){
            
         });
         tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
         */
        for (String k : treeMap.keySet()) {
            DefaultMutableTreeNode t = new DefaultMutableTreeNode(k);

            for (ElementModel el : treeMap.get(k)) {
                t.add(new ElementMutableTreeNode(el));
            }

            root.add(t);
        }

        //root.add(new ElementMutableTreeNode(el));
        btnCanvasColor.setBackground(pnCanvas.getBackground());

        tableLibrary.setModel(new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return all.size();
            }

            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                ElementModel el = all.get(rowIndex);
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

    private void addElement(ElementModel el) {

        DefaultMutableTreeNode parentNode;
        TreePath parentPath = tree.getSelectionPath();

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

        treeMap.get(parentNode.toString()).add(el);
        parentNode.add(new ElementMutableTreeNode(el));
        tree.updateUI();
    }

    private void removeElement(DefaultMutableTreeNode node) {
        if (!(node instanceof ElementMutableTreeNode)) {
            throw new IllegalArgumentException("Invalid node location");
        }

        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        ElementModel el = (ElementModel) node.getUserObject();
        TreeNode parent = node.getParent();

        treeMap.get(parent.toString()).remove(el);
        model.removeNodeFromParent(node);
        tree.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialodNewElement = new javax.swing.JFrame();
        jLabelID = new javax.swing.JLabel();
        jLabelName = new javax.swing.JLabel();
        dlgTfID = new javax.swing.JTextField();
        dlgTfName = new javax.swing.JTextField();
        radioTypeImage = new javax.swing.JRadioButton();
        radioTypeSprite = new javax.swing.JRadioButton();
        radioTypeText = new javax.swing.JRadioButton();
        radioTypeButton = new javax.swing.JRadioButton();
        dlgNewElementCancel = new javax.swing.JButton();
        dlgNewElementOK = new javax.swing.JButton();
        buttonGroupType = new javax.swing.ButtonGroup();
        pnHead = new javax.swing.JPanel();
        pnEditTools = new javax.swing.JPanel();
        btnEdTLSelect = new javax.swing.JToggleButton();
        btnEdTLHand = new javax.swing.JToggleButton();
        pnBody = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
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
        jPanel2 = new javax.swing.JPanel();
        pnTree = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
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
        menuMain = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnSave = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        dialodNewElement.setTitle("Add new Element");

        jLabelID.setText("ID:");

        jLabelName.setText("Name:");

        dlgTfID.setText("1");

        dlgTfName.setText("Element");

        buttonGroupType.add(radioTypeImage);
        radioTypeImage.setSelected(true);
        radioTypeImage.setText("Image / Block");

        buttonGroupType.add(radioTypeSprite);
        radioTypeSprite.setText("Sprite / Tiles");

        buttonGroupType.add(radioTypeText);
        radioTypeText.setText("Text");

        buttonGroupType.add(radioTypeButton);
        radioTypeButton.setText("Button");

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
                    .addGroup(dialodNewElementLayout.createSequentialGroup()
                        .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgTfName, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                    .addComponent(radioTypeImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioTypeSprite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioTypeText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioTypeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialodNewElementLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(dlgNewElementOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgNewElementCancel)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioTypeImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioTypeSprite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioTypeText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioTypeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialodNewElementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dlgNewElementOK)
                    .addComponent(dlgNewElementCancel))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jSplitPane1.setDividerLocation(700);

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

        jSplitPane1.setLeftComponent(pnCanvas);

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
                .addContainerGap(216, Short.MAX_VALUE))
        );
        pnLibraryLayout.setVerticalGroup(
            pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLibraryLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddNewElement)
                    .addComponent(btnRemoveElement))
                .addContainerGap())
        );

        tabLibrary.addTab("Library", pnLibrary);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)
        );

        tabLibrary.addTab("Config", jPanel2);

        pnTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(tree);

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

        jSplitPane1.setRightComponent(pnMenu);

        javax.swing.GroupLayout pnBodyLayout = new javax.swing.GroupLayout(pnBody);
        pnBody.setLayout(pnBodyLayout);
        pnBodyLayout.setHorizontalGroup(
            pnBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        pnBodyLayout.setVerticalGroup(
            pnBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
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

        tfSceneWidth.setText("800");

        tfSceneHeight.setText("600");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfWindowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfWindowHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPreview)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfWindowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfWindowHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(btnPreview))
                .addContainerGap(39, Short.MAX_VALUE))
        );

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
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        mnSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnSave.setText("Save");
        mnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSaveActionPerformed(evt);
            }
        });
        jMenu1.add(mnSave);

        menuMain.add(jMenu1);

        jMenu2.setText("Edit");
        menuMain.add(jMenu2);

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

    private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeValueChanged

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();
        //if (node.isLeaf()) {
        if (nodeInfo instanceof ElementModel) {
            selectElementOnStage((ElementModel) nodeInfo);
        }

    }//GEN-LAST:event_treeValueChanged

    private void btnCanvasColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanvasColorActionPerformed
        if (dialog == null) {
            colorChooser = new JColorChooser();
            ActionListener ac = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pnCanvas.setBackground(colorChooser.getColor());
                    btnCanvasColor.setBackground(pnCanvas.getBackground());

                    Color x = colorChooser.getColor();
                    Color c = (x.getRed() + x.getGreen() + x.getBlue()) / 3 < 127 ? Color.WHITE : Color.BLACK;

                    selector.setColor(c);
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

        selectElementOnLibrary(all.get(sel));

    }//GEN-LAST:event_tableLibraryMouseClicked

    private void btnRemoveElementTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveElementTreeActionPerformed

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

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

        FileUtil.saveList(treeMap, all);

    }//GEN-LAST:event_mnSaveActionPerformed

    private void btnEdTLSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdTLSelectActionPerformed

        selEditTool((JToggleButton) evt.getSource());


    }//GEN-LAST:event_btnEdTLSelectActionPerformed

    private void btnEdTLHandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdTLHandActionPerformed

        selEditTool((JToggleButton) evt.getSource());

    }//GEN-LAST:event_btnEdTLHandActionPerformed

    private void btnAddNewElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewElementActionPerformed

        dlgTfID.setText(String.valueOf(all.size() + 1));
        dialodNewElement.pack();
        dialodNewElement.setLocationRelativeTo(this);
        
        dialodNewElement.setVisible(true);
        

    }//GEN-LAST:event_btnAddNewElementActionPerformed

    private void dlgNewElementOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNewElementOKActionPerformed

        ElementModel el = new ElementModel(20, 20, dlgTfName.getText());
        el.setId(Util.getInt(dlgTfID));
        all.add(el);
        dialodNewElement.dispose();
        tableLibrary.updateUI();

    }//GEN-LAST:event_dlgNewElementOKActionPerformed

    private void dlgNewElementCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgNewElementCancelActionPerformed

        dialodNewElement.setVisible(false);

    }//GEN-LAST:event_dlgNewElementCancelActionPerformed

    private JDialog dialog;
    private JColorChooser colorChooser;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntAddElement;
    private javax.swing.JButton btnAddNewElement;
    private javax.swing.JButton btnCanvasColor;
    private javax.swing.JToggleButton btnEdTLHand;
    private javax.swing.JToggleButton btnEdTLSelect;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnRemoveElement;
    private javax.swing.JButton btnRemoveElementTree;
    private javax.swing.ButtonGroup buttonGroupType;
    private javax.swing.JFrame dialodNewElement;
    private javax.swing.JButton dlgNewElementCancel;
    private javax.swing.JButton dlgNewElementOK;
    private javax.swing.JTextField dlgTfID;
    private javax.swing.JTextField dlgTfName;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblCanvasInfo;
    private javax.swing.JMenuBar menuMain;
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
    private javax.swing.JRadioButton radioTypeButton;
    private javax.swing.JRadioButton radioTypeImage;
    private javax.swing.JRadioButton radioTypeSprite;
    private javax.swing.JRadioButton radioTypeText;
    private javax.swing.JTabbedPane tabLibrary;
    private javax.swing.JTable table;
    private javax.swing.JTable tableLibrary;
    private javax.swing.JTextField tfCamPx;
    private javax.swing.JTextField tfCamPy;
    private javax.swing.JTextField tfElName;
    private javax.swing.JTextField tfElPx;
    private javax.swing.JTextField tfElPy;
    private javax.swing.JTextField tfSceneHeight;
    private javax.swing.JTextField tfSceneWidth;
    private javax.swing.JTextField tfWindowHeight;
    private javax.swing.JTextField tfWindowWidth;
    private javax.swing.JTree tree;
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

    private int id = 1;

    private ElementModel copy(float px, float py, ElementModel source) {
        ElementModel el = new ElementModel(px, py, source.getWidth(), source.getHeight(), id + " " + source.getName());
        el.setId(id++);
        el.setColor(source.getColor());
        el.setImage(source.getImage());

        return el;
    }

    private Point mousePos = new Point();
    private Point startDrag;

    private ElementModel[] stageElements = new ElementModel[30];

    private final ElementModel mouseElement = new ElementModel(10, 10, "mouseElement");

    private ElementModel hasColision(ElementModel element) {

        String[] tabs = treeMap.keySet().toArray(new String[0]);

        for (int i = tabs.length - 1; i >= 0; i--) {
            String k = tabs[i];

            for (int j = treeMap.get(k).size() - 1; j >= 0; j--) {
                ElementModel el = treeMap.get(k).get(j);
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
                    Camera.c().rollX(-temp);
                    tfCamPx.setText(String.valueOf(Camera.c().getPx()));
                } else if (i == 1) {
                    camMoveButton[i].setFocus(true);
                    Camera.c().rollX(temp);
                    tfCamPx.setText(String.valueOf(Camera.c().getPx()));
                } else if (i == 2) {
                    camMoveButton[i].setFocus(true);
                    Camera.c().rollY(-temp);
                    tfCamPy.setText(String.valueOf(Camera.c().getPy()));
                } else {
                    camMoveButton[i].setFocus(true);
                    Camera.c().rollY(temp);
                    tfCamPy.setText(String.valueOf(Camera.c().getPy()));
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
                g.fillRect(-Camera.c().getPx(), -Camera.c().getPy(), SCENE_WIDTH, SCENE_HEIGHT);

                if (scene != null) {
                    //scene.drawElements(g);
                    for (String k : treeMap.keySet()) {
                        for (ElementModel el : treeMap.get(k)) {
                            //el.drawMe(g);
                            Camera.c().draw(g, el);
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

                        g.drawRect(Camera.c().fx(el.getPx() - sp), Camera.c().fy(el.getPy() - sp), el.getWidth() + sp * 2, el.getHeight() + sp * 2);
                    }

                    if (startDrag != null) {
                        int npx = mousePos.x - startDrag.x;
                        int npy = mousePos.y - startDrag.y;
                        for (ElementModel el : stageElements) {
                            if (el == null) {
                                break;
                            }

                            g.drawRect(Camera.c().fx(el.getPx() + npx), Camera.c().fy(el.getPy() + npy), el.getWidth(), el.getHeight());
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
                g.drawRect(-Camera.c().getPx(), -Camera.c().getPy(), SCENE_WIDTH, SCENE_HEIGHT);

                g.setColor(Color.RED);
                g.drawRect(-Camera.c().getPx(), -Camera.c().getPy(), SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        };

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {

                    if (selectedElement != null) {
                        addElement(copy(e.getX() + Camera.c().getCpx(), e.getY() + Camera.c().getCpy(), selectedElement));

                        //TODO add if control key is pressed
                        //selectedElement = null;
                        //tableLibrary.clearSelection();
                        //
                    } else {
                        mouseElement.setPxy(e.getX() + Camera.c().getCpx(), e.getY() + Camera.c().getCpy());

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

                    mouseElement.setPxy(e.getX() + Camera.c().getCpx(), e.getY() + Camera.c().getCpy());

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
                        selector.setPx(selector.getPx() + Camera.c().getCpx());
                        selector.setPy(selector.getPy() + Camera.c().getCpy());

                        singleSelection(null);

                        for (String k : treeMap.keySet()) {
                            for (ElementModel el : treeMap.get(k)) {

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
                lblCanvasInfo.setText(String.format("%d %d Cam: %.0f %.0f ", mousePos.x, mousePos.y, mousePos.x + Camera.c().getCpx(), mousePos.y + Camera.c().getCpy()));
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
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

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

                tree.updateUI();

            } catch (NumberFormatException e) {

            }
        }
    }

    private IScene initScene() {
        return new IScene() {

            @Override
            public void update() {
            }

            @Override
            public void changeSceneEvent() {
            }

            @Override
            public void selectElement(ElementModel e) {
            }

            @Override
            public void focusElement(ElementModel e) {
            }

            @Override
            public void releaseElement(ElementModel element, ElementModel anotherElement) {
            }

            @Override
            public void closeWindow() {
            }

            @Override
            public boolean startScene() {
                return true;
            }

            @Override
            public IMemory getElements() {
                return null;
            }

            @Override
            public void clickElement(int clickCount) {
            }

            @Override
            public void clickElement(Click m) {
            }

            @Override
            public void selectElement(ElementModel[] arr) {
            }

            @Override
            public void mouseMove(ElementModel e, Click m) {
            }

            @Override
            public void keyEvent(char keyChar, int keyCode) {
            }

            @Override
            public void keyRelease(char keyChar, int keyCode) {
            }

            @Override
            public void setTitle(String title) {
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public void releaseElement(ElementModel element) {
            }

            @Override
            public void drawElements(Graphics2D g) {
                for (String k : treeMap.keySet()) {
                    for (ElementModel el : treeMap.get(k)) {
                        el.drawMe(g);
                    }
                }
            }

            @Override
            public void clickButton(IButtonElement button) {
            }

            @Override
            public void moveElement(ElementMovableModel selectedMovableElement) {
            }

            @Override
            public void reflashElementPosition(ElementMovableModel e) {
            }

            @Override
            public void startGame() {
            }

            @Override
            public Color getBgColor() {
                return null;
            }

            @Override
            public void resizeWindow() {
            }
        };
    }

    private void singleSelection(ElementModel elementModel) {
        for (int i = 0; i < stageElements.length; i++) {
            stageElements[i] = null;

        }

        stageElements[0] = elementModel;
    }

    private void positionCam() {
        int px = Util.getInt(tfCamPx);
        int py = Util.getInt(tfCamPy);

        if (px != -1 && py != -1) {
            Camera.c().move(px, py);
        }
    }

    private void positionCam(int px, int py) {
        Camera.c().rollX(px);
        Camera.c().rollY(py);
    }

    private void positionCam(MouseEvent e) {
        Camera.c().rollX(mousePos.x - e.getX());
        Camera.c().rollY(mousePos.y - e.getY());
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
        updateSelectedOnTable(elementModel);
        tree.clearSelection();
    }

    private enum EditTool {

        SELECTOR, HAND;
    }

}
