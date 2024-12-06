package br.edu.ifba.demo.backend.api.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifba.demo.backend.api.repository.UsuarioRepository;
import br.edu.ifba.demo.backend.api.dto.LoginRequestDTO;
import br.edu.ifba.demo.backend.api.dto.UsuarioDTO;
import br.edu.ifba.demo.backend.api.model.UsuarioModel;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	private UsuarioRepository usuRepository;
	
	public UsuarioController(UsuarioRepository usuRepository) {
		super();
		this.usuRepository = usuRepository;
	}

	@GetMapping
	public String teste() {
		return "Testando Rota Usuario";
	}
	
	
	@GetMapping("/listall")
	public List<UsuarioModel> listall() {
		var usuarios = usuRepository.findAll();
		return usuarios;
	}

	
	@GetMapping("id/{id}")
    public UsuarioModel findById(@PathVariable("id") Long id) {
		Optional<UsuarioModel> usuario = usuRepository.findById(id);
		if ( usuario.isPresent() )
			return usuario.get();
        return null;
    }

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@PostMapping("login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<UsuarioModel> usuario = usuRepository.findByLogin(loginRequestDTO.getLogin());
		boolean isAuthorized = usuario.isPresent() && usuario.get().getSenha().equals(loginRequestDTO.getSenha());
		if (isAuthorized) {
			UsuarioModel usuModel = usuario.get();
			UsuarioDTO user = new UsuarioDTO(
				usuModel.getId_usuario(), 
				usuModel.getLogin(),
				null,
				usuModel.getCreate_at(),
				usuModel.getLast_login());
			return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

	
	@PostMapping
	public ResponseEntity<UsuarioModel> addUsuario(@RequestBody UsuarioModel usuario) {
		UsuarioModel savedUsuario = usuRepository.save(usuario);
		return new ResponseEntity<>(savedUsuario, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id_usario}")
	public ResponseEntity<UsuarioModel> deleteUsuario(@PathVariable Long id_usario) {
		Optional<UsuarioModel> usuario = usuRepository.findById(id_usario);
		
		if (usuario.isPresent()) {
			usuRepository.delete(usuario.get());
			return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}