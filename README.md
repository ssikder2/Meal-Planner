# Meal Planner Project

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [Contact](#contact)

## Introduction

The Meal Planner project is a console-based application developed to assist users in planning their meals for the week. Users can input meals, organize them into breakfast, lunch, and dinner, and generate a shopping list based on the ingredients required for their planned meals.

## Features

- **Add Meals**: Users can add meals with names, categories (breakfast, lunch, dinner), and ingredients.
- **View Meals**: List all meals stored in the database.
- **Plan Meals**: Organize meals into a weekly plan with slots for breakfast, lunch, and dinner.
- **Generate Shopping List**: Automatically create a shopping list based on the ingredients of the planned meals.
- **Database Integration**: Persist meal data and plans using PostgreSQL.

## Technologies Used

- **Java**: Primary programming language.
- **PostgreSQL**: Database for storing meals and meal plans.
- **JDBC**: For database connectivity.
- **Maven**: Build automation tool for managing dependencies.

## Setup and Installation

### Prerequisites

- **Java JDK 8+**
- **PostgreSQL**
- **Maven**

### Steps

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/meal-planner.git
    cd meal-planner
    ```

2. **Configure the database**:
   - Create a PostgreSQL database named `mealplanner`.
   - Update the `Database.java` file with your PostgreSQL credentials.

3. **Build the project**:
    ```bash
    mvn clean install
    ```

4. **Run the application**:
    ```bash
    java -jar target/mealplanner-1.0.jar
    ```

## Usage

- **Add a Meal**: Follow the prompts in the console to add a new meal.
- **View Meals**: Choose the option to view all meals.
- **Plan Meals**: Select meals for each day of the week.
- **Generate Shopping List**: Generate a list of all ingredients required for the week’s meal plan.

## Database Schema

The PostgreSQL database consists of the following tables:

### Meals Table

| Column     | Type     | Description                            |
|------------|----------|----------------------------------------|
| id         | SERIAL   | Primary key, auto-incremented ID       |
| name       | TEXT     | Name of the meal                       |
| category   | TEXT     | Meal category (e.g., breakfast, lunch) |
| ingredients| TEXT[]   | List of ingredients                    |

### Plan Table

| Column    | Type   | Description                            |
|-----------|--------|----------------------------------------|
| day       | TEXT   | Day of the week                        |
| breakfast | TEXT   | Meal planned for breakfast             |
| lunch     | TEXT   | Meal planned for lunch                 |
| dinner    | TEXT   | Meal planned for dinner                |

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an issue for any suggestions or improvements.


## Contact

For any questions or inquiries, please contact:
- **Name**: Shams Sikder
- **LinkedIn**: [Shams Sikder](https://www.linkedin.com/in/shams-sikder)
