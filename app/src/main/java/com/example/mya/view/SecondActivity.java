package com.example.mya.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.mya.R;
import com.example.mya.model.Notebook;
import com.example.mya.viewmodel.Adapter;
import com.example.mya.viewmodel.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    // создание полей
    private RecyclerView recyclerView; // поле для списка RecyclerView
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDel;// поле для кнопки добавить новую заметку

    private List<Notebook> notesList; // поле для контейнера списка заметок

    private DatabaseHelper database; // поле работы с БД
    private Adapter adapter; // поле для адаптера


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // присваивание id полям
        recyclerView = findViewById(R.id.recycler_list);
        fabAdd = findViewById(R.id.fabAdd);
        fabDel = findViewById(R.id.fabDel);

        notesList = new ArrayList<>(); // выделение памяти и задание типа контейнера для списка заметок
        database = new DatabaseHelper(this); // выделение памяти и задание текущего контекста работы с БД

        // считывание данных из БД и запись их в коллекцию notesList
        fetchAllNotes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // задание структуры вывода данных в recyclerView
        adapter = new Adapter(this, SecondActivity.this, notesList); // инициализация адаптера и передача в рего данных из БД
        recyclerView.setAdapter(adapter); // передача в recyclerView адаптер

        // обработка нажатия кнопки создания новой заметки
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // переключение на новую активность
                Intent intent = new Intent(SecondActivity.this,  AddNotesActivity.class); // переключение на новую активность
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        fabDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               database.deleteAllNotes();// удаление записи БД
              
                  // создание намерения переключения активности
                Intent intent = new Intent(SecondActivity.this, SecondActivity.class); // переключение на новую активность
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity (intent);
            }
        });
    }


    // метод считывания из БД всех записей
    public void fetchAllNotes(){
        // чтение БД и запись данных в курсор
        Cursor cursor = database.readNotes();

        if (cursor.getCount() == 0) { // если данные отсутствую, то вывод на экран об этом тоста
            Toast.makeText(this, "Заметок нет", Toast.LENGTH_SHORT).show();
        } else { // иначе помещение их в контейнер данных notesList
            while (cursor.moveToNext()){
                // помещение в контейнер notesList из курсора данных
                notesList.add(new Notebook(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }
}
