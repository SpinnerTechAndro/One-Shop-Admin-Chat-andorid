package dev.spinner_tech.admin_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dev.spinner_tech.admin_chat.Models.CustomerListModel;
import dev.spinner_tech.admin_chat.Models.ShopListModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Marchant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Marchant extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Marchant() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Marchant.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Marchant newInstance(String param1, String param2) {
        Fragment_Marchant fragment = new Fragment_Marchant();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View v ;
    RecyclerView list;
    DatabaseReference userRef, fref;
    FirebaseRecyclerAdapter<ShopListModel, MarchantChatListingViewholdeers> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<ShopListModel> options;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment__chat, container, false);
        context = v.getContext();

        list = v.findViewById(R.id.customer_chat);
        list.setLayoutManager(new LinearLayoutManager(context));

        loadList();
        return  v ;

    }

    public void loadList() {

        DatabaseReference histroyref = FirebaseDatabase.getInstance().getReference("SHOP_TO_ADMIN_CHATLIST");

        Query query = histroyref.orderByChild("lastMessageTime");

        histroyref.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<ShopListModel>()
                .setQuery(query, ShopListModel.class)
                .build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShopListModel, MarchantChatListingViewholdeers>(options) {
            @NonNull
            @Override
            public MarchantChatListingViewholdeers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_for_chat_display, parent, false);

                final MarchantChatListingViewholdeers viewholder = new MarchantChatListingViewholdeers(view);


                return viewholder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final MarchantChatListingViewholdeers viewholder, final int i, @NonNull final ShopListModel userModel) {

                viewholder.setDetails(getContext() , userModel.getMerchantName() , userModel.getLastMessage() , userModel.getShopLogo());

                viewholder.setOnClickListener(new MarchantChatListingViewholdeers.ClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {


                            String frindShipId = getItem(position).getShopIdOrChatBoxId();
                            Intent o = new Intent(context, chatPage.class);
                            o.putExtra("type", "SHOP");
                            o.putExtra("id",frindShipId );
                            o.putExtra("name" , userModel.getShopName()) ;
                            o.putExtra("image" , userModel.getShopLogo());
                            startActivity(o);




                    }
                });

            }

        };

        firebaseRecyclerAdapter.startListening();
        list.setAdapter(firebaseRecyclerAdapter);


    }


}




