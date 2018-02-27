package esialrobotik.simulateur.asserv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Asserv {

	/**
	 * Décode une séquence d'instructions séparées (ou non) par des retours à la ligne
	 * @param sequence à decoder
	 * @return true si s'est terminé sans erreur, false sinon
	 */
	public boolean decode_serie(String sequence) {
		boolean res = true;
		try {
			for (String code : sequence.split("\n")) {
				res &= decode(code);
			}
		} catch (Exception e) {
			System.err.println("Decode failed with error:" + e);
			res = false;
		}
		return res;
	}
	
	/**
	 * Décode un code envoyé sur la liaison serie en appelant les fonctions correspondantes avec les bons parametres
	 * @param code pour l'asserv
	 * @throws Exception si le match de regex echoue et/ou si la string ne peut être convertie en entier.
	 * @return true si le decodage a reussi, false sinon
	 */
	public boolean decode(String code) throws Exception {
		if (code.length() == 0) {
			// Decode is finished
			return true;
		}
		int x = 0;
		int y = 0;
		int d = 0;
		int a = 0;
		int s = 0;
		char r = 'g';
		Pattern p;
		Matcher m;
		char first_char = code.charAt(0);
		switch (first_char) {
		case '\n':
			// Ignore newlines
			code = code.substring(1, code.length());
		case 'g':
			p = Pattern.compile("^g(\\d+)#(\\d+)(.*)");
			m = p.matcher(code);
			m.find();
			x = Integer.parseInt(m.group(1));
			y = Integer.parseInt(m.group(2));
			goto_simple(x, y);
			code = m.group(3);
			break;
		case 'e':
			p = Pattern.compile("^e(\\d+)#(\\d+)(.*)");
			m = p.matcher(code);
			m.find();
			x = Integer.parseInt(m.group(1));
			y = Integer.parseInt(m.group(2));
			goto_enchaine(x, y);
			code = m.group(3);
			break;
		case 'v':
			p = Pattern.compile("^v(\\d+)(.*)");
			m = p.matcher(code);
			m.find();
			d = Integer.parseInt(m.group(1));
			avancer(d);
			code = m.group(2);
			break;
		case 't':
			p = Pattern.compile("^t(\\d+)(.*)");
			m = p.matcher(code);
			m.find();
			a = Integer.parseInt(m.group(1));
			tourner(a);
			code = m.group(2);
			break;
		case 'f':
			p = Pattern.compile("^f(\\d+)#(\\d+)(.*)");
			m = p.matcher(code);
			m.find();
			x = Integer.parseInt(m.group(1));
			y = Integer.parseInt(m.group(2));
			faire_face(x, y);
			code = m.group(3);
			break;
		case 'h':
			halt();
			code = code.substring(1, code.length());
			break;
		case 'r':
			reset_arret_urgence();
			code = code.substring(1, code.length());
			break;
		case 'c':
			p = Pattern.compile("^c(\\d+)([gp])(.*)");
			m = p.matcher(code);
			m.find();
			s = Integer.parseInt(m.group(1));
			r = m.group(2).charAt(0);
			calage_bordure(s, r);
			code = m.group(3);
			break;
		case 'p':
			position();
			code = code.substring(1, code.length());
			break;

		default:
			// Instruction inconnue
			return false;
		}
		return true && decode_serie(code);
	}
	
	/**
	 * Le robot se déplace au point de coordonnée (x, y).
	 * Il tourne vers le point, puis avance en ligne droite.
	 * L'angle est sans cesse corrigé pour bien viser le point voulu.
	 * @param x, coordonnees x en mm
	 * @param y, coordonnees y en mm
	 */
	public void goto_simple(int x, int y) {
		System.out.println("GOTO x: " + x + " y: "+y);
	}
	
	/**
	 * Idem que le Goto, sauf que lorsque le robot est proche du point d'arrivée (x, y),
	 * on s'autorise à enchaîner directement la consigne suivante si c'est un Goto ou un Goto enchaîné,
	 * sans marquer d'arrêt.
	 * @param x, coordonnees x en mm
	 * @param y, coordonnees y en mm
	 */
	public void goto_enchaine(int x, int y) {
		System.out.println("GOTOE x: " + x + " y: "+y);
	}
	
	/**
	 * Fait avancer le robot de d mm, tout droit
	 * @param d entier, en mm
	 */
	public void avancer(int d) {
		System.out.println("AVANCE d: " + d);
	}
	
	/**
	 * Fait tourner le robot de a degrées.
	 * Le robot tournera dans le sens trigonométrique : si a est positif, il tourne à gauche, et vice-versa.
	 * @param a entier, en degrées
	 */
	public void tourner(int a) {
		System.out.println("TOURNE a: " +a);
	}
	
	/**
	 * Fait tourner le robot pour être en face du point de coordonnées (x, y).
	 * En gros, ça réalise la première partie d'un Goto : on se tourne vers le point cible, mais on avance pas.
	 * @param x, coordonnees x en mm
	 * @param y, coordonnees y en mm
	 */
	public void faire_face(int x, int y) {
		System.out.println("FACE x: " + x + " y: "+y);
	}
	
	/**
	 * Arrêt d'urgence ! Le robot est ensuite systématiquement asservi à sa position actuelle.
	 * Cela devrait suffire à arrêter le robot correctement.
	 * La seule commande acceptée par la suite sera un Reset de l'arrêt d'urgence : toute autre commande sera ignorée.
	 */
	public void halt() {
		System.out.println("HALT");
	}
	
	/**
	 * Remet le robot dans son fonctionnement normal après un arrêt d'urgence.
	 * Les commandes en cours au moment de l'arrêt d'urgence NE sont PAS reprises.
	 * Si le robot n'est pas en arrêt d'urgence, cette commande n'a aucun effet.
	 */
	public void reset_arret_urgence() {
		System.out.println("RESET");
	}
	
	/**
	 * Effectue un calage bordure. Le robot doit être dans sa zone de départ au début du calage,
	 * dirigé vers la case de départ adverse en face de la table.
	 * Il doit être assez proche de la bordure derrière lui, et pas trop proche de la bordure sur le côté.
	 * A la fin du calage, le robot est prêt à partir pour un match dans sa case de départ.
	 * Le choix du robot est possible, si on veut que deux robots asservis concourent en même temps sur la même table,
	 * pour qu'ils puissent faire un calage bordure en même temps sans se rentrer dedans.
	 * @param s sens du calage bordure
	 * @param r robot ('g' : gros ; 'p' : petit)
	 */
	public void calage_bordure(int s, char r) {
		System.out.println("CALAGE s: " + s + " r: "+r);
	}
	
	/**
	 * Récupère la position et le cap du robot sur la connexion série, sous la forme "x%xy%ya%a\n",
	 * avec x, y, et a les coordonnées et l'angle du robot.
	 * @return position du robot
	 */
	public String position() {
		System.out.println("POSITION");
		return "";
	}
}
