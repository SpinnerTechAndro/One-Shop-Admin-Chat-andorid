package dev.spinner_tech.admin_chat.api;

import java.util.List;


import dev.spinner_tech.admin_chat.Models.LoginResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface OneShopApi {

    @Multipart
    @POST("chat/save-image")
    Call<LoginResponse.megResponse> saveChatImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);









}