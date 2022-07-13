package br.ce.mjosuel.servicos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.mjosuel.dados.LocacaoDAO;
import br.ce.mjosuel.entidades.Locacao;
import br.ce.mjosuel.entidades.Usuario;
import br.ce.mjosuel.exceptions.FilmeSemEstoqueException;
import br.ce.mjosuel.exceptions.LocadoraException;
import br.ce.mjosuel.utils.DataUtils;
import br.ce.mjosuel.entidades.Filme;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException
	{

		if(usuario == null){
			throw new LocadoraException("Usuário vazio");
		}

		if(filmes == null || filmes.isEmpty()){
			throw new LocadoraException("Filme Vazio");
		}

		for (Filme filme: filmes){
			if(filme.getEstoque() == 0 ){
				throw new FilmeSemEstoqueException();
			}
		}

		if(spcService.possuiNegativacao(usuario)){
			throw new LocadoraException("Usuario Negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;

		for (int i = 0 ; i < filmes.size(); i++){
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();

			switch (i){
				case 2: valorFilme = valorFilme * 0.75; break;
				case 3: valorFilme = valorFilme * 0.5;  break;
				case 4: valorFilme = valorFilme * 0.25; break;
				case 5: valorFilme = 0d; break;
			}
			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);


		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);

		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
			dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	public void notificarAtrasos(){

		List<Locacao> locacoes = dao.obterLocacoesPendentes();

		for (Locacao locacao: locacoes){
			if(locacao.getDataRetorno().before(new Date())){
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}

	public void setLocacaoDAO( LocacaoDAO dao ){
		this.dao = dao;
	}

	public void setSpcService(SPCService spc){
		spcService = spc;
	}

	public void setEmailService( EmailService email ){
		emailService = email;
	}
}