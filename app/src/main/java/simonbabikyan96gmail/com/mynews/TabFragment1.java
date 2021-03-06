package simonbabikyan96gmail.com.mynews;

/**
 * Created by Admin on 23.12.2016.
 */

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TabFragment1 extends Fragment {

    private static final String LOG_TAG = "LOG";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    MyAsynk asynk;
    String count = "Fragment1";
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        //Adapters
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //Notification service
//        getActivity().startService(new Intent(getActivity(), Notification.class));

        asynk = new MyAsynk();
        asynk.execute();

        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<News> arrayList;

        public MyAdapter() {

            arrayList = new ArrayList<>();

            notifyDataSetChanged();
            //  createNotification(getContext());
        }

        public void addNews(News news) {
            arrayList.add(news);
            notifyDataSetChanged();

        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_context_1, parent, false);
            return new ViewHolder(itemView);
        }

        //Кидаем название, текст новости и изображение по местам.
        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {

            holder.article.setText(arrayList.get(position).getArticle());
            holder.title.setText(arrayList.get(position).getTitle());
            Picasso.with(getContext())
                    .load(arrayList.get(position).getImageUrl())
                    .transform(new CropSquareTransformation())
                    .placeholder(R.drawable.loader_image)
                    .error(R.drawable.not_found)
                    .into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Article_Activity.class);
                    intent.putExtra("count", count);
                    intent.putExtra("title", arrayList.get(position).getTitle());
                    intent.putExtra("article", arrayList.get(position).getArticle());
                    intent.putExtra("image", arrayList.get(position).getImageUrl());
                    intent.putExtra("newsurl", arrayList.get(position).getArticleUrl());
                    startActivity(intent);
                }
            });

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

            //Определяем title, article, image;
            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                article = (TextView) itemView.findViewById(R.id.desc);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

    //Поток
    class MyAsynk extends AsyncTask<Void, Void, StringBuilder> {

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

            //News news = new News(stringBuilder.toString(), "", "");
            //myAdapter.addNews(news);
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray array = jsonObject.getJSONArray("articles");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("description");
                    String imageUrl = object.getString("urlToImage");
                    String articleUrl = object.getString("url");
                    String newsdata = object.getString("publishedAt");

                    sPref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(SAVED_TEXT, newsdata);
                    ed.commit();
                    Toast.makeText(getActivity(), "Text saved", Toast.LENGTH_SHORT).show();
                    News news = new News(title, desc, imageUrl, articleUrl);
                    myAdapter.addNews(news);

                    myAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {

            }

        }
    }



    public void onResume() {
        getActivity().startService(new Intent(getActivity(), Notification.class));
        super.onResume();
        Log.d(LOG_TAG, "Fragment2 onResume");
    }
}