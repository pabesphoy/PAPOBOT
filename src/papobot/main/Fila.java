package papobot.main;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fila{
	private String nombre;
	private Integer monedas;
	private LocalDateTime horaClaim;
	
	public String getNombre() {
		return nombre;
	}
	public Integer getMonedas() {
		return monedas;
	}
	public LocalDateTime getHoraClaim() {
		return horaClaim;
	}
	
	public void setMonedas(Integer monedas) {
		this.monedas = monedas;	
	}
	
	public void setHoraClaim(LocalDateTime horaClaim) {
		this.horaClaim = horaClaim;
	}
	
	public Fila(String linea) {
		String[] atributos = linea.split("#");
		this.nombre = atributos[0].replace("nombre:", "");
		this.monedas = Integer.valueOf(atributos[1].replace("monedas:", ""));
		this.horaClaim = stringToTime(atributos[2].replace("horaclaim:", ""));
		
	}
	
	public Fila(String nombre, Integer monedas, LocalDateTime horaClaim) {
		this.nombre = nombre;
		this.monedas = monedas;
		this.horaClaim = horaClaim;
	}
	
	public String filaAsText() {
		return String.format("nombre:%s#monedas:%s#horaclaim:%s\n", this.nombre, this.monedas,this.horaClaim);
	}
	
	
	private static LocalDateTime stringToTime(String time){	//Devuelve año-mes-dia-horas-minutos-segundos
		
		List<Integer> res = new ArrayList<Integer>();
		
		String dia = time.split("T")[0];
		String hora = time.split("T")[1];
		for(String num : dia.split("-")) {
			res.add(Integer.valueOf(num));
		}
		res.add(Integer.valueOf(hora.split(":")[0]));
		res.add(Integer.valueOf(hora.split(":")[1]));
		
		return LocalDateTime.of(res.get(0), res.get(1), res.get(2), res.get(3), res.get(4));
	}

	
	public static void main(String[] args) {
		System.out.println(110/100.0);
	}
}