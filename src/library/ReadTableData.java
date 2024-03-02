package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadTableData {

    public List<String[]> readTableData(String tableName) {
        List<String[]> tableData = new ArrayList<>();
        String code = "01";
        switch (tableName) {
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
                if (line.equalsIgnoreCase("# TABLA " + tableName)) {
                    tableData.add(br.readLine().trim().split("\\s+"));
                } else if (line.startsWith("#") || !line.startsWith(code)) {
                    continue;
                } else {
                    tableData.add(line.split("\\s+"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return tableData;
    }

}
