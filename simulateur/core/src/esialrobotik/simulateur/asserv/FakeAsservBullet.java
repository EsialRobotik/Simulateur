package esialrobotik.simulateur.asserv;

public class FakeAsservBullet extends Asserv {
	
	public static void main(String[] args) {
		String sequence = "g100#33\nc1g\nh\nr\ng100#45\ne115#46\nt30\nv45\nf45#36\np";
		Asserv fa = new FakeAsservBullet();
		boolean res = fa.decode_serie(sequence);
		System.out.println(res);
	}
}
