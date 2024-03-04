package library;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        // Obtener las columnas y la tabla utilizando una expresión regular
        Pattern pattern = Pattern.compile("select\\s+(.*?)\\s+from\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        String columns = "";
        String table = "";
        if (matcher.find()) {
            columns = matcher.group(1).trim().replace(",", " ");
            table = matcher.group(2).trim();
        }

        // Obtener el índice de "ORDER BY" (si existe)
        int orderByIndex = lowerCaseSql.indexOf("order by");

        String orderByColumn = "null";
        if (orderByIndex >= 0) {
            int orderByStartIndex = orderByIndex + 8; // Longitud de "order by"
            int orderByEndIndex = lowerCaseSql.length();
            int descIndex = lowerCaseSql.indexOf(" desc", orderByStartIndex);
            int ascIndex = lowerCaseSql.indexOf(" asc", orderByStartIndex);
            if (descIndex >= 0 && descIndex < orderByEndIndex) {
                orderByEndIndex = descIndex;
            }
            if (ascIndex >= 0 && ascIndex < orderByEndIndex) {
                orderByEndIndex = ascIndex;
            }
            orderByColumn = sql.substring(orderByStartIndex, orderByEndIndex).trim();
        }

        // Agregar las columnas, la tabla y los demás datos a la lista
        data.add(columns);
        data.add(table);

        // Verificar si la consulta incluye un ORDER BY DESC
        String order = lowerCaseSql.contains("desc") ? "DESC" : "ASC";
        data.add(order);
        data.add(orderByColumn);
        data.add(obtenerNombre(sql));
        data.add(obtenerValor(sql));
        System.out.println(data);
        return data;
    }

    public static String obtenerNombre(String query) {
        Pattern pattern = Pattern.compile("where\\s+(\\w+)\\s*=\\s*'(.+?)'", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String obtenerValor(String query) {
        Pattern pattern = Pattern.compile("where\\s+\\w+\\s*=\\s*'(.+?)'", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
