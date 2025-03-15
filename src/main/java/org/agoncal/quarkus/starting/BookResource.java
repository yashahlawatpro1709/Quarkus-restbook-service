package org.agoncal.quarkus.starting;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.Null;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    @Inject
    BookRepository repository;
    @Inject
    Logger logger;

    @GET
    public List<Book> getAllBooks() {
        logger.info("return all books");
        return repository.getAllBooks();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public int countAllBooks() {
        logger.info("return the number of books");
        return repository.getAllBooks().size();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int id) {
        logger.info("Enter the id to get dataset");
        Optional<Book> bookOptional = repository.getBook(id);

        if (bookOptional.isPresent()) {
            return Response.ok(bookOptional.get()).build();
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        logger.info("add a new book");
        repository.addBook(book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @POST
    @Path("/dummy")
    public Response dummy(Object body) {
        return Response.ok(body).build();
    }

    @GET
    @Path("/overdue")
    public List<Book> getOverdueBooks() {
        return repository.getAllBooks().stream().filter(Book::isOverdue).toList();
    }

    @POST
    @Path("/{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReview(@PathParam("id") int id, Review review) {
        boolean success = repository.addReview(id, review);
        if (success) {
            return Response.status(Response.Status.CREATED).entity("Review added successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
        }
    }

    @GET
    @Path("/{id}/reviews")
    public List<Review> getReviews(@PathParam("id") int id) {
        return repository.getReviews(id);
    }

    @Inject
    AnalyticsService analyticsService;

    @POST
    @Path("/{id}/borrow/{username}")
    public Response borrowBook(@PathParam("id") int id, @PathParam("username") String username) {
        logger.info(username + " is borrowing book ID: " + id);
        boolean success = repository.borrowBook(id, username);
        if (success) {
            analyticsService.recordBorrowing(id, username);
            return Response.ok("Book borrowed successfully by " + username).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Book is already borrowed").build();
        }
    }

    @POST
    @Path("/{id}/return")
    public Response returnBook(@PathParam("id") int id) {
        logger.info("Returning book ID: " + id);
        boolean success = repository.returnBook(id);
        if (success) {
            analyticsService.recordReturning(id, "SomeUsername");
            return Response.ok("Book returned successfully").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Book was not borrowed").build();
        }
    }

    @GET
    @Path("/most-borrowed")
    public Response getMostBorrowedBooks(@QueryParam("topN") @DefaultValue("5") int topN) {
        List<Integer> mostBorrowedBooks = analyticsService.getMostBorrowedBooks(topN);
        return Response.ok(mostBorrowedBooks).build();
    }

    @GET
    @Path("/most-active-users")
    public Response getMostActiveUsers(@QueryParam("topN") @DefaultValue("5") int topN) {
        List<String> mostActiveUsers = analyticsService.getMostActiveUsers(topN);
        return Response.ok(mostActiveUsers).build();
    }

    @GET
    @Path("/return-count")
    public Response getBookReturnCount() {
        Map<Integer, Integer> returnCounts = analyticsService.getBookReturnCount();
        return Response.ok(returnCounts).build();
    }

    @GET
    @Path("/user-returns")
    public Response getUserReturns() {
        Map<String, Integer> userReturns = analyticsService.getUserReturns();
        return Response.ok(userReturns).build();
    }

    @GET
    @Path("/last-return-time")
    public Response getLastReturnTime() {
        Map<String, Long> lastReturnTime = analyticsService.getLastReturnTime();
        return Response.ok(lastReturnTime).build();
    }

    @Inject
    DeliveryService deliveryService;

    @POST
    @Path("/{id}/deliver")
    public Response requestHomeDelivery(@PathParam("id") int id,
                                        @QueryParam("username") String username,
                                        @QueryParam("address") String address) {
        if (username == null || address == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username and address are required").build();
        }
        boolean success = deliveryService.processDelivery(id, username, address);
        return success ? Response.ok("Book delivery requested").build()
                : Response.status(Response.Status.BAD_REQUEST).entity("Book not available for delivery").build();
    }

    @Inject
    OpenAIService openAIService;
    @Inject
    BookRepository bookRepository;

    @POST
    @Path("/{id}/ask")
    public Response askBook(@PathParam("id") int id, String userQuestion) {
        Optional<Book> bookOpt = bookRepository.getBook(id);
        if (bookOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("\"{\\\"error\\\": \\\"Book not found\\\"}\"").build();
        }
        Book book = bookOpt.get();
        String prompt = "Summarize the book '" + book.getTitle() + "' by " + book.getAuthor() +
                ", published in " + book.getYear() + ". Genre: " + book.getGenre() +
                ". User asked: " + userQuestion;
        String openAIResponse = openAIService.chatWithOpenAI(prompt);
        return Response.ok(openAIResponse).build();
    }
    @Inject
    OpenAIBookData openAIBookData;
    @Inject
    LibraryManager libraryManager;

    @POST
    @Path("/search")
    public Response searchBook(JsonObject request) {
        String bookTitle = request.getString("title");
        String aiResponse = openAIBookData.searchBook(bookTitle);
        return Response.ok(aiResponse).build();
    }

    @GET
    @Path("/library")
    public Response getAllBooksFromLibrary() {
        List<Book> books = libraryManager.getBooks();
        return Response.ok(books).build();
    }
}