package library;

import java.util.ArrayList;
import java.util.List;

public class SearchIndex {

    
    /**
     * 
     * @param arreglo arreglos de datos
     * @param elementos Elemtos a buscar
     * @return Array de indices del elemento
     */
    public int[] buscarIndices(String[] arreglo, String[] elementos) {
        List<Integer> indicesEncontrados = new ArrayList<>();

        for (String elemento : elementos) {
            for (int i = 0; i < arreglo.length; i++) {
                if (elemento.toLowerCase().trim().equals(arreglo[i].toLowerCase().trim())) {
                    indicesEncontrados.add(i);
                    break; // Para pasar al siguiente elemento buscado
                }
            }
        }

        // Ordenar los índices de forma ascendente
        
//        Collections.sort(indicesEncontrados);

        // Convertir la lista de índices a un arreglo de enteros
        int[] resultado = new int[indicesEncontrados.size()];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = indicesEncontrados.get(i);
        }

        return resultado;
    }
}
