package br.com.developbox.favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.developbox.services.Favorite;
import br.com.developbox.services.FavoritesDatabase;

public class AddActivity extends AppCompatActivity {

    private EditText addTitleField;
    private EditText urlField;

    Button clearButton;
    Button addButton;
    InputMethodManager keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.addTitleField = (EditText) findViewById(R.id.addTitleField);
        this.urlField = (EditText) findViewById(R.id.urlField);

        this.clearButton = (Button) findViewById(R.id.clearButton);
        this.addButton = (Button) findViewById(R.id.addButton);

        this.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTitleField.getText().toString().equals("")){
                    Toast.makeText(AddActivity.this, "O título não pode está limpo.", Toast.LENGTH_SHORT).show();
                    forceKeyBoard(addTitleField);
                }
                if(urlField.getText().toString().equals("")){
                    Toast.makeText(AddActivity.this, "O campo de URL não pode está limpo.", Toast.LENGTH_SHORT).show();
                    forceKeyBoard(urlField);
                }
                if(!addTitleField.getText().toString().equals("") && !urlField.getText().toString().equals("")){
                    FavoritesDatabase db = new FavoritesDatabase(getBaseContext());
                    Favorite favorite = new Favorite(addTitleField.getText().toString(), urlField.getText().toString());
                    db.add(db.getWritableDatabase(), favorite);
                    clearFields();
                    finish();
                }
            }
        });

        getExtras();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.keyboard.showSoftInput(this.addTitleField, InputMethodManager.HIDE_IMPLICIT_ONLY);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void clearFields(){
        this.addTitleField.setText("");
        this.urlField.setText("");
        this.addTitleField.requestFocus();

        forceKeyBoard(addTitleField);

    }
    private void forceKeyBoard(View component){
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(component, InputMethodManager.SHOW_FORCED);
    }
    private void getExtras(){
        Intent extras = getIntent();

        if(extras.getAction() == Intent.ACTION_VIEW){
            urlField.setText(extras.getData().toString());
            forceKeyBoard(addTitleField);
        }
    }

}
