package br.ce.mjosuel.servicos;

import br.ce.mjosuel.exceptions.NaoPodeDividirPorZeroException;
import br.ce.mjosuel.servicos.Calculadora;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public
class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup(){
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        //cenário
        int a = 5;
        int b = 3;

        //ação
        int resultado = calc.somar(a,b);

        //verificação
        Assert.assertEquals(8,resultado);

    }

    @Test
    public void deveSubtrairDoisValores(){
        //cenário
        int a = 8;
        int b = 5;

        //ação
        int resultado = calc.subtrair(a,b);

        //verificação
        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException
    {
        //cenário
        int a = 6;
        int b = 3;

        //ação
        int resultado = calc.dividir(a,b);

        //verificação
        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException
    {
        //cenário
        int a = 10;
        int b = 0;
        Calculadora calc = new Calculadora();

        //ação
        calc.dividir(a,b);

    }

    @Test
    public void deveDividir(){
        String a = "6";
        String b = "3";

        int resultado  = calc.divide(a,b);

        Assert.assertEquals(2,resultado);
    }

}
