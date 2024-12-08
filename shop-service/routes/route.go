package routes

import (
	"github.com/gofiber/fiber/v2"
	"gorm.io/gorm"
	"shop-service/handler"
	"shop-service/repository"
)

type Route struct {
	r  fiber.Router
	db *gorm.DB
}

func (m *Route) ShopModule() handler.IShopHandler {
	shopRespository := repository.ShopRepositoryInit(m.db)
	shophandler := handler.ShopHandlerInit(shopRespository)
	return shophandler
}

func (m *Route) Setup(app *fiber.App) {

	//product
	app.Post("/api/save", m.ShopModule().Save)
	app.Get("/api/product", m.ShopModule().Get)
	app.Get("/api/product/:id", m.ShopModule().GetById)
	app.Get("/api/grpc", m.ShopModule().SaveGrpcProduct)
	app.Get("/api/stream/grpc", m.ShopModule().SaveGrpcProductsMaxBalance)
	app.Get("/api/all/grpc", m.ShopModule().SaveProductsDataAll)
	app.Get("/api/back/grpc", m.ShopModule().SaveProductDataAllBack)
}
