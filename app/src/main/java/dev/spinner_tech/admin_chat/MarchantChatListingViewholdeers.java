package dev.spinner_tech.admin_chat;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.spinner_tech.admin_chat.Models.CustomerListModel;

public class MarchantChatListingViewholdeers extends RecyclerView.ViewHolder {

    View mView;
    public TextView nameTv , lastMsg ;
    public CircleImageView pp ;
     public ConstraintLayout container ;
    public  static  String IMAGE_URL = "http://oneshop.spinnertechbd.com/one_shop_admin/all_images/";



    public MarchantChatListingViewholdeers(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        container = mView.findViewById(R.id.full_layout) ;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mClickListener.onItemClick(view, getAdapterPosition());


            }
        });

    }

    public  void setNameTv( String name )
    {
        container = mView.findViewById(R.id.full_layout) ;
        nameTv  = (TextView) mView.findViewById(R.id.display_name);
        nameTv.setText(name);
    }
    public  void setPp(String link , Context context )
    {
        container = mView.findViewById(R.id.full_layout) ;
        pp =  mView.findViewById(R.id.profile_image);

        Glide.with(context).load(link).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(pp) ;
    }





    private static MarchantChatListingViewholdeers.ClickListener mClickListener;


    public void setDetails(Context context, String customerName, String lastMessage, String customerImage) {
        // views
        // containerLayout = itemView.findViewById(R.id.container);
        nameTv  = (TextView) mView.findViewById(R.id.display_name);
        pp =  mView.findViewById(R.id.profile_image);
        lastMsg = (TextView) mView.findViewById(R.id.lastMsg) ;

        container = mView.findViewById(R.id.full_layout) ;

        nameTv.setText(customerName);
        lastMsg.setText(lastMessage);
        Glide.with(context).load(IMAGE_URL + customerImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp) ;

    }


    public interface ClickListener {
        void onItemClick(View view, int position);

    }


    public  void setOnClickListener(MarchantChatListingViewholdeers.ClickListener clickListener) {

        mClickListener = clickListener;

    }
}