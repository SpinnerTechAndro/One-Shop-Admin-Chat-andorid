package dev.spinner_tech.admin_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joooonho.SelectableRoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class chatAdapter extends RecyclerView.Adapter<chatAdapter.myHolder> {
    public  static  String IMAGE_URL = "http://oneshop.spinnertechbd.com/one_shop_admin/all_images/";
    List <chatMsgModel> chatList ;
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
    private   Context contextt ;
    private  static  final  int MSG_TYPE_RIGHT = 1 ;
    private  static  final  int MSG_TYPE_LEFT = 0 ;
    private  static  final  int IMAGE_MSG_TYPE_RIGHT = 5 ;
    private  static  final  int IMAGE_MSG_TYPE_LEFT = 6 ;
    String uid ;


    public chatAdapter(Context context , List <chatMsgModel> chatList  )
    {
        this.contextt = context ;
        this.chatList =  chatList ;
        this.uid = "admin";
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = null  ;

        if(viewType == MSG_TYPE_LEFT)
        {
             view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_left_chat ,  parent, false);
        }
        else if ( viewType == MSG_TYPE_RIGHT)
        {
             view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_right_chat ,  parent, false);
        }

        else if (viewType == IMAGE_MSG_TYPE_LEFT)
        {
            view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_image_left ,  parent, false);
        }
        else if (viewType == IMAGE_MSG_TYPE_RIGHT)
        {
            view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_image_right ,  parent, false);
        }






        return  new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final  int position) {

        String msg = chatList.get(position).getMsg() ;
        holder.date.setText(formatter.format(chatList.get(position).getTime()));
        holder.msgView.setText(msg);

        if(!chatList.get(position).getContent_type().equals("null") && !chatList.get(position).getContent_link().equals("null") )
        {

          Glide.with(contextt).asBitmap()
                  .placeholder(R.drawable.placeholder)
                  .diskCacheStrategy(DiskCacheStrategy.ALL)
                  .load(IMAGE_URL +chatList.get(position)
                  .getContent_link()).into(holder.imageView) ;
         //  Picasso.get().load(chatList.get(position).getContent_link()).into(holder.imageView) ;


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText( contextt, chatList.get(position).getContent_link()+"", Toast.LENGTH_LONG).show();
                }
            });

        }




    }

    @Override
    public int getItemViewType(int position) {



       if( chatList.get(position).getUid().equals(uid)  && chatList.get(position).getContent_type().equals("null"))
       {

           return MSG_TYPE_RIGHT ;

       }
       else if( chatList.get(position).getUid().equals(uid)  && !chatList.get(position).getContent_type().equals("null"))
        {

            return IMAGE_MSG_TYPE_RIGHT ;

        }

       else if(!chatList.get(position).getUid().equals(uid) && chatList.get(position).getContent_type().equals("null"))
       {
           return  MSG_TYPE_LEFT ;
       }

       else
      {
               return  IMAGE_MSG_TYPE_LEFT ;
       }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }





    class myHolder extends RecyclerView.ViewHolder
    {

      public  TextView msgView , date  ; SelectableRoundedImageView imageView ;








        public myHolder(@NonNull View itemView) {
            super(itemView);


            msgView = itemView.findViewById(R.id.msgTv);
            date = itemView.findViewById(R.id.dateview) ;
            imageView = itemView.findViewById(R.id.image) ;







        }

        private  void setImageView( String url , Context context)
        {
            imageView = itemView.findViewById(R.id.image) ;
            Glide.with(context).load(url).into(imageView) ;
        }



    }



}





