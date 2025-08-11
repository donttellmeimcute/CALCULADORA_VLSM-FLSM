import java.util.*;

/**
 * Calculadora sencilla de VLSM.  A partir de una IP base y una lista de
 * requerimientos de hosts genera subredes de tamaño adecuado.
 */
public class VLSMCalculator {

    /** Representa una subred creada mediante VLSM. */
    public static class Subred {
        public final String ipInicial;
        public final String mascara;
        public final String ipFinal;
        public final String broadcast;
        public final int hostsAsignados;

        public Subred(String ipInicial, String mascara, String ipFinal,
                      String broadcast, int hostsAsignados) {
            this.ipInicial = ipInicial;
            this.mascara = mascara;
            this.ipFinal = ipFinal;
            this.broadcast = broadcast;
            this.hostsAsignados = hostsAsignados;
        }
    }

    /**
     * Valida una IP en formato decimal punteado.
     */
    public static boolean validarIP(String ip) {
        String regex =
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
        return ip != null && ip.matches(regex);
    }

    // Conversión entre IP y entero para facilitar cálculos.
    private static long ipToLong(String ip) {
        String[] octetos = ip.split("\\.");
        long resultado = 0;
        for (String o : octetos) {
            resultado = (resultado << 8) | Integer.parseInt(o);
        }
        return resultado;
    }

    private static String longToIp(long valor) {
        return String.format("%d.%d.%d.%d",
                (valor >> 24) & 0xFF,
                (valor >> 16) & 0xFF,
                (valor >> 8) & 0xFF,
                valor & 0xFF);
    }

    private static String prefixToMask(int prefijo) {
        int mask = 0xFFFFFFFF << (32 - prefijo);
        return String.format("%d.%d.%d.%d",
                (mask >> 24) & 0xFF,
                (mask >> 16) & 0xFF,
                (mask >> 8) & 0xFF,
                mask & 0xFF);
    }

    /**
     * Calcula las subredes necesarias mediante VLSM.
     */
    public static List<Subred> calcular(String ipBase, List<Integer> requerimientos) {
        if (!validarIP(ipBase)) {
            throw new IllegalArgumentException("IP base invalida");
        }

        // Ordenar requerimientos de mayor a menor
        List<Integer> hosts = new ArrayList<>(requerimientos);
        hosts.sort(Collections.reverseOrder());

        long actual = ipToLong(ipBase);
        List<Subred> resultado = new ArrayList<>();

        for (int req : hosts) {
            // bits de host necesarios (incluye red y broadcast)
            int bits = 0;
            while ((1L << bits) - 2 < req) {
                bits++;
            }
            int prefijo = 32 - bits;
            long tamanio = 1L << bits;

            long network = actual;
            long broadcast = actual + tamanio - 1;
            String mascara = prefixToMask(prefijo);
            String ipInicial = longToIp(network);
            String ipFinal = longToIp(broadcast - 1);
            String ipBroadcast = longToIp(broadcast);
            int capacidad = (int) (tamanio - 2);

            resultado.add(new Subred(ipInicial, mascara, ipFinal, ipBroadcast,
                    capacidad));

            actual = broadcast + 1; // siguiente subred
        }
        return resultado;
    }

    // Ejemplo de uso de la clase y conexión con el exportador a Excel
    public static void main(String[] args) throws Exception {
        String ipBase = "192.168.0.0";
        List<Integer> requerimientos = Arrays.asList(50, 20, 10);

        List<Subred> subredes = calcular(ipBase, requerimientos);

        // Exportar resultados
        ExcelExporter.exportar(subredes, "resultado.xls");
    }
}
