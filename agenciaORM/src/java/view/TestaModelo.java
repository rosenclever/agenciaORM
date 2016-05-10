package view;

import control.FabricaConexoes;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import model.Modelo;

/**
 *
 * @author rosenclever
 */
public class TestaModelo {
    
    //private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("agenciaPU");
    
    
    public static void main(String[] args) {
        String opcao="";
        while (!opcao.equals("5")){
            opcao = JOptionPane.showInputDialog("Informe a opção desejada:\n"
                    + "1) Incluir\n"
                    + "2) Listar\n"
                    + "3) Excluir\n"
                    + "4) Alterar\n"
                    + "5) Sair\n");

            switch(opcao){
                case "1":
                    incluir();
                    break;

                case "2":
                    listar();
                    break;
                
                case "3":
                    excluir();
                    break;
                    
                case "4":
                    alterar();
                    break;
                
                case "5":
                    sair();
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção " + opcao + "Inválida");
                    
            }
        }
    }
    
    public static void sair(){
        FabricaConexoes.closeEntityManagerFactory();
        JOptionPane.showMessageDialog(null, "Programa encerrado!");
        System.exit(0);
    }
    
    public static Modelo consultaPorId(int id){
        EntityManager em = FabricaConexoes.getEntityManager();
        em.getTransaction().begin();
        Modelo modelo = em.find(Modelo.class, id);
        em.getTransaction().commit();
        return modelo;
    }
    
    public static ArrayList<Modelo> obterTodos(){
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
    public static void listar(){
        String lista = "";
        ArrayList<Modelo> modelos = obterTodos();
        if (modelos.isEmpty())
            JOptionPane.showMessageDialog(null, "Nenhum modelo cadastrado!");
        else{
            for(Modelo m: modelos){
                lista += m.getId() + " - " + m.getNome() + "\n";
            }
            JOptionPane.showMessageDialog(null, lista);
        }
        
    }
    
    public static void incluir(){
        EntityManager em = FabricaConexoes.getEntityManager();
        Modelo modelo = new Modelo();
        modelo.setNome(JOptionPane.showInputDialog("Informe o nome do modelo:"));
        em.getTransaction().begin();
        em.persist(modelo);
        em.getTransaction().commit();
        FabricaConexoes.closeEntityManager();
    }
    
    public static void excluir(){
        
        String lista = "";
        try{
            ArrayList<Modelo> modelos = obterTodos();
            for(Modelo m: modelos){
                lista += m.getId() + " - " + m.getNome() + "\n";
            }
            String opcao = JOptionPane.showInputDialog("Informe o código do modelo que deseja excluir:\n" + lista);
            int id = Integer.parseInt(opcao);
            EntityManager em = FabricaConexoes.getEntityManager();
           
            Modelo modelo = consultaPorId(id);
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
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Código de modelo inválido. Verifique a lista de modelos disponíveis!");
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Código Inexistente. Verifique a lista de modelos disponíveis!");
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
    
    public static void alterar(){
        
    }
    
}
