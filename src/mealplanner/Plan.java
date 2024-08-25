package mealplanner;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Plan {

    public static final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    public static final String[] mealCategories = {"breakfast", "lunch", "dinner"};

    public static void planMeals(Scanner scanner) throws SQLException {

        for (String day : daysOfWeek) {
            System.out.println(day);

            int breakfast_id = chooseMeal(day, "breakfast", scanner);
            int lunch_id = chooseMeal(day, "lunch", scanner);
            int dinner_id = chooseMeal(day, "dinner", scanner);

            Integer[] meal_ids = {breakfast_id, lunch_id, dinner_id};

            addPlanRow(day, meal_ids);
        }
        displayPlan();
    }

    public static Integer chooseMeal(String day, String category, Scanner scanner) throws SQLException {
        String selectMealSQL = "SELECT m.meal_id, m.meal " +
                "FROM meals m " +
                "WHERE m.category = ? " +
                "ORDER BY m.meal_id";

        PreparedStatement pstmt = DatabaseInitializer.connection.prepareStatement(selectMealSQL);
        pstmt.setString(1, category);

        ResultSet rs = pstmt.executeQuery();

        TreeMap<Integer, String> meals = new TreeMap<>();

        while (rs.next()) {
            meals.put(rs.getInt("meal_id"), rs.getString("meal"));
        }

        meals.values().forEach(System.out::println);

        String chosenMeal;
        int chosenMealID = 0;

        System.out.println("Choose the " + category + " for " + day + " from the list above:");

        while (true){
            chosenMeal = scanner.nextLine();
            if(!meals.values().contains(chosenMeal)){
                System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                continue;
            }
            break;
        }

        for (Map.Entry<Integer, String> entry : meals.entrySet()) {
            if (entry.getValue().equals(chosenMeal)) {
                chosenMealID = entry.getKey();
                break;
            }
        }

        return chosenMealID;
    }

    public static void addPlanRow(String day, Integer[] meal_ids) throws SQLException {

        String insertPlanSQL = "INSERT INTO plan (day_name, breakfast_meal_id, lunch_meal_id, dinner_meal_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt2 = DatabaseInitializer.connection.prepareStatement(insertPlanSQL);
        pstmt2.setString(1, day);
        pstmt2.setInt(2, meal_ids[0]);
        pstmt2.setInt(3, meal_ids[1]);
        pstmt2.setInt(4, meal_ids[2]);
        pstmt2.executeUpdate();

        System.out.println("Yeah! We planned the meals for " + day + ".");
        System.out.println();
    }

    public static void displayPlan() throws SQLException {
        String selectPlanSQL =
                "SELECT d.day_name, " +
                        "bm.meal AS breakfast, " +
                        "lm.meal AS lunch, " +
                        "dm.meal AS dinner " +
                        "FROM plan p " +
                        "JOIN days d ON p.day_name = d.day_id " +
                        "JOIN meals bm ON p.breakfast_meal_id = bm.meal_id " +
                        "JOIN meals lm ON p.lunch_meal_id = lm.meal_id " +
                        "JOIN meals dm ON p.dinner_meal_id = dm.meal_id";

        PreparedStatement pstmt = DatabaseInitializer.connection.prepareStatement(selectPlanSQL);

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            System.out.println("Day: " + rs.getString("day_name"));
            System.out.println("Breakfast: " + rs.getString("breakfast"));
            System.out.println("Lunch: " + rs.getString("lunch"));
            System.out.println("Dinner: " + rs.getString("dinner"));
            System.out.println();
        }

    }

    public static void saveShoppingList(Scanner scanner) throws SQLException, IOException {

        ArrayList<Integer> mealIds = new ArrayList<>();
        ArrayList<String> ingredientList = new ArrayList<>();
        ArrayList<String> formattedIngredientsList = new ArrayList<>();

        String selectPlanSQL = "SELECT breakfast_meal_id, lunch_meal_id, dinner_meal_id " +
                "FROM plan";

        PreparedStatement pstmt = DatabaseInitializer.connection.prepareStatement(selectPlanSQL);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            mealIds.add(rs.getInt("breakfast_meal_id"));
            mealIds.add(rs.getInt("lunch_meal_id"));
            mealIds.add(rs.getInt("dinner_meal_id"));
        }

        if (!mealIds.isEmpty()){
            String mealPlaceholders = String.join(",", Collections.nCopies(mealIds.size(), "?"));

            String selectIngredientsSQL = "SELECT i.ingredient " +
                    "FROM ingredients i " +
                    "JOIN meals m ON i.meal_id = m.meal_id " +
                    "WHERE m.meal_id IN (" + mealPlaceholders + ")";

            pstmt = DatabaseInitializer.connection.prepareStatement(selectIngredientsSQL);

            for (int i = 0; i < mealIds.size(); i++) {
                pstmt.setInt(i + 1, mealIds.get(i));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                ingredientList.add(rs.getString("ingredient"));
            }

            Map<String, Integer> ingredientCount = new HashMap<>();
            for (String ingredient: ingredientList) {
                ingredientCount.put(ingredient, ingredientCount.getOrDefault(ingredient, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : ingredientCount.entrySet()) {
                String formattedIngredient = entry.getKey();
                int count = entry.getValue();
                if (count > 1) {
                    formattedIngredient += " x" + count;
                }
                formattedIngredientsList.add(formattedIngredient);
            }

            System.out.println("Input a filename:");
            String fileName = scanner.nextLine();

            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);

            for (String ingredient : formattedIngredientsList) {
                writer.write(ingredient + System.lineSeparator());
            }

            writer.close();
            System.out.println("Saved!");
        } else {
            System.out.println("Unable to save. Plan your meals first.");
        }
    }
}
