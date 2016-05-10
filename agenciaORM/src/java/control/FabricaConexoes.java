package control;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author rosenclever
 */
public class FabricaConexoes {
    
    private static EntityManagerFactory emf;
    private static ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
    
    private FabricaConexoes(){
        
    }
    
    public static EntityManager getEntityManager(){
        if (emf == null)
            emf = Persistence.createEntityManagerFactory("agenciaPU");
        EntityManager em = threadEntityManager.get();
        if(em == null || !em.isOpen()){
            em = emf.createEntityManager();
            threadEntityManager.set(em);
        }
        return em;
    }
    
    public static void closeEntityManager(){
        EntityManager em = threadEntityManager.get();
        if (em != null){
            EntityTransaction transaction = em.getTransaction();
            if(transaction.isActive())
                transaction.commit();
        
            em.close();
            threadEntityManager.set(null);
        }
    }
    public static void closeEntityManagerFactory(){
        closeEntityManager();
        if(emf!=null)
            emf.close();
    }
}
