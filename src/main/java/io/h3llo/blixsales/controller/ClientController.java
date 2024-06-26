package io.h3llo.blixsales.controller;

import io.h3llo.blixsales.dto.GenericResponse;
import io.h3llo.blixsales.dto.GenericResponseRecord;
import io.h3llo.blixsales.dto.ClientDTO;
import io.h3llo.blixsales.model.Client;
import io.h3llo.blixsales.service.IClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/clients")
//@AllArgsConstructor
@RequiredArgsConstructor
public class ClientController {

    private final IClientService service;
    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<GenericResponseRecord<ClientDTO>> readAll() throws Exception{
        List<ClientDTO> list = service.readAll().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(new GenericResponseRecord<>(200, "success", new ArrayList<>(list)));

    }



    @PostMapping
    public ResponseEntity<ClientDTO> save(@Valid @RequestBody ClientDTO dto) throws Exception {
        Client obj = service.save(convertToEntity(dto));

        return new ResponseEntity<>(convertToDto(obj), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ClientDTO>> readById(@PathVariable("id") Integer id) throws Exception{

        ClientDTO dto = convertToDto(service.readById(id));

        return ResponseEntity.ok(new GenericResponse<>(200, "success", Arrays.asList(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody ClientDTO dto) throws Exception {
        //category.setIdClient(id);
        Client obj = service.update(convertToEntity(dto), id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws Exception{
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    /////////////////////////////////////////////////////////////////////////
    private ClientDTO convertToDto (Client obj) {
        return modelMapper.map(obj, ClientDTO.class);
    }

    private Client convertToEntity(ClientDTO dto) {
        return modelMapper.map(dto, Client.class);
    }

}
