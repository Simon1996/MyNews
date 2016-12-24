package simonbabikyan96gmail.com.mynews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Article_Activity extends AppCompatActivity {
    TextView title;
    TextView article;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_);

        title = (TextView)findViewById(R.id.title_title);
        article = (TextView)findViewById(R.id.article_article);
        img = (ImageView)findViewById(R.id.img);

        Intent intent = getIntent();

        String tTitle = intent.getStringExtra("title");
        String aArticle = intent.getStringExtra("article");
        String iImage = intent.getStringExtra("image");

        title.setText(tTitle);
        article.setText(aArticle);
        //img.setImageURI();


        Picasso.with(this)
                .load(intent.getStringExtra("image"))
                .transform(new CropSquareTransformation())
                .placeholder(R.drawable.loader_image)
                .error(R.drawable.not_found)
                .into(img);
    }
}
