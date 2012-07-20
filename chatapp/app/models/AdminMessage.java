package models;

import javax.persistence.Entity;

@Entity
public class AdminMessage extends Message {

	public AdminMessage(String message) {
		super(null,message);
	}
	
	public String toString() {
		return messageContent + "\n";
	}
}
