package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	
	public String username;
	public Date joined;
	
	public User(String uname) {
		username=uname;
		joined = new Date();
	}
	
	public String toString() {
		return username;
	}
	public static User createUser(String username) {
		User u = new User(username);
		u.save();
		return u;
	}
	
	public static User fetchUser(String username){ 
		List<User> userList = User.find("byUsername", username).fetch(1);
		if ( userList == null || userList.isEmpty() )
			return null;
		User user = userList.get(0);
		return user;
	}
}
