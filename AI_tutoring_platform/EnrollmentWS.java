package de.hnu.eae;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hnu.eae.data.Course;
import de.hnu.eae.data.Enrollment;
import de.hnu.eae.data.Student;

@Stateless
@Path("/enrollments")
public class EnrollmentWS {

    @PersistenceContext(unitName = "AITutorDB")
    private EntityManager em;

    @POST
    @Path("/signup/{courseId}/{matrNr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enrollStudentInCourse(@PathParam("courseId") long courseId, @PathParam("matrNr") long matrNr) {
        Course course = em.find(Course.class, courseId);
        Student student = em.find(Student.class, matrNr);

        if (course == null || student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Course or student not found").build();
        }

        Enrollment enrollment = new Enrollment(student, course);
        em.persist(enrollment);

        return Response.ok(enrollment).build();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllEnrollments() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Enrollment> cq = cb.createQuery(Enrollment.class);
        Root<Enrollment> rootEntry = cq.from(Enrollment.class);
        CriteriaQuery<Enrollment> all = cq.select(rootEntry);
        
        List<Enrollment> enrollments = em.createQuery(all).getResultList();
        return Response.ok(enrollments).build();
    }

}
