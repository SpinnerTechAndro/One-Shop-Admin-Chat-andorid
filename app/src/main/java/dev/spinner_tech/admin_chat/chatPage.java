package dev.spinner_tech.admin_chat;

import android.Manifest;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.spinner_tech.admin_chat.Models.CustomerListModel;
import dev.spinner_tech.admin_chat.Models.LoginResponse;
import dev.spinner_tech.admin_chat.Models.ShopListModel;
import dev.spinner_tech.admin_chat.services.ServiceGenerator;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class chatPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    FirebaseDatabase firebaseDatabase;
    List<chatMsgModel> loadedChat;
    RecyclerView recyclerView;
    DatabaseReference mref, chatListRef;
    ImageView sendBtn;
    EditText chatINput;
    String uid;
    LinearLayoutManager llm;
    Uri mFilePathUri;
    public static String IMAGE_URL = "http://oneshop.spinnertechbd.com/one_shop_admin/all_images/";
    //CHATBOX ID
    String mar_name = "";
    String id, name, image, ref;
    Boolean isUser = false;
    TextView titleView;
    ImageView proPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        titleView = findViewById(R.id.topbar_title);
        proPic = findViewById(R.id.image);
        RequestPermission();
        // get the data
        Intent p = getIntent();
        ref = p.getStringExtra("type");
        name = p.getStringExtra("name");
        image = p.getStringExtra("image");
        id = p.getStringExtra("id");
        isUser = p.getBooleanExtra("is_user", false);
        mar_name = p.getStringExtra("mar_name");

        // set up the views
        titleView.setText(name);
        Glide.with(getApplicationContext())
                .load(IMAGE_URL + image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(proPic);

        if (isUser) {
            chatListRef = FirebaseDatabase.getInstance().getReference("CUSTOMER_TO_ADMIN_CHATLIST");
            mref = FirebaseDatabase.getInstance().getReference("CUSTOMER_TO_ADMIN_CHAT").child(id);

        } else {

            chatListRef = FirebaseDatabase.getInstance().getReference("SHOP_TO_ADMIN_CHATLIST");
            mref = FirebaseDatabase.getInstance().getReference("SHOP_TO_ADMIN_CHAT").child(id);

        }

        chatINput = findViewById(R.id.message_input);
        sendBtn = findViewById(R.id.message_send_btn);
        uid = "ADMIN";
        llm = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.list);
        llm.setStackFromEnd(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

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
                    try {
                        chatMsgModel chat = ds.getValue(chatMsgModel.class);

                        loadedChat.add(chat);
                    } catch (Exception e) {
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

                if (isUser) {
//                    CustomerListModel chatList = new CustomerListModel(
//                            "You : " + msg
//                            , name,
//                            id,
//                            image,
//                            Calendar.getInstance().getTimeInMillis()
//                    );

                    //    chatListRef.child(id).setValue(chatList);
                } else {
                    //    String lastMessage ,merchantName , shopIdOrChatBoxId , shopLogo ,shopName ;
                    //     long lastMessageTime ;
//                    ShopListModel chatList = new ShopListModel(
//                            "You : " + msg
//                            , mar_name,
//                            id,
//                            image,
//                            name,
//                            Calendar.getInstance().getTimeInMillis()
//                    );
//
//                    chatListRef.child(id).setValue(chatList);

                }


            }
        });


    }

    private void BringImagePicker() {


        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE) {

            //   mFilePathUri = data.getData();
            // Toast.makeText(getApplicationContext() , "TEst" + mFilePathUri.toString(), Toast.LENGTH_LONG) .show();
            Uri selectedMediaUri = data.getData();

            mFilePathUri = selectedMediaUri;
            sendTheFile(selectedMediaUri);


        }


    }

    private void sendTheFile(Uri mFilePathUri) {
        File file = new File(getPath(chatPage.this, mFilePathUri));

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

                    //send the msg
                    if (!response.body().getError()) {
                        String msg_ID = mref.push().getKey();
                        chatMsgModel msgModel = new chatMsgModel("", msg_ID, uid, "image", response.body().getMsg(), System.currentTimeMillis());
                        Log.d("TAG", "onActivityResult: File  uploaded   " + response.body().getMsg());
                        mref.child(msg_ID).setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


//                                if (isUser) {
//                                    CustomerListModel chatList = new CustomerListModel(
//                                            "You : Send a photo",
//                                            name,
//                                            id,
//                                            image,
//                                            Calendar.getInstance().getTimeInMillis()
//                                    );
//                                    chatListRef.child(id).setValue(chatList);
//                                } else {
//                                    //    String lastMessage ,merchantName , shopIdOrChatBoxId , shopLogo ,shopName ;
//                                    //     long lastMessageTime ;
//                                    ShopListModel chatList = new ShopListModel(
//                                            "You : Send a photo",
//                                            mar_name,
//                                            id,
//                                            image,
//                                            name,
//                                            Calendar.getInstance().getTimeInMillis()
//                                    );
//
//                                    chatListRef.child(id).setValue(chatList);
//
//                                }


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
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RequestPermission() {

        Dexter.withContext(chatPage.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    public static String getPath(Context ctx, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(ctx, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}