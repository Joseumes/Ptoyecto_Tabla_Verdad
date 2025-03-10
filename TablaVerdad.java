import java.util.*;

class ExpresionLogica {
    private static final Map<String, String> operadores = Map.of(
        "^", "&&",  // Conjunción (AND)
        "v", "||",  // Disyunción (OR)
        "⊕", "!=",  // XOR
        "→", "<=",  // Condicional (A implica B es equivalente a A <= B en lógica)
        "↔", "=="   // Bicondicional
    );

    public static boolean evaluar(String expresion) {
        try {
            return switch (expresion) {
                case "1&&1" -> true;
                case "1&&0", "0&&1", "0&&0" -> false;
                case "1||1", "1||0", "0||1" -> true;
                case "0||0" -> false;
                case "1!=1", "0!=0" -> false;
                case "1!=0", "0!=1" -> true;
                case "1<=1", "0<=1", "0<=0" -> true;
                case "1<=0" -> false;
                case "1==1", "0==0" -> true;
                case "1==0", "0==1" -> false;
                default -> throw new IllegalArgumentException("Expresión inválida: " + expresion);
            };
        } catch (Exception e) {
            System.out.println("Error al evaluar: " + expresion);
            return false;
        }
    }

    public static int evaluarExpresion(String expresion, String[] variables, int[] valores) {
        for (int i = 0; i < variables.length; i++) {
            expresion = expresion.replace(variables[i], String.valueOf(valores[i]));
        }

        for (Map.Entry<String, String> entry : operadores.entrySet()) {
            expresion = expresion.replace(entry.getKey(), entry.getValue());
        }

        return evaluar(expresion) ? 1 : 0;
    }
}

public class TablaVerdad {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                *** Menú de opciones ***
                1. Generar tabla de verdad
                2. Salir
                """);
            System.out.print("Seleccione una opción: ");
            int opcion = leerEntero(scanner);
            if (opcion == 1) {
                String[] variables = solicitarVariables(scanner);
                String[] expresiones = solicitarExpresiones(scanner, variables);

                if (!validarVariables(variables, expresiones) || !validarEstructuraExpresiones(expresiones)) {
                    continue; // Vuelve al menú principal si hay un error
                }

                generarTablaVerdad(variables, expresiones);
            } else if (opcion == 2) {
                System.out.println("Programa finalizado...");
                break;
            } else {
                System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }

    private static String[] solicitarVariables(Scanner scanner) {
        String[] variables;
        do {
            System.out.print("Ingrese las variables a utilizar (máx 3, separadas por comas): ");
            String input = scanner.nextLine().trim().replaceAll("\\s+", ""); // Elimina espacios
            variables = input.split(",");
            if (variables.length > 3 || Arrays.stream(variables).anyMatch(v -> v.length() != 1 || !Character.isLetter(v.charAt(0)))) {
                System.out.println("Error: Solo se permiten hasta 3 variables válidas (letras individuales).");
            }
        } while (variables.length > 3 || Arrays.stream(variables).anyMatch(v -> v.length() != 1 || !Character.isLetter(v.charAt(0))));
        return variables;
    }

    private static String[] solicitarExpresiones(Scanner scanner, String[] variables) {
        String[] expresiones;
        do {
            System.out.println("Operadores lógicos disponibles:");
            System.out.println("• Conjunción : Use '&&'' (ejemplo: p&&q)");
            System.out.println("• Disyunción : Use '||' (ejemplo: p||q)");
            System.out.println("• Disyunción excluyente: Use '!=' (ejemplo: p!=q)");
            System.out.println("• Condicional : Use '<=' (ejemplo: p<=q)");
            System.out.println("• Bicondicional : Use '==' (ejemplo: p==q)");
            System.out.println();
            System.out.println("Ingrese las expresiones lógicas utilizando los operadores anteriores.");
            System.out.println("Recuerde que solo se permiten estructuras básicas con un máximo de 2 variables.");
            System.out.println();
            System.out.print("Ingrese las expresiones lógicas (máx 3, separadas por comas): ");
            String input = scanner.nextLine().trim().replaceAll("\\s+", ""); // Elimina espacios
            expresiones = input.split(",");
            if (expresiones.length > 3) {
                System.out.println("Error: Solo se permiten hasta 3 expresiones.");
            }
        } while (expresiones.length > 3);
        return expresiones;
    }

    private static boolean validarVariables(String[] variables, String[] expresiones) {
        Set<String> variableSet = new HashSet<>(Arrays.asList(variables));
        for (String expresion : expresiones) {
            for (char c : expresion.toCharArray()) {
                if (Character.isLetter(c) && !variableSet.contains(String.valueOf(c))) {
                    System.out.println("Error: La variable '" + c + "' no fue ingresada previamente.");
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean validarEstructuraExpresiones(String[] expresiones) {
        for (String expresion : expresiones) {
            long variableCount = expresion.chars()
                                          .filter(Character::isLetter)
                                          .distinct()
                                          .count();
            if (variableCount > 2) {
                System.out.println("Error: La expresión '" + expresion + "' contiene más de 2 variables.");
                return false;
            }
        }
        return true;
    }

    private static void generarTablaVerdad(String[] variables, String[] expresiones) {
        int n = variables.length;
        int filas = (int) Math.pow(2, n);

        // Encabezado de la tabla
        System.out.print("| ");
        for (String var : variables) {
            System.out.printf("%-2s | ", var);
        }
        for (String expr : expresiones) {
            System.out.printf("%-6s | ", expr);
        }
        System.out.println("\n" + "-".repeat((variables.length * 5) + (expresiones.length * 8)));

        // Filas de la tabla
        for (int i = 0; i < filas; i++) {
            int[] valores = new int[n];
            for (int j = 0; j < n; j++) {
                valores[j] = (i >> (n - 1 - j)) & 1;
            }

            System.out.print("| ");
            for (int v : valores) {
                System.out.printf("%-2d | ", v);
            }

            for (String expresion : expresiones) {
                int resultado = ExpresionLogica.evaluarExpresion(expresion, variables, valores);
                System.out.printf("%-6d | ", resultado);
            }
            System.out.println();
        }
    }

    private static int leerEntero(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Ingrese un número entero: ");
            }
        }
    }
}