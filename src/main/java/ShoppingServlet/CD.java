package ShoppingServlet;

public class CD {

	String album;
	String artist;
	String country;
	float price;
	int quantity;

	public CD() {
		album = "";
		artist = "";
		country = "";
		price = 0;
		quantity = 0;
	}

	public void setAlbum(String title) {
		album = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setArtist(String group) {
		artist = group;
	}

	public String getArtist() {
		return artist;
	}

	public void setCountry(String cty) {
		country = cty;
	}

	public String getCountry() {
		return country;
	}

	public void setPrice(float p) {
		price = p;
	}

	public float getPrice() {
		return price;
	}

	public void setQuantity(int q) {
		quantity = q;
	}

	public int getQuantity() {
		return quantity;
	}
}