package com.kissanfactory.groceryuserapplication.WebServices;

import com.kissanfactory.groceryuserapplication.Models.Address;
import com.kissanfactory.groceryuserapplication.Models.AddressResponse;
import com.kissanfactory.groceryuserapplication.Models.AgentLocationResponse;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.Checkout_Create_Pojo;
import com.kissanfactory.groceryuserapplication.Models.Customer;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAppApi {

    @GET("dashboard/notification")
    Call<NotificationDto> getNotifications(@Header("auth-token") String token);

    @GET("user/slideshows")
    Call<SlideShowResponse> getSlideShowImages();

    @GET("product/categories/all")
    Call<ApiResponse> getCategories();

    @POST("checkout/checkout.php")
    Call<ApiResponse> getHash(@Body Hash hash);

    @POST("auth/register")
    Call<ApiResponse> registerCustomer(@Body Customer customer);

    @PUT("auth/register/verify-otp")
    Call<ApiResponse> verifyRegistrationOtp(@Body Customer customer);

    @POST("auth/login")
    Call<ApiResponse> loginCustomer(@Body Customer customer);

    @POST("auth/login/forget-password/request-otp")
    Call<NoMsgResponse> forgotPasswordOtp(@Body Customer customer);

    @POST("auth/login/forget-password/verify-otp")
    Call<ApiResponse> verifyForgetPasswordOtp(@Body Customer customer);

    @POST("auth/login/forget-password/resend-otp")
    Call<NoMsgResponse> resendPasswordOtp(@Body Customer customer);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("auth/forgot-password/change/password")
    Call<ApiResponse> changePassword(@Body Password password, @Header("auth-token") String token);

    @GET("dashboard/view/profile")
    Call<ApiResponse> customerProfile(@Header("auth-token") String token);

    // getting all products
    @GET("product/all")
    Call<ApiResponse> allProducts();

    // get products from category
    @GET("product/category/{categoryId}")
    Call<ApiResponse> categoryProducts(@Path("categoryId") String category); // get products from category

    // get products from category
    @GET("shop/{url}")
    Call<TopProductViewAlltDTO> getalltop_category(@Path("url") String category); // get products from category

    @GET("shop/top-products")
    Call<TopProductDTO> Top_Products_Api();

    // get specific product
    @GET("product/{productId}")
    Call<ProductResponse> productDetails(@Path("productId") String id);

//    // searching for products
//    @GET("product/name/{productName}")
//    Call<ApiResponse> searchProduct(@Path("productName") String productName);  // searching for products

    @GET("product/search/product")
    Call<ProductList> searchProduct(@Query("productName") String productName, @Query("offset") int offset);

    // get cart items
    @GET("cart/view")
    Call<ApiResponse> getCart(@Header("auth-token") String token);

    // add to cart
    @POST("cart/product/add-toCart/{productId}")
    Call<NoMsgResponse> addToCart(@Header("auth-token") String token, @Path("productId") String id, @Body Cart cart);

    // delete from cart
    @POST("cart/product/remove/{id}")
    Call<NoMsgResponse> deleteFromCart(@Header("auth-token") String token, @Path("id") String id);

    // get favourites
    @GET("product/favourite/view")
    Call<ApiResponse> getFavourite(@Header("auth-token") String token);

    // delete from favourite
    @DELETE("product/favourite/delete/{id}")
    Call<ApiResponse> deleteFavourite(@Header("auth-token") String token, @Path("id") String id);

    // add to favourites
    @POST("product/favourite/add")
    Call<NoMsgResponse> addToFavourites(@Header("auth-token") String token, @Body Product productId);

    // address routes
    @POST("dashboard/address/add")
    Call<NoMsgResponse> addAddress(@Body Address address, @Header("auth-token") String token);

    @PUT("dashboard/address/edit/{addressId}")
    Call<ApiResponse> editAddress(@Path("addressId") String id, @Header("auth-token") String token, @Body Address address);

    @DELETE("dashboard/address/delete/{addressId}")
    Call<ApiResponse> deleteAddress(@Path("addressId") String id, @Header("auth-token") String token);

    @GET("dashboard/address/{addressId}")
    Call<AddressResponse> addressDetails(@Path("addressId") String id, @Header("auth-token") String token);

    @GET("dashboard/address/all")
    Call<ApiResponse> allAddresses(@Header("auth-token") String token);

    @POST("dashboard/address/makedefault")
    Call<ApiResponse> makeDefaultAddress(@Header("auth-token") String token, @Body DefaultAddress address);

    @PUT("cart/product/add/quantity/{productId}")
    Call<ApiResponse> updateQuantity(@Path("productId") String id, @Header("auth-token") String token, @Body Cart product);

    // clear cart
    @DELETE("cart/product/remove")
    Call<NoMsgResponse> clearCart(@Header("auth-token") String token);

    // add order
    @POST("order/add")
    Call<NoMsgResponse> addOrder(@Header("auth-token") String token, @Body Order order);

    // get orders
    @GET("order/all")
    Call<OrderResponse> getOrders(@Header("auth-token") String token);

    // get order details
    @GET("order/{id}")
    Call<SingleOrderResponse> getOrder(@Header("auth-token") String token, @Path("id") String id);

    // add payment history
    @POST("payment/history/add")
    Call<NoMsgResponse> addPayment(@Header("auth-token") String token, @Body PaymentHistory history);

//    // get payment history
//    @GET("payment/historys")
//    Call<PaymentHIstoryREsponse> getPayments(@Header("auth-token") String token); // get payment history

    @GET("payment/historys")
    Call<PaymentHistoryDto> getPaymentshistory(@Header("auth-token") String token);

    // cancel order
    @POST("order/cancel")
    Call<NoMsgResponse> cancelOrder(@Header("auth-token") String token, @Body Order order);

    // get delivery agent details
    @GET("order/driver-info/{orderId}")
    Call<DeliveryAgentResponse> getAgent(@Header("auth-token") String token, @Path("orderId") String id);

    // get agent location
    @GET("order/driver-location/{orderId}")
    Call<AgentLocationResponse> agentLocation(@Header("auth-token") String token, @Path("orderId") String id);

    // google sign in
    @POST("auth/google-auth")
    Call<ApiResponse> googleSignIn(@Body Customer user);

    // create checkout for payment
    @POST("payment/payment_history/create")
    Call<PaymentCreateDto> api_create_checkout(@Header("auth-token") String token, @Body Checkout_Create_Pojo checkout_create_pojo);

    // create checkout for payment
    @POST("payment/payment_history/checkstatus")
    Call<PaymentCheckStatusDto> api_payment_checkout_status(@Header("auth-token") String token,@Body Payment_Checkout_status_pojo payment_checkout_status_pojo);

    // add order for payment
    @POST("payment/payment_history/checkstatus-add-order")
    Call<PaymentAddOrderDto> api_payment_add_order(@Header("auth-token") String token, @Body Payment_Checkout_status_pojo payment_checkout_status_pojo);



}

