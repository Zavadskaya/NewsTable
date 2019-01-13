package com.example.user.task.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.task.R;
import com.example.user.task.model.Category;
import com.example.user.task.model.Costs;

import java.util.List;



public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Costs> costs;
    private Context context;
    private static int position;


    public NewsDataAdapter(Context context, List<Costs> costs) {
        this.context = context;
        this.costs = costs;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final NewsDataAdapter.ViewHolder holder, int position) {
        Costs cost = costs.get(position);

        holder.categoryView.setText(cost.getCategoryName());
        holder.sumCostView.setText(cost.getSumCosts());

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
        });  holder.bind(costs.get(position));
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public static int getPosition() {
        return position;
    }


    @Override
    public int getItemCount() {
        return costs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mCardView;
        final TextView categoryView, sumCostView;


        ViewHolder(View view) {
            super(view);
            categoryView =  view.findViewById(R.id.category_name);
            sumCostView =  view.findViewById(R.id.sum_cost);
            mCardView = view.findViewById(R.id.cardView);
        }

        public void bind(final Costs item) {
        }
    }
}