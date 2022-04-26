package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Helpers.EditTextToClassConverter;
import com.kissanfactory.groceryuserapplication.Helpers.Validator;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Customer;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.kissanfactory.groceryuserapplication.session_manager.SessionManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private EditText phoneNum, password;
    private TextView err;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    String device_token = "";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isComplete()) {
                        final String[] token = {""};
                        token[0] = task.getResult();
                        device_token = token[0];
                        System.out.println("fcmtoken......" + device_token);
                        Log.e("AppConstants", "onComplete: new Token got: " + token[0]);
                        sessionManager.setFCM_TOKEN(device_token);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    private void init() {
        phoneNum = findViewById(R.id.userPhone);
        password = findViewById(R.id.userPassword);
        err = findViewById(R.id.loginError);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(v -> {
            signIn();
        });

        // facebook sign in
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Demo", "Login Succesfull");
            }

            @Override
            public void onCancel() {
                Log.d("Demo", "Login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Demo", "Login failed");
            }
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d("Demo", object.toString());
                    try {
                        String name = object.getString("name");
                        String email = object.getString("email");
                        signInEmail(name, email, "facebook");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Can't retrieve email or name from facebook", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("fields", "gender, name, id, first_name, email, last_name");
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.startTracking();
    }

    // sign in email
    private void signInEmail(String name, String email, String method) {
        Toast.makeText(this, name + " " + email, Toast.LENGTH_SHORT).show();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Logging in");
        dialog.create();
        dialog.show();
        Customer customer = new Customer();
        customer.setEmailID(email);
        customer.setName(name);
        customer.setMethod(method);
        customer.setFcm_id(device_token);
        Call<ApiResponse> call = new UserApiToJsonHandler().googleSignIn(customer);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    saveSharedPrefs(response.body().getToken());
                    dialog.dismiss();
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    LoginActivity.this.finish();
                } else {
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                signInEmail(personName, personEmail, "google");
            }
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void login(View view) {
        if (new Validator().validInputs(password, phoneNum)) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Logging in");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();
            Customer customer = new EditTextToClassConverter().toCustomer(phoneNum, password,device_token);

            Call<ApiResponse> call = new UserApiToJsonHandler().loginCustomer(customer);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        // open main activity
                        saveSharedPrefs(response.body().getToken());
                        dialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        LoginActivity.this.finish();
                    } else {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                        showErrorMessage(message.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    dialog.dismiss();
                    showErrorMessage(t.getMessage());
                }
            });

        } else {
            showErrorMessage("Fill all Fields");
        }
    }

    // save data to shared prefs
    private void saveSharedPrefs(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("token", token);
        editor.putBoolean("exist", true);

        editor.apply();
    }

    private void showErrorMessage(String error) {
        err.setText(error);
        err.setVisibility(View.VISIBLE);
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class)
                .putExtra("login", "y"));
    }

}