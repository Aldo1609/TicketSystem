package com.aldob.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aldob.dtos.responses.CustomerTicketResponse;
import com.aldob.entities.Ticket;
import com.aldob.exceptions.customs.CustomerNotFoundException;
import com.aldob.mappers.CustomerMapper;
import com.aldob.repositories.CustomerRepository;
import com.aldob.repositories.TicketRepository;


/*
 * JUnit 5
 *
 * Esta anotación permite que JUnit utilice la extensión de Mockito.
 *
 * JUnit 5 = framework de testing.
 * Mockito  = framework para crear mocks.
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    /*
     * Mockito
     *
     * @Mock crea un objeto simulado (mock).
     *
     * No estamos creando un CustomerRepository real.
     * No se conecta a PostgreSQL.
     *
     * Cuando nuestro Service llame:
     *
     * customerRepository.existsById(...)
     *
     * nosotros podemos decirle a Mockito qué debe devolver.
     */
    @Mock
    private CustomerRepository customerRepository;

    /*
     * Mockito
     *
     * Simulamos el mapper porque NO queremos probar
     * CustomerMapper en este test.
     *
     * Este test se enfoca únicamente en CustomerServiceImpl.
     */
    @Mock
    private CustomerMapper customerMapper;

    /*
     * Mockito
     *
     * Simulamos el TicketRepository.
     *
     * Así evitamos acceder a una base de datos real.
     */
    @Mock
    private TicketRepository ticketRepository;


    /*
     * Mockito
     *
     * @InjectMocks crea una instancia REAL de
     * CustomerServiceImpl e inyecta dentro de ella
     * los mocks que declaramos arriba.
     *
     * Conceptualmente sería algo parecido a:
     *
     * new CustomerServiceImpl(
     *     customerRepository,
     *     customerMapper,
     *     ticketRepository
     * );
     */
    @InjectMocks
    private CustomerServiceImpl customerService;


    /*
     * JUnit 5
     *
     * @Test indica que este método es un caso de prueba.
     *
     * Estamos probando el escenario:
     *
     * "El customer existe y tiene tickets".
     */
    @Test
    void shouldReturnCustomerTickets() {

        // =========================================================
        // ARRANGE
        // =========================================================
        //
        // Preparamos todos los datos y comportamientos
        // necesarios para ejecutar el test.
        //

        Long customerId = 1L;

        Ticket ticket = new Ticket();

        CustomerTicketResponse response =
                new CustomerTicketResponse();


        /*
         * Mockito
         *
         * when(...).thenReturn(...)
         *
         * Le estamos diciendo al mock:
         *
         * "Cuando el Service pregunte si existe el customer
         * con ID 1, responde true".
         */
        when(customerRepository.existsById(customerId))
                .thenReturn(true);


        /*
         * Mockito
         *
         * Cuando el Service busque los tickets del customer,
         * simulamos que encuentra nuestro ticket.
         */
        when(ticketRepository.findByCustomerId(customerId))
                .thenReturn(List.of(ticket));


        /*
         * Mockito
         *
         * Cuando el Service mande el ticket al mapper,
         * simulamos que el mapper devuelve este DTO.
         */
        when(customerMapper.toCustomerTicketResponse(ticket))
                .thenReturn(response);


        // =========================================================
        // ACT
        // =========================================================
        //
        // Aquí ejecutamos REALMENTE el método que queremos probar.
        //
        // CustomerServiceImpl es real.
        // Sus dependencias son mocks.
        //

        List<CustomerTicketResponse> result =
                customerService.getCustomerTickets(customerId);


        // =========================================================
        // ASSERT
        // =========================================================
        //
        // Aquí comprobamos que el resultado sea el esperado.
        //
        // assertEquals pertenece a JUnit 5.
        //

        assertEquals(1, result.size());

        assertEquals(response, result.get(0));


        // =========================================================
        // VERIFY
        // =========================================================
        //
        // verify() pertenece a Mockito.
        //
        // Sirve para comprobar que nuestro Service
        // realmente interactuó con sus dependencias.
        //

        verify(customerRepository)
                .existsById(customerId);

        verify(ticketRepository)
                .findByCustomerId(customerId);

        verify(customerMapper)
                .toCustomerTicketResponse(ticket);
    }


    /*
     * JUnit 5
     *
     * Segundo escenario:
     *
     * "El customer NO existe".
     */
    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        // =========================================================
        // ARRANGE
        // =========================================================

        Long customerId = 1L;


        /*
         * Mockito
         *
         * Simulamos que el customer NO existe.
         */
        when(customerRepository.existsById(customerId))
                .thenReturn(false);


        // =========================================================
        // ACT + ASSERT
        // =========================================================
        //
        // En este caso el "Act" y el "Assert" están juntos
        // porque esperamos que la ejecución lance una excepción.
        //

        /*
         * JUnit 5
         *
         * assertThrows() verifica que el código ejecutado
         * lance la excepción indicada.
         */
        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomerTickets(customerId)
        );


        // =========================================================
        // VERIFY
        // =========================================================

        /*
         * Mockito
         *
         * Comprobamos que sí verificamos la existencia
         * del customer.
         */
        verify(customerRepository)
                .existsById(customerId);


        /*
         * Mockito
         *
         * never() significa:
         *
         * "Esta llamada NO debe haberse realizado".
         *
         * Tiene sentido porque si el customer no existe,
         * el Service lanza la excepción y nunca debería
         * buscar sus tickets.
         */
        verify(ticketRepository, never())
                .findByCustomerId(customerId);
    }
}
