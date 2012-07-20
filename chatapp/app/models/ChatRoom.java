package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class ChatRoom extends Model {
	@OneToMany
	public List<User> currentUsers;
	@OneToMany
	public List<Message> messages;
	
	public String roomName;
	
	public ChatRoom(String name) {
		currentUsers = new ArrayList<User>();
		messages = new ArrayList<Message>();
		roomName = name;
	}
	
	public void addUser(User u) {
		currentUsers.add(u);
		save();
	}
	
	public void addMessage(Message m) {
		messages.add(m);
		save();
	}
	
	public static ChatRoom getChatRoom(String name) {
		return find("byRoomName", name).first();
	}
	
	
	
}
