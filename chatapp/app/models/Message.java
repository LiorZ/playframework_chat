package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Message extends Model {
	
	@ManyToOne
	public User sentBy;
	public Date whenSent;
	public String messageContent;
	
	public Message(User u, String msg) {
		sentBy = u;
		messageContent = msg;
		whenSent = new Date();
		save();
	}
	public static void addNewMessage(String content, User who) {
		Message m = new Message(who,content);
		m.save();
	}
	
	public static void addNewAdminMessage(String content) { 
		Message m = new AdminMessage(content);
		m.save();
	}
	public String toString(){
		return sentBy + ":"+ " " + messageContent + "\n";
	}
	public static String getMessagesByDateAsString(Date d) {
		System.out.println("looking for messages before ... " + d );
		List<Message> msgList = find("select m from Message m where m.whenSent > ?",d).fetch();
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
