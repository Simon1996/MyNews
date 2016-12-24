package simonbabikyan96gmail.com.mynews;

/**
 * Created by Admin on 23.12.2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TabFragment1 extends Fragment {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    MyAsynk asynk;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle);

        //recyclerView = (RecyclerView)getView().findViewById(R.id.recycle);
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapter);
        asynk = new MyAsynk();
        asynk.execute();
        return view;
       // return inflater.inflate(R.layout.tab_fragment_1, container, false);
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<News> arrayList;

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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_context_1, parent, false);
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
                article = (TextView) itemView.findViewById(R.id.article_not_all);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

    class MyAsynk extends AsyncTask<Void,Void,StringBuilder> {

        @Override
        //работа в бекграунде
        protected StringBuilder doInBackground(Void... voids) {
            StringBuilder stringBuilder = new StringBuilder();
            String key = "0aa2713d5a1a4aad9a914c9294f6a22b";
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=" + key);
                URLConnection uc = url.openConnection();
                uc.connect();
                BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
                int ch;
                while ((ch = in.read()) != -1) {
                    stringBuilder.append((char) ch);
                }
            } catch (Exception e) {
            }
            return stringBuilder;
        }

        @Override
        protected void onPostExecute(StringBuilder stringBuilder) {
            News news = new News(stringBuilder.toString(), "", "");
            myAdapter.addNews(news);
            try {
                JSONObject json = new JSONObject(stringBuilder.toString());
                JSONArray array = json.getJSONArray("article");
            } catch (Exception e) {
            }
        }
    }
}