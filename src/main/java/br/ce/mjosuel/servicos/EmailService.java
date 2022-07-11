package br.ce.mjosuel.servicos;

import br.ce.mjosuel.entidades.Usuario;

public interface EmailService {

    public void notificarAtraso(Usuario usuario);
}
