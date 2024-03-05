package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReadTableData {

    /**
     *
     * @param tableName Nombre de la tabla en el SM.DAT
     * @return Lista de datos de una tabla en especifico
     */
    public List<String[]> readTableData(String tableName) {
        List<String[]> tableData = new ArrayList<>();
        String code = "01";
        String tableParse = tableName.toUpperCase();
        switch (tableParse) {
            case "UBIGEO" ->
                code = "01";
            case "MONEDAS" ->
                code = "02";
            case "CURSOS" ->
                code = "03";
            case "ALUMNOS" ->
                code = "04";
            case "PROFESORES" ->
                code = "05";
            case "SEXO" ->
                code = "06";
            case "ESCUELA" ->
                code = "07";
            case "DEP_ACAD" ->
                code = "08";
            default ->
                throw new AssertionError();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("SM.DAT"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("# TABLA " + tableParse)) {
                    tableData.add(br.readLine().trim().split("\\s+"));
                } else if (line.startsWith("#") || !line.startsWith(code)) {
                    continue;
                } else {
                    List<String> data = new ArrayList<>();
                    StringBuilder currentPart = new StringBuilder();
                    boolean inQuotes = false;
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        if (c == '"') {
                            inQuotes = !inQuotes;
                        } else if (c == ' ' && !inQuotes) {
                            if (currentPart.length() > 0) {
                                data.add(currentPart.toString());
                                currentPart = new StringBuilder();
                            }
                        } else {
                            currentPart.append(c);
                        }
                    }
                    if (currentPart.length() > 0) {
                        data.add(currentPart.toString());
                    }
                    tableData.add(data.toArray(new String[0]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return tableData;
    }

    /**
     * Método para order de forma descendete o ascendente
     *
     * @param tableData lista de registros de la tabla
     * @param index indice de la colmuna a ordenar
     * @param ascending Si el orden es ascende o descendete
     */
    public static void sortByIndex(List<String[]> tableData, int index, boolean ascending) {
        Comparator<String[]> comparator = (arr1, arr2) -> arr1[index].compareTo(arr2[index]);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(tableData, comparator);
    }

    /**
     * Método para buscar un valor en el arreglo de datos
     *
     * @param tableData lista de registros de la tabla
     * @param index index a buscar
     * @param value Valor a buscar
     * @return
     */
    public static List<String[]> searchByValue(List<String[]> tableData, int index, String value) {
        List<String[]> result = new ArrayList<>();
        for (String[] record : tableData) {
            if (record[index].equalsIgnoreCase(value)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * *******************************************************************
     * METODOS NUEVOS
     ********************************************************************
     */
    
    /**
     * Método para convertir a mayusculas y minusculas
     *
     * @param tableData lista de registros de la tabla
     * @param indices indices a ordernar
     * @param toUpperCase si es uppercase o lowercase
     */
    public static void upperCaseAndLowerCase(List<String[]> tableData, int[] indices, boolean toUpperCase) {
        for (String[] row : tableData) {
            for (int index : indices) {
                String value = row[index];
                row[index] = toUpperCase ? value.toUpperCase() : value.toLowerCase();
            }
        }
    }

    /**
     * Método para reemplazar por la longitud en indices especificados
     *
     * @param tableData
     * @param indices
     */
    public static void replaceWithLength(List<String[]> tableData, int[] indices) {
        for (String[] row : tableData) {
            for (int index : indices) {
                String value = row[index];
                row[index] = String.valueOf(value.length());
            }
        }
    }

//    public static void main(String[] args) {
//        ReadTableData d = new ReadTableData();
//        List<String[]> tableData = d.readTableData("ubigeo");
//
//        int[] index = {2};
////        upperCaseAndLowerCase(tableData, index, true);
//        replaceWithLength(tableData, index);
//
//        for (String[] record : tableData) {
//            System.out.print("{");
//            for (int i = 0; i < record.length; i++) {
//                System.out.print(record[i]);
//                if (i < record.length - 1) {
//                    System.out.print(",");
//                }
//                if (i == record.length - 1) {
//                    System.out.println("}");
//                } else {
//                    System.out.print(" ");
//                }
//            }
//        }
//
//    }
}
