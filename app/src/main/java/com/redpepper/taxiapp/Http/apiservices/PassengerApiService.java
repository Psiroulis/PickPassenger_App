 package com.redpepper.taxiapp.Http.apiservices;

 import com.redpepper.taxiapp.Http.PostModels.AcceptedDriverArrivalTimePost;
 import com.redpepper.taxiapp.Http.PostModels.DirectionPathPost;
 import com.redpepper.taxiapp.Http.PostModels.FavoritePlacePost;
 import com.redpepper.taxiapp.Http.PostModels.Fcm.PostFcmToken;
 import com.redpepper.taxiapp.Http.PostModels.Fcm.UserDevicePost;
 import com.redpepper.taxiapp.Http.PostModels.FoursquareApiPost;
 import com.redpepper.taxiapp.Http.PostModels.NearestDriverTimePost;
 import com.redpepper.taxiapp.Http.PostModels.RecentPlacePost;
 import com.redpepper.taxiapp.Http.PostModels.ResentPasswordSmsPost;
 import com.redpepper.taxiapp.Http.PostModels.RidePost;
 import com.redpepper.taxiapp.Http.PostModels.SignUpPost;
 import com.redpepper.taxiapp.Http.PostModels.TokenPost;
 import com.redpepper.taxiapp.Http.PostModels.UserExistsPost;
 import com.redpepper.taxiapp.Http.PostModels.ValidatePasswordPost;
 import com.redpepper.taxiapp.Http.PostModels.payments.PostPaymentMethodId;
 import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.AllPlacesResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.AllRecentPlacesResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.ComingDriverLocationResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.DirectionPathResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.FoursquarePlacesResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.NearestDriverTimeResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.TokenResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeAddPaymentMethodResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeClientSecretResponse;
 import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeGetAllPaymentMethodsResponse;

 import io.reactivex.Single;
 import retrofit2.Response;
 import retrofit2.http.Body;
 import retrofit2.http.DELETE;
 import retrofit2.http.GET;
 import retrofit2.http.Header;
 import retrofit2.http.PATCH;
 import retrofit2.http.POST;
 import retrofit2.http.PUT;
 import retrofit2.http.Path;
 import retrofit2.http.Query;


 public interface PassengerApiService {


    @POST("/oauth/token")
    Single<TokenResponse> getToken(@Body TokenPost post);

    @POST("api/userexists")
    Single<SimpleResponse> checkUserExists(@Body UserExistsPost post);

    @POST("api/register")
    Single<SimpleResponse> register(@Body SignUpPost post);

    @POST("api/login")
    Single<SimpleResponse> login(@Body ValidatePasswordPost post);

    @POST("api/forgotpassword")
    Single<SimpleResponse> resetPassword(@Body ResentPasswordSmsPost post);

    @GET("api/favoriteplace")
    Single<Response<AllPlacesResponse>> getAllPlaces(@Header("Authorization") String token);

    @POST("api/favoriteplace")
    Single<Response<SimpleResponse>> createPlace(@Header("Authorization") String Token , @Body FavoritePlacePost post);

    @DELETE("api/favoriteplace/{id}")
    Single<Response<SimpleResponse>> deletePlace(@Header("Authorization") String Token, @Path("id") int id);

    @PATCH("api/favoriteplace/{id}")
    Single<Response<SimpleResponse>> editPlace(@Header("Authorization") String Token, @Path("id") int id, @Body FavoritePlacePost post);

    @POST("api/recentplace")
    Single<Response<SimpleResponse>> createRecentPlace(@Header("Authorization") String Token , @Body RecentPlacePost post);

    @GET("api/recentplace")
    Single<Response<AllRecentPlacesResponse>> getAllRecentPlaces(@Header("Authorization") String token);

    @DELETE("api/recentplace/{id}")
    Single<Response<SimpleResponse>> deleteRecentPlace(@Header("Authorization") String Token, @Path("id") int id);

    @GET("api/recentplace/clear")
    Single<Response<SimpleResponse>> clearHistory(@Header("Authorization") String Token);

//    @POST("api/rides")
//    Single<Response<SimpleResponseWithRideId>> createRide(@Header("Authorization") String Token, @Body RidePost post);
                //
                //    @DELETE("api/rides/{id}")
                //    Single<Response<SimpleResponse>> deleteRide(@Header("Authorization") String Token, @Path("id") int id);

    @POST("api/directionspath")
    Single<Response<DirectionPathResponse>> getDirectionsPath(@Header("Authorization")String token, @Body DirectionPathPost post);

    @POST("api/nearestdrivertime")
    Single<Response<NearestDriverTimeResponse>> getNearestDriverArrivalTime(@Header("Authorization") String Token, @Body NearestDriverTimePost post);

    @POST("api/foursquareplaces")
    Single<Response<FoursquarePlacesResponse>> getFoursquareNearPlaces(@Header("Authorization") String Token , @Body FoursquareApiPost post);

    @POST("api/payments/createcustomer")
    Single<Response<SimpleResponse>> createStripeCustomer(@Header("Authorization") String token);

    @GET("api/payments/clientsecret")
    Single<Response<StripeClientSecretResponse>> getStripeClientSecret(@Header("Authorization") String token);

    @POST("api/payments/add_payment_method")
    Single<Response<StripeAddPaymentMethodResponse>> addNewPaymentMethod(@Header("Authorization") String Token , @Body PostPaymentMethodId post);

    @GET("api/payments/get_payment_methods")
    Single<Response<StripeGetAllPaymentMethodsResponse>> getStripeClientAllPaymentMethods(@Header("Authorization") String token);

    @POST("api/payments/delete_payment_method")
    Single<Response<SimpleResponse>> deleteStripePaymentMethod(@Header("Authorization") String Token , @Body PostPaymentMethodId post);

    @POST("api/store_fcm_token")
    Single<Response<SimpleResponse>> storeUserFcmToken(@Header("Authorization") String Token, @Body PostFcmToken post);

    @POST("api/device")
    Single<Response<SimpleResponse>> createDevice(@Header("Authorization") String token, @Body UserDevicePost post);

    @GET("api/accptdrivinf")
    Single<Response<AcceptedDriverInfoResponse>> getAcceptedDriverInfos(@Header("Authorization") String token, @Query("driver_id") String driverId);

    @GET("api/sendridetodrivers")
    Single<Response<SimpleResponse>> sendRideToDrivers(@Header("Authorization") String token, @Query("lat") double pick_up_lat, @Query("lng") double pick_up_lng);

    @GET("api/rides/{id}")
    Single<Response<RideResponse>> getRideInfo(@Header("Authorization") String token, @Path("id") int id);

    @PUT("api/rides/{id}")
    Single<Response<SimpleResponse>> addInfoToRide(@Header("Authorization") String token, @Path("id") int id, @Body RidePost post);

    @GET("api/getdriverlocation")
    Single<Response<ComingDriverLocationResponse>> getComingDriverLocation(@Header("Authorization") String token, @Query("driver_id") String driverId);

    @POST("api/comingdrivertime")
     Single<Response<NearestDriverTimeResponse>> getAcceptedDriverArrivalTime(@Header("Authorization") String token, @Body AcceptedDriverArrivalTimePost post);
 }

