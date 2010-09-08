
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// Contestants do not need to worry about anything in this file. This is just
// helper code that does the boring stuff for you, so you can focus on the
// interesting stuff. That being said, you're welcome to change anything in
// this file if you know what you're doing.
public class PlanetWars {
	// Constructs a PlanetWars object instance, given a string containing a
	// description of a game state.

	public PlanetWars(String gameStateString) {
		planets = new ArrayList<Planet>();
		fleets = new ArrayList<Fleet>();

		neutral = new Player(0);
		local = new Player(1);
		remote = new Player(2);

		ParseGameState(gameStateString);
	}

	// Returns the number of planets. Planets are numbered starting with 0.
	public int NumPlanets() {
		return planets.size();
	}

	// Returns the planet with the given planet_id. There are NumPlanets()
	// planets. They are numbered starting at 0.
	public Planet GetPlanet(int planetID) {
		return planets.get(planetID);
	}

	// Returns the number of fleets.
	public int NumFleets() {
		return fleets.size();
	}

	// Returns the fleet with the given fleet_id. Fleets are numbered starting
	// with 0. There are NumFleets() fleets. fleet_id's are not consistent from
	// one turn to the next.
	public Fleet GetFleet(int fleetID) {
		return fleets.get(fleetID);
	}

	// Returns a list of all the planets.
	public List<Planet> Planets() {
		return planets;
	}

