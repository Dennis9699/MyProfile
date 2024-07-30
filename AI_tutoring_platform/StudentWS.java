package de.hnu.eae;


import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hnu.eae.data.Course;
import de.hnu.eae.data.Student;

@Stateless
@Path("student")
public class StudentWS {

    @PersistenceContext(unitName = "AITutorDB")
    private EntityManager em;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Student createStudent (Student student){
        em.persist(student);
        return student;
    }

    @GET
    @Path("{matrNr}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(@PathParam("matrNr") long matrNr){
        return em.find(Student.class, matrNr);
        
    }

    @DELETE
    @Path("{matrNr}")
    public void deleteStudent (@PathParam("matNr") long matrNr){
        em.remove(em.find(Student.class, matrNr));
    }

    @PUT
    @Path("{matrNr}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("matrNr") long matrNr, Student updatedStudent) {
    Student student = em.find(Student.class, matrNr);
    if (student == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
    }
    student.setFirstname(updatedStudent.getFirstname());
    student.setLastname(updatedStudent.getLastname());
    student.setDateOfBirth(updatedStudent.getDateOfBirth());
    student.setPlaceOfBirth(updatedStudent.getPlaceOfBirth());
    student.setNationality(updatedStudent.getNationality());
    student.setCountryOfResidence(updatedStudent.getCountryOfResidence());
    em.merge(student);
    return Response.ok(student).build();
    }


    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAllStudents() {
        CriteriaQuery<Student> cq = em.getCriteriaBuilder().createQuery(Student.class);
        cq.select(cq.from(Student.class));
        return em.createQuery(cq).getResultList();
    }

          
    
}
