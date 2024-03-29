package main;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import library.InterpretSQl;
import library.ReadCatalog;
import library.ReadTableData;
import library.SearchIndex;

public class SQL extends javax.swing.JFrame {

    public SQL() {
        initComponents();
        this.setLocationRelativeTo(this);
        this.setTitle("COMPILADOR DE SQL");
    }

    private void executeQuery(String query) {
        //validamos si el query es vacio
        if (txtQuery.getText().length() <= 6) {
            JOptionPane.showMessageDialog(this, "Ingresa la consulta por favor...", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model;

        ReadTableData arrayData = new ReadTableData();
        ReadCatalog catalogs = new ReadCatalog();

        InterpretSQl sql = new InterpretSQl();
        ArrayList arraySQL = null;
        arraySQL = sql.interpretSQL(query);
        if (arraySQL == null) {
            JOptionPane.showMessageDialog(this, "Ingresa la consulta válida por favor...", "ERROR", JOptionPane.ERROR_MESSAGE);
            
            return;
        }

        boolean contieneNumero = false;
        String valorNumerico = "";
        if (arraySQL != null) {
            String[] newColumn = arraySQL.get(0).toString().split(" ");

            for (String elemento : newColumn) {
                try {
                    Double.parseDouble(elemento);
                    valorNumerico = elemento; // Intenta convertir el elemento a un número
                    contieneNumero = true; // Si tiene éxito, el elemento es un número
                    break; // Sal del ciclo porque ya encontraste un número
                } catch (NumberFormatException e) {
                    // El elemento no es un número
                }
            }
        }
        //Valida si el filtrado de datos es por colmunas o en general
        boolean exist = arraySQL.get(0).equals("*");

        //Indices de elementos
        int[] index = null;
        String[] headers = null;
        int[] indexOrder = null;
        String colmunName = null;
        int[] indexOrderWhere = null;

        ArrayList<String> data = new ArrayList<>();
        data = catalogs.catalog(arraySQL.get(1).toString().toUpperCase());
        if (contieneNumero == true) {
            data.add(String.valueOf(valorNumerico)); // Asigna el valor numérico al tercer elemento de cada arreglo
        }

        if (exist) {

            //Creamos la colmunas de las tablas
            headers = new String[data.size()];
            for (int i = 0; i < headers.length; i++) {
                headers[i] = data.get(i);
            }
            SearchIndex indexs = new SearchIndex();
            String[] datas = data.toArray(new String[0]);
            colmunName = arraySQL.get(3).toString();
            String[] columns = arraySQL.get(3).toString().contains("null") ? "codigo".split(",") : arraySQL.get(3).toString().split(",");
            indexOrder = indexs.buscarIndices(datas, columns);

        } else {

            String[] datas = data.toArray(new String[0]);
            String[] columns = arraySQL.get(0).toString().split(" ");
            SearchIndex indexs = new SearchIndex();
            index = indexs.buscarIndices(datas, columns);
            //ORDENAMIENTO DE DATOS
            String[] columnsOrder = arraySQL.get(3).toString().contains("null") ? "codigo".split(",") : arraySQL.get(3).toString().split(",");

            colmunName = arraySQL.get(3).toString();
            indexOrder = indexs.buscarIndices(datas, columnsOrder);

            headers = new String[index.length];
            for (int i = 0; i < index.length; i++) {
                headers[i] = data.get(index[i]);
            }
        }
        if (indexOrder.length <= 0) {
            JOptionPane.showMessageDialog(this, "Ingresa una columna valida de ordenamiento...\nLa columna [" + colmunName + "] no existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Lista de datos de la tabla
        List<String[]> tableData = arrayData.readTableData(String.valueOf(arraySQL.get(1)));

        if (contieneNumero) {
            for (String[] arreglo : tableData) {
                String[] nuevoArreglo = new String[arreglo.length + 1]; // Crear un nuevo arreglo con un tamaño mayor
                System.arraycopy(arreglo, 0, nuevoArreglo, 0, arreglo.length); // Copiar los elementos del arreglo original al nuevo arreglo
                nuevoArreglo[nuevoArreglo.length - 1] = valorNumerico; // Asignar valorNumerico a la última posición del nuevo arreglo
                tableData.set(tableData.indexOf(arreglo), nuevoArreglo); // Reemplazar el arreglo original con el nuevo arreglo en la lista
            }
        }
        /**
         * ***********************************************************************
         * Convertimos si el usuario a ingresado mayusculas o minusculas
         * ***********************************************************************
         */
        if (arraySQL.get(6) != null && arraySQL.get(7) != null) {
            int[] indexElement = null;
            SearchIndex searchIndex = new SearchIndex();
            String[] functions = arraySQL.get(6).toString().split(" ");

            indexElement = searchIndex.buscarIndices(data.toArray(new String[0]), arraySQL.get(7).toString().split(" "));
            for (int i = 0; i < functions.length; i++) {
                if (indexElement.length > i) {
                    int[] indexScope = {indexElement[i]};
                    String function = functions[i];

                    if (function.equals("upper")) {
                        arrayData.upperCaseAndLowerCase(tableData, indexScope, true);
                    } else if (function.equals("lower")) {
                        arrayData.upperCaseAndLowerCase(tableData, indexScope, false);
                    }

                    /*
                     * ***********************************************************************
                     * Validamos si el usuario ingreso el length
                     * ***********************************************************************
                     */
                    if (function.equals("length")) {
                        arrayData.replaceWithLength(tableData, indexScope);
                    }

                }

            }
        }

        //Ordenar la data de forma descendiente o ascendente
        arrayData.sortByIndex(tableData, indexOrder != null ? indexOrder[0] : 1, arraySQL.get(2).toString().equals("DESC") ? false : true);

        List<String[]> searchResult = null;
        //Funcionalidad de busqueda de datos
        if (arraySQL.get(4) != null && arraySQL.get(5) != null) {

            String[] columns = arraySQL.get(4).toString().split(" ");

            SearchIndex indexs = new SearchIndex();
            indexOrderWhere = indexs.buscarIndices(data.toArray(new String[0]), columns);
            if (indexOrderWhere != null && indexOrderWhere.length > 0) {
                searchResult = arrayData.searchByValue(tableData, indexOrderWhere != null ? indexOrderWhere[0] : 1, arraySQL.get(5).toString());
            }
        } else {
            searchResult = tableData;
        }

        model = new DefaultTableModel(null, headers);
        if (searchResult != null) {
            if (exist) {
                for (String[] record : searchResult) {
                    model.addRow(record);
                }
            } else {
                // Agregar las filas de datos al modelo
                for (int i = 0; i < searchResult.size(); i++) {
                    String[] record = searchResult.get(i);
                    Object[] rowData = new Object[index.length];
                    for (int j = 0; j < index.length; j++) {
                        rowData[j] = record[index[j]];
                    }
                    model.addRow(rowData);
                }
            }

        }

        //Llenamos los datos a la tabla
        dataTable.setModel(model);

        dataTable.setDefaultEditor(Object.class, null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtQuery = new javax.swing.JTextArea();
        btnExecute = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        dataTable.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(dataTable);

        txtQuery.setBackground(new java.awt.Color(255, 255, 255));
        txtQuery.setColumns(20);
        txtQuery.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txtQuery.setForeground(new java.awt.Color(68, 78, 96));
        txtQuery.setRows(5);
        txtQuery.setBorder(null);
        jScrollPane2.setViewportView(txtQuery);

        btnExecute.setText("Ejecutar consulta");
        btnExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecuteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
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

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed
        executeQuery(txtQuery.getText());
    }//GEN-LAST:event_btnExecuteActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(SQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SQL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExecute;
    private javax.swing.JTable dataTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtQuery;
    // End of variables declaration//GEN-END:variables
}
