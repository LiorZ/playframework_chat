package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.*;

import javax.swing.text.DateFormatter;

import models.ChatRoom;
import models.Message;
import models.User;
import play.mvc.Controller;
import play.mvc.Scope.Session;

public class Application extends Controller {
	private static String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss.SSS";
	
    public static void index() {
        render();
    }
    
    public static void enterChat(String username, String roomName) {
    	ChatRoom room = ChatRoom.getChatRoom(roomName);
    	if ( room == null ) {
    		room = new ChatRoom(roomName);
    	}
    	if ( room.userExists(username) ){
    		//that's bad ..
    	}else {
    		room.addUser(username);
    	}
    	Session.current().put("roomName", roomName);
    	Session.current().put("username", username);
    	Session.current().put("lastMsg", getNowAsString());
    	room.addNewAdminMessage("[" + username+ " entered the chat!]");
    	render("Application/chat.html",username);
    }

    private static String getNowAsString() {
    	DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    	return formatter.format(new Date());
    }
    
    public static void sendMessage(String message) {
    	User user = getCurrentUser();
    	ChatRoom chatRoom = getCurrentChatRoom();
    	chatRoom.addMessage(message, user);
    	refreshMessages();
    }
    
    public static void refreshMessages() {
    	String lastMsg = Session.current().get("lastMsg");
    	ChatRoom chatRoom = getCurrentChatRoom();
    	
    	Date lastDate = new Date();
    	DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    	Session.current().put("lastMsg", formatter.format(new Date()));
    	if ( lastMsg != null && !lastMsg.isEmpty() ) {
    		try {
				lastDate = formatter.parse(lastMsg);
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	String messages = chatRoom.getMessagesByDateAsString(lastDate);
    	Map<String,Object> json = new HashMap<String,Object>();
    	json.put("messages", messages);
    	renderJSON(json);
    }
    
    
    private static User getCurrentUser() {
    	String username = Session.current().get("username");
    	User user = User.fetchUser(username);
    	return user;
    }
    
    private static ChatRoom getCurrentChatRoom() {
    	String roomName = Session.current().get("roomName");
    	ChatRoom chatRoom = ChatRoom.getChatRoom(roomName);
    	return chatRoom;
    }
    
    public static void leaveChat() {
    	ChatRoom chatRoom = getCurrentChatRoom();
    	User currentUser = getCurrentUser();
    	if ( currentUser == null || chatRoom == null ) {
    		return; //leaving the chat anyway so no need to render anything .. 
    	}
    	Session.current().put("username", null);
    	chatRoom.addNewAdminMessage("[" + currentUser + " has left the chat!]");
    }
    
    
}