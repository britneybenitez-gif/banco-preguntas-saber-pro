package co.edu.unicauca.bancopreguntas.domain.repositories;

import java.util.List;
import java.util.Optional;

import co.edu.unicauca.bancopreguntas.domain.entities.Rol;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;

public interface UsuarioRepository {

    Optional<Usuario> buscarPorId(int id);

    List<Usuario> listarPorRol(Rol rol);

    List<Usuario> listarTodos();
}
