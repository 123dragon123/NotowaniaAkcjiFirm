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

import com.przybylskik.stachn.notowaniaakcjifirm.CompareStockData;
import com.przybylskik.stachn.notowaniaakcjifirm.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompareFragment extends android.app.Fragment implements View.OnClickListener{

    //Tworzymy obiekty GIU
    EditText editTextNazwaFirmy1, editTextNazwaFirmy2;
    EditText editTextFirma1DD, editTextFirma1MM, editTextFirma1RRRR;
    EditText editTextFirma2DD, editTextFirma2MM, editTextFirma2RRRR;
    Button buttonPorownajDane;

    public CompareFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inicjalizacja obiektów GUI
        ConstraintLayout cl = (ConstraintLayout)inflater.inflate(R.layout.fragment_compare, container, false);

        editTextNazwaFirmy1 = (EditText)cl.findViewById(R.id.editTextNazwaFirmy1);
        editTextFirma1DD = (EditText)cl.findViewById(R.id.editTextFirma1DD);
        editTextFirma1MM = (EditText)cl.findViewById(R.id.editTextFirma1MM);
        editTextFirma1RRRR = (EditText)cl.findViewById(R.id.editTextFirma1RRRR);

        editTextNazwaFirmy2 = (EditText)cl.findViewById(R.id.editTextNazwaFirmy2);
        editTextFirma2DD = (EditText)cl.findViewById(R.id.editTextFirma2DD);
        editTextFirma2MM = (EditText)cl.findViewById(R.id.editTextFirma2MM);
        editTextFirma2RRRR = (EditText)cl.findViewById(R.id.editTextFirma2RRRR);

        buttonPorownajDane = (Button)cl.findViewById(R.id.buttonPorownajDane);
        buttonPorownajDane.setOnClickListener(this);

        return cl;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void onClick(View v) //W przypadku nacisnięcia"Porównaj dane"
    {
        switch(v.getId())
        {
            case R.id.buttonPorownajDane:
            {
                //Zapisujemy dane podane przez użytkownika w zmiennt
                String nazwaFirmy1 = editTextNazwaFirmy1.getText().toString();
                String nazwaFirmy2 = editTextNazwaFirmy2.getText().toString();

                String firma1Data = editTextFirma1RRRR.getText().toString()+"-"+editTextFirma1MM.getText().toString()+"-"+editTextFirma1DD.getText().toString();
                String firma2Data = editTextFirma2RRRR.getText().toString()+"-"+editTextFirma2MM.getText().toString()+"-"+editTextFirma2DD.getText().toString();

                try
                {
                    //------Sprawdzamy czy data jest poprawna
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    Date date;
                    date = sdf.parse(firma1Data);
                    date = sdf.parse(firma2Data);
                    //------

                    //Przekazujemy dane do aktywności CompareStockData
                    Intent intent = new Intent(this.getActivity(), CompareStockData.class);
                    intent.putExtra("nazwaFirmy1", nazwaFirmy1);
                    intent.putExtra("nazwaFirmy2",nazwaFirmy2);
                    intent.putExtra("data1", firma1Data);
                    intent.putExtra("data2", firma2Data);

                    startActivity(intent);

                }
                catch (java.text.ParseException e)
                {
                    Toast.makeText(this.getActivity(), "Niepoprawna data", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }
}
