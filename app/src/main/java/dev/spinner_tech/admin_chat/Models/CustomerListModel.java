package dev.spinner_tech.admin_chat.Models;

 public  class CustomerListModel {
     String lastMessage, customerName, customerIdOrChatBoxId, customerImage;
     long lastMessageTime;

     public CustomerListModel() {
     }

     public CustomerListModel(String lastMessage, String customerName, String customerIdOrChatBoxId, String customerImage, long lastMessageTime) {
         this.lastMessage = lastMessage;
         this.customerName = customerName;
         this.customerIdOrChatBoxId = customerIdOrChatBoxId;
         this.customerImage = customerImage;
         this.lastMessageTime = lastMessageTime;
     }

     public String getLastMessage() {
         return lastMessage;
     }

     public void setLastMessage(String lastMessage) {
         this.lastMessage = lastMessage;
     }

     public String getCustomerName() {
         return customerName;
     }

     public void setCustomerName(String customerName) {
         this.customerName = customerName;
     }

     public String getCustomerIdOrChatBoxId() {
         return customerIdOrChatBoxId;
     }

     public void setCustomerIdOrChatBoxId(String customerIdOrChatBoxId) {
         this.customerIdOrChatBoxId = customerIdOrChatBoxId;
     }

     public String getCustomerImage() {
         return customerImage;
     }

     public void setCustomerImage(String customerImage) {
         this.customerImage = customerImage;
     }

     public long getLastMessageTime() {
         return lastMessageTime;
     }

     public void setLastMessageTime(long lastMessageTime) {
         this.lastMessageTime = lastMessageTime;
     }
 }