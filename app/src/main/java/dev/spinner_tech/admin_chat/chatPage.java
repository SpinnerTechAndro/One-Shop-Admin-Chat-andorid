package dev.spinner_tech.admin_chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.spinner_tech.admin_chat.Models.CustomerListModel;
import dev.spinner_tech.admin_chat.Models.LoginResponse;
import dev.spinner_tech.admin_chat.services.ServiceGenerator;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class chatPage extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    List<chatMsgModel> loadedChat;
    RecyclerView recyclerView;
    DatabaseReference mref,chatListRef;
    ImageView sendBtn;
    EditText chatINput;
    String uid;
    LinearLayoutManager llm;
    Uri mFilePathUri;

    //CHATBOX ID
    String chatboxIdOrCustomerId = "",customerName ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        chatINput = findViewById(R.id.message_input);
        sendBtn = findViewById(R.id.message_send_btn);
        uid = "ADMIN";
        llm = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.list);
        llm.setStackFromEnd(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        chatListRef = FirebaseDatabase.getInstance().getReference("CUSTOMER_TO_ADMIN_CHATLIST");
        mref = FirebaseDatabase.getInstance().getReference("CUSTOMER_TO_ADMIN_CHAT").child(chatboxIdOrCustomerId);

        downloadMsg();
        sendBtn.setOnClickListener(v -> {

            String msg = chatINput.getText().toString().trim();


            if (msg.isEmpty()) {

            } else {

                sendTheMsg(msg);


            }

        });
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.camera).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(chatPage.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    BringImagePicker();


                } else {

                    BringImagePicker();

                }

            } else {

                BringImagePicker();
            }
        });
    }

    private void downloadMsg() {

        loadedChat = new ArrayList<>();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadedChat.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                   try{
                       chatMsgModel chat = ds.getValue(chatMsgModel.class);

                       loadedChat.add(chat);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
                chatAdapter adapter = new chatAdapter(getApplicationContext(), loadedChat);
                adapter.notifyDataSetChanged();
                // set adapter
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    private void sendTheMsg(String msg) {


        String msg_ID = mref.push().getKey();
        // String msg , msg_id , uid , time   ;

        chatMsgModel msgModel = new chatMsgModel(msg.trim(), msg_ID, uid, "null", "null", System.currentTimeMillis());

        mref.child(msg_ID).setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                chatINput.setText("");
                // writeChatHistory(uid  , friendUserID , frindShipId);
                // chatINput.getText().clear();
                // TODO IF ELSE ON TYPE VAR

//                CustomerListModel chatList = new CustomerListModel(
//                        "You"+msg
//                        ,customerName,
//                        chatboxIdOrCustomerId,
//                        ,
//                        ,
//                        Calendar.getInstance().getTimeInMillis());

            //    chatListRef.child(chatboxIdOrCustomerId).setValue(chatList);

            }
        });


    }


    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(chatPage.this);


    }

    @Override
    protected void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();

                Log.d("TAG", "onActivityResult: Imgae Picked ");
                //sending data once  user select the image
                sendTheFile(mFilePathUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sendTheFile(Uri mFilePathUri) {
        File file = new File(mFilePathUri.getPath());

        File compressed;

        try {
            compressed = new Compressor(this)
                    .setMaxHeight(600)
                    .setMaxWidth(600)
                    .setQuality(45)
                    .compressToFile(file);
        } catch (Exception e) {
            compressed = file;
        }

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
        Log.d("TAG", "onActivityResult: File  Created  ");
        Call<LoginResponse.megResponse> imageResp = ServiceGenerator.getInstance()
                .getApi()
                .saveChatImage(requestFile);

        imageResp.enqueue(new Callback<LoginResponse.megResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.megResponse> call, Response<LoginResponse.megResponse> response) {
                if (response.code() == 200) {
                    String msg_ID = mref.push().getKey();

                    //send the msg
                    if (!response.body().getError()) {

                        chatMsgModel msgModel = new chatMsgModel("", msg_ID, uid, "image", response.body().getMsg(), System.currentTimeMillis());
                        Log.d("TAG", "onActivityResult: File  uploaded   ");
                        mref.child(msg_ID).setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // update the last msg


//                                ChatListCustomerToAdmin chatList = new ChatListCustomerToAdmin(
//                                        customerName,
//                                        SharedPrefManager.getInstance(chatPage.this).getUser().getCustomerImage(),
//                                        chatboxIdOrCustomerId,
//                                        "You: Send a photo",
//                                        Calendar.getInstance().getTimeInMillis());

                                chatListRef.child(chatboxIdOrCustomerId).setValue(chatList);

                            }
                        });
                    } else {
                        Log.d("TAG", "onActivityResult: erorr   ");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.megResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }


}