package br.ce.mjosuel.servicos;

import br.ce.mjosuel.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

    public int soma(int a, int b)
    {
        return a + b;
    }

    public int subtrair(int a, int b)
    {
        return a - b;
    }

    public int dividir(int a, int b) throws NaoPodeDividirPorZeroException
    {
        if(b ==0)
        {
            throw new NaoPodeDividirPorZeroException();
        }
        return a / b;
    }
}
