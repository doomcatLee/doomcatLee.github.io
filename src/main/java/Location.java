import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DateFormat;
import java.util.Date;

public class Location{
  private int id;
  private String name;

  public Location(String name){
    this.name = name;
    this.id = id;
  }

  public int getId(){
    return id;
  }

  public String getName(){
    return name;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO locations (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

}
