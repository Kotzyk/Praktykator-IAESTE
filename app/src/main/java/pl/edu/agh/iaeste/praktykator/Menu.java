package pl.edu.agh.iaeste.praktykator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Mateusz on 2015-11-29.
 */
        String protocol = "pop3";
        String host = "pop.gmail.com";
        String port = "995";
        String userName = "iaesteaghpraktyki";
        String password = "projektandroid";

        EmailReceiver receiver = new EmailReceiver();
        receiver.downloadEmails(protocol, host, port, userName, password);

public class Menu extends ListActivity{
 String pliki[] = {"MainActivity","dwa","trzy","cztery","piec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(Menu.this, android.R.layout.simple_list_item_1, pliki));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String cheese = pliki[position];
        try {
            Class klasa = Class.forName("pl.edu.agh.iaeste.praktykator."+cheese);
            Intent intent1 = new Intent(Menu.this, klasa);
            startActivity(intent1);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
