package br.senac.rn.agenda.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.senac.rn.agenda.models.Aluno;

public class AlunoDao extends SQLiteOpenHelper {

    public AlunoDao(Context context) {
        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";
        sql += "CREATE TABLE Alunos(";
        sql += "id INTEGER PRIMARY KEY, ";
        sql += "nome TEXT, ";
        sql += "endereco TEXT, ";
        sql += "fone TEXT, ";
        sql += "site TEXT, ";
        sql += "nota REAL, ";
        sql += "foto TEXT";
        sql += ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = new String();
        switch (oldVersion) {
            case 1: //Indo pra versão 2
                sql = "ALTER TABLE Alunos ADD COLUMN foto TEXT";
                break;
        }
        db.execSQL(sql);
    }

    private ContentValues pegaDadosAluno(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("endereco", aluno.getEndereco());
        values.put("fone", aluno.getFone());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("foto", aluno.getCaminhoFoto());
        return values;
    }

    public void inserir(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert("Alunos", null, pegaDadosAluno(aluno));
    }

    public void editar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String where = "id = ?";
        String[] params = {aluno.getId().toString()};
        db.update("Alunos", pegaDadosAluno(aluno), where, params);
    }

    public void deletar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String where = "id = ?";
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", where, params);
    }

    public List<Aluno> buscarTodos() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Alunos";
        Cursor cursor = db.rawQuery(sql, null);
        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setFone(cursor.getString(cursor.getColumnIndex("fone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("foto")));
            alunos.add(aluno);
        }
        cursor.close();
        return alunos;
    }

}
