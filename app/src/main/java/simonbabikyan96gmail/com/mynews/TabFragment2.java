package simonbabikyan96gmail.com.mynews;

/**
 * Created by Admin on 23.12.2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TabFragment2 extends Fragment {
    RecyclerView recyclerView;
    MyAdapter myAdapterTwo;
    ArrayList<News> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleTwo);
        myAdapterTwo = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapterTwo);
        arrayList.add(new News());
        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        public MyAdapter() {
            arrayList = new ArrayList<>();
            arrayList.add(new News());
            arrayList.add(new News());
            notifyDataSetChanged();
        }

        public void addNews(News news) {
            arrayList.add(news);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_context_2, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.article.setText(arrayList.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        //Определяем елементы
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView article;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                article = (TextView) itemView.findViewById(R.id.desc);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

}