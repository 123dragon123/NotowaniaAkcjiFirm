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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class CompareStockData extends AppCompatActivity
{
    TextView textViewFirma1Nazwa, textViewFirma1Data, textViewFirma1Otwarcie, textViewFirma1Zamkniecie, textViewFirma1Roznica, textViewFirma1Min, textViewFirma1Max, textViewFirma1Wolumien;

    TextView textViewFirma2Nazwa, textViewFirma2Data, textViewFirma2Otwarcie, textViewFirma2Zamkniecie, textViewFirma2Roznica, textViewFirma2Min, textViewFirma2Max, textViewFirma2Wolumien;

    TextView textViewPorownanieData, textViewPorownanieOtwarcie, textViewPorownanieZamkniecie, textViewPorownanieRoznica, textViewPorownanieMax, textViewPorownanieMin, textViewPorownanieWolumien;

    ProgressBar progressBar;

    Vector<String> dane1 = new Vector<>();
    Vector<String> dane2 = new Vector<>();

    private double round(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (double) (int) ((tmp - (int) tmp) >= 0.5 ? tmp + 1 : tmp) / pow;
    }

    class GetRequest extends AsyncTask<Void, Void, Void>
    {
        private String url1;
        private String url2;
        private String answer;

        GetRequest(String url1, String url2)
        {
            this.url1 = url1;
            this.url2 = url2;
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
                URL u1 = new URL(url1);
                URLConnection conn = u1.openConnection();
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

                byte[] contents = new byte[1024];
                int bytesRead;

                answer = "";

                while((bytesRead = in.read(contents)) != -1)
                {
                    answer += new String(contents, 0, bytesRead);
                }

                int pos = 0;

                String line;

                for(int i=0; i<answer.length(); i++) {
                    if (answer.charAt(i) == '\n') {
                        line = answer.substring(pos, i);
                        //System.out.println("Line added");
                        pos = i+1;
                        dane1.add(line);
                    }
                }
            }

            catch (IOException e) {
                // TODO: handle exception
            }

            try
            {
                URL u1 = new URL(url2);
                URLConnection conn = u1.openConnection();
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

                byte[] contents = new byte[1024];
                int bytesRead;

                answer = "";

                while((bytesRead = in.read(contents)) != -1)
                {
                    answer += new String(contents, 0, bytesRead);
                }

                int pos = 0;

                String line;

                for(int i=0; i<answer.length(); i++) {
                    if (answer.charAt(i) == '\n') {
                        line = answer.substring(pos, i);
                        //System.out.println("Line added");
                        pos = i+1;
                        dane2.add(line);
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

            if(dane1.size() == 0) {
                Toast.makeText(CompareStockData.this, "Firma nie istnieje", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            if(dane1.size() == 1)
            {
                Toast.makeText(CompareStockData.this, "Brak danych w tym okresie", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {

                double otw=0, zam=0;
                FormatowanieDanych formDane = new FormatowanieDanych();

                Vector<String> tabela = new Vector<>();
                tabela = formDane.sformatowac(dane1.elementAt(0));

                Vector<String> znaczenia = new Vector<>();

                znaczenia = formDane.sformatowac(dane1.elementAt(1));
                for(int i=0; i<tabela.size(); i++)
                {
                    switch(tabela.elementAt(i))
                    {
                        case "Date":
                        {
                            textViewFirma1Data.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Open":
                        {
                            textViewFirma1Otwarcie.setText(znaczenia.elementAt(i));
                            otw = Double.parseDouble(znaczenia.elementAt(i));
                            break;
                        }
                        case "High":
                        {
                            textViewFirma1Max.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Low":
                        {
                            textViewFirma1Min.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Close":
                        {
                            textViewFirma1Zamkniecie.setText(znaczenia.elementAt(i));
                            zam = Double.parseDouble(znaczenia.elementAt(i));
                            break;
                        }
                        case "Volume":
                        {
                            textViewFirma1Wolumien.setText(znaczenia.elementAt(i));
                            break;
                        }
                    }
                }

                double rozn = zam - otw;
                if(rozn>0)
                {
                    textViewFirma1Roznica.setTextColor(Color.parseColor("#3CAA3C"));
                    textViewFirma1Roznica.setText(textViewFirma1Roznica.getText() + "&#x25B2");
                }
                if(rozn<0)
                {
                    textViewFirma1Roznica.setTextColor(Color.parseColor("#F63D2C"));
                    textViewFirma1Roznica.setText(textViewFirma1Roznica.getText() + "&#x25BC");
                }
                textViewFirma1Roznica.setText(String.valueOf(round(rozn, 2)));

            }
            dane1.clear();

            //---------------------

            if(dane2.size() == 0) {
                Toast.makeText(CompareStockData.this, "Firma nie istnieje", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            if(dane2.size() == 1)
            {
                Toast.makeText(CompareStockData.this, "Brak danych w tym okresie", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {

                double otw=0, zam=0;
                FormatowanieDanych formDane = new FormatowanieDanych();

                Vector<String> tabela = new Vector<>();
                tabela = formDane.sformatowac(dane2.elementAt(0));

                Vector<String> znaczenia = new Vector<>();

                znaczenia = formDane.sformatowac(dane2.elementAt(1));
                for(int i=0; i<tabela.size(); i++)
                {
                    switch(tabela.elementAt(i))
                    {
                        case "Date":
                        {
                            textViewFirma2Data.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Open":
                        {
                            textViewFirma2Otwarcie.setText(znaczenia.elementAt(i));
                            otw = Double.parseDouble(znaczenia.elementAt(i));
                            break;
                        }
                        case "High":
                        {
                            textViewFirma2Max.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Low":
                        {
                            textViewFirma2Min.setText(znaczenia.elementAt(i));
                            break;
                        }
                        case "Close":
                        {
                            textViewFirma2Zamkniecie.setText(znaczenia.elementAt(i));
                            zam = Double.parseDouble(znaczenia.elementAt(i));
                            break;
                        }
                        case "Volume":
                        {
                            textViewFirma2Wolumien.setText(znaczenia.elementAt(i));
                            break;
                        }
                    }
                }

                double rozn = zam - otw;
                if(rozn>0)
                {
                    textViewFirma2Roznica.setTextColor(Color.parseColor("#3CAA3C"));
                    textViewFirma2Roznica.setText(textViewFirma2Roznica.getText() + "&#x25B2");
                }
                if(rozn<0)
                {
                    textViewFirma2Roznica.setTextColor(Color.parseColor("#F63D2C"));
                    textViewFirma2Roznica.setText(textViewFirma2Roznica.getText() + "&#x25BC");
                }
                textViewFirma2Roznica.setText(String.valueOf(round(rozn, 2)));

            }
            dane2.clear();

            //------------------Ten kod wykonuje porównanie danych, sformatowanie oraz wyświetlenie na ekranie
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            try
            {
                Date data1 = sdf.parse(textViewFirma1Data.getText().toString());
                Date data2 = sdf.parse(textViewFirma2Data.getText().toString());
                long difference = data1.getTime() - data2.getTime();
                long days = difference / (24 * 60 * 60 * 1000);
                textViewPorownanieData.setText(String.valueOf(Math.abs(days)) + " dni");
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            double f1, f2;
            f1 = Double.valueOf(textViewFirma1Otwarcie.getText().toString());
            f2 = Double.valueOf(textViewFirma2Otwarcie.getText().toString());
            textViewPorownanieOtwarcie.setText(String.valueOf(round(f1-f2, 2)));
            if((f1-f2)>=0)
            {
                textViewPorownanieOtwarcie.setTextColor(Color.parseColor("#3CAA3C"));
            }
            else
            {
                textViewPorownanieOtwarcie.setTextColor(Color.parseColor("#F63D2C"));
            }

            f1 = Double.valueOf(textViewFirma1Zamkniecie.getText().toString());
            f2 = Double.valueOf(textViewFirma2Zamkniecie.getText().toString());
            textViewPorownanieZamkniecie.setText(String.valueOf(round(f1-f2, 2)));
            if((f1-f2)>=0)
            {
                textViewPorownanieZamkniecie.setTextColor(Color.parseColor("#3CAA3C"));
            }
            else
            {
                textViewPorownanieZamkniecie.setTextColor(Color.parseColor("#F63D2C"));
            }

            f1 = Double.valueOf(textViewFirma1Roznica.getText().toString());
            f2 = Double.valueOf(textViewFirma2Roznica.getText().toString());
            textViewPorownanieRoznica.setText(String.valueOf(round(f1-f2, 2)));
            if((f1-f2)>=0)
            {
                textViewPorownanieRoznica.setTextColor(Color.parseColor("#3CAA3C"));
            }
            else
            {
                textViewPorownanieRoznica.setTextColor(Color.parseColor("#F63D2C"));
            }

            f1 = Double.valueOf(textViewFirma1Min.getText().toString());
            f2 = Double.valueOf(textViewFirma2Min.getText().toString());
            textViewPorownanieMin.setText(String.valueOf(round(f1-f2, 2)));
            if((f1-f2)>=0)
            {
                textViewPorownanieMin.setTextColor(Color.parseColor("#3CAA3C"));
            }
            else
            {
                textViewPorownanieMin.setTextColor(Color.parseColor("#F63D2C"));
            }

            f1 = Double.valueOf(textViewFirma1Max.getText().toString());
            f2 = Double.valueOf(textViewFirma2Max.getText().toString());
            textViewPorownanieMax.setText(String.valueOf(round(f1-f2, 2)));
            if((f1-f2)>=0)
            {
                textViewPorownanieMax.setTextColor(Color.parseColor("#3CAA3C"));
            }
            else
            {
                textViewPorownanieMax.setTextColor(Color.parseColor("#F63D2C"));
            }

            f1 = Double.valueOf(textViewFirma1Wolumien.getText().toString());
            f2 = Double.valueOf(textViewFirma2Wolumien.getText().toString());
            textViewPorownanieWolumien.setText(String.valueOf(round(f1-f2, 2)));
            if((f1-f2)>=0)
            {
                textViewPorownanieWolumien.setTextColor(Color.parseColor("#3CAA3C"));
            }
            else
            {
                textViewPorownanieWolumien.setTextColor(Color.parseColor("#F63D2C"));
            }
            //------------------
            progressBar.setVisibility(View.GONE);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_stock_data);

        Intent intent = getIntent();

        String nazwaFirmy1 = intent.getStringExtra("nazwaFirmy1");
        String nazwaFirmy2 = intent.getStringExtra("nazwaFirmy2");
        String firma1Data = intent.getStringExtra("data1");
        String firma2Data = intent.getStringExtra("data2");

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        textViewFirma1Nazwa = (TextView)findViewById(R.id.textViewFirma1Nazwa);
        textViewFirma1Data = (TextView)findViewById(R.id.textViewFirma1Data);
        textViewFirma1Otwarcie = (TextView)findViewById(R.id.textViewFirma1Otwarcie);
        textViewFirma1Zamkniecie = (TextView)findViewById(R.id.textViewFirma1Zamkniecie);
        textViewFirma1Roznica = (TextView)findViewById(R.id.textViewFirma1Roznica);
        textViewFirma1Max = (TextView)findViewById(R.id.textViewFirma1Max);
        textViewFirma1Min = (TextView)findViewById(R.id.textViewFirma1Min);
        textViewFirma1Wolumien = (TextView)findViewById(R.id.textViewFirma1Wolumien);

        textViewFirma2Nazwa = (TextView)findViewById(R.id.textViewFirma2Nazwa);
        textViewFirma2Data = (TextView)findViewById(R.id.textViewFirma2Data);
        textViewFirma2Otwarcie = (TextView)findViewById(R.id.textViewFirma2Otwarcie);
        textViewFirma2Zamkniecie = (TextView)findViewById(R.id.textViewFirma2Zamkniecie);
        textViewFirma2Roznica = (TextView)findViewById(R.id.textViewFirma2Roznica);
        textViewFirma2Max = (TextView)findViewById(R.id.textViewFirma2Max);
        textViewFirma2Min = (TextView)findViewById(R.id.textViewFirma2Min);
        textViewFirma2Wolumien = (TextView)findViewById(R.id.textViewFirma2Wolumien);

        textViewPorownanieData = (TextView)findViewById(R.id.textViewPorownanieData);
        textViewPorownanieOtwarcie = (TextView)findViewById(R.id.textViewPorownanieOtwarcie);
        textViewPorownanieZamkniecie = (TextView)findViewById(R.id.textViewPorownanieZamkniecie);
        textViewPorownanieRoznica = (TextView)findViewById(R.id.textViewPorownanieRoznica);
        textViewPorownanieMax = (TextView)findViewById(R.id.textViewPorownanieMax);
        textViewPorownanieMin = (TextView)findViewById(R.id.textViewPorownanieMin);
        textViewPorownanieWolumien = (TextView)findViewById(R.id.textViewPorownanieWolumien);

        textViewFirma1Nazwa.setText(nazwaFirmy1.toUpperCase());
        textViewFirma2Nazwa.setText(nazwaFirmy2.toUpperCase());

        String requestURL1 = "https://www.quandl.com/api/v3/datasets/WIKI/" + nazwaFirmy1 + "/data.csv?api_key=9sxGPaZxxwqAxSYK26q2&start_date=" + firma1Data + "&end_date=" + firma1Data;

        String requestURL2 = "https://www.quandl.com/api/v3/datasets/WIKI/" + nazwaFirmy2 + "/data.csv?api_key=9sxGPaZxxwqAxSYK26q2&start_date=" + firma2Data + "&end_date=" + firma2Data;

        GetRequest request = new GetRequest(requestURL1, requestURL2);
        request.execute();
    }



}
