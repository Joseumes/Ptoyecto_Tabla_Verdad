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

