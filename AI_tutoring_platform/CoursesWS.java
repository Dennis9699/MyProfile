package de.hnu.eae;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import de.hnu.eae.data.Conversation;
import de.hnu.eae.data.ConversationId;
import de.hnu.eae.data.Course;
import de.hnu.eae.data.Student;


@Stateless
@Path("/courses")
public class CoursesWS {

    

	@PersistenceContext(unitName = "AITutorDB")
    private EntityManager em;

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Course createCourse(Course course) {
        em.persist(course);
        return course;
    }
    
    @GET
    @Path("{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourse(@PathParam("courseId") long courseId){
        return em.find(Course.class, courseId);
    }

    @PUT
    @Path("update/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("courseId") long courseId, Course updatedCourse) {
    Course course = em.find(Course.class, courseId);
    if (course == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Course not found").build();
    }
    course.setName(updatedCourse.getName());
    course.setLecturer(updatedCourse.getLecturer());
    course.setContent(updatedCourse.getContent());
    em.merge(course);
    return Response.ok(course).build();
    }

	
	@DELETE
    @Path("delete/{courseId}")
    public void deleteCoursebyCourseId(@PathParam("courseId") long courseId) {
        em.remove(em.find(Course.class, courseId));
    }


    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getAllCourses() {
        CriteriaQuery<Course> cq = em.getCriteriaBuilder().createQuery(Course.class);
        cq.select(cq.from(Course.class));
        return em.createQuery(cq).getResultList();
    }

    // AI Functionality 

    String apiKey = ""; //removed in this public version

    // AI Chat
    @POST
    @Path("gpt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Conversation askQuestion(Conversation conversation){
        ConversationId id = new ConversationId(conversation.getMatrNr(), conversation.getCourseId());
        Conversation conv = em.find(Conversation.class, id);
        if (conv == null){
            em.persist(conversation);
        } 

        conv = conversation;
        String studentQuestion = conv.getContent();
        Course course = em.find(Course.class, conversation.getCourseId());
        String courseMaterial = course.getContent();

        String prompt = "Course material: " + courseMaterial + "\n\nStudent Question: " + studentQuestion +
        "\n\nAnswer the student's question in the context of the course material. Act as an expert on the topic explained in the course material.";
        
        // Call OpenAI API
        OpenAiService service = new OpenAiService(apiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .maxTokens(3000)
                .echo(true)
                .build();      
        String response = service.createCompletion(completionRequest).getChoices().get(0).getText();

        // Append answer to content
        conv.setContent(response);
        return conv;
    }

    //Summarise content of a course
    @POST
    @Path("summarise/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response summariseCourseContent(@PathParam("courseId") long courseId){
        // Retrieve Course Information
        Course course = em.find(Course.class, courseId);
        if (course == null) {
            //if course is not found
            return Response.status(Response.Status.NOT_FOUND).entity("Course not found").build();
        }

        // Construct the prompt for summarization
        String courseMaterial = course.getContent();
        String prompt = "Summarize the following course content:\n\n" + courseMaterial;

        // Call OpenAI API
        OpenAiService service = new OpenAiService(apiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .maxTokens(3000)
                .build();
        String summary = service.createCompletion(completionRequest).getChoices().get(0).getText();

        // Return the summary
        return Response.ok(summary).build();
    }
    
    
}

