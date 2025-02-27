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
                String[] variables;
                do {
                    System.out.print("Ingrese las variables a utilizar (máx 3, separadas por comas): ");
                    variables = scanner.nextLine().replace(" ", "").split(",");
                    if (variables.length > 3) {
                        System.out.println("Error: Solo se permiten hasta 3 variables.");
                    }
                } while (variables.length > 3);
                String[] expresiones;
                do {
                    System.out.print("Ingrese las expresiones lógicas (máx 3, separadas por comas): ");
                    expresiones = scanner.nextLine().replace(" ", "").split(",");
                    if (expresiones.length > 3) {
                        System.out.println("Error: Solo se permiten hasta 3 expresiones.");
                    }
                } while (expresiones.length > 3);

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
    private static void generarTablaVerdad(String[] variables, String[] expresiones) {
        int n = variables.length;
        int filas = (int) Math.pow(2, n);

        System.out.print("| ");
        for (String var : variables) {
            System.out.print(var + " | ");
        }
        for (String expr : expresiones) {
            System.out.print(expr + " | ");
        }
        System.out.println("\n" + "-".repeat((variables.length + expresiones.length) * 6));

        for (int i = 0; i < filas; i++) {
            int[] valores = new int[n];
            for (int j = 0; j < n; j++) {
                valores[j] = (i >> (n - 1 - j)) & 1;
            }

            System.out.print("| ");
            for (int v : valores) {
                System.out.print(v + " | ");
            }

            for (String expresion : expresiones) {
                int resultado = ExpresionLogica.evaluarExpresion(expresion, variables, valores);
                System.out.print(resultado + " | ");
            }
            System.out.println();
        }
    }

    private static int leerEntero(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Ingrese un número entero: ");
            }
        }
    }
    private static void generarTablaVerdad(String[] variables, String[] expresiones) {
        int n = variables.length;
        int filas = (int) Math.pow(2, n);

        System.out.print("| ");
        for (String var : variables) {
            System.out.print(var + " | ");
        }
        for (String expr : expresiones) {
            System.out.print(expr + " | ");
        }
        System.out.println("\n" + "-".repeat((variables.length + expresiones.length) * 6));

        for (int i = 0; i < filas; i++) {
            int[] valores = new int[n];
            for (int j = 0; j < n; j++) {
                valores[j] = (i >> (n - 1 - j)) & 1;
            }

            System.out.print("| ");
            for (int v : valores) {
                System.out.print(v + " | ");
            }

            for (String expresion : expresiones) {
                int resultado = ExpresionLogica.evaluarExpresion(expresion, variables, valores);
                System.out.print(resultado + " | ");
            }
            System.out.println();
        }
    }

    private static int leerEntero(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Ingrese un número entero: ");
            }
        }
    }
}


