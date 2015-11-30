package pl.edu.agh.iaeste.praktykator;

/**
 * Created by Mateusz on 2015-11-30.
 */
public class Message {
    private String  subject,
                    sender,
                    attachmentFilename;

    public Message(String subject, String sender, String attachmentFilename) {
        this.subject = subject;
        this.sender = sender;
        this.attachmentFilename = attachmentFilename;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAttachmentFilename() {
        return attachmentFilename;
    }

    public void setAttachmentFilename(String attachmentFilename) {
        this.attachmentFilename = attachmentFilename;
    }


}
