package papobot.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class BaseDeDatos {
	
	public static void main(String[] args) throws IOException {
		
	}
	
	

	
	public static void escribirFila(Fila f) throws IOException {
		File f1 = new File(".\\src\\data\\usuarios.txt");
		File f2 = new File(".\\src\\data\\usuariosAux.txt");
		BufferedReader reader = new BufferedReader(new FileReader(f1));
		BufferedWriter writer = new BufferedWriter(new FileWriter(f2));
		String lineaLeida;
		boolean habíaRegistro = false;
		while((lineaLeida = reader.readLine()) != null) {
			if(!new Fila(lineaLeida).getNombre().equals(f.getNombre())) {
				System.out.println("Escribiendo: " +lineaLeida);
				writer.write(lineaLeida + "\n");
			}else {
				habíaRegistro = true;
				writer.write(f.filaAsText());
			}
		}
		
		if(!habíaRegistro) {
			writer.write(f.filaAsText());
		}
		
		reader.close();
		writer.close();
		
		f1.delete();
		f2.renameTo(f1);
		
	}
	
	public static Fila getFilaByNombre(String nombre) throws IOException {
		File f1 = new File(".\\src\\data\\usuarios.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(f1));
		String lineaLeida;
		
		while((lineaLeida = reader.readLine()) != null) {
			if(new Fila(lineaLeida).getNombre().equals(nombre)) {
				System.out.println("ENCONTRADA FILA DE " + nombre);
				reader.close();
				return new Fila(lineaLeida);
			}else {
				System.out.println(new Fila(lineaLeida).getNombre() + " no corresponde a " + nombre);
			}
		}
		
		
		reader.close();
		escribirFila(new Fila(nombre, 0, LocalDateTime.of(2000, 1, 1, 1, 1)));
		return getFilaByNombre(nombre);
		
	}
	

}
