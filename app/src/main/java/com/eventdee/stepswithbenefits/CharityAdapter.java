package com.eventdee.stepswithbenefits;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CharityAdapter extends RecyclerView.Adapter<CharityAdapter.CharityViewHolder> {

    private Context mContext;
    private ArrayList<Charity> mCharityArray;

    public CharityAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public CharityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.fragment_charity, parent, false);
        return new CharityViewHolder(item);
    }

    @Override
    public void onBindViewHolder(CharityViewHolder holder, int position) {

        GlideApp.with(mContext)
                .load("")
                .placeholder(mCharityArray.get(position).getBackground())
                .centerCrop()
                .into(holder.mIvBackground);
        GlideApp.with(mContext)
                .load("")
                .placeholder(mCharityArray.get(position).getLogo())
                .centerCrop()
                .into(holder.mIvLogo);
//        holder.mIvBackground.setImageResource(mCharityArray.get(position).getBackground());
//        holder.mIvLogo.setImageResource(mCharityArray.get(position).getLogo());
        holder.mTvName.setText(mCharityArray.get(position).getName());
        holder.mTvTagline.setText(mCharityArray.get(position).getTagline());

        // Set the view to fade in
        setFadeAnimation(holder.itemView);

//        glide.load(R.drawable.background_acres).into(holder.mIvBackground);

//        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mCharityArray.size();
    }


    class CharityViewHolder extends ViewHolder {

        ImageView mIvBackground;
        ImageView mIvLogo;
        TextView mTvName;
        TextView mTvTagline;

        public CharityViewHolder(View itemView) {
            super(itemView);
            mIvBackground = (ImageView) itemView.findViewById(R.id.charity_background);
            mIvLogo = (ImageView) itemView.findViewById(R.id.charity_logo);
            mTvName = (TextView) itemView.findViewById(R.id.charity_name);
            mTvTagline = (TextView) itemView.findViewById(R.id.charity_tagline);
        }
    }

    public void setCharityArray(ArrayList<Charity> charityArray) {
        mCharityArray = charityArray;
        notifyDataSetChanged();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }
}
