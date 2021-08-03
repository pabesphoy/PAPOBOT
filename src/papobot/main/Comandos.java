package papobot.main;

import java.awt.Color;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.plaf.multi.MultiOptionPaneUI;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Comandos extends ListenerAdapter{
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		
		//System.out.println("Mensaje detectado: " + event.getMessage().getContentRaw());
		try{
			if(event.getMessage().getContentRaw().length() > 0 && event.getMessage().getContentRaw().charAt(0) == '*'){
				
				String comando = event.getMessage().getContentRaw();
				System.out.println("Comando detectado: " + comando.split(" ")[0]);
				//System.out.println("El comando " + comando + "tiene " + event.getMessage().getContentRaw().length() + " caracteres");
				
				switch (comando.split(" ")[0]) {
				case "*info":
					ejecutarInfo(event);break;
				case "*saluda":
					ejecutarSaluda(event);break;
				case "*roll":
					ejecutarRoll(event);break;
				case "*hungergames":
					ejecutarHG(event);break;
				case "*coins":
					ejecutarCoins(event);break;
				case "*claim":
					ejectuarClaim(event);break;
				case "*bet":
					ejecutarBet(event);break;
				default:
					event.getChannel().sendMessage("Comando inválido: " + comando).queue();break;
				}	
			}
			
		}catch (Exception e){
			System.err.println("Error al procesar comando: " + event.getMessage().getContentRaw());
			e.printStackTrace();
		}
	}
	
	
	//COMANDOS
	private void ejecutarBet(GuildMessageReceivedEvent event) throws IOException {
		Integer dineroApostado = Integer.valueOf(event.getMessage().getContentRaw().split(" ")[1]);
		Fila fila = BaseDeDatos.getFilaByNombre(event.getAuthor().getName());
		if(dineroApostado <= fila.getMonedas()) {
			Integer multiplicador = new Random().nextInt(201);
			Integer dineroDevuelto = (int) Math.round(dineroApostado*(multiplicador/100.0));
			System.out.println(String.format("dinero devuleto = apostado*(mult/100) || %d = %d * (%d/100)",dineroDevuelto,dineroApostado,multiplicador));
			System.out.println(multiplicador/100);
			
			System.out.println(String.format("Tenía %d, apostó %d y se le devolvieron %d, ahora tiene %d", fila.getMonedas(), dineroApostado, dineroDevuelto, fila.getMonedas() - dineroApostado + dineroDevuelto));
			fila.setMonedas(fila.getMonedas() - dineroApostado + dineroDevuelto);
			event.getChannel().sendMessage(String.format("%s ha invertido %d monedas y ha recuperado el %d por ciento del dinero. vaya maquina", fila.getNombre(), dineroApostado, multiplicador)).queue();
			BaseDeDatos.escribirFila(fila);
		}else {
			event.getChannel().sendMessage(String.format("No tienes %d monedas, %s. estas ties@ hermano.", dineroApostado, fila.getNombre())).queue();
			System.out.println(String.format("[DEBUG] No tienes %d monedas, %s. estas ties@ hermano.", dineroApostado, fila.getNombre()));
		}
	}
	private void ejectuarClaim(GuildMessageReceivedEvent event) throws IOException {
		
		Long horasDesdeClaim = Duration.between(BaseDeDatos.getFilaByNombre(event.getAuthor().getName()).getHoraClaim(), LocalDateTime.now()).toHours();
		
		if(horasDesdeClaim >= 5) {
			event.getChannel().sendMessage(String.format("A %s se le han añadido %d monedas", event.getAuthor().getName(), 50)).queue();
			
			Fila oldFila = BaseDeDatos.getFilaByNombre(event.getAuthor().getName());
			oldFila.setMonedas(oldFila.getMonedas() + 50);
			oldFila.setHoraClaim(LocalDateTime.now());
			
			BaseDeDatos.escribirFila(oldFila);
		}else {
			event.getChannel().sendMessage(String.format("ya has hecho el claim hace %d horas %s primo relajate", horasDesdeClaim, event.getAuthor().getName())).queue();
		}
	}
	
	private void ejecutarCoins(GuildMessageReceivedEvent event) throws IOException {
		Fila f = BaseDeDatos.getFilaByNombre(event.getAuthor().getName());
		event.getChannel().sendMessage(f.getNombre() + " tiene " + f.getMonedas() + " monedas 💰💰").queue();
		
	}

	private void ejecutarHG(GuildMessageReceivedEvent event) {
		List<Member> jugadores = new ArrayList<Member>(Main.jda.getGuilds().get(0).getMembers());
		jugadores.removeAll(Main.jda.getGuilds().get(0).getMembersByName("PAPÓBOT", true));
		
		String[] muertes = {
				" disparándole con una pistola",
				" cortándol@ en trocitos pequeños y comiéndoselos después",
				" obligándol@ a pasar una noche en mas mio que tuyo hasta que murio de ansiedad",
				" tirándol@ por un tobogan del bahía park",
				" quitándole las pastillas. No pudo soportarlo.",
				" a bocados.",
				" poniéndole videos de José Mota hasta que murió de risa",
				" metiéndole los dedos en los ojos.",
				" de una patada voladora.",
				" a puñetazo limpio.",
				" con un hacha.",
				" dándole con el manifiesto comunista en la crisma.",
				" meándole en el ojo.",
				" golpeándole con la cabeza de Pablo."
				};
		Random rand = new Random();
		
		while(jugadores.size() > 1) {
			Collections.shuffle(jugadores);
			event.getChannel().sendMessage(jugadores.get(0).getAsMention() + " ha asesinado a " + jugadores.get(1).getAsMention() + muertes[rand.nextInt(muertes.length)]).queue();			
			jugadores.remove(jugadores.get(1));
		}
		event.getChannel().sendMessage("El ganador es: " + jugadores.get(0).getAsMention()).queue();
	}

	private void ejecutarRoll(GuildMessageReceivedEvent event) {
		
		try {
			int r = (int) (Math.random() * (Integer.valueOf(event.getMessage().getContentRaw().split(" ")[1]) - 1)) + 1;
			System.out.println(r);
			event.getChannel().sendMessage("Ha salido............. " + r + "!").queue();
			
		} catch (Exception e) {
			event.getChannel().sendMessage("Error al procesar comando").queue();
			System.err.println("Error al procesar comando");
		}
		
		
	}

	public void ejecutarSaluda(GuildMessageReceivedEvent event) {
		
		event.getChannel().sendMessage("illo que pasa " + event.getAuthor().getAsMention()).queue();
		
	}

	@SuppressWarnings("deprecation")
	public void ejecutarInfo(GuildMessageReceivedEvent event) {
		
		EmbedBuilder info = new EmbedBuilder();
		info.setTitle("Comandos disponibles");
		info.setDescription("1) *info\n2) *saluda\n3) *roll (numero)\n4) *hungergames\n5) *coins");
		info.setColor(Color.BLUE);
		event.getChannel().sendMessage(info.build()).queue();
					
	}

}
