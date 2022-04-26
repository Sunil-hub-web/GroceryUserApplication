package com.kissanfactory.groceryuserapplication.WebServices;

import com.kissanfactory.groceryuserapplication.Models.Address;
import com.kissanfactory.groceryuserapplication.Models.AddressResponse;
import com.kissanfactory.groceryuserapplication.Models.AgentLocationResponse;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.Checkout_Create_Pojo;
import com.kissanfactory.groceryuserapplication.Models.Customer;
import com.kissanfactory.groceryuserapplication.Models.DefaultAddress;
import com.kissanfactory.groceryuserapplication.Models.DeliveryAgentResponse;
import com.kissanfactory.groceryuserapplication.Models.Hash;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.Models.OrderResponse;
import com.kissanfactory.groceryuserapplication.Models.Password;
import com.kissanfactory.groceryuserapplication.Models.PaymentHistory;
import com.kissanfactory.groceryuserapplication.Models.Payment_Checkout_status_pojo;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.Models.ProductResponse;
import com.kissanfactory.groceryuserapplication.Models.Product_Details.ProductList;
import com.kissanfactory.groceryuserapplication.Models.SingleOrderResponse;
import com.kissanfactory.groceryuserapplication.Models.SlideShowResponse;
import com.kissanfactory.groceryuserapplication.Models.notification.NotificationDto;
import com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo.PaymentAddOrderDto;
import com.kissanfactory.groceryuserapplication.Models.payment_create.PaymentCreateDto;
import com.kissanfactory.groceryuserapplication.Models.paymenthistory.PaymentHistoryDto;
import com.kissanfactory.groceryuserapplication.Models.paymet_check_status.PaymentCheckStatusDto;
import com.kissanfactory.groceryuserapplication.Models.top_productsdto.TopProductDTO;
import com.kissanfactory.groceryuserapplication.Models.viewallTopProducts.TopProductViewAlltDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;

import com.kissanfactory.groceryuserapplication.Models.ApiResponse;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiToJsonHandler {
    public static String BASE_URL = "https://kisaanandfactory.com/api/v1/userapp/";

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            //     .baseUrl("https://api.kisaan-factory.com/api/v1/userapp/")
            //    .baseUrl("http://kisan.aimplatfarm.com/api/v1/userapp/")
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getUnsafeOkHttpClient().build())
            .build();

    UserAppApi userAppApi = retrofit.create(UserAppApi.class);

    // get hash
    public Call<ApiResponse> getHash(Hash hash) {
        return new Retrofit.Builder()
                .baseUrl("https://kisaanandfactory.com/")
                //               .baseUrl("https://kisaan-factory.com/")
//                .baseUrl("http://192.168.1.18/website/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(UserAppApi.class).getHash(hash);
    }

    public Call<SlideShowResponse> getSlideShow() {
        return new Retrofit.Builder()
                .baseUrl("https://kisaanandfactory.com/api/v1/adminapp/")
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(UserAppApi.class).getSlideShowImages();
    }


    // getting the notificatios
    public Call<NotificationDto> getNotifications(String token) {
        return userAppApi.getNotifications(token);
    }

    // customer register
    public Call<ApiResponse> registerCustomer(Customer customer) {
        return userAppApi.registerCustomer(customer);
    }

    // verifying customer account otp
    public Call<ApiResponse> verifyPhoneOtp(Customer customer) {
        return userAppApi.verifyRegistrationOtp(customer);
    }

    // logging in the customer
    public Call<ApiResponse> loginCustomer(Customer customer) {
        return userAppApi.loginCustomer(customer);
    }

    // forgot password request otp
    public Call<NoMsgResponse> forgotPasswordOtp(Customer customer) {
        return userAppApi.forgotPasswordOtp(customer);
    }

    // reset customer otp
    public Call<NoMsgResponse> resendPasswordOtp(Customer customer) {
        return userAppApi.resendPasswordOtp(customer);
    }

    // verify forgot password otp
    public Call<ApiResponse> verifyForgotPassword(Customer customer) {
        return userAppApi.verifyForgetPasswordOtp(customer);
    }

    // change the password
    public Call<ApiResponse> changePassword(Password password, String token) {
        return userAppApi.changePassword(password, token);
    }

    // view customer profile
    public Call<ApiResponse> customerProfile(String token) {
        return userAppApi.customerProfile(token);
    }

    // add user address
    public Call<NoMsgResponse> addAddress(Address address, String token) {
        return userAppApi.addAddress(address, token);
    }

    // edit user address
    public Call<ApiResponse> editAddress(String token, String id, Address address) {
        return userAppApi.editAddress(id, token, address);
    }

    // get all addresses
    public Call<ApiResponse> getAllAddresses(String token) {
        return userAppApi.allAddresses(token);
    }

    // make the default address
    public Call<ApiResponse> makeDefaultAddress(String token, DefaultAddress address) {
        return userAppApi.makeDefaultAddress(token, address);
    }

    // getting all the products
    public Call<ApiResponse> getAllProducts() {
        return userAppApi.allProducts();
    }

    // get products from categories
    public Call<ApiResponse> categoryProducts(String category) {
        return userAppApi.categoryProducts(category);
    }   // get products from categories

    public Call<TopProductViewAlltDTO> getalltop_category(String category) {
        return userAppApi.getalltop_category(category);
    }

    // get products from categories

    public Call<TopProductDTO> Top_Products_Api() {
        return userAppApi.Top_Products_Api();
    }

    // get specific product
    public Call<ProductResponse> getProductDetails(String id) {
        return userAppApi.productDetails(id);
    }

    // search products
    public Call<ProductList> searchProducts(String productName, int offset) {
        return userAppApi.searchProduct(productName, offset);
    }

    // get user cart
    public Call<ApiResponse> getCart(String token) {
        return userAppApi.getCart(token);
    }

    // add to cart
    public Call<NoMsgResponse> addToCart(String token, String id, Cart cart) {
        return userAppApi.addToCart(token, id, cart);
    }

    // get favourites
    public Call<ApiResponse> getFavourites(String token) {
        return userAppApi.getFavourite(token);
    }

    // delete favourites
    public Call<ApiResponse> deleteFavourite(String token, String id) {
        return userAppApi.deleteFavourite(token, id);
    }

    // add to favourites
    public Call<NoMsgResponse> addToFavourites(String token, Product product) {
        return userAppApi.addToFavourites(token, product);
    }

    // delete from cart
    public Call<NoMsgResponse> deleteFromCart(String token, String id) {
        return userAppApi.deleteFromCart(token, id);
    }

    // update cart quantity
    public Call<ApiResponse> changeQuantity(String id, String token, Cart product) {
        return userAppApi.updateQuantity(id, token, product);
    }

    public Call<ApiResponse> getCategories() {
        return userAppApi.getCategories();
    }

