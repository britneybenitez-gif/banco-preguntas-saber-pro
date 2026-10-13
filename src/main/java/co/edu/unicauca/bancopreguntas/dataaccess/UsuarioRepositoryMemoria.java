package co.edu.unicauca.bancopreguntas.dataaccess;

import java.util.List;
import java.util.Optional;

import co.edu.unicauca.bancopreguntas.domain.entities.Rol;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.repositories.UsuarioRepository;

/** Usuarios de ejemplo (aún no hay módulo de autenticación en este corte). */
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final List<Usuario> usuarios = List.of(
            new Usuario(1, "Docente Autor 1", "autor1@example.com", Rol.AUTOR),
            new Usuario(2, "Administrador del Banco", "admin@example.com", Rol.ADMINISTRADOR),
            new Usuario(3, "Carlos Gómez", "carlos.gomez@example.com", Rol.REVISOR),
            new Usuario(4, "Laura Ramírez", "laura.ramirez@example.com", Rol.REVISOR),
            new Usuario(5, "Andrés Mejía", "andres.mejia@example.com", Rol.REVISOR),
            new Usuario(6, "Docente Autor 2", "autor2@example.com", Rol.AUTOR));

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return usuarios.stream().filter(u -> u.id() == id).findFirst();
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        return usuarios.stream().filter(u -> u.rol() == rol).toList();
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarios;
    }
}
