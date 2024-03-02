package library;

import java.util.ArrayList;

public class InterpretSQl {

    // * CODIGO PARA INTERPRETAR CODIGO SQL
    public static ArrayList<String> interpretSQL(String sql) {

        ArrayList<String> data = new ArrayList<String>();
        // Eliminar espacios en blanco extra y convertir a minúsculas
        if (!sql.trim().toLowerCase().startsWith("select")) {
        System.out.println("Error: La consulta debe comenzar con SELECT");
        return null;
    }

    // Encontrar la posición de la palabra "from" (ignorando mayúsculas/minúsculas)
    int fromIndex = sql.trim().toLowerCase().indexOf("from");

    // Verificar si "from" se encuentra en la consulta
    if (fromIndex < 0) {
        System.out.println("Error: Consulta incompleta");
        return null;
    }

    // Obtener las columnas y la tabla
    String columns = sql.substring(6, fromIndex).trim();
    String table = sql.substring(fromIndex + 4).trim();

    // Agregar las columnas y la tabla a la lista
    data.add(columns);
    data.add(table);

    return data;
    }

}
