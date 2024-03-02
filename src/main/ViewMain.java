package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import library.ReadTableData;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import library.InterpretSQl;
import library.ReadCatalog;

public class ViewMain extends javax.swing.JFrame {

    public ViewMain() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void read(String table, String query) {

            InterpretSQl sql = new InterpretSQl();
            ArrayList dataSQl = sql.interpretSQL(query);
            boolean exist = dataSQl.get(0).equals("*");
        if (query.equals("") || exist) {
            ReadTableData readData = new ReadTableData();
            ReadCatalog readCatalog = new ReadCatalog();
            DefaultTableModel model;
            ArrayList<String> data = readCatalog.catalog(exist?dataSQl.get(1).toString():table);
            String[] headers = new String[data.size()];
            String[] registers = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                headers[i] = data.get(i);
            }
            model = new DefaultTableModel(null, headers);

            List<String[]> tableData = readData.readTableData(exist?dataSQl.get(1).toString():table);

            if (tableData != null) {
                String str = "";
                for (String[] row : tableData) {
                    for (String value : row) {
                        str += value + " ";
                    }
                    Pattern patron = Pattern.compile("\\d+|\\w+\\s+\\w+|\\W+");
                    Matcher matcher = patron.matcher(str);
                    ArrayList<String> reg = new ArrayList<>();
                    while (matcher.find()) {
                        String parte = matcher.group();
                        if (!parte.trim().isEmpty()) {
                            reg.add(parte.trim());

                        }
                    }
                    str = "";

                    System.out.println(reg);
                    for (int i = 0; i < data.size(); i++) {
                        if (i < data.size()) {
                            registers[i] = reg.get(i);
                        }
                    }
                    model.addRow(registers);

                }

            } else {
                System.out.println("Error reading table data.");
            }
            tabla.setModel(model);
        } else {

            ReadTableData readData = new ReadTableData();
            ReadCatalog readCatalog = new ReadCatalog();
            DefaultTableModel model;
            ArrayList<String> data = readCatalog.catalog(String.valueOf(dataSQl.get(1)).toUpperCase());

            String[] datas = data.toArray(new String[0]);
            String[] columns = dataSQl.get(0).toString().split(",");
            int[] index = indices(datas, columns);

            String[] headers = new String[index.length];
            String[] registers = new String[headers.length];
            for (int i = 0; i < index.length; i++) {
                headers[i] = data.get(index[i]);
            }
            model = new DefaultTableModel(null, headers);

            List<String[]> tableData = readData.readTableData(String.valueOf(dataSQl.get(1)));

            if (tableData != null) {
                String str = "";
                for (String[] row : tableData) {
                    for (String value : row) {
                        str += value + " ";
                    }
                    Pattern patron = Pattern.compile("\\d+|\\w+\\s+\\w+|\\W+");
                    Matcher matcher = patron.matcher(str);
                    ArrayList<String> reg = new ArrayList<>();
                    while (matcher.find()) {
                        String parte = matcher.group();
                        if (!parte.trim().isEmpty()) {
                            reg.add(parte.trim());

                        }
                    }
                    str = "";
                    for (int i = 0; i < index.length; i++) {
                        if (i < index.length) {
                            registers[i] = reg.get(index[i]);
                        }
                    }
                    model.addRow(registers);

                }

            } else {
                System.out.println("Error reading table data.");
            }
            tabla.setModel(model);

        }

    }

    public static int[] indices(String[] arreglo, String[] elementosBuscados) {
        int[] ind = buscarIndices(arreglo, elementosBuscados);

        return ind;
    }

    public static int[] buscarIndices(String[] arreglo, String[] elementos) {
        List<Integer> indicesEncontrados = new ArrayList<>();

        for (String elemento : elementos) {
            for (int i = 0; i < arreglo.length; i++) {
                if (elemento.equals(arreglo[i])) {
                    indicesEncontrados.add(i);
                    break; // Para pasar al siguiente elemento buscado
                }
            }
        }

        // Ordenar los índices de forma ascendente
        Collections.sort(indicesEncontrados);

        // Convertir la lista de índices a un arreglo de enteros
        int[] resultado = new int[indicesEncontrados.size()];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = indicesEncontrados.get(i);
        }

        return resultado;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtQuery = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        cbxTables = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 0, 51));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("X");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(32, 51, 78));
        jLabel2.setText("Compilador de codigoSQL");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabla);

        txtQuery.setColumns(20);
        txtQuery.setRows(5);
        txtQuery.setText("SELECT Codigo FROM UBIGEO ORDER BY DESC");
        txtQuery.setToolTipText("");
        jScrollPane2.setViewportView(txtQuery);

        jButton1.setBackground(new java.awt.Color(3, 141, 79));
        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ejecutar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbxTables.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ESCRIBIR SENTENCIA", "UBIGEO", "MONEDAS", "CURSOS", "ALUMNOS", "PROFESORES", "SEXO", "ESCUELA" }));
        cbxTables.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTablesItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cbxTables, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(cbxTables))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (cbxTables.getSelectedItem().toString().equals("ESCRIBIR SENTENCIA")) {
            read(cbxTables.getSelectedItem().toString(), txtQuery.getText());
        } else {
            read(cbxTables.getSelectedItem().toString(), "");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbxTablesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTablesItemStateChanged
        if (cbxTables.getSelectedItem().toString().equals("ESCRIBIR SENTENCIA")) {
            read(cbxTables.getSelectedItem().toString(), txtQuery.getText());
        } else {
            read(cbxTables.getSelectedItem().toString(), "");
        }

    }//GEN-LAST:event_cbxTablesItemStateChanged

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbxTables;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla;
    private javax.swing.JTextArea txtQuery;
    // End of variables declaration//GEN-END:variables
}
