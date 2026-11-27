package br.com.gof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Projeto Spring boot.
 * Os seguintes módulos/dependecias foram selecionados:
 * - Spring Data JPA
 * - Spring WEB
 * - H2 Database
 * - OpenFeign
 *
 * @author DiegoSilvaDev
 */

@EnableFeignClients
@SpringBootApplication
public class PadroesProjetoSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PadroesProjetoSpringApplication.class, args);
    }

}
