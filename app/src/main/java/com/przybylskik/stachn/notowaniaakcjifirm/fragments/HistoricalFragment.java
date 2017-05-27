package com.przybylskik.stachn.notowaniaakcjifirm.fragments;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.constraint.ConstraintLayout;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.przybylskik.stachn.notowaniaakcjifirm.GetStocksData;
        import com.przybylskik.stachn.notowaniaakcjifirm.R;

        import java.text.SimpleDateFormat;
        import java.util.Date;

public class HistoricalFragment extends android.app.Fragment implements View.OnClickListener
{
    //------Towrzymy obiekty interfejsu użytkownika
    EditText editTextDzien, editTextMiesiac, editTextRok, editTextNazwaFirmy;
    Button buttonPobierzDane;
    //------

    public HistoricalFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //------Inicjalizacja obiektów interfejsu użytkownika
        ConstraintLayout cl = (ConstraintLayout)inflater.inflate(R.layout.fragment_historical, container, false);
        buttonPobierzDane = (Button)cl.findViewById(R.id.buttonPobierzDane);
        buttonPobierzDane.setOnClickListener(this);

        editTextDzien = (EditText)cl.findViewById(R.id.editTextDzien);
        editTextMiesiac = (EditText)cl.findViewById(R.id.editTextMiesiac);
        editTextRok = (EditText)cl.findViewById(R.id.editTextRok);
        editTextNazwaFirmy = (EditText)cl.findViewById(R.id.editTextNazwaFirmy);
        //------

        return cl;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v)//W przypadku kiedy naciśnięto
    {
        switch(v.getId())
        {
            case R.id.buttonPobierzDane: //Pobierz dane
            {
                String data = editTextRok.getText().toString()+"-"+editTextMiesiac.getText().toString()+"-"+editTextDzien.getText().toString();//Zapisujemy datę do zmiennej
                try
                {
                    //------Ten kod sprawdza, czy istnieje taka data
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    Date date = sdf.parse(data);
                    //------

                    //------Jeżeli istnieje to przekazuje dane do aktywności GetStockData
                    Intent intent = new Intent(this.getActivity(), GetStocksData.class);
                    intent.putExtra("nazwaFirmy", editTextNazwaFirmy.getText().toString());
                    intent.putExtra("dataStart", data);
                    intent.putExtra("dataEnd", data);

                    startActivity(intent);
                }
                catch (java.text.ParseException e)//Jeżeli data wskazana niepoprawnie to otrzymujemy Exception
                {
                    Toast.makeText(this.getActivity(), "Niepoprawna data", Toast.LENGTH_SHORT).show();//Wyświetlamy odowiedni komunikat
                }
                break;
            }

            default:
            {

            }
        }
    }
}