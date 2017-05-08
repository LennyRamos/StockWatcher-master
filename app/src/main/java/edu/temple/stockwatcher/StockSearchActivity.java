package edu.temple.stockwatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class StockSearchActivity extends Activity
{

    AutoCompleteTextView stock_search_text;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search);

        stock_search_text = (AutoCompleteTextView) findViewById(R.id.searchEditText);
        add_button = (Button) findViewById(R.id.addStock_button);

        add_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!stock_search_text.getText().toString().isEmpty())
                { // if the text field is not empty

                    System.out.println(stock_search_text.getText().toString());
                    String toSend = stock_search_text.getText().toString();
                    Intent passIntent = new Intent();
                    passIntent.putExtra("symbol", toSend);
                    setResult(RESULT_OK, passIntent);
                    finish();

                }
                else
                {
                    System.out.println("failed");
                }
            }
        });
    }
}//end of class StockSearchActivity
