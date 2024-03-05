package library;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class InterpretSQl {

    public static ArrayList<String> interpretSQL(String sql) {
        String sqlAlter = sql;
        sql = sql.replaceAll("(length|upper|lower)\\((.*?)\\)", "$2");
        
        
        // Verificar si después de una coma biene mas comas seguidos
        Pattern patternVerifyErrorComa = Pattern.compile(",,");
        Matcher matcherVerifyErrorComa = patternVerifyErrorComa.matcher(sqlAlter);
        if (matcherVerifyErrorComa.find()) {
            JOptionPane.showMessageDialog(null, "Por favor ingresa una sentencias valida,\n no puedes ingresar [,,] seguidos.","ERROR",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        
        // Verificar si después de una coma viene un asterisco
        Pattern patternVerifyErrorAsterisk = Pattern.compile(",\\s*\\*");
        Matcher matcherVerifyErrorAsterisk = patternVerifyErrorAsterisk.matcher(sqlAlter);
        if (matcherVerifyErrorAsterisk.find()) {
            JOptionPane.showMessageDialog(null, "Por favor ingresa una sentencias valida","ERROR",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        

        // Obtener las funciones separadas por coma
        // Inicializar la variable functions como null
        String functions = null;
        Pattern patternFunctions = Pattern.compile("(length|upper|lower)");
        Matcher matcherFunctions = patternFunctions.matcher(sqlAlter);
        StringBuilder functionsBuilder = new StringBuilder();
        while (matcherFunctions.find()) {
            if (functions == null) {
                functions = "";
            }
            functionsBuilder.append(matcherFunctions.group()).append(" ");
        }
        // Asignar el valor solo si se encontraron funciones
        if (functionsBuilder.length() > 0) {
            functions = functionsBuilder.toString().replaceAll(",$", "");
        }

        // Inicializar la variable FunctionsValues como null
        String FunctionsValues = null;

        // Obtener los valores dentro de los paréntesis separados por comas
        Pattern patternFunctionsValues = Pattern.compile("\\((.*?)\\)");
        Matcher matcherFunctionsValues = patternFunctionsValues.matcher(sqlAlter);
        StringBuilder valuesBuilder = new StringBuilder();
        while (matcherFunctionsValues.find()) {
            if (FunctionsValues == null) {
                FunctionsValues = "";
            }
            valuesBuilder.append(matcherFunctionsValues.group(1)).append(" ");
        }

        // Asignar el valor solo si se encontraron valores
        if (valuesBuilder.length() > 0) {
            FunctionsValues = valuesBuilder.toString().replaceAll(",$", "");
        }
        
        
        
        

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
        data.add(columns); //Indice 0
        data.add(table); //Indice 1

        // Verificar si la consulta incluye un ORDER BY DESC
        String order = lowerCaseSql.contains("desc") ? "DESC" : "ASC";
        data.add(order); //Indice 2
        data.add(orderByColumn); //Indice 3
        data.add(obtenerNombre(sql)); //Indice 4
        data.add(obtenerValor(sql)); //Indice 5
        data.add(functions); //Indice 6 nombre de las funcionas
        data.add(FunctionsValues); //Indice  7 valor de las funciones
        
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
