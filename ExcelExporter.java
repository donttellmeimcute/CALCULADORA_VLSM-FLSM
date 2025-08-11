import java.io.File;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Exporta una lista de subredes a un archivo Excel (.xls) utilizando la
 * biblioteca JXL.
 */
public class ExcelExporter {

    public static void exportar(List<VLSMCalculator.Subred> subredes, String ruta)
            throws Exception {
        WritableWorkbook libro = Workbook.createWorkbook(new File(ruta));
        WritableSheet hoja = libro.createSheet("Subredes", 0);

        // Encabezados
        String[] headers = {"IP Inicial", "Mascara", "IP Final", "Broadcast", "Hosts"};
        for (int i = 0; i < headers.length; i++) {
            hoja.addCell(new Label(i, 0, headers[i]));
        }

        // Datos
        for (int i = 0; i < subredes.size(); i++) {
            VLSMCalculator.Subred s = subredes.get(i);
            hoja.addCell(new Label(0, i + 1, s.ipInicial));
            hoja.addCell(new Label(1, i + 1, s.mascara));
            hoja.addCell(new Label(2, i + 1, s.ipFinal));
            hoja.addCell(new Label(3, i + 1, s.broadcast));
            hoja.addCell(new Label(4, i + 1, String.valueOf(s.hostsAsignados)));
        }

        libro.write();
        libro.close();
    }
}
