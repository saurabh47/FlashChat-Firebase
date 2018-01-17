package com.webdestro.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Admin on 1/17/2018.
 */

public class ChatListAdafter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshots;

    public ChatListAdafter(Activity activity, DatabaseReference databaseReference, String displayName) {
        mActivity = activity;
        mDatabaseReference = databaseReference.child("messages");
        mDisplayName = displayName;
        mDatabaseReference.addChildEventListener(mListener);

        mSnapshots=new ArrayList<>();

    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;

    }

    private ChildEventListener mListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public int getCount() {
        return mSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot=mSnapshots.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder holder=new ViewHolder();
            holder.authorName=(TextView) convertView.findViewById(R.id.author);
            holder.body=(TextView) convertView.findViewById(R.id.message);
            holder.params =(LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);

        }

        final InstantMessage message=getItem(position);
        final ViewHolder holder=(ViewHolder)convertView.getTag();

        boolean isMe=message.getAuthor().equals(mDisplayName);
        setChatApperance(isMe,holder);

        String author=message.getAuthor();
        holder.authorName.setText(author);

        String mess=message.getMessage();
        holder.body.setText(mess);

        return convertView;
    }


    private void setChatApperance(boolean isItme,ViewHolder holder){
        if(isItme){
            holder.params.gravity= Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }else {
            holder.params.gravity=Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
