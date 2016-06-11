package br.com.developbox.favorites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.com.developbox.services.Favorite;
import br.com.developbox.services.FavoritesDatabase;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String list[];
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
                sendIt.setType("text/html");
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
            this.list = new String[t.length];
            for(int i = 0; i < t.length; i++){
                list[i] = String.format("%s", t[i].getTitle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list);
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
