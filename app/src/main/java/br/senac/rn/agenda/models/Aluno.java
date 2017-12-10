package br.senac.rn.agenda.models;

import java.io.Serializable;

public class Aluno implements Serializable {

    private Integer id;
    private String nome;
    private String endereco;
    private String fone;
    private String site;
    private Double nota;
    private String caminhoFoto;

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public Aluno() {}

    public Aluno(String nome, String endereco, String fone, String site, Double nota) {
        this.nome = nome;
        this.endereco = endereco;
        this.fone = fone;
        this.site = site;
        this.nota = nota;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getFone() {
        return fone;
    }

    public String getSite() {
        return site;
    }

    public Double getNota() {
        return nota;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aluno aluno = (Aluno) o;

        return id != null ? id.equals(aluno.id) : aluno.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return nome;
    }
}