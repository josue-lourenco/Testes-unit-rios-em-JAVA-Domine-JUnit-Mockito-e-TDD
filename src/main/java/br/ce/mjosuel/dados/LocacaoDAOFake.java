package br.ce.mjosuel.dados;

import br.ce.mjosuel.entidades.Locacao;

import java.util.List;

public class LocacaoDAOFake implements LocacaoDAO{

    public void salvar(Locacao locacao) {

    }

    @Override
    public List<Locacao> obterLocacoesPendentes()
    {
        return null;
    }
}
