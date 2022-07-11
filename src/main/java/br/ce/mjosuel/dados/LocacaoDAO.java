package br.ce.mjosuel.dados;

import br.ce.mjosuel.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    public void salvar(Locacao locacao);

    public List<Locacao> obterLocacoesPendentes();
}
