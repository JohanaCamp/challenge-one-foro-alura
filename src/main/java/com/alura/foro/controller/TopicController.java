package com.alura.foro.controller;

import com.alura.foro.modelo.Topico;
import com.alura.foro.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;

    @GetMapping()
    public ResponseEntity<List<Topico>> listarTopicos() {
        List<Topico> topicos = topicRepository.findAll();
        return ResponseEntity.ok(topicos);
    }

    @PostMapping()
    public ResponseEntity<?> crearTopico(@RequestBody Topico topico) {
        if (topicRepository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un tópico con este título y mensaje.");
        } else {
            Topico nuevoTopico = topicRepository.save(topico);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tópico creado con éxito. ID: " + nuevoTopico.getId());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTopico(@PathVariable Long id) {
        Topico topico = topicRepository.findById(id).orElse(null);

        if (topico != null) {
            return ResponseEntity.ok(topico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico no encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTopico(@PathVariable Long id, @RequestBody Topico topico) {
        Topico existingTopico = topicRepository.findById(id).orElse(null);

        if (existingTopico != null) {
            existingTopico.setTitulo(topico.getTitulo());
            existingTopico.setMensaje(topico.getMensaje());
            topicRepository.save(existingTopico);
            return ResponseEntity.status(HttpStatus.OK).body("Tópico actualizado con éxito.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico no encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        if (topicRepository.existsById(id)) {
            topicRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Tópico eliminado con éxito.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico no encontrado.");
        }
    }
}
