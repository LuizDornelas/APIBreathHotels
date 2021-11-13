/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author luiz.dornelas
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;

    public GenericResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("usuario/consultartodos")
    public String consultarTodos() throws ClassNotFoundException, SQLException {
        List<Usuario> todosUsuarios = new ArrayList<Usuario>();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            todosUsuarios = dao.consultarTodos();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro CNF: " + ex.getMessage());
        }
        Gson g = new Gson();
        return g.toJson(todosUsuarios);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("usuario/consultarporid/{x}")
    public String consultarId(@PathParam("x")int x) throws ClassNotFoundException, SQLException {
        Usuario user = new Usuario();
        user.setId(x);
        UsuarioDAO userC = new UsuarioDAO();
        try {
            userC.consultarporId(user);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro CNF: " + ex.getMessage());
        }
        Gson g = new Gson();
        return g.toJson(user);
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("usuario/delete/{x}")
    public String excluir(@PathParam("x")int x) throws ClassNotFoundException, SQLException {        
        UsuarioDAO userC = new UsuarioDAO();
        String user = null;
        try {
            userC.excluir(x);    
            user = "Usuário excluído com sucesso!";
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro CNF: " + ex.getMessage());
        }
        Gson g = new Gson();
        return g.toJson(user);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("usuario/cadastrar")
    public void cadastrar(String usuario) throws ClassNotFoundException {
        Gson g = new Gson();
        Usuario user = (Usuario) g.fromJson(usuario, Usuario.class);
        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.cadastraNovoUsuario(user);
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro CNF: " + ex.getMessage());
        }
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("usuario/atualizar")
    public void atualizar(String usuario) throws ClassNotFoundException, SQLException {
        Gson g = new Gson();
        Usuario user = (Usuario) g.fromJson(usuario, Usuario.class);
        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.Editar(user);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro CNF: " + ex.getMessage());
        }
    }
}
