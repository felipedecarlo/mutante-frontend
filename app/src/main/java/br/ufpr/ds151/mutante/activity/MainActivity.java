package br.ufpr.ds151.mutante.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufpr.ds151.mutante.R;
import br.ufpr.ds151.mutante.api.RetrofitConfig;
import br.ufpr.ds151.mutante.model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static int LOGIN = 1;

    UsuarioDTO usuarioDTO;
    TextView textViewMutanteCount, textViewTopHab1, textViewTopHab2, textViewTopHab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioDTO = (UsuarioDTO) getIntent().getSerializableExtra("usuario");

        textViewMutanteCount = findViewById(R.id.textViewMutanteCount);
        textViewTopHab1 = findViewById(R.id.textViewTopHab1);
        textViewTopHab2 = findViewById(R.id.textViewTopHab2);
        textViewTopHab3 = findViewById(R.id.textViewTopHab3);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getMutanteCount();
        getTopHabilities();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMutanteCount();
        getTopHabilities();
    }

    private void getTopHabilities() {

        Call<List<String>> call2 = new RetrofitConfig().getMutanteService().getTopHabilities();

        call2.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> lista = response.body();

                    if (lista.size() > 0)
                        textViewTopHab1.setText(lista.get(0));
                    else
                        textViewTopHab1.setText("");

                    if (lista.size() > 1)
                        textViewTopHab2.setText(lista.get(1));
                    else
                        textViewTopHab2.setText("");

                    if (lista.size() > 2)
                        textViewTopHab3.setText(lista.get(2));
                    else
                        textViewTopHab3.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("INFO", "falha: " + t.toString());

            }
        });

    }

    private void getMutanteCount() {

        Call<Integer> call1 = new RetrofitConfig().getMutanteService().getCount();

        call1.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.i("INFO", "sucesso");
                    Integer qtd = response.body();
                    textViewMutanteCount.setText(String.format("%d", qtd));
                } else
                    Log.i("INFO", "erro");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("INFO", "falha: " + t.toString());

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.menu_novo) {
            Bundle params = new Bundle();
            params.putString("operacao", "new");
            params.putSerializable("usuario", usuarioDTO);

            Intent it = new Intent(this, NovoActivity.class);
            it.putExtras(params);
            startActivity(it);
        } else if (menuItem.getItemId() == R.id.menu_listar) {
            Intent it = new Intent(this, ListarActivity.class);
            startActivity(it);
        } else if (menuItem.getItemId() == R.id.menu_pesquisar) {
            Intent it = new Intent(this, PesquisarActivity.class);
            startActivity(it);
        } else if (menuItem.getItemId() == R.id.menu_sair) {
            finish();
        }

        return super.onOptionsItemSelected(menuItem);
    }

}