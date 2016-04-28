package restService;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

import model.Seminario;

@SuppressWarnings("unchecked")
public class SeminarioRestTest {

    private static final String URL = "http://localhost:8080/seminario-java-rest-example/resources/seminarios";

    private Seminario seminario;

    @Before
    public void setUp() {
        seminario = new Seminario();
        seminario.setNome("Seminario SENAI");
        seminario.setQuantidadeParticipantes(20);

        limparSeminarios();
    }

    @Test
    public void deveLimparSeminarios() {
        Response response = limparSeminarios();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void deveRetornarSeminariosSalvos() {
        salvarSeminario();
        Seminario seminario2 = new Seminario();
        seminario2.setNome("Novo Seminário SENAI");
        seminario2.setQuantidadeParticipantes(10);
        salvarSeminario(seminario2);

        seminario2 = buscarPorNome(seminario2.getNome());

        List<Object> seminarios = ClientBuilder.newClient()
                                               .target(URL)
                                               .request()
                                               .get()
                                               .readEntity(List.class);

        assertEquals(2, seminarios.size());
    }

    @Test
    public void deveRetornarSemSeminarios() {
        List<Object> seminarios = ClientBuilder.newClient()
                                               .target(URL)
                                               .request()
                                               .get()
                                               .readEntity(List.class);

        assertEquals(0, seminarios.size());
    }

    @Test
    public void deveSalvarNovoSeminario() {
        Seminario seminario = salvarSeminario();
        assertNotNull(seminario);
    }

    @Test
    public void deveObterPorId() {
        Seminario seminarioSalvo = salvarSeminario();
        assertNotNull(seminarioSalvo);
        Long id = seminarioSalvo.getId();

        Seminario seminarioPorId = buscarPorId(id.toString());

        assertNotNull(seminarioPorId);
        assertEquals(20, seminarioPorId.getQuantidadeParticipantes().intValue());
    }

    @Test
    public void deveAtualizarSeminario() {
        Seminario seminarioSalvo = salvarSeminario();
        assertEquals(20, seminarioSalvo.getQuantidadeParticipantes().intValue());

        this.seminario.setId(seminarioSalvo.getId());
        this.seminario.setNome("SENAI");
        this.seminario.setQuantidadeParticipantes(14);

        Seminario seminarioAtualizado = salvarSeminario();
        assertEquals("SENAI", seminarioAtualizado.getNome());
        assertEquals(14, seminarioAtualizado.getQuantidadeParticipantes().intValue());
    }

    @Test(expected = NullPointerException.class)
    public void deveLancarExcecaoAoNaoEncontrarSeminarioPorNome() {
        buscarPorNome("nao existe");
    }

    @Test(expected = NullPointerException.class)
    public void deveLancarExcecaoAoEnviarComTipoErrado() {
        buscarPorId("teste");
    }

    @Test(expected = NullPointerException.class)
    public void deveLancarExcecaoAoNaoEncontrarId() {
        buscarPorId("2000");
    }

    @Test
    public void deveRemoverSeminarioPorId() {
        Seminario seminario = salvarSeminario();

        Response response = limparSeminario(seminario.getId());
        assertEquals(response.getStatus(), Status.OK.getStatusCode());
    }

    @Test
    public void naoDeveRemoverSeminarioQueNaoExiste() {
        Response response = limparSeminario(30L);
        assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
    }

    /**
     * Metodos auxiliares
     * 
     */
    private Seminario buscarPorNome(String nome) {
        Response response = ClientBuilder.newClient()
                                         .target(URL)
                                         .path("nome")
                                         .path(nome)
                                         .request()
                                         .buildGet()
                                         .invoke();

        if (Status.NO_CONTENT.getStatusCode() == response.getStatus()) {
            throw new NullPointerException();
        }

        return response.readEntity(Seminario.class);
    }

    private Seminario buscarPorId(String id) {
        Response response = ClientBuilder.newClient()
                                         .target(URL)
                                         .path(id)
                                         .request()
                                         .buildGet()
                                         .invoke();

        if (Status.NO_CONTENT.getStatusCode() == response.getStatus() || Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
            throw new NullPointerException();
        }

        return response.readEntity(Seminario.class);
    }

    private Seminario salvarSeminario() {
        return ClientBuilder.newClient()
                            .target(URL)
                            .request()
                            .post(Entity.json(seminario))
                            .readEntity(Seminario.class);
    }

    private Seminario salvarSeminario(Seminario seminario) {
        return ClientBuilder.newClient()
                            .target(URL)
                            .request()
                            .post(Entity.json(seminario))
                            .readEntity(Seminario.class);
    }

    private Response limparSeminario(Long id) {
        return ClientBuilder.newClient()
                            .target(URL)
                            .path(id.toString())
                            .request()
                            .delete();
    }

    private Response limparSeminarios() {
        return ClientBuilder.newClient()
                            .target(URL)
                            .path("todos")
                            .request()
                            .delete();
    }

}
