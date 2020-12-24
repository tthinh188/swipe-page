package com.example.cpsc475_project4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewPager2_Adapter extends RecyclerView.Adapter {
    private static final String CNU_URL = "https://www.pcs.cnu.edu/~kperkins/pets/";

    private final Context ctx;
    private final LayoutInflater li;
    private JSONArray petList;
    private String[] image_resources;

    private static class PagerViewHolder extends RecyclerView.ViewHolder {
        private static final int UNINITIALIZED = -1;
        WebImageView_TP iv;
        TextView tv1, tv2;
        int position = UNINITIALIZED;     //start off uninitialized, set it when we are populating
        //with a view in onBindViewHolder

        private PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (WebImageView_TP) itemView.findViewById(R.id.imageView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetImage extends AsyncTask<Void, Void, Void> {
        //ref to a viewholder
        private PagerViewHolder myVh;

        //since myVH may be recycled and reused
        //we have to verify that the result we are returning
        //is still what the viewholder wants
        private int original_position;

        GetImage(PagerViewHolder myVh) {
            //hold on to a reference to this viewholder
            //note that its contents (specifically iv) may change
            //iff the viewholder is recycled
            this.myVh = myVh;
            //make a copy to compare later, once we have the image
            this.original_position = myVh.position;
        }

        @Override
        protected Void doInBackground(Void... params) {

            //just sleep for a bit
            try {
                Thread.sleep(2000); //sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            //got a result, if the following are NOT equal
            // then the view has been recycled and is being used by another
            // number DO NOT MODIFY
            try {
                for (int i = 0; i < petList.length(); i++) { // setting image URLs
                    JSONObject jsonObject = petList.getJSONObject(i);
                    image_resources[i] = CNU_URL + jsonObject.getString("file");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (this.myVh.position == this.original_position) {
                //still valid
                //set the result on the main thread
                myVh.iv.setImageUrl(image_resources[this.myVh.position]);
//                myVh.iv.setImageResource(image_resources[this.myVh.position ]);
            } else
                Toast.makeText(ViewPager2_Adapter.this.ctx, "YIKES! Recycler view reused, my result is useless", Toast.LENGTH_SHORT).show();
        }
    }

    ViewPager2_Adapter(Context ctx, JSONArray petList) {
        this.ctx = ctx;
        //will use this to ceate swipe_layouts in onCreateViewHolder
        li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.petList = petList;
        image_resources = new String[petList.length()];
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.swipe_layout, parent, false);
        return new PagerViewHolder(view);   //the new one
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //passing in an existing instance, reuse the internal resources
        //pass our data to our ViewHolder.
        PagerViewHolder viewHolder = (PagerViewHolder) holder;
        //set to some default image

        viewHolder.iv.setImageResource(R.drawable.error);

        try {
            JSONObject jsonObject = petList.getJSONObject(position);
            viewHolder.tv1.setText(jsonObject.getString("name"));
            String url = CNU_URL + jsonObject.getString("file");
            viewHolder.tv2.setText(url);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewHolder.position = position;       //remember which image this view is bound to

        //launch a thread to 'retreive' the image
        GetImage myTask = new GetImage(viewHolder);
        myTask.execute();
    }

    @Override
    public int getItemCount() {
        return image_resources.length;
    }
}
