package com.cky.a3dtetris.rank;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cky.a3dtetris.R;


public class RankListAdapter extends BaseAdapter {

    private Context context;

    public RankListAdapter(Context context) {
        this.context = context;
        RankManager.getManager().rank();
    }

    @Override
    public int getCount() {
        return RankManager.getManager().getRankList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setValue(RankManager.getManager().getRankList().get(position));
        return holder.root;
    }

    private class ViewHolder {
        View root;
        TextView name;
        TextView score;

        public ViewHolder() {
            root = View.inflate(context, R.layout.item_rank_list, null);
            name = root.findViewById(R.id.item_rank_name);
            score = root.findViewById(R.id.item_rank_score);
            root.setTag(this);
        }

        public void setValue(RankItem rankItem) {
            name.setText(rankItem.getName());
            score.setText(String.valueOf(rankItem.getScore()));
        }

    }
}
