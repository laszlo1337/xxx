package io.finefabric.szmatapp;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 100;
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";

    @BindView(R.id.activity_root)
    View root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                            .setTheme(R.style.GreenTheme)
                            .setLogo(R.drawable.logo_googleg_color_144dp)
                            .setProviders(getLoginProviders())
                            .setTosUrl(GOOGLE_TOS_URL)
                            .setIsSmartLockEnabled(false)
                            .build(), REQUEST_CODE_SIGN_IN);
        } else {
            startActivity(new Intent(this, NavigationActivity.class));
        }
    }

    private List<IdpConfig> getLoginProviders() {
        List<IdpConfig> providers = new ArrayList<>();
        providers.add(new IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
        providers.add(new IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        return providers;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SIGN_IN){
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startActivity(new Intent(this, NavigationActivity.class));
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }
    }

    private void showSnackbar(@StringRes int errorMessage) {
        Snackbar.make(root, errorMessage, Snackbar.LENGTH_LONG).show();
    }
}
