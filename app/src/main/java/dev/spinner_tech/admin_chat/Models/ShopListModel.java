package dev.spinner_tech.admin_chat.Models;

 public  class ShopListModel {
     String lastMessage ,merchantName , shopIdOrChatBoxId , shopLogo ,shopName ;
     long lastMessageTime ;

     public ShopListModel() {
     }

     public ShopListModel(String lastMessage, String merchantName, String shopIdOrChatBoxId, String shopLogo, String shopName, long lastMessageTime) {
         this.lastMessage = lastMessage;
         this.merchantName = merchantName;
         this.shopIdOrChatBoxId = shopIdOrChatBoxId;
         this.shopLogo = shopLogo;
         this.shopName = shopName;
         this.lastMessageTime = lastMessageTime;
     }

     public String getLastMessage() {
         return lastMessage;
     }

     public void setLastMessage(String lastMessage) {
         this.lastMessage = lastMessage;
     }

     public String getMerchantName() {
         return merchantName;
     }

     public void setMerchantName(String merchantName) {
         this.merchantName = merchantName;
     }

     public String getShopIdOrChatBoxId() {
         return shopIdOrChatBoxId;
     }

     public void setShopIdOrChatBoxId(String shopIdOrChatBoxId) {
         this.shopIdOrChatBoxId = shopIdOrChatBoxId;
     }

     public String getShopLogo() {
         return shopLogo;
     }

     public void setShopLogo(String shopLogo) {
         this.shopLogo = shopLogo;
     }

     public String getShopName() {
         return shopName;
     }

     public void setShopName(String shopName) {
         this.shopName = shopName;
     }

     public long getLastMessageTime() {
         return lastMessageTime;
     }

     public void setLastMessageTime(long lastMessageTime) {
         this.lastMessageTime = lastMessageTime;
     }
 }
