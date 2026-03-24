package hackathon.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Photos {
	
	private Random random = new Random();

	public String getPath() {
		return String.format(  "/img/photos/photo%02d.jpg", random.nextInt(23) + 1 );
	}

	public String getPath2() {
		return String.format(  "/img/photos/photo%02d.jpg", random.nextInt(13) + 30 );
	}

	public String getPath3() {
		return String.format(  "/img/photos/photo%02d.jpg", random.nextInt(4) + 50 );
	}
	
	
}
