package ivanc.swap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginPageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Sets up the getStartedButton
        // When clicked, sends intent to go to home page
        final Button getStartedButton = (Button)findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText loginUsername = (EditText)findViewById(R.id.login_username);
                String username = loginUsername.getText().toString();

                Intent intent = new Intent(LoginPageActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}
