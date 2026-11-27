package br.com.gof.service.impl;

import br.com.gof.model.Client;
import br.com.gof.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    //TODO Singleton: Injetar os componentes do Spring com @AutoWired.
    //TODO Strategy: Implementar os métodos definidos na interface.
    //TODO Facade: Abstrair integrações com subsistemas, provendo uma interface simples.


    @Override
    public Iterable<Client> findAll() {
        // FIXME Buscar todos os clientes
        return null;
    }

    @Override
    public Client findById(Long id) {
        // FIXME Buscar Cliente por ID
        return null;
    }

    @Override
    public void save(Client client) {
        // FIXME Verificar se o Endereço do Cliente já existe (pelo CEP).
        // FIXME Caso não exista, integrar com o ViaCep e persistir o retorno.
        // FIXME Inserir o Cliente, vinculando o Endereço (novo ou existente).
    }

    @Override
    public void update(Long id, Client client) {
        // FIXME Buscar Cliente por ID, caso exista:
        // FIXME Verificar se o Endereço do Cliente já existe (pelo CEP).
    }

    @Override
    public void deleteById(Long id) {
        // FIXME Deletar Cliente por ID.
    }



}
