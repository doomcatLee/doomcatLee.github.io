import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", NotEndangeredAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("sightings", Sighting.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/endangered_sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      String rangerContact = request.queryParams("contactNumber");
      int rangerBadge = Integer.parseInt(request.queryParams("badgeNumber"));

      Ranger ranger = new Ranger(rangerName, rangerContact, rangerBadge);
      ranger.save();
      int rangerId = ranger.getId();

      int animalIdSelected = Integer.parseInt(request.queryParams("endangeredAnimalSelected"));
      String latLong = request.queryParams("latLong");
      Sighting sighting = new Sighting(animalIdSelected, rangerId, latLong, rangerName);
      sighting.save();
      model.put("sighting", sighting);
      model.put("animals", EndangeredAnimal.all());
      String animal = EndangeredAnimal.find(animalIdSelected).getName();
      model.put("animal", animal);
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      String rangerContact = request.queryParams("contactNumber");
      int rangerBadge = Integer.parseInt(request.queryParams("badgeNumber"));

      Ranger ranger = new Ranger(rangerName, rangerContact, rangerBadge);
      ranger.save();
      
      int rangerId = ranger.getId();
      int animalIdSelected = Integer.parseInt(request.queryParams("animalSelected"));
      String latLong = request.queryParams("latLong");
      Sighting sighting = new Sighting(animalIdSelected, rangerId, latLong, rangerName);
      sighting.save();
      model.put("sighting", sighting);
      model.put("animals", NotEndangeredAnimal.all());
      String animal = NotEndangeredAnimal.find(animalIdSelected).getName();
      model.put("animal", animal);
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", NotEndangeredAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/animal-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      boolean endangered = request.queryParamsValues("endangered")!=null;
      if (endangered) {
        String name = request.queryParams("name");
        String health = request.queryParams("health");
        String age = request.queryParams("age");
        EndangeredAnimal endangeredAnimal = new EndangeredAnimal(name, health, age);
        endangeredAnimal.save();
        model.put("animals", NotEndangeredAnimal.all());
        model.put("endangeredAnimals", EndangeredAnimal.all());
      } else {
        String name = request.queryParams("name");
        NotEndangeredAnimal animal = new NotEndangeredAnimal(name, "ill", "helc");
        animal.save();
        model.put("animals", NotEndangeredAnimal.all());
        model.put("endangeredAnimals", EndangeredAnimal.all());
      }
      response.redirect("/");
        return null;
      });

    get("/animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      NotEndangeredAnimal animal = NotEndangeredAnimal.find(Integer.parseInt(request.params("id")));
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/endangered_animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(Integer.parseInt(request.params("id")));
      model.put("endangeredAnimal", endangeredAnimal);
      model.put("template", "templates/endangered_animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/error.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
