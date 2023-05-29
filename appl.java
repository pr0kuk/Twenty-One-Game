import java.applet.*;
import java.awt.*;

public class appl extends Applet {
    public static Game can;
	public void init() {
		can = new Game();
        add(can);
	}
}