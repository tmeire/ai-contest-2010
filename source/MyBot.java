
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;


/**
 * @author blackskad
 */
public class MyBot {

	public static void main(String[] args) {
		String line = "";
		StringBuilder message = new StringBuilder();

		try {
			// try to capture all output to stderr
			PrintStream err = new PrintStream("bot.err.txt");
			System.setErr(err);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while ((line = reader.readLine()) != null) {
				if (line.equals("go")) {
					PlanetWars pw = new PlanetWars(message.toString());
					Strategy ai = new Strategy();
					ai.run(pw);
					message = new StringBuilder();
				} else {
					// FIXME: use lists here to prevent unnecessairy splitting
					message.append(line);
					message.append("\n");
				}
			}
		} catch (Exception e) {
			// Oh crap, well, just skip this turn but write the error to logs for safety
			e.printStackTrace();
			System.out.println("go");
			System.out.flush();
		}
	}
}