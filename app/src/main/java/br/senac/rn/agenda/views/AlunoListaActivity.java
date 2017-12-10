package br.senac.rn.agenda.views;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.senac.rn.agenda.adapters.AlunoAdapter;
import br.senac.rn.agenda.daos.AlunoDao;
import br.senac.rn.agenda.models.Aluno;
import br.senac.rn.agenda.service.AlunoService;

public class AlunoListaActivity extends AppCompatActivity {

    private ListView lvAlunos;
    private Button btCadastrar;
    private TextView teste;
    private List<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_lista);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarComponentes();
        definirEventos();
    }

    private void inicializarComponentes() {
        alunos = new ArrayList<>();
        lvAlunos = (ListView) findViewById(R.id.lista_alunos);
        btCadastrar = (Button) findViewById(R.id.cadastrar);
        carregaLista();
        registerForContextMenu(lvAlunos);
    }

    private void carregaLista() {
        alunos = new AlunoDao(this).buscarTodos();
        AlunoAdapter adapter = new AlunoAdapter(AlunoListaActivity.this, alunos);
        lvAlunos.setAdapter(adapter);
    }

    private void definirEventos() {
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChamaFormulario = new Intent(AlunoListaActivity.this, AlunoFormularioActivity.class);
                startActivity(intentChamaFormulario);
            }
        });

        lvAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int posicao, long id) {
                Aluno aluno = (Aluno) lvAlunos.getItemAtPosition(posicao);
                Intent intentChamaFormulario = new Intent(AlunoListaActivity.this, AlunoFormularioActivity.class);
                intentChamaFormulario.putExtra("aluno", aluno);
                startActivity(intentChamaFormulario);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_opcoes, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo alunoEscolhido;
        alunoEscolhido = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Aluno aluno = (Aluno) lvAlunos.getItemAtPosition(alunoEscolhido.position);
        switch (item.getItemId()) {
            case R.id.item_sms:
                Intent intentSms = new Intent(Intent.ACTION_VIEW);
                intentSms.setData(Uri.parse("sms:" + aluno.getFone()));
                item.setIntent(intentSms);
                break;
            case R.id.item_site:
                Intent intentSite = new Intent(Intent.ACTION_VIEW);
                intentSite.setData(Uri.parse(aluno.getSite()));
                item.setIntent(intentSite);
                break;
            case R.id.item_mapa:
                Intent intentMapa = new Intent(Intent.ACTION_VIEW);
                intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
                item.setIntent(intentMapa);
                break;
            case R.id.item_ligar:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 9);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getFone()));
                    item.setIntent(intentLigar);
                }
                break;
            case R.id.item_deletar:
                AlunoDao dao = new AlunoDao(this);
                dao.deletar(aluno);
                carregaLista();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class Teste extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(AlunoListaActivity.this, "Aguarde", "recebendo alunos...", true, true);
        }

        @Override
        protected Void doInBackground(Void... objects) {
            alunos = new AlunoService().buscar();
            AlunoAdapter adapter = new AlunoAdapter(AlunoListaActivity.this, alunos);
            lvAlunos.setAdapter(adapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }

    }

}