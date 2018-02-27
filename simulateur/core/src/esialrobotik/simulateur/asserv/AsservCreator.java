package esialrobotik.simulateur.asserv;

public abstract class AsservCreator {
	
	public abstract Asserv createAsserv(TypeAsserv type);
	// Types d'asserv possible
	public enum TypeAsserv 
	{
	    BULLET,
	    BOX2D
	}

}
