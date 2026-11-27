package br.com.gof.service;

import br.com.gof.model.Client;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio do cliente.
 * Com isso, se necessário, podemos ter multiplas implementações dessa mesma
 * interface.
 *
 * @author DiegoSilvaDev
 */

public interface ClientService {
    Iterable<Client> findAll();
    Client findById(Long id);
    void save(Client client);
    void update(Long id, Client client);
    void deleteById(Long id);
}
