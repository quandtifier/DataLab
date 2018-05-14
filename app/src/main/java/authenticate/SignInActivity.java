package authenticate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.tacoma.uw.css.quandm.datalab.R;

public class SignInActivity extends AppCompatActivity
        implements LoginFragment.LoginInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.signin_fragment_container, new LoginFragment() )
                .commit();

    }

    @Override
    public void login(String email, String pwd) {

    }
}
