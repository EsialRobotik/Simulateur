package esialrobotik.simulateur.asserv;

public class FakeAsservCreator extends AsservCreator {
	
	@Override
	public Asserv createAsserv(AsservCreator.TypeAsserv type) {
		Asserv asserv = null;
		switch (type) {
		case BULLET:
			asserv = new FakeAsservBullet();
			break;
		
		case BOX2D:
			// TODO write FakeAsserv for Box2D if needed
			asserv = new FakeAsservBox2D();
			break;

		default:
			break;
		}
		return asserv;
	}

}
