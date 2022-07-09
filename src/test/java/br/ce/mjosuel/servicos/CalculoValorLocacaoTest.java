package br.ce.mjosuel.servicos;

import br.ce.mjosuel.buliders.UsuarioBuilder;
import br.ce.mjosuel.dados.LocacaoDAO;
import br.ce.mjosuel.dados.LocacaoDAOFake;
import br.ce.mjosuel.exceptions.FilmeSemEstoqueException;
import br.ce.mjosuel.exceptions.LocadoraException;
import br.ce.mjosuel.entidades.Filme;
import br.ce.mjosuel.entidades.Locacao;
import br.ce.mjosuel.entidades.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.ce.mjosuel.buliders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value=1)
    public Double valorLocacao;

    @Parameterized.Parameter(value=2)
    public String cenario;

    @Before
    public void setup(){
        service = new LocacaoService();
        //LocacaoDAO dao = new LocacaoDAOFake();
        LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
        SPCService spc = Mockito.mock(SPCService.class);
        service.setSpcService(spc);
    }

    private static Filme filme1 = umFilme().agora();
    private static Filme filme2 = umFilme().agora();
    private static Filme filme3 = umFilme().agora();
    private static Filme filme4 = umFilme().agora();
    private static Filme filme5 = umFilme().agora();
    private static Filme filme6 = umFilme().agora();
    private static Filme filme7 = umFilme().agora();

    @Parameterized.Parameters(name="Teste {index} = {2}")
    public static Collection<Object[]> getParametros(){

        return Arrays.asList
                (new Object[][]{
                    {Arrays.asList( filme1,filme2), 8.0, "2 Filmes: Sem Desconto"},
                    {Arrays.asList( filme1,filme2,filme3), 11.0, "3 Filmes: 25%"},
                    {Arrays.asList( filme1,filme2,filme3,filme4), 13.0, "4 Filmes 50%"},
                    {Arrays.asList( filme1,filme2,filme3,filme4,filme5), 14.0, "5 Filmes 75%"},
                    {Arrays.asList( filme1,filme2,filme3,filme4,filme5,filme6), 14.0, "6 Filmes 100%"},
                    {Arrays.asList( filme1,filme2,filme3,filme4,filme5,filme6,filme7), 18.0, "7 Filmes: Sem Desconto"}
                });
    }
    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        //Usuario usuario = new Usuario("Usuario 1");
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(valorLocacao));
    }

    @Test
    public void print(){
        System.out.println(valorLocacao);
    }

}
