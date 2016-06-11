package br.com.developbox.favorites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.developbox.services.Favorite;
import br.com.developbox.services.FavoritesDatabase;

public class EditActivity extends AppCompatActivity {
    private String id;
    private Favorite favorite;

    private EditText editTitleField;
    private EditText editUrlField;

    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getExtras();

        editTitleField = (EditText) findViewById(R.id.editTitleField);
        editUrlField = (EditText) findViewById(R.id.editUrlField);
        editButton = (Button) findViewById(R.id.editButton);

        editTitleField.setText(favorite.getTitle());
        editUrlField.setText(favorite.getUrl());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTitleField.getText().equals("")){
                    Toast.makeText(EditActivity.this, "O campo de título não pode está limpo.", Toast.LENGTH_SHORT).show();
                    forceKeyBoard(editTitleField);
                }
                if(editUrlField.getText().equals("")){
                    Toast.makeText(EditActivity.this, "O campo de URL não pode está limpo.", Toast.LENGTH_SHORT).show();
                    forceKeyBoard(editUrlField);
                }
                if(!editTitleField.getText().equals("") && !editUrlField.getText().equals("")){
                    FavoritesDatabase db = new FavoritesDatabase(getBaseContext());
                    favorite.setTitle(editTitleField.getText().toString());
                    favorite.setUrl(editUrlField.getText().toString());

                    db.edit(db.getWritableDatabase(), favorite);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);

        MenuItemCompat.setShowAsAction(menu.findItem(R.id.deleteItem), MenuItem.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setShowAsAction(menu.findItem(R.id.showOnBrowser), MenuItem.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setShowAsAction(menu.findItem(R.id.shareItem), MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
            case R.id.deleteItem:
                Log.i("ACTION BAR CLICK", "DELETE ITEM");
                FavoritesDatabase db = new FavoritesDatabase(getBaseContext());
                db.delete(db.getWritableDatabase(), favorite);
                finish();

                return true;
            case R.id.showOnBrowser:
                Log.i("ACTION BAR CLICK", "SHOW ON BROWSER");
                Uri url = Uri.parse(favorite.getUrl());

                Intent it = new Intent(Intent.ACTION_VIEW, url);
                startActivity(it);

                return true;
            case R.id.shareItem:
                Log.i("ACTION BAR CLICK", "SHARE ITEM");
                Intent sendIt = new Intent(Intent.ACTION_SEND);
                sendIt.setType("text/html");
                sendIt.putExtra(Intent.EXTRA_TEXT, favorite.getShareString());
                startActivity(sendIt);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExtras() {
        Intent extra = getIntent();
        if (extra != null) {
            this.id = extra.getStringExtra("id");
            FavoritesDatabase db = new FavoritesDatabase(getBaseContext());
            favorite = db.getById(db.getReadableDatabase(), id);
        } else {
            finish();
        }
    }

    private void forceKeyBoard(View component){
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(component, InputMethodManager.SHOW_FORCED);
    }
}
