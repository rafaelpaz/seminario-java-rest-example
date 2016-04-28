package repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import model.Seminario;

@Named
public class SeminarioRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Seminario> consultarTodos() {
        return entityManager.createQuery("From Seminario").getResultList();
    }

    @Transactional
    public Seminario salvar(Seminario seminario) {
        if (seminario.getId() == null) {
            entityManager.persist(seminario);
        } else {
            entityManager.merge(seminario);
        }
        return seminario;
    }

    @Transactional
    public void removerSeminario(Long id) {
        Seminario seminario = entityManager.find(Seminario.class, id);
        if (seminario == null) {
            throw new IllegalArgumentException("Seminário não encontrado");
        }
        entityManager.remove(seminario);
    }

    @Transactional
    public void limpar() {
        entityManager.createQuery("DELETE FROM Seminario").executeUpdate();
    }

    public Seminario getById(Long id) {
        return entityManager.find(Seminario.class, id);
    }

    public Seminario getByNome(String nome) {
        try {
            return (Seminario) entityManager.createQuery("FROM Seminario s WHERE s.nome = :nome")
                                            .setParameter("nome", nome)
                                            .getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {}
        return null;
    }

}
