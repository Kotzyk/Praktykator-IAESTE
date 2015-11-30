package pl.edu.agh.iaeste.praktykator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

/**
 * Created by Mateusz on 2015-11-29.
 */

public class Menu extends ListActivity{
    String protocol = "pop3";
    String host = "pop.gmail.com";
    String port = "995";
    String usr = String.valueOf(R.string.userName);
    String pwd = String.valueOf(R.string.password);
    List<Message> maile = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            Properties properties = getServerProperties(protocol, host, port);
            Session session = Session.getDefaultInstance(properties);

            try {
                // connects to the message store
                Store store = session.getStore(protocol);
                store.connect(usr, pwd);

                // opens the inbox folder
                Folder folderInbox = store.getFolder("INBOX");
                folderInbox.open(Folder.READ_ONLY);

                // fetches new messages from server
                Message[] messages = folderInbox.getMessages();

                for (int i = 0; i < messages.length; i++) {
                    Message msg = messages[i];
                    Address[] fromAddress = msg.getFrom();
                    String from = fromAddress[0].toString();
                    String subject = msg.getSubject();
                    String toList = parseAddresses(msg
                            .getRecipients(javax.mail.Message.RecipientType.TO));
                    String ccList = parseAddresses(msg
                            .getRecipients(Message.RecipientType.CC));
                    String sentDate = msg.getSentDate().toString();

                    String contentType = msg.getContentType();
                    String messageContent = "";

                    if (contentType.contains("text/plain")
                            || contentType.contains("text/html")) {
                        try {
                            Object content = msg.getContent();
                            if (content != null) {
                                messageContent = content.toString();
                            }
                        } catch (Exception ex) {
                            messageContent = "[Error downloading content]";
                            ex.printStackTrace();
                        }
                    }
                }
                maile = Arrays.asList(messages);

                // disconnect
                folderInbox.close(false);
                store.close();
            } catch (NoSuchProviderException ex) {
                System.out.println("No provider for protocol: " + protocol);
                ex.printStackTrace();
            } catch (MessagingException ex) {
                System.out.println("Could not connect to the message store");
                ex.printStackTrace();
            }
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, maile));
        }

        private String parseAddresses(Address[] address) {
            String listAddress = "";

            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    listAddress += address[i].toString() + ", ";
                }
            }
            if (listAddress.length() > 1) {
                listAddress = listAddress.substring(0, listAddress.length() - 2);
            }

            return listAddress;
        }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

            Intent intent1 = new Intent();
            startActivity(intent1);

    }


    private Properties getServerProperties(String protocol, String host,
                                           String port) {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));

        return properties;
    }
}

