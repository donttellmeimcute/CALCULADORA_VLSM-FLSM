import com.sun.net.httpserver.Request;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import jxl.Workbook;
import jxl.write.*;

class red_requisitos{
	String ip;
	int prefijo;
	String requisitos;
	List<Integer> ip_ordenada;
	List<Integer> req_or;

	public red_requisitos(String ip, int prefijo, String requisitos){
		this.ip = ip;
		this.prefijo = prefijo;
		this.requisitos = requisitos;
	}
	public int obtener_prefijo(){
		return prefijo;
	}

	public List<Integer> obtener_requisitos(){
		req_or = ordenar_ip_req(requisitos);
		return req_or;
	}

	public List<Integer> obtener_ip(){
		ip_ordenada = ordenar_ip_req(ip);
		return ip_ordenada;
	}
	
	public List<Integer> ordenar_ip_req(String ip_t){
        ip_t += ".";
        List<Integer> ip_ordenada = new ArrayList<>();
        String flota = "";
        for (int a = 0; a < ip_t.length(); a++){
            char cosa = ip_t.charAt(a);
            String s = Character.toString(cosa);
            if(s.equals(".") || s.equals("/") || s.equals(",")){
                int flota_2 = Integer.parseInt(flota);
                ip_ordenada.add(flota_2);
                flota = "";
                continue;
            }else{
                flota += cosa;
            }
        }
		return ip_ordenada;
	}

	
}

class ventana_vlsm{

}

class ventana_flsm{

}


public class main{
        public static void main(String[] args) throws Exception{
                Scanner entra = new Scanner(System.in);
                ventana_vlsm vlsm = new ventana_vlsm();
                ventana_flsm flsm = new ventana_flsm();
                red_requisitos p1 = new red_requisitos("192.168.32.0", 24, "6,30/3,6/1,2");
                red_requisitos p2 = new red_requisitos("10.20.30.0", 20, "6");
		System.out.println(p1.obtener_ip());
		System.out.println(p1.obtener_prefijo());
		System.out.println(p1.obtener_requisitos());
		System.out.println(p2.obtener_ip());
		System.out.println(p2.obtener_prefijo());
                System.out.println(p2.obtener_requisitos());

                // Crear archivo Excel con las subredes
                WritableWorkbook workbook = Workbook.createWorkbook(new File("subredes.xls"));
                WritableSheet sheet = workbook.createSheet("Subredes", 0);
                sheet.addCell(new Label(0, 0, "IP"));
                sheet.addCell(new Label(1, 0, "Prefijo"));
                sheet.addCell(new Label(2, 0, "Requisitos"));
                sheet.addCell(new Label(0, 1, p1.ip));
                sheet.addCell(new Number(1, 1, p1.obtener_prefijo()));
                sheet.addCell(new Label(2, 1, p1.requisitos));
                sheet.addCell(new Label(0, 2, p2.ip));
                sheet.addCell(new Number(1, 2, p2.obtener_prefijo()));
                sheet.addCell(new Label(2, 2, p2.requisitos));
                workbook.write();
                workbook.close();
        }
}
