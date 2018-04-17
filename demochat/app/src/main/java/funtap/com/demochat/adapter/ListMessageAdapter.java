package funtap.com.demochat.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import funtap.com.demochat.R;
import funtap.com.demochat.model.ChatModel;
import funtap.com.demochat.util.CircleTransform;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by Vinh on 4/12/2018.
 */

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.ViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;

    private String nameUser;
    private List<ChatModel> listChat;
    public ListMessageAdapter(String nameUser, List<ChatModel> listChat) {
        this.nameUser = nameUser;
        this.listChat = listChat;
        Log.e("","listChat: "+ listChat.size()+" & nameUser: "+ nameUser );
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new ViewHolder(view);
        } else if (viewType == LEFT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new ViewHolder(view);
        } else if (viewType == RIGHT_MSG_IMG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_img, parent, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_img, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel chatModel = listChat.get(position);
        holder.setIvUser(chatModel.getUser().getPhoto_profile());
        holder.setTxtMessage(chatModel.getMessage());
        holder.setTvTimestamp(chatModel.getTimeStamp());
        if (chatModel.getFile() != null) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.setIvChatPhoto(chatModel.getFile().getUrl_file());
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = listChat.get(position);
        if (chatModel.getFile() != null) {
            if (chatModel.getFile().getType().equals("img") && chatModel.getUser().getName().equals(nameUser)) {
                return RIGHT_MSG_IMG;
            } else {
                return LEFT_MSG_IMG;
            }
        } else if (chatModel.getUser().getName().equals(nameUser)) {
            return RIGHT_MSG;
        } else {
            return LEFT_MSG;
        }
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTimestamp;
        public EmojiconTextView txtMessage;
        public ImageView ivUser, ivChatPhoto;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.timestamp);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            ivChatPhoto = itemView.findViewById(R.id.img_chat);
            ivUser = itemView.findViewById(R.id.ivUserChat);
            progressBar = itemView.findViewById(R.id.progress);
        }

        public void setTxtMessage(String message) {
            if (txtMessage == null) return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser) {
            if (ivUser == null) return;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .transform(new CircleTransform(ivUser.getContext()))
                    .override(100, 100);
            Glide.with(ivUser.getContext()).load(urlPhotoUser).apply(options).into(ivUser);
        }

        public void setTvTimestamp(String timestamp) {
            if (tvTimestamp == null) return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url) {
            if (ivChatPhoto == null) return;
            Glide.with(ivChatPhoto.getContext()).load(url).apply(new RequestOptions().fitCenter().override(500,500))
                    .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            })
                    .into(ivChatPhoto);
        }

    }

    private CharSequence converteTimestamp(String mileSegundos) {
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

}