//    // get history
//    public Call<PaymentHIstoryREsponse> getPayments(String token){
//        return userAppApi.getPayments(token);
//    }

    // get payment history new

    public Call<PaymentHistoryDto> getPaymentshistory(String token) {
        return userAppApi.getPaymentshistory(token);
    }

    // add payment
    public Call<NoMsgResponse> addPayment(String token, PaymentHistory paymentHistory) {
        return userAppApi.addPayment(token, paymentHistory);
    }

    // add order
    public Call<NoMsgResponse> addOrder(String token, Order order) {
        return userAppApi.addOrder(token, order);
    }

    // delete address
    public Call<ApiResponse> deleteAddress(String token, String id) {
        return userAppApi.deleteAddress(id, token);
    }

    //get address details
    public Call<AddressResponse> getAddress(String token, String id) {
        return userAppApi.addressDetails(id, token);
    }

    // get orders
    public Call<OrderResponse> getOrders(String token) {
        return userAppApi.getOrders(token);
    }

    // get single order
    public Call<SingleOrderResponse> getOrder(String token, String id) {
        return userAppApi.getOrder(token, id);
    }

    // clear cart
    public Call<NoMsgResponse> clearCart(String token) {
        return userAppApi.clearCart(token);
    }

    // cancel order
    public Call<NoMsgResponse> cancelOrder(String token, Order order) {
        return userAppApi.cancelOrder(token, order);
    }

    // get agent details
    public Call<DeliveryAgentResponse> getAgent(String token, String orderId) {
        return userAppApi.getAgent(token, orderId);
    }

    // get agent location
    public Call<AgentLocationResponse> agentLocation(String token, String orderId) {
        return userAppApi.agentLocation(token, orderId);
    }

    // google sign in
    public Call<ApiResponse> googleSignIn(Customer customer) {
        return userAppApi.googleSignIn(customer);
    }

    // create checkout for payment
    public Call<PaymentCreateDto> api_create_checkout(String token, Checkout_Create_Pojo checkout_create_pojo) {
        return userAppApi.api_create_checkout(token,checkout_create_pojo);
    }

    // create checkout for payment
    public Call<PaymentCheckStatusDto> api_payment_checkout_status(String token, Payment_Checkout_status_pojo payment_checkout_status_pojo) {
        return userAppApi.api_payment_checkout_status(token,payment_checkout_status_pojo);
    }

    // add order for payment
    public Call<PaymentAddOrderDto> api_payment_add_order(String token, Payment_Checkout_status_pojo payment_checkout_status_pojo) {
        return userAppApi.api_payment_add_order(token, payment_checkout_status_pojo);
    }

}
