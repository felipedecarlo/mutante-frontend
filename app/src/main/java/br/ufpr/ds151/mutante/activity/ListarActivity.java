package br.ufpr.ds151.mutante.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.ds151.mutante.adapter.MutanteListAdapter;
import br.ufpr.ds151.mutante.R;
import br.ufpr.ds151.mutante.api.RetrofitConfig;
import br.ufpr.ds151.mutante.helper.RecyclerItemClickListener;
import br.ufpr.ds151.mutante.model.MutanteDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MutanteListAdapter mutanteListAdapter;
    private List<MutanteDTO> mutanteDTOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        recyclerView = findViewById(R.id.reciclerViewMutanteList);

        //Adicionar eventos de clique no recycler
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Long id = mutanteDTOList.get(position).getId();

                                Call<MutanteDTO> call1 = new RetrofitConfig().getMutanteService().getMutanteById(id);

                                call1.enqueue(new Callback<MutanteDTO>() {
                                    @Override
                                    public void onResponse(Call<MutanteDTO> call, Response<MutanteDTO> response) {
                                        if (response.isSuccessful()) {
                                            MutanteDTO selectedMutanteDTO = response.body();

                                            Bundle params = new Bundle();
                                            params.putString("operacao", "view");
                                            params.putSerializable("mutante", selectedMutanteDTO);

                                            Intent it = new Intent(ListarActivity.this, NovoActivity.class);
                                            it.putExtras(params);
                                            startActivity(it);
                                        } else
                                            Toast.makeText(ListarActivity.this, "Erro ao recuperar Mutante", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<MutanteDTO> call, Throwable t) {
                                        Toast.makeText(ListarActivity.this, "Erro de API:" + t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        })
        );

    }

    public void updateRecyclerMutanteList() {

        Call<List<MutanteDTO>> call1 = new RetrofitConfig().getMutanteService().getAllMutantes();

        call1.enqueue(new Callback<List<MutanteDTO>>() {
            @Override
            public void onResponse(Call<List<MutanteDTO>> call, Response<List<MutanteDTO>> response) {
                if (response.isSuccessful()) {
                    mutanteDTOList = response.body();

                    //configura adapter
                    mutanteListAdapter = new MutanteListAdapter(mutanteDTOList);

                    //configura recyclerView
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
                    recyclerView.setAdapter(mutanteListAdapter);
                } else
                    Toast.makeText(ListarActivity.this, "Erro ao carregar Mutantes", Toast.LENGTH_SHORT).show();
                    Log.i("INFO", "erro");
            }

            @Override
            public void onFailure(Call<List<MutanteDTO>> call, Throwable t) {
                Toast.makeText(ListarActivity.this, "Erro de API", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        updateRecyclerMutanteList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerMutanteList();
    }

}