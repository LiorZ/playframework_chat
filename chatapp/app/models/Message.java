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
	
	public String toString(){
		return sentBy + ":"+ " " + messageContent + "\n";
	}

}
