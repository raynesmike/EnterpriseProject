package javafx.model;

public class GatewayException extends Exception {
	private static final long serialVersionUID = 1L;

	public GatewayException(Exception e) {
		super(e);
	}
	
	public GatewayException(String s) {
		super(s);
	}
}