	// Return a list of all the planets owned by the current player. By
	// convention, the current player is always player number 1.
	public List<Planet> MyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.Owner() == 1) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all neutral planets.
	public List<Planet> NeutralPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.Owner() == 0) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all the planets owned by rival players. This excludes
	// planets owned by the current player, as well as neutral planets.
	public List<Planet> EnemyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.Owner() >= 2) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all the planets that are not owned by the current
	// player. This includes all enemy planets and neutral planets.
	public List<Planet> NotMyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
			if (p.Owner() != 1) {
				r.add(p);
			}
		}
		return r;
	}

	// Return a list of all the fleets.
	public List<Fleet> Fleets() {
		List<Fleet> r = new ArrayList<Fleet>();
		for (Fleet f : fleets) {
			r.add(f);
		}
		return r;
	}

	// Return a list of all the fleets owned by the current player.
	public List<Fleet> MyFleets() {
		List<Fleet> r = new ArrayList<Fleet>();
		for (Fleet f : fleets) {
			if (f.Owner() == 1) {
				r.add(f);
			}
		}
		return r;
	}

	// Return a list of all the fleets owned by enemy players.
	public List<Fleet> EnemyFleets() {
		List<Fleet> r = new ArrayList<Fleet>();
		for (Fleet f : fleets) {
			if (f.Owner() != 1) {
				r.add(f);
			}
		}
		return r;
	}

	// Returns the distance between two planets, rounded up to the next highest
	// integer. This is the number of discrete time steps it takes to get
	// between the two planets.
	public int Distance(int sourcePlanet, int destinationPlanet) {
		Planet source = planets.get(sourcePlanet);
		Planet destination = planets.get(destinationPlanet);
		double dx = source.X() - destination.X();
		double dy = source.Y() - destination.Y();
		return (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));
	}

	// If the game is not yet over (ie: at least two players have planets or
	// fleets remaining), returns -1. If the game is over (ie: only one player
	// is left) then that player's number is returned. If there are no
	// remaining players, then the game is a draw and 0 is returned.
	public int Winner() {
		Set<Integer> remainingPlayers = new TreeSet<Integer>();
		for (Planet p : planets) {
			remainingPlayers.add(p.Owner());
		}
		for (Fleet f : fleets) {
			remainingPlayers.add(f.Owner());
		}
		switch (remainingPlayers.size()) {
			case 0:
				return 0;
			case 1:
				return ((Integer) remainingPlayers.toArray()[0]).intValue();
			default:
				return -1;
		}
	}

	// Returns the number of ships that the current player has, either located
	// on planets or in flight.
	public int NumShips(int playerID) {
		int numShips = 0;
		for (Planet p : planets) {
			if (p.Owner() == playerID) {
				numShips += p.getCapacity();
			}
		}
		for (Fleet f : fleets) {
			if (f.Owner() == playerID) {
				numShips += f.NumShips();
			}
		}
		return numShips;
	}

	/**
	 * While 'neutral' is not an active player (it doesn't send out ships), it
	 * does own some planets. We're keeping it here to manage those unowned planets.
	 */
	private Player neutral;

	/** The local player (i.e. this bot) */
	private Player local;

	/** The remote player, a collection of all enemies */
	private Player remote;

	public Player getNeutralPlayer() {
		return neutral;
	}

	public Player getLocalPlayer() {
		return local;
	}

	public Player getRemotePlayer() {
		return remote;
	}

	// Parses a game state from a string. On success, returns 1. On failure,
	// returns 0.
	private int ParseGameState(String s) {
		planets.clear();
		fleets.clear();
		int planetID = 0;
		String[] lines = s.split("\n");
		for (int i = 0; i < lines.length; ++i) {
			String line = lines[i];
			int commentBegin = line.indexOf('#');
			if (commentBegin >= 0) {
				line = line.substring(0, commentBegin);
			}
			if (line.trim().length() == 0) {
				continue;
			}
			String[] tokens = line.split(" ");
			if (tokens.length == 0) {
				continue;
			}
			if (tokens[0].equals("P")) {
				if (tokens.length != 6) {
					return 0;
				}
				double x = Double.parseDouble(tokens[1]);
				double y = Double.parseDouble(tokens[2]);
				int owner = Integer.parseInt(tokens[3]);
				int numShips = Integer.parseInt(tokens[4]);
				int growthRate = Integer.parseInt(tokens[5]);
				Planet p = new Planet(planetID++,
						owner,
						numShips,
						growthRate,
						x, y);
				planets.add(p);
				switch (owner) {
					case 0: /* 'neutral' player has id 0 */
						neutral.addPlanet(p);
						break;
					case 1: /* local player has id 1 */
						local.addPlanet(p);
						break;
					default: /* enemies have id 2 or larger */
						remote.addPlanet(p);
						break;
				}
			} else if (tokens[0].equals("F")) {
				if (tokens.length != 7) {
					return 0;
				}
				int owner = Integer.parseInt(tokens[1]);
				int numShips = Integer.parseInt(tokens[2]);
				int source = Integer.parseInt(tokens[3]);
				int destination = Integer.parseInt(tokens[4]);
				int totalTripLength = Integer.parseInt(tokens[5]);
				int turnsRemaining = Integer.parseInt(tokens[6]);
				Fleet f = new Fleet(owner,
						numShips,
						source,
						destination,
						totalTripLength,
						turnsRemaining);
				fleets.add(f);
				switch (owner) {
					case 0: /* 'neutral' player has id 0 */
						neutral.addFleet(f);
						break;
					case 1: /* local player has id 1 */
						local.addFleet(f);
						break;
					default: /* enemies have id 2 or larger */
						remote.addFleet(f);
						break;
				}
			} else {
				return 0;
			}
		}
		return 1;
	}

	// Loads a map from a text file. The text file contains a description of
	// the starting state of a game. See the project wiki for a description of
	// the file format. It should be called the Planet Wars Point-in-Time
	// format. On success, return 1. On failure, returns 0.
	private int LoadMapFromFile(String mapFilename) {
		String s = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(mapFilename));
			int c;
			while ((c = in.read()) >= 0) {
				s += (char) c;
			}
		} catch (Exception e) {
			return 0;
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				// Fucked.
			}
		}
		return ParseGameState(s);
	}

	// Store all the planets and fleets. OMG we wouldn't wanna lose all the
	// planets and fleets, would we!?
	private ArrayList<Planet> planets;

	private ArrayList<Fleet> fleets;

}
