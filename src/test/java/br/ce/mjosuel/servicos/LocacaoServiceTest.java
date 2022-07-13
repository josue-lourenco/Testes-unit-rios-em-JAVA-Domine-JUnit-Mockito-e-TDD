package br.ce.mjosuel.servicos;

import br.ce.mjosuel.builders.LocacaoBuilder;
import br.ce.mjosuel.builders.UsuarioBuilder;
import br.ce.mjosuel.dados.LocacaoDAO;
import br.ce.mjosuel.exceptions.FilmeSemEstoqueException;
import br.ce.mjosuel.exceptions.LocadoraException;
import br.ce.mjosuel.entidades.Filme;
import br.ce.mjosuel.entidades.Locacao;
import br.ce.mjosuel.entidades.Usuario;

import br.ce.mjosuel.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.MockInjection;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.mjosuel.builders.FilmeBuilder.umFilme;
import static br.ce.mjosuel.matchers.MatchersProprios.*;
import static br.ce.mjosuel.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    private LocacaoService service;

    private SPCService spc;

    private LocacaoDAO dao;

    private EmailService email;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        service = new LocacaoService();
        //LocacaoDAO dao = new LocacaoDAOFake();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
        spc = Mockito.mock(SPCService.class);
        service.setSpcService(spc);
        email = Mockito.mock(EmailService.class);
        service.setEmailService(email);
    }

    @Test
    public void deveAlugarFilme() throws Exception {

        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        //Usuario usuario = new Usuario("Usuario 1");
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        //Assert.assertEquals(locacao.getValor(),5.0,0.01) ;
        //Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        //Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        /*Uso de Assertivas - Vai checando erro a erro não tendo visibilidade de todos os erros nas verificações.
        precisando corrigir erro a erro, para descobrir o proximo erro que falhou*/

        //assertThat(locacao.getValor(), is(equalTo(5.0)));
        //assertThat(locacao.getValor(), is(not(6.0)));
        //assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        //assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

        //Uso de Rules - verifica todos os erros que falharam de uma só vez
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        error.checkThat(locacao.getDataRetorno(), ehHoje());
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenario
        //Usuario usuario = new Usuario("Usuario 1");
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().umFilmeSemEstoque().agora());

        //ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //ação
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        }catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));
        }

        System.out.println("Forma Robusta");
    }

    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        LocacaoService service = new LocacaoService();
        //Usuario usuario = new Usuario("Usuario 1");
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme Vazio");

        //ação
        service.alugarFilme(usuario, null);

        System.out.println("Forma Nova");
    }

    /*
    @Test
    public void devePagar75pctNoFilme3() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                                            new Filme("Filme 2", 2, 4.0),
                                            new Filme("Filme 3", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(11.0));
    }

    @Test
    public void devePagar50pctNoFilme4() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                                            new Filme("Filme 2", 2, 4.0),
                                            new Filme("Filme 3", 2, 4.0),
                                            new Filme("Filme 4", 2, 4.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(13.0));
    }

    @Test
    public void devePagar25pctNoFilme5() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(14.0));
    }

    @Test
    public void devePagar0pctNoFilme6() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 2, 4.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(14.0));
    }*/

    @Test
    public void deveDevolverNaSegundaAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException
    {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        //Usuario usuario = new Usuario("Usuário 1");
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList( umFilme().agora());

        //acao
        Locacao retorno = service.alugarFilme(usuario,filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);

        //assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        //assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());

    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException
    {
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        //acao
        try{
            service.alugarFilme(usuario,filmes);
            Assert.fail();
        } catch (LocadoraException e){
            Assert.assertThat(e.getMessage(),is("Usuario Negativado"));
        }

        //verificacao
        Mockito.verify(spc).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasados(){

        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();

        List<Locacao> locacoes = Arrays.asList(
            LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario).agora(),
            LocacaoBuilder.umLocacao().comUsuario(usuario2).agora(),
            LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora(),
            LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora());

        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //acao
        service.notificarAtrasos();

        //verificacao
        Mockito.verify(email, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
        Mockito.verify(email).notificarAtraso(usuario);
        Mockito.verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);
        Mockito.verify(email, never()).notificarAtraso(usuario2);

        Mockito.verifyNoMoreInteractions(email);
    }
}
