package com.shivshankar.classes;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationItem {
    private String NotificationCustBindId, mText, ImageUrl, Message;
    boolean unDoClicked = false;

    public NavigationItem(String bindId, String text, String strmessage, String strimageUrl, boolean undoClicked) {
        NotificationCustBindId = bindId;
        mText = text;
        Message = strmessage;
        ImageUrl = strimageUrl;
        unDoClicked = undoClicked;
    }


    public boolean isUnDoClicked() {
        return unDoClicked;
    }

    public void setUnDoClicked(boolean unDoClicked) {
        this.unDoClicked = unDoClicked;
    }

    public String getNotificationCustBindId() {
        return NotificationCustBindId;
    }

    public void setNotificationCustBindId(String notificationCustBindId) {
        NotificationCustBindId = notificationCustBindId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }
}
