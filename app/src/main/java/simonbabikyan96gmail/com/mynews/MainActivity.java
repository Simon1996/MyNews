package simonbabikyan96gmail.com.mynews;

/**
 * Created by Admin on 23.12.2016.
 */
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

        RecyclerView recyclerView;
        MyAdapter myAdapter;


    //массив иконок для табов
    private int[] tabIcons = {
            R.drawable.news_icon,
            R.drawable.favorite_icon,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("News"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorite News"));


        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//// TODO: тут программа крашится, нужно проверить ошибку

//        recyclerView = (RecyclerView)findViewById(R.id.recycle);
//        myAdapter = new MyAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(myAdapter);
//        MyAsynk asynk = new MyAsynk();
//        asynk.execute();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });


    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<News> arrayList;

        public MyAdapter() {
            arrayList = new ArrayList<>();
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
                article = (TextView) itemView.findViewById(R.id.desc);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }


//// TODO: Сделать AsynksTast JSON объекта

//    class MyAsynk extends AsyncTask<Void,Void,StringBuilder> {
//
//        @Override
//        //работа в бекграунде
//        protected StringBuilder doInBackground(Void... voids) {
//            StringBuilder stringBuilder = new StringBuilder();
//            String key = "0aa2713d5a1a4aad9a914c9294f6a22b";
//            try {
//                URL url = new URL("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=" + key);
//                URLConnection uc = url.openConnection();
//                uc.connect();
//                BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
//                int ch;
//                while ((ch = in.read()) != -1) {
//                    stringBuilder.append((char) ch);
//                }
//            } catch (Exception e) {
//            }
//            return stringBuilder;
//        }
//
//        @Override
//        protected void onPostExecute(StringBuilder stringBuilder) {
//            News news = new News(stringBuilder.toString(), "", "");
////            myAdapter.addNews(news);
//            try {
//                JSONObject json = new JSONObject(stringBuilder.toString());
//                JSONArray array = json.getJSONArray("article");
//            } catch (Exception e) {
//            }
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}