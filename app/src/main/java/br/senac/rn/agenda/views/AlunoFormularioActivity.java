package br.senac.rn.agenda.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import br.senac.rn.agenda.daos.AlunoDao;
import br.senac.rn.agenda.models.Aluno;

public class AlunoFormularioActivity extends AppCompatActivity {

    private String caminhoImagem;
    private static final int CODIGO_CAMERA = 123;

    private ImageView ivFoto;
    private EditText etNome, etEndereco, etFone, etSite;
    private RatingBar rbNota;
    private Button btCadastrar, btFoto;
    private Aluno aluno;
    private AlunoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_formulario);
        inicializarComponentes();
        definirEventos();
    }

    private void inicializarComponentes() {
        ivFoto = (ImageView) findViewById(R.id.formulario_foto);
        etNome = (EditText) findViewById(R.id.formulario_nome);
        etEndereco = (EditText) findViewById(R.id.formulario_endereco);
        etFone = (EditText) findViewById(R.id.formulario_fone);
        etSite = (EditText) findViewById(R.id.formulario_site);
        rbNota = (RatingBar) findViewById(R.id.formulario_nota);
        btCadastrar = (Button) findViewById(R.id.formulario_cadastrar);
        btFoto = (Button) findViewById(R.id.formulario_camera);
        Intent intent = getIntent();
        aluno = (Aluno) intent.getSerializableExtra("aluno");
        if (aluno != null) {
            preencherCampos(aluno);
        } else {
            aluno = new Aluno();
        }

    }

    private void definirEventos() {
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_CAMERA && resultCode == RESULT_OK) {
            carregaImagem(caminhoImagem);
        }

    }

    private void carregaImagem(String caminho) {
        Bitmap bitmap = BitmapFactory.decodeFile(caminho);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        ivFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        ivFoto.setImageBitmap(bitmap);
    }

    private void cadastrar() {
        dao = new AlunoDao(this);

        aluno.setNome(etNome.getText().toString());
        aluno.setEndereco(etEndereco.getText().toString());
        aluno.setFone(etFone.getText().toString());
        aluno.setSite(etSite.getText().toString());
        aluno.setNota(Double.valueOf(rbNota.getProgress()));
        aluno.setCaminhoFoto(caminhoImagem);

        if (aluno.getId() == null) {
            dao.inserir(aluno);
        } else {
            dao.editar(aluno);
        }
        finish();
    }

    private void preencherCampos(Aluno aluno) {
        etNome.setText(aluno.getNome());
        etEndereco.setText(aluno.getEndereco());
        etFone.setText(aluno.getFone());
        etSite.setText(aluno.getSite());
        rbNota.setProgress(aluno.getNota().intValue());
        carregaImagem(aluno.getCaminhoFoto());
    }

    private File criarArquivoImagem() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName /* prefix */, ".jpg" /* suffix */, storageDir /* directory */);
        caminhoImagem = image.getAbsolutePath();
        return image;
    }

    private void tirarFoto() {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentCamera.resolveActivity(getPackageManager()) != null) {
            File arquivoImagem = null;
            try {
                arquivoImagem = criarArquivoImagem();
            } catch (IOException ex) {
                Log.d("Exceção:", ex.toString());
            }
            if (arquivoImagem != null) {
                String autoridade = "br.senac.rn.agenda.views";
                Uri photoURI = FileProvider.getUriForFile(this,autoridade,arquivoImagem);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        }
    }

}