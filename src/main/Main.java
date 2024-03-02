package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
     public static void main(String[] args) {
        String filePath = "CATALOG.DAT";
        String targetRecord = "UBIGEO";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inRecord = false;
            boolean foundRecord = false;
            String[] fields = null;
            ArrayList<String> columns = new ArrayList<>();
            ArrayList<String> realColumns = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("#")) {
                    if (foundRecord && fields != null) {
                        printFields(columns);
                    }
                    inRecord = true;
                    foundRecord = false;
                    fields = null;
                    columns.clear();  // Limpiar la lista antes de procesar el nuevo registro
                } else if (inRecord) {
                    fields = line.split("\\s+", 3);
                    
                    for (int i = 0; i < fields.length; i++) {
                        columns.add(fields[i].split(" ")[0]);  // Agregar elementos a la lista en lugar de reemplazarla
                        
                    }
                    if (fields.length >= 3 && fields[2].split(" ")[0].equals(targetRecord)) {
                        foundRecord = true;
                    }
                }
            }

            if (foundRecord && fields != null) {
                printFields(columns);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printFields(ArrayList<String> columns) {
         for (int i = 0; i < columns.size(); i += 3) {
            System.out.print(columns.get(i + 2) +" ");
        }
    }
}
