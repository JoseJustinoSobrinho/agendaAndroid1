package br.senac.rn.agenda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.senac.rn.agenda.models.Aluno;
import br.senac.rn.agenda.views.R;

public class AlunoAdapter extends BaseAdapter {

    private List<Aluno> alunos;
    private Context context;

    public AlunoAdapter(Context context, List<Aluno> alunos) {
        this.alunos = alunos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.alunos.size();
    }

    @Override
    public Object getItem(int posicao) {
        return this.alunos.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        return this.alunos.get(posicao).getId();
    }

    @Override
    public View getView(int posicao, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View moldura = inflater.inflate(R.layout.item_lista, null);

        TextView campoNome = (TextView) moldura.findViewById(R.id.lista_nome);
        TextView campoFone = (TextView) moldura.findViewById(R.id.lista_fone);

        Aluno aluno = (Aluno) getItem(posicao);

        campoNome.setText(aluno.getNome());
        campoFone.setText(aluno.getFone());

        return moldura;
    }
}