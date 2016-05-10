package model;

import control.FabricaConexoes;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

/**
 *
 * @author rosenclever
 */
public class JpaModeloDao implements Dao {

    @Override
    public void incluir(Modelo modelo) {
        EntityManager em = FabricaConexoes.getEntityManager();
        em.getTransaction().begin();
        em.persist(modelo);
        em.getTransaction().commit();
        FabricaConexoes.closeEntityManager();
    }

    @Override
    public ArrayList<Modelo> obterTodos() {
        EntityManager em = FabricaConexoes.getEntityManager();
        try{
            ArrayList<Modelo> modelos;
            em.getTransaction().begin();
            modelos = (ArrayList<Modelo>) em.createQuery("select m from Modelo m").getResultList();
            em.getTransaction().commit();
            return modelos;
        
        } catch(Exception e){
            if(em.isOpen()){
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        finally{
            if(em.isOpen())
                if (em.getTransaction().isActive())
                    em.getTransaction().commit();
                FabricaConexoes.closeEntityManager();
        
        }
    }

    @Override
    public Modelo consultarPorId(int id) {
        EntityManager em = FabricaConexoes.getEntityManager();
        em.getTransaction().begin();
        Modelo modelo = em.find(Modelo.class, id);
        em.getTransaction().commit();
        return modelo;
    }

    @Override
    public void excluir(Modelo modelo) {
        EntityManager em = FabricaConexoes.getEntityManager();
        //String lista = "";
        try{
            int confirma = JOptionPane.showConfirmDialog(null, "Confirma a exclusão do modelo\n" + modelo.getId() + " - " + modelo.getNome() + "?");
            if (confirma==0){
                em.getTransaction().begin();
                em.remove(modelo);
                em.getTransaction().commit();
                JOptionPane.showMessageDialog(null, "Exclusão concluída!");
                
            }
            else{
                JOptionPane.showMessageDialog(null, "Exclusão cancelada!");
            }

        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Modelo Inexistente. Verifique a lista de modelos disponíveis!");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Exclusão não efetuada.\nContate o administrador do sistema.");
            System.err.println("Erro desconhecido");
            e.printStackTrace();
        }
        finally{
            FabricaConexoes.closeEntityManager();
        }
    }

    @Override
    public void alterar(Modelo modelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
