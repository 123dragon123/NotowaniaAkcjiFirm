package com.przybylskik.stachn.notowaniaakcjifirm.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.przybylskik.stachn.notowaniaakcjifirm.FormatowanieDanych;
import com.przybylskik.stachn.notowaniaakcjifirm.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class StocksFragment extends android.app.Fragment implements View.OnClickListener
{
    Vector<String> dane = new Vector<>(); // Wektor, do którego zapisujemy otrzymane od serwera dane
    //------Obiekty aktywniści, które utworzone w pliku XML
    EditText editTextNazwaFirmy;
    Button buttonPobracDane;
    ProgressBar progressBar;
    TextView textViewData, textViewOtwarcie, textViewZamkniecie, textViewMin, textViewMax, textViewWolumien, textViewRoznica;
    //------

    private Date yesterday()//Funkcja zwraca datę, która była 7 dni temu
    {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    private double round(double number, int scale) {//Funkcja zaokrągla wartość do "scale" znaków po przecinku
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (double) (int) ((tmp - (int) tmp) >= 0.5 ? tmp + 1 : tmp) / pow;
    }

    class GetRequest extends AsyncTask<Void, Void, Void>//Klasa, która rozszera klase AsyncTask dla pobierania danych z serwera w osobnym wątku
    {
        private String url;//Link do danych
        private String answer = "";//Odpowiedź serwera

        GetRequest(String url)
        {
            this.url = url;
        }//Konstruktor klasy

        protected void onPreExecute()//Metoda klasy AsyncTask, która wykonuje się przed uruchamieniem wątku
        {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)//Metoda klasyAsyncTask, która wykonuje się w osobnym wątku
        {
            try
            {
                URL u1 = new URL(url);//Zapisujemy link w postaci URL
                URLConnection conn = u1.openConnection();//Tworzymy nowe połączenie
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());//Otrzymujemy odpowiedź od serwera

                //------Ten kod pozwala skonwertować BufferedInputStream w String
                byte[] contents = new byte[1024];
                int bytesRead;

                while((bytesRead = in.read(contents)) != -1)
                {
                    answer += new String(contents, 0, bytesRead);
                }
                //------

                int pos = 0;//Pozycja początku nowego wierszu

                String line;//Zmienna, w której przechowywujemy wierz tabeli z danymi

                //------Ten kod dzieli tabelę na wierszy i zapisuje do Vector<String> "dane"
                for(int i=0; i<answer.length(); i++)
                {
                    if (answer.charAt(i) == '\n') {
                        line = answer.substring(pos, i);
                        //System.out.println("Line added");
                        pos = i+1;
                        dane.add(line);
                    }
                }
                //------
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

            if(dane.size() == 0) //W przypadku kiedy ilość wierszy tabeli równa się 0 otrzymaliśmy pustą odpowiedz, firma nie istnieje
            {
                Toast.makeText(getActivity(), "Firma nie istnieje", Toast.LENGTH_SHORT).show();
                textViewData.setText("----");
                textViewMin.setText("----");
                textViewMax.setText("----");
                textViewWolumien.setText("----");
                textViewOtwarcie.setText("----");
                textViewZamkniecie.setText("----");
                textViewRoznica.setText("----");
                textViewRoznica.setTextColor(textViewData.getTextColors());
                textViewData.setText("----");
                textViewMin.setText("----");
                textViewMax.setText("----");
                textViewWolumien.setText("----");
                textViewOtwarcie.setText("----");
                textViewZamkniecie.setText("----");
                textViewRoznica.setText("----");
                textViewRoznica.setTextColor(textViewData.getTextColors());
            }
            else
            if(dane.size()==1)//W przypadku kiedy otrzymaliśmy tylko wierz z nazwami kolumn - brak danych za wskazany okres
            {
                Toast.makeText(getActivity(), "Brak danych w tym okresie", Toast.LENGTH_SHORT).show();
                textViewData.setText("----");
                textViewMin.setText("----");
                textViewMax.setText("----");
                textViewWolumien.setText("----");
                textViewOtwarcie.setText("----");
                textViewZamkniecie.setText("----");
                textViewRoznica.setText("----");
                textViewRoznica.setTextColor(textViewData.getTextColors());
                textViewData.setText("----");
                textViewMin.setText("----");
                textViewMax.setText("----");
                textViewWolumien.setText("----");
                textViewOtwarcie.setText("----");
                textViewZamkniecie.setText("----");
                textViewRoznica.setText("----");
                textViewRoznica.setTextColor(textViewData.getTextColors());
            }
            else //W przypadku kiedy otrzymaliśmy dane
            {

                double otw=0, zam=0; //Zmienne, w których zapiszemy dane o otwarciu oraz zamknięciu rynku
                FormatowanieDanych formDane = new FormatowanieDanych();//Obkiekt klasy FormatowanieDanych. Ta klasa pozwala podzielić wiersz tabeli na oddzielne dane

                Vector<String> tabela = new Vector<>(); //W ten wektor zapisujemy nazwy kolumn tabeli
                tabela = formDane.sformatowac(dane.elementAt(0)); //Metoda sformatowac() przyjmuje jeden z wiersz wektora i zwraca wector z nazwami zapisanymi w osobnych komórkach

                Vector<String> znaczenia = new Vector<>();//W ten wektor zapisujemy dane
                znaczenia = formDane.sformatowac(dane.elementAt(1));

                //------Ten kod szuka potrzebne dane i wyświetla na ekranie smartfonu
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
                //------
                //------Ten kod oblicza różnicę między otwarciem a zamknięciem rynku
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
                //------
            }
            progressBar.setVisibility(View.INVISIBLE);//Wyłączenie animacji na ekranie
            dane.clear();//Wyczyścienie danych
        }

    }

    public StocksFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ConstraintLayout cl = (ConstraintLayout)inflater.inflate(R.layout.fragment_stocks, container, false);//Inicjalizacja obiektu za pomocą którego otrzymujemy dostęp do obiektów na ekranie

        //------Inicjalizacja obiektów interfejsu użytkownika
        buttonPobracDane = (Button)cl.findViewById(R.id.buttonPobracDane);
        buttonPobracDane.setOnClickListener(this);
        editTextNazwaFirmy = (EditText)cl.findViewById(R.id.editTextNazwaFirmy);
        progressBar = (ProgressBar)cl.findViewById(R.id.progressBar);

        textViewData = (TextView)cl.findViewById(R.id.textViewData);
        textViewOtwarcie = (TextView)cl.findViewById(R.id.textViewOtwarcie);
        textViewZamkniecie = (TextView)cl.findViewById(R.id.textViewZamkniecie);
        textViewMin = (TextView)cl.findViewById(R.id.textViewMin);
        textViewMax = (TextView)cl.findViewById(R.id.textViewMax);
        textViewWolumien = (TextView)cl.findViewById(R.id.textViewWolumien);
        textViewRoznica = (TextView)cl.findViewById(R.id.textViewRoznica);
        //------

        return cl;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v)//OnClickListener dla przycisków
    {
        switch(v.getId())
        {
            case R.id.buttonPobracDane: //W przypadku kiedy nacisnięto "PobraC dane"
            {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(yesterday()); //Program oblicza datę 7 dni temu

                //Tworzymy obiekt klasy GetRequest dla pobrania danych z serweru w osobnym wątku
                GetRequest request = new GetRequest("https://www.quandl.com/api/v3/datasets/WIKI/" + editTextNazwaFirmy.getText() + "/data.csv?api_key=9sxGPaZxxwqAxSYK26q2&start_date=" + date);

                request.execute();//Start AsyncTask

                break;
            }

            default:
            {
                break;
            }
        }
    }
}
