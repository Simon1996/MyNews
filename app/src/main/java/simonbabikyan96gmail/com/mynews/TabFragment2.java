package simonbabikyan96gmail.com.mynews;

/**
 * Created by Admin on 23.12.2016.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class TabFragment2 extends Fragment {
    RecyclerView recyclerView;
    MyAdapter myAdapterTwo;
    ArrayList<News> arrayList;

    TextView title;
    TextView article;
    ImageView img;

    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    String count = "Fragment2";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleTwo);
        myAdapterTwo = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapterTwo);
        arrayList.add(new News());

        title = (TextView)getActivity().findViewById(R.id.title);
        article = (TextView)getActivity().findViewById(R.id.desc);
        img = (ImageView)getActivity().findViewById(R.id.img);


//        mRealmConfig = new RealmConfiguration
//                .Builder(getContext())
//                .build();
//        Realm.setDefaultConfiguration(mRealmConfig);
//        mRealm = Realm.getDefaultInstance();

//
//        RealmConfiguration myConfig = new RealmConfiguration.Builder(getContext())
//                .name("default.realm")
//                .inMemory()
//                .build();
        dbHelper = new DBHelper(getContext());
        return view;



    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        public MyAdapter() {

            arrayList = new ArrayList<>();

            notifyDataSetChanged();
        }

        public void addNews(News news) {
            //arrayList.clear();
            arrayList.add(news);
//          notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_context_2, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.article.setText(arrayList.get(position).getArticle());
            holder.title.setText(arrayList.get(position).getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(getActivity(), Article_Activity.class);
                    intent.putExtra("count", count);
                    intent.putExtra("title", arrayList.get(position).getTitle());
                    intent.putExtra("article", arrayList.get(position).getArticle());
                    intent.putExtra("image", arrayList.get(position).getImageUrl());
                    intent.putExtra("newsurl", arrayList.get(position).getArticleUrl());
                    startActivity(intent);
                }
            });
//            ContentValues cv = new ContentValues();

            // подключаемся к БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Log.d(LOG_TAG, "--- Rows in mytable: ---");
            // делаем запрос всех данных из таблицы mytable, получаем Cursor
            Cursor c = db.query("mytable", null, null, null, null, null, null);

            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, вернется false
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("title");
                int emailColIndex = c.getColumnIndex("article");
                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d(LOG_TAG,
                            "ID = " + c.getInt(idColIndex) +
                                    ", title = " + c.getString(nameColIndex) +
                                    ", article = " + c.getString(emailColIndex));

                    String title = c.getString(nameColIndex);
                    String article = c.getString(emailColIndex);

                    String img = null;

                    News news = new News(title,article, img, null);
                    myAdapterTwo.addNews(news);

//                    myAdapterTwo.notifyDataSetChanged();
//                    title.setText(c.getString(nameColIndex));
//                    article.setText(c.getString(emailColIndex));
                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());
            } else
                Log.d(LOG_TAG, "0 rows");
            c.close();
            dbHelper.close();

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