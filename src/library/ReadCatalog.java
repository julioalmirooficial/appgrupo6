package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCatalog {

    public ArrayList<String> catalog(String catalog) {
        try (BufferedReader br = new BufferedReader(new FileReader("CATALOG.DAT"))) {
            String line;
            boolean inRecord = false;
            boolean foundRecord = false;
            String[] fields = null;
            ArrayList<String> columns = new ArrayList<>();
            ArrayList newColmuns = new ArrayList();

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("#")) {
                    if (foundRecord && fields != null) {
                        for (int i = 0; i < columns.size(); i += 3) {
                    newColmuns.add(columns.get(i + 2));
                }
                        return newColmuns;
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
                    if (fields.length >= 3 && fields[2].split(" ")[0].equals(catalog)) {
                        foundRecord = true;
                    }
                }
            }
            if (foundRecord && fields != null) {

                for (int i = 0; i < columns.size(); i += 3) {
                    newColmuns.add(columns.get(i + 2));
                }
                return newColmuns;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
