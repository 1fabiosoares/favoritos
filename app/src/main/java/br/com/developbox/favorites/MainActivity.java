package br.com.developbox.favorites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.developbox.services.Favorite;
import br.com.developbox.services.FavoritesDatabase;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Favorite t[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        loadList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getBaseContext(), AddActivity.class);
                startActivity(it);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_send:
                String shareString = "";
                for(int i = 0; i < t.length; i++){
                    shareString += t[i].getShareString()+"\n";
                }
                Intent sendIt = new Intent(Intent.ACTION_SEND);
                sendIt.setType("text/plain");
                sendIt.putExtra(Intent.EXTRA_TEXT, shareString);

                startActivity(sendIt);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadList(){
        this.listView = (ListView) findViewById(R.id.listView);

        FavoritesDatabase db = new FavoritesDatabase(getBaseContext());
        if(db.getAll(db.getReadableDatabase()) == null){
            Intent it = new Intent(getBaseContext(), AddActivity.class);
            startActivity(it);
        }else {
            this.t = db.getAll(db.getReadableDatabase());
            ArrayList<Map<String, String>> listaMap = new ArrayList<Map<String, String>>();
            for(int i = 0; i < t.length; i++){
                Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("Linha 1", t[i].getTitle());
                datum.put("Linha 2", t[i].getUrl());
                listaMap.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(this,
                    listaMap,
                    android.R.layout.simple_list_item_2,
                    new String[] {"Linha 1", "Linha 2"},
                    new int[] {android.R.id.text1, android.R.id.text2});
            this.listView.setAdapter(adapter);

            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent(getBaseContext(), EditActivity.class);
                    String extra = String.valueOf(t[position].getId());
                    it.putExtra("id", extra);
                    startActivity(it);
                }
            });

            this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri url = Uri.parse(t[position].getUrl());
                    Intent it = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(it);
                    return false;
                }
            });
        }
    }
}
