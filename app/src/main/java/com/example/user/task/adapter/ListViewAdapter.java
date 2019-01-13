package com.example.user.task.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.task.R;
import com.example.user.task.model.Category;
import com.example.user.task.model.User;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<User> users;
    private Context context;
    private static int position;


    public ListViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_item, parent, false);
        return new ListViewAdapter.ViewHolder(view);
    }

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ListViewAdapter.ViewHolder holder, int position) {
        User  category = users.get(position);

        holder.categoryNameView.setText(category.getSecondname());

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0,  0, "Удалить");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });

        holder.bind(users.get(position));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static int getPosition() {
        return position;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mCardView;
        final TextView categoryNameView;


        ViewHolder(View view) {
            super(view);
            categoryNameView =  view.findViewById(R.id.user_name);
            mCardView = view.findViewById(R.id.cardView);
        }

        public void bind(final User item) {

        }
    }
}
