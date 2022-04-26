package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Hash;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.payu.base.models.ErrorResponse;
import com.payu.base.models.PayUPaymentParams;
import com.payu.checkoutpro.PayUCheckoutPro;
import com.payu.checkoutpro.utils.PayUCheckoutProConstants;
import com.payu.ui.model.listeners.PayUCheckoutProListener;
import com.payu.ui.model.listeners.PayUHashGenerationListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_payment);
    }

    public void payClick(View view){
        PayUPaymentParams.Builder builder = new PayUPaymentParams.Builder();
        builder.setAmount("100")
                .setIsProduction(false)
                .setProductInfo("Wallmart")
                .setKey("gtKFFx")
                .setTransactionId("Dan-tx2001")
                .setFirstName("Tester")
                .setEmail("test@gmail.com")
                .setSurl("http://192.168.1.84/website/checkout/success.php")
                .setFurl("http://192.168.1.84/website/checkout/failure.php");
        PayUPaymentParams payUPaymentParams = builder.build();

        PayUCheckoutPro.open(
                this,
                payUPaymentParams,
                new PayUCheckoutProListener() {

                    @Override
                    public void onPaymentSuccess(Object response) {
                        //Cast response object to HashMap
                        HashMap<String,Object> result = (HashMap<String, Object>) response;
                        String payuResponse = (String)result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                        String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);
                    }

                    @Override
                    public void onPaymentFailure(Object response) {
                        //Cast response object to HashMap
                        HashMap<String,Object> result = (HashMap<String, Object>) response;
                        String payuResponse = (String)result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                        String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);
                    }

                    @Override
                    public void onPaymentCancel(boolean isTxnInitiated) {
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
                        String errorMessage = errorResponse.getErrorMessage();
                        Log.e("Pay errur", errorMessage);
                    }

                    @Override
                    public void setWebViewProperties(@Nullable WebView webView, @Nullable Object o) {
                        //For setting webview properties, if any. Check Customized Integration section for more details on this
                    }

                    @Override
                    public void generateHash(HashMap<String, String> valueMap, PayUHashGenerationListener hashGenerationListener) {
                        String hashName = valueMap.get(PayUCheckoutProConstants.CP_HASH_NAME);
                        String hashData = valueMap.get(PayUCheckoutProConstants.CP_HASH_STRING);
                        if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {
                            Call<ApiResponse> getHash = new UserApiToJsonHandler().getHash(new Hash(hashData));
                            getHash.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful()){
                                        String hash = response.body().getOther();
                                        HashMap<String, String> dataMap = new HashMap<>();
                                        dataMap.put(hashName, hash);
                                        Log.d("hash", hashData);
                                        hashGenerationListener.onHashGenerated(dataMap);
                                    }else{
                                        Toast.makeText(TestPaymentActivity.this, "Somethig went wrong, try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    String hash = "";
                                    HashMap<String, String> dataMap = new HashMap<>();
                                    dataMap.put(hashName, hash);
                                    Log.d("hash", hashData);
                                    hashGenerationListener.onHashGenerated(dataMap);
                                }
                            });

                        }

                    }
                }
        );
    }

}