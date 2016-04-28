package restService;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Seminario;
import repository.SeminarioRepository;

@Path("/seminarios")
public class SeminarioRestService {

    @Inject
    private SeminarioRepository seminarioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Seminario> getSeminarios() {
        return seminarioService.consultarTodos();
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Seminario getById(@NotNull @PathParam("id") Long id) {
        return seminarioService.getById(id);
    }

    @GET
    @Path("nome/{nome}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Seminario getByNome(@NotNull @PathParam("nome") String nome) {
        return seminarioService.getByNome(nome);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Seminario salvarSeminario(Seminario seminario) {
        return seminarioService.salvar(seminario);
    }

    @DELETE
    @Path("{id}")
    public Response removerPorId(@NotNull @PathParam("id") Long id) {
        try {
            seminarioService.removerSeminario(id);
        } catch (IllegalArgumentException e) {
            return Response.noContent().build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/todos")
    public void limparDados() {
        seminarioService.limpar();
    }

}
