package br.ufpr.ds151.mutante.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.ds151.mutante.R;
import br.ufpr.ds151.mutante.api.RetrofitConfig;
import br.ufpr.ds151.mutante.adapter.MutanteListAdapter;
import br.ufpr.ds151.mutante.model.MutanteDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesquisarActivity extends AppCompatActivity {

    EditText editTextSearch;
    private RecyclerView recyclerView;
    private MutanteListAdapter mutanteListAdapter;
    private List<MutanteDTO> mutanteDTOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);

        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerView = findViewById(R.id.recyclerViewMutanteSearchList);
    }

    public void pesquisar(View view) {

        String searchText = editTextSearch.getText().toString();

        if (searchText.isEmpty()) {
            Toast.makeText(this, "Informa a habilidade", Toast.LENGTH_SHORT).show();
        } else {

            Call<List<MutanteDTO>> call1 = new RetrofitConfig().getMutanteService().getMutantesByHabilidade(searchText);

            call1.enqueue(new Callback<List<MutanteDTO>>() {
                @Override
                public void onResponse(Call<List<MutanteDTO>> call, Response<List<MutanteDTO>> response) {
                    if (response.isSuccessful()) {
                        mutanteDTOList = response.body();
                        Log.i("INFO", "Search result size:" + mutanteDTOList.size());

                        if (mutanteDTOList == null || mutanteDTOList.size() == 0)
                            Toast.makeText(PesquisarActivity.this, "Nenhum Mutante encontrado", Toast.LENGTH_SHORT).show();

                        //configura adapter
                        mutanteListAdapter = new MutanteListAdapter(mutanteDTOList);

                        //configura recyclerView
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
                        recyclerView.setAdapter(mutanteListAdapter);
                    } else
                        Toast.makeText(PesquisarActivity.this, "Erro ao carregar Mutantes", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<MutanteDTO>> call, Throwable t) {
                    Toast.makeText(PesquisarActivity.this, "Erro de API", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}