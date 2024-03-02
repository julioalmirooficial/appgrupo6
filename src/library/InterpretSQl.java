package library;

import java.util.ArrayList;

public class InterpretSQl {

    public static ArrayList<String> interpretSQL(String sql) {
        ArrayList<String> data = new ArrayList<String>();
        // Eliminar espacios en blanco extra y convertir a minúsculas
        String lowerCaseSql = sql.trim().toLowerCase();
        if (!lowerCaseSql.startsWith("select")) {
            System.out.println("Error: La consulta debe comenzar con SELECT");
            return null;
        }

        // Encontrar la posición de la palabra "from" (ignorando mayúsculas/minúsculas)
        int fromIndex = lowerCaseSql.indexOf("from");

        // Verificar si "from" se encuentra en la consulta
        if (fromIndex < 0) {
            System.out.println("Error: Consulta incompleta");
            return null;
        }

        // Obtener las columnas y la tabla
        String columns = sql.substring(6, fromIndex).trim();
        // Obtener el índice después de "FROM"
        int afterFromIndex = fromIndex + 4;
        // Obtener el índice de "ORDER BY" (si existe)
        int orderByIndex = lowerCaseSql.indexOf("order by");
        String table;
        if (orderByIndex >= 0) {
            table = sql.substring(afterFromIndex, orderByIndex).trim();
        } else {
            table = sql.substring(afterFromIndex).trim();
        }

        // Agregar las columnas y la tabla a la lista
        data.add(columns);
        data.add(table);

        // Verificar si la consulta incluye un ORDER BY DESC
        String order = lowerCaseSql.contains("desc") ? "DESC" : "ASC";
        data.add(order);

        return data;
    }

}
