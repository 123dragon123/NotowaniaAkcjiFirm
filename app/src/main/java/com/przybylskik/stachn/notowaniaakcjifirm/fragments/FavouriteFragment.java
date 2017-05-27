package com.przybylskik.stachn.notowaniaakcjifirm.fragments;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.constraint.ConstraintLayout;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.przybylskik.stachn.notowaniaakcjifirm.GetStocksData;
        import com.przybylskik.stachn.notowaniaakcjifirm.R;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

public class FavouriteFragment extends android.app.Fragment implements  View.OnClickListener
{
    List<String> array = new ArrayList<>(); // Lista, w której przechowujemy ulubione firmy
    ArrayAdapter<String> adapter; //Array adapter, co przechowuje liste firm dla obiektu Spinner

    //------Tworzenie obiektów interfejsu użytkownika
    EditText editTextNazwaFirmy;
    Button buttonDodaj, buttonUsun, buttonPobierzDane;
    Spinner spinnerLista;
    //------

    private Date yesterday()//Funkcja zwraca datę, która była 7 dni temu
    {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    public FavouriteFragment()
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
        ConstraintLayout cl = (ConstraintLayout)inflater.inflate(R.layout.fragment_favourite, container, false); //Inicjalizacja obiektu za pomocą którego otrzymujemy dostęp do obiektów na ekranie
        //------Inicjalizacja obiektów GUI
        editTextNazwaFirmy = (EditText)cl.findViewById(R.id.editTextNazwaFirmy);

        buttonDodaj = (Button)cl.findViewById(R.id.buttonDodaj);
        buttonDodaj.setOnClickListener(this);
        buttonUsun = (Button)cl.findViewById(R.id.buttonUsun);
        buttonUsun.setOnClickListener(this);
        buttonPobierzDane = (Button)cl.findViewById(R.id.buttonPobierzDane);
        buttonPobierzDane.setOnClickListener(this);

        spinnerLista = (Spinner)cl.findViewById(R.id.spinnerLista);
        //------

        //------Ten kod pobiera listę firm z pliku w pamięci telefonu, jeżeli ten plik istneije
        try {
            FileInputStream fileIn = this.getActivity().openFileInput("ListaUlubionych.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            array = (List<String>) in.readObject();
            Log.i("palval", "dir.exists()");
            in.close();
            fileIn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //------

        //------Ten kod przetwarza listę firm w ArrayAdapter i aktualizuje Spinner
        final String[] lista = new String[ array.size() ];
        array.toArray(lista);

        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLista.setAdapter(adapter);
        //------

        return cl;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void onClick(View v)//OnClickListener
    {
        switch(v.getId())//W przypadku kiedy naćiśnięto
        {
            case R.id.buttonDodaj://"Dodaj firme"
            {
                if(array.contains(editTextNazwaFirmy.getText().toString().toUpperCase()))//Jeżeli firma już jest na liście
                {
                    Toast.makeText(this.getActivity(), "Firma już zapisana do ulubionych", Toast.LENGTH_SHORT).show();//Wyświetli się odpowiedni komunikat
                }
                else
                {
                    array.add(editTextNazwaFirmy.getText().toString().toUpperCase());

                    //------Aktualizacja ArrayAdapter oraz Spinner
                    final String[] lista = new String[ array.size() ];
                    array.toArray(lista);

                    adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, lista);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLista.setAdapter(adapter);
                    //------

                    //------Ten kod serializuje i zapisuje listę firm w pamięci telefonu
                    try
                    {
                        FileOutputStream fileOut = this.getActivity().openFileOutput("ListaUlubionych.dat", Context.MODE_PRIVATE);
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(array);
                        out.close();
                        fileOut.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //------
                }

                break;
            }

            case R.id.buttonPobierzDane: //"Pobierz Dane"
            {
                final String[] lista = new String[ array.size() ];
                array.toArray(lista);

                //Tworzymy połączenie z aktywnościa "GetStocksData"
                Intent intent = new Intent(this.getActivity(), GetStocksData.class);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(yesterday());//Otrzymujemy datę 7 dni tenu

                //Przekazujemy dodatkowe paramenty do nowej aktywności
                intent.putExtra("nazwaFirmy", lista[spinnerLista.getSelectedItemPosition()]);
                intent.putExtra("dataStart", date);
                startActivity(intent);//Uruchamiamy aktywność
                break;
            }

            case R.id.buttonUsun:
            {
                array.remove(spinnerLista.getSelectedItemPosition());//Usuwamy zaznaczoną firmę z listy

                //------Aktualizacja ArrayAdapter oraz Spinner
                final String[] lista = new String[ array.size() ];
                array.toArray(lista);

                adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, lista);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLista.setAdapter(adapter);
                //------

                //------Ten kod serializuje i zapisuje listę firm w pamięci telefonu
                try {
                    FileOutputStream fileOut = this.getActivity().openFileOutput("ListaUlubionych.dat", Context.MODE_PRIVATE);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(array);
                    out.close();
                    fileOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //------

                break;
            }

            default:
            {
                break;
            }
        }
    }
}
