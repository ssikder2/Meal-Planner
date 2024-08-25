package mealplanner;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main{

  public static void main(String[] args) throws SQLException, IOException {

    DatabaseInitializer.initializeDatabase();
    Scanner scanner = new Scanner(System.in);

      while(true){
        System.out.println("What would you like to do (add, show, plan, save, exit)?");
        String input = scanner.nextLine();


        if(input.equals("add")){
            Meal.addMeal(scanner);
        }
        else if(input.equals("show")){
            Meal.showMeals(scanner);
        }
        else if(input.equals("plan")){
            Plan.planMeals(scanner);
        }
        else if(input.equals("save")){
            Plan.saveShoppingList(scanner);
        }
        else if(input.equals("exit")){
            System.out.println("Bye!");
            break;
        }
      }

    DatabaseInitializer.connection.close();
  }
}

