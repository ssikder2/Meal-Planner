package mealplanner;

import java.sql.*;
import java.util.Scanner;

public class Meal {
  private String mealCategory;
  private String mealName;
  private String[] ingredients;

  public Meal(String mealCategory, String mealName, String[] ingredients) {
    this.mealCategory = mealCategory;
    this.mealName = mealName;
    this.ingredients = ingredients;
  }

  public static void addMeal(Scanner scanner) throws SQLException {

    String meal;
    String mealName;
    String ingredientsLine;
    String[] ingredients;

    // Get Meal Of Day
    while (true) {
      System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
      meal = scanner.nextLine();
      if (!meal.equals("breakfast") && !meal.equals("lunch") && !meal.equals("dinner")) {
        System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
        continue;
      }
      break;
    }

    // Get Meal Name
    while (true) {
      System.out.println("Input the meal's name:");
      mealName = scanner.nextLine();

      if (mealName.matches("^[a-zA-Z]+( [a-zA-Z]+)*$")) {
        break;
      } else {
        System.out.println("Wrong format. Use letters only!");
      }
    }

    // Get Ingredients
    while (true) {
      System.out.println("Input the ingredients:");
      ingredientsLine = scanner.nextLine();

      if (ingredientsLine.matches("^[a-zA-Z ]+(, ?[a-zA-Z ]+)*$")) {
        ingredients = ingredientsLine.split(",\\s*");
        break;
      } else {
        System.out.println("Wrong format. Use letters only!");
      }
    }

    try {
      String insertMealSQL = "INSERT INTO meals (category, meal) VALUES (?, ?) RETURNING meal_id";
      PreparedStatement pstmt = DatabaseInitializer.connection.prepareStatement(insertMealSQL);
      pstmt.setString(1, meal);
      pstmt.setString(2, mealName);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int mealId = rs.getInt("meal_id");
        String insertIngredientSQL = "INSERT INTO ingredients (meal_id, ingredient) VALUES (?, ?)";
        PreparedStatement pstmt2 = DatabaseInitializer.connection.prepareStatement(insertIngredientSQL);
        for (String ingredient : ingredients) {
          pstmt2.setInt(1, mealId);
          pstmt2.setString(2, ingredient);
          pstmt2.executeUpdate();
        }
      }

      System.out.println("The meal has been added!");

    } catch (SQLException e) {
      System.out.println("Error adding meal: " + e.getMessage());
    }
  }

  public static void showMeals(Scanner scanner) throws SQLException {
    String filterCategory;
    while (true) {
      System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
      filterCategory = scanner.nextLine();

      if (!filterCategory.equals("breakfast") && !filterCategory.equals("lunch") && !filterCategory.equals("dinner")) {
        System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
        continue;
      }
      break;
    }

    String selectMealSQL = "SELECT m.category, m.meal, array_to_string(array_agg(i.ingredient), ', ') AS ingredients " +
            "FROM meals m " +
            "LEFT JOIN ingredients i ON m.meal_id = i.meal_id " +
            "WHERE m.category = ? " +  // Use WHERE instead of HAVING
            "GROUP BY m.category, m.meal, m.meal_id " +
            "ORDER BY m.meal_id";

    try {
      PreparedStatement pstmt = DatabaseInitializer.connection.prepareStatement(selectMealSQL);
      pstmt.setString(1, filterCategory);

      ResultSet rs = pstmt.executeQuery();

      boolean hasRows = false;

      while (rs.next()) {
        if (!hasRows) {
          System.out.println("Category: " + filterCategory);
          hasRows = true;
        }
        String mealName = rs.getString("meal");
        String[] ingredients = rs.getString("ingredients").split(",\\s*");

        System.out.println("Name: " + mealName);
        System.out.println("Ingredients:");
        for (String ingredient : ingredients) {
          if (!ingredient.trim().isEmpty()) {
            System.out.println(ingredient.trim());
          }
        }
        System.out.println();
      }

      if (!hasRows) {
        System.out.println("No meals found.");
      }

    } catch (SQLException e) {
      System.out.println("Error fetching meals: " + e.getMessage());
    }
  }

  public String toString() {
    return "Category: " + mealCategory + "\n" +
            "Name: " + mealName + "\n" +
            "Ingredients:" + "\n" +
            String.join("\n", ingredients);
  }
}
