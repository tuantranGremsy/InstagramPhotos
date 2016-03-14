package com.gremsy.tuantran.instagramphotos;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item
        InstagramPhoto photo = getItem(position);

        if(convertView == null){
            // Create a new view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Populate data
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.profile_image);
        TextView authorName = (TextView) convertView.findViewById(R.id.author_name);
        TextView likes = (TextView) convertView.findViewById(R.id.likes_for_this);

        // Insert caption data
        tvCaption.setText(photo.caption);
        // Set author name
        authorName.setText(photo.username);
        // Display likes count
        likes.setText(photo.likesCount + " Likes");
        // Clear out and insert the image using Picasso
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.ic_placeholder).into(ivPhoto);
        // Profile image
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(photo.profile_image)
                .fit()
                .placeholder(R.drawable.ic_author_placeholder)
                .transform(transformation)
                .into(ivProfile);
        // Return the created item as a view
        return convertView;
    }
}
