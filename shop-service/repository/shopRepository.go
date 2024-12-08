package repository

import (
	"gorm.io/gorm"
	"log"
	"shop-service/database"
	"shop-service/models"
)

type IShopRepository interface {
	GetAllProduct() []models.Product
	CreateUser(product *models.Product) error
	GetById(id int) models.Product
}

type shopRepository struct {
	db *gorm.DB
}

func ShopRepositoryInit(db *gorm.DB) IShopRepository {
	return &shopRepository{db}
}

func (s shopRepository) GetAllProduct() []models.Product {
	var products []models.Product
	database.DB.Model(models.Product{}).Find(&products)
	return products
}

func (s shopRepository) CreateUser(product *models.Product) error {
	err := database.DB.Model(models.Product{}).Create(product).Error
	if err != nil {
		return err
	}
	return nil
}

func (s shopRepository) GetById(id int) models.Product {
	var product models.Product
	log.Println(id)
	database.DB.Model(models.Product{}).Where("id=?", id).First(&product)
	return product
}
