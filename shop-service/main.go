package main

import (
	"github.com/gofiber/fiber/v2"
	"github.com/joho/godotenv"
	"log"
	"os"
	"shop-service/database"
	"shop-service/routes"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env files")
	}
	database.Connect()

	port := os.Getenv("PORT")
	app := fiber.New()
	var route routes.Route
	route.Setup(app)
	app.Listen(":" + port)

}
