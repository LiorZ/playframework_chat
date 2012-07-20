package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.*;

import javax.swing.text.DateFormatter;

import models.Message;
import models.User;
import play.mvc.Controller;
import play.mvc.Scope.Session;

public class Application extends Controller {
	private static String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss.SSS";
	
    public static void index() {
        render();
    }
    
    public static void enterChat(String username) {
    	User user = User.fetchUser(username);
    	if ( user == null )
    		user = User.createUser(username);
    	Session.current().put("username", username);
    	Session.current().put("lastMsg", getNowAsString());
    	Message.addNewAdminMessage("[" + username+ " entered the chat!]");
    	render("Application/chat.html",user);
    }

    private static String getNowAsString() {
    	DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    	return formatter.format(new Date());
    }
    
    public static void sendMessage(String message) {
    	User user = getCurrentUser();
    	Message.addNewMessage(message, user);
    	refreshMessages();
    }
    
    public static void refreshMessages() {
    	Date lastDate = new Date();
    	DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    	String lastMsg = Session.current().get("lastMsg");
    	Session.current().put("lastMsg", formatter.format(new Date()));
    	if ( lastMsg != null && !lastMsg.isEmpty() ) {
    		try {
				lastDate = formatter.parse(lastMsg);
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	String messages = Message.getMessagesByDateAsString(lastDate);
    	Map<String,Object> json = new HashMap<String,Object>();
    	json.put("messages", messages);
    	renderJSON(json);
    }
    
    
    private static User getCurrentUser() {
    	String username = Session.current().get("username");
    	User user = User.fetchUser(username);
    	return user;
    }
    
    public static void leaveChat() {
    	User currentUser = getCurrentUser();
    	if ( currentUser == null ) {
    		return; //leaving the chat anyway so no need to render anything .. 
    	}
    	Session.current().put("username", null);
    	Message.addNewAdminMessage("[" + currentUser + " has left the chat!]");
    }
    
    
}