package com.przybylskik.stachn.notowaniaakcjifirm;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class GetStocksData extends AppCompatActivity {

    Vector<String> dane = new Vector<>();

    ProgressBar progressBar;
    TextView textViewNazwaFirmy, textViewData, textViewOtwarcie, textViewZamkniecie, textViewMin, textViewMax, textViewWolumien, textViewRoznica;

    private Date yesterday()
    {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    private double round(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (double) (int) ((tmp - (int) tmp) >= 0.5 ? tmp + 1 : tmp) / pow;
    }

    class GetRequest extends AsyncTask<Void, Void, Void>
    {
        private String url;
        private String answer = "";

        GetRequest(String url)
        {
            this.url = url;
        }

        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                URL u1 = new URL(url);
                URLConnection conn = u1.openConnection();
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

                byte[] contents = new byte[1024];
                int bytesRead;

                while((bytesRead = in.read(contents)) != -1)
                {
                    answer += new String(contents, 0, bytesRead);
                }

                int pos = 0;

                String line;

                for(int i=0; i<answer.length(); i++) {
                    if (answer.charAt(i) == '\n') {
                        line = answer.substring(pos, i);
                        pos = i+1;
                        dane.add(line);
                    }
                }

            }

            catch (IOException e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);



            if(dane.size() == 0) {
                Toast.makeText(GetStocksData.this, "Firma nie istnieje", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            if(dane.size() == 1)
            {
                Toast.makeText(GetStocksData.this, "Brak danych w tym okresie", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {

                double otw=0, zam=0;
                FormatowanieDanych formDane = new FormatowanieDanych();

                Vector<String> tabela = new Vector<>();
                tabela = formDane.sformatowac(dane.elementAt(0));

                Vector<String> znaczenia = new Vector<>();

                znaczenia = formDane.sformatowac(dane.elementAt(1));
                for(int i=0; i<tabela.size(); i++)
                {
                    switch(tabela.elementAt(i))
                    {
                        case "Date":
                        {
                            textViewData.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Open":
                        {
                            textViewOtwarcie.setText(znaczenia.elementAt(i));
                            otw = Double.parseDouble(znaczenia.elementAt(i));
                            break;
                        }
                        case "High":
                        {
                            textViewMax.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Low":
                        {
                            textViewMin.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Close":
                        {
                            textViewZamkniecie.setText(znaczenia.elementAt(i));
                            zam = Double.parseDouble(znaczenia.elementAt(i));
                            break;
                        }
                        case "Volume":
                        {
                            textViewWolumien.setText(znaczenia.elementAt(i));
                            break;
                        }
                    }
                }
                double rozn = zam - otw;
                if(rozn>0)
                {
                    textViewRoznica.setTextColor(Color.parseColor("#3CAA3C"));
                    textViewRoznica.setText(textViewRoznica.getText() + "&#x25B2");
                }
                if(rozn<0)
                {
                    textViewRoznica.setTextColor(Color.parseColor("#F63D2C"));
                    textViewRoznica.setText(textViewRoznica.getText() + "&#x25BC");
                }
                textViewRoznica.setText(String.valueOf(round(rozn, 2)));
            }
            progressBar.setVisibility(View.INVISIBLE);
            dane.clear();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stocks_data);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        textViewNazwaFirmy = (TextView)findViewById(R.id.textViewNazwaFirmy);
        textViewData = (TextView)findViewById(R.id.textViewData);
        textViewOtwarcie = (TextView)findViewById(R.id.textViewOtwarcie);
        textViewZamkniecie = (TextView)findViewById(R.id.textViewZamkniecie);
        textViewMin = (TextView)findViewById(R.id.textViewMin);
        textViewMax = (TextView)findViewById(R.id.textViewMax);
        textViewWolumien = (TextView)findViewById(R.id.textViewWolumien);
        textViewRoznica = (TextView)findViewById(R.id.textViewRoznica);

        Intent intent = getIntent();

        String nazwa = intent.getStringExtra("nazwaFirmy");
        String dataStart = intent.getStringExtra("dataStart");
        String dataEnd = intent.getStringExtra("dataEnd");
        textViewNazwaFirmy.setText(nazwa.toUpperCase());

        String requestURL = "https://www.quandl.com/api/v3/datasets/WIKI/" + nazwa + "/data.csv?api_key=9sxGPaZxxwqAxSYK26q2&start_date=" + dataStart;

        if(dataEnd != null)
        {
            requestURL = requestURL+"&end_date="+dataEnd;
        }

        GetRequest request = new GetRequest(requestURL);

        request.execute();
    }

}
