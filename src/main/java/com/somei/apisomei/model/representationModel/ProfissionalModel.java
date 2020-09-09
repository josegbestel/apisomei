package com.somei.apisomei.model.representationModel;

import com.somei.apisomei.model.CategoriaMei;
import com.somei.apisomei.model.Localizacao;
import com.somei.apisomei.model.Profissional;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ProfissionalModel implements Serializable {

    @NotBlank
    private String nome;

    @NotBlank
    private String nomeFantasia;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @CNPJ
    private String cnpj;

    @NotBlank
    private String telefone;

    @NotBlank
    private String senha;

    private String avatar;

    @NotNull
    private int anoNascimento;

    @NotNull
    private long categoriaId;

    private Localizacao localizacao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Profissional byModel(CategoriaMei categoriaMei){
        Profissional profissional = new Profissional();
        profissional.setId(0);
        profissional.setCnpj(this.cnpj);
        profissional.setNome(this.nome);
        profissional.setNomeFantasia(this.nomeFantasia);
        profissional.setEmail(this.email);
        profissional.setSenha(this.senha);
        profissional.setAnoNascimento(this.anoNascimento);
        profissional.setAvatar(this.avatar);
        profissional.setTelefone(this.telefone);
        profissional.setCategoria(categoriaMei);
        profissional.setLocalizacao(this.localizacao);

        return profissional;
    }
}