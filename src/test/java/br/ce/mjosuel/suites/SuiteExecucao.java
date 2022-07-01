package br.ce.mjosuel.suites;

import br.ce.mjosuel.servicos.CalculadoraTest;
import br.ce.mjosuel.servicos.CalculoValorLocacaoTest;
import br.ce.mjosuel.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {

}
