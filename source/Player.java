
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Thomas Meire
 */
public class Player {

	private int id;

	/** List of controlled planets */
	private TreeSet<Planet> planets;

	/** List of airborn fleets */
	private TreeSet<Fleet> fleets;

	public Player(int id) {
		this.id = id;
		planets = new TreeSet<Planet>();
		fleets = new TreeSet<Fleet>();
	}

	/**
	 * Check if a player is alive. A player is alive when he owns at least one
	 * planet or fleet. Otherwise, the player is deemed to be dead.
	 *
	 * @return true if this player is alive
	 */
	public boolean isAlive() {
		return !(planets.isEmpty() && fleets.isEmpty());
	}

	public void addPlanet(Planet planet) {
		planets.add(planet);
	}

	public Planet getStrongestPlanet() {
		try {
			return planets.last();
		} catch (NoSuchElementException nse) {
			return null;
		}
	}

	public Planet getWeakestPlanet() {
		try {
			return planets.first();
		} catch (NoSuchElementException nse) {
			return null;
		}
	}

	public void addFleet(Fleet fleet) {
		fleets.add(fleet);
	}

	public Set<Fleet> getFleets() {
		return fleets;
	}

}
