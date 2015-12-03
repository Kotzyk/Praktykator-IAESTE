package pl.edu.agh.iaeste.praktykator;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

public class Menu extends ListActivity {
    String protocol = "imaps";
    String host = "imaps.gmail.com";
    String port = "993";
    String usr = "iaesteaghpraktyki@gmail.com";
    String pwd = "projektandroid";
    Message[] maile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Properties properties = getServerProperties(protocol, host, port);

        new EmailDownloader(this).execute(properties);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private Properties getServerProperties(String protocol, String host, String port) {
        Properties properties = new Properties();

        // user props
        properties.setProperty("username", usr);
        properties.setProperty("password",pwd);

        // server setting
        properties.setProperty("mail.store.protocol", protocol);
        properties.setProperty("mail.imaps.host", host);
        properties.setProperty("mail.imaps.port", port);

        // SSL setting
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");

        return properties;

    }

    private class EmailDownloader extends AsyncTask<Properties, Void, Message[]> {

        public Menu activity;

        public EmailDownloader(Menu menu) {
            this.activity = menu;
        }

        @Override
        protected Message[] doInBackground(Properties... arg0) {

            final Properties props = arg0[0];

            Session imapSession = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("username"), props.getProperty("password"));
                }
            });

            try {
                Store store = imapSession.getStore("imaps");
                //Connect to server by sending username and password.
                store.connect("imap.gmail.com", props.getProperty("username"), props.getProperty("password"));
                //Get all mails in Inbox Forlder
                Folder inbox = store.getFolder("Inbox");
                inbox.open(Folder.READ_ONLY);
                //Return result to array of message
                Message[] messages = inbox.getMessages();

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
                maile = messages.clone();

                // disconnect
                inbox.close(false);
                store.close();

            } catch (NoSuchProviderException ex) {
                Log.e("EmailDownloader", "No provider for protocol: " + protocol);
                ex.printStackTrace();
            } catch (MessagingException ex) {
                Log.e("EmailDownloader", "Could not connect to the message store");
                ex.printStackTrace();
            }

            return maile;
        }

        @Override
        protected void onPostExecute(Message[] results) {
            ArrayList<String> mailTitles = new ArrayList<>();
            for(Message mail : results){
                try {
                    mailTitles.add(mail.getSubject());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            activity.setListAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, mailTitles));

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

    }
}

