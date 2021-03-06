package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Conexao.ConectaBanco;
import Modelo.Usuario;
import Modelo.EnumTipoAcesso;
import Modelo.EnumAtivo;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String CONSULTA_USUARIO = "select usuarioid, nome, rg, telefone, rua, numero, bairro, cidade, estado, cep, login, tipo, ativo from usuario, login where usuarioid = fk_usuario order by usuarioid;";
    private static final String CADASTRA_NOVO_USUARIO = "INSERT INTO public.usuario(usuarioid, nome, rg, telefone, rua, numero, bairro, cidade, estado, cep) VALUES (?,?,?,?,?,?,?,?,?,?);";
    private static final String CADASTRA_NOVO_LOGIN = "INSERT INTO public.login(loginid, login, senha, ativo, tipo, fk_usuario) VALUES (?,?,?,'SIM',?,?);";
    
    public List<Usuario> consultarTodos() throws ClassNotFoundException, SQLException {

        Connection con = ConectaBanco.getConexao();
        PreparedStatement comando;

        comando = con.prepareStatement(CONSULTA_USUARIO);

        ResultSet resultado = comando.executeQuery();

        List<Usuario> todosUsuarios = new ArrayList<>();
        while (resultado.next()) {
            Usuario user = new Usuario();

            user.setId(resultado.getInt("usuarioid"));
            user.setNome(resultado.getString("nome"));
            user.setRg(resultado.getString("rg"));
            user.setTelefone(resultado.getString("telefone"));
            user.setRua(resultado.getString("rua"));
            user.setNumero(resultado.getString("numero"));
            user.setBairro(resultado.getString("bairro"));
            user.setCidade(resultado.getString("cidade"));
            user.setEstado(resultado.getString("estado"));
            user.setCep(resultado.getString("cep"));
            user.setLogin(resultado.getString("login"));            
            user.setTipo((EnumTipoAcesso.valueOf(resultado.getString("tipo"))));
            user.setAtivo((EnumAtivo.valueOf(resultado.getString("ativo"))));

            todosUsuarios.add(user);
        }
        con.close();
        return todosUsuarios;
    }
    
    public void Editar(Usuario usuario) throws ClassNotFoundException, SQLException {
        PreparedStatement pstmt = null;

        Connection con = ConectaBanco.getConexao();

        //Se n??o houver login igual ir?? cadastrar na tabela usu??rio e login conforme parametros
        pstmt = con.prepareStatement("UPDATE public.login SET login=?, ativo=?, tipo=?, fk_usuario=? WHERE loginid=?;");
        pstmt.setString(1, usuario.getLogin());
        pstmt.setString(2, usuario.getAtivo().toString());
        pstmt.setString(3, usuario.getTipo().toString());
        pstmt.setInt(4, usuario.getId());
        pstmt.setInt(5, usuario.getId());
        pstmt.execute();
        pstmt.close();

        pstmt = con.prepareStatement("UPDATE public.usuario SET nome=?, rg=?, telefone=?, rua=?, numero=?, bairro=?, cidade=?, estado=?, cep=? WHERE usuarioid=?;");
        pstmt.setString(1, usuario.getNome());
        pstmt.setString(2, usuario.getRg());
        pstmt.setString(3, usuario.getTelefone());
        pstmt.setString(4, usuario.getRua());
        pstmt.setString(5, usuario.getNumero());
        pstmt.setString(6, usuario.getBairro());
        pstmt.setString(7, usuario.getCidade());
        pstmt.setString(8, usuario.getEstado());
        pstmt.setString(9, usuario.getCep());
        pstmt.setInt(10, usuario.getId());
        pstmt.execute();
    }
    
    public void consultarporId(Usuario user) throws ClassNotFoundException, SQLException {
        Connection con = ConectaBanco.getConexao();
        PreparedStatement com = con.prepareStatement("select usuarioid, nome, rg, telefone, rua, numero, bairro, cidade, estado, cep, login, tipo, ativo from usuario, login where usuarioid = fk_usuario and usuarioid= ?");
        com.setInt(1, user.getId());
        ResultSet resultado = com.executeQuery();

        if (resultado.next()) {
            user.setId(resultado.getInt("usuarioid"));
            user.setNome(resultado.getString("nome"));
            user.setRg(resultado.getString("rg"));
            user.setTelefone(resultado.getString("telefone"));
            user.setRua(resultado.getString("rua"));
            user.setNumero(resultado.getString("numero"));
            user.setBairro(resultado.getString("bairro"));
            user.setCidade(resultado.getString("cidade"));
            user.setEstado(resultado.getString("estado"));
            user.setCep(resultado.getString("cep"));
            user.setLogin(resultado.getString("login"));
            user.setTipo((EnumTipoAcesso.valueOf(resultado.getString("tipo"))));
            user.setAtivo((EnumAtivo.valueOf(resultado.getString("ativo"))));
        }
    }
    
    public void excluir(int user) throws ClassNotFoundException, SQLException {
        Connection con = ConectaBanco.getConexao();
        PreparedStatement com = con.prepareStatement("delete from login where loginid=?;");
        com.setInt(1, user);
        com.execute();
        com.close();
        com = con.prepareStatement("delete from usuario where usuarioid=?;");
        com.setInt(1, user);
        com.execute();
    }
    
    public void cadastraNovoUsuario(Usuario usuario) throws ClassNotFoundException {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rsUsuario = null;

        try {
            //Conecta com o banco
            con = ConectaBanco.getConexao();
            pstmt = con.prepareStatement("select count(usuarioid) from usuario;");
            rsUsuario = pstmt.executeQuery();
            rsUsuario.next();
            //A querry acima conta quantos usu??rios tem criados para ter o pr??ximo n??mero de id do usu??rio que ser?? criado
            int id_num = Integer.parseInt(rsUsuario.getString("count"));

            id_num += 1;

            rsUsuario.close();

            //Essa querry ir?? validar se h?? um login com o mesmo nome do inserido
            pstmt = con.prepareStatement("select count(login) from login where login=?;");
            pstmt.setString(1, usuario.getLogin());
            rsUsuario = pstmt.executeQuery();
            rsUsuario.next();

            int qntLogin = Integer.parseInt(rsUsuario.getString("count"));

            rsUsuario.close();

            if (qntLogin == 0) {
                //Se n??o houver login igual ir?? cadastrar na tabela usu??rio e login conforme parametros
                pstmt = con.prepareStatement(CADASTRA_NOVO_USUARIO);
                pstmt.setInt(1, id_num);
                pstmt.setString(2, usuario.getNome());
                pstmt.setString(3, usuario.getRg());
                pstmt.setString(4, usuario.getTelefone());
                pstmt.setString(5, usuario.getRua());
                pstmt.setString(6, usuario.getNumero());
                pstmt.setString(7, usuario.getBairro());
                pstmt.setString(8, usuario.getCidade());
                pstmt.setString(9, usuario.getEstado());
                pstmt.setString(10, usuario.getCep());
                pstmt.execute();
                pstmt.close();

                pstmt = con.prepareStatement(CADASTRA_NOVO_LOGIN);
                pstmt.setInt(1, id_num);
                pstmt.setString(2, usuario.getLogin());
                pstmt.setString(3, usuario.getSenha());
                pstmt.setString(4, usuario.getTipo().toString());
                pstmt.setInt(5, id_num);
                pstmt.execute();
            } else {
                usuario.setLogin_duplicado(true);
            }

        } catch (SQLException sqlErro) {
            throw new RuntimeException(sqlErro);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }
}
