package model;

import java.util.ArrayList;

/**
 *
 * @author rosenclever
 */
public interface Dao {
    
    public void incluir(Modelo modelo);
    public ArrayList<Modelo> obterTodos();
    public Modelo consultarPorId(int id);
    public void excluir(Modelo modelo);
    public void alterar(Modelo modelo);
    
}
