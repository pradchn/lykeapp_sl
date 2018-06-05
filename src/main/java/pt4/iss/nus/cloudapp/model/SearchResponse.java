package pt4.iss.nus.cloudapp.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
	private List<User> user= new ArrayList<>();

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}
}

