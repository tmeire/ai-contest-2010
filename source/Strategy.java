
/**
 *
 * @author Thomas Meire
 */
public class Strategy {

	/**
	 * The PlanetWars object contains the state of the game, including
	 * information about all planets and fleets that currently exist.
	 * Inside this function, you issue orders using the issueOrder() function.
	 * For example, to send 10 ships from planet 3 to planet 8,
	 * you would say issueOrder(3, 8, 10).
	 *
	 * There is already a basic strategy in place here. You can use it as a
	 * starting point, or you can throw it out entirely and replace it with
	 * your own. Check out the tutorials and articles on the contest website at
	 * http://www.ai-contest.com/resources.
	 */
	public void run(PlanetWars context) {
		Player neutral = context.getNeutralPlayer();
		Player player = context.getLocalPlayer();
		Player enemy  = context.getRemotePlayer();	// collection of all enemies

		// if we have no fleets up in the air, send one out
		if (player.getFleets().isEmpty()) {
			// send half of the capacity of our strongest planet
			// to the weakest planet of the enemy
			Planet src  = player.getStrongestPlanet();

			Planet enemyDest = enemy.getWeakestPlanet();
			Planet neutralDest = neutral.getWeakestPlanet();
			Planet dest = null;
			if (enemyDest == null && neutralDest != null) {
				dest = neutralDest;
			} else if (neutralDest == null && enemyDest != null) {
				dest = enemyDest;
			} else if (neutralDest != null && enemyDest != null) {
				dest = (neutralDest.getCapacity() < enemyDest.getCapacity()) ? neutralDest : enemyDest;
			}
			if (src != null && dest != null) {
				issueOrder(src, dest, src.getCapacity() / 2);
			}
		}
		finish();
	}

	/**
	 * Issue an order to send a fleet of given size from source to destination
	 * A few things to keep in mind:
	 *	- you can issue many orders per turn if you like.
	 *	- the planets are numbered starting at zero, not one.
	 *	- you must own the source planet. If you break this rule, the game
	 *	  engine kicks your bot out of the game instantly.
	 *	- you can't move more ships than are currently on the source planet.
	 *	- the ships will take a few turns to reach their destination. Travel is
	 *	  not instant. See the PlanetWars. Distance() function for more info.
	 * 
	 * @param source The source planet
	 * @param destination The destination planet
	 * @param size The size of the fleet
	 */
	private void issueOrder(Planet source, Planet dest, int size) {
		System.out.println("" + source.PlanetID() + " " + dest.PlanetID() + " " + size);
		System.out.flush();
	}

	private void finish() {
		System.out.println("go");
		System.out.flush();
	}
}
