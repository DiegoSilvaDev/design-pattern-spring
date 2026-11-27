package br.com.gof.service.impl;

import br.com.gof.model.Address;
import br.com.gof.model.AddressRepository;
import br.com.gof.model.Client;
import br.com.gof.model.ClientRepository;
import br.com.gof.service.ClientService;
import br.com.gof.service.ViaCepService;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Implementação da <b>Strategy</b> {@link ClientService}, a qual pode ser
 * injetada pelo Spring (via {@link org.springframework.beans.factory.annotation.Autowired}).
 * Com isso, como essa classe é um {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author DiegoSilvaDev
 */

@Service
public class ClientServiceImpl implements ClientService {
    // Singleton: Injetar os componentes do Spring com @AutoWired
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ViaCepService viaCepService;

    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository, ViaCepService viaCepService) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.viaCepService = viaCepService;
    }
    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.


    @Override
    public Iterable<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        Optional<Client> findById = clientRepository.findById(id);
        return findById.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    @Override
    public void save(Client client) {
        saveCostumerWithCep(client);
    }

    @Override
    public void update(Long id, Client client) {
        // Buscar Cliente por ID, caso exista:
        Optional<Client> findById = clientRepository.findById(id);
        if (findById.isPresent()) {
            // Verificar se o Endereço do Cliente já existe (pelo CEP).
            // Caso não exista, integrar com o ViaCep e persistir o retorno.
            // Alterar Cliente, vinculando o endereço (novo ou existente)
            client.setId(id);
            saveCostumerWithCep(client);
        }
    }

    @Override
    public void deleteById(Long id) {
        // Deletar Cliente por ID.
        clientRepository.deleteById(id);
    }

    private void saveCostumerWithCep(Client client) {
        // Verificar se o Endereço do Cliente já existe (pelo CEP).
        String cep = client.getAddress().getCep();
        Address address = addressRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCep e persistir o retorno.
            Address newAddress = viaCepService.getCep(cep);
            addressRepository.save(newAddress);
            return newAddress;
        });
        // Inserir o Cliente, vinculando o Endereço (novo ou existente).
        client.setAddress(address);
        clientRepository.save(client);
    }

}
