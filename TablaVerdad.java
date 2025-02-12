import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TablaVerdad {
    private static final Map<String, String> operadores = Map.of(
        "^", "&&",  // Conjunción
        "v", "||",  // Disyunción
        "⊕", "^",   // Disyunción excluyente
        "→", "<=",  // Condicional
        "↔", "=="   // Bicondicional
    );
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("*** Menú de opciones ***");
            System.out.println("1. Generar tabla de verdad");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            if (opcion == 1) {
                System.out.print("Ingrese las variables a utilizar (máx 3, separadas por comas): ");
                String[] variables = scanner.nextLine().replace(" ", "").split(",");
                if (variables.length > 3) {
                    System.out.println("Error: Solo se permiten hasta 3 variables.");
                    continue;
                }
                
                System.out.print("Ingrese las expresiones lógicas (máx 3, separadas por comas): ");
                String[] expresiones = scanner.nextLine().replace(" ", "").split(",");
                if (expresiones.length > 3) {
                    System.out.println("Error: Solo se permiten hasta 3 expresiones.");
                    continue;
                }
                
                generarTablaVerdad(variables, expresiones);
            } else if (opcion == 2) {
                System.out.println("Programa finalizado…");
                break;
            } else {
                System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }

    private static void generarTablaVerdad(String[] variables, String[] expresiones) {
        int n = variables.length;
        int filas = (int) Math.pow(2, n);
        
        System.out.println("| " + String.join(" | ", variables) + " | " + String.join(" | ", expresiones) + " |");
        System.out.println("-".repeat((variables.length + expresiones.length) * 5));
        
        for (int i = 0; i < filas; i++) {
            int[] valores = new int[n];
            for (int j = 0; j < n; j++) {
                valores[j] = (i >> (n - 1 - j)) & 1;
            }
            
            List<Integer> resultados = new ArrayList<>();
            for (String expresion : expresiones) {
                resultados.add(evaluarExpresion(expresion, variables, valores));
            }
            
            System.out.println("| " + Arrays.toString(valores).replaceAll("[\\[\\],]", "") + " | " + resultados.toString().replaceAll("[\\[\\],]", "") + " |");
        }
    }

    private static int evaluarExpresion(String expresion, String[] variables, int[] valores) {
        for (int i = 0; i < variables.length; i++) {
            expresion = expresion.replace(variables[i], String.valueOf(valores[i]));
        }
        for (Map.Entry<String, String> entry : operadores.entrySet()) {
            expresion = expresion.replace(entry.getKey(), entry.getValue());
        }
        return evaluar(expresion) ? 1 : 0;
    }

    private static boolean evaluar(String expresion) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        if (engine == null) {
            throw new RuntimeException("No se pudo encontrar el motor de JavaScript.");
        }
        try {
            return (boolean) engine.eval(expresion);
        } catch (ScriptException e) {
            e.printStackTrace();
            return false;
        }
    }
}