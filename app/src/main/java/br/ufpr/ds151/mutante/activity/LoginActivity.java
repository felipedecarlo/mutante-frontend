package br.ufpr.ds151.mutante.activity;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.ufpr.ds151.mutante.R;
import br.ufpr.ds151.mutante.api.RetrofitConfig;
import br.ufpr.ds151.mutante.model.LoginDTO;
import br.ufpr.ds151.mutante.model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextLogin, editTextPassword;
    UsuarioDTO usuarioDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
    }
    
    public void login(View view) {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin(editTextLogin.getText().toString());
        loginDTO.setSenha(editTextPassword.getText().toString());

        Call<UsuarioDTO> call1 = new RetrofitConfig().getMutanteService().login(loginDTO);

        call1.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful()) {
                    usuarioDTO = response.body();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("usuario", usuarioDTO);
                    startActivity(intent);

                    finish();
                } else
                    if (response.code() == 401)
                        Toast.makeText(LoginActivity.this, "Usuário/senha inválidos", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "Erro de login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro de API", Toast.LENGTH_SHORT).show();
            }
        });

    }

}