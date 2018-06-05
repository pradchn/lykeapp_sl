package pt4.iss.nus.cloudapp.model;

import java.util.ArrayList;
import java.util.List;

public class FeedResponse {
	private List<Picture> pics= new ArrayList<>();

	public List<Picture> getPics() {
		return pics;
	}

	public void setPics(List<Picture> pics) {
		this.pics = pics;
	}

}
