package hackathon.util;

public class Alert {

	// -------
	// Champs
	// -------

	private Color	color;
	private String	html;

	// -------
	// Constructeurs
	// -------

	public Alert( Color color, String html ) {
		this.color = color;
		this.html = html;
	}

	public Alert( String html ) {
		this( Color.SECONDARY, html );
	}

	// -------
	// Getters
	// -------

	public String getColor() {
		return color.getName();
	}

	public String getHtml() {
		return html;
	}

	// -------
	// Types auxiliaires
	// -------

	public static enum Color {
		PRIMARY, SECONDARY, SUCCESS, DANGER, WARNING, INFO, LIGHT, DARK;

		public String getName() {
			return this.name().toLowerCase();
		}

	};
}
