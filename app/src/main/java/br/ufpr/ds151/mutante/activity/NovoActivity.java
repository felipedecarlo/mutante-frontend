package br.ufpr.ds151.mutante.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.ds151.mutante.R;
import br.ufpr.ds151.mutante.api.RetrofitConfig;
import br.ufpr.ds151.mutante.helper.ImageConverter;
import br.ufpr.ds151.mutante.model.HabilidadeDTO;
import br.ufpr.ds151.mutante.model.MutanteDTO;
import br.ufpr.ds151.mutante.model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovoActivity extends AppCompatActivity {

    private static final int CAMERA  = 100;
    private static final int GALERIA = 200;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    String base64;
    Bitmap bitmap;
    UsuarioDTO usuarioDTO, usuarioDono;
    MutanteDTO mutanteDTO;
    String operacao;

    EditText editTextNome, editTextHabilidade1, editTextHabilidade2, editTextHabilidade3;
    ImageView imageView;
    TextView textViewUsuario;
    Button buttonSalvar, buttonCamera, buttonGaleria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Desabilita title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_novo);

        Bundle params = getIntent().getExtras();

        if (params != null) {
            operacao = params.getString("operacao");
            mutanteDTO = (MutanteDTO) params.getSerializable("mutante");
            usuarioDTO = (UsuarioDTO) params.getSerializable("usuario");
        }

        editTextNome        = findViewById(R.id.editTextNome);
        editTextHabilidade1 = findViewById(R.id.editTextHabilidade1);
        editTextHabilidade2 = findViewById(R.id.editTextHabilidade2);
        editTextHabilidade3 = findViewById(R.id.editTextHabilidade3);
        imageView           = findViewById(R.id.imageViewFotoMutante);
        buttonSalvar        = findViewById(R.id.buttonSalvar);
        buttonCamera        = findViewById(R.id.buttonCamera);
        buttonGaleria       = findViewById(R.id.buttonGaleria);
        textViewUsuario     = findViewById(R.id.textViewUsuario);

        if (!operacao.equals("new")) {
            editTextNome.setText(mutanteDTO.getNome());
            if (mutanteDTO.getHabilidades() != null ) {
                if (mutanteDTO.getHabilidades().size() > 0)
                    editTextHabilidade1.setText(mutanteDTO.getHabilidades().get(0).getDescricao());
                if (mutanteDTO.getHabilidades().size() > 1)
                    editTextHabilidade2.setText(mutanteDTO.getHabilidades().get(1).getDescricao());
                if (mutanteDTO.getHabilidades().size() > 2)
                    editTextHabilidade3.setText(mutanteDTO.getHabilidades().get(2).getDescricao());
            }
            base64 = mutanteDTO.getFoto();
            bitmap = ImageConverter.base64ToBitmap(base64);
            imageView.setImageBitmap(bitmap);

            recuperaUsuarioDono(mutanteDTO.getIdUsuario());
            
            editTextNome.setEnabled(false);
            editTextHabilidade1.setEnabled(false);
            editTextHabilidade2.setEnabled(false);
            editTextHabilidade3.setEnabled(false);
            buttonSalvar.setEnabled(false);
            buttonCamera.setEnabled(false);
            buttonGaleria.setEnabled(false);

        }
    }

    private void recuperaUsuarioDono(Long idUsuario) {

        Call<UsuarioDTO> call1 = new RetrofitConfig().getMutanteService().getUsuario(idUsuario);

        call1.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful()) {
                    usuarioDono = response.body();
                    textViewUsuario.setText("Usuário:"+ usuarioDono.getNome());
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                Toast.makeText(NovoActivity.this, "Erro ao recuperar Usuário", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!operacao.equals("new")) {
            getMenuInflater().inflate(R.menu.menu_edicao, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.menu_editar) {
            operacao = "edit";
            editTextNome.setEnabled(true);
            editTextHabilidade1.setEnabled(true);
            editTextHabilidade2.setEnabled(true);
            editTextHabilidade3.setEnabled(true);
            buttonSalvar.setEnabled(true);
            buttonCamera.setEnabled(true);
            buttonGaleria.setEnabled(true);
        } else if (menuItem.getItemId() == R.id.menu_excluir) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(NovoActivity.this);
            dialog.setTitle("Confirmar Exclusão");
            dialog.setMessage("Deseja excluir o Mutante: " + mutanteDTO.getNome() + "?");
            dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Call<MutanteDTO> call1 = new RetrofitConfig().getMutanteService().deleteMutante(mutanteDTO.getId());

                    call1.enqueue(new Callback<MutanteDTO>() {
                        @Override
                        public void onResponse(Call<MutanteDTO> call, Response<MutanteDTO> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(NovoActivity.this, "Mutante deletado!!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else
                                Toast.makeText(NovoActivity.this, "Erro ao deletar Mutante", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<MutanteDTO> call, Throwable t) {
                            Toast.makeText(NovoActivity.this, "Erro de API:" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
            dialog.setNegativeButton("Não", null);

            dialog.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void salvarMutante(View view) {

        if (editTextNome.getText().toString().isEmpty())
            Toast.makeText(this, "Informe o nome", Toast.LENGTH_SHORT).show();
        else if (editTextHabilidade1.getText().toString().isEmpty() &&
                editTextHabilidade2.getText().toString().isEmpty() &&
                editTextHabilidade3.getText().toString().isEmpty())
            Toast.makeText(this, "Informe pelo menos uma habilidade", Toast.LENGTH_SHORT).show();
        else if (base64 == null || base64.isEmpty())
            Toast.makeText(this, "Foto obrigatória", Toast.LENGTH_SHORT).show();
        else if  (operacao.equals("new"))
            insert();
        else
            update();

    }

    private void insert() {

        List<HabilidadeDTO> lh = new ArrayList<>();

        if (!editTextHabilidade1.getText().toString().isEmpty()) {
            HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
            habilidadeDTO.setDescricao(editTextHabilidade1.getText().toString());
            lh.add(habilidadeDTO);
        }
        if (!editTextHabilidade2.getText().toString().isEmpty()) {
            HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
            habilidadeDTO.setDescricao(editTextHabilidade2.getText().toString());
            lh.add(habilidadeDTO);
        }
        if (!editTextHabilidade3.getText().toString().isEmpty()) {
            HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
            habilidadeDTO.setDescricao(editTextHabilidade3.getText().toString());
            lh.add(habilidadeDTO);
        }

        mutanteDTO = new MutanteDTO();
        mutanteDTO.setNome(editTextNome.getText().toString().toUpperCase());
        mutanteDTO.setIdUsuario(usuarioDTO.getId());
        mutanteDTO.setHabilidades(lh);
        mutanteDTO.setFoto(base64);

        Call<MutanteDTO> call1 = new RetrofitConfig().getMutanteService().postMutante(mutanteDTO);

        call1.enqueue(new Callback<MutanteDTO>() {
            @Override
            public void onResponse(Call<MutanteDTO> call, Response<MutanteDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(NovoActivity.this, "Mutante salvo com sucesso!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 409)
                        Toast.makeText(NovoActivity.this, "Mutante já cadastrado", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(NovoActivity.this, "Erro ao salvar Mutante", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MutanteDTO> call, Throwable t) {
                Toast.makeText(NovoActivity.this, "Erro de API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update() {

        List<HabilidadeDTO> lh = new ArrayList<>();

        if (!editTextHabilidade1.getText().toString().isEmpty()) {
            HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
            habilidadeDTO.setDescricao(editTextHabilidade1.getText().toString());
            habilidadeDTO.setIdMutante(mutanteDTO.getId());
            lh.add(habilidadeDTO);
        }
        if (!editTextHabilidade2.getText().toString().isEmpty()) {
            HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
            habilidadeDTO.setDescricao(editTextHabilidade2.getText().toString());
            habilidadeDTO.setIdMutante(mutanteDTO.getId());
            lh.add(habilidadeDTO);
        }
        if (!editTextHabilidade3.getText().toString().isEmpty()) {
            HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
            habilidadeDTO.setDescricao(editTextHabilidade3.getText().toString());
            habilidadeDTO.setIdMutante(mutanteDTO.getId());
            lh.add(habilidadeDTO);
        }

        mutanteDTO.setNome(editTextNome.getText().toString().toUpperCase());
        mutanteDTO.setHabilidades(lh);
        mutanteDTO.setFoto(base64);

        Call<MutanteDTO> call1 = new RetrofitConfig().getMutanteService().putMutante(mutanteDTO.getId(), mutanteDTO);

        call1.enqueue(new Callback<MutanteDTO>() {
            @Override
            public void onResponse(Call<MutanteDTO> call, Response<MutanteDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(NovoActivity.this, "Mutante salvo com sucesso!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 409)
                        Toast.makeText(NovoActivity.this, "Mutante já cadastrado", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(NovoActivity.this, "Erro ao salvar Mutante", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MutanteDTO> call, Throwable t) {
                Toast.makeText(NovoActivity.this, "Erro de API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void camera(View view) {

        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA);
        }
    }

    public void galeria(View view) {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(it, GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                Log.i("INFO", String.format("Camera: %dx%d\n", bitmap.getWidth(), bitmap.getHeight()));
                imageView.setImageBitmap(bitmap);
                base64 = ImageConverter.bitmapToBase64(bitmap);
            }

            if (requestCode == GALERIA) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Log.i("INFO", String.format("Galeria: %dx%d\n", bitmap.getWidth(), bitmap.getHeight()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(bitmap);
                base64 = ImageConverter.bitmapToBase64(bitmap);
            }
        }
    }
}