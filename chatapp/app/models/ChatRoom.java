package models;

import java.util.ArrayList;
import java.util.Date;
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
		save();
	}
	
	public User addUser(String username) {
		User user = User.createUser(username);
		currentUsers.add(user);
		save();
		return user;
	}
	
	public List<User> getActiveUsers() {
		return currentUsers;
	}

	public boolean userExists(String username){
		for (User u : currentUsers) {
			if ( u.username.equals(username) ) 
				return true;
		}
		return false;
	}
	
	public void addMessage(String msg, User u) {
		Message m = new Message(u, msg);
		messages.add(m);
		save();
	}
	
	public static ChatRoom getChatRoom(String name) {
		return find("byRoomName", name).first();
	}
	
	public void addNewAdminMessage(String content) { 
		Message m = new AdminMessage(content);
		m.save();
	}
	
	public String getMessagesByDateAsString(Date d) {
		System.out.println("looking for messages before ... " + d );
		List<Message> msgList = find("select m from Message m, ChatRoom c where m.whenSent > ? and c.id = ? and m IN elements(c.messages)",d,this.id).fetch();
		for (Message message : msgList) {
			System.out.println(message.whenSent);
		}
		StringBuilder builder = new StringBuilder();
		for (Message message : msgList) {
			builder.append(message);
		}
		return builder.toString();
	}
}
