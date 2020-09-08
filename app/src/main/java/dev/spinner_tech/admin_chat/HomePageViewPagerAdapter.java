package dev.spinner_tech.admin_chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;



//Created By Rahat Shovo
 public  class HomePageViewPagerAdapter extends FragmentStateAdapter {

 private String[] titles = new String[]{"Customers", "Merchants"};

 public HomePageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
  super(fragmentActivity);
 }
 @NonNull
 @Override
 public Fragment createFragment(int position) {

  switch (position) {
   case 0:
    return new Fragment_Chat();
   case 1:
    return new Fragment_Marchant();

  }

  return new Fragment_Chat();
 }



 @Override
 public int getItemCount() {
  return titles.length;
 }
}
