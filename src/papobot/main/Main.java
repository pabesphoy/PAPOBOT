package papobot.main;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class Main {
	
	public static JDA jda;
	public static String token = "ODcxODUxNjUzNjQ3NzI4Njcy.YQhVIA.OYochv8Buc9-sh-VuW7SV-jMbrY";

	public static void main(String[] args) throws LoginException {

		jda = JDABuilder.createDefault(token).build();
		jda.getPresence().setStatus(OnlineStatus.ONLINE);
		jda.getPresence().setActivity(Activity.of(ActivityType.LISTENING, "a la madre de Alejandro gemir"));
		jda.addEventListener(new Comandos());
	}
}