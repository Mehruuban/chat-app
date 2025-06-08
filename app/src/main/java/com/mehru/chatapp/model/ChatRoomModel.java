package com.mehru.chatapp.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {

    String chatRoomId;
    List<String> userIds;
    Timestamp lastMessageTimeStamp;
    String lastMessageSender;
    String lastMessage ;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId, List<String> userIds, Timestamp lastMessageTimeStamp, String lastMessageSender) {
        this.chatRoomId = chatRoomId;
        this.userIds = userIds;
        this.lastMessageTimeStamp = lastMessageTimeStamp;
        this.lastMessageSender = lastMessageSender;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimeStamp() {
        return lastMessageTimeStamp;
    }

    public void setLastMessageTimeStamp(Timestamp lastMessageTimeStamp) {
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public String getLastMessageSender() {
        return lastMessageSender;
    }

    public void setLastMessageSender(String lastMessageSender) {
        this.lastMessageSender = lastMessageSender;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
