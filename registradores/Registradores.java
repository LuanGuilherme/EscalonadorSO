package registradores;

public class Registradores {

	private int X;
	private int Y;

	public Registradores (int x, int y) {
		setX(x);
		setY(y);
	}

	public int getX () {
		return this.X;
	}

	public int getY () {
		return this.Y;
	}

	public void setX (int x) {
		this.X = x;
	}

	public void setY (int y) {
		this.Y = y;
	}

}