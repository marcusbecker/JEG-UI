/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jeg.ui;

import br.com.mvbos.jeg.ui.table.PropertyElementTable;
import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.ui.tree.ElementMutableTreeNode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
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

    private final List<ElementModel> all = new ArrayList<>(100);
    private final Map<String, List<ElementModel>> treeMap = new LinkedHashMap<>(3);

    private static final String STAGE = "Stage";
    private static final String FOREGROUND = "Foreground";
    private static final String BACKGROUND = "Background";

    private JPanel canvas;
    private final DefaultMutableTreeNode root;
    private ElementModel selectedElement;

    /**
     * Creates new form Window
     */
    public Window() {

        treeMap.put(BACKGROUND, new ArrayList<ElementModel>(5));
        treeMap.put(STAGE, new ArrayList<ElementModel>(40));
        treeMap.put(FOREGROUND, new ArrayList<ElementModel>(20));

        ElementModel temp = new ElementModel(0, 0, "Image");
        temp.setImage(new ImageIcon("images\\bg.png"));
        all.add(temp);

        temp = new ElementModel(15, 15, "Bloco");
        all.add(temp);

        initComponents();

        root = new DefaultMutableTreeNode("Memory");
        tree.removeAll();
        tree.setModel(new DefaultTreeModel(root));

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

        iniciaAnimacao();

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

        pnHead = new javax.swing.JPanel();
        pnBody = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnCanvas = createCanvas();
        pnMenu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = elementTable;
        pnTree = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        bntAddElement = new javax.swing.JButton();
        btnRemoveElementTree = new javax.swing.JButton();
        tabLibrary = new javax.swing.JTabbedPane();
        pnLibrary = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableLibrary = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnBottom = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        tfWidth = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfHeight = new javax.swing.JTextField();
        tfCamPx = new javax.swing.JTextField();
        tfCamPy = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnCanvasColor = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tfElPy = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tfElPx = new javax.swing.JTextField();
        tfElName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pnHeadLayout = new javax.swing.GroupLayout(pnHead);
        pnHead.setLayout(pnHeadLayout);
        pnHeadLayout.setHorizontalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnHeadLayout.setVerticalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 83, Short.MAX_VALUE)
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
            .addGap(0, 722, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(pnCanvas);

        jScrollPane1.setViewportView(table);

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

        jButton2.setText("+");

        jButton3.setText("-");

        javax.swing.GroupLayout pnLibraryLayout = new javax.swing.GroupLayout(pnLibrary);
        pnLibrary.setLayout(pnLibraryLayout);
        pnLibraryLayout.setHorizontalGroup(
            pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(pnLibraryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(220, Short.MAX_VALUE))
        );
        pnLibraryLayout.setVerticalGroup(
            pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLibraryLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnLibraryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(0, 9, Short.MAX_VALUE))
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
            .addGap(0, 181, Short.MAX_VALUE)
        );

        tabLibrary.addTab("Config", jPanel2);

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
            .addComponent(tabLibrary)
        );
        pnTreeLayout.setVerticalGroup(
            pnTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTreeLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bntAddElement)
                    .addComponent(btnRemoveElementTree))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabLibrary, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnMenuLayout = new javax.swing.GroupLayout(pnMenu);
        pnMenu.setLayout(pnMenuLayout);
        pnMenuLayout.setHorizontalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMenuLayout.createSequentialGroup()
                .addGroup(pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnTree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );
        pnMenuLayout.setVerticalGroup(
            pnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMenuLayout.createSequentialGroup()
                .addComponent(pnTree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tfWidth.setText("800");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("x");

        tfHeight.setText("600");

        tfCamPx.setText("0");

        tfCamPy.setText("0");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(",");

        btnCanvasColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanvasColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tfCamPx, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfWidth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(tfCamPy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCanvasColor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel3, jLabel4});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfCamPx, tfCamPy, tfHeight, tfWidth});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(tfCamPx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfCamPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCanvasColor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfCamPx, tfCamPy, tfHeight, tfWidth});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jLabel4});

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tfElPy.setText("0");
        tfElPy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfElPyKeyReleased(evt);
            }
        });

        jLabel1.setText("x,y");

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfElName)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfElPx, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfElPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfElPx, tfElPy});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfElName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfElPy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(tfElPx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnBottomLayout = new javax.swing.GroupLayout(pnBottom);
        pnBottom.setLayout(pnBottomLayout);
        pnBottomLayout.setHorizontalGroup(
            pnBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(130, 130, 130))
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
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );

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
                .addComponent(pnHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
            //updateSelectedOnTable((ElementModel) nodeInfo);
            updateSelectedPropertie((ElementModel) nodeInfo);
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

        selectedElement = all.get(sel);
        updateSelectedOnTable(selectedElement);

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

    private JDialog dialog;
    private JColorChooser colorChooser;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntAddElement;
    private javax.swing.JButton btnCanvasColor;
    private javax.swing.JButton btnRemoveElementTree;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnBody;
    private javax.swing.JPanel pnBottom;
    private javax.swing.JPanel pnCanvas;
    private javax.swing.JPanel pnHead;
    private javax.swing.JPanel pnLibrary;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnTree;
    private javax.swing.JTabbedPane tabLibrary;
    private javax.swing.JTable table;
    private javax.swing.JTable tableLibrary;
    private javax.swing.JTextField tfCamPx;
    private javax.swing.JTextField tfCamPy;
    private javax.swing.JTextField tfElName;
    private javax.swing.JTextField tfElPx;
    private javax.swing.JTextField tfElPy;
    private javax.swing.JTextField tfHeight;
    private javax.swing.JTextField tfWidth;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

    private void updateSelectedOnTable(ElementModel el) {

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
                        canvas.repaint();
                        prxAtualizacao = System.currentTimeMillis() + fps;
                    }
                }
            }

        };

        t.start();

    }

    private int id = 1;

    private ElementModel copy(int px, int py, ElementModel source) {
        ElementModel el = new ElementModel(px, py, source.getWidth(), source.getHeight(), id + " " + source.getName());
        el.setId(id++);
        el.setColor(source.getColor());
        el.setImage(source.getImage());

        return el;
    }

    private Point p = new Point();

    private JPanel createCanvas() {

        canvas = new JPanel() {

            @Override
            protected void paintComponent(Graphics gg) {
                super.paintComponent(gg);

                Graphics2D g = (Graphics2D) gg;

                g.setColor(btnCanvasColor.getBackground());
                g.drawRect(0, 0, getWidth(), getHeight());

                for (String k : treeMap.keySet()) {

                    for (ElementModel el : treeMap.get(k)) {
                        el.drawMe(g);
                    }
                }

                if (p.x < 0 || p.y < 0 || p.x > getWidth() || p.y > getHeight()) {
                    return;
                }

                if (selectedElement != null) {
                    //g.setColor(Color.LIGHT_GRAY);
                    //g.drawRect(p.x - 5, p.y - 5, 10, 10);

                    if (selectedElement.isValidImage()) {
                        g.drawImage(selectedElement.getImage().getImage(), p.x - 5, p.y - 5, null);
                    } else {
                        g.setColor(selectedElement.getDefaultColor());
                        g.drawRect(p.x - 5, p.y - 5, 10, 10);
                    }

                }

            }
        };

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (selectedElement != null) {
                        addElement(copy(p.x, p.y, selectedElement));
                    }
                } else {
                    selectedElement = null;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                p.x = -1;
            }

        });

        canvas.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //p = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                p = e.getPoint();
            }
        });

        return canvas;
    }

    private void updateSelectedPropertie(ElementModel el) {
        tfElName.setText(el.getName());
        tfElPx.setText(String.valueOf(el.getPx()));
        tfElPy.setText(String.valueOf(el.getPy()));
    }

    private void setNewPxy() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();

        if (nodeInfo instanceof ElementModel) {
            int px, py;
            ElementModel el = (ElementModel) nodeInfo;
            try {
                px = Integer.parseInt(tfElPx.getText());
                py = Integer.parseInt(tfElPy.getText());

                el.setPxy(px, py);
                el.setName(tfElName.getText());

                
                tree.updateUI();
                
            } catch (NumberFormatException e) {

            }
        }
    }

}
