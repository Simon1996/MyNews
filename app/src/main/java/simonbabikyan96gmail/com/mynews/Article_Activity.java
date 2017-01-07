package simonbabikyan96gmail.com.mynews;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.Attributes;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.R.attr.name;
import static android.R.attr.visible;

public class Article_Activity extends AppCompatActivity {
    TextView title;
    TextView article;
    TextView newsurl;
    ImageView img;
    ImageButton share;
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;
    boolean shareability;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);


        share = (ImageButton)findViewById(R.id.share);
        title = (TextView)findViewById(R.id.title_title);
        article = (TextView)findViewById(R.id.article_article);
        img = (ImageView)findViewById(R.id.img);
        //newsurl = (TextView)findViewById(R.id.url);
        final Intent intent = getIntent();
        final String count = intent.getStringExtra("count");

        final String tTitle = intent.getStringExtra("title");
        final String aArticle = intent.getStringExtra("article");
        final String aArticleurl = intent.getStringExtra("newsurl");

        final String tTitleTwo = intent.getStringExtra("title");
        final String aArticleTwo = intent.getStringExtra("article");
        final String aArticleurlTwo = intent.getStringExtra("newsurl");


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //in the toolbar

        if(count == "Fragment1" ) {
            title.setText(tTitle);
            article.setText(aArticle);
            shareability = true;//для sharingIntent. Если tru то устанавливаем другие данные для отправки(Фрагмента 1)
        }
        else {
            title.setText(tTitleTwo);
            article.setText(aArticleTwo);
            shareability = false;//для sharingIntent. Если false то устанавливаем другие данные для отправки(Фрагмента 2)
        }
        //Устанавливаем полученную картинку
        Picasso.with(this)
                .load(intent.getStringExtra("image"))
                .transform(new CropSquareTransformation())
                .placeholder(R.drawable.loader_image)
                .error(R.drawable.not_found)
                .into(img);

        MaterialFavoriteButton toolbarFavorite = new MaterialFavoriteButton.Builder(this) //
                .favorite(false)
                .color(MaterialFavoriteButton.STYLE_WHITE)
                .type(MaterialFavoriteButton.STYLE_HEART)
                .rotationDuration(400)
                .create();

        MaterialFavoriteButton materialFavoriteButtonNice =
                (MaterialFavoriteButton) findViewById(R.id.favorite);
        materialFavoriteButtonNice.setFavorite(false, true);
        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Article added to Favorites", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Article removed from Favorites", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
//        final Bitmap bitimg = getBitmapFromURL(intent.getStringExtra("image"));

        materialFavoriteButtonNice.setOnFavoriteAnimationEndListener(
                new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
                            if(favorite ) {

                                // создаем объект для данных
                                ContentValues cv = new ContentValues();

                                // получаем данные из полей ввода
                                String tit = title.getText().toString();
                                String art = article.getText().toString();



                                // подключаемся к БД
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                                // подготовим данные для вставки в виде пар: наименование столбца - значение

                                cv.put("title", tit);
                                cv.put("article", art);

                                // вставляем запись и получаем ее ID
                                long rowID = db.insert("mytable", null, cv);
                                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                                Toast toast = Toast.makeText(getApplicationContext(),
                                    "Добавляем в кеш", Toast.LENGTH_SHORT);
                            toast.show();
                                db.close();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Удаляем из кеша", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        //закрываем подклбючение к базе данных
                        dbHelper.close();
                    }
                });
        //Share Заголовок и ссылку стандартными средствами
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
//                String shareBody = "";
//                String shareSub = "Your subject here";
if(shareability = true) {
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tTitle);
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aArticleurl);
}
                else {
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tTitleTwo);
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aArticleurlTwo);
                }
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        //Проверяем, если данные отправлены с фрагмента 1 то делаем кнопку "Добавить в избранное VISIBLE
        //Если данные отправленны с фрагмента 2, то делаем кнопку Добавить в избраное INVISIBLE

    }
//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    private void loadNotes() {
//        RealmResults<News> notes = mRealm.where(News.class).findAllSorted("date", Sort.ASCENDING);
//        NoteRecyclerViewAdapter noteAdapter = new NoteRecyclerViewAdapter(getBaseContext(), notes);
//        mNotes.setAdapter(noteAdapter);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mRealm.close(); // Remember to close Realm when done.
    }
//    private void insertNote(final String noteText ) {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                News note = new News();
//                note.setTitle(noteText);
//                //note.setArticle(notetext);
//                mRealm.copyToRealm(note);
//            }
//        });
//    }
}
